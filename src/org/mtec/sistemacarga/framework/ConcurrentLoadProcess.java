package org.mtec.sistemacarga.framework;

import java.util.List;

/**
 * Classe que implementa a interface LoadProcess e destina os processos de carga
 * para uma execução com concorrencia (multi-threads).
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 15/05/2007
 */
class ConcurrentLoadProcess implements LoadProcess {

	/**
	 * Instancia objeto que controle cargas com concorrencia
	 */
	private ConcurrencyManager loader = new ConcurrencyManager();

	/**
	 * @see org.mtec.sistemacarga.framework.LoadProcess#doLoad(java.lang.String, java.lang.String, java.lang.String, java.util.ArrayList)
	 */
	public void doLoad(String nomePai, String nomeProcesso, String storedProc, List params) {
		loader.doLoad(nomePai, nomeProcesso, storedProc, params);
	}

	/**
	 * @see org.mtec.sistemacarga.framework.LoadProcess#doLoad(java.lang.String, java.lang.String, java.lang.String, java.util.List, boolean)
	 */
	public void doLoad(String nomePai, String nomeProcesso, String storedProc, List params, boolean padraoGenerico) {
		loader.doLoad(nomePai, nomeProcesso, storedProc, params, padraoGenerico);
	}
	
	/** 
	 * Método responsável por fazer com que a aplicação aguarde o termino de todos processos de carga
	 * antes de disparar métodos de encerramento
	 */
	public void waitForFinalization() throws InterruptedException {
		for (int i = 0; i < ConcurrencyManager.t.length; i++) {
			if ((ConcurrencyManager.t[i] != null) && (ConcurrencyManager.t[i].isAlive())) {
				i = 0;
				Thread.sleep(1000);
			}
		}
	}
}
