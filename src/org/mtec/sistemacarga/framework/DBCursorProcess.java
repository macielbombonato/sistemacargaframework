package org.mtec.sistemacarga.framework;

import java.sql.ResultSet;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.mtec.sistemacarga.framework.dao.CursorLoad;
import org.mtec.sistemacarga.framework.exception.SisCarException;



/**
 * Classe responsável por oferecer facilidades e padrões de desenvolvimento
 * para obtenção de dados de cursores dos scripts de carga.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 * @version 1.1 - 18/10/2007
 */
@SuppressWarnings("unchecked")
public final class DBCursorProcess implements CursorInterface {
	
	/**
	 * Instrução SQL a ser executada
	 */
	private String sql = null;
	
	/**
	 * Lista de parâmetros que preenchem a clausula where da instrução SQL
	 */
	private List parametros = null;

	/**
	 * Construtor padrão da classe
	 * @param sql String -> Instrução SQL que deverá ser executada junto ao banco de dados.
	 * @param parametros ArrayList que possui os valores que serão utilizados para preencher
	 * as bind variables da instrução SQL.
	 */
	public DBCursorProcess(String sql, List parametros) {
		super();
		this.sql = sql;
		this.parametros = parametros;
	}

	/**
	 * Retorna um objeto CachedRowSet com os dados que devem ser utilizados no
	 * processo de carga. O retorno deste método normalmente deve prover do 
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
	 * processo de carga. O retorno deste método normalmente deve prover do 
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
