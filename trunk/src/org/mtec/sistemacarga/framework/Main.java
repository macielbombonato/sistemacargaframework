package org.mtec.sistemacarga.framework;

import java.util.Set;

import javax.swing.JPanel;

import org.mtec.sistemacarga.framework.gui.builder.ApplicationWindow;
import org.mtec.sistemacarga.framework.gui.builder.PanelBuilder;



/**
 * Classe Main da aplicação.
 * Esta classe deve ser executada para que os componentes associados pelos arquivos 
 * de configuração sejam montados na tela para o usuário
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 11/05/2007
 */
public class Main {

	/**
	 * Método Main da aplicação
	 * @param args String[] -> Não tratado nesta aplicação
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Set<String> p = PanelBuilder.getAllAvailablePanels();
		ApplicationWindow f = new ApplicationWindow();
		for (String name : p) {
			JPanel panel = PanelBuilder.getPanelByName(name);
			f.addTab(name, panel);
		}
		f.setVisible(true);
	}

}
