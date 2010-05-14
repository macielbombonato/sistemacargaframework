package org.mtec.sistemacarga.framework.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Properties;

import org.mtec.sistemacarga.framework.gui.builder.GUIPrintStream;
import org.mtec.sistemacarga.framework.gui.builder.LogTextArea;



/**
 * Classe responsável por recarregar o procedimento de log da aplicação.
 * 
 * @author Jeferson Barboza
 * @author Maciel Escuder Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 12/04/2007
 */
class LogReload extends Thread {
	
	public void run() {
		Thread.currentThread().setName("LogReaload");
	    do {
	      try {
	        Thread.sleep(refreshTime * 60L * 1000L);
	      }
	      catch(InterruptedException e) { }
	      configureLog4j();
	    } while(true);
	}

	private String complementaXML(Properties parametros) throws Exception {
		File f = new File(ResourceLocation.RESOURCES_PATH + ResourceLocation.LOG_CONFIG_XML);
		FileInputStream fis = new FileInputStream( f );
		String xmlDocument = "";
		String linha = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		do {
	        linha = br.readLine();
	        if(linha != null) {
	            xmlDocument = xmlDocument + linha;
	        } else {
	            br.close();
	            fis.close();
	            return formatarXML(xmlDocument, parametros);
	        }
	    } while(true);
	}
	
	@SuppressWarnings("deprecation")
	private void configureLog4j() {
	    try {
	        Properties parametros = loadParametros();
	        refreshTime = Long.parseLong(parametros.getProperty("log.refreshTime", "30"));
	        String xmlDocument = complementaXML(parametros);
	        Object o = invokeMethod(org.apache.log4j.Category.class, "getDefaultHierarchy", null, null);
	        invokeMethod(o, "resetConfiguration", null, null);
	        org.apache.log4j.xml.DOMConfigurator domConf = new org.apache.log4j.xml.DOMConfigurator();
	        Class paramClass = org.apache.log4j.Hierarchy.class;
	        try {
	        	paramClass = Class.forName("org.apache.log4j.spi.LoggerRepository");
	        } catch(Exception e) { }
	        InputStream bytesStream = new ByteArrayInputStream(xmlDocument.getBytes());
	        invokeMethod(domConf, "doConfigure", new Class[] {
	        		java.io.InputStream.class, paramClass
	        }, new Object[] {
	        		bytesStream, o
	        	});
	        Log.info("ApplicationLog inicializado - " + ResourceLocation.LOG_CONFIG_XML);
	    } catch(Exception e) {
	    	System.out.println("Erro carregando o arquivo de configuracao " + ResourceLocation.LOG_CONFIG_XML);
	    	e.printStackTrace();
	    }
	}
	
	private String formatarXML(String xml, Properties parametros) {
	    String retorno = xml;
	    String paramFileAppender = parametros.getProperty("log.fileAppender.path", "/tmp/");
	    String paramXmlAppender = parametros.getProperty("log.xmlAppender.path", "/tmp/");
	    int indTokenFile = retorno.indexOf("$PATH_FILE_APPENDER$");
	    if(indTokenFile > -1) {
	    	String parteA = retorno.substring(0, indTokenFile);
	    	String parteB = retorno.substring(indTokenFile + "$PATH_FILE_APPENDER$".length(), retorno.length());
	    	retorno = parteA + paramFileAppender + parteB;
	    }
	    int indTokenXml = retorno.indexOf("$PATH_XML_APPENDER$");
	    if(indTokenXml > -1) {
	    	String parteA = retorno.substring(0, indTokenXml);
	    	String parteB = retorno.substring(indTokenXml + "$PATH_XML_APPENDER$".length(), retorno.length());
	    	retorno = parteA + paramXmlAppender + parteB;
	    }
	    return retorno;
	}
	
	private Properties loadParametros() throws IOException {
	    Properties parametros = new Properties();
	    if(parametros == null) {
			Log.info("CARREGANDO O ARQUIVO DE PROPRIEDADE");
			parametros = new Properties();
			parametros.setProperty("log.refreshTime", ResourceUtil.getInstance().getProperty(ResourceLocation.CONF_PROPERTIES, "log.refreshTime"));
			parametros.setProperty("log.fileAppender.path", ResourceUtil.getInstance().getProperty(ResourceLocation.CONF_PROPERTIES, "log.fileAppender.path"));
			parametros.setProperty("log.xmlAppender.path", ResourceUtil.getInstance().getProperty(ResourceLocation.CONF_PROPERTIES, "log.xmlAppender.path"));
			
		}
	    return parametros;
	}
	
	public LogReload() {
		GUIPrintStream out = new GUIPrintStream(LogTextArea.getInstance().getTxtLog(), System.out);
		System.setOut(out);
		System.setErr(out);
	    synchronized(isIntanciated) {
	    	if(isIntanciated.equals(Boolean.FALSE)) {
	    		isIntanciated = Boolean.TRUE;
	    		configureLog4j();
	    		setDaemon(true);
	    		start();
	    	}
	    }
	}
	
	private Object invokeMethod(Object o, String methodName, Class paramClasses[], Object params[]) throws Exception {
	    Method method = o.getClass().getMethod(methodName, paramClasses);
	    return method.invoke(o, params);
	}
	
	@SuppressWarnings("unchecked")
	private Object invokeMethod(Class clazz, String methodName, Class paramClasses[], Object params[]) throws Exception {
	    Method method = clazz.getMethod(methodName, paramClasses);
	    return method.invoke(null, params);
	}
	
	private static Boolean isIntanciated;
	private static long refreshTime = 30L;
	
	static {
	    isIntanciated = Boolean.FALSE;
	}
}
