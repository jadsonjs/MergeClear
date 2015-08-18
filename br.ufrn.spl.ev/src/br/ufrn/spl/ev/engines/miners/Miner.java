/**
 * 
 */
package br.ufrn.spl.ev.engines.miners;

import java.util.Properties;

import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.engines.connectors.SystemConnector;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;


/**
 * <p>Abstract Miner to implements by the specific miner of each supported system.</p>
 * 
 * <p>A miner is a entity that reads information export by the Configuration Management System (IPROJECT, REDMINE, etc) 
 * and Version Control System (SVN, GIT, etc) and compile all these information in a internal model to give a right level 
 * information about the systems evolution over the time.</p>
 * 
 * @author jadson - jadsonjs@gmail.com
 * @vesion 1.0 - criação da classe.
 * @since 29/07/2013
 *
 */
public abstract class Miner {

	/** A connector to the system */
	protected SystemConnector systemConnector;
	
	/** A connector to the repository of the system */
	protected RepositoryConnector repositoryConnector;
	
	public Miner(){
		
	}
	
	/**
	 * <p> Create here the algorithm of miner the evolution information!.</p>
	 * <p> Created at:  29/07/2013  </p>
	 *
	 * @throws Exception
	 */
	public abstract ChangeLogHistory performMining(Properties configurationKnowledge) throws Exception;

	
	
	public void setSystemConnector(SystemConnector systemConnector) {
		this.systemConnector = systemConnector;
	}

	public void setRepositoryConnector(RepositoryConnector repositoryConnector) {
		this.repositoryConnector = repositoryConnector;
	}

	public SystemConnector getSystemConnector() {
		return systemConnector;
	}

	public RepositoryConnector getRepositoryConnector() {
		return repositoryConnector;
	}
	
}
