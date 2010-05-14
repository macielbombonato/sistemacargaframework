package org.mtec.sistemacarga.framework.util;

import java.text.DecimalFormat;

/**
 * Classe utilitária que retorna padrões de formatação e parce para campos númericos
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 14/02/2007
 */
public class NumberFormat {
	
	/**
	 * Instancia do objeto
	 * Singletone
	 */
	private static final NumberFormat instance = new NumberFormat();
	
	/**
	 * Retorna a instancia do objeto
	 * @return this
	 */
	public static NumberFormat getInstance() {
		return instance;
	}
	
	/**
     * formata um numero double para a forma ###.###.###.##0,00
     */
    public String formatDoubleToMoney(double value) {
        DecimalFormat formater = new DecimalFormat("###,###,###,##0.00");
        return formater.format(value);
    }
    
    /**
     * formata um numero double para a forma ###.###.###.##0
     */
    public String formatInteger(int value) {
        DecimalFormat formater = new DecimalFormat("###,###,###,##0");
        return formater.format(value);
    }


}
