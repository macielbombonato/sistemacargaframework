package org.mtec.sistemacarga.framework.util;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Classe utilitária que retorna padrões de formatação e parce para campos de data
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 14/02/2007
 */
public class DateFormat {
	/**
	 * SimpleDateFormat
	 */
	private static SimpleDateFormat sdfDefault;
	
	private static SimpleDateFormat sdfCustom;
	
	/**
	 * Instancia do objeto
	 * Singletone
	 */
	private static final DateFormat instance = new DateFormat();
	
	/**
	 * Construtor da classe
	 */
	private DateFormat() {
		sdfDefault = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	/**
	 * Retorna instancia deste objeto.
	 * @return this
	 */
	public static DateFormat getInstance() {
		return instance;
	}
	
	/**
	 * Realiza formatação de uma data do tipo java.util.Date
	 * @param data entrada de um objeto do tipo java.util.Date para formatação
	 * @return String da data formatada.
	 */
	public String formatDate(java.util.Date data) {
		if (data != null) {
			return sdfDefault.format(data);
		}
		return "";
	}
	
	/**
	 * Realiza formatação de uma data do tipo java.sql.Date
	 * @param data entrada de um objeto do tipo java.sql.Date para formatação
	 * @return String da data formatada.
	 */
	public String formatDate(java.sql.Date data) {
		if (data != null) {
			return sdfDefault.format( new java.util.Date(data.getTime()) );
		}
		return "";
	}
	
	/**
	 * Converte um campo String contendo uma data para java.util.Date
	 * @param data String com a data para conversão
	 * @return java.util.Date
	 */
	public java.util.Date parseDate(String data) {
		java.util.Date dataConvertida = null;
		try {
			dataConvertida = sdfDefault.parse( data );
		} catch (ParseException e) {
			Log.info("Ocorreu um erro ao converter a data [" + data + "]");
			dataConvertida = null;
		}
		return dataConvertida;
	}
	
	public java.util.Date parseDate(String data, String pattern) {
		java.util.Date dataConvertida = null;
		try {
			sdfCustom = new SimpleDateFormat(pattern);
			
			dataConvertida = sdfCustom.parse( data );
		} catch (ParseException e) {
			Log.info("Ocorreu um erro ao converter a data [" + data + "]");
			dataConvertida = null;
		}
		return dataConvertida;
	}
	
	/**
	 * Converte um campo String contendo uma data para java.util.Date
	 * @param data String com a data para conversão
	 * @return java.util.Date
	 */
	public java.sql.Timestamp parseTimestamp(String data) {
		java.util.Date dataConvertida = null;
		java.sql.Timestamp dataRetorno = null;
		try {
			dataConvertida = sdfDefault.parse( data );
			dataRetorno = new Timestamp(dataConvertida.getTime());
		} catch (ParseException e) {
			Log.info("Ocorreu um erro ao converter a data [" + data + "]");
			dataConvertida = null;
		}
		return dataRetorno;
	}
	
	public java.sql.Timestamp parseTimestamp(String data, String pattern) {
		java.util.Date dataConvertida = null;
		java.sql.Timestamp dataRetorno = null;
		try {
			sdfCustom = new SimpleDateFormat(pattern);
			dataConvertida = sdfCustom.parse( data );
			dataRetorno = new Timestamp(dataConvertida.getTime());
		} catch (ParseException e) {
			Log.info("Ocorreu um erro ao converter a data [" + data + "]");
			dataConvertida = null;
		}
		return dataRetorno;
	}
	
	/**
	 * Converte um campo String contendo uma data para java.util.Date
	 * @param data String com a data para conversão
	 * @return java.util.Date
	 */
	public java.sql.Timestamp parseTimestamp(String data, int gmt) {
		java.util.Date dataConvertida = null;
		java.sql.Timestamp dataRetorno = null;
		try {
			dataConvertida = sdfDefault.parse( data );
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(dataConvertida);
			cal.add(Calendar.HOUR, gmt);
			cal.set(Calendar.SECOND, 1);
			dataRetorno = new Timestamp(cal.getTime().getTime());
		} catch (ParseException e) {
			Log.info("Ocorreu um erro ao converter a data [" + data + "]");
			dataConvertida = null;
		}
		return dataRetorno;
	}
	
	public java.sql.Timestamp parseTimestamp(String data, int gmt, String pattern) {
		java.util.Date dataConvertida = null;
		java.sql.Timestamp dataRetorno = null;
		try {
			sdfCustom = new SimpleDateFormat(pattern);
			dataConvertida = sdfCustom.parse( data );
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(dataConvertida);
			cal.add(Calendar.HOUR, gmt);
			cal.set(Calendar.SECOND, 1);
			dataRetorno = new Timestamp(cal.getTime().getTime());
		} catch (ParseException e) {
			Log.info("Ocorreu um erro ao converter a data [" + data + "]");
			dataConvertida = null;
		}
		return dataRetorno;
	}
	
	/**
	 * Baseado em um tempo de processamento em milisegungos (long) retorna
	 * uma String informando quanto isto representa em tempo (formatado).
	 * @param miliSegundos long
	 * @return String 
	 */
	public String getTempoGasto(long miliSegundos) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		//
		long seg  = 0L;
		long min  = 0L;
		long hora = 0L;
		//
		while (miliSegundos > 1000) {
			seg++;
			miliSegundos -= 1000;
		}
		while (seg > 60) {
			min++;
			seg -= 60;
		}
		while (min > 60) {
			hora++;
			min -= 60;
		}
		return nf.format(hora) + ":" + nf.format(min) + ":" + nf.format(seg);
	}
	
	/**
	 * Retorna uma String que representa a data e hora atual do sistema.
	 * @return String representando uma data no seguinte padrão de formatação: dd/MM/yyyy - HH:mm:ss
	 */
	public String getDataHoraAtual() {
		sdfDefault.applyPattern("dd/MM/yyyy - HH:mm:ss");
		sdfDefault.setTimeZone(TimeZone.getDefault());
		String dataHora = sdfDefault.format( new java.util.Date() );
		sdfDefault.applyPattern("dd/MM/yyyy");
		return dataHora;
	}
	
}
