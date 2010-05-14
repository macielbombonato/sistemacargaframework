package org.mtec.sistemacarga.framework.util;

/**
 * Classe utilizada para padronizar as localidades de arquivos de configuração e constantes de texto
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 * @version 1.1 - 01/10/2007 - Inclusão de novas chaves para padronização da aplicação.
 */
public class ResourceLocation {
	
	/**
	 * Nome da aplicação que será exibido na barra de título.
	 */
	public static final String TITLE_APPLICATION_NAME = "Sistema de Carga [v: 1.40]";
	
	/**
	 * Título da tela de configuração de conexão.
	 */
	public static final String TITLE_CONFIGURAR_CONEXAO = "Configuração de Conexão";
	
	/**
	 * Título da janela de reprocessamento
	 */
	public static final String TITLE_REDO_WINDOW = "Reprocessamento de Carga";
	
	/**
	 * Ícone: bookmark-next
	 */
	public static final String IMG_BOOKMARK_NEXT = "/org/mtec/sistemacarga/framework/gui/builder/icons/bookmark-next.png";
	
	/**
	 * Ícone: edsTitle
	 */
	public static final String IMG_LOGO = "/org/mtec/sistemacarga/framework/gui/builder/icons/";
	
	/**
	 * Ícone: nextlittle
	 */
	public static final String IMG_NEXT_LITTLE = "/org/mtec/sistemacarga/framework/gui/builder/icons/nextlittle.GIF";
	
	/**
	 * Ícone: processingBar
	 */
	public static final String IMG_PROCESSING_BAR = "/org/mtec/sistemacarga/framework/gui/builder/icons/processingBar.gif";
	
	/**
	 * Ícone: circleload
	 */
	public static final String IMG_CIRCLE_LOAD = "/org/mtec/sistemacarga/framework/gui/builder/icons/circleload.gif";
	
	/**
	 * Define o inicio do pacote de um componente de carga
	 */
	public static final String PKG_NAME = "com/eds";
	
	/**
	 * Define o nome do arquivo de configurações da aplicação
	 */
	public static final String CONF_PROPERTIES = "config";
	
	/**
	 * Define o local padrão dos arquivos de configuração da aplicação.
	 */
	public static final String RESOURCES_PATH = "resources/";

	/**
  * Define o Look and Feel padrão da aplicação
	 */
	public static final String LNF = "com.jgoodies.looks.plastic.PlasticXPLookAndFeel";
	
	/**
	 * Define o nome do arquivo XML utilizado para configuração do dispositivo de log da aplicação.
	 */
	public static final String LOG_CONFIG_XML = "LogConfig.xml";
	
	/**
	 * Define o diretório onde estão os jar dos componentes de carga
	 */
	public static final String JAR_FILE_DIRECTORY = new String(ResourceUtil.getInstance().getProperty(CONF_PROPERTIES, "resources.file.jar_file"));
	
	/**
	 * Define a chave que identifica a localidade do arquivo TNSNAMES.ORA
	 */
	public static final String KEY_TNSNAMES_DATABASE_PATH = "resources.database.tnsnames_database.path";
	
	/**
	 * Define a chave que identifica o usuário de origem
	 */
	public static final String KEY_USER_ORIGEM = "resources.database.user.origem";
	
	/**
	 * Define a chave que identifica o usuário de destino
	 */
	public static final String KEY_USER_DESTINO = "resources.database.user.destino";
	
	/**
	 * Define a chave que identifica a senha do usuário de origem
	 */
	public static final String KEY_PASS_ORIGEM = "resources.database.pass.origem";
	
	/**
	 * Define a chave que identifica a senha do usuário de destino
	 */
	public static final String KEY_PASS_DESTINO = "resources.database.pass.destino";
	
	/**
	 * Define a chave que identifica a URL do banco de origem
	 */
	public static final String KEY_URL_ORIGEM = "resources.database.url.origem";
	
	/**
	 * Define a chave que identifica a senha do usuário de destino
	 */
	public static final String KEY_URL_DESTINO = "resources.database.url.destino";
	
	/**
	 * Define a chave que identifica o driver do banco de origem
	 */
	public static final String KEY_DRIVER_ORIGEM = "resources.database.driver.origem";
	
	/**
	 * Define a chave que identifica o driver do banco de destino
	 */
	public static final String KEY_DRIVER_DESTINO = "resources.database.driver.destino";
	
	/**
	 * Define a string que identifica o driver de conexão com o banco MS-Access
	 */
	public static final String STRING_DRIVER_MSACCESS = "sun.jdbc.odbc.JdbcOdbcDriver";
	
	/**
	 * Define a string que identifica o driver de conexão com o banco Oracle
	 */
	public static final String STRING_DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
	
	/**
	 * Define a string que identifica o driver de conexão com o banco mySql
	 */
	public static final String STRING_DRIVER_MYSQL = "oracle.jdbc.driver.OracleDriver";
	
	/**
	 * Define a string que identifica a url que deve ser utilizada para o banco MS-Access
	 * OBS.: O desta string deve ser preenchido com o caminho do banco, 
	 * por exemplo, jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=d:\banco.mdb
	 */
	public static final String STRING_URL_MSACCESS = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
	
	/**
	 * Define a string que identifica a url que deve ser utilizada para o banco Oracle
	 * OBS.: Após o @ devem ser inseridas informações do banco de dados, 
	 * por exemplo, jdbc:oracle:thin:@10.0.0.1:1521:bancoteste
	 */
	public static final String STRING_URL_ORACLE = "jdbc:oracle:thin:@";
	
	/**
	 * Define a string que identifica a url que deve ser utilizada para o banco mySql
	 */
	public static final String STRING_URL_MYSQL = "oracle.jdbc.driver.OracleDriver";
	
	/**
	 * Define o driver do banco de dados de origem
	 */
	public static String DATABASE_DRIVER_ORIGEM = new String(ResourceUtil.getInstance().getProperty(CONF_PROPERTIES, KEY_DRIVER_ORIGEM));
	
	/**
	 * Define o driver do banco de dados de destino
	 */
	public static String DATABASE_DRIVER_DESTINO = new String(ResourceUtil.getInstance().getProperty(CONF_PROPERTIES, KEY_DRIVER_DESTINO));
	
	/**
	 * Define o usuário do banco de dados de origem
	 */
	public static String DATABASE_USER_ORIGEM = new String(ResourceUtil.getInstance().getProperty(CONF_PROPERTIES, KEY_USER_ORIGEM));
	
	/**
	 * Define o driver do banco de dados de destino
	 */
	public static String DATABASE_USER_DESTINO = new String(ResourceUtil.getInstance().getProperty(CONF_PROPERTIES, KEY_USER_DESTINO));
	
	/**
	 * Define a senha do usuário do banco de dados de origem
	 */
	public static String DATABASE_PASS_ORIGEM = new String(ResourceUtil.getInstance().getProperty(CONF_PROPERTIES, KEY_PASS_ORIGEM));
	
	/**
	 * Define a senha do usuário do banco de dados de destino
	 */
	public static String DATABASE_PASS_DESTINO = new String(ResourceUtil.getInstance().getProperty(CONF_PROPERTIES, KEY_PASS_DESTINO));
	
	/**
	 * Define o caminho do banco de dados de origem
	 */
	public static String DATABASE_URL_ORIGEM = new String(ResourceUtil.getInstance().getProperty(CONF_PROPERTIES, KEY_URL_ORIGEM));

	/**
	 * Define o caminho do banco de dados de origem
	 */
	public static String DATABASE_URL_DESTINO = new String(ResourceUtil.getInstance().getProperty(CONF_PROPERTIES, KEY_URL_DESTINO));
	
	/**
	 * Define o número máximo de conexões permitidas
	 */
	public static int DATABASE_MAX_CONNECTIONS = Integer.parseInt(ResourceUtil.getInstance().getProperty(ResourceLocation.CONF_PROPERTIES, "resources.database.config.maxpoolsize").trim());
	
	/**
	 * Define o número inicial de conexões
	 */
	public static int DATABASE_INI_CONNECTIONS = Integer.parseInt(ResourceUtil.getInstance().getProperty(ResourceLocation.CONF_PROPERTIES, "resources.database.config.poolsize").trim());
	
	/**
	 * Define a localidade do arquivo TNS_NAMES
	 */
	public static String DATABASE_TNSNAMES_DATABASE_PATH = new String(ResourceUtil.getInstance().getProperty(ResourceLocation.CONF_PROPERTIES, KEY_TNSNAMES_DATABASE_PATH));
}
