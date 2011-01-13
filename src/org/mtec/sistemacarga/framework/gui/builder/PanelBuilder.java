package org.mtec.sistemacarga.framework.gui.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.mtec.sistemacarga.framework.service.ClassService;
import org.mtec.sistemacarga.framework.sorts.KeysComparator;
import org.mtec.sistemacarga.framework.util.Log;

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
		ClassService classService = new ClassService();
		List<Class> classes = classService.getDBProcessClassList();
		AnnotationProcessor processor = new AnnotationProcessor();
		Object obj = null;
		PanelMetaData metadata = null;
		for (Class c : classes) {
			metadata = null;
			try {
				obj = c.newInstance();
				metadata = processor.processAnnotations(obj);
				obj = null;
			} catch (InstantiationException e) {
				Log.error("Erro durante a conversao e criacao da classe anotada.");
				Log.error(e);
			} catch (IllegalAccessException e) {
				Log.error("Erro durante a conversao e criacao da classe anotada.");
				Log.error(e);
			}
			Tuple<JPanel, JPanel> tuple = buildPanel(metadata);				
			PanelPrototype prototype = new PanelPrototype(tuple.getFirstObject(), tuple.getSecondObject(), c);
			panelTable.put(metadata.getName(), prototype);
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
	private static Tuple<InputFieldMetaData[], InputFieldMetaData[]> disjoint(Set<InputFieldMetaData> components) {
		
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
			panel.add(createTextField(obj), cc.xy(3, i + 1));
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

	static private JFormattedTextField createTextField(InputFieldMetaData obj) {
		JFormattedTextField f = new JFormattedTextField(obj.getElementType());
		f.setName(obj.getFieldName());
		f.setToolTipText(obj.getTip());
		if (obj.getElementValue() != null && !obj.getElementValue().equalsIgnoreCase("null")) {
			f.setText(obj.getElementValue());	
		} else {
			f.setText(null);
		}
		
		
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

}
