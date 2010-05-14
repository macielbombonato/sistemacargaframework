package org.mtec.sistemacarga.framework.gui.builder;


/**
 * Classe que define os tipos de dados a serem considerados para montagem de um JTextField
 * na interface gráfica associada a um objeto de entrada do componente de carga.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 */
public class InputFieldMetaData {

	/**
	 * Referente ao Label que será apresentado na interface gráfica.
	 */
	private String labelName = null;

	/**
	 * Nome do objeto
	 */
	private String fieldName = null;
	
	/**
	 * Tipo do objeto segundo as anotações
	 * @see UIElementType
	 */
	private UIElementType type = null;
	
	/**
	 * Tool tipo do JTextField
	 */
	private String tip = null;
	
	/**
	 * Tipo do objeto definido no componente de carga
	 */
	private Class elementType;
	
	/**
	 * Recupera o tipo do objeto definido no componente de carga
	 * @return Class - retornos prováveis, long, int, double, float, 
	 * String, java.util.Date, dentre outros
	 */
	public Class getElementType() {
		return elementType;
	}

	/**
	 * Define o tipo do objeto informado no componente de carga
	 * @param elementType - Class podendo ser long, int, double, float, 
	 * String, java.util.Date, dentre outros
	 */
	public void setElementType(Class elementType) {
		this.elementType = elementType;
	}

	/**
	 * Retorna tooltip do JTextField
	 * @return String tip
	 */
	public String getTip() {
		return tip;
	}

	/**
	 * Define o tooltip do JTextField
	 * @param tip String de instrução ao usuário
	 */
	public void setTip(String tip) {
		this.tip = tip;
	}

	/**
	 * Construtor da classe
	 */
	public InputFieldMetaData() {
		super();
	}
	
	/**
	 * Retorna o tipo do objeto
	 * @see UIElementType
	 * @return UIElementType
	 */
	public UIElementType getType() {
		return type;
	}

	/**
	 * Define o tipo do objeto
	 * @see UIElementType
	 * @param type Define o tipo do objeto enum UIElementType
	 */
	public void setType(UIElementType type) {
		this.type = type;
	}

	/**
	 * Retorna o nome do objeto
	 * @return String fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Define o nome do objeto
	 * @param fieldName Define o nome do campo
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Retorna o Label do objeto
	 * @return String labelName
	 */
	public String getLabelName() {
		return labelName;
	}

	/**
	 * Define o Label do objeto
	 * @param labelName Define o Label que será apresentado na minterface gráfica da aplicação para este campo. 
	 */
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	/**
	 * toString do objeto
	 */
	@Override
	public String toString() {		
		String fieldName = "Field Name: "+this.fieldName;
		String labelName = "Label Name: "+this.labelName;
		String type      = "UIElementType: "+this.type;
		String tip       = "ToolTipText: " + this.tip;
		return "\n"+ fieldName +"\n"+ labelName+"\n"+ type+"\n"+ tip+"\n";
	}
	
}
