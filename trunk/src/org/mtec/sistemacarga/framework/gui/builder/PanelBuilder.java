package org.mtec.sistemacarga.framework.gui.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import org.mtec.sistemacarga.framework.annotations.DBProcess;
import org.mtec.sistemacarga.framework.sorts.KeysComparator;
import org.mtec.sistemacarga.framework.util.Log;
import org.mtec.sistemacarga.framework.util.ResourceLocation;


import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Classe utilitária responsável por recuperar todas as classes que implementam
 * um processo de carga e monta JPanels adequados para a cada uma delas.
 * 
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * 
 * @version 1.0 - 25/04/2007
 * @version 1.1 - 16/10/2007 - Inclusão de tratamento para o caso de existir componente no diretório Lib
 *                             mas não ter sido mencionado no arquivo executável da aplicação, antes deste
 *                             tratamento a aplicação não abria agora, a aplicação gera log e continua o
 *                             processo de inicialização.
 */
@SuppressWarnings({ "static-access", "unchecked" })
public class PanelBuilder {

	private static Map<String, JPanel> panelTable = new TreeMap<String, JPanel>();

	private static Map fieldMap = new TreeMap( new KeysComparator() );
	
	/**
	 * Define o Look and Feel dos panels, carrega os componentes de carga e insere os paineis 
	 * na tela principal da aplicação.
	 */
	static {
		try {
			applyLookAndFeel();
			List<Class> classes = filterClasses(loadAllClasses());
			AnnotationProcessor processor = new AnnotationProcessor();
			for (Class c : classes) {
				PanelMetaData metadata = processor.processAnnotations(c);
				Tuple<JPanel, JPanel> tuple = buildPanel(metadata);				
				PanelPrototype prototype = new PanelPrototype(tuple.getFirstObject(), tuple.getSecondObject(), c);
				panelTable.put(metadata.getName(), prototype);
			}
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	/**
	 * Método que verifica a versão da VM do usuário e tenta aplicar 
	 * o Look and Feel padrão escolhido para a aplicação.
	 */
	static void applyLookAndFeel() {
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
	
	public static JPanel getPanelByName(String name) {
		return panelTable.get(name);
	}

	public static Set<String> getAllAvailablePanels() {
		return panelTable.keySet();
	}

	static private Tuple<JPanel, JPanel> buildPanel(PanelMetaData metadata) {
		
		Set<InputFieldMetaData> components = metadata.getInputFields();
		
		Tuple<InputFieldMetaData[], InputFieldMetaData[]> sets = disjoint(components);
		JPanel form = createFormPanel(sets.getFirstObject());
		JPanel report = createReportPanel(sets.getSecondObject());
		Tuple<JPanel, JPanel> tuple = new Tuple<JPanel, JPanel>(form, report);
		return tuple;
	}

	@SuppressWarnings("unchecked")
	private static Tuple<InputFieldMetaData[], InputFieldMetaData[]> disjoint(
			Set<InputFieldMetaData> components) {
		List<InputFieldMetaData> formObjs = new ArrayList<InputFieldMetaData>();
		List<InputFieldMetaData> reportObjs = new ArrayList<InputFieldMetaData>();
		// insere componentes em um map ordenado
		fieldMap.clear();
		for (InputFieldMetaData data : components) {
			fieldMap.put(data.getFieldName(), data);
		}
		// insere componentes no respectivo panel ordenado
		Set keys = fieldMap.keySet();
		Iterator it = keys.iterator();
		Object o = null;
		while (it.hasNext()) {
			o = it.next();
			switch (((InputFieldMetaData) fieldMap.get(o)).getType()) {
			case INPUT:
				formObjs.add((InputFieldMetaData) fieldMap.get(o));
				break;
			case PROGRESS_BAR:
				reportObjs.add((InputFieldMetaData) fieldMap.get(o));
				break;
			}
		}
		
		Tuple<InputFieldMetaData[], InputFieldMetaData[]> tuple = new Tuple<InputFieldMetaData[], InputFieldMetaData[]>(
				formObjs.toArray(new InputFieldMetaData[formObjs.size()]),
				reportObjs.toArray(new InputFieldMetaData[reportObjs.size()]));
		return tuple;
	}

	@SuppressWarnings("static-access")
	private static JPanel createReportPanel(InputFieldMetaData[] components) {
		String rows = getRows(components.length * 2);
		FormLayout layout = new FormLayout("pref, 4dlu, 120dlu, 80dlu", // columns
				rows); // rows
		layout.setRowGroups(getRowGroups(components.length));
		JPanel panel = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		for (int i = 0; i < components.length; i++) {
			InputFieldMetaData obj = components[i];
			panel.add(new JLabel(obj.getLabelName()), cc.xy(1, 2 * i + 1));
			JLabel sizeLabel = new JLabel("0");
			sizeLabel.setName("size" + obj.getFieldName());
			sizeLabel.setHorizontalAlignment(4);
			panel.add(sizeLabel, cc.xy(4, 2 * i + 1));
			panel.add(createJProgressBar(obj.getFieldName()), cc.xyw(1,
					2 * i + 2, 4));
		}
		return panel;
	}

	private static JPanel createFormPanel(InputFieldMetaData[] components) {
		String rows = getRows(components.length);
		FormLayout layout = new FormLayout("pref, 4dlu, 80dlu", // columns
				rows); // rows
		layout.setRowGroups(getRowGroups(components.length));
		JPanel panel = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		for (int i = 0; i < components.length; i++) {
			InputFieldMetaData obj = components[i];
			panel.add(new JLabel(obj.getLabelName()), cc.xy(1, i + 1));
			panel.add(createTextField(obj.getFieldName(), obj.getTip(), obj.getElementType()), cc.xy(3, i + 1));
		}
		return panel;
	}

	private static int[][] getRowGroups(int qtd) {
		int[][] rowGroups = new int[qtd][1];
		for (int i = 0; i < rowGroups.length; i++) {
			rowGroups[i][0] = 2 * i + 1;
		}
		return rowGroups;
	}

	private static JProgressBar createJProgressBar(String fieldName) {
		JProgressBar bar = new JProgressBar();
		bar.setStringPainted(true);
		bar.setSize(200, 22);
		bar.setName(fieldName);
		bar.setMinimum(0);
		return bar;
	}

	static private JFormattedTextField createTextField(String fieldName, String tip, Class elementType) {
		JFormattedTextField f = new JFormattedTextField(elementType);
		f.setName(fieldName);
		f.setToolTipText(tip);
		f.setText(null);
		
		return f;
	}

	static private String getRows(int qtdObjs) {
		String rows = "";
		for (int i = 0; i < qtdObjs - 1; i++) {
			rows += "pref, 20dlu, ";
		}
		rows += "pref";
		return rows;
	}

	@SuppressWarnings("unchecked")
	static private List<Class> filterClasses(List<Class> classes) {
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

	static private List<Class> loadAllClasses() throws ClassNotFoundException {
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

	private static void getClassesFromJar(List<Class> classes, String jarFile) throws ClassNotFoundException {
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
