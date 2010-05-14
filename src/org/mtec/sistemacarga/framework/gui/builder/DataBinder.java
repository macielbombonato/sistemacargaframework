package org.mtec.sistemacarga.framework.gui.builder;

import java.lang.reflect.Field;

import org.mtec.sistemacarga.framework.annotations.DBProcess;
import org.mtec.sistemacarga.framework.util.DateFormat;
import org.mtec.sistemacarga.framework.util.Log;



/**
 * Classe responsável por associar os objetos da interface gráfica aos objetos do
 * componente de carga, fazendo cast dos dados para o tipo correto contido no componente 
 * de carga.
 * @author Daniel Quirino Oliveira - danielqo@gmail.com
 * @author Maciel Escudero Bombonato - maciel.bombonato@gmail.com
 * @version 1.0 - 30/04/2007
 */
public class DataBinder {

	/**
	 * Método responsável por realizar o bind dos campos da interface gráfica de String para
	 * o tipo correto de cada objeto do componente de carga.
	 * @param instance instancia do objeto DBLoadEngine.
	 * @param propertyName String com o nome do campo para busca
	 * @param propertyValue String com o valor do objeto que deverá ser atribuido ao campo respectivo da instancia.
	 */
	@SuppressWarnings("unchecked")
	public static void bind(Object instance, String propertyName, String propertyValue) {
		if(! instance.getClass().isAnnotationPresent(DBProcess.class)) {
			return;
		}
		if(propertyName != null) {
			try {
				//
				Field[] field = instance.getClass().getDeclaredFields();
				for (int i = 0; i < field.length; i++) {
					if (field[i].getName().equalsIgnoreCase(propertyName)) {
						//
						field[i].setAccessible(true);
						//
						if (field[i].getType() == int.class) {
							if (propertyValue != null && !propertyValue.equalsIgnoreCase("")) {
								field[i].setInt(instance, Integer.parseInt(propertyValue));
							} else {
								field[i].setInt(instance, 0);
							}
							break;
						} else if (field[i].getType() == float.class) {
							if (propertyValue != null && !propertyValue.equalsIgnoreCase("")) {
								field[i].setFloat(instance, Float.parseFloat(propertyValue));
							} else {
								field[i].setFloat(instance, 0f);
							}
							break;
						} else if (field[i].getType() == double.class) {
							if (propertyValue != null && !propertyValue.equalsIgnoreCase("")) {
								field[i].setDouble(instance, Double.parseDouble(propertyValue));
							} else {
								field[i].setDouble(instance, 0d);
							}
							break;
						} else if (field[i].getType() == long.class) {
							if (propertyValue != null && !propertyValue.equalsIgnoreCase("")) {
								field[i].setLong(instance, Long.parseLong(propertyValue));
							} else {
								field[i].setLong(instance, 0L);
							}
							break;
						} else if (field[i].getType() == java.util.Date.class) {
							if (propertyValue != null && !propertyValue.equalsIgnoreCase("")) {
								field[i].set(instance, DateFormat.getInstance().parseDate(propertyValue));
							} else {
								field[i].set(instance, null);
							}
							break;
						} else {
							if (propertyValue != null && !propertyValue.equalsIgnoreCase("")) {
								field[i].set(instance, propertyValue);
							} else {
								field[i].set(instance, null);
							}
							break;
						}
					}
				}
				//
			} catch (Exception e) {
				Log.error("Erro ao tentar realizar a conversão dos dados informados na tela " +
						"para o tipo de dado do objeto do componente de carga.");
				Log.error(e);
			}
		}	
	}

}
