package org.mtec.sistemacarga.framework.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.mtec.sistemacarga.framework.annotations.Cursor;
import org.mtec.sistemacarga.framework.annotations.DBLoadScript;
import org.mtec.sistemacarga.framework.annotations.Property;
import org.mtec.sistemacarga.framework.gui.builder.DataBinder;
import org.mtec.sistemacarga.framework.reload.ProcessToReLoad;
import org.mtec.sistemacarga.framework.util.DateFormat;
import org.mtec.sistemacarga.framework.util.Log;

public class ExecutaProcessoCargaBatchService<T> {
	
	private T loadProcess;
	
	@SuppressWarnings("unchecked")
	public ExecutaProcessoCargaBatchService(Class<?> process) {
		super();
		try {
			this.loadProcess = (T)Class.forName(process.getName()).newInstance();
		} catch (Exception e) {
			Log.error("Erro ao tentar criar uma nova instancia do objeto para o componente: " + process.getName());
			Log.error(e);
		}
	} 
	
	public void executeProcess(Map<String, String> mapValores) {
		final Field[] fields = loadProcess.getClass().getDeclaredFields();
		Log.info("Lista de Atributos do processo de Carga:");
		for (Field field : fields) {
			field.setAccessible(true);
			boolean prop = field.isAnnotationPresent(Property.class);
			if(prop) {
				try {
					DataBinder.bind(
							loadProcess, 
							field.getName(), 
							mapValores.get(((Property)field.getDeclaredAnnotations()[0]).value())
						);

					Log.info(((Property)field.getDeclaredAnnotations()[0]).value() + " = " + field.get(loadProcess));
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
			}
		}
		executeProcess(fields);
	}

	private void executeProcess(Field[] fields) {
		try {
			Date hrIniCarga = startProcess(fields);
			
			Runnable r = new Runnable() {

				@SuppressWarnings("unchecked")
				public void run() {
					Method[] methods = loadProcess.getClass().getMethods();
					for (Method method : methods) {
						if(method.isAnnotationPresent(DBLoadScript.class)) {
							try {
								method.invoke(loadProcess, new Object[0]);
							} catch (Exception e) {
								Log.error("Erro ao tentar executar o processo de carga");
								Log.error(e);
							}
						}
					}
				}
				
			};
			
			final Thread t = new Thread(r);
			t.setName("SistemaCarga");
			t.start();
			
			processMonitor(fields, hrIniCarga, t);
		} catch (Exception e1) {
			Log.error("Erro ao tentar iniciar a thread de carga.");
			Log.error(e1);
		}
	}
	
	private Date startProcess(final Field[] fields) {
		ProcessToReLoad.load();
		final Date hrIniCarga = new Date();
		Log.info("Inicio do processo de carga: " + DateFormat.getInstance().getDataHoraAtual());
		//
		return hrIniCarga;
	}
	
	private void processMonitor(final Field[] fields, final Date hrIniCarga, final Thread t) {
		Thread monitor = new Thread() {
			@SuppressWarnings({ "static-access", "unchecked" })
			public void run() {
				double percent = 0.0d;
				List<? extends Object> listFieldRowSet;
				CachedRowSet cachedFieldRowSet;
				while(t.isAlive()) {
					for (Field field : fields) {
						field.setAccessible(true);
						Cursor cr = field.getAnnotation(Cursor.class);
						if (cr != null) {
							try {
								if (field.getType() == CachedRowSet.class) {
									cachedFieldRowSet = (CachedRowSet) field.get(loadProcess);
									if (cachedFieldRowSet != null) {
										if (cachedFieldRowSet.size() > 0 && !cachedFieldRowSet.isAfterLast()) {
											percent = ((cachedFieldRowSet.getRow()+0.0) / (cachedFieldRowSet.size()+0.0)) * 100.0;
										}
									}
								} else if (field.getType() == List.class) {
									try {
										if (field != null) {
											listFieldRowSet = (List) field.get(loadProcess);
											if (listFieldRowSet != null) {
												if (listFieldRowSet.size() > 0) {
													int fieldValue = Integer.parseInt(listFieldRowSet.get(listFieldRowSet.size()-1).toString());
													if (listFieldRowSet.size()-1 > 0 && fieldValue < listFieldRowSet.size()) {
														percent = ( (fieldValue + 0.0) / (listFieldRowSet.size()-1+0.0)) * 100.0;
													}
												}
											}
										}
									} catch (Exception e) {
									}
								}
							} catch (Exception e) {
								Log.error("Erro ao tentar realizar a conversão do objeto field para um objeto JProgressBar.");
								Log.error(e);
							}
						}
					}
					try {
						Thread.currentThread().sleep(1);
					} catch (InterruptedException e) {
						Log.error("Erro ao tentar executar o comando sleep para a thread " + this.getName());
						Log.error(e);
					}
					ProcessToReLoad.persist();
				}
				//
				finishProcess(loadProcess, hrIniCarga, fields);
			}
		};
		if (!t.isAlive()) {
			while(t.isAlive()) {
				for (Field field : fields) {
					field.setAccessible(true);
				}
			}
		}
		monitor.start();
	}
	
	private void finishProcess(final T loadProcess, final Date hrIniCarga, final Field[] fields) {
		Date hrFimCarga = new Date();
		long duracao = hrFimCarga.getTime() - hrIniCarga.getTime();
		Log.info("Fim do processo de carga: " + DateFormat.getInstance().getDataHoraAtual());
		Log.info("Duração do processo de carga: " + DateFormat.getInstance().getTempoGasto(duracao));
		//
		ProcessToReLoad.persist();
		//
		String message = "Processo de carga concluído.";
		if (ProcessToReLoad.size() > 0) {
			message += "\n" + ProcessToReLoad.size() + " registros devem ser reprocessados.";
		}
		//
		Log.info(message);
	}
}
