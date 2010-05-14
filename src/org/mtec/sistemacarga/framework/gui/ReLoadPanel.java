package org.mtec.sistemacarga.framework.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;


import javax.swing.WindowConstants;

import org.mtec.sistemacarga.framework.dao.ExecuteDMLCommand;
import org.mtec.sistemacarga.framework.dao.ExecuteStoredProcedure;
import org.mtec.sistemacarga.framework.reload.ProcessToReLoad;
import org.mtec.sistemacarga.framework.util.ResourceLocation;


/**
 * Classe responsável por realizar o reprocessamento de cargas que retornaram erros.
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 15/05/2007
 */
public class ReLoadPanel extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	/**
	 * Progress bar que apresenta o andamento do reprocessamento
	 */
	private JProgressBar redoProgressBar = null;

	/**
	 * Label que apresenta qual storedProcedure ou comando DML está sendo reprocessado
	 */
	private JLabel processNameLabel = null;

	private JPanel southPanel = null;

	/**
	 * Label que apresenta o método toString do ArrayList de parâmetros da storedProcedure
	 */
	private JLabel parametersLabel = null;

	/**
	 * Label que contem um GIF animando.
	 * Utilizado para que o usuário perceba quando a aplicação estiver travada.
	 */
	private JLabel processingIconLabel = null;
	
	/**
	 * Thread onde será alocado o objeto de reprocessamento
	 */
	private Thread t = null;  //  @jve:decl-index=0:

	public ReLoadPanel(Frame owner) {
		super(owner);
		initialize();
		setCenter();
		redoProcess();
	}

	private void initialize() {
		this.setSize(569, 117);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setName("redoDialog");
		this.setTitle(ResourceLocation.TITLE_REDO_WINDOW);
		this.setContentPane(getJContentPane());
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
			}
		});
	}
	
	/**
	 * Método utilizado para centralizar a tela de reprocessamento
	 */
	private void setCenter() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		int alturaTela = tk.getScreenSize().height;
		int comprimentoTela = tk.getScreenSize().width;
		
		int alturaJanela = this.getHeight();
		int comprimentoJanela = this.getWidth();
		
		this.setLocation( (comprimentoTela-comprimentoJanela)/2, (alturaTela-alturaJanela)/2 );		
	}
	
	/**
	 * Método responsável por realizar o reprocessamento
	 */
	public void redoProcess() {
		Runnable r = new Runnable() {
			public void run() {
				//
				String message = "";
				int qtdRegToReLoad = 0;
				processingIconLabel.setVisible(true);
				//
				ProcessToReLoad.load();
				LinkedList<ExecuteStoredProcedure> process = new LinkedList<ExecuteStoredProcedure>(ProcessToReLoad.get());
				if (process.size() > 0) {
					qtdRegToReLoad += process.size();
					Iterator<ExecuteStoredProcedure> it = process.iterator();
					ProcessToReLoad.clear();
					int processSize = process.size();
					int processPos  = 0;
					redoProgressBar.setMaximum(processSize);
					while(it.hasNext()) {
						processPos++;
						redoProgressBar.setValue(processPos);
						redoProgressBar.setString(processPos + " / " + qtdRegToReLoad);
						ExecuteStoredProcedure obj = it.next();
						if (obj.getParams() != null) { 
							if (!obj.isPadraoGenerico()) { 
								obj.exec(obj.getParams());
							} else {
								obj.execGenerico(obj.getParams());
							}
						} else {
							if (!obj.isPadraoGenerico()) { 
								obj.exec(null);
							} else {
								obj.execGenerico(null);
							}
						}
						processNameLabel.setText(obj.getSql());
						if (obj.getParams() != null) { 
							parametersLabel.setText(obj.getParams().toString());
						} else {
							parametersLabel.setText("");
						}
						//
						ProcessToReLoad.persist();
						//
					}
					ProcessToReLoad.persist();
					message += "\nReprocessamento concluído.";
				} 
				//
				LinkedList<ExecuteDMLCommand> processDML = new LinkedList<ExecuteDMLCommand>(ProcessToReLoad.DmlGet());
				if (processDML.size() > 0) {
					qtdRegToReLoad += process.size();
					Iterator<ExecuteDMLCommand> it = processDML.iterator();
					ProcessToReLoad.clear();
					int processSize = process.size();
					int processPos  = 0;
					redoProgressBar.setMaximum(processSize);
					while(it.hasNext()) {
						processPos++;
						redoProgressBar.setValue(processPos);
						redoProgressBar.setString(processPos + " / " + qtdRegToReLoad);
						ExecuteDMLCommand obj = it.next();
						if (obj.getParams() != null) { 
							obj.execute(obj.getParams());
						} else {
							obj.execute(null);
						}
						processNameLabel.setText(obj.getSql());
						if (obj.getParams() != null) { 
							parametersLabel.setText(obj.getParams().toString());
						} else {
							parametersLabel.setText("");
						}
					}
					ProcessToReLoad.persist();
					if (!message.equalsIgnoreCase("\nReprocessamento concluído.")) { 
						message += "\nReprocessamento concluído.";
					}
				} 
				//
				if (!message.equalsIgnoreCase("\nReprocessamento concluído.")) {
					message += "\nNenhum registro reprocessado.";
				} else {
					if (ProcessToReLoad.size() > 0 || ProcessToReLoad.DmlSize() > 0) { 
						message += "\n" + (ProcessToReLoad.size() + ProcessToReLoad.DmlSize()) + " devem ser reprocessados.";
					}
				}
				processingIconLabel.setVisible(false);
				JOptionPane.showMessageDialog(null, message);
				ReLoadPanel.this.dispose();
			}
		};
		this.t = new Thread(r);
		t.setName("ReLoad");
		t.start();
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			processNameLabel = new JLabel();
			processNameLabel.setText(" ");
			processNameLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			processNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getRedoProgressBar(), BorderLayout.NORTH);
			jContentPane.add(processNameLabel, BorderLayout.CENTER);
			jContentPane.add(getSouthPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	private JProgressBar getRedoProgressBar() {
		if (redoProgressBar == null) {
			redoProgressBar = new JProgressBar();
			redoProgressBar.setStringPainted(true);
			redoProgressBar.setFont(new Font("Tahoma", Font.BOLD, 11));
			redoProgressBar.setPreferredSize(new Dimension(148, 30));
			redoProgressBar.setString("0 / 0");
		}
		return redoProgressBar;
	}

	private JPanel getSouthPanel() {
		if (southPanel == null) {
			processingIconLabel = new JLabel();
			processingIconLabel.setText("");
			processingIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
			processingIconLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			try {
				processingIconLabel.setIcon(new ImageIcon(getClass().getResource(ResourceLocation.IMG_PROCESSING_BAR)));
			} catch (Exception e) {
			}
			parametersLabel = new JLabel();
			parametersLabel.setHorizontalAlignment(SwingConstants.CENTER);
			parametersLabel.setText("");
			parametersLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			southPanel = new JPanel();
			southPanel.setLayout(new BorderLayout());
			southPanel.setPreferredSize(new Dimension(100, 30));
			southPanel.add(parametersLabel, BorderLayout.NORTH);
			southPanel.add(processingIconLabel, BorderLayout.SOUTH);
		}
		return southPanel;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
