package org.mtec.sistemacarga.framework.gui.builder;

import java.lang.reflect.Field;

import org.mtec.sistemacarga.framework.annotations.Cursor;
import org.mtec.sistemacarga.framework.annotations.DBProcess;
import org.mtec.sistemacarga.framework.annotations.Property;
import org.mtec.sistemacarga.framework.util.Log;



/**
 * Processa as anotações dos processos de carga para possibilar o processo 
 * de reflexão e montagem da interface gráfica da aplicação.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 25/04/2007
 */
class AnnotationProcessor {

	/**
	 * Conforme os tipos de anotação retorna o metadata dos objetos
	 * para possibilitar montar a interface gráfica.
	 * @param clazz Classe que será verificada para geração do PanelMetaData
	 * @return PanelMetaData
	 */
	@SuppressWarnings("unchecked")
	public PanelMetaData processAnnotations(Object obj) {
		Class<?> clazz = obj.getClass();
		PanelMetaData metadata = new PanelMetaData();
		DBProcess dbProc = (DBProcess)clazz.getAnnotation(DBProcess.class);
		String name = dbProc.value();
		metadata.setName(name);
		Field[] declaredFields = clazz.getDeclaredFields();
		for(Field field : declaredFields) {
			field.setAccessible(true);
			Property propAnnotation = field.getAnnotation(Property.class);
			InputFieldMetaData input = new InputFieldMetaData();
			if(propAnnotation != null) {
				input.setFieldName(field.getName());
				input.setLabelName(propAnnotation.value());
				input.setType(UIElementType.INPUT);
				input.setTip(propAnnotation.message());
				input.setElementType(field.getType());
				try {
					input.setElementValue(field.get(obj)+"");
				} catch (IllegalArgumentException e) {
					Log.warn("Nao foi possivel recuperar o valor default do campo.");
					Log.warn(e);
				} catch (IllegalAccessException e) {
					Log.warn("Nao foi possivel recuperar o valor default do campo.");
					Log.warn(e);
				}
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
