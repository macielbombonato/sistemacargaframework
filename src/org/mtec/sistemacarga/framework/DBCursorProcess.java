package org.mtec.sistemacarga.framework;

import java.sql.ResultSet;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.mtec.sistemacarga.framework.dao.CursorLoad;
import org.mtec.sistemacarga.framework.exception.SisCarException;



/**
 * Classe respons�vel por oferecer facilidades e padr�es de desenvolvimento
 * para obten��o de dados de cursores dos scripts de carga.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 * @version 1.1 - 18/10/2007
 */
@SuppressWarnings("unchecked")
public final class DBCursorProcess implements CursorInterface {
	
	/**
	 * Instru��o SQL a ser executada
	 */
	private String sql = null;
	
	/**
	 * Lista de par�metros que preenchem a clausula where da instru��o SQL
	 */
	private List parametros = null;

	/**
	 * Construtor padr�o da classe
	 * @param sql String -> Instru��o SQL que dever� ser executada junto ao banco de dados.
	 * @param parametros ArrayList que possui os valores que ser�o utilizados para preencher
	 * as bind variables da instru��o SQL.
	 */
	public DBCursorProcess(String sql, List parametros) {
		super();
		this.sql = sql;
		this.parametros = parametros;
	}

	/**
	 * Retorna um objeto CachedRowSet com os dados que devem ser utilizados no
	 * processo de carga. O retorno deste m�todo normalmente deve prover do 
	 * banco QA para indicar os dados que devem ser carregados no banco de 
	 * desenvolvimento.
	 * @return CachedRowSet com dados da consulta realizada.
	 * @throws SisCarException
	 */
	public CachedRowSet getCursor() throws SisCarException {
		CursorLoad cursor = new CursorLoad(sql);
		CachedRowSet crs = cursor.getCursor(parametros);
		return crs;
	}
	
	/**
	 * Retorna um objeto ResultSet com os dados que devem ser utilizados no
	 * processo de carga. O retorno deste m�todo normalmente deve prover do 
	 * banco QA para indicar os dados que devem ser carregados no banco de 
	 * desenvolvimento.
	 * @return ResultSet com dados da consulta realizada.
	 * @throws SisCarException
	 * @since 1.1
	 */
	public ResultSet getCursorRs() throws SisCarException {
		CursorLoad cursor = new CursorLoad(sql);
		ResultSet rs = cursor.getCursorRs(parametros);
		return rs;
	}

}
