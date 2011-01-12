package org.mtec.sistemacarga.framework;

import org.mtec.sistemacarga.framework.service.AplicacaoCargaService;

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
	 * @param args String[] -> utilizados para aplicacao batch
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		AplicacaoCargaService appCarga = new AplicacaoCargaService();
		
		if (args == null || args.length == 0) {
			appCarga.executaAplicacaoVisual();
		} else {
			appCarga.executaAplicacaoBatch(args);
		}
	}
}
