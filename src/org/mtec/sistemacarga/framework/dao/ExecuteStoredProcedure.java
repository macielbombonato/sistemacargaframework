package org.mtec.sistemacarga.framework.dao;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mtec.sistemacarga.framework.reload.ProcessToReLoad;
import org.mtec.sistemacarga.framework.util.Log;


import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleCallableStatement;


/**
 * A partir de uma instrução SQL passada como parâmetro executa processos de carga.
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 03/01/2007
 */
public class ExecuteStoredProcedure implements Serializable {

	/**
	 * Número Serial da classe.
	 */
	private static final long serialVersionUID = -5078587155385499130L;
	
	/**
	 * String que representa a instrução SQL que será processada pelo banco. 
	 */
	private String sql;
	/**
	 * Variável de retorno padrão dos sistemas TAM
	 * Indica o código do sistema que está sendo executado
	 */
	private String cdSistema;
	/**
	 * Variável de retorno padrão dos sistemas TAM
	 * Indica o código do processo que está sendo executado
	 */
	private int cdProcesso;
	/**
	 * Variável de retorno padrão dos sistemas TAM
	 * Indica o código de retorno do processo, 0 (zero) para OK 
	 * e diferente de 0 (zero) pra indicar erros
	 */
	private int cdRetorno;
	/**
	 * Variável de retorno padrão dos sistemas TAM
	 * Indica a mensagem de erro retornada pelo processo, 
	 * estando nula, o retorno foi OK
	 */
	private String dsMensagem;
	
	/**
	 * Lista de Parâmetros para execução da storedProcedure
	 */
	private List params;
	
	/**
	 * Flag que informa se a storedProcedure a ser executada está ou não no padrão TAM de desenvolvimento
	 * TRUE - não está no padrão TAM
	 * FALSE - está no padrão TAM
	 */
	private boolean padraoGenerico = false;
	
	/**
	 * Retorna instrução SQL utilizada pelo processo
	 * @return sql String contendo a instrução SQL utilizada pelo processo
	 */
	public String getSql() {
		return sql;
	}
	
	/**
	 * Informa se a storedProcedure está ou não no padrão TAM de desenvolvimento
	 * @return boolean - TRUE - não está no padrão TAM
	 *                   FALSE - está no padrão TAM
	 */
	public boolean isPadraoGenerico() {
		return padraoGenerico;
	}

	/**
	 * Define o padrão de desenvolvimento da storedProcedure
	 * @param padraoGenerico TRUE - fora do padrão TAM
	 *                       FALSE - padrão TAM de desenvolvimento
	 */
	public void setPadraoGenerico(boolean padraoGenerico) {
		this.padraoGenerico = padraoGenerico;
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
	 * Construtor da classe que recebe como parâmetro a instrução SQL a ser executada.
	 * @param sql
	 */
	public ExecuteStoredProcedure(String sql) {
		super();
		this.sql = sql;
	}
	
	/**
	 * Executa o procedimento passado como parâmetro para o construtor da classe
	 * combinado com os parâmetros passados via parâmetro para o método. 
	 * @param lista ArrayList com objetos utilizados como parâmetros de entrada para a instrução SQL.
	 */
	@SuppressWarnings("unchecked")
	public void exec(List lista) {
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
			CallableStatement proc = null;
			if ((conn != null) && (!conn.isClosed())) {
				proc = (CallableStatement) conn.prepareCall(sql);
			} else {
				conn = ConnectionPoolManagerDestino.getInstance().getConnection();
				proc = (OracleCallableStatement) conn.prepareCall(sql);
			}

			// Indice utilizado pra mapear a quantidade de parâmetros do procedimento.
			int ind = 0;
			// Carrega lista de parâmetros da instrução SQL
			if (lista != null) {
				//
				ind = lista.size();
				//
				for (int i = 0; i < lista.size(); i++) {
					if (lista.get(i) == null) {
						proc.setNull(i+1, Types.NULL);
					} else if (lista.get(i) instanceof String) {
						proc.setString(i+1, (String)lista.get(i));
					} else if (lista.get(i) instanceof Integer) {
						proc.setInt(i+1, ((Integer)lista.get(i)).intValue());
					} else if (lista.get(i) instanceof Date) {
						proc.setDate(i+1, (java.sql.Date)lista.get(i));
					} else if (lista.get(i) instanceof java.sql.Date) {
						proc.setDate(i+1, (java.sql.Date)lista.get(i));
					} else if (lista.get(i) instanceof Long) {
						proc.setLong(i+1, ((Long)lista.get(i)).longValue());
					} else if (lista.get(i) instanceof Float) {
						proc.setFloat(i+1, ((Float)lista.get(i)).floatValue());
					} else if (lista.get(i) instanceof Double) {
						proc.setDouble(i+1, ((Double)lista.get(i)).doubleValue());
					}
				}
			}
			// Complementa lista de parâmetros com parâmetros padrão da TAM.
			proc.setString(ind+1, cdSistema);
			proc.setInt(ind+2, cdProcesso);
			proc.setInt(ind+3, cdRetorno);
			proc.setString(ind+4, dsMensagem);
			//
			// Registra parâmetros de saída padrão TAM
			proc.registerOutParameter(ind+1, OracleTypes.VARCHAR);
			proc.registerOutParameter(ind+2, OracleTypes.NUMBER);
			proc.registerOutParameter(ind+3, OracleTypes.NUMBER);
			proc.registerOutParameter(ind+4, OracleTypes.VARCHAR);
			
			// Executa instrução SQL
			if ((conn != null) && (!conn.isClosed()) && (proc.getConnection() != null) && (!proc.getConnection().isClosed())) {
				try {
					proc.execute();
				} catch (Exception e) {
					Log.error("Erro ao executar a instrução SQL: " + sql);
					Log.error(e);
				}
			} else {
				Log.warn("Conexão fechada.");
			}
			
			// Commita Transação.
			if ((conn != null) && (!conn.isClosed()) && (proc.getConnection() != null) && (!proc.getConnection().isClosed())) {
				try {
					conn.commit();
				} catch (Exception e) {
					Log.error("Erro ao tentar confirmar a transação: "  + sql);
					Log.error(e);
				}
				//
				try {
					if (proc != null) {
						if ((proc.getString(ind+3) != null) && 
								(!proc.getString(ind+3).equalsIgnoreCase("0"))) {
							Log.warn("Processo: "    + sql +
									"\nParâmetros: " + lista + 
									"\nRetorno: "    + proc.getLong(ind+3) + 
									"\nMensagem: "   + proc.getString(ind+4));
							ProcessToReLoad.add(this);
						}
					} else {
						Log.warn("Erro ao verificar retorno do processo. Objeto de retorno nulo.");
					}
				} catch(Exception e1) {
					Log.error("Erro ao verificar retorno do processo: "  + sql);
					Log.error(e1);
				}
				// Fechando a conexão
				proc.close();
			} else {
				Log.warn("Conexão fechada.");
			}
		} catch (Exception e) {
			ProcessToReLoad.add(this);
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
	
	/**
	 * Executa o procedimento passado como parâmetro para o construtor da classe
	 * combinado com os parâmetros passados via parâmetro para o método. 
	 * @param lista List 
	 */
	public void execGenerico(List lista) {
		//
		Connection conn = null;
		//
		try {
			//
			conn = ConnectionPoolManagerDestino.getInstance().getConnection();
			//
			CallableStatement proc = null;
			if ((conn != null) && (!conn.isClosed())) {
				proc = (CallableStatement) conn.prepareCall(sql);
			} else {
				conn = ConnectionPoolManagerDestino.getInstance().getConnection();
				proc = (CallableStatement) conn.prepareCall(sql);
			}

			// Carrega lista de parâmetros da instrução SQL
			if (lista != null) {
				//
				for (int i = 0; i < lista.size(); i++) {
					if (lista.get(i) == null) {
						proc.setNull(i+1, Types.NULL);
					} else if (lista.get(i) instanceof String) {
						proc.setString(i+1, (String)lista.get(i));
					} else if (lista.get(i) instanceof Integer) {
						proc.setInt(i+1, ((Integer)lista.get(i)).intValue());
					} else if (lista.get(i) instanceof Date) {
						proc.setDate(i+1, (java.sql.Date)lista.get(i));
					} else if (lista.get(i) instanceof Long) {
						proc.setLong(i+1, ((Long)lista.get(i)).longValue());
					} else if (lista.get(i) instanceof Float) {
						proc.setFloat(i+1, ((Float)lista.get(i)).floatValue());
					} else if (lista.get(i) instanceof Double) {
						proc.setDouble(i+1, ((Double)lista.get(i)).doubleValue());
					}
				}
				//
			}
			// Executa instrução SQL
			if ((conn != null) && (!conn.isClosed()) && (proc.getConnection() != null) && (!proc.getConnection().isClosed())) {
				proc.execute();
			} else {
				Log.warn("Conexão fechada.");
			}
			// Commita Transação.
			if ((conn != null) && (!conn.isClosed()) && (proc.getConnection() != null) && (!proc.getConnection().isClosed())) {
				conn.commit();
			} else {
				Log.warn("Não foi possível confirmar a transação no banco. Conexão fechada.");
			}
			//
			// Fechando a conexão
			proc.close();
			//
		} catch (Exception e) {
			ProcessToReLoad.add(this);
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
