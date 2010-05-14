package org.mtec.sistemacarga.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classe que define o tipo de anotação referente a dados de entrada
 * dos processos de carga, esta anotação deve ser inserida em campos, 
 * como por exemplo, adPessoa, e para esta anotação não é definido um 
 * tipo específico para o objeto.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property {
	
	String value();
	
	String message() default "";
	
}
