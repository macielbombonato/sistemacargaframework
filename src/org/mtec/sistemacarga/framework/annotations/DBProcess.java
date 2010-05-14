package org.mtec.sistemacarga.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classe que define o tipo de anotação referente a processos
 * de carga, esta anotação deve ser inserida sobre o nome da classe
 * que representa o objeto de carga de cada sistema e será utilizada 
 * para objetos do tipo DBLoadEngine.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBProcess {
	
	String value() default "Sem Nome";

}
