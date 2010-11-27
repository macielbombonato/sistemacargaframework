package org.mtec.sistemacarga.framework;

import java.util.List;

import org.mtec.sistemacarga.framework.exception.SisCarException;
import org.mtec.sistemacarga.framework.util.Log;



/**
 * Processo abstrato para cria��o de classes para execu��o de processos de carga
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 05/02/2007
 */
@SuppressWarnings("unchecked")
abstract class AbstractProcess implements Runnable {
	
	/**
	 * Lista de par�metros que deve ser enviada a package de carga do banco
	 */
	private List parameters;
	
	/**
	 * Construtor da classe abstrata
	 * @param parameters ArrayList com os par�metros de entrada para execu��o
	 *                   do processo junto ao banco de dados. 
	 */
	public AbstractProcess (List parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Dispara a thread do processo de carga
	 */
	public void run() {
		try {
			exec();
		} catch (SisCarException e) {
			Log.error("Erro ao tentar disparar o m�todo de execu��o da thread");
			Log.error(e);
		}
	}
	
	/**
	 * M�todo que deve ser implementado pelo desenvolvedor de cada sistema
	 * @throws SisCarException
	 */
	public abstract void exec() throws SisCarException;
	
	/**
	 * Retorna a lista de par�metros que deve ser enviada a package de carga do banco
	 * @return parameters ArrayList com os par�metros de entrada para execu��o
	 *                    do processo junto ao banco de dados. 
	 */
	protected List getParameters(){
		return parameters;
	}
}
