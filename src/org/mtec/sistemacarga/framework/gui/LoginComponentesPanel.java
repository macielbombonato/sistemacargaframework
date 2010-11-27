package org.mtec.sistemacarga.framework.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.mtec.sistemacarga.framework.dao.ConnectionPoolManagerDestino;
import org.mtec.sistemacarga.framework.dao.ConnectionPoolManagerOrigem;
import org.mtec.sistemacarga.framework.exception.SisCarException;
import org.mtec.sistemacarga.framework.util.Log;
import org.mtec.sistemacarga.framework.util.OraUtil;
import org.mtec.sistemacarga.framework.util.ResourceLocation;
import org.mtec.sistemacarga.framework.util.ResourceUtil;



/**
 * <strong><h1>Definição</h1></strong>
 * <br/>
 * <br/>
 * O objetivo desta classe é apresentar uma tela amigável para o usuário de 
 * forma que possa selecionar o alias do banco de dados desejado para
 * logar no sistema. Esta <i>GUI (Graphics User Interface)</i> é desenvolvida
 * utilizando APIs de Swing e AWT.
 * @author Antonio Cesar
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 14/02/2007
 * @version 1.1 - 27/09/2007 - Alteração para suportar bancos de origem e destino
 * @version 1.2 - 16/10/2007 - Inclusão de ícones nos botões e tratamento de erro caso os ícones sejam retirados do jar.
 * @version 1.3 - 05/12/2007 - Localidade de ícones colocadas em constante em ResourceLocation
 */
@SuppressWarnings("serial")
class LoginComponentesPanel extends JPanel {

	/**
	 * Flag que direciona qual banco deve ser utilizado
	 * TRUE  = Banco de Origem dos dados
	 * False = Banco de Destino dos dados
	 */
	private boolean direction = true;
	
	/**
	 * Componentes visuais para labels (rótulos de texto)
	 */
	private JLabel lbUsuario;
	private JLabel lbSenha;
	private JLabel lbBanco;
	private JPanel panelCentral;
	private JPanel panelBotoes;
	private JScrollPane scrollListaBancos;
	public String valueAlias;
	
	/**
	 * Componentes visuais para entrada de texto
	 */
	private JTextField tfUsuario;
	
	/**
	 * Componentes visuais para entrada de texto seguro
	 */
	private JPasswordField pfSenha;
	
	/**
	 * Componentes visuais para listagem de dados
	 */
	private JList listaBancos;
	
	/**
	 * Componente não visual para representar os dados exibidos
	 * pela lista. Esta combinação de componentes representa
	 * o modelo MVC.
	 */
	private DefaultListModel model;
	
	/**
	 * Componentes visuais para botões de ação.
	 */
	private JButton btLogar;
	private JButton btSair;
	
	/**
	 * Map contendo os mapeamentos de chave/valor para
	 * o alias/caminho do banco descritos no arquivo 
	 * TNSNAMES.ORA
	 */
	@SuppressWarnings("unchecked")
	private Map mapAliasBanco;

	/**
	 * Atributo valorizado com o usuario cadastrado no arquivo database.properties
	 */
	private String userProperties;  //  @jve:decl-index=0:
	
	/**
	 * Atributo valorizado com a senha cadastrada no arquivo database.properties
	 */
	private String passProperties;  //  @jve:decl-index=0:
	
	/**
	 * Atributo valorizado com a url do banco cadastrado no arquivo database.properties
	 */
	private String urlProperties;  //  @jve:decl-index=0:
	
	/**
	 * Mantêm referência com a janela pai para poder fechá-la. 
	 */
	private JDialog parent;

	private JPanel pnlChooseDataBase = null;

	private JCheckBox checkOracle = null;

	private JCheckBox checkAccess = null;

	private JLabel lblSep01 = null;

	private JPanel pnlPath = null;

	private JLabel lblPath = null;

	private JTextField txtPath = null;

	/**
	 * Construtor default
	 *
	 */
	public LoginComponentesPanel(JDialog parent, boolean direction) {
		this.parent = parent;
		this.direction = direction;
		initComponentes();
		addComponentesTela();
		addEventosBotoes();
		initialize();
	}
		
	
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setLayout(null);
        this.setSize(new Dimension(297, 360));
        this.add(panelCentral, null);
        this.add(panelBotoes, null);
        this.add(panelCentral, null);
        this.add(panelBotoes, null);
	}

	/**
	 * Este metodo deve criar os listener para os botões, 
	 * implementando suas respectivas funcionalidades
	 */
	private void addEventosBotoes() {
		btLogar.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvaConfiguracoes();
			}
		});
		
		btSair.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
	}
	
	private void close() {
		parent.dispose();
	}
	
	/**
	 * O objetivo deste método é popular o componente JList
	 * com os alias recuperados através da leitura do arquivo
	 * TNSNAMES.ORA
	 */
	@SuppressWarnings("unchecked")
	private void populaListaBancos() {
		/*
		 * Primeiro vai tentar recuperar o map lendo o arquivo de propriedades
		 */
		try {
			//
			if (direction) {
				ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_DRIVER_ORIGEM, ResourceLocation.STRING_DRIVER_ORACLE);
			} else {
				ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_DRIVER_DESTINO, ResourceLocation.STRING_DRIVER_ORACLE);
			}
			//
			String tnsNamesPath = ResourceLocation.DATABASE_TNSNAMES_DATABASE_PATH; 
			if (tnsNamesPath.indexOf(".ora") >= 0) { 
				mapAliasBanco = OraUtil.getInstance().getAliasMap( tnsNamesPath );
			} else {
				throw new SisCarException("Arquivo encontrado não corresponde ao TNSNames.ora");
			}
		} catch (SisCarException e) {
			Log.error("Ocorreu um erro ao tentar ler o map de alias do modo tradicional! " +
					  "O sistema tentará obter o caminho do arquivo com o usuário...");
		}
		/*
		 * Caso a tentativa anterior não tenha sido bem sucedida o sistema
		 * devera ignorar o caminho no arquivo de propriedades e tentar 
		 * localizar o arquivo num local informado pelo usuário.
		 * Pelo fato de que o arquivo apontado pelo usuario possa não estar correto,
		 * esta rotina deve ser processado num loop. As condicoes de parada sao: 
		 * 1) Quando o objeto Map for processado (diferente de null, portanto) ou
		 * 2) Cancelado pelo usuario. Neste caso, o sistema será encerrado. 
		 */
		while (mapAliasBanco == null) {
			JOptionPane.showMessageDialog(null,
										 "Ocorreu um erro com o arquivo no local informado!\n" +
										 "Por favor, selecione o arquivo TNSNAMES.ORA na caixa de diálogo!",
										 "INFORMAÇÃO",
										 JOptionPane.INFORMATION_MESSAGE);
			
			JFileChooser chooser = new JFileChooser(ResourceLocation.DATABASE_TNSNAMES_DATABASE_PATH);
			// TODO: Acertar o esquema de filtro de arquivo
//			FileFilter fileFilter = new FileFilter();  //"ORA File", "ora"); 
//			chooser.setFileFilter(fileFilter);    //addChoosableFileFilter(filter);
			int opcao = chooser.showOpenDialog( this );
			if(opcao == JFileChooser.APPROVE_OPTION) {
				try {
					getTxtPath().setText(chooser.getSelectedFile().getAbsolutePath());
					ResourceLocation.DATABASE_TNSNAMES_DATABASE_PATH = chooser.getSelectedFile().getAbsolutePath();
					mapAliasBanco = OraUtil.getInstance().getAliasMap( chooser.getSelectedFile().getAbsolutePath() );
					ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_TNSNAMES_DATABASE_PATH, chooser.getSelectedFile().getAbsolutePath());
				} catch (SisCarException e) {
					Log.error("Ocorreu um erro novamente ao tentar ler o map de alias do modo alternativo! " +
							  "O sistema tentará obter o caminho do arquivo com o usuário novamente...");
					opcao = JOptionPane.showConfirmDialog(null, 
														 "Ocorreu um erro com o arquivo no local informado!\nDeseja tentar novamente ?", 
														 "CONFIRMAÇÃO",
														 JOptionPane.YES_NO_OPTION,
														 JOptionPane.QUESTION_MESSAGE);
					if (opcao == JOptionPane.NO_OPTION) {
						JOptionPane.showMessageDialog(null,
													 "O sistema está sendo encerrado pelo usuário!",
													 "INFORMAÇÃO",
													 JOptionPane.INFORMATION_MESSAGE);
						System.exit( 0 );
					}
				}
			}
		}
		//
		if (direction) {
			userProperties = ResourceLocation.DATABASE_USER_ORIGEM;
			passProperties = ResourceLocation.DATABASE_PASS_ORIGEM;
			urlProperties  = ResourceLocation.DATABASE_URL_ORIGEM;
		} else {
			userProperties = ResourceLocation.DATABASE_USER_DESTINO;
			passProperties = ResourceLocation.DATABASE_PASS_DESTINO;
			urlProperties  = ResourceLocation.DATABASE_URL_DESTINO;
		}
		//
		Set keys = mapAliasBanco.keySet();
		Iterator it = keys.iterator();
		String keyToSelect = null;
		Object o = null;
		while (it.hasNext()) {
			o = it.next();
			model.addElement( o );
			if ( urlProperties.equals((String) mapAliasBanco.get(o)) ) {
				keyToSelect = o.toString();
			}
		}
		//
		tfUsuario.setText( userProperties );
		pfSenha.setText( passProperties );
		listaBancos.setSelectedValue(keyToSelect, true);
	}

	private void addComponentesTela() {
		Box boxComponentesEsquerda = Box.createVerticalBox();
		boxComponentesEsquerda.setBounds(new Rectangle(17, 7, 258, 70));
		boxComponentesEsquerda.add( lbUsuario );
		boxComponentesEsquerda.add( tfUsuario );
		boxComponentesEsquerda.add( lbSenha );
		boxComponentesEsquerda.add( pfSenha );
		//
		Box boxComponentesDireita  = Box.createVerticalBox();
		boxComponentesDireita.setBounds(new Rectangle(14, 171, 258, 144));
		boxComponentesDireita.add( lbBanco );
		//
		scrollListaBancos = new JScrollPane(listaBancos);
		//
		boxComponentesDireita.add( scrollListaBancos );
		//
		panelCentral = new JPanel(new FlowLayout());
		panelCentral.setLayout(null);
		panelCentral.setBounds(new Rectangle(3, 2, 292, 317));
		panelCentral.add(boxComponentesEsquerda, null);
		panelCentral.add(boxComponentesDireita, null);
		panelCentral.add(getPnlChooseDataBase(), null);
		panelCentral.add(getPnlPath(), null);
		panelBotoes = new JPanel(new FlowLayout());
		panelBotoes.setBounds(new Rectangle(2, 323, 293, 31));
		panelBotoes.add( btLogar );
		panelBotoes.add( btSair );
		//
		setLayout( new BorderLayout() );
		this.add( panelCentral, BorderLayout.CENTER );
		this.add( panelBotoes, BorderLayout.SOUTH );
	}

	private void initComponentes() {
		lbUsuario   = new JLabel("Usuário");
		lbSenha     = new JLabel("Senha");
		lbBanco     = new JLabel("Alias Banco Oracle");
		
		tfUsuario   = new JTextField("", 10);
		pfSenha     = new JPasswordField("", 10);
		
		model       = new DefaultListModel();
		listaBancos = new JList( model );
		listaBancos.setSelectedIndex(0);
		
		btLogar     = new JButton("Logar");
		btSair      = new JButton("Sair");
		btLogar.setMnemonic('l');
		btLogar.setPreferredSize(new Dimension(67, 22));
		btSair.setMnemonic('s');
		try {
			btLogar.setIcon(new ImageIcon(getClass().getResource(ResourceLocation.IMG_NEXT_LITTLE)));
			btSair.setIcon(new ImageIcon(getClass().getResource(ResourceLocation.IMG_LOGO)));
		} catch (Exception e) {
		}
	}

	/**
	 * Salva as configurações com dados fornecidos pelo usuário.
	 */
	private void salvaConfiguracoes() {
		String senha = "";
		
		if (!checkAccess.isSelected()) {
			if (tfUsuario.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(null, 
											 "Você deve informar o usuário de banco!", 
											 "ATENÇÃO", 
											 JOptionPane.INFORMATION_MESSAGE);
				tfUsuario.requestFocus();
				return;
			}
			senha = new String(pfSenha.getPassword() );
			senha = senha.trim();
			if (senha.length() == 0) {
				JOptionPane.showMessageDialog(null, 
											 "Você deve informar a senha do usuário de banco!", 
											 "ATENÇÃO", 
											 JOptionPane.INFORMATION_MESSAGE);
				pfSenha.requestFocus();
				return;
			}
			if (checkOracle.isSelected()) {
				if (listaBancos.getSelectedIndex() < 0) {
					JOptionPane.showMessageDialog(null, 
												 "Você deve selecionar um banco!", 
												 "ATENÇÃO", 
												 JOptionPane.INFORMATION_MESSAGE);
					listaBancos.requestFocus();
					return;
				}
				//
				String keyAlias   = model.get( listaBancos.getSelectedIndex() ).toString();
				valueAlias = mapAliasBanco.get( keyAlias ).toString();
			}
		}
		//
		boolean connOk = false;
		//
		connOk = checkConnectionStatus(senha, valueAlias, direction);
		//
		if (connOk) {
			if (direction) {
				if (!tfUsuario.getText().equalsIgnoreCase("")) {
					if (userProperties != null && !userProperties.equals(tfUsuario.getText()) ) {
						ResourceLocation.DATABASE_USER_ORIGEM = tfUsuario.getText().trim();
						ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_USER_ORIGEM, tfUsuario.getText().trim());
					} else {
						ResourceLocation.DATABASE_USER_ORIGEM = tfUsuario.getText().trim();
						ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_USER_ORIGEM, tfUsuario.getText().trim());
					}
				} else {
					ResourceLocation.DATABASE_USER_ORIGEM = "";
					ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_USER_ORIGEM, "_");
				}
				if (!senha.equalsIgnoreCase("")) {
					if (passProperties != null && !passProperties.equals(senha) ) {
						ResourceLocation.DATABASE_PASS_ORIGEM = senha;
						ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_PASS_ORIGEM, senha);
					} else {
						ResourceLocation.DATABASE_PASS_ORIGEM = senha;
						ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_PASS_ORIGEM, senha);
					}
				} else {
					ResourceLocation.DATABASE_PASS_ORIGEM = "";
					ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_PASS_ORIGEM, "_");
				}
				if (urlProperties == null || !urlProperties.equals(valueAlias) ) {
					ResourceLocation.DATABASE_URL_ORIGEM = valueAlias;
					ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_URL_ORIGEM, valueAlias);
				}
			} else {
				if (!tfUsuario.getText().equalsIgnoreCase("")) {
					if (userProperties != null && !userProperties.equals(tfUsuario.getText()) ) {
						ResourceLocation.DATABASE_USER_DESTINO = tfUsuario.getText().trim();
						ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_USER_DESTINO, tfUsuario.getText().trim());
					} else {
						ResourceLocation.DATABASE_USER_DESTINO = tfUsuario.getText().trim();
						ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_USER_DESTINO, tfUsuario.getText().trim());
					}
				} else {
					ResourceLocation.DATABASE_USER_DESTINO = "";
					ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_USER_DESTINO, "_");
				}
				if (!senha.equalsIgnoreCase("")) {
					if (passProperties != null && !passProperties.equals(senha) ) {
						ResourceLocation.DATABASE_PASS_DESTINO = senha;
						ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_PASS_DESTINO, senha);
					} else {
						ResourceLocation.DATABASE_PASS_DESTINO = senha;
						ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_PASS_DESTINO, senha);
					}
				} else {
					ResourceLocation.DATABASE_PASS_DESTINO = "";
					ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_PASS_DESTINO, "_");
				}
				if (urlProperties == null || !urlProperties.equals(valueAlias) ) {
					ResourceLocation.DATABASE_URL_DESTINO = valueAlias;
					ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_URL_DESTINO, valueAlias);
				}
			}
			
			JOptionPane.showMessageDialog(null, 
										 "Informações registradas com sucesso!", 
										 "INFORMAÇÃO", 
										 JOptionPane.INFORMATION_MESSAGE);
			//
			close();
			//
		}
	}

	/**
	 * Verifica se é possível realizar conexão com o banco de dados utilizando o usuário, senha e o alias selecionado pelo usuário
	 * @param senha String 
	 * @param valueAlias String 
	 * @return boolean connOk
	 *         TRUE  - Conexão realizada com sucesso
	 *         FALSE - Não foi possível realizar conexão com o banco utilizando os dados informados.
	 */
	private boolean checkConnectionStatus(String senha, String valueAlias, boolean direction) {
		boolean connOk = false;
		String dataBaseDriver = "";
		if (checkAccess.isSelected()) {
			dataBaseDriver = ResourceLocation.STRING_DRIVER_MSACCESS;
		} else if (checkOracle.isSelected()) {
			dataBaseDriver = ResourceLocation.STRING_DRIVER_ORACLE;
		} 
		if (direction) {
			connOk = ConnectionPoolManagerOrigem.getInstance().testConnection(dataBaseDriver, valueAlias, tfUsuario.getText().trim(), senha);
		} else {
			connOk = ConnectionPoolManagerDestino.getInstance().testConnection(dataBaseDriver, valueAlias, tfUsuario.getText().trim(), senha);
		}
		if (!connOk) {
			JOptionPane.showMessageDialog(null, 
						 "Não foi possivel realizar conexão com o banco selecionado!", 
						 "ERRO", 
						 JOptionPane.ERROR_MESSAGE);
		}
		return connOk;
	}


	/**
	 * This method initializes pnlChooseDataBase	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlChooseDataBase() {
		if (pnlChooseDataBase == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			lblSep01 = new JLabel();
			lblSep01.setText("     ");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 2;
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			pnlChooseDataBase = new JPanel();
			pnlChooseDataBase.setLayout(new GridBagLayout());
			pnlChooseDataBase.setBounds(new Rectangle(17, 83, 257, 37));
			pnlChooseDataBase.add(getCheckOracle(), gridBagConstraints2);
			pnlChooseDataBase.add(getCheckAccess(), gridBagConstraints3);
			pnlChooseDataBase.add(lblSep01, gridBagConstraints);
		}
		return pnlChooseDataBase;
	}


	/**
	 * This method initializes checkOracle	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCheckOracle() {
		if (checkOracle == null) {
			checkOracle = new JCheckBox();
			checkOracle.setText("Oracle");
			checkOracle.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					getTxtPath().setText(ResourceLocation.DATABASE_TNSNAMES_DATABASE_PATH);
					if (checkOracle.isSelected()) {
						ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_DRIVER_ORIGEM, ResourceLocation.STRING_DRIVER_ORACLE);
						checkAccess.setSelected(false);
						if (model.getSize() > 0) {
							model.removeAllElements();
						}
						populaListaBancos();
					} else {
						if (model.getSize() > 0) {
							model.removeAllElements();
						}
					}
				}
			});
		}
		return checkOracle;
	}


	/**
	 * This method initializes checkAccess	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCheckAccess() {
		if (checkAccess == null) {
			checkAccess = new JCheckBox();
			checkAccess.setText("MS-Access");
			final LoginComponentesPanel telaLogin = this;
			checkAccess.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (checkAccess.isSelected()) {
						checkOracle.setSelected(false);
						if (model.getSize() > 0) {
							model.removeAllElements();
						}
						if (direction) {
							JFileChooser chooser = new JFileChooser(ResourceLocation.DATABASE_TNSNAMES_DATABASE_PATH);
							// TODO: Acertar o esquema de filtro de arquivo
//							FileNameExtensionFilter filter = new FileNameExtensionFilter("Microsoft Access Database", "mdb");
//							chooser.addChoosableFileFilter(filter);
							int opcao = chooser.showOpenDialog( telaLogin );
							if(opcao == JFileChooser.APPROVE_OPTION) {
								getTxtPath().setText(chooser.getSelectedFile().getAbsolutePath());
								ResourceLocation.DATABASE_TNSNAMES_DATABASE_PATH = chooser.getSelectedFile().getAbsolutePath();
								ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_DRIVER_ORIGEM, ResourceLocation.STRING_DRIVER_MSACCESS);
								ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_TNSNAMES_DATABASE_PATH, chooser.getSelectedFile().getAbsolutePath());
								valueAlias = ResourceLocation.STRING_URL_MSACCESS + ResourceLocation.DATABASE_TNSNAMES_DATABASE_PATH;
							} else {
								return;
							}
							//
							
						} else {
							JFileChooser chooser = new JFileChooser(ResourceLocation.DATABASE_TNSNAMES_DATABASE_PATH);
							// TODO: Acertar o esquema de filtro de arquivo
//							FileNameExtensionFilter filter = new FileNameExtensionFilter("Microsoft Access Database", "mdb");
//							chooser.addChoosableFileFilter(filter);
							int opcao = chooser.showOpenDialog( telaLogin );
							if(opcao == JFileChooser.APPROVE_OPTION) {
								getTxtPath().setText(chooser.getSelectedFile().getAbsolutePath());
								ResourceLocation.DATABASE_TNSNAMES_DATABASE_PATH = chooser.getSelectedFile().getAbsolutePath();
								ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_DRIVER_DESTINO, ResourceLocation.STRING_DRIVER_MSACCESS);
								ResourceUtil.getInstance().setProperty(ResourceLocation.KEY_TNSNAMES_DATABASE_PATH, chooser.getSelectedFile().getAbsolutePath());
								valueAlias = ResourceLocation.STRING_URL_MSACCESS + ResourceLocation.DATABASE_TNSNAMES_DATABASE_PATH;
							} else {
								return;
							}
							//
						}
					} else {
						if (model.getSize() > 0) {
							model.removeAllElements();
						}
					}
				}
			});
		}
		return checkAccess;
	}


	/**
	 * This method initializes pnlPath	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlPath() {
		if (pnlPath == null) {
			lblPath = new JLabel();
			lblPath.setText("Caminho do Arquivo TNSNames ou Base de Dados");
			pnlPath = new JPanel();
			pnlPath.setLayout(new BorderLayout());
			pnlPath.setBounds(new Rectangle(16, 128, 257, 39));
			pnlPath.add(lblPath, BorderLayout.NORTH);
			pnlPath.add(getTxtPath(), BorderLayout.CENTER);
		}
		return pnlPath;
	}


	/**
	 * This method initializes txtPath	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtPath() {
		if (txtPath == null) {
			txtPath = new JTextField();
			txtPath.setEditable(false);
		}
		return txtPath;
	}
	
}  //  @jve:decl-index=0:visual-constraint="17,18"
