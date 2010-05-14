package org.mtec.sistemacarga.framework.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.mtec.sistemacarga.framework.util.ResourceLocation;


/**
 * Apresenta tela para o usuário selecionar o banco de dados alvo da carga e informar
 * usuário e senha que devem ser utilizados pela aplicação.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 */
@SuppressWarnings("serial")
public class LoginVisual extends JDialog {

	/**
	 * Flag que direciona qual banco deve ser utilizado
	 * TRUE  = Banco de Origem dos dados
	 * False = Banco de Destino dos dados
	 */
	private boolean direction = true;
	
	private JPanel jContentPane = null;

	private LoginComponentesPanel loginComponentesPanel = null;

	public LoginVisual(Frame owner, boolean direction) {
		super(owner);
		this.direction = direction;
		initialize();
	}

	private void initialize() {
		this.setResizable(false);
		this.setSize(new Dimension(305, 398));
		this.setTitle(ResourceLocation.TITLE_CONFIGURAR_CONEXAO);
		this.setContentPane(getJContentPane());
		setCenter();
	}

	/**
	 * Método que centraliza o painel na tela
	 */
	private void setCenter() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		int alturaTela = tk.getScreenSize().height;
		int comprimentoTela = tk.getScreenSize().width;
		
		int alturaJanela = this.getHeight();
		int comprimentoJanela = this.getWidth();
		
		this.setLocation( (comprimentoTela-comprimentoJanela)/2, (alturaTela-alturaJanela)/2 );		
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getLoginComponentesPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	private LoginComponentesPanel getLoginComponentesPanel() {
		if (loginComponentesPanel == null) {
			loginComponentesPanel = new LoginComponentesPanel(this, direction);
		}
		return loginComponentesPanel;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
