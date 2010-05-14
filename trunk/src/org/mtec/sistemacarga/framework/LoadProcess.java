package org.mtec.sistemacarga.framework;

import java.util.List;

/**
 * Interface que possibilita que o sistema tenha implementa��es para disparar
 * processos de carga com concorr�ncia ou sem.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author MAciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 15/05/2007
 */
public interface LoadProcess {

	/**
	 * M�todo respons�vel pela execu��o de processo onde no caso de ser uma storedProcedure
	 * o padr�o adotado ser� o da TAM (com os par�metros de retorno cdSistema, cdProcesso, 
	 * cdRetorno e dsMensagem).
	 * @param nomePai String com o nome do processo que deve ser executado antes de 
	 * iniciar este processo de carga
	 * @param nomeProcesso String que define o nome deste processo de carga
	 * @param storedProc String com o nome da storedProcedure (ou comandos DML).
	 * @param params ArrayList com os par�metros para preenchimento das bind variables
	 * do processo indicado no par�metro storedProc
	 */
	public void doLoad(String nomePai, String nomeProcesso,
			String storedProc, List params);

	/**
	 * M�todo respons�vel pela execu��o de processo onde no caso de ser uma storedProcedure
	 * o padr�o pode ser definido pelo desenvolvedor preenchendo o campo padraoGenerico 
     * @param nomePai String com o nome do processo que deve ser executado antes de 
	 * iniciar este processo de carga
	 * @param nomeProcesso String que define o nome deste processo de carga
	 * @param storedProc String com o nome da storedProcedure (ou comandos DML).
	 * @param params ArrayList com os par�metros para preenchimento das bind variables
	 * do processo indicado no par�metro storedProc
	 * @param padraoGenerico boolean<br>
	 * TRUE - StoredProcedure fora do padr�o TAM (quantidade de par�metros igual ao informado no 
	 * campo params)<br>
	 * FALSE - StoredProcedure no padr�o TAM de desenvolvimento onde s�o inseridos os par�metros
	 * cdSistema, cdProcesso, cdRetorno e dsMensagem no final da lista e o retorno � tratado 
	 * conforme informado no par�metro cdRetorno
	 */
	public void doLoad(String nomePai, String nomeProcesso,
			String storedProcedure, List parametros, boolean padraoGenerico);

	/**
	 * M�todo que possibilita que a aplica��o aguarde pelo termino de todos processos de carga
	 * que ainda est�o em andamento antes que os processos de conclus�o do processo de carga
	 * sejam executadas.
	 * @throws InterruptedException
	 */
	public void waitForFinalization() throws InterruptedException;

}