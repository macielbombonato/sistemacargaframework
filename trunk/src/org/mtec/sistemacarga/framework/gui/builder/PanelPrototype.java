package org.mtec.sistemacarga.framework.gui.builder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import org.mtec.sistemacarga.framework.annotations.Cursor;
import org.mtec.sistemacarga.framework.annotations.DBLoadScript;
import org.mtec.sistemacarga.framework.annotations.Property;
import org.mtec.sistemacarga.framework.reload.ProcessToReLoad;
import org.mtec.sistemacarga.framework.util.DateFormat;
import org.mtec.sistemacarga.framework.util.Log;
import org.mtec.sistemacarga.framework.util.NumberFormat;
import org.mtec.sistemacarga.framework.util.ResourceLocation;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Classe de protótipo para os panels da tela.
 * Esta classe é utilizada para implementar funcionalidades genéricas que possam ser utilizadas
 * por todos componentes de carga.
 * 
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 *
 * @param <T> DBLoadEngine
 * 
 * @version 1.0 - 25/04/2007
 * @version 1.1 - 16/10/2007 - Inclusão do método setEnableComponentsFromTopPanel para realizar tratamento de 
 *                             ativação e destativação dos componentes do formPrincipal da aplicação.
 * @version 1.2 - 03/12/2007 - Inclusão de tratamento que possibilita ao sistema trabalhar com hibernate.
 */
@SuppressWarnings("serial")
class PanelPrototype<T> extends JPanel {

	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
	private JPanel controlPanel = null;
	private JButton runButton = null;
	private JLabel startProcessTitleLabel = null;
	private JLabel startProcessLabel = null;
	private JLabel endProcessTitleLabel = null;
	private JLabel endProcessLabel = null;
	private JLabel elapsedTimeTitleLabel = null;
	private JLabel elapsedTimeLabel = null;
	private JPanel jPanel2 = null;
	private JPanel jPanel3 = null;
	private JPanel form = null;
	private JPanel reportPanel = null;
	private JLabel processingIconLabel = null;
	
	private T loadProcess;  //  @jve:decl-index=0:
	
	private List parameters;  //  @jve:decl-index=0:
	
	protected List getParameters() {
		return parameters;
	}
	
	protected void setParameters(List parameters) {
		this.parameters = parameters;
	}
	
	@SuppressWarnings("unchecked")
	public PanelPrototype(JPanel form, JPanel reportPane, Class process) {
		super();
		try {
			this.loadProcess = (T)Class.forName(process.getName()).newInstance();;
		} catch (Exception e) {
			Log.error("Erro ao tentar criar uma nova instancia do objeto para o componente: " + process.getName());
			Log.error(e);
		}
		this.setForm(form);
		// Adiciona esquema de formatação aos campos de entrada de dados.
		addFormatModel(form);
		
		this.setReportPanel(reportPane);
		initialize();
	}

	private void addFormatModel(JPanel form) {
		// Adiona formatação aos campos de entrada de dados.
		Component[] components = form.getComponents();
		for (Component guiComponent : components) {
			final Component component = guiComponent;
			if (component.getClass() == JFormattedTextField.class) {
				component.addKeyListener(new java.awt.event.KeyAdapter() {
					
					private final String NUMERIC_CHARS = "9876543210";
					
					public void keyReleased(KeyEvent e) {
						try {
							JFormattedTextField aux = (JFormattedTextField)component;
							int pos = aux.getText().length()-1;
							if (pos >= 0) {
								// formatação para tipos numericos
								if(aux.getValue() == long.class || 
										aux.getValue() == double.class || 
										aux.getValue() == float.class || 
										aux.getValue() == int.class) {
									if (aux.getValue() == double.class || 
											aux.getValue() == float.class) {
										if (".,".indexOf(aux.getText().charAt(pos)) >= 0) {
										} else {
											aux.setText(aux.getText(0, pos));
										}
									} else {
										if (NUMERIC_CHARS.indexOf(aux.getText().charAt(pos)) >= 0) {
										} else {
											aux.setText(aux.getText(0, pos));
										}
									}
								// formatação para datas
								} else if (aux.getValue() == Date.class) {
									// teste de posições númericas
									if (pos == 0 || 
											pos == 1 || 
											pos == 3 ||
											pos == 4 ||
											pos == 6 ||
											pos == 7 ||
											pos == 8 ||
											pos == 9) {
										if (NUMERIC_CHARS.indexOf(aux.getText().charAt(pos)) >= 0) {
										} else {
											aux.setText(aux.getText(0, pos));
										}
										// teste para dias válidos
										if (pos == 0) {
											int dia = Integer.parseInt(aux.getText().charAt(0)+"");
											if (dia > 3) {
												aux.setText(null);
											}
										} else if (pos == 1) {
											int dia = Integer.parseInt((aux.getText().charAt(0)+"") + (aux.getText().charAt(1)+ ""));
											if (dia > 31 || dia == 0) {
												aux.setText(aux.getText(0, 1));
											}
										}
										// teste para meses válidos
										if (pos == 3) {
											int mes = Integer.parseInt(aux.getText().charAt(3)+"");
											if (mes > 1) {
												aux.setText(aux.getText(0, 3));
											}
										} else if (pos == 4) {
											int mes = Integer.parseInt((aux.getText().charAt(3)+"") + (aux.getText().charAt(4)+ ""));
											if (mes > 12 || mes == 0) {
												aux.setText(aux.getText(0, 4));
											}
										}
										// teste para inclusão das barras
										if (pos == 1 ||
												 pos == 4) {
											if (e.getKeyCode() != 8) {
												aux.setText(aux.getText()+"/");
											} else {
												aux.setText(aux.getText(0, pos));
											}
										}
									} else if (pos == 2 ||
											pos == 5 ) {
										if (aux.getText().charAt(pos) == '/') {
										} else {
											aux.setText(aux.getText(0, pos));
										}
									} else {
										aux.setText(aux.getText(0, pos));
									}
								}
							}
						} catch (Exception e1) {
						}
					}
				});
				
				component.addFocusListener(new java.awt.event.FocusAdapter() {
					
					private final String NUMERIC_CHARS = "9876543210";
					
					public void focusLost(java.awt.event.FocusEvent e) {
						try {
							JFormattedTextField aux = (JFormattedTextField)component;
							for (int i = 0; i < aux.getText().length(); i++) {
								if(aux.getValue() == long.class || 
										aux.getValue() == double.class || 
										aux.getValue() == float.class || 
										aux.getValue() == int.class) {
									if (aux.getValue() == double.class || 
											aux.getValue() == float.class) {
										if (".,".indexOf(aux.getText().charAt(i)) != ' ' || 
												NUMERIC_CHARS.indexOf(aux.getText().charAt(i)) >= 0) {
										} else {
											aux.setText(aux.getText().replace(aux.getText().charAt(i), ' '));
											aux.setText(aux.getText().trim());
										}
									} else {
										if (NUMERIC_CHARS.indexOf(aux.getText().charAt(i)) >= 0) {
										} else {
											aux.setText(aux.getText().replace(aux.getText().charAt(i), ' '));
											aux.setText(aux.getText().trim());
										}
									}
								// formatação para datas
								} else if (aux.getValue() == Date.class) {
									// teste de posições númericas
									if (i == 0 || 
											i == 1 || 
											i == 3 ||
											i == 4 ||
											i == 6 ||
											i == 7 ||
											i == 8 ||
											i == 9) {
										if (NUMERIC_CHARS.indexOf(aux.getText().charAt(i)) >= 0) {
										} else {
											JOptionPane.showMessageDialog(null, "Formato de data inválido, digite a data no formato DD/MM/YYYY.");
											aux.setText(null);
										}
										// teste para dias válidos
										if (i == 1) {
											int dia = Integer.parseInt((aux.getText().charAt(0)+"") + (aux.getText().charAt(1)+ ""));
											if (dia > 31 || dia == 0) {
												JOptionPane.showMessageDialog(null, "Dia da Data inválido, digite uma data válida.");
												aux.setText(null);
											}
										}
										// teste para meses válidos
										if (i == 4) {
											int mes = Integer.parseInt((aux.getText().charAt(3)+"") + (aux.getText().charAt(4)+""));
											if (mes > 12 || mes == 0) {
												JOptionPane.showMessageDialog(null, "Mês da Data inválido, digite uma data válida.");
												aux.setText(null);
											}
										}
									} else if (i == 2 ||
											i == 5 ) {
										if (aux.getText().charAt(i) == '/') {
										} else {
											JOptionPane.showMessageDialog(null, "A data informada não possui as barras ou estão em posições inválidas, digite uma data válida.");
											aux.setText(null);
										}
									} else {
										aux.setText(null);
									}
								}
							} // fim do for
						} catch (Exception e1) {
						}
					}
				});
			}
		}
	}
	
	private void initialize() {
		this.setSize(1051, 483);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(getJPanel(), null);
		this.add(getJPanel1(), null);
	}

	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.setBackground(new Color(212, 208, 200));
			jPanel.add(getJPanel2(), BorderLayout.CENTER);
		}
		return jPanel;
	}

	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BorderLayout());
			jPanel1.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return jPanel1;
	}

	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getReportPanel());
		}
		return jScrollPane;
	}

	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setPreferredSize(new Dimension(21, 21));
			jScrollPane1.setViewportView(getForm());
		}
		return jScrollPane1;
	}
	
	private JPanel getForm() {
		return form;
	}

	void setForm(JPanel form) {
		this.form = form;
	}
	
	private JPanel getReportPanel() {
		return reportPanel;
	}
	
	void setReportPanel(JPanel p) {
		this.reportPanel = p;
		reportPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	}
	
	private JPanel getControlPanel() {
		if (controlPanel == null) {
			elapsedTimeLabel = new JLabel();
			elapsedTimeLabel.setText("");
			elapsedTimeTitleLabel = new JLabel();
			elapsedTimeTitleLabel.setText("Tempo Total:");
			elapsedTimeTitleLabel.setVisible(false);
			endProcessLabel = new JLabel();
			endProcessLabel.setText("");
			endProcessTitleLabel = new JLabel();
			endProcessTitleLabel.setText("Fim do Processo:");
			endProcessTitleLabel.setVisible(false);
			startProcessLabel = new JLabel();
			startProcessLabel.setText("");
			startProcessTitleLabel = new JLabel();
			startProcessTitleLabel.setText("Início do Processo:");
			//
			startProcessTitleLabel.setVisible(false);
			FormLayout layout = new FormLayout("pref, 4dlu, 80dlu", // columns
					                           "pref, 2dlu, pref, 2dlu, pref, 2dlu, pref"); // rows
			layout.setRowGroups(new int[][] { { 1, 3, 5 , 7} });
			
			//
			controlPanel = new JPanel(layout);
			controlPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			CellConstraints cc = new CellConstraints();
			
			processingIconLabel = new JLabel();
			processingIconLabel.setIcon(new ImageIcon(getClass().getResource(ResourceLocation.IMG_CIRCLE_LOAD)));
			processingIconLabel.setVisible(false);
			
			controlPanel.add(getRunButton(), cc.xy(1, 1));
			controlPanel.add(processingIconLabel, cc.xy(3, 1));
			
			controlPanel.add(startProcessTitleLabel, cc.xy(1, 3));
			controlPanel.add(startProcessLabel, cc.xy(3, 3));
			
			controlPanel.add(endProcessTitleLabel, cc.xy(1, 5));
			controlPanel.add(endProcessLabel, cc.xy(3, 5));
			
			controlPanel.add(elapsedTimeTitleLabel, cc.xy(1, 7));
			controlPanel.add(elapsedTimeLabel, cc.xy(3, 7));
		}
		return controlPanel;

	}

	private JButton getRunButton() {
		if (runButton == null) {
			runButton = new JButton();
			runButton.setText("Executar");
			runButton.setMnemonic('x');
			runButton.setIcon(new ImageIcon(getClass().getResource(ResourceLocation.IMG_BOOKMARK_NEXT)));
			runButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					executeProcess();
				}
			});
		}
		return runButton;
	}
	
	protected void executeProcess() {
		final Field[] fields = loadProcess.getClass().getDeclaredFields();
		Component[] formComponents = this.getForm().getComponents();
		final Component[] reportComponents = this.getReportPanel().getComponents();
		for (Field field : fields) {
			field.setAccessible(true);
			boolean prop = field.isAnnotationPresent(Property.class);
			if(prop) {
				String fieldName = field.getName();
				JFormattedTextField txtField = (JFormattedTextField)getComponentByName(fieldName, formComponents);
				String value = txtField.getText();
				DataBinder.bind(loadProcess, field.getName(), value);
				txtField.setEnabled(false);
			}
		}
		executeProcess(fields, reportComponents);
	}

	private void executeProcess(Field[] fields, Component[] reportComponents) {
		try {
			Date hrIniCarga = startProcess(fields, reportComponents);
			
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
			
			processMonitor(fields, reportComponents, hrIniCarga, t);
		} catch (Exception e1) {
			Log.error("Erro ao tentar iniciar a thread de carga.");
			Log.error(e1);
		}
	}

	private Date startProcess(final Field[] fields, final Component[] reportComponents) {
		this.getParent().setEnabled(false);
		JFrame frame = (JFrame) this.getRootPane().getParent();
		frame.setTitle(ResourceLocation.TITLE_APPLICATION_NAME + " - " + loadProcess.getClass().getSimpleName());
		Component[] components = this.getParent().getParent().getComponents();
		setEnableComponentsFromTopPanel(components, false);
		ProcessToReLoad.load();
		startProcessTitleLabel.setVisible(true);
		final Date hrIniCarga = new Date();
		Log.info("Inicio do processo de carga: " + DateFormat.getInstance().getDataHoraAtual());
		startProcessLabel.setText(DateFormat.getInstance().getDataHoraAtual());
		//
		for (Field field : fields) {
			field.setAccessible(true);
			boolean cr = field.isAnnotationPresent(Cursor.class);
			if (cr) {
				String fieldName = field.getName();
				try {
					if (field.getType() == CachedRowSet.class) {
						CachedRowSet fieldRowSet = (CachedRowSet) field.get(loadProcess);
						if (fieldRowSet != null) {
							JProgressBar progressBar = (JProgressBar)getComponentByName(fieldName, reportComponents);
							progressBar.setMaximum(0);
							progressBar.setValue(0);
							progressBar.setString("0%");
							JLabel label = (JLabel)getComponentByName("size"+fieldName, reportComponents);
							label.setText(0+"");
						}
					}
				} catch (Exception e) {
					Log.error("Erro ao tentar realizar a conversão do objeto field para um objeto CachedRowSet.");
					Log.error(e);
				}
			}
		}
		//
		endProcessTitleLabel.setVisible(false);
		endProcessLabel.setText("");
		elapsedTimeTitleLabel.setVisible(false);
		elapsedTimeLabel.setText("");
		//
		processingIconLabel.setVisible(true);
		//
		runButton.setEnabled(false);
		//
		return hrIniCarga;
	}

	private void processMonitor(final Field[] fields, final Component[] reportComponents, final Date hrIniCarga, final Thread t) {
		Thread monitor = new Thread() {
			@SuppressWarnings({ "static-access", "unchecked" })
			public void run() {
				double percent = 0.0d;
				while(t.isAlive()) {
					for (Field field : fields) {
						field.setAccessible(true);
						Cursor cr = field.getAnnotation(Cursor.class);
						if (cr != null) {
							String fieldName = field.getName();
							try {
								if (field.getType() == CachedRowSet.class) {
									CachedRowSet fieldRowSet = (CachedRowSet) field.get(loadProcess);
									if (fieldRowSet != null) {
										JProgressBar progressBar = (JProgressBar)getComponentByName(fieldName, reportComponents);
										if (fieldRowSet.size() > 0 && !fieldRowSet.isAfterLast()) {
											percent = ((fieldRowSet.getRow()+0.0) / (fieldRowSet.size()+0.0)) * 100.0;
											progressBar.setMaximum(fieldRowSet.size());
											progressBar.setValue(fieldRowSet.getRow());
											progressBar.setString(NumberFormat.getInstance().formatInteger(fieldRowSet.getRow()) + " / " + 
													NumberFormat.getInstance().formatDoubleToMoney(percent) + "%");
											JLabel label = (JLabel)getComponentByName("size"+fieldName, reportComponents);
											label.setText(NumberFormat.getInstance().formatInteger(fieldRowSet.size())+" - C");
										} else if (fieldRowSet.size() > 0 && fieldRowSet.isAfterLast()) {
											if (!progressBar.getString().equalsIgnoreCase("0%")) { 
												progressBar.setValue(fieldRowSet.size());
												percent = ((fieldRowSet.size()+0.0) / (fieldRowSet.size()+0.0)) * 100.0;
												progressBar.setString(NumberFormat.getInstance().formatInteger(fieldRowSet.size()) + " / " + 
														NumberFormat.getInstance().formatDoubleToMoney(percent) + "%");
											}
										}
									}
								} else if (field.getType() == ResultSet.class) {
									ResultSet fieldRowSet = (ResultSet) field.get(loadProcess);
									if (fieldRowSet != null) {
										JProgressBar progressBar = (JProgressBar)getComponentByName(fieldName, reportComponents);
										if (fieldRowSet.getRow() > 0 && !fieldRowSet.isAfterLast()) {
											progressBar.setMaximum(fieldRowSet.getRow());
											progressBar.setValue(fieldRowSet.getRow());
											progressBar.setString(NumberFormat.getInstance().formatInteger(fieldRowSet.getRow()));
											JLabel label = (JLabel)getComponentByName("size"+fieldName, reportComponents);
											label.setText("? - R");
										}
									}
								} else if (field.getType() == List.class) {
									try {
										if (field != null) {
											List<? extends Object> fieldRowSet = (List) field.get(loadProcess);
											if (fieldRowSet != null) {
												JProgressBar progressBar = (JProgressBar)getComponentByName(fieldName, reportComponents);
												if (fieldRowSet.size() > 0) {
													int fieldValue = Integer.parseInt(fieldRowSet.get(fieldRowSet.size()-1).toString());
													if (fieldRowSet.size()-1 > 0 && fieldValue < fieldRowSet.size()) {
														percent = ( (fieldValue + 0.0) / (fieldRowSet.size()-1+0.0)) * 100.0;
														progressBar.setMaximum(fieldRowSet.size()-1);
														progressBar.setValue(fieldValue);
														progressBar.setString(NumberFormat.getInstance().formatInteger(fieldValue) + " / " + 
																NumberFormat.getInstance().formatDoubleToMoney(percent) + "%");
														JLabel label = (JLabel)getComponentByName("size"+fieldName, reportComponents);
														label.setText(NumberFormat.getInstance().formatInteger(fieldRowSet.size()-1)+" - H");
													} else if (fieldRowSet.size()-1 > 0) {
														if (!progressBar.getString().equalsIgnoreCase("0%")) { 
															progressBar.setValue(fieldRowSet.size()-1);
															percent = ((fieldRowSet.size()-1+0.0) / (fieldRowSet.size()-1+0.0)) * 100.0;
															progressBar.setString(NumberFormat.getInstance().formatInteger(fieldValue) + " / " + 
																	NumberFormat.getInstance().formatDoubleToMoney(percent) + "%");
														}
													}
												} else {
													JLabel label = (JLabel)getComponentByName("size"+fieldName, reportComponents);
													label.setText(NumberFormat.getInstance().formatInteger(fieldRowSet.size())+" - H");
													progressBar.setMaximum(fieldRowSet.size() + 1);
													progressBar.setValue(fieldRowSet.size() + 1);
													progressBar.setString("Consulta não Retornou dados.");
												}
											}
										}
									} catch (Exception e) {
										try {
											JProgressBar progressBar = (JProgressBar)getComponentByName(fieldName, reportComponents);
											progressBar.setValue(progressBar.getValue()+1);
											progressBar.setString("Hibernate: Ausência de indice. Carregando...");
										} catch (Exception ex) {
										}
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
				finishProcess(hrIniCarga, fields, reportComponents);
			}
		};
		if (!t.isAlive()) {
			while(t.isAlive()) {
				for (Field field : fields) {
					field.setAccessible(true);
					Cursor cr = field.getAnnotation(Cursor.class);
					if (cr != null) {
						String fieldName = field.getName();
						try {
							JProgressBar progressBar = (JProgressBar)getComponentByName(fieldName, reportComponents);
							progressBar.setValue(progressBar.getMaximum());
							progressBar.setString("100.00%");
						} catch (Exception e) {
						}
					}
				}
			}
		}
		monitor.start();
	}
	
	private void finishProcess(final Date hrIniCarga, final Field[] fields, final Component[] reportComponents) {
		Date hrFimCarga = new Date();
		endProcessTitleLabel.setVisible(true);
		endProcessLabel.setText(DateFormat.getInstance().getDataHoraAtual());
		long duracao = hrFimCarga.getTime() - hrIniCarga.getTime();
		elapsedTimeTitleLabel.setVisible(true);
		elapsedTimeLabel.setText(DateFormat.getInstance().getTempoGasto(duracao));
		Log.info("Fim do processo de carga: " + DateFormat.getInstance().getDataHoraAtual());
		Log.info("Duração do processo de carga: " + DateFormat.getInstance().getTempoGasto(duracao));
		//
		runButton.setEnabled(true);
		//
		processingIconLabel.setVisible(false);
		//
		enableFormPanel(fields);
		//
		double percent = 0.0d;
		//
		for (Field field : fields) {
			field.setAccessible(true);
			boolean cr = field.isAnnotationPresent(Cursor.class);
			if (cr) {
				String fieldName = field.getName();
				try {
					if (field.getType() == CachedRowSet.class) {
						CachedRowSet fieldRowSet = (CachedRowSet) field.get(loadProcess);
						if (fieldRowSet != null) {
							JProgressBar progressBar = (JProgressBar)getComponentByName(fieldName, reportComponents);
							if (fieldRowSet.size() > 0 && !fieldRowSet.isAfterLast()) {
								percent = ((fieldRowSet.getRow()+0.0) / (fieldRowSet.size()+0.0)) * 100.0;
								progressBar.setMaximum(fieldRowSet.size());
								progressBar.setValue(fieldRowSet.getRow());
								progressBar.setString(NumberFormat.getInstance().formatDoubleToMoney(percent) + "%");
								JLabel label = (JLabel)getComponentByName("size"+fieldName, reportComponents);
								label.setText(fieldRowSet.size()+"");
							} else if (fieldRowSet.size() > 0) {
								if (!progressBar.getString().equalsIgnoreCase("0%")) { 
									progressBar.setValue(fieldRowSet.size());
									percent = ((fieldRowSet.size()+0.0) / (fieldRowSet.size()+0.0)) * 100.0;
									progressBar.setString(NumberFormat.getInstance().formatDoubleToMoney(percent) + "%");
								}
							}
						}
					}
				} catch (Exception e) {
					Log.error("Erro ao tentar realizar a conversão do objeto field para um objeto JProgressBar.");
					Log.error(e);
				}
			}
		}
		//
		ProcessToReLoad.persist();
		//
		String message = "Processo de carga concluído.";
		if (ProcessToReLoad.size() > 0) {
			message += "\n" + ProcessToReLoad.size() + " registros devem ser reprocessados.";
		}
		//
		JOptionPane.showMessageDialog(null, message);
		//
		JFrame frame = (JFrame) this.getRootPane().getParent();
		frame.setTitle(ResourceLocation.TITLE_APPLICATION_NAME);
		this.getParent().setEnabled(true);
		Component[] components = this.getParent().getParent().getComponents();
		setEnableComponentsFromTopPanel(components, true);
		//
	}
	
	private void setEnableComponentsFromTopPanel(Component[] components, boolean isEnabled) {
		for (Component component : components) {
			if (component.getName() != null && component.getName().equalsIgnoreCase("topPanel")) {
				JPanel panel = (JPanel)component;
				Component[] panelComponents = panel.getComponents();
				for (Component panelComp : panelComponents) {
					if (panelComp.getName() != null && panelComp.getName().equalsIgnoreCase("pnlTools")) {
						JPanel interPanel = (JPanel)panelComp;
						Component[] interPanelComponents = interPanel.getComponents();
						for (Component interPanelComp : interPanelComponents) {
							if (interPanelComp.getName() != null && 
									interPanelComp.getName().equalsIgnoreCase("redoButton")) { 
								interPanelComp.setEnabled(isEnabled);
							}
						}	
					}
					if (panelComp.getName() != null && panelComp.getName().equalsIgnoreCase("pnlBancos")) {
						JPanel interPanel = (JPanel)panelComp;
						Component[] interPanelComponents = interPanel.getComponents();
						for (Component interPanelComp : interPanelComponents) {
							if (interPanelComp.getName() != null && 
									interPanelComp.getName().equalsIgnoreCase("connectionButton")) { 
								interPanelComp.setEnabled(isEnabled);
							}
							if (interPanelComp.getName() != null && 
									interPanelComp.getName().equalsIgnoreCase("connectionButtonDest")) { 
								interPanelComp.setEnabled(isEnabled);
							}
						}	
					}
				}
			}
		}
	}
	
	private void enableFormPanel(final Field[] fields) {
		Component[] formComponents = getForm().getComponents();
		for (Field field : fields) {
			field.setAccessible(true);
			Property prop = field.getAnnotation(Property.class);
			if(prop != null) {
				String fieldName = field.getName();
				JFormattedTextField txtField = (JFormattedTextField)getComponentByName(fieldName, formComponents);
				txtField.setEnabled(true);
			}
		}
	}
	
	private Component getComponentByName(String fieldName, Component[] components) {
		Component c = null;
		for (Component component : components) {
			if(fieldName.equals(component.getName())){
				c = component;
				break;
			}
			else continue;
		}
		return c;
	}

	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BorderLayout());
			jPanel2.add(getJPanel3(), BorderLayout.CENTER);
			jPanel2.add(getControlPanel(), BorderLayout.SOUTH);
		}
		return jPanel2;
	}

	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.setLayout(new BorderLayout());
			jPanel3.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			jPanel3.add(getJScrollPane1(), BorderLayout.CENTER);
		}
		return jPanel3;
	}

}  //  @jve:decl-index=0:visual-constraint="36,14"
