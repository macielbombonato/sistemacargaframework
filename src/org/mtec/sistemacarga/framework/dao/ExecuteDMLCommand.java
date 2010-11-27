package org.mtec.sistemacarga.framework.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mtec.sistemacarga.framework.reload.ProcessToReLoad;
import org.mtec.sistemacarga.framework.util.Log;



/**
 * Classe responsável por realizar a execução direta dos comandos insert, update e delete
 * para contemplar a necessidade de alguns scripts de carga específicos que não chamam 
 * storedprocedures para realizar esta ação.
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 04/05/2007
 * @version 1.1 - 23/10/2007 - Inclusão dos tipos Double e Float na montagem de parâmetros.
 */
@SuppressWarnings("unchecked")
public class ExecuteDMLCommand {
	
	/**
	 * String que representa a instrução SQL que será processada pelo banco. 
	 */
	private String sql;
	
	/**
	 * Lista de Parâmetros para execução da storedProcedure
	 */
	
	private List params;
	
	/**
	 * Retorna instrução SQL utilizada pelo processo
	 * @return sql String contendo a instrução SQL utilizada pelo processo
	 */
	public String getSql() {
		return sql;
	}
	
	/**
	 * Método para definir lista de parâmetros a serem utilizados.
	 * Este método é privado devido ao fato de dever ser utilizado apenas internamente.
	 * @param params ArrayList com os parâmetros de entrada da storedProcedure
	 */
	private void setParams(List params) {
		this.params = params;
	}
	
	/**
	 * Retorna lista de parâmetros a serem utilizados pela storedProcedure definida no field SQL
	 * @return params ArrayList com os parâmetros de entrada da storedProcedure.
	 */
	public List getParams() {
		return params;
	}
	
	/**
	 * Define Instrução SQL
	 * @param sql String com comando DML para execução direta no banco.
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * Construtor da classe que recebe como parâmetro a instrução SQL a ser executada.
	 * @param sql
	 */
	public ExecuteDMLCommand(String sql) {
		super();
		this.sql = sql;
	}
	
	/**
	 * Executa o procedimento passado como parâmetro para o construtor da classe
	 * combinado com os parâmetros passados para o método. 
	 * @param lista List de parâmetros de entrada para preenchimento das bind variables
	 */
	@SuppressWarnings("unchecked")
	public void execute(List lista) {
		//
		if (lista != null) { 
			setParams(new ArrayList(lista));
		}
		//
		Connection conn = null;
		//
		try {
			//
			conn = ConnectionPoolManagerDestino.getInstance().getConnection();
			//
			PreparedStatement pst = null;
			if ((conn != null) && (!conn.isClosed())) {
				pst = (PreparedStatement) conn.prepareStatement(sql);
			} else {
				conn = ConnectionPoolManagerDestino.getInstance().getConnection();
				pst = (CallableStatement) conn.prepareCall(sql);
			}

			// Carrega lista de parâmetros da instrução SQL
			if (lista != null) {
				//
				for (int i = 0; i < lista.size(); i++) {
					if (lista.get(i) == null) {
						pst.setNull(i+1, Types.VARCHAR);
					} else if (lista.get(i) instanceof String) {
						pst.setString(i+1, (String)lista.get(i));
					} else if (lista.get(i) instanceof Integer) {
						pst.setInt(i+1, ((Integer)lista.get(i)).intValue());
					} else if (lista.get(i) instanceof Timestamp) {
						pst.setTimestamp(i+1, (java.sql.Timestamp)lista.get(i));
					} else if (lista.get(i) instanceof Date) {
						pst.setDate(i+1, (java.sql.Date)lista.get(i));
					} else if (lista.get(i) instanceof Long) {
						pst.setLong(i+1, ((Long)lista.get(i)).longValue());
					} else if (lista.get(i) instanceof Double) {
						pst.setDouble(i+1, ((Double)lista.get(i)).doubleValue());
					} else if (lista.get(i) instanceof Float) {
						pst.setFloat(i+1, ((Float)lista.get(i)).floatValue());
					}
				}
				//
			}
			// Executa instrução SQL
			if ((conn != null) && (!conn.isClosed()) && (pst.getConnection() != null) && (!pst.getConnection().isClosed())) {
				pst.execute();
			} else {
				Log.warn("Conexão fechada.");
			}
			// Commita Transação.
			if ((conn != null) && (!conn.isClosed()) && (pst.getConnection() != null) && (!pst.getConnection().isClosed())) {
				conn.commit();
			} else {
				Log.warn("Não foi possível confirmar a transação no banco. Conexão fechada.");
			}
			//
			// Fechando a conexão
			pst.close();
			//
		} catch (SQLException e) {
			if (e.getErrorCode() != 1) { 
				ProcessToReLoad.DmlAdd(this);
				Log.error("Erro ao tentar executar o processo: "  + sql + " -- Parâmetros: " + lista);
				Log.error(e);
			}
		} catch (Exception e) {
			ProcessToReLoad.DmlAdd(this);
			Log.error("Erro ao tentar executar o processo: "  + sql + " -- Parâmetros: " + lista);
			Log.error(e);
		} finally {
			try {
				if ((conn != null) && (!conn.isClosed())){
					conn.close();
				}
			} catch (SQLException e) {
				Log.error("Erro ao tentar liberar a conexão.");
				Log.error(e);
			}
		}
	}

}
