package org.mtec.sistemacarga.framework.util;


/**
 * Classe utilizada para padronizar as localidades de arquivos de configura��o e constantes de texto
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 * @version 1.1 - 01/10/2007 - Inclusao de novas chaves para padronizacao da aplicacao.
 * @version 1.2 - 13/11/2010 - Inclusao de nova chave para definicao de pacote de filtro dinamico.
 */
public class ResourceLocation {
	
	/**
	 * Nome da aplica��o que ser� exibido na barra de t�tulo.
	 */
	public static final String TITLE_APPLICATION_NAME = "Sistema de Carga [v: 1.50]";
	
	/**
	 * T�tulo da tela de configura��o de conex�o.
	 */
	public static final String TITLE_CONFIGURAR_CONEXAO = "Configuracao de Conexao";
	
	/**
	 * T�tulo da janela de reprocessamento
	 */
	public static final String TITLE_REDO_WINDOW = "Reprocessamento de Carga";
	
	/**
	 * �cone: bookmark-next
	 */
	public static final String IMG_BOOKMARK_NEXT = "/org/mtec/sistemacarga/framework/gui/builder/icons/bookmark-next.png";
	
	/**
	 * �cone: edsTitle
	 */
	public static final String IMG_LOGO = "/org/mtec/sistemacarga/framework/gui/builder/icons/";
	
	/**
	 * �cone: nextlittle
	 */
	public static final String IMG_NEXT_LITTLE = "/org/mtec/sistemacarga/framework/gui/builder/icons/nextlittle.GIF";
	
	/**
	 * �cone: processingBar
	 */
	public static final String IMG_PROCESSING_BAR = "/org/mtec/sistemacarga/framework/gui/builder/icons/processingBar.gif";
	
	/**
	 * �cone: circleload
	 */
	public static final String IMG_CIRCLE_LOAD = "/org/mtec/sistemacarga/framework/gui/builder/icons/circleload.gif";
	
	/**
	 * Define a chave que deve ser lida no arquivo de propriedades para obter o pacote que deve ser lido pelo framework.
	 */
	public static final String PKG_CONF_KEY = "resources.filter.full_package_name";
	
	/**
	 * Define o nome do arquivo de configura��es da aplica��o
	 */
	public static final String CONF_PROPERTIES = "config";
	
	/**
	 * Define o local padr�o dos arquivos de configura��o da aplica��o.
	 */
	public static final String RESOURCES_PATH = "resources/";

	/**
     * Define o Look and Feel padr�o da aplica��o
	 */
	public static final String LNF = "com.jgoodies.looks.plastic.PlasticXPLookAndFeel";
//	public static final String LNF = "com.jgoodies.looks.windows.WindowsLookAndFeel";
//	public static final String LNF = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	
	/**
	 * Define o nome do arquivo XML utilizado para configura��o do dispositivo de log da aplica��o.
	 */
	public static final String LOG_CONFIG_XML = "LogConfig.xml";
	
	/**
	 * Define o diret�rio onde est�o os jar dos componentes de carga
	 */
	public static final String JAR_FILE_DIRECTORY = new String(ResourceUtil.getInstance().getProperty("resources.file.jar_file"));
	
	/**
	 * Define a chave que identifica a localidade do arquivo TNSNAMES.ORA
	 */
	public static final String KEY_TNSNAMES_DATABASE_PATH = "resources.database.tnsnames_database.path";
	
	/**
	 * Define a chave que identifica o usu�rio de origem
	 */
	public static final String KEY_USER_ORIGEM = "resources.database.user.origem";
	
	/**
	 * Define a chave que identifica o usu�rio de destino
	 */
	public static final String KEY_USER_DESTINO = "resources.database.user.destino";
	
	/**
	 * Define a chave que identifica a senha do usu�rio de origem
	 */
	public static final String KEY_PASS_ORIGEM = "resources.database.pass.origem";
	
	/**
	 * Define a chave que identifica a senha do usu�rio de destino
	 */
	public static final String KEY_PASS_DESTINO = "resources.database.pass.destino";
	
	/**
	 * Define a chave que identifica a URL do banco de origem
	 */
	public static final String KEY_URL_ORIGEM = "resources.database.url.origem";
	
	/**
	 * Define a chave que identifica a senha do usu�rio de destino
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
	 * Define a string que identifica o driver de conex�o com o banco MS-Access
	 */
	public static final String STRING_DRIVER_MSACCESS = "sun.jdbc.odbc.JdbcOdbcDriver";
	
	/**
	 * Define a string que identifica o driver de conex�o com o banco Oracle
	 */
	public static final String STRING_DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
	
	/**
	 * Define a string que identifica o driver de conex�o com o banco mySql
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
	 * OBS.: Ap�s o @ devem ser inseridas informa��es do banco de dados, 
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
	public static String DATABASE_DRIVER_ORIGEM = new String(ResourceUtil.getInstance().getProperty(KEY_DRIVER_ORIGEM));
	
	/**
	 * Define o driver do banco de dados de destino
	 */
	public static String DATABASE_DRIVER_DESTINO = new String(ResourceUtil.getInstance().getProperty(KEY_DRIVER_DESTINO));
	
	/**
	 * Define o usu�rio do banco de dados de origem
	 */
	public static String DATABASE_USER_ORIGEM = new String(ResourceUtil.getInstance().getProperty(KEY_USER_ORIGEM));
	
	/**
	 * Define o driver do banco de dados de destino
	 */
	public static String DATABASE_USER_DESTINO = new String(ResourceUtil.getInstance().getProperty(KEY_USER_DESTINO));
	
	/**
	 * Define a senha do usu�rio do banco de dados de origem
	 */
	public static String DATABASE_PASS_ORIGEM = new String(ResourceUtil.getInstance().getProperty(KEY_PASS_ORIGEM));
	
	/**
	 * Define a senha do usu�rio do banco de dados de destino
	 */
	public static String DATABASE_PASS_DESTINO = new String(ResourceUtil.getInstance().getProperty(KEY_PASS_DESTINO));
	
	/**
	 * Define o caminho do banco de dados de origem
	 */
	public static String DATABASE_URL_ORIGEM = new String(ResourceUtil.getInstance().getProperty(KEY_URL_ORIGEM));

	/**
	 * Define o caminho do banco de dados de origem
	 */
	public static String DATABASE_URL_DESTINO = new String(ResourceUtil.getInstance().getProperty(KEY_URL_DESTINO));
	
	/**
	 * Define o n�mero m�ximo de conex�es permitidas
	 */
	public static int DATABASE_MAX_CONNECTIONS = Integer.parseInt(ResourceUtil.getInstance().getProperty("resources.database.config.maxpoolsize").trim());
	
	/**
	 * Define o n�mero inicial de conex�es
	 */
	public static int DATABASE_INI_CONNECTIONS = Integer.parseInt(ResourceUtil.getInstance().getProperty("resources.database.config.poolsize").trim());
	
	/**
	 * Define a localidade do arquivo TNS_NAMES
	 */
	public static String DATABASE_TNSNAMES_DATABASE_PATH = new String(ResourceUtil.getInstance().getProperty(KEY_TNSNAMES_DATABASE_PATH));
	
	/**
	 * Define o inicio do pacote de um componente de carga
	 */
	public static final String PKG_NAME = new String(ResourceUtil.getInstance().getProperty(PKG_CONF_KEY));
	
	/**
	 * Define a chave do ponto de commit que a aplicacao deve seguir.
	 */
	public static String CONF_DATABASE_COMMIT_POINT = "resources.database.commit_point";
	
	/**
	 * Define o valor do ponto de commit da aplicacao.
	 */
	public static String DATABASE_COMMIT_POINT = new String(ResourceUtil.getInstance().getProperty(CONF_DATABASE_COMMIT_POINT));
}
