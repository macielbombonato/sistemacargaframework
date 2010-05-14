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
package org.mtec.sistemacarga.framework.exception;

/**
 * <strong><h1>Definição</h1></strong>
 * <br/>
 * <br/>
 * <pre>
 * O objetivo desta classe é padronizar as mensagens de erros
 * por um objeto que representa uma Exception
 * @author Antonio Cesar
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SisCarException extends Exception {

	/**
	 * Este atributo representa um código de erro
	 * que pode ser valorizado com erros de SQL por exemplo,
	 * ou códigos internos para representar um processo.
	 */
	private int errorCode;

	public SisCarException(String msgErro) {
		super( msgErro );
	}

	public SisCarException(String msgErro, Throwable exception) {
		super( msgErro, exception );
	}

	public SisCarException(String msgErro, int errorCode) {
		super( msgErro.concat("\n Codigo de erro: " + errorCode) );
		this.errorCode = errorCode;
	}

	public SisCarException(String msgErro, int errorCode, Throwable exception) {
		super( msgErro.concat("\n Codigo de erro: " + errorCode), exception );
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

}
