package org.mtec.sistemacarga.framework.gui.builder;

import java.lang.reflect.Field;

import org.mtec.sistemacarga.framework.annotations.Cursor;
import org.mtec.sistemacarga.framework.annotations.DBProcess;
import org.mtec.sistemacarga.framework.annotations.Property;



/**
 * Processa as anota��es dos processos de carga para possibilar o processo 
 * de reflex�o e montagem da interface gr�fica da aplica��o.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 */
class AnnotationProcessor {

	/**
	 * Conforme os tipos de anota��o retorna o metadata dos objetos
	 * para possibilitar montar a interface gr�fica.
	 * @param clazz Classe que ser� verificada para gera��o do PanelMetaData
	 * @return PanelMetaData
	 */
	@SuppressWarnings("unchecked")
	public PanelMetaData processAnnotations(Class clazz) {
		PanelMetaData metadata = new PanelMetaData();
		DBProcess dbProc = (DBProcess)clazz.getAnnotation(DBProcess.class);
		String name = dbProc.value();
		metadata.setName(name);
		Field[] declaredFields = clazz.getDeclaredFields();
		for(Field field : declaredFields) {
			Property propAnnotation = field.getAnnotation(Property.class);
			InputFieldMetaData input = new InputFieldMetaData();
			if(propAnnotation != null) {
				input.setFieldName(field.getName());
				input.setLabelName(propAnnotation.value());
				input.setType(UIElementType.INPUT);
				input.setTip(propAnnotation.message());
				input.setElementType(field.getType());
			} else {
				Cursor cursorAnnotation = field.getAnnotation(Cursor.class);
				if(cursorAnnotation != null) {
					input.setLabelName(cursorAnnotation.value());
					input.setFieldName(field.getName());
					input.setType(UIElementType.PROGRESS_BAR);
				} else {
					continue;
				}
			}
			metadata.getInputFields().add(input);
		}
		return metadata;
	}
	
}
