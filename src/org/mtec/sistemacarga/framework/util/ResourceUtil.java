package org.mtec.sistemacarga.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.mtec.sistemacarga.framework.vo.ConfigurationVO;

/**
 * <strong><h1>Definição</h1></strong>
 * <br/>
 * <br/>
 * <pre>
 * O objetivo desta classe é ler e gravar pares de chave/valor em
 * arquivos de propriedades.
 * @author Antonio Cesar
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 09/04/2007
 */
public class ResourceUtil {
	
	/**
	 * Collection para representar repositório de objetos properties
	 */
	@SuppressWarnings("unchecked")
	private static Map mapProperties;
	
	/**
	 * Implementação do pattern de Singleton
	 */
	private static ResourceUtil instance = new ResourceUtil();
	
	/**
	 * Ponteiro que direciona para o arquivo de propriedades informado
	 */
	private File f;
	
	/**
	 * Ponteiro que direciona para o arquivo de propriedades criptografado
	 */
	private File fDat;
	
	/**
	 * Objeto que representa o arquivo de configuracoes.
	 */
	private ConfigurationVO conf;
	
	/**
	 * Variável de controle para cerificacao se o arquivo de propriedades criptografado ja foi salvo.
	 */
	private boolean hasSafeSave = false;
	
	/**
	 * É necessário que o construtor default seja privado para
	 * a implementação do pattern de Singleton
	 */
	@SuppressWarnings("unchecked")
	private ResourceUtil() {
		mapProperties = new HashMap();
		
		if (!hasSafeSave) {
			hasSafeSave = true;
			if (f == null) {
				f = new File(ResourceLocation.RESOURCES_PATH + ResourceLocation.CONF_PROPERTIES + ".properties");	
			}
			if (fDat == null) {
				fDat = new File(ResourceLocation.RESOURCES_PATH + ResourceLocation.CONF_PROPERTIES + ".properties.dat");	
			}
			
			if (!fDat.exists()) {
				propertySafeSave();
			} else {
				loadSafeSaveConfig();
			}
		}
	}
	
	public static ResourceUtil getInstance() {
		return instance;
	}
	
	private void propertySafeSave() {
		if (f.exists()) {
			try {
				if (!fDat.exists()) {
					ConfigurationVO conf = setConfiguracoesVO();
					
					ObjectOutputStream out = new ObjectOutputStream(
							new FileOutputStream(fDat));
					out.writeObject(conf);
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				Log.error("Ocorreu um erro de escrita no arquivo [" + f.getAbsolutePath() + ".properties]!");
				Log.error( e );
			}
		}
	}

	private ConfigurationVO setConfiguracoesVO() {
		ConfigurationVO conf = new ConfigurationVO();
		conf.setFullPackageName(new StringBuffer(this.getProperty("resources.filter.full_package_name")));
		conf.setJarFilesDir(new StringBuffer(this.getProperty("resources.file.jar_file")));
		
		conf.setLogXmlAppenderPath(new StringBuffer(this.getProperty("log.xmlAppender.path")));
		conf.setLogFileAppenderPath(new StringBuffer(this.getProperty("log.fileAppender.path")));
		conf.setLogRefreshTime(new StringBuffer(this.getProperty("log.refreshTime")));

		conf.setDataBaseTnsNamesDataBasePath(new StringBuffer(this.getProperty("resources.database.tnsnames_database.path")));
		conf.setDataBaseConfigPoolSize(new StringBuffer(this.getProperty("resources.database.config.poolsize")));
		conf.setDataBaseConfigMaxPoolSize(new StringBuffer(this.getProperty("resources.database.config.maxpoolsize")));
		conf.setDataBaseCommitPoint(new StringBuffer(this.getProperty("resources.database.commit_point")));
		
		conf.setDataBaseDriverOrigem(new StringBuffer(this.getProperty("resources.database.driver.origem")));
		conf.setDataBaseUrlOrigem(new StringBuffer(this.getProperty("resources.database.url.origem")));
		conf.setDataBaseUserOrigem(new StringBuffer(this.getProperty("resources.database.user.origem")));
		conf.setDataBasePasswordOrigem(new StringBuffer(this.getProperty("resources.database.pass.origem")));
		
		conf.setDataBaseDriverDestino(new StringBuffer(this.getProperty("resources.database.driver.destino")));
		conf.setDataBaseUrlDestino(new StringBuffer(this.getProperty("resources.database.url.destino")));
		conf.setDataBaseUserDestino(new StringBuffer(this.getProperty("resources.database.user.destino")));
		conf.setDataBasePasswordDestino(new StringBuffer(this.getProperty("resources.database.pass.destino")));
		return conf;
	}
	
	@SuppressWarnings("unchecked")
	private void loadSafeSaveConfig() {
		if (fDat.exists()) {
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(fDat));
				conf = (ConfigurationVO) in.readObject();
				in.close();
				Properties prop = getConfiguracoesVO(conf);
				mapProperties.put(ResourceLocation.CONF_PROPERTIES, prop);
			} catch (FileNotFoundException e) {
				Log.error("Arquivos de propriedades criptografado nao encontrado.");
				Log.error(e);
			} catch (IOException e) {
				Log.error("Falha ao abrir o arquivo de propriedades criptografado.");
				Log.error(e);
			} catch (ClassNotFoundException e) {
				Log.error("Objeto de configuracao nao encontrado.");
				Log.error(e);
			}
		}
	}

	private Properties getConfiguracoesVO(ConfigurationVO conf) {
		Properties prop = new Properties();
		
		prop.setProperty("resources.filter.full_package_name", conf.getFullPackageName().toString());
		prop.setProperty("resources.file.jar_file", conf.getJarFilesDir().toString());
		prop.setProperty("log.xmlAppender.path", conf.getLogXmlAppenderPath().toString());
		prop.setProperty("log.fileAppender.path", conf.getLogFileAppenderPath().toString());
		prop.setProperty("log.refreshTime", conf.getLogRefreshTime().toString());
		prop.setProperty("resources.database.tnsnames_database.path", conf.getDataBaseTnsNamesDataBasePath().toString());
		prop.setProperty("resources.database.config.poolsize", conf.getDataBaseConfigPoolSize().toString());
		prop.setProperty("resources.database.config.maxpoolsize", conf.getDataBaseConfigMaxPoolSize().toString());
		prop.setProperty("resources.database.commit_point", conf.getDataBaseCommitPoint().toString());
		prop.setProperty("resources.database.driver.origem", conf.getDataBaseDriverOrigem().toString());
		prop.setProperty("resources.database.url.origem", conf.getDataBaseUrlOrigem().toString());
		prop.setProperty("resources.database.user.origem", conf.getDataBaseUserOrigem().toString());
		prop.setProperty("resources.database.pass.origem", conf.getDataBasePasswordOrigem().toString());
		prop.setProperty("resources.database.driver.destino", conf.getDataBaseDriverDestino().toString());
		prop.setProperty("resources.database.url.destino", conf.getDataBaseUrlDestino().toString());
		prop.setProperty("resources.database.user.destino", conf.getDataBaseUserDestino().toString());
		prop.setProperty("resources.database.pass.destino", conf.getDataBasePasswordDestino().toString());
		
		return prop;
	}
	
	/**
	 * <strong>Definição</strong>
	 * <br/>
	 * <br/>
	 * Este método deve retornar o valor de uma key no arquivo de propriedades.
	 * @param arquivoProperty
	 * @param key
	 * @return value
	 */
	@SuppressWarnings("unchecked")
	public String getProperty(String key) {
		String retorno = null;
		if (mapProperties.containsKey(ResourceLocation.CONF_PROPERTIES) ) {
			retorno = ((Properties)mapProperties.get(ResourceLocation.CONF_PROPERTIES)).getProperty(key);
		} else {
			try {
				Properties prop = new Properties();
				Log.info("Processando a carga do arquivo [" + ResourceLocation.CONF_PROPERTIES + ".properties] em getProperty()...");
				FileInputStream fis = new FileInputStream( f );
				prop.load( fis );
				fis.close();
				mapProperties.put(ResourceLocation.CONF_PROPERTIES, prop);
				Log.info("Arquivo de propriedades: " + f.getAbsolutePath());
				retorno = ((Properties)mapProperties.get(ResourceLocation.CONF_PROPERTIES)).getProperty(key);
				

			} catch(Exception e) {
				Log.error("Ocorreu um erro na carga do arquivo: resources/" + ResourceLocation.CONF_PROPERTIES + ".properties");
			}
		}
		return retorno;
	}
	
	/**
	 * <strong>Definição</strong>
	 * <br/>
	 * <br/>
	 * Este método deve setar o valor de uma key no arquivo de propriedades.
	 * @param arquivoProperty
	 * @param key
	 * @param value
	 * @return true se obter sucesso na gravação ou false se houverem erros.
	 */
	@SuppressWarnings("unchecked")
	public boolean setProperty(String key, String value) {
		boolean retorno = false;
		Properties props = null;
		if (mapProperties.containsKey(ResourceLocation.CONF_PROPERTIES) ) {
			props = (Properties) mapProperties.get(ResourceLocation.CONF_PROPERTIES);
		} else {
			try {
				FileInputStream fis = new FileInputStream(f);
				props = new Properties();
				Log.info("Processando a carga do arquivo [" + ResourceLocation.CONF_PROPERTIES + "] de propriedades em setProperty()...");
				props.load( fis );
				mapProperties.put(ResourceLocation.CONF_PROPERTIES, props);
			} catch(Exception e) {
				Log.info("Ocorreu um erro na carga do arquivo: resources/" + ResourceLocation.CONF_PROPERTIES + ".properties");
			}
		}
		if (props != null) {
			try {
				FileOutputStream fos = new FileOutputStream( f );
				props.setProperty(key, value);
				props.store( fos, "Configuração registrada pelo componente " + getClass().getName() );
				fos.flush();
				fos.close();
				retorno = true;
			} catch (FileNotFoundException e) {
				Log.error("Ocorreu um erro na abertura do arquivo [" + ResourceLocation.CONF_PROPERTIES + ".properties]!");
				Log.error( e );
			} catch (IOException e) {
				Log.error("Ocorreu um erro de escrita no arquivo [" + ResourceLocation.CONF_PROPERTIES + ".properties]!");
				Log.error( e );
			}
		}
		return retorno;
	}

}
