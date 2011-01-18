package org.mtec.sistemacarga.framework;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.mtec.sistemacarga.framework.dao.ConnectionPoolManagerDestino;
import org.mtec.sistemacarga.framework.dao.ConnectionPoolManagerOrigem;
import org.mtec.sistemacarga.framework.dao.HibernateConcurrentExecution;
import org.mtec.sistemacarga.framework.util.Log;
import org.mtec.sistemacarga.framework.util.ResourceLocation;


/**
 * Classe responsável por disponibilizar ao desenvolvedor um dispositivo de chaveamento
 * que utilize hibernate e retire os dados do banco de origem e os repasse ao banco
 * de destino.
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 02/12/2007
 */
public class HibernateLoadProcess {
	
	/**
	 * Factory que aponta para o banco de origem de dados
	 */
	private SessionFactory sourceFactory;
	
	/**
	 * Session que aponta para o banco de origem de dados
	 */
	private Session sourceSession;
	
	/**
	 * Annotation Configuration utilizado para o banco de dados de origem
	 */
	private AnnotationConfiguration sourceConfig;
	
	/**
	 * Factory que aponta para o banco de destino de dados
	 */
	private SessionFactory destinyFactory;
	
	/**
	 * Session que aponta para o banco de destino de dados
	 */
	private Session destinySession;
	
	/**
	 * Annotation Configuration utilizado para o banco de dados de destino
	 */
	private AnnotationConfiguration destinyConfig;
	
	/**
	 * Valor definido no arquivo de configuracao para o ponto de commit que a aplicacao deve
	 * seguir.
	 */
	private int commitPoint = Integer.parseInt(ResourceLocation.DATABASE_COMMIT_POINT);
	
	/**
	 * Pool de threads da aplicação
	 */
	public static Thread[] t = new Thread[5];
	
	/**
	 * Construtor da classe.
	 * Executa método que inicializa os componentes de configuração de anotações.
	 */
	public HibernateLoadProcess() {
		configInitialize();
	}
	
	/**
	 * Inicializa a configuração dos objetos annotationConfiguration de origem e destino
	 */
	private void configInitialize() {
		//
		// instancia annotation configuration do banco de origem
		sourceConfig  = new AnnotationConfiguration();
		sourceConfig.setProperty("hibernate.dialect", "org.hibernate.dialect.OracleDialect");
		sourceConfig.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
		sourceConfig.setProperty("hibernate.connection.driver_class", ResourceLocation.DATABASE_DRIVER_ORIGEM);
		sourceConfig.setProperty("hibernate.connection.username", ResourceLocation.DATABASE_USER_ORIGEM);
		sourceConfig.setProperty("hibernate.connection.password", ResourceLocation.DATABASE_PASS_ORIGEM);
		sourceConfig.setProperty("hibernate.connection.url", ResourceLocation.DATABASE_URL_ORIGEM);
		sourceConfig.setProperty("hibernate.connection.pool_size", ResourceLocation.DATABASE_MAX_CONNECTIONS+"");
		sourceConfig.setProperty("hibernate.query.substitutions", "true 1, false 0, yes 'Y', no 'N'");
		sourceConfig.setProperty("hibernate.format_sql", "false");
		sourceConfig.setProperty("hibernate.show_sql", "false");
		sourceConfig.setProperty("hibernate.proxool.pool_alias", "HibernatePool_01");
		sourceConfig.setProperty("hibernate.order_updates", "true");
		sourceConfig.setProperty("hibernate.max_fetch_depth", "100");
		sourceConfig.setProperty("hibernate.default_batch_fetch_size", commitPoint+"");
		sourceConfig.setProperty("hibernate.jdbc.batch_versioned_data", "false");
		sourceConfig.setProperty("hibernate.jdbc.use_streams_for_binary", "true");
		sourceConfig.setProperty("hibernate.cache.region_prefix", "hibernate.test");
		sourceConfig.setProperty("hibernate.cache.use_query_cache", "false");
		sourceConfig.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider");
		//
		// instancia annotation configuration do banco de destino
		destinyConfig = new AnnotationConfiguration();
		destinyConfig.setProperty("hibernate.dialect", "org.hibernate.dialect.OracleDialect");
		destinyConfig.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
		destinyConfig.setProperty("hibernate.connection.driver_class", ResourceLocation.DATABASE_DRIVER_DESTINO);
		destinyConfig.setProperty("hibernate.connection.username", ResourceLocation.DATABASE_USER_DESTINO);
		destinyConfig.setProperty("hibernate.connection.password", ResourceLocation.DATABASE_PASS_DESTINO);
		destinyConfig.setProperty("hibernate.connection.url", ResourceLocation.DATABASE_URL_DESTINO);
		destinyConfig.setProperty("hibernate.connection.pool_size", ResourceLocation.DATABASE_MAX_CONNECTIONS+"");
		destinyConfig.setProperty("hibernate.query.substitutions", "true 1, false 0, yes 'Y', no 'N'");
		destinyConfig.setProperty("hibernate.format_sql", "false");
		destinyConfig.setProperty("hibernate.show_sql", "false");
		destinyConfig.setProperty("hibernate.proxool.pool_alias", "HibernatePool_01");
		destinyConfig.setProperty("hibernate.order_updates", "true");
		destinyConfig.setProperty("hibernate.max_fetch_depth", "100");
		destinyConfig.setProperty("hibernate.default_batch_fetch_size", commitPoint+"");
		destinyConfig.setProperty("hibernate.jdbc.batch_versioned_data", "false");
		destinyConfig.setProperty("hibernate.jdbc.use_streams_for_binary", "true");
		destinyConfig.setProperty("hibernate.cache.region_prefix", "hibernate.test");
		destinyConfig.setProperty("hibernate.cache.use_query_cache", "false");
		destinyConfig.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider");
		destinyConfig.setProperty("hibernate.connection.autocommit", "true");
		//
	}
	
	/**
	 * Adiciona Classes anotadas nos objetos annotationConfiguration
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	public void addAnnotatedClass(Class clazz) {
		try {
			Map imports = sourceConfig.getImports();
			if (imports.get(clazz.getName()) == null) {
				// Adiciona classe anotada no objeto annotation configuration de origem
				sourceConfig.addAnnotatedClass(clazz);
				// Adiciona classe anotada no objeto annotation configuration de destino
				destinyConfig.addAnnotatedClass(clazz);
			}
			//
		} catch (MappingException e) {
			Log.info("Classe: " + clazz.getName() + " já cadastrada na lista de classes anotadas.");
		}
	}
	
	/**
	 * Após adicionar todas classes anotadas nos objetos de annotationConfiguration os factories 
	 * de origem e destino e os objetos sessions de origem e destino devem ser instanciados utilizando 
	 * esses objetos annotationConfiguration.
	 */
	public void sessionsInitialize() {
		try {
			// 
			if (sourceFactory == null) {
				// Inicializa origem
				sourceFactory = sourceConfig.buildSessionFactory();
				sourceSession = sourceFactory.openSession(ConnectionPoolManagerOrigem.getInstance().getConnection());
				//
				// Inicializa destino
				destinyFactory = destinyConfig.buildSessionFactory();
				destinySession = destinyFactory.openSession(ConnectionPoolManagerDestino.getInstance().getConnection());
			} else if (sourceFactory.isClosed()) {
				sourceSession = sourceFactory.openSession(ConnectionPoolManagerOrigem.getInstance().getConnection());
				destinySession = destinyFactory.openSession(ConnectionPoolManagerDestino.getInstance().getConnection());
			}
			//
		} catch (HibernateException e) {
			Log.error("Erro ao inicializar mecanismo Hibernate de comunicacao com o banco de dados.");
			Log.error(e);
		}
	}
	
	/**
	 * Encerra sessões com o banco de dados de origem e destino
	 */
	public void sessionsClose() {
		//
		// Fecha origem
		sourceFactory.close();
		sourceSession.close();
		//
		// Fecha destino
		destinySession.flush();
		destinyFactory.close();
		destinySession.close();
		//
	}
	
	/**
	 * Método responsável por retornar um objeto do tipo List<Object> contendo o resultado de uma query (consulta)
	 * no banco de origem de dados.
	 * @param criteriaExpression - HQL criteria expression preenchido
	 * @param obj - Objeto anotado que deverá ser retornado
	 * @return List<Object>
	 */
	@SuppressWarnings("unchecked")
	public List<? extends Object> getData(String criteriaExpression, List params) {
		// 
		// Realiza consulta e preenche um Objeto List<Object> com o resultado da consulta
		List<Object> list = null;
		try {
			Query query = sourceSession.createQuery(criteriaExpression);
			// Carrega lista de parâmetros da instrução SQL 
			if (params != null) {
				//
				for (int i = 0; i < params.size(); i++) {
					if (params.get(i) == null) {
						//
						query.setString(i, null);
						//
					} else if (params.get(i) instanceof String) {
						//
						query.setString(i, (String)params.get(i));
						//
					} else if (params.get(i) instanceof Integer) {
						//
						query.setInteger(i, (Integer)params.get(i));
						//
					} else if (params.get(i) instanceof Date) {
						//
						query.setDate(i, (Date)params.get(i));
						//
					} else if (params.get(i) instanceof Long) {
						//
						query.setLong(i, (Long)params.get(i));
						//
					} else if (params.get(i) instanceof Double) {
						//
						query.setDouble(i, (Double)params.get(i));
						//
					} else if (params.get(i) instanceof Float) {
						//
						query.setFloat(i, (Float)params.get(i));
						//
					}
				}
				//
			}
			list = query.list();
		} catch (HibernateException e) {
			Log.error("Erro ao realizar a consulta.");
			Log.error(e);
		}
		// 
		return list;
	}
	
	public Object getIdentifier(Object obj) {
		return sourceSession.getIdentifier(obj);
	}
	
	public void refresh(Object obj) {
		sourceSession.refresh(obj);
	}
	
	/**
	 * Método responsável por persistir o objeto no banco de destino inserindo ou atualizando.
	 * @param obj - Objeto anotado que deverá ser inserido ou atualizado.
	 */
	public void saveData(Object obj) {
		HibernateConcurrentExecution process = new HibernateConcurrentExecution(null, obj, destinySession); 
		try {
			int i = 0;
			while(true) {
				try {
					if (i >= (t.length)) {
						i = 0;
						Thread.sleep(10);
					} else if (t[i] == null) {
						t[i] = new Thread(process);
						//
						t[i].start();
						i++;
						break;
					} else if (!threadAtiva(t[i])) {
						t[i] = null;
						t[i] = new Thread(process);
						t[i].start();
						i++;
						break;
					} else {
						i++;
					}
				} catch (Throwable e) {
					Log.error("Erro ao criar a thread.");
					Log.error(e);
					t[i] = null;
				}
			}
		} catch (Exception e) {
			Log.error("Erro ao tentar criar a thread " + obj);
			Log.error(e);
		}

	}
	
	/**
	 * Bloco sincronizado que informa se a thread está ativa.
	 * @param thread
	 * @return
	 */
	public static boolean threadAtiva(Thread thread) {
		if (thread != null) {
			synchronized (thread) {
				if (thread != null && !thread.isAlive()) {
					return false;
				} else if (thread != null && thread.isAlive()) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
	
}
