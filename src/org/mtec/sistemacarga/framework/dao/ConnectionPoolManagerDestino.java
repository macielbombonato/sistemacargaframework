package org.mtec.sistemacarga.framework.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.mtec.sistemacarga.framework.util.Log;
import org.mtec.sistemacarga.framework.util.ResourceLocation;



/**
 * Classe respons�vel por gerenciar as conex�es junto ao banco de dados que
 * s�o utilizadas pela aplica��o.
 * Nesta classe dados como quantidade m�xima e inicial de conex�es, url, usu�rio, 
 * senha, dentre outros s�o definidos.
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 
 */
public class ConnectionPoolManagerDestino {
	
	/**
	 * Instancia do objeto.
	 * Singletone
	 */
	private static ConnectionPoolManagerDestino instance = null;
	
	static {
		if(instance == null) {
			instance = new ConnectionPoolManagerDestino();
		}
	}
	
	/**
	 * Objeto que armazena as configura��es do pool de conex�es.
	 */
	private DataSource ds;
	/**
	 * Driver que deve ser utilizado para realizar as conex�es
	 * Definido no arquivo de configura��es do sistema
	 */
	private String driver;
	/**
	 * URL do banco de dados.
	 * Definido no arquivo de configura��es do sistema
	 */
	private String url;
	/**
	 * Nome do usu�rio que deve ser utilizado pelo processo de carga.
	 * Definido no arquivo de configura��es do sistema
	 */
	private String username;
	/**
	 * Senha do usu�rio.
	 * Definido no arquivo de configura��es do sistema
	 */
	private String password;
	/**
	 * N�mero m�ximo de conex�es que devem ser abertas junto ao banco de dados.
	 * Definido no arquivo de configura��es do sistema
	 */
	private int maxConnections;
	/**
	 * N�mero inicial de conex�es que deve ser estabelecido junto ao banco de dados.
	 * Definido no arquivo de configura��es do sistema
	 */
	private int iniConnections;

	/**
	 * Retorna instancia do gerenciador de conex�es.
	 * @return ConnectionPoolManager instance
	 */
	public static ConnectionPoolManagerDestino getInstance() {
		if(instance == null) {
			instance = new ConnectionPoolManagerDestino();
		}
		return instance;
	}
	
	/**
	 * Construtor da classe. 
	 */
	private ConnectionPoolManagerDestino() {
		//recuperando configuracoes de acesso ao db
		driver = ResourceLocation.DATABASE_DRIVER_DESTINO;
		url = ResourceLocation.DATABASE_URL_DESTINO;
		username = ResourceLocation.DATABASE_USER_DESTINO;
		password = ResourceLocation.DATABASE_PASS_DESTINO;
		maxConnections = ResourceLocation.DATABASE_MAX_CONNECTIONS;
		iniConnections = ResourceLocation.DATABASE_INI_CONNECTIONS;
		ds = setupDataSource();
	}
	
	/**
	 * M�todo respons�vel por verificar se os dados que est�o sendo fornecidos pelo usu�rio
	 * possibilitam uma conex�o junto ao banco de dados.
	 * @param url String que identifica a URL do banco de dados.
	 * @param username String informada pelo usu�rio da aplica��o contendo o usu�rio de acesso ao banco de dados.
	 * @param password String informada pelo usu�rio da aplica��o contendo a senha de acesso ao banco de dados.
	 * @return boolean <br>
	 *         TRUE = conseguiu realizar a conex�o com sucesso<br>
	 * 		   FALSE = n�o conseguiu conectar<br>
	 */
	public boolean testConnection( String dataBaseDriver
            , String url
            , String username
            , String password
            ) {
		boolean connOk = false;
		try {
			//recuperando configuracoes de acesso ao db
			this.driver = dataBaseDriver;
			this.url = url;
			this.username = username;
			this.password = password;
			maxConnections = ResourceLocation.DATABASE_MAX_CONNECTIONS;
			iniConnections = ResourceLocation.DATABASE_INI_CONNECTIONS;
			this.ds = setupDataSource();
			
			Connection conn = ds.getConnection();
			//
			if (!conn.isClosed()) {
				conn.close();
				ConnectionPoolManagerDestino.getInstance().shutdown();
				connOk = true;
			}
			//
		} catch (NumberFormatException e) {
			Log.error("Erro ao tentar realizar a convers�o dos n�meros m�ximos e inicial de conex�es para tipo n�merico, " +
					"verifique o arquivo de configura��es da aplica��o.");
			Log.error(e);
		} catch (SQLException e) {
			Log.error("Erro ao tentar obter uma conex�o junto ao banco de dados.");
			Log.error(e);
		}
		
		return connOk;
	}
	
	/**
	 * Retorna uma conex�o livre do pool de conex�es.
	 * @return Connection
	 * @throws SQLException
	 */
	@SuppressWarnings("finally")
	public Connection getConnection() {
		Connection conn = null;
		while(conn == null) {
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				try {
					Thread.sleep(100);
				} catch (Exception e1) {
				}
				conn = null;
				setupDataSource();
			}
		}
		return conn;
	}
		  
	/**
	 * Configura DataSource
	 * @return DataSource
	 */
	private DataSource setupDataSource() {
		BasicDataSource ds = new BasicDataSource();
		
		ds.setDriverClassName(driver);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setUrl(url);
		ds.setMaxActive(maxConnections);
		ds.setPoolPreparedStatements(true);
		ds.setMaxWait(Long.MAX_VALUE);
		ds.setInitialSize(iniConnections);
		ds.setMinIdle(0);
		ds.setMaxIdle(iniConnections);
		ds.setDefaultAutoCommit(true);
		return ds;
	}
		 
	/**
	 * Imprime no direcionamento de log a quantidade de conex�es ativas e 
	 * conex�es ociosas.
	 * @throws SQLException
	 */
	public void printDataSourceStats() throws SQLException {
		BasicDataSource bds = (BasicDataSource) ds;
		Log.info("N�mero de conex�es ativas: " + bds.getNumActive());
		Log.info("N�mero de conex�es inativas: " + bds.getNumIdle());
	}
	  
	/** 
	 * shutdown DataSource
	 * @throws SQLException
	 */
	public void shutdown() throws SQLException {
		BasicDataSource bds = (BasicDataSource) ds;
		bds.close();
	}
}
