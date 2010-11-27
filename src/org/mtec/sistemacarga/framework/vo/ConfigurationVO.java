/**
 * 
 */
package org.mtec.sistemacarga.framework.vo;

import java.io.Serializable;

/**
 * Objeto usado para serializar dados de configuração.
 * @author Maciel Escudero Bombonato
 * @since 22/11/2010
 */
public class ConfigurationVO implements Serializable {
	
	private static final long serialVersionUID = -6885096974732745890L;
	
	private StringBuffer fullPackageName;
	private StringBuffer jarFilesDir;
	private StringBuffer logXmlAppenderPath;
	private StringBuffer logFileAppenderPath;
	private StringBuffer logRefreshTime;
	private StringBuffer dataBaseConfigPoolSize;
	private StringBuffer dataBaseConfigMaxPoolSize;
	private StringBuffer dataBaseCommitPoint;
	private StringBuffer dataBaseTnsNamesDataBasePath;
	
	private StringBuffer dataBaseDriverOrigem;
	private StringBuffer dataBaseUrlOrigem;
	private StringBuffer dataBaseUserOrigem;
	private StringBuffer dataBasePasswordOrigem;
	
	private StringBuffer dataBaseDriverDestino;
	private StringBuffer dataBaseUrlDestino;
	private StringBuffer dataBaseUserDestino;
	private StringBuffer dataBasePasswordDestino;
	/**
	 * @return the fullPackageName
	 */
	public StringBuffer getFullPackageName() {
		return fullPackageName;
	}
	/**
	 * @param fullPackageName the fullPackageName to set
	 */
	public void setFullPackageName(StringBuffer fullPackageName) {
		this.fullPackageName = fullPackageName;
	}
	/**
	 * @return the jarFilesDir
	 */
	public StringBuffer getJarFilesDir() {
		return jarFilesDir;
	}
	/**
	 * @param jarFilesDir the jarFilesDir to set
	 */
	public void setJarFilesDir(StringBuffer jarFilesDir) {
		this.jarFilesDir = jarFilesDir;
	}
	/**
	 * @return the logXmlAppenderPath
	 */
	public StringBuffer getLogXmlAppenderPath() {
		return logXmlAppenderPath;
	}
	/**
	 * @param logXmlAppenderPath the logXmlAppenderPath to set
	 */
	public void setLogXmlAppenderPath(StringBuffer logXmlAppenderPath) {
		this.logXmlAppenderPath = logXmlAppenderPath;
	}
	/**
	 * @return the logFileAppenderPath
	 */
	public StringBuffer getLogFileAppenderPath() {
		return logFileAppenderPath;
	}
	/**
	 * @param logFileAppenderPath the logFileAppenderPath to set
	 */
	public void setLogFileAppenderPath(StringBuffer logFileAppenderPath) {
		this.logFileAppenderPath = logFileAppenderPath;
	}
	/**
	 * @return the logRefreshTime
	 */
	public StringBuffer getLogRefreshTime() {
		return logRefreshTime;
	}
	/**
	 * @param logRefreshTime the logRefreshTime to set
	 */
	public void setLogRefreshTime(StringBuffer logRefreshTime) {
		this.logRefreshTime = logRefreshTime;
	}
	/**
	 * @return the dataBaseConfigPoolSize
	 */
	public StringBuffer getDataBaseConfigPoolSize() {
		return dataBaseConfigPoolSize;
	}
	/**
	 * @param dataBaseConfigPoolSize the dataBaseConfigPoolSize to set
	 */
	public void setDataBaseConfigPoolSize(StringBuffer dataBaseConfigPoolSize) {
		this.dataBaseConfigPoolSize = dataBaseConfigPoolSize;
	}
	/**
	 * @return the dataBaseConfigMaxPoolSize
	 */
	public StringBuffer getDataBaseConfigMaxPoolSize() {
		return dataBaseConfigMaxPoolSize;
	}
	/**
	 * @param dataBaseConfigMaxPoolSize the dataBaseConfigMaxPoolSize to set
	 */
	public void setDataBaseConfigMaxPoolSize(StringBuffer dataBaseConfigMaxPoolSize) {
		this.dataBaseConfigMaxPoolSize = dataBaseConfigMaxPoolSize;
	}
	/**
	 * @return the dataBaseCommitPoint
	 */
	public StringBuffer getDataBaseCommitPoint() {
		return dataBaseCommitPoint;
	}
	/**
	 * @param dataBaseCommitPoint the dataBaseCommitPoint to set
	 */
	public void setDataBaseCommitPoint(StringBuffer dataBaseCommitPoint) {
		this.dataBaseCommitPoint = dataBaseCommitPoint;
	}
	/**
	 * @return the dataBaseTnsNamesDataBasePath
	 */
	public StringBuffer getDataBaseTnsNamesDataBasePath() {
		return dataBaseTnsNamesDataBasePath;
	}
	/**
	 * @param dataBaseTnsNamesDataBasePath the dataBaseTnsNamesDataBasePath to set
	 */
	public void setDataBaseTnsNamesDataBasePath(
			StringBuffer dataBaseTnsNamesDataBasePath) {
		this.dataBaseTnsNamesDataBasePath = dataBaseTnsNamesDataBasePath;
	}
	/**
	 * @return the dataBaseDriverOrigem
	 */
	public StringBuffer getDataBaseDriverOrigem() {
		return dataBaseDriverOrigem;
	}
	/**
	 * @param dataBaseDriverOrigem the dataBaseDriverOrigem to set
	 */
	public void setDataBaseDriverOrigem(StringBuffer dataBaseDriverOrigem) {
		this.dataBaseDriverOrigem = dataBaseDriverOrigem;
	}
	/**
	 * @return the dataBaseUrlOrigem
	 */
	public StringBuffer getDataBaseUrlOrigem() {
		return dataBaseUrlOrigem;
	}
	/**
	 * @param dataBaseUrlOrigem the dataBaseUrlOrigem to set
	 */
	public void setDataBaseUrlOrigem(StringBuffer dataBaseUrlOrigem) {
		this.dataBaseUrlOrigem = dataBaseUrlOrigem;
	}
	/**
	 * @return the dataBaseUserOrigem
	 */
	public StringBuffer getDataBaseUserOrigem() {
		return dataBaseUserOrigem;
	}
	/**
	 * @param dataBaseUserOrigem the dataBaseUserOrigem to set
	 */
	public void setDataBaseUserOrigem(StringBuffer dataBaseUserOrigem) {
		this.dataBaseUserOrigem = dataBaseUserOrigem;
	}
	/**
	 * @return the dataBasePasswordOrigem
	 */
	public StringBuffer getDataBasePasswordOrigem() {
		return dataBasePasswordOrigem;
	}
	/**
	 * @param dataBasePasswordOrigem the dataBasePasswordOrigem to set
	 */
	public void setDataBasePasswordOrigem(StringBuffer dataBasePasswordOrigem) {
		this.dataBasePasswordOrigem = dataBasePasswordOrigem;
	}
	/**
	 * @return the dataBaseDriverDestino
	 */
	public StringBuffer getDataBaseDriverDestino() {
		return dataBaseDriverDestino;
	}
	/**
	 * @param dataBaseDriverDestino the dataBaseDriverDestino to set
	 */
	public void setDataBaseDriverDestino(StringBuffer dataBaseDriverDestino) {
		this.dataBaseDriverDestino = dataBaseDriverDestino;
	}
	/**
	 * @return the dataBaseUrlDestino
	 */
	public StringBuffer getDataBaseUrlDestino() {
		return dataBaseUrlDestino;
	}
	/**
	 * @param dataBaseUrlDestino the dataBaseUrlDestino to set
	 */
	public void setDataBaseUrlDestino(StringBuffer dataBaseUrlDestino) {
		this.dataBaseUrlDestino = dataBaseUrlDestino;
	}
	/**
	 * @return the dataBaseUserDestino
	 */
	public StringBuffer getDataBaseUserDestino() {
		return dataBaseUserDestino;
	}
	/**
	 * @param dataBaseUserDestino the dataBaseUserDestino to set
	 */
	public void setDataBaseUserDestino(StringBuffer dataBaseUserDestino) {
		this.dataBaseUserDestino = dataBaseUserDestino;
	}
	/**
	 * @return the dataBasePasswordDestino
	 */
	public StringBuffer getDataBasePasswordDestino() {
		return dataBasePasswordDestino;
	}
	/**
	 * @param dataBasePasswordDestino the dataBasePasswordDestino to set
	 */
	public void setDataBasePasswordDestino(StringBuffer dataBasePasswordDestino) {
		this.dataBasePasswordDestino = dataBasePasswordDestino;
	}

}
