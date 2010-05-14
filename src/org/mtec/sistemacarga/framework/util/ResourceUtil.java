package org.mtec.sistemacarga.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
	 * É necessário que o construtor default seja privado para
	 * a implementação do pattern de Singleton
	 *
	 */
	private ResourceUtil() {
		mapProperties = new HashMap();
	}
	
	public static ResourceUtil getInstance() {
		return instance;
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
	public String getProperty(String arquivoProperty, String key) {
		String retorno = null;
		//
		f = new File(ResourceLocation.RESOURCES_PATH + arquivoProperty.concat(".properties"));
		//
		if (mapProperties.containsKey(arquivoProperty) ) {
			retorno = ((Properties)mapProperties.get(arquivoProperty)).getProperty(key);
		} else {
			try {
				Properties prop = new Properties();
				Log.info("Processando a carga do arquivo [" + arquivoProperty + ".properties] em getProperty()...");
				//
				FileInputStream fis = new FileInputStream( f );
				//
				prop.load( fis );
				fis.close();
				mapProperties.put(arquivoProperty, prop);
				
				System.out.println(f.getAbsolutePath());
				
				retorno = ((Properties)mapProperties.get(arquivoProperty)).getProperty(key);
			} catch(Exception e) {
				Log.error("Ocorreu um erro na carga do arquivo: resources/" + arquivoProperty + ".properties");
			}
		}
		//Log.info(this, "Buscando valor de " + key + " em " + arquivoProperty + ". Encontrado: " + retorno);
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
	public boolean setProperty(String arquivoProperty, String key, String value) {
		boolean retorno = false;
		//
		f = new File(ResourceLocation.RESOURCES_PATH + arquivoProperty.concat(".properties"));
		//
		Properties props = null;
		if (mapProperties.containsKey(arquivoProperty) ) {
			props = (Properties) mapProperties.get(arquivoProperty);
		} else {
			try {
				//
				FileInputStream fis = new FileInputStream(f);
				//
				props = new Properties();
				Log.info("Processando a carga do arquivo [" + arquivoProperty + "] de propriedades em setProperty()...");
				props.load( fis );
				mapProperties.put(arquivoProperty, props);
			} catch(Exception e) {
				Log.info("Ocorreu um erro na carga do arquivo: resources/" + arquivoProperty + ".properties");
			}
		}
		if (props != null) {
			try {
				FileOutputStream fos = new FileOutputStream( f );
				//
				props.setProperty(key, value);
				props.store( fos, "Configuração registrada pelo componente " + getClass().getName() );
				fos.flush();
				fos.close();
				retorno = true;
			} catch (FileNotFoundException e) {
				Log.error("Ocorreu um erro na abertura do arquivo [" + arquivoProperty + ".properties]!");
				Log.error( e );
			} catch (IOException e) {
				Log.error("Ocorreu um erro de escrita no arquivo [" + arquivoProperty + ".properties]!");
				Log.error( e );
			}
		}
		return retorno;
	}

}
