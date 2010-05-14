package org.mtec.sistemacarga.framework.gui.builder;

import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

/**
 * Classe responsável por redirecionar a saída de log de console para um componente
 * gráfico (JTextArea) para que o usuário final da aplicação possa acompanhar o 
 * processo de carga
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 01/05/2007
 */
public class GUIPrintStream extends PrintStream {

	private JTextArea textArea;

	private final static String newline = "\n";
	
	private int limit = 1000;
	
	private int position = 0;

	public GUIPrintStream(JTextArea area, OutputStream out) {
		super(out);
		textArea = area;
	}

	@SuppressWarnings("deprecation")
	public void println(String string) {
		if(position == limit) {
			textArea.setText("");
		}
		textArea.append(string + newline);
		textArea.setCaretPosition(textArea.getText().length());
		position++;
	}

	public void print(String string) {
		if(position == limit) {
			textArea.setText("");
		}
		textArea.append(string);
		textArea.setCaretPosition(textArea.getText().length());
		position++;
	}

}

