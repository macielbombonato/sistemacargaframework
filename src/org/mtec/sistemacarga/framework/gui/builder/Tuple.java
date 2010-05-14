package org.mtec.sistemacarga.framework.gui.builder;

/**
 * Classe utilizada para criar e retonar uma tupla de panels.
 * Este objeto é utilizado pelo framework para criação e retorno dos panels 
 * da interface gráfica.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 *
 * @param <T>
 * @param <U>
 * 
 * @version 1.0 - 25/04/2007
 */
class Tuple<T extends Object, U extends Object> {

	/**
	 * Utilizado para criação do panel de campos de entrada de dados.
	 */
	private T firstObject;

	/**
	 * Utilizado para criação do panel de monitoramento do componente de carga.
	 */
	private U secondObject;

	/**
	 * Construtor da classe
	 * @param obj1 panel de inputFields.
	 * @param obj2 panel de monitoramento.
	 */
	public Tuple(T obj1, U obj2) {
		super();
		this.firstObject = obj1;
		this.secondObject = obj2;
	}

	/**
	 * Retorna o panel de inputFields
	 * @return T
	 */
	public T getFirstObject() {
		return firstObject;
	}

	/**
	 * Define o panel de inputFields
	 * @param obj1 T
	 */
	public void setFirstObject(T obj1) {
		this.firstObject = obj1;
	}

	/**
	 * Retorna o panel de monitoramento.
	 * @return U
	 */
	public U getSecondObject() {
		return secondObject;
	}

	/**
	 * Define o panel de monitoramento.
	 * @param obj2 U
	 */
	public void setSecondObject(U obj2) {
		this.secondObject = obj2;
	}

}