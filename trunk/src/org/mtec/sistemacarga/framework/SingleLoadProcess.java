package org.mtec.sistemacarga.framework;

import java.util.List;

/**
 * Classe respons�vel por executar o processo de carga de forma sequenciada, 
 * sem a utiliza��o de multi threads
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 11/05/2007
 */
@SuppressWarnings("unchecked")
class SingleLoadProcess implements LoadProcess {
	
	/**
	 * Instancia do objeto que dispara o processo de carga
	 */
	private DBCommandExecutor process = null; 

	/**
	 * M�todo respons�vel por disparar um processo de carga junto ao banco.
	 * Neste m�todo � definido se um objeto possui depend�ncias ou se pode
	 * ser executado diretamente.
	 * @param nomePai String que identifica o nome do objeto pai que deve ter 
	 * sua execu��o conclu�da antes do inicio deste.
	 * @param nomeProcesso String que identifica o processo para eventual monitoramento.
	 * @param storedProcedure String que identifica a storedprocedure do 
	 * banco de dados que deve ser executada.
	 * @param parametros ArrayList com objetos para preenchimento das bind 
	 * variables da storedProcedure (par�metros de entrada).
	 */
	public void doLoad(String nomePai, String nomeProcesso, String storedProc,
			List params) {
		process = new DBCommandExecutor(storedProc, params);
		process.run();
	}

	/**
	 * M�todo respons�vel por disparar um processo de carga junto ao banco.
	 * Neste m�todo � definido se um objeto possui depend�ncias ou se pode
	 * ser executado diretamente.
	 * @param nomePai String que identifica o nome do objeto pai que deve ter sua execu��o 
	 * conclu�da antes do inicio deste.
	 * @param nomeProcesso String que identifica o processo para eventual monitoramento.
	 * @param storedProcedure String que identifica a storedprocedure do banco de dados que 
	 * deve ser executada.
	 * @param parametros ArrayList com objetos para preenchimento das bind variables da 
	 * storedProcedure (par�metros de entrada).
	 * @param padraoGenerico Flag que indica de a storedProcedure est� em um padr�o de 
	 * par�metros gen�rico ou no padr�o TAM<br>
	 *        TRUE - Padr�o gen�rico<br>
	 *        FALSE - Padr�o TAM com os quatro par�metros de retorno.
	 */
	public void doLoad(String nomePai, String nomeProcesso, String storedProc,
			List params, boolean padraoGenerico) {
		process = new DBCommandExecutor(storedProc, params, padraoGenerico);
		process.run();
	}

	/**
	 * Quando o processo de carga est� sendo disparado de forma single thread
	 * este m�todo n�o possui funcionalidade.
	 */
	public void waitForFinalization() throws InterruptedException {
		return;
	}

}
