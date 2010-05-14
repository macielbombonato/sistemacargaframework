package org.mtec.sistemacarga.framework.gui.builder;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import javax.swing.ImageIcon;

import org.mtec.sistemacarga.framework.dao.ConnectionPoolManagerDestino;
import org.mtec.sistemacarga.framework.dao.ConnectionPoolManagerOrigem;
import org.mtec.sistemacarga.framework.gui.LoginVisual;
import org.mtec.sistemacarga.framework.gui.ReLoadPanel;
import org.mtec.sistemacarga.framework.reload.ProcessToReLoad;
import org.mtec.sistemacarga.framework.util.Log;
import org.mtec.sistemacarga.framework.util.ResourceLocation;


/**
 * Tela principal da aplicação, onde serão montados todos objetos de carga.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 * @version 1.1 - 27/09/2007 - Alteração para suportar bancos de origem e destino de dados.
 * @version 1.2 - 16/10/2007 - Inclusão de ícone no botão sair, alteração do ícone da aplicação 
 *                             e tratamento de erro (sem ações) caso o ícone seja retirado do jar.
 * @version 1.3 - 05/12/2007 - Título da aplicação colocado como constante em ResourceLocation.APPLICATION_NAME
 */
@SuppressWarnings("serial")
public class ApplicationWindow extends JFrame {

	private JPanel jContentPane = null;

	private JTabbedPane tabbedPane = null;
	
	/**
	 * Painel superior da tela, onde ficam os botões de controle da aplicação:
	 * reprocessamento e conexão.
	 */
	private JPanel topPanel = null;
	
	private JButton connectionButton = null;
	private JLabel connectionStatusLabel = null;
	
	private JButton connectionButtonDest = null;
	private JLabel connectionStatusLabelDest = null;
	
	
	
	private JPanel logPanel = null;

	private JScrollPane logScrollPane = null;

	/**
	 * Apresenta ao usuário a saída de console
	 */
	private JTextArea jTextArea = null;

	/**
	 * Botão que abre a tela de reprocessamento
	 */
	private JButton redoButton = null;

	private JButton closeButton = null;

	private JPanel pnlTools = null;

	private JPanel pnlBancos = null;

	/**
	 * Coloca a aplicação no centro da tela da máquina do usuário
	 */
	private void centralizaTela() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		int alturaTela = tk.getScreenSize().height;
		int comprimentoTela = tk.getScreenSize().width;
		
		int alturaJanela = this.getHeight();
		int comprimentoJanela = this.getWidth();
		
		this.setLocation( (comprimentoTela-comprimentoJanela)/2, (alturaTela-alturaJanela)/2 );
	}
	
	private JPanel getTopPanel() {
		if (topPanel == null) {
			connectionStatusLabel = new JLabel();
			connectionStatusLabel.setText(ResourceLocation.DATABASE_USER_ORIGEM  + " - " + ResourceLocation.DATABASE_URL_ORIGEM);
			connectionStatusLabel.setBounds(new Rectangle(106, 4, 691, 14));
			connectionStatusLabelDest = new JLabel();
			connectionStatusLabelDest.setText(ResourceLocation.DATABASE_USER_DESTINO  + " - " + ResourceLocation.DATABASE_URL_DESTINO);
			connectionStatusLabelDest.setBounds(new Rectangle(105, 25, 692, 14));
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			topPanel = new JPanel();
			topPanel.setName("topPanel");
			topPanel.setComponentOrientation(ComponentOrientation.UNKNOWN);
			topPanel.setPreferredSize(new Dimension(1175, 52));
			topPanel.setLayout(flowLayout);
			topPanel.add(getPnlTools(), null);
			topPanel.add(getPnlBancos(), null);
		}
		return topPanel;
	}
	
	/**
	 * Botão que abre a tela de escolha de banco de dados alvo da carga
	 * @return JButton connectionButton
	 */
	private JButton getConnectionButton() {
		if (connectionButton == null) {
			connectionButton = new JButton();
			connectionButton.setText("Banco Origem -->");
			connectionButton.setName("connectionButton");
			connectionButton.setBounds(new Rectangle(1, 1, 99, 21));
			connectionButton.setMnemonic('o');
			connectionButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					openLoginDialogOrigem();
				}
			});
		}
		return connectionButton;
	}
	
	/**
	 * Método responsável por abrir a tela de escolha de banco de dados
	 * alvo do processo de carga
	 */
	private void openLoginDialogOrigem() {
		LoginVisual login = new LoginVisual(this, true);
		login.setModal(true);
		login.setVisible(true);
		connectionStatusLabel.setText(new String(ResourceLocation.DATABASE_USER_ORIGEM + " - " + ResourceLocation.DATABASE_URL_ORIGEM));
	}
	
	/**
	 * Método responsável por abrir a tela de escolha de banco de dados
	 * alvo do processo de carga
	 */
	private void openLoginDialogDestino() {
		LoginVisual login = new LoginVisual(this, false);
		login.setModal(true);
		login.setVisible(true);
		connectionStatusLabelDest.setText(ResourceLocation.DATABASE_USER_DESTINO + " - " + ResourceLocation.DATABASE_URL_DESTINO);
	}
	
	public ApplicationWindow() {
		super();
		PanelBuilder.applyLookAndFeel();
		initialize();
	}

	private void initialize() {
		this.setSize(932, 750);
		try {
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(ResourceLocation.IMG_BOOKMARK_NEXT)));
		} catch (Exception e2) {
		}
		this.setMinimumSize(new Dimension(790, 590));
		this.setPreferredSize(new Dimension(790, 590));
		centralizaTela();
		this.setContentPane(getJContentPane());
		this.setTitle(ResourceLocation.TITLE_APPLICATION_NAME);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				try {
					ProcessToReLoad.persist();
					ConnectionPoolManagerOrigem.getInstance().shutdown();
					ConnectionPoolManagerDestino.getInstance().shutdown();
				} catch (SQLException e1) {
					Log.error(e1);
				}
				System.exit(0);
			}
		});
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.setName("jContentPane");
			jContentPane.add(getTabbedPane(), BorderLayout.CENTER);
			jContentPane.add(getTopPanel(), BorderLayout.NORTH);
			jContentPane.add(getLogPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	public JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
		}
		return tabbedPane;
	}
	
	public void addTab(String name, JPanel panel) {
		getTabbedPane().addTab(name, panel);
	}

	private JPanel getLogPanel() {
		if (logPanel == null) {
			logPanel = new JPanel();
			logPanel.setLayout(new BorderLayout());
			logPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			logPanel.add(getLogScrollPane(), BorderLayout.NORTH);
		}
		return logPanel;
	}

	private JScrollPane getLogScrollPane() {
		if (logScrollPane == null) {
			logScrollPane = new JScrollPane();
			logScrollPane.setViewportView(getJTextArea());
		}
		return logScrollPane;
	}

	/**
	 * TextArea que apresenta saída de console para que o usuário possa acompanhar o
	 * andamento do processo de carga
	 * @return JTextArea
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = LogTextArea.getInstance().getTxtLog();
			jTextArea.setText("");
			jTextArea.setEditable(false);
		}
		return jTextArea;
	}

	/**
	 * Botão que controla esquema de reprocessamento de carga
	 * @return JButton redoButton
	 */
	private JButton getRedoButton() {
		if (redoButton == null) {
			redoButton = new JButton();
			redoButton.setText("Reprocessar");
			redoButton.setName("redoButton");
			redoButton.setMnemonic('r');
			redoButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runnable r = new Runnable() {
						public void run() {
							openRedoDialog();
						}
					};
					Thread t = new Thread(r);
					t.setName("ReLoadPanel");
					t.start();
				}
			});
		}
		return redoButton;
	}
	
	/**
	 * Método que abre e dispara sistema de reprocessamento de cargas que retornaram erros
	 */
	private void openRedoDialog() {
		ReLoadPanel redo = new ReLoadPanel(this);
		redo.setModal(true);
		redo.setVisible(true);
	}

	/**
	 * This method initializes connectionButtonDest	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getConnectionButtonDest() {
		if (connectionButtonDest == null) {
			connectionButtonDest = new JButton();
			connectionButtonDest.setName("connectionButtonDest");
			connectionButtonDest.setText("Banco Destino -->");
			connectionButtonDest.setBounds(new Rectangle(1, 22, 99, 21));
			connectionButtonDest.setMnemonic('d');
			connectionButtonDest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					openLoginDialogDestino();
				}
			});
		}
		return connectionButtonDest;
	}

	/**
	 * This method initializes closeButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setName("closeButton");
			closeButton.setText("Sair");
			try {
			} catch (Exception e2) {
			}
			closeButton.setPreferredSize(new Dimension(49, 21));
			try {
				closeButton.setIcon(new ImageIcon(getClass().getResource(ResourceLocation.IMG_LOGO)));
			} catch (Exception e2) {
			}
			closeButton.setMnemonic('s');
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						ProcessToReLoad.persist();
						ConnectionPoolManagerOrigem.getInstance().shutdown();
						ConnectionPoolManagerDestino.getInstance().shutdown();
					} catch (SQLException e1) {
						Log.error(e1);
					}
					System.exit(0);
				}
			});
		}
		return closeButton;
	}

	/**
	 * This method initializes pnlTools	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlTools() {
		if (pnlTools == null) {
			pnlTools = new JPanel();
			pnlTools.setLayout(new BorderLayout());
			pnlTools.setName("pnlTools");
			pnlTools.add(getCloseButton(), BorderLayout.NORTH);
			pnlTools.add(getRedoButton(), BorderLayout.EAST);
		}
		return pnlTools;
	}

	/**
	 * This method initializes pnlBancos	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlBancos() {
		if (pnlBancos == null) {
			pnlBancos = new JPanel();
			pnlBancos.setPreferredSize(new Dimension(800, 45));
			pnlBancos.setLayout(null);
			pnlBancos.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			pnlBancos.setName("pnlBancos");
			pnlBancos.add(getConnectionButton(), null);
			pnlBancos.add(connectionStatusLabel, null);
			pnlBancos.add(getConnectionButtonDest(), null);
			pnlBancos.add(connectionStatusLabelDest, null);
		}
		return pnlBancos;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
