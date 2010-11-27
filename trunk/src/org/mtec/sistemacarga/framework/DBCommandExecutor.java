package org.mtec.sistemacarga.framework;

import java.util.List;

import org.mtec.sistemacarga.framework.dao.ExecuteDMLCommand;
import org.mtec.sistemacarga.framework.dao.ExecuteStoredProcedure;
import org.mtec.sistemacarga.framework.exception.SisCarException;



/**
 * Classe respons�vel por disparar a chamada do objeto de carga
 * definindo padr�es de desenvolvimento e qual padr�o de 
 * stored procedure ser� executada.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 */
@SuppressWarnings("unchecked")
final class DBCommandExecutor extends AbstractProcess {
	
	/**
	 * Nome da stored procedure que ser� disparada junto ao banco de dados.
	 */
	private String storedProcedureName;
	
	/**
	 * Flag para verificar se a stored procedure est� no padr�o TAM (com os
	 * quatro par�metros de retorno) ou se est� em um padr�o gen�rico (quantidade
	 * livre de par�metros).
	 * Caso este par�metro n�o seja informado, ser� considerado como FALSE.
	 * TRUE  = padr�o gen�rico, com quantidade livre de par�metros
	 * FALSE = padr�o TAM de desenvolvimento, quatro par�metros ser�o adicionados
	 *         no final da lista de par�metros
	 */
	private boolean padraoGenerico = true;

	/**
	 * Construtor da classe para chamada de objetos no padr�o de desenvolvimento TAM
	 * onde quatro par�metros s�o adicionados no final da lista de par�metros.
	 * @param storedProcedureName String 
	 * @param parameters List 
	 */
	public DBCommandExecutor(String storedProcedureName, List parameters) {
		super(parameters);
		this.storedProcedureName = storedProcedureName;
	}
	
	/**
	 * Construtor da classe que deixa aberto ao desenvolvedor definir se a stored 
	 * procedure a ser executada est� no padr�o TAM ou n�o.
	 * @param storedProcedureName String que identifica a storedProcedure do bando de dados que deve ser executada.
	 * @param parameters ArrayList com objetos para preenchimento dos par�metros de entrada da storedProcedure.
	 * @param padraoGenerico Flag que identifica se a storedProcedure est� no padr�o TAM (FALSE) ou se est� em padr�o gen�rico (TRUE).  
	 */
	public DBCommandExecutor(String storedProcedureName, List parameters, boolean padraoGenerico) {
		super(parameters);
		this.storedProcedureName = storedProcedureName;
		this.padraoGenerico = padraoGenerico;
	}

	/**
	 * M�todo respons�vel por verificar e executar o processo de carga conforme par�metros informados.
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
	 * M�todo respons�vel por verificar se o processo a ser executado � uma stored procedure ou 
	 * a execu��o direta de um dos comandos (INSERT, UPDATE ou DELETE).
	 * @return boolean<br>
	 *         TRUE -> � uma storedProcedure<br>
	 *         FALSE -> N�o � uma storedProcedure<br>
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
