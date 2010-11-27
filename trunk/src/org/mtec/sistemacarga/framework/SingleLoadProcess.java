package org.mtec.sistemacarga.framework;

import java.util.List;

/**
 * Classe responsável por executar o processo de carga de forma sequenciada, 
 * sem a utilização de multi threads
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
	 * Método responsável por disparar um processo de carga junto ao banco.
	 * Neste método é definido se um objeto possui dependências ou se pode
	 * ser executado diretamente.
	 * @param nomePai String que identifica o nome do objeto pai que deve ter 
	 * sua execução concluída antes do inicio deste.
	 * @param nomeProcesso String que identifica o processo para eventual monitoramento.
	 * @param storedProcedure String que identifica a storedprocedure do 
	 * banco de dados que deve ser executada.
	 * @param parametros ArrayList com objetos para preenchimento das bind 
	 * variables da storedProcedure (parâmetros de entrada).
	 */
	public void doLoad(String nomePai, String nomeProcesso, String storedProc,
			List params) {
		process = new DBCommandExecutor(storedProc, params);
		process.run();
	}

	/**
	 * Método responsável por disparar um processo de carga junto ao banco.
	 * Neste método é definido se um objeto possui dependências ou se pode
	 * ser executado diretamente.
	 * @param nomePai String que identifica o nome do objeto pai que deve ter sua execução 
	 * concluída antes do inicio deste.
	 * @param nomeProcesso String que identifica o processo para eventual monitoramento.
	 * @param storedProcedure String que identifica a storedprocedure do banco de dados que 
	 * deve ser executada.
	 * @param parametros ArrayList com objetos para preenchimento das bind variables da 
	 * storedProcedure (parâmetros de entrada).
	 * @param padraoGenerico Flag que indica de a storedProcedure está em um padrão de 
	 * parâmetros genérico ou no padrão TAM<br>
	 *        TRUE - Padrão genérico<br>
	 *        FALSE - Padrão TAM com os quatro parâmetros de retorno.
	 */
	public void doLoad(String nomePai, String nomeProcesso, String storedProc,
			List params, boolean padraoGenerico) {
		process = new DBCommandExecutor(storedProc, params, padraoGenerico);
		process.run();
	}

	/**
	 * Quando o processo de carga está sendo disparado de forma single thread
	 * este método não possui funcionalidade.
	 */
	public void waitForFinalization() throws InterruptedException {
		return;
	}

}
