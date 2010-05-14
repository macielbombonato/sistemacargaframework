package org.mtec.sistemacarga.framework.reload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import org.mtec.sistemacarga.framework.dao.ExecuteDMLCommand;
import org.mtec.sistemacarga.framework.dao.ExecuteStoredProcedure;



public class ProcessToReLoad implements Serializable {
	
	private static final String APP_HOME_DIR = "./.siscarga/";

	/**
	 * Número de Série
	 */
	private static final long serialVersionUID = -1358147468392180114L;
	
	/**
	 * Lista de processos (StoredProcedures)
	 */
	private static LinkedList<ExecuteStoredProcedure> process = new LinkedList<ExecuteStoredProcedure>();
	
	/**
	 * Lista de processos DML (comandos de inserção, atualização e deleção)
	 */
	private static LinkedList<ExecuteDMLCommand> processDML = new LinkedList<ExecuteDMLCommand>();

	public static boolean add(ExecuteStoredProcedure o) {
		return process.add(o);
	}
	
	public static boolean DmlAdd(ExecuteDMLCommand o) {
		return processDML.add(o);
	}

	public static void addFirst(ExecuteStoredProcedure o) {
		process.addFirst(o);
	}
	
	public static void DmlAddFirst(ExecuteDMLCommand o) {
		processDML.addFirst(o);
	}

	public static void addLast(ExecuteStoredProcedure o) {
		process.addLast(o);
	}
	
	public static void DmlAddLast(ExecuteDMLCommand o) {
		processDML.addLast(o);
	}

	public static void clear() {
		process.clear();
		processDML.clear();
	}
	
	public static ExecuteStoredProcedure get(int index) {
		return process.get(index);
	}
	
	public static ExecuteDMLCommand DmlGet(int index) {
		return processDML.get(index);
	}

	public static ExecuteStoredProcedure getFirst() {
		return process.getFirst();
	}
	
	public static ExecuteDMLCommand DmlGetFirst() {
		return processDML.getFirst();
	}

	public static ExecuteStoredProcedure getLast() {
		return process.getLast();
	}
	
	public static ExecuteDMLCommand DmlGetLast() {
		return processDML.getLast();
	}
	
	public static LinkedList<ExecuteStoredProcedure> get() {
		return process;
	}
	
	public static LinkedList<ExecuteDMLCommand> DmlGet() {
		return processDML;
	}

	public static ExecuteStoredProcedure remove(int index) {
		return process.remove(index);
	}
	
	public static ExecuteDMLCommand DmlRemove(int index) {
		return processDML.remove(index);
	}

	public static ExecuteStoredProcedure removeFirst() {
		return process.removeFirst();
	}
	
	public static ExecuteDMLCommand DmlRemoveFirst() {
		return processDML.removeFirst();
	}

	public static ExecuteStoredProcedure removeLast() {
		return process.removeLast();
	}
	
	public static ExecuteDMLCommand DmlRemoveLast() {
		return processDML.removeLast();
	}

	public static int size() {
		return process.size();
	}
	
	public static int DmlSize() {
		return processDML.size();
	}
	
	public static void persist() {
		if (process.size() > 0) {
			File home = new File(APP_HOME_DIR);
			if(!home.exists()) home.mkdir();
			File file = new File(home + File.separator + "redo.dat");
			try {
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream(file));
				out.writeObject(process);
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}
		//
		if (processDML.size() > 0) {
			File home = new File(APP_HOME_DIR);
			File dmlHome = new File(APP_HOME_DIR);
			if(!dmlHome.exists()) home.mkdir();
			File dmlFile = new File(dmlHome + File.separator + "redoDml.dat");
			try {
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream(dmlFile));
				out.writeObject(processDML);
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void load() {
		String home = APP_HOME_DIR;
		File file = new File(home + File.separator + "redo.dat");
		if (file.exists()) {
			try {
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(file));
				process = (LinkedList<ExecuteStoredProcedure>) in.readObject();
				in.close();
				file.delete();
			} catch (IOException e) {
			} catch (ClassNotFoundException e) {
			} 
		}
		//
		String dmlHome = APP_HOME_DIR;
		File dmlFile = new File(dmlHome + File.separator + "redoDml.dat");
		if (dmlFile.exists()) {
			try {
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(dmlFile));
				processDML = (LinkedList<ExecuteDMLCommand>) in.readObject();
				in.close();
				dmlFile.delete();
			} catch (IOException e) {
			} catch (ClassNotFoundException e) {
			} 
		}
	}
	
	public static Iterator<ExecuteStoredProcedure> iterator() {
		return process.iterator();
	}
	
	public static Iterator<ExecuteDMLCommand> DmlIterator() {
		return processDML.iterator();
	}
}
