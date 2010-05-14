package org.mtec.sistemacarga.framework;

/**
 * Classe responsável por direcionar a carga para single-thread ou para multi-thread, 
 * conforme definido pelo usuário
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 15/05/2007
 */
public class LoadProcessFactory {
	
	/**
	 * Enum para possilitar a escolha pelo desenvolvedor de processos de carga por 
	 * Single Thread ou Multi Thread
	 * @author Daniel Quirino Oliveira - danielqo@gmail.com
	 * @version 1.0 - 15/05/2007
	 */
	public static enum ProcessType {SINGLE, CONCURRENT}

	/**
	 * Construtor default da classe
	 */
	private LoadProcessFactory() {
		super();
	}
	
	/**
	 * Classe que faz a seleção do tipo de processo que deve ser executado.
	 * @param type enum ProcessType {SINGLE, CONCURRENT}
	 * @return LoadProcess conforme parâmetro informado
	 */
	public static LoadProcess getLoadProcess(ProcessType type) {
		LoadProcess proc = null;
		switch(type){
			case SINGLE:
				proc = new SingleLoadProcess();
				break;
			case CONCURRENT:
				proc = new ConcurrentLoadProcess();
				break;
		}
		return proc;
	}

}
