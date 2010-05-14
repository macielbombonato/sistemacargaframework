package org.mtec.sistemacarga.framework;

import java.util.List;

/**
 * Interface que possibilita que o sistema tenha implementações para disparar
 * processos de carga com concorrência ou sem.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author MAciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 15/05/2007
 */
public interface LoadProcess {

	/**
	 * Método responsável pela execução de processo onde no caso de ser uma storedProcedure
	 * o padrão adotado será o da TAM (com os parâmetros de retorno cdSistema, cdProcesso, 
	 * cdRetorno e dsMensagem).
	 * @param nomePai String com o nome do processo que deve ser executado antes de 
	 * iniciar este processo de carga
	 * @param nomeProcesso String que define o nome deste processo de carga
	 * @param storedProc String com o nome da storedProcedure (ou comandos DML).
	 * @param params ArrayList com os parâmetros para preenchimento das bind variables
	 * do processo indicado no parâmetro storedProc
	 */
	public void doLoad(String nomePai, String nomeProcesso,
			String storedProc, List params);

	/**
	 * Método responsável pela execução de processo onde no caso de ser uma storedProcedure
	 * o padrão pode ser definido pelo desenvolvedor preenchendo o campo padraoGenerico 
     * @param nomePai String com o nome do processo que deve ser executado antes de 
	 * iniciar este processo de carga
	 * @param nomeProcesso String que define o nome deste processo de carga
	 * @param storedProc String com o nome da storedProcedure (ou comandos DML).
	 * @param params ArrayList com os parâmetros para preenchimento das bind variables
	 * do processo indicado no parâmetro storedProc
	 * @param padraoGenerico boolean<br>
	 * TRUE - StoredProcedure fora do padrão TAM (quantidade de parâmetros igual ao informado no 
	 * campo params)<br>
	 * FALSE - StoredProcedure no padrão TAM de desenvolvimento onde são inseridos os parâmetros
	 * cdSistema, cdProcesso, cdRetorno e dsMensagem no final da lista e o retorno é tratado 
	 * conforme informado no parâmetro cdRetorno
	 */
	public void doLoad(String nomePai, String nomeProcesso,
			String storedProcedure, List parametros, boolean padraoGenerico);

	/**
	 * Método que possibilita que a aplicação aguarde pelo termino de todos processos de carga
	 * que ainda estão em andamento antes que os processos de conclusão do processo de carga
	 * sejam executadas.
	 * @throws InterruptedException
	 */
	public void waitForFinalization() throws InterruptedException;

}