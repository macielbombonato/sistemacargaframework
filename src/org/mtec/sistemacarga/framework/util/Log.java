package org.mtec.sistemacarga.framework.util;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.mtec.sistemacarga.framework.gui.builder.GUIPrintStream;
import org.mtec.sistemacarga.framework.gui.builder.LogTextArea;



/**
 * Classe responsável pela geração de Log da aplicação
 * 
 * @author Jeferson Barboza
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 12/04/2007
 */
public final class Log {
	
	private Log() {
		GUIPrintStream out = new GUIPrintStream(LogTextArea.getInstance().getTxtLog(), System.out);
		System.setOut(out);
		System.setErr(out);
	}

	private static void init() {
		new LogReload();
		try {
			InetAddress host = InetAddress.getLocalHost();
			hostName = host.getHostName();
		} catch(UnknownHostException e) {
			hostName = "";
			System.out.println("Erro buscando nome do Host para o " + ResourceLocation.CONF_PROPERTIES);
		}
	}

	@SuppressWarnings("deprecation")
	private static String getClassMethodAndNumber() {
		try {
			StringBuffer outStr;
			StringWriter sw = new StringWriter();
			(new Throwable()).printStackTrace(new PrintWriter(sw));
			BufferedReader reader = new BufferedReader(new StringReader(sw.toString()));
			sw.close();
			reader.readLine();
			reader.readLine();
			reader.readLine();
			String line = reader.readLine();
			outStr = new StringBuffer(line.substring(4, line.indexOf("(")));
			int index1 = line.indexOf(":");
			int index2 = line.indexOf(")");
			if(index1 > 0 && index2 > 0)
				outStr.append(line.substring(index1, index2));
			return outStr.toString();
		} catch(Throwable e) {
			String classe = "org.mtec.sistemacarga.framework.util.Log.getClassMethodAndNumber";
			Category cat = Category.getInstance(classe);
			cat.fatal(classe + " - " + getStackTrace(e));
			System.out.println(classe + " - " + getStackTrace(e));
			return "***";
		}
	}
	// fatal error //
	@SuppressWarnings("deprecation")
	public static void fatal(String str) {
		String cmn = getClassMethodAndNumber();
		String classe;
		if(cmn.indexOf(":") > 0)
			classe = cmn.substring(0, cmn.indexOf(":"));
		else
			classe = cmn;
		Category cat = Category.getInstance(classe);
		cat.fatal(hostName + " - " + cmn + " - " + str);
		System.out.println(hostName + " - " + cmn + " - " + str);
	}
	// error //
	@SuppressWarnings("deprecation")
	public static void error(String str) {
		String cmn = getClassMethodAndNumber();
		String classe;
		if(cmn.indexOf(":") > 0)
			classe = cmn.substring(0, cmn.indexOf(":"));
		else
			classe = cmn;
		Category cat = Category.getInstance(classe);
		cat.error(hostName + " - " + cmn + " - " + str);
		System.out.println(hostName + " - " + cmn + " - " + str);
	}
	// warn //
	@SuppressWarnings("deprecation")
	public static void warn(String str) {
		Category cat = Category.getInstance("org.mtec.sistemacarga.framework.util.Log.getClassMethodAndNumber");
		if(cat.isEnabledFor(Priority.WARN)) {
			String cmn = getClassMethodAndNumber();
			String classe;
			if(cmn.indexOf(":") > 0)
				classe = cmn.substring(0, cmn.indexOf(":"));
			else
				classe = cmn;
			cat = Category.getInstance(classe);
			cat.warn(hostName + " - " + cmn + " - " + str);
			System.out.println(hostName + " - " + cmn + " - " + str);
		}
	}
	// info //
	@SuppressWarnings("deprecation")
	public static void info(String str) {
		Category cat = Category.getInstance("org.mtec.sistemacarga.framework.util.Log");
		if(cat.isInfoEnabled()) {
			String cmn = getClassMethodAndNumber();
			String classe;
			if(cmn.indexOf(":") > 0) {
				classe = cmn.substring(0, cmn.indexOf(":"));
			} else {
				classe = cmn;
			}
			cat = Category.getInstance(classe);
			cat.info(cmn + " - " + str);
			System.out.println(cmn + " - " + str);
		}
	}
	// debug //
	@SuppressWarnings("deprecation")
	public static void debug(Object str[]) {
		Category cat = Category.getInstance("org.mtec.sistemacarga.framework.util.Log");
		if(cat.isDebugEnabled()) {
			String cmn = getClassMethodAndNumber();
			String classe;
			if(cmn.indexOf(":") > 0)
				classe = cmn.substring(0, cmn.indexOf(":"));
			else
				classe = cmn;
			cat = Category.getInstance(classe);
			StringBuffer sb = (new StringBuffer(cmn)).append(" - ").append(((Object) (str)));
			for(int i = 0; i < str.length; i++)
				sb.append(str[i]);
			cat.debug(sb.toString());
			System.out.println(sb.toString());
		}
	}
	// debug //
	@SuppressWarnings("deprecation")
	public static void debug(String str) {
		Category cat = Category.getInstance("org.mtec.sistemacarga.framework.util.Log");
		if(cat.isDebugEnabled()) {
			String cmn = getClassMethodAndNumber();
			String classe;
			if(cmn.indexOf(":") > 0)
				classe = cmn.substring(0, cmn.indexOf(":"));
			else
				classe = cmn;
			cat = Category.getInstance(classe);
			cat.debug(cmn + " - " + str);
			System.out.println(cmn + " - " + str);
		}
	}
	// stack Trace
	public static String getStackTrace(Throwable e) {
		try {
			if(e == null)
				return null;
			String strStackTrace;
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			strStackTrace = sw.toString();
			sw.close();
			return strStackTrace;
		} catch(Exception e2) {
			return "Error getting StackTrace:" + e2.toString();
		}  
	}
	// fatal com trowable //
	@SuppressWarnings("deprecation")
	public static void fatal(Throwable e) {
		String cmn = getClassMethodAndNumber();
		String classe;
		if(cmn.indexOf(":") > 0)
			classe = cmn.substring(0, cmn.indexOf(":"));
		else
			classe = cmn;
		Category cat = Category.getInstance(classe);
		cat.fatal(hostName + " - " + cmn + " - " + getStackTrace(e));
		System.out.println(hostName + " - " + cmn + " - " + getStackTrace(e));
	}
	// error String //
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void error(String s, Class clazz) {
		Category cat = Category.getInstance(clazz);
		cat.error((new StringBuffer(hostName)).append(" - ").append(s));
		System.out.println((new StringBuffer(hostName)).append(" - ").append(s));
	}
	// error trowable //
	@SuppressWarnings("deprecation")
	public static void error(Throwable e) {
		String cmn = getClassMethodAndNumber();
		String classe;
		if(cmn.indexOf(":") > 0)
			classe = cmn.substring(0, cmn.indexOf(":"));
		else
			classe = cmn;
		Category cat = Category.getInstance(classe);
		cat.error(hostName + " - " + cmn + " - " + getStackTrace(e));
		System.out.println(hostName + " - " + cmn + " - " + getStackTrace(e));
	}
	// Warn //
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void warn(String s, Class clazz) {
		Category cat = Category.getInstance(clazz);
		cat.warn((new StringBuffer(hostName)).append(" - ").append(s));
		System.out.println((new StringBuffer(hostName)).append(" - ").append(s));
	}
	// Warn trowable //
	@SuppressWarnings("deprecation")
	public static void warn(Throwable e) {
		Category cat = Category.getInstance("org.mtec.sistemacarga.framework.util.Log");
		if(cat.isEnabledFor(Priority.WARN)) {
			String cmn = getClassMethodAndNumber();
			String classe;
			if(cmn.indexOf(":") > 0)
				classe = cmn.substring(0, cmn.indexOf(":"));
			else
				classe = cmn;
			cat = Category.getInstance(classe);
			cat.warn(hostName + " - " + cmn + " - " + getStackTrace(e));
			System.out.println(hostName + " - " + cmn + " - " + getStackTrace(e));
		}
	}
	// Start //
	private static String hostName;
	static {
		init();
	}
}
