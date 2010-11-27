package org.mtec.sistemacarga.framework;

import java.util.List;

import org.mtec.sistemacarga.framework.dao.ExecuteDMLCommand;
import org.mtec.sistemacarga.framework.dao.ExecuteStoredProcedure;
import org.mtec.sistemacarga.framework.exception.SisCarException;



/**
 * Classe responsável por disparar a chamada do objeto de carga
 * definindo padrões de desenvolvimento e qual padrão de 
 * stored procedure será executada.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 */
@SuppressWarnings("unchecked")
final class DBCommandExecutor extends AbstractProcess {
	
	/**
	 * Nome da stored procedure que será disparada junto ao banco de dados.
	 */
	private String storedProcedureName;
	
	/**
	 * Flag para verificar se a stored procedure está no padrão TAM (com os
	 * quatro parâmetros de retorno) ou se está em um padrão genérico (quantidade
	 * livre de parâmetros).
	 * Caso este parâmetro não seja informado, será considerado como FALSE.
	 * TRUE  = padrão genérico, com quantidade livre de parâmetros
	 * FALSE = padrão TAM de desenvolvimento, quatro parâmetros serão adicionados
	 *         no final da lista de parâmetros
	 */
	private boolean padraoGenerico = true;

	/**
	 * Construtor da classe para chamada de objetos no padrão de desenvolvimento TAM
	 * onde quatro parâmetros são adicionados no final da lista de parâmetros.
	 * @param storedProcedureName String 
	 * @param parameters List 
	 */
	public DBCommandExecutor(String storedProcedureName, List parameters) {
		super(parameters);
		this.storedProcedureName = storedProcedureName;
	}
	
	/**
	 * Construtor da classe que deixa aberto ao desenvolvedor definir se a stored 
	 * procedure a ser executada está no padrão TAM ou não.
	 * @param storedProcedureName String que identifica a storedProcedure do bando de dados que deve ser executada.
	 * @param parameters ArrayList com objetos para preenchimento dos parâmetros de entrada da storedProcedure.
	 * @param padraoGenerico Flag que identifica se a storedProcedure está no padrão TAM (FALSE) ou se está em padrão genérico (TRUE).  
	 */
	public DBCommandExecutor(String storedProcedureName, List parameters, boolean padraoGenerico) {
		super(parameters);
		this.storedProcedureName = storedProcedureName;
		this.padraoGenerico = padraoGenerico;
	}

	/**
	 * Método responsável por verificar e executar o processo de carga conforme parâmetros informados.
	 * @throws SisCarException
	 */
	@Override
	public void exec() throws SisCarException {
		boolean isStoredProcedure = isStoredProcedure();
		if (isStoredProcedure) {
			String sql = SQLStatamentBuilder.createStoredProcedureCallStatement(storedProcedureName, getParameters(), padraoGenerico);
			ExecuteStoredProcedure carga = new ExecuteStoredProcedure(sql);
			carga.setPadraoGenerico(padraoGenerico);
			if (!padraoGenerico) {
				carga.exec(getParameters());
			} else {
				carga.execGenerico(getParameters());
			}
		} else {
			String sql = storedProcedureName;
			ExecuteDMLCommand carga = new ExecuteDMLCommand(sql);
			carga.execute(getParameters());
		}
		
	}

	/**
	 * Método responsável por verificar se o processo a ser executado é uma stored procedure ou 
	 * a execução direta de um dos comandos (INSERT, UPDATE ou DELETE).
	 * @return boolean<br>
	 *         TRUE -> É uma storedProcedure<br>
	 *         FALSE -> Não é uma storedProcedure<br>
	 */
	private boolean isStoredProcedure() {
		boolean isStoredProcedure = true;
		String[] test = {"insert ", "update ", "delete "};
		for (int i = 0; i < test.length; i++) {
			isStoredProcedure = (!storedProcedureName.contains(test[i]));
			if (!isStoredProcedure) {
				break;
			}
			isStoredProcedure = (!storedProcedureName.contains(test[i].toUpperCase()));
			if (!isStoredProcedure) {
				break;
			}
		}
		return isStoredProcedure;
	}
}
