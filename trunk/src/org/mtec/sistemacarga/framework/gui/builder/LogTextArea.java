package org.mtec.sistemacarga.framework.gui.builder;

import javax.swing.JTextArea;

/**
 * Objeto JTextArea utilizado por toda aplicação para apresentar ao usuário final
 * a saída de console da aplicação para acompanhamento do processo de carga
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 01/05/2007
 */
public class LogTextArea {
	
	private JTextArea txtLog;
	
	private static final LogTextArea instance = new LogTextArea();
	
	private LogTextArea() {
		if (txtLog == null) {
			txtLog = new JTextArea();
			txtLog.setRows(10);
			txtLog.setName("txtLog");
			txtLog.setEditable(false);
		}
	}
	
	/**
	 * Retorna instancia deste objeto.
	 * @return this
	 */
	public static LogTextArea getInstance() {
		return instance;
	}
	
	public JTextArea getTxtLog() {
		if (txtLog != null) {
			return txtLog;
		} else {
			txtLog = new JTextArea();
			txtLog.setRows(10);
			txtLog.setName("txtLog");
			txtLog.setEditable(false);
			return txtLog;
		}
	}

}
