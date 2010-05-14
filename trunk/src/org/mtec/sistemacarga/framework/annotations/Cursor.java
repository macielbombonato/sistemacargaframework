package org.mtec.sistemacarga.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classe que define o tipo de anota��o referente a cursores dos processos 
 * de carga, esta anota��o deve ser inserida em cursores, que s�o
 * objetos do tipo CachedRowSet.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cursor {
	
	String value() default "Sem Nome";
	
}
