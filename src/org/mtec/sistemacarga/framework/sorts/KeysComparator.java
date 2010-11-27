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
package org.mtec.sistemacarga.framework.sorts;

import java.util.Comparator;

/**
 * <strong><h1>Defini��o</h1></strong>
 * <br/>
 * <br/>
 * <pre>
 * Esta classe tem como objetivo, prover uma ordem para as chaves
 * a serem armazenadas em um map.
 *  
 * @author Antonio Cesar
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 14/02/2007
 *
 */
@SuppressWarnings("unchecked")
public class KeysComparator implements Comparator {

	/**
	 * <strong>Defini��o</strong>
	 * <br/>
	 * <br/>
	 * Este m�todo ser� invocado pelas Collections para ordena��o
	 * das chaves.
	 * @param objA - Chave anterior
	 * @param objB - Chave posterior
	 * @return �ndice para calculo da ordena��o.
	 */
	public int compare(Object objA, Object objB) {
		int resultado = 0;
		if (objA instanceof String && objB instanceof String) {
			resultado = objA.toString().compareTo( objB.toString() );
		}
		return resultado;
	}

}
