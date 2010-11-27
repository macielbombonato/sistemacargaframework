package org.mtec.sistemacarga.framework.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.mtec.sistemacarga.framework.util.DateFormat;
import org.mtec.sistemacarga.framework.util.Log;

import com.sun.rowset.CachedRowSetImpl;

/**
 * A partir de uma instru��o SQL passada como par�metro recupera informa��es do banco
 * de dados retornando um CachedRowSet
 * @author Maciel Escudero Bombonato
 * @version 1.0 - 03/01/2007
 * @version 1.1 - 18/10/2007
 */
@SuppressWarnings("unchecked")
public class CursorLoad {
	
	/**
	 * String que representa a instru��o SQL que ser� processada pelo banco.
	 */
	private String sql;
	
	
	/**
	 * Construtor da Classe
	 */
	public CursorLoad(String sql) {
		super();
		this.sql = sql;
	}
	
	/**
	 * Completa a pesquisa informada na constru��o objeto com os dados contidos no 
	 * ArrayList passado como par�metro e retorna o CachedRowSet resultante disto.
	 * @param lista ArrayList com objetos utilizados como par�metros de entrada para a instru��o SQL.
	 * @return CachedRowSet cursor
	 */
	public CachedRowSet getCursor(List lista) {
		//
		CachedRowSet crs = null;
		//
		Connection conn = null;
		//
		try {
			//
			conn = ConnectionPoolManagerOrigem.getInstance().getConnection();
			// Realiza conex�o com banco de dados.
			PreparedStatement p = conn.prepareStatement(sql);
			// Caso a lista de par�metros n�o seja nula
			if (lista != null) { 
				// Completa a Query com a lista de par�metros contida em um ArrayList
				for (int i = 0; i < lista.size(); i++) {
					if (lista.get(i) == null) {
						p.setNull(i+1, Types.NULL);
					} else if (lista.get(i) instanceof String) {
						p.setString(i+1, (String)lista.get(i));
					} else if (lista.get(i) instanceof Integer) {
						p.setInt(i+1, ((Integer)lista.get(i)).intValue());
					} else if (lista.get(i) instanceof java.util.Date) {
						p.setString(i+1, DateFormat.getInstance().formatDate((java.util.Date)lista.get(i)));
					} else if (lista.get(i) instanceof java.sql.Date) {
						p.setString(i+1, DateFormat.getInstance().formatDate((java.sql.Date)lista.get(i)));
					} else if (lista.get(i) instanceof Long) {
						p.setLong(i+1, ((Long)lista.get(i)).longValue());
					}
				}
			}
			
			// Define a quantidade m�xima de linhas que devem ser processadas.
			// por padr�o esta linha fica desativada, por�m, foi deixada no c�digo
			// para realiza��o de prov�veis testes futuros.
//			p.setMaxRows(10);
			p.setFetchSize(1000);
			// Criando CachedRowSet
			crs = new CachedRowSetImpl();
			crs.setPageSize(1000);
			crs.setFetchSize(1000);
			//
			// Preenchendo o CachedRowSet com dados da consulta
			crs.populate(p.executeQuery());
			//
			// Fechando a conex�o
			p.close();
			//
		} catch (Exception e) {
			Log.error("Erro ao tentar carregar o cursor.");
			Log.error(e);
		} finally {
			try {
				if ((conn != null) && (!conn.isClosed())){
					conn.close();
				}
			} catch (SQLException ex) {
				Log.error("Erro ao tentar liberar a conex�o.");
				Log.error(ex);
			}
		}
		// Retorna o CachedRowSet populado.
		return crs;
	}
	
	
	/**
	 * Completa a pesquisa informada na constru��o objeto com os dados contidos no 
	 * ArrayList passado como par�metro e retorna o CachedRowSet resultante disto.
	 * @param lista ArrayList com objetos utilizados como par�metros de entrada para a instru��o SQL.
	 * @return ResultSet cursor
	 * @since 1.1
	 */
	public ResultSet getCursorRs(List lista) {
		//
		ResultSet rs = null;
		PreparedStatement p = null;
		//
		Connection conn = null;
		//
		try {
			//
			conn = ConnectionPoolManagerOrigem.getInstance().getConnection();
			// Realiza conex�o com banco de dados.
			p = conn.prepareStatement(sql);
			// Caso a lista de par�metros n�o seja nula
			if (lista != null) { 
				// Completa a Query com a lista de par�metros contida em um ArrayList
				for (int i = 0; i < lista.size(); i++) {
					if (lista.get(i) == null) {
						p.setNull(i+1, Types.NULL);
					} else if (lista.get(i) instanceof String) {
						p.setString(i+1, (String)lista.get(i));
					} else if (lista.get(i) instanceof Integer) {
						p.setInt(i+1, ((Integer)lista.get(i)).intValue());
					} else if (lista.get(i) instanceof java.util.Date) {
						p.setString(i+1, DateFormat.getInstance().formatDate((java.util.Date)lista.get(i)));
					} else if (lista.get(i) instanceof java.sql.Date) {
						p.setString(i+1, DateFormat.getInstance().formatDate((java.sql.Date)lista.get(i)));
					} else if (lista.get(i) instanceof Long) {
						p.setLong(i+1, ((Long)lista.get(i)).longValue());
					}
				}
			}
			
			// Popula Objeto
			p.executeQuery();
			
			rs = p.getResultSet();
			rs.setFetchSize(1000);
			//
			// Fechando a conex�o
//			p.close();
			//
		} catch (Exception e) {
			Log.error("Erro ao tentar carregar o cursor.");
			Log.error(e);
			
		} 
//		finally {
//			try {
//				if ((conn != null) && (!conn.isClosed())){
//					conn.close();
//				}
//			} catch (SQLException e) {
//				Log.error("Erro ao tentar liberar a conex�o.");
//				Log.error(e);
//			}
//		}
		// Retorna o CachedRowSet populado.
		return rs;
	}

	/**
	 * M�todo utilizado para que a instru��o SQL possa ser alterada sem que seja necess�rio
	 * criar uma nova instancia do objeto.
	 * @param sql
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

}
