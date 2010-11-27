package org.mtec.sistemacarga.framework;

import java.util.ArrayList;
import java.util.List;

import org.mtec.sistemacarga.framework.util.Log;



/**
 * Classe de controle de processos concorrentes.
 * A esta classe cabe o controle do pool de threads e de hierarquia de execu��o de 
 * processos de carga.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 * @version 1.1 - 29/10/2007 - Realizados ajustes no monitoramento de threads (inclus�o de bloco sincronizado).
 */
@SuppressWarnings("unchecked")
final class ConcurrencyManager {
	
	/**
	 * Pool de threads da aplica��o
	 */
	public static Thread[] t = new Thread[10];
	/**
	 * Pool de threads de monitoramento, normalmente utilizadas em situa��es onde
	 * existam objetos com dep�ndias, objetos filhos.
	 */
	public static Thread[] monitor = new Thread[10];
	/**
	 * String que define o nome do objeto pai que deve ser executado antes que o
	 * processo atual seja iniciado.
	 */
	public static String nomePai = "";
	/**
	 * Nome do processo que ser� executado pelo processo.
	 */
	public static String nomeProcesso = "";
	/**
	 * Nome da stored procedure que ser� executada no banco de dados para 
	 * realizar a carga do banco de desenvolvimento
	 */
	public static String storedProcedure = "";
	/**
	 * Lista de par�metros da stored procedure
	 */
	public static List parametros;
	
	/**
	 * Construtor padr�o da classe.
	 * Carrega a lista de instru��es SQL de cursores.
	 */
	public ConcurrencyManager() {
	}
	
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
	public void doLoad(String nomePai, String nomeProcesso, String storedProcedure, List parametros) {
		//
		ConcurrencyManager.nomePai         = nomePai;
		ConcurrencyManager.nomeProcesso    = nomeProcesso;
		ConcurrencyManager.storedProcedure = storedProcedure;
		ConcurrencyManager.parametros      = parametros;
		//
		try {
			DBCommandExecutor process = new DBCommandExecutor(storedProcedure, parametros); 
			if ((nomePai == null) || (nomePai.equalsIgnoreCase(""))){
				try {
					for (int i = 0; i < t.length; i++) {
						if (t[i] == null) {
							t[i] = new Thread(process);
							//
							t[i].setName(nomeProcesso);
							t[i].start();
							break;
						} else if (!threadAtiva(t[i])) {
							t[i] = null;
							t[i] = new Thread(process);
							//
							t[i].setName(nomeProcesso);
							t[i].start();
							break;
						} else if (i == (t.length - 1)) {
							i = -1;
							Thread.sleep(10);
						}
					}
				} catch (Exception e) {
					Log.error("Erro ao tentar criar a thread " + nomeProcesso);
					Log.error(e);
				}
			} else {
				try {
					for (int i = 0; i < monitor.length; i++) {
						if (monitor[i] == null) {
							monitor[i] = new Thread(new CargaFilhos());
							//
							monitor[i].setName("Monitor-" + nomePai);
							monitor[i].start();
							break;
						} else if (!threadAtiva(t[i])) {
							monitor[i] = null;
							monitor[i] = new Thread(new CargaFilhos());
							//
							monitor[i].setName("Monitor-" + nomePai);
							monitor[i].start();
							break;
						} else if (i == (monitor.length - 1)) {
							i = -1;
							Thread.sleep(10);
						}
					}
				} catch (Exception e) {
					Log.error("Erro ao tentar criar a thread de monitoramento " + "Monitor-" + nomePai);
					Log.error(e);
				}
			}
		} catch (Exception e) {
			Log.error("Erro ao tentar fazer a reflex�o do processo: " + storedProcedure);
			Log.error(e);
		}
	}
	
	/**
	 * Bloco sincronizado que informa se a thread est� ativa.
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
	 * M�todo respons�vel por disparar um processo de carga junto ao banco.
	 * Neste m�todo � definido se um objeto possui depend�ncias ou se pode
	 * ser executado diretamente.
	 * @param nomePai String que identifica o nome do objeto pai que deve ter sua execu��o conclu�da antes do inicio deste.
	 * @param nomeProcesso String que identifica o processo para eventual monitoramento.
	 * @param storedProcedure String que identifica a storedprocedure do banco de dados que deve ser executada.
	 * @param parametros ArrayList com objetos para preenchimento das bind variables da storedProcedure (par�metros de entrada).
	 * @param padraoGenerico Flag que indica de a storedProcedure est� em um padr�o de par�metros gen�rico ou no padr�o TAM<br>
	 *        TRUE - Padr�o gen�rico<br>
	 *        FALSE - Padr�o TAM com os quatro par�metros de retorno.
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
				try {
					for (int i = 0; i < t.length; i++) {
						if (t[i] == null) {
							t[i] = new Thread(process);
							//
							t[i].setName(nomeProcesso);
							t[i].start();
							break;
						} else if (!threadAtiva(t[i])) {
							t[i] = null;
							t[i] = new Thread(process);
							//
							t[i].setName(nomeProcesso);
							t[i].start();
							break;
						} else if (i == (t.length - 1)) {
							i = -1;
							Thread.sleep(10);
						}
					}
				} catch (Exception e) {
					Log.error("Erro ao tentar criar a thread " + nomeProcesso);
					Log.error(e);
				}
			} else {
				try {
					for (int i = 0; i < monitor.length; i++) {
						if (monitor[i] == null) {
							monitor[i] = new Thread(new CargaFilhos());
							//
							monitor[i].setName("Monitor-" + nomePai);
							monitor[i].start();
							break;
						} else if (!threadAtiva(t[i])) {
							monitor[i] = null;
							monitor[i] = new Thread(new CargaFilhos());
							//
							monitor[i].setName("Monitor-" + nomePai);
							monitor[i].start();
							break;
						} else if (i == (monitor.length - 1)) {
							i = -1;
							Thread.sleep(10);
						}
					}
				} catch (Exception e) {
					Log.error("Erro ao tentar criar a thread de monitoramento " + "Monitor-" + nomePai);
					Log.error(e);
				}
			}
		} catch (Exception e) {
			Log.error("Erro ao tentar fazer a reflex�o do processo: " + storedProcedure);
			Log.error(e);
		}
	}
}
/**
 * Classe respons�vel por realizar o monitoramento se o objeto pai
 * ainda est� em execu��o e ao fim do processo pai, disparar o processo
 * filho para a realiza��o da carga.
 * @author Maciel Escudero Bombonato
 * @version 1.0 - 25/04/2007
 */
class CargaFilhos implements Runnable {
	
	/**
	 * M�todo padr�o o objeto Runnable para disparar uma nova thread.
	 * Realiza o monitoramento do objeto pai e ao t�rmino do mesmo, 
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
					Thread.sleep(10);
				} catch (Exception e) {
					Log.error("Erro ao tentar executar o comando sleep da thread " + nomePai);
					Log.error(e);
				}
				//   
			}
		}
		//
		try {
			for (int i = 0; i < ConcurrencyManager.t.length; i++) {
				try {
					if (ConcurrencyManager.t[i] == null) {
						ConcurrencyManager.t[i] = new Thread(new DBCommandExecutor(storedProcedure, parametros));
						//
						ConcurrencyManager.t[i].setName(nomeProcesso);
						ConcurrencyManager.t[i].start();
						break;
					} else if (!ConcurrencyManager.threadAtiva(ConcurrencyManager.t[i])) {
						ConcurrencyManager.t[i] = null;
						ConcurrencyManager.t[i] = new Thread(new DBCommandExecutor(storedProcedure, parametros));
						//
						ConcurrencyManager.t[i].setName(nomeProcesso);
						ConcurrencyManager.t[i].start();
						break;
					} else if (i == (ConcurrencyManager.t.length - 1)) {
						i = -1;
						Thread.sleep(10);
					}
				} catch (Exception e) {
					ConcurrencyManager.t[i] = new Thread(new DBCommandExecutor(storedProcedure, parametros));
					//
					ConcurrencyManager.t[i].setName(nomeProcesso);
					ConcurrencyManager.t[i].start();
					break;
				}
			}
		} catch (Exception e) {
			Log.error("Erro durante a cria��o da thread: " + ConcurrencyManager.nomeProcesso);
			Log.error(e);
		}
		//
	}
}
