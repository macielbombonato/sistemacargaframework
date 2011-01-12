package org.mtec.sistemacarga.framework.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.mtec.sistemacarga.framework.annotations.DBProcess;
import org.mtec.sistemacarga.framework.util.Log;
import org.mtec.sistemacarga.framework.util.ResourceLocation;

public class ClassService {
	
	@SuppressWarnings("unchecked")
	public List<Class> getDBProcessClassList() {
		List<Class> classes;
		try {
			classes = filterClasses(loadAllClasses());
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
		
		return classes;
	}
	
	@SuppressWarnings("unchecked")
	private List<Class> filterClasses(List<Class> classes) {
		int size = classes.size();
		for (int i = size - 1; i >= 0; i--) {
			Class c = classes.get(i);
			boolean annotationPresent = c.isAnnotationPresent(DBProcess.class);
			if(!annotationPresent) {
				classes.remove(c);
			}
		}
		return classes;
	}
	
	@SuppressWarnings("unchecked")
	private List<Class> loadAllClasses() throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		File f = new File(ResourceLocation.JAR_FILE_DIRECTORY);
		if (f.exists() && f.isDirectory()) {
			String[] files = f.list();
			for (String jarFile : files) {
				if(jarFile.endsWith(".jar")) {
					getClassesFromJar(classes, f.getAbsolutePath()+File.separatorChar+jarFile);
				}
			}
		} else if (f.exists() && f.isFile()) {
			if(f.getName().endsWith(".jar")) {
				getClassesFromJar(classes, f.getAbsolutePath());
			}
		}
		return classes;
	}
	
	@SuppressWarnings("unchecked")
	private void getClassesFromJar(List<Class> classes, String jarFile) throws ClassNotFoundException {
		try {
			JarFile jar = new JarFile(jarFile);
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry elem = entries.nextElement();
				String name = elem.getName();
				if (name.startsWith(ResourceLocation.PKG_NAME) && name.endsWith(".class")) {
					String tmp = "";
					try {
						tmp = name.replace('/', '.');
						tmp = tmp.substring(0, tmp.length() - 6);
						Class<?> clazz = Class.forName(tmp);
						classes.add(clazz);
					} catch (Exception e) {
						Log.error("Erro ao tentar carregar o componente " + tmp + ", verifique se o JAR que está no diretório LIB foi mencionado no classPath do executável da aplicação.");
						Log.error(e);
					}
				}
			}
		} catch (IOException e) {
			Log.error("Erro ao tentar abrir o arquivo jar de componente de carga.");
			Log.error(e);
		} catch (Exception ex) {
			Log.error("Erro fatal ao tentar processar o componente de carga, verifique o classPath da aplicação.");
			Log.error(ex);
		}
	}


}
