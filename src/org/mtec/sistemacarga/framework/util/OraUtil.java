/**
 *  Copyright (c) 2007 EDS Electronic Data System do Brasil LTDA
 *
 *  This software is not designed or intended for use in
 *  the design, construction, operation or maintenance of any nuclear
 *  facility. Licensee represents and warrants that it will not use or
 *  redistribute the Software for such purposes.
 *
 *  This software is the proprietary information of EDS Sistems.
 */
package org.mtec.sistemacarga.framework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.mtec.sistemacarga.framework.exception.SisCarException;
import org.mtec.sistemacarga.framework.sorts.KeysComparator;



/**
 * <strong><h1>Definição</h1></strong>
 * <br/>
 * <br/>
 * <pre>
 * Esta classe tem como objetivo, ler o arquivo TNS_NAMES que representa o
 * repositório de endereços dos bancos de dados registrados, podendo ser
 * mapeados por <i>alias</i>. O método público <i>getAliasMap()</i> deve
 * retornar um <u>java.util.Map</u> ao ser invocado, sendo populado conforme
 * a representação abaixo:
 * </pre>
 * <table border="1" width="90%">
 *     <tr>
 *         <td>
 *             <strong>KEY</strong>
 *         </td>
 *         <td>
 *             <strong>VALUE</strong>
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>DV28TAM</td>
 *         <td>jdbc:oracle:thin:@16.168.5.118:1521:dv28</td>
 *     </tr>
 *     <tr>
 *         <td>DV32EDS</td>
 *         <td>jdbc:oracle:thin:@148.91.106.33:3580:dv32</td>
 *     </tr>
 *     <tr>
 *         <td>FB01</td>
 *         <td>jdbc:oracle:thin:@16.168.5.8:1521:fb01</td>
 *     </tr>
 *     <tr>
 *         <td></td>
 *         <td></td>
 *     </tr>
 * </table>
 * <br/>
 *  
 * @author Antonio Cesar
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 13/02/2007
 * @version 1.1 - 27/09/2007 - Inclusão da opção SERVICE_NAME na obtenção do banco de dados e correções na busca do Alias.
 *
 */
public class OraUtil {
	/**
	 * Constante que define os caracteres alfas (letras) permitidas
	 */
	private static final String ALFA_CHARS = "ABCDEFGHJIKLMNOPQRSTUVXZWY";
	
	/**
	 * Constante que define os caracteres numéricos permitidos
	 */
	private static final String NUMERIC_CHARS = "9876543210";
	
	/**
	 * Constante que define os caracteres alfa numéricos permitidos
	 */
	private static final String ALFA_NUMERIC_CHARS = ALFA_CHARS + NUMERIC_CHARS;
	
	/**
	 * Constante que define os caracteres permitidos para montagem do nome
	 * do Host, lembrando que o nome de um servidor pode ser formado por 
	 * seu domínio (ex.: servidor.amer.corp.eds.com) ou por seu 
	 * endereco IP (ex.: 16.168.5.8)
	 */
	private static final String VALID_CHARS_FOR_HOST = ALFA_NUMERIC_CHARS + ".";
	
	/**
	 * Constante que define os caracteres válidos para busca de informações.
	 * Estes caracteres serão analisados no início do processo para verificar se
	 * a linha que está sendo lida do arquivo TNS_NAMES pode ser processada para
	 * extração de informações como ALIAS, HOST, PORTA e SID. Dessa forma,
	 * as linhas com comentários e afins devem ser ignoradas.
	 */
	private static final String VALID_CHARS_FOR_SEARCH = ALFA_NUMERIC_CHARS + "()"; 
	
	/**
	 * Constante para implementação do pattern de Singleton
	 */
	private static final OraUtil instance = new OraUtil();
	
	/**
	 * É necessário que o construtor default seja privado para
	 * implementação do pattern de Singleton 
	 */
	private OraUtil() {}
	
	public static OraUtil getInstance() {
		return instance;
	}
	
	/**
	 * <strong>Definição</strong>
	 * <br/>
	 * <br/>
	 * O objetivo deste método é efetivamente ler o arquivo TNS_NAMES do path
	 * informado. Após realizado o processamento, deverá ser retornado um Map
	 * conforme apresentado no início desta documentação. 
	 * @param path - Caminho do arquivo TNS_NAMES
	 * @return Map contendo pares de chave/valor para os alias
	 * @throws SisCarException
	 */
	@SuppressWarnings("unchecked")
	public Map getAliasMap(String path) throws SisCarException {
		Map    map = new TreeMap( new KeysComparator() );
		String linha = null;
		//
		String alias = null; 
		String host  = null;
		String porta = null;
		String sid   = null;
		//
		String aliasAux = null;
		//
		// Opção para o caso do alias possui conexão com portas diferentes
		String alias2 = null; 
		String host2  = null;
		String porta2 = null;
		String sid2   = null;
		//
		BufferedReader buffer = abreArquivo( path );
		//
		try {
			while ( (linha = buffer.readLine()) != null ) {
				linha = linha.trim().toUpperCase();
				if ( !isLinhaValida(linha) ) { continue; }
				//
				// Verificação se o ponteiro está em outro alias
				aliasAux = getAliasBanco( linha );
				if (aliasAux != null && alias!= null && !alias.equalsIgnoreCase(aliasAux)) {
					//
					alias = null;
					host  = null;
					porta = null;
					sid   = null;
					//
					alias2 = null;
					host2  = null;
					porta2 = null;
					sid2   = null;
					//
				}
				//
				if (alias == null) {
					alias = getAliasBanco( linha );
					if (alias != null) {
						alias2 = alias;
					}
				}
				if (host == null) {
					host = getHostBanco( linha );
					if (host != null) {
						host2 = host;
					}
				}
				if (porta == null) {
					porta = getPortBanco( linha );
					if (porta != null) {
						alias += " --> "+ porta;
					}
					if (porta != null && porta2 == null) {
						porta2 = getPortBanco( linha );
						if (porta.equalsIgnoreCase(porta2)) {
							porta2 = null;
						}
						if (porta2 != null) {
							alias2 += "-"+ porta2;
						}
					}
				} 
				if (porta != null && porta2 == null) {
					porta2 = getPortBanco( linha );
					if (porta.equalsIgnoreCase(porta2)) {
						porta2 = null;
					}
					if (porta2 != null) {
						alias2 += "-"+ porta2;
					}
				} 
				if (sid == null) {
					sid = getSIDBanco( linha );
					if (sid != null) {
						sid2 = sid;
					}
					/*
					 * Caso o SID tenha sido encontrado, porém
					 * a porta não, significa que haverão erros na montagem do
					 * mapeamento porque os dados no arquivo TNS divergem do esperado.
					 * Sendo assim, deve-se abortar esta configuração.
					 */
					if (sid != null && porta == null) {
						Log.info("Abortando mapeamento para o alias [" + alias + "]");
						alias = null;
						host  = null;
						porta = null;
						sid   = null;
						//
						alias2 = null;
						host2  = null;
						porta2 = null;
						sid2   = null;
					}
				}
				//
				if (alias != null &&
					host  != null &&
					porta != null &&
					sid   != null ) {
					/*
					 * DB path==> jdbc:oracle:thin:@server:1521:banco
					 */
					//Log.info("Encontrado o alias [" + alias + "] com [" + host + ":" + porta + ":" + sid + "]");
					map.put(alias, "jdbc:oracle:thin:@" + host + ":" + porta + ":" + sid);
					//
				}
				//
				if (alias2 != null &&
					host2  != null &&
					porta2 != null &&
					sid2   != null ) {
					/*
					 * DB path==> jdbc:oracle:thin:@server:1521:banco
					 */
					//Log.info("Encontrado o alias [" + alias + "] com [" + host + ":" + porta + ":" + sid + "]");
					map.put(alias2, "jdbc:oracle:thin:@" + host2 + ":" + porta2 + ":" + sid2);
					//
				}
			}
		} catch (IOException e) {
			Log.error("Erro na leitura do buffer!");
			throw new SisCarException("Ocorreu um erro na leitura do buffer!", e);
		} finally {
			try {
				buffer.close();
			} catch (IOException e) {
				Log.error("Um erro ocorreu no fechamento do buffer!");
			}
		}
		return map;
	}
	
	/**
	 * <strong>Definição</strong>
	 * <br/>
	 * <br/>
	 * O objetivo deste método é abrir o arquivo TNS_NAMES
	 * e devolver uma instância de um buffer de leitura para
	 * iteração com o mesmo
	 * @param path - Caminho do arquivo TNS_NAMES
	 * @return Buffer para leitura do arquivo
	 * @throws SisCarException
	 */
	private BufferedReader abreArquivo(String path) throws SisCarException {
		BufferedReader buffer = null;
		try {
			File f = new File( path );
			//
			if ( !f.exists() ) {
				throw new SisCarException("O arquivo TNSNAMES.ORA não foi encontrado no caminho informado!", 7100 );
			}
			if ( !f.isFile() ) {
				throw new SisCarException("O arquivo TNSNAMES.ORA não é um arquivo válido no caminho especificado!", 7101 );
			}
			if ( !f.canRead() ) {
				throw new SisCarException("O sistema não possui previlégio de leitura ao arquivo TNSNAMES.ORA no caminho especificado!", 7102 );
			}
			try {
				buffer = new BufferedReader( new FileReader(f) );
			} catch (FileNotFoundException e) {
				Log.error("Ocorreu um erro inesperado ao ler o arquivo!");
			}
		} catch (Exception e) {
			Log.error("Ocorreu um erro inesperado ao ler o arquivo!");
			throw new SisCarException("Ocorreu um erro inesperado!", 7103, e);
		}
		return buffer;
	}
	
	/**
	 * <strong>Definição</strong>
	 * <br/>
	 * <br/>
	 * O objetivo deste método é extrair o alias da String
	 * informada. Caso não exista, <i><u>null</u></i> será
	 * retornado.
	 * @param linha - String a ser analisada
	 * @return Valor encontrado ou <i><u>null</u></i>.
	 */
	private String getAliasBanco(String linha) {
		String alias = "";
		int i = 0;
		if ((ALFA_NUMERIC_CHARS.indexOf( linha.charAt(0) ) > 0) || (linha.charAt(0) == 'A')) {
			i = 0;
			while ( linha.charAt(i) != '='   &&   linha.charAt(i) != ' ' ) {
				alias += linha.charAt( i++ );
			}
		} else {
			alias = null;
		}
		return alias;
	}
	
	/**
	 * <strong>Definição</strong>
	 * <br/>
	 * <br/>
	 * O objetivo deste método é extrair o host da String
	 * informada. Caso não exista, <i><u>null</u></i> será
	 * retornado.
	 * @param linha - String a ser analisada
	 * @return Valor encontrado ou <i><u>null</u></i>.
	 */
	private String getHostBanco(String linha) {
		String host = "";
		if ( linha.indexOf("HOST") > 0) {
			int i = linha.indexOf( "HOST" );
			/*
			 * Os dois blocos while abaixo são necessários para posicionar o índice
			 * exatamente na posição em que inicia-se a informação procurada
			 */
			while (linha.charAt(i) != '=' && i < linha.length()) { i++; }
			while ( VALID_CHARS_FOR_HOST.indexOf(linha.charAt(i)) < 0 && i < linha.length()) { i++; }
			for (; i < linha.length(); i++) {
				if ( VALID_CHARS_FOR_HOST.indexOf(linha.charAt(i)) >= 0 ) {
					host += linha.charAt(i);
				} else {
					break;
				}
			}
			host = host.toLowerCase();
		} else {
			host = null;
		}
		return host;
	}
	
	/**
	 * <strong>Definição</strong>
	 * <br/>
	 * <br/>
	 * O objetivo deste método é extrair a porta da String
	 * informada. Caso não exista, <i><u>null</u></i> será
	 * retornado.
	 * @param linha - String a ser analisada
	 * @return Valor encontrado ou <i><u>null</u></i>.
	 */
	private String getPortBanco(String linha) {
		String port = "";
		if ( linha.indexOf("PORT") > 0) {
			int i = linha.indexOf( "PORT" );
			/*
			 * Os dois blocos while abaixo são necessários para posicionar o índice
			 * exatamente na posição em que inicia-se a informação procurada
			 */
			while (linha.charAt(i) != '=' && i < linha.length()) { i++; }
			while ( NUMERIC_CHARS.indexOf(linha.charAt(i)) < 0 && i < linha.length()) { i++; }
			for (; i < linha.length(); i++) {
				if ( NUMERIC_CHARS.indexOf(linha.charAt(i)) >= 0 ) {
					port += linha.charAt(i);
				} else {
					break;
				}
			}
		} else {
			port = null;
		}
		return port;
	}

	/**
	 * <strong>Definição</strong>
	 * <br/>
	 * <br/>
	 * O objetivo deste método é extrair o SID da String
	 * informada. Caso não exista, <i><u>null</u></i> será
	 * retornado.
	 * @param linha - String a ser analisada
	 * @return Valor encontrado ou <i><u>null</u></i>.
	 */
	private String getSIDBanco(String linha) {
		String sid = "";
		if ( linha.indexOf("SID") > 0 || linha.indexOf("SERVICE_NAME") > 0 ) {
			int i;
			if ( linha.indexOf("SID") > 0 ) {
				i = linha.indexOf( "SID" );
			} else {
				i = linha.indexOf( "SERVICE_NAME" );
			}
			/*
			 * Os dois blocos while abaixo são necessários para posicionar o índice
			 * exatamente na posição em que inicia-se a informação procurada
			 */
			while (linha.charAt(i) != '=' && i < linha.length()) { i++; }
			while ( ALFA_NUMERIC_CHARS.indexOf(linha.charAt(i)) < 0 && i < linha.length()) { i++; }
			for (; i < linha.length(); i++) {
				if ( ALFA_NUMERIC_CHARS.indexOf(linha.charAt(i)) >= 0 ) {
					sid += linha.charAt(i);
				} else {
					break;
				}
			}
			sid = sid.toLowerCase();
		} else {
			sid = null;
		}
		return sid;
	}
	
	/**
	 * <strong>Definição</strong>
	 * <br/>
	 * <br/>
	 * O objetivo deste método é verificar se a String a ser analisada
	 * possui caracteres que podem valorizar informações como <i>alias</i>,
	 * <i>host</i>, <i>porta</i> ou <i>SID</i> e sendo assim, o valor retornado deverá
	 * ser <b>true</b>. Do contrário, esta linha pode representar um comentário e
	 * dessa forma, o valor retornado é <b>false</b>.
	 * @param linha
	 * @return boolean
	 */
	private boolean isLinhaValida(String linha) {
		if ( linha.length() == 0 || VALID_CHARS_FOR_SEARCH.indexOf(linha.charAt(0)) < 0 ) {
			return false;
		} else {
			return true;
		}
	}
}
