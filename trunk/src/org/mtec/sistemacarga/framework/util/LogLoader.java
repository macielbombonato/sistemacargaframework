package org.mtec.sistemacarga.framework.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.log4j.Category;
import org.mtec.sistemacarga.framework.gui.builder.GUIPrintStream;
import org.mtec.sistemacarga.framework.gui.builder.LogTextArea;



/**
 * Classe responsável por ler o arquivo de parâmetros e inicializar o procedimento de log.
 * 
 * @author Jeferson Barboza
 * @author Maciel Escuder Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 12/04/2007
 */
public class LogLoader {
	
	public static synchronized LogLoader getInstance() throws Exception {
		if(logLoader == null)
			logLoader = new LogLoader();
		return logLoader;
	}

	private LogLoader() throws Exception {
		GUIPrintStream out = new GUIPrintStream(LogTextArea.getInstance().getTxtLog(), System.out);
		System.setOut(out);
		System.setErr(out);
		loadParametros();
		configureLog4j();
	}

	private String complementaXML() throws Exception {
		if(xmlFormatado == null) {
			Log.info("CARREGANDO AS CONFIGURACOES DO LOG4J");
			System.out.println("CARREGANDO AS CONFIGURACOES DO LOG4J");
			File f = new File(ResourceLocation.RESOURCES_PATH + ResourceLocation.LOG_CONFIG_XML);
			FileInputStream fis = new FileInputStream( f );
			String xmlDocument = "";
			String linha = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			do {
				linha = br.readLine();
				if(linha == null)
					break;
				xmlDocument = xmlDocument + linha;
			} while(true);
			br.close();
			fis.close();
			xmlFormatado = formatarXML(xmlDocument, parametros);
		}
		return xmlFormatado;
	}

	@SuppressWarnings("deprecation")
	private void configureLog4j() throws Exception {
		try {
			clearLog();
			String xmlDocument = complementaXML();
			org.apache.log4j.xml.DOMConfigurator domConf = new org.apache.log4j.xml.DOMConfigurator();
			domConf.doConfigure(new ByteArrayInputStream(xmlDocument.getBytes()), Category.getDefaultHierarchy());
			Log.info("ApplicationLog inicializado - LogConfig.xml");
		} catch(Exception e) {
			System.out.println("Erro carregando o arquivo de configuracao " + ResourceLocation.LOG_CONFIG_XML);
			e.printStackTrace();
			throw new Exception("FMW", e);
		}
	}

	private void clearLog() {
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

	private void loadParametros() throws Exception {
		if(parametros == null) {
			Log.info("CARREGANDO O ARQUIVO DE PROPRIEDADE");
			parametros = new Properties();
			parametros.setProperty("log.refreshTime", ResourceUtil.getInstance().getProperty("log.refreshTime"));
			parametros.setProperty("log.fileAppender.path", ResourceUtil.getInstance().getProperty("log.fileAppender.path"));
			parametros.setProperty("log.xmlAppender.path", ResourceUtil.getInstance().getProperty("log.xmlAppender.path"));
			
		}
	}

	public void reloadProperties() throws Exception {
		parametros = null;
		loadParametros();
	}

	public void clearAppLoag() throws Exception {
		xmlFormatado = null;
		configureLog4j();
	}

	private static LogLoader logLoader;
	private Properties parametros;
	private String xmlFormatado;
}
