package org.mtec.sistemacarga.framework;

import javax.sql.rowset.CachedRowSet;

import org.mtec.sistemacarga.framework.exception.SisCarException;



/**
 * Interface para criação de classes de cacheamento de dados.
 * @author Maciel Escudero Bombonato, Antonio Cezar Murakami Silva
 * @version 1.0 - 05/02/2007
 */
interface CursorInterface {
	
	/**
	 * Método responsável por estabelecer um padrão de desenvolvimento na obtenção de dados
	 * de cursores do banco.
	 * @return CachedRowSet Dados retornados pela consulta realizada junto ao banco de dados.
	 * @throws SisCarException
	 */
	public CachedRowSet getCursor() throws SisCarException;
}
