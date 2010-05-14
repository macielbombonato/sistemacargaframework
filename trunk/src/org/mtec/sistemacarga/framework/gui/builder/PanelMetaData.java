package org.mtec.sistemacarga.framework.gui.builder;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe que define o metadata para geração de um Panel respectivo para cada componente de carga.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 */
class PanelMetaData {
	
	/**
	 * Nome do objeto
	 */
	private String name = null;

	/**
	 * HashMap com os campos do panel
	 */
	private Set<InputFieldMetaData> inputFields = new HashSet<InputFieldMetaData>();
	
	/**
	 * Construtor da classe
	 */
	public PanelMetaData() {
		super();
	}

	/**
	 * Retorna os campos do objeto
	 * @return inputFields
	 */
	public Set<InputFieldMetaData> getInputFields() {
		return inputFields;
	}

	/**
	 * Retorna o nome do objeto
	 * @return String name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Define o nome do objeto
	 * @param name Nome do objeto
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sobre-escreve o método toString para este objeto.
	 */
	@Override
	public String toString() {
		return inputFields.toString();
	}	

}