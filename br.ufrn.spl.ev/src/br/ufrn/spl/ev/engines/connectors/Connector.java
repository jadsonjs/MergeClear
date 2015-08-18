/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.connectors;

import br.ufrn.spl.ev.engines.miners.MinerFactory.MergeSide;


/**
 * <p>A Connector contains information about to connect to a service or repository. </p>
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - criação da classe.
 * @since 29/07/2013
 *
 */
public abstract class Connector {

	/**  the system that we area analysis*/
	protected String systemName;
		
	/**  the modulo of the system that we area analysis (optional) */
	protected String moduleName;
	
	/** The initial version of evolution */
	protected String startVersion;
	
	/** The final version of evolution */
	protected String endVersion;
	
	protected String url;
	protected String user;	
	protected String password;
	
	/** A file that have information about the evolution, to analyzed it without connect to the system. */
	protected String offLineEvolutionFile;
	
	protected ConnectorType type;
	
	/** Identify if is a connector of the source or target analysis */
	protected MergeSide side;
	
	/** IF the connector will execute on online or offline method.
	 * On online method a real connection is created, need network access, system access, database access, etc.
	 * On offline method a connection if not created when is possible.
	 */
	protected boolean onLineModel = false;
	
	/** The directory where the tool is reading or saving its information (changeLogs, etc.. ) */
	//protected String workDirectoryPath;
	
	public Connector(){
		
	}
	
	/** Configure the connection  information in the connector, have to be called after all the data be full filled */
	public void performSetup(MergeSide side, boolean onLineModel){
		this.side = side;
		this.onLineModel = onLineModel;
		
		if( onLineModel ){
			performSetupOnLine();
		}else{
			performSetupOffLine();
		}	
	}
	
	/** Execute what the connector have to execute. have to the called after performSetup method. */
	public void performsRun() {
		if( onLineModel ){
			performRunOnLine();
		}else{
			performRunOffLine();
		}
	}
	
	
	public abstract void performSetupOnLine();
	
	public abstract void performSetupOffLine();
	
	
	public abstract void performRunOnLine();
	
	public abstract void performRunOffLine();
	
	public String getUrl() {return url;}
	
	public String getUser() {return user;}
	
	public String getPassword() {return password;}
	
	public void setUrl(String url) { this.url = url;}

	public void setUser(String user) {this.user = user;}

	public void setPassword(String password) {this.password = password;}

	public void setType(ConnectorType type) {this.type = type;}
	
	public boolean isSystem() { return type == ConnectorType.SYSTEM; }
	
	public boolean isRepository() { return type == ConnectorType.REPOSITORY; }

	public String getSystemName() {return systemName;}

	public void setSystemName(String systemName) {this.systemName = systemName;}

	public String getStartVersion() {return startVersion;}

	public void setStartVersion(String startVersion) {this.startVersion = startVersion;}

	public String getEndVersion() {return endVersion;}

	public void setEndVersion(String endVersion) {this.endVersion = endVersion;}

	public String getOffLineEvolutionFile() {return offLineEvolutionFile;}

	public void setOffLineEvolutionFile(String offLineEvolutionFile) {this.offLineEvolutionFile = offLineEvolutionFile;}

	public String getModuleName() {	return moduleName;}

	public void setModuleName(String moduloName) {	this.moduleName = moduloName;}

//	public String getWorkDirectoryPath() {
//		return workDirectoryPath;
//	}
//
//	public void setWorkDirectoryPath(String workDirectoryPath) {
//		this.workDirectoryPath = workDirectoryPath;
//	}	

}
