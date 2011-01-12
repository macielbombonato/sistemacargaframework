package org.mtec.sistemacarga.framework.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.mtec.sistemacarga.framework.AbstractProcess;
import org.mtec.sistemacarga.framework.exception.SisCarException;
import org.mtec.sistemacarga.framework.util.Log;

public class HibernateConcurrentExecution  extends AbstractProcess {

	private Object obj;
	
	/**
	 * Session que aponta para o banco de destino de dados
	 */
	private Session destinySession;
	
	
	public HibernateConcurrentExecution(List parameters) {
		super(parameters);
	}
	
	public HibernateConcurrentExecution(List parameters, Object obj, Session destinySession) {
		super(parameters);
		this.obj = obj;
		this.destinySession = destinySession;
	}
	
	@Override
	public void exec() throws SisCarException {
		try {
			//
			// Salva ou atualiza dados no banco de destino
			synchronized (destinySession) {
				destinySession.beginTransaction();
				destinySession.saveOrUpdate(obj);
				destinySession.getTransaction().commit();				
			}

			//
		} catch (HibernateException e) {
			Log.error("Erro ao tentar salvar o registro.");
			Log.error(e);
			throw new SisCarException("Erro ao tentar salvar o registro.", e);
		}
	}
	
	public void setObj(Object obj) {
		this.obj = obj;
	}

	/**
	 * @param destinySession the destinySession to set
	 */
	public void setDestinySession(Session destinySession) {
		this.destinySession = destinySession;
	}

}
