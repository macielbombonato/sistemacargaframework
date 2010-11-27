package org.mtec.sistemacarga.framework;

import java.util.List;

/**
 * Classe respons�vel por montar a instru��o SQL para chamada de 
 * stored procedures.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 */
@SuppressWarnings("unchecked")
final class SQLStatamentBuilder {

	/**
	 * M�todo respons�vel por montar a instru��o SQL para chamada de 
	 * stored procedures do banco conforme par�metros de entrada.
	 * @param storedProcedureName String que identifica a storedProcedure do bando de dados.
	 * @param parameters ArrayList com objetos para preenchimento dos par�metros da storedProcedure 
	 * @param padraoGenerico Flag que identifica se a storedProcedure est� em um padr�o gen�rico de desenvolvimento(TRUE),
	 * ou no padr�o TAM(FALSE). 
	 * @return String - Instru��o SQL montada
	 */
	public static String createStoredProcedureCallStatement( String storedProcedureName
			                                               , List parameters
			                                               , boolean padraoGenerico
			                                               ) {
		//
		String callStatement = "CALL";
		String openParentesis = "(";
		String parameter = "?";
		String parameterSeparator = ",";
		String closeParentesis = ")";
		StringBuffer buf = new StringBuffer();
		int size = 0;
		if (padraoGenerico) {
			size = 0;
		} else {
			size = 4;
		}
		if(parameters != null && ! parameters.isEmpty()) {
			size += parameters.size();

			for(int i = 0; i < size - 1; i++) {
				buf.append(parameter).append(parameterSeparator);
			}
			buf.append(parameter);
		}

		
		String stmt = "{"+callStatement + " " + storedProcedureName  + openParentesis + buf.toString() + closeParentesis+"}";
		
		return stmt;
	}
}
