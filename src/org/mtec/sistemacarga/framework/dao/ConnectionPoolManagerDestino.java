package org.mtec.sistemacarga.framework.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.mtec.sistemacarga.framework.util.Log;
import org.mtec.sistemacarga.framework.util.ResourceLocation;



/**
 * Classe responsável por gerenciar as conexões junto ao banco de dados que
 * são utilizadas pela aplicação.
 * Nesta classe dados como quantidade máxima e inicial de conexões, url, usuário, 
 * senha, dentre outros são definidos.
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
	 * Objeto que armazena as configurações do pool de conexões.
	 */
	private DataSource ds;
	/**
	 * Driver que deve ser utilizado para realizar as conexões
	 * Definido no arquivo de configurações do sistema
	 */
	private String driver;
	/**
	 * URL do banco de dados.
	 * Definido no arquivo de configurações do sistema
	 */
	private String url;
	/**
	 * Nome do usuário que deve ser utilizado pelo processo de carga.
	 * Definido no arquivo de configurações do sistema
	 */
	private String username;
	/**
	 * Senha do usuário.
	 * Definido no arquivo de configurações do sistema
	 */
	private String password;
	/**
	 * Número máximo de conexões que devem ser abertas junto ao banco de dados.
	 * Definido no arquivo de configurações do sistema
	 */
	private int maxConnections;
	/**
	 * Número inicial de conexões que deve ser estabelecido junto ao banco de dados.
	 * Definido no arquivo de configurações do sistema
	 */
	private int iniConnections;

	/**
	 * Retorna instancia do gerenciador de conexões.
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
	 * Método responsável por verificar se os dados que estão sendo fornecidos pelo usuário
	 * possibilitam uma conexão junto ao banco de dados.
	 * @param url String que identifica a URL do banco de dados.
	 * @param username String informada pelo usuário da aplicação contendo o usuário de acesso ao banco de dados.
	 * @param password String informada pelo usuário da aplicação contendo a senha de acesso ao banco de dados.
	 * @return boolean <br>
	 *         TRUE = conseguiu realizar a conexão com sucesso<br>
	 * 		   FALSE = não conseguiu conectar<br>
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
			Log.error("Erro ao tentar realizar a conversão dos números máximos e inicial de conexões para tipo númerico, " +
					"verifique o arquivo de configurações da aplicação.");
			Log.error(e);
		} catch (SQLException e) {
			Log.error("Erro ao tentar obter uma conexão junto ao banco de dados.");
			Log.error(e);
		}
		
		return connOk;
	}
	
	/**
	 * Retorna uma conexão livre do pool de conexões.
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
	 * Imprime no direcionamento de log a quantidade de conexões ativas e 
	 * conexões ociosas.
	 * @throws SQLException
	 */
	public void printDataSourceStats() throws SQLException {
		BasicDataSource bds = (BasicDataSource) ds;
		Log.info("Número de conexões ativas: " + bds.getNumActive());
		Log.info("Número de conexões inativas: " + bds.getNumIdle());
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
