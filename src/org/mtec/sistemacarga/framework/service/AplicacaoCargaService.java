package org.mtec.sistemacarga.framework.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.mtec.sistemacarga.framework.annotations.DBProcess;
import org.mtec.sistemacarga.framework.gui.builder.ApplicationWindow;
import org.mtec.sistemacarga.framework.gui.builder.PanelBuilder;
import org.mtec.sistemacarga.framework.util.Log;
import org.mtec.sistemacarga.framework.util.ResourceLocation;

public class AplicacaoCargaService {
	
	public void executaAplicacaoVisual() {
		Log.info("Executando aplicacao de carga em modo visual.");
		applyLookAndFeel();
		Set<String> p = PanelBuilder.getAllAvailablePanels();
		ApplicationWindow f = new ApplicationWindow();
		for (String name : p) {
			JPanel panel = PanelBuilder.getPanelByName(name);
			f.addTab(name, panel);
		}
		f.setVisible(true);
	}
	
	@SuppressWarnings("unchecked")
	public void executaAplicacaoBatch(String[] args) {
		Log.info("Executando aplicacao de carga em modo batch.");
		
		Map<String, String> mapValores = new HashMap<String, String>();
		
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length - 1; i += 2) {
				mapValores.put(args[i], args[i+1]);
			}
		}
		
		ClassService classService = new ClassService();
		List<Class> classes = classService.getDBProcessClassList();
		
		boolean isLoadProcess = false;
		boolean isCalledLoadProccess = false;
		for (Class c : classes) {
			isLoadProcess = c.isAnnotationPresent(DBProcess.class);
			if (isLoadProcess) {
				isCalledLoadProccess = mapValores.get("carga").equalsIgnoreCase(((DBProcess)c.getDeclaredAnnotations()[0]).value());
				if (isCalledLoadProccess) {
					mapValores.remove("carga");
					ExecutaProcessoCargaBatchService exec = new ExecutaProcessoCargaBatchService(c);
					exec.executeProcess(mapValores);
					break;
				}
			}
		}
	}
	
	/**
	 * Método que verifica a versão da VM do usuário e tenta aplicar 
	 * o Look and Feel padrão escolhido para a aplicação.
	 */
	private void applyLookAndFeel() {
		//
		String javaVersion = System.getProperty("java.version");		
		String laf = null;
		//
		if(javaVersion.startsWith("1.5")) {
			laf = ResourceLocation.LNF;
		} else if(javaVersion.startsWith("1.3")) {
			JOptionPane.showMessageDialog(null, 
					"Você está utilizando a versão 1.3 do java \n" +
					"Esta aplicação necessita da versão 1.5 ou superior" +
					"do java para poder ser executada.");
		} else if(javaVersion.startsWith("1.4")) {
			JOptionPane.showMessageDialog(null, 
					"Você está utilizando a versão 1.4 do java \n" +
					"Esta aplicação necessita da versão 1.5 ou superior" +
					"do java para poder ser executada.");
		} else {
			laf = ResourceLocation.LNF;
		}
		
		try {
			UIManager.setLookAndFeel(laf);
		} catch (Exception e) {
			Log.error("Não foi possível aplicar o Look and Feel padrão da aplicação, " +
					"Será aplicado o Look and Feel padrão do Java (Metal).");
		}
	}

}
