package org.mtec.sistemacarga.framework;

import java.util.ArrayList;
import java.util.List;

import org.mtec.sistemacarga.framework.util.Log;



/**
 * Classe de controle de processos concorrentes.
 * A esta classe cabe o controle do pool de threads e de hierarquia de execução de 
 * processos de carga.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 * @version 1.1 - 29/10/2007 - Realizados ajustes no monitoramento de threads (inclusão de bloco sincronizado).
 */
@SuppressWarnings("unchecked")
final class ConcurrencyManager {
	
	private static int countThreads = 0;
	
	/**
	 * Pool de threads da aplicação
	 */
	public static Thread[] t = new Thread[5];
	
	/**
	 * Pool de threads de monitoramento, normalmente utilizadas em situações onde
	 * existam objetos com depêndias, objetos filhos.
	 */
	public static Thread[] monitor = new Thread[5];
	
	/**
	 * String que define o nome do objeto pai que deve ser executado antes que o
	 * processo atual seja iniciado.
	 */
	public static String nomePai = "";
	
	/**
	 * Nome do processo que será executado pelo processo.
	 */
	public static String nomeProcesso = "";
	
	/**
	 * Nome da stored procedure que será executada no banco de dados para 
	 * realizar a carga do banco de desenvolvimento
	 */
	public static String storedProcedure = "";
	
	/**
	 * Lista de parâmetros da stored procedure
	 */
	public static List parametros;
	
	/**
	 * Construtor padrão da classe.
	 * Carrega a lista de instruções SQL de cursores.
	 */
	public ConcurrencyManager() {
	}
	
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
	public void doLoad(String nomePai, String nomeProcesso, String storedProcedure, List parametros) {
		//
		ConcurrencyManager.nomePai         = nomePai;
		ConcurrencyManager.nomeProcesso    = nomeProcesso;
		ConcurrencyManager.storedProcedure = storedProcedure;
		ConcurrencyManager.parametros      = parametros;
		//
		realizaLimpezadeMemoria();
		//
		try {
			DBCommandExecutor process = new DBCommandExecutor(storedProcedure, parametros); 
			if ((nomePai == null) || (nomePai.equalsIgnoreCase(""))){
				criaThread(nomeProcesso, process);
			} else {
				try {
					criaMonitordeThread(nomePai);
				} catch (Exception e) {
					Log.error("Erro ao tentar criar a thread de monitoramento " + "Monitor-" + nomePai);
					Log.error(e);
				}
			}
		} catch (Exception e) {
			Log.error("Erro ao tentar fazer a reflexão do processo: " + storedProcedure);
			Log.error(e);
		}
	}

	private void criaMonitordeThread(String nomePai) throws InterruptedException {
		int i = 0;
		while(true) {
			if (i >= (monitor.length)) {
				i = 0;
				Thread.sleep(1);
			} else if (monitor[i] == null || !threadAtiva(t[i])) {
				monitor[i] = new Thread(new CargaFilhos());
				monitor[i].setName("Monitor-" + nomePai);
				monitor[i].start();
				i++;
				break;
			} else {
				i++;
			}
		}
	}

	private void criaThread(String nomeProcesso, DBCommandExecutor process) {
		try {
			int i = 0;
			while(true) {
				if (i >= (t.length)) {
					i = 0;
					Thread.sleep(1);
				} else if (t[i] == null || !threadAtiva(t[i])) {
					t[i] = new Thread(process);
					t[i].setName(nomeProcesso);
					t[i].start();
					i++;
					countThreads++;
					break;
				} else {
					i++;
				}
			}
		} catch (Exception e) {
			Log.error("Erro ao tentar criar a thread " + nomeProcesso);
			Log.error(e);
		}
	}

	private void realizaLimpezadeMemoria() {
		if (countThreads == 2500 || countThreads == 0) {
			countThreads = 0;
			System.gc();
		}
	}
	
	/**
	 * Bloco sincronizado que informa se a thread está ativa.
	 * @param thread
	 * @return
	 */
	public static boolean threadAtiva(Thread thread) {
		if (thread != null) {
			synchronized (thread) {
				if (thread != null && !thread.isAlive()) {
					return false;
				} else {
					return true;
				}
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Método responsável por disparar um processo de carga junto ao banco.
	 * Neste método é definido se um objeto possui dependências ou se pode
	 * ser executado diretamente.
	 * @param nomePai String que identifica o nome do objeto pai que deve ter sua execução concluída antes do inicio deste.
	 * @param nomeProcesso String que identifica o processo para eventual monitoramento.
	 * @param storedProcedure String que identifica a storedprocedure do banco de dados que deve ser executada.
	 * @param parametros ArrayList com objetos para preenchimento das bind variables da storedProcedure (parâmetros de entrada).
	 * @param padraoGenerico Flag que indica de a storedProcedure está em um padrão de parâmetros genérico ou no padrão TAM<br>
	 *        TRUE - Padrão genérico<br>
	 *        FALSE - Padrão TAM com os quatro parâmetros de retorno.
	 */
	public void doLoad(String nomePai, String nomeProcesso, String storedProcedure, List parametros, boolean padraoGenerico) {
		//
		ConcurrencyManager.nomePai         = nomePai;
		ConcurrencyManager.nomeProcesso    = nomeProcesso;
		ConcurrencyManager.storedProcedure = storedProcedure;
		ConcurrencyManager.parametros      = parametros;
		//
		try {
			DBCommandExecutor process = new DBCommandExecutor(storedProcedure, parametros, padraoGenerico); 
			if ((nomePai == null) || (nomePai.equalsIgnoreCase(""))){
				criaThread(nomeProcesso, process);
			} else {
				criaMonitordeThread(nomePai);
			}
		} catch (Exception e) {
			Log.error("Erro ao tentar fazer a reflexão do processo: " + storedProcedure);
			Log.error(e);
		}
	}
}
/**
 * Classe responsável por realizar o monitoramento se o objeto pai
 * ainda está em execução e ao fim do processo pai, disparar o processo
 * filho para a realização da carga.
 * @author Maciel Escudero Bombonato
 * @version 1.0 - 25/04/2007
 */
class CargaFilhos implements Runnable {
	
	/**
	 * Método padrão o objeto Runnable para disparar uma nova thread.
	 * Realiza o monitoramento do objeto pai e ao término do mesmo, 
	 * inicia a carga do objeto filho.
	 */
	@SuppressWarnings("unchecked")
	public void run() {
		String nomePai = new String(ConcurrencyManager.nomePai);
		String nomeProcesso = new String(ConcurrencyManager.nomeProcesso);
		String storedProcedure = new String(ConcurrencyManager.storedProcedure);
		List parametros = new ArrayList(ConcurrencyManager.parametros);

		for (int j = 0; j < ConcurrencyManager.t.length; j++) {
			if ((ConcurrencyManager.t[j] != null) && 
			    (ConcurrencyManager.threadAtiva(ConcurrencyManager.t[j])) && 
			    (ConcurrencyManager.t[j].getName().equalsIgnoreCase(nomePai)) ) {
				//
				j = -1;
				try {
					Thread.sleep(1);
				} catch (Exception e) {
					Log.error("Erro ao tentar executar o comando sleep da thread " + nomePai);
					Log.error(e);
				}
				//   
			}
		}
		//
		try {
			int i = 0;
			while(true) {
				try {
					if (i >= (ConcurrencyManager.t.length)) {
						i = 0;
						Thread.sleep(1);
					} else if (ConcurrencyManager.t[i] == null || !ConcurrencyManager.threadAtiva(ConcurrencyManager.t[i])) {
						ConcurrencyManager.t[i] = new Thread(new DBCommandExecutor(storedProcedure, parametros));
						ConcurrencyManager.t[i].setName(nomeProcesso);
						ConcurrencyManager.t[i].start();
						i++;
						break;
					} else {
						i++;
					}
				} catch (Exception e) {
					System.gc();
					ConcurrencyManager.t[i] = new Thread(new DBCommandExecutor(storedProcedure, parametros));
					ConcurrencyManager.t[i].setName(nomeProcesso);
					ConcurrencyManager.t[i].start();
					i++;
					break;
				}
			}
		} catch (Exception e) {
			Log.error("Erro durante a criação da thread: " + ConcurrencyManager.nomeProcesso);
			Log.error(e);
		}
		//
	}
}
