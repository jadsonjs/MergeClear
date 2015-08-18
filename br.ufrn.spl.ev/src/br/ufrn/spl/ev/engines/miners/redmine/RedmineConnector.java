/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.redmine;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import br.ufrn.spl.ev.engines.connectors.Connector;
import br.ufrn.spl.ev.engines.connectors.SystemConnector;
import br.ufrn.spl.ev.util.StringUtils;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManager.INCLUDE;
import com.taskadapter.redmineapi.bean.Issue;

/**
 * Connector to the read information of Redmine CMS 
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class criation.
 * @since 31/07/2013
 *
 */
public class RedmineConnector extends Connector implements SystemConnector{
	
	
	/** Contains the RedMine tasks that had evolved.
	 * For now, this information is give to us, we don't have how get this for RedMine
	 * Inject by ConnectorFactory 
	 */
	private Properties tasksEvolution;
	
	
	/** The object to manipulate the data from the REDMINE, REDMINE have a JSON/REST web service too, like IPROJECT, but it provides a higher level API for it */
	private RedmineManager  mgr;
    
	/** The evolution information */
	private List<Issue> issuesOfEvolution;
	
	
	@Override
	public void performSetupOnLine() {
		mgr = new RedmineManager (url, password);
	}

	@Override
	public void performSetupOffLine() {
		// not necessary
	}

	@Override
	public void performRunOnLine() {
		// This is not a optimized way to do, but is the only possible // 
		issuesOfEvolution = new ArrayList<Issue>();
		
		try {
			List<Integer> idsIssuesEvolution = new ArrayList<Integer>(); 
			 
			String tasksEvolutionIdsStr = tasksEvolution.getProperty("tasks");
			 
			 if( StringUtils.isNotEmpty(tasksEvolutionIdsStr)){
				 
				 String[] tasksIds = tasksEvolutionIdsStr.split(";");
			 
				 for (String taskIds : tasksIds) {
					 idsIssuesEvolution.add( Integer.valueOf(taskIds));
				 }
			 }
			 
			for (Integer issueId : idsIssuesEvolution) {
					issuesOfEvolution.add( mgr.getIssueById(issueId, INCLUDE.changesets, INCLUDE.journals, INCLUDE.relations) );
			}
		
		} catch (RedmineException rme) {
			rme.printStackTrace();
			issuesOfEvolution = new ArrayList<Issue>();
		}
	}
	
	@Override
	public void performRunOffLine() {
		// not necessary
	}
	
	
	/** Returns the information about all RedMiner evolutions issues 
	 * @throws RedmineException 
	 */
	public List<Issue> getIssuesOfEvolution() throws RedmineException {
		return issuesOfEvolution;
	}


	// gets e gets //
	
	

	public RedmineManager getMgr() {
		return mgr;
	}
	
	public String getStartVersion() {
		return startVersion;
	}

	public void setStartVersion(String startVersion) {
		this.startVersion = startVersion;
	}

	public String getEndVersion() {
		return endVersion;
	}

	public void setEndVersion(String endVersion) {
		this.endVersion = endVersion;
	}


	public Properties getTasksEvolution() {
		return tasksEvolution;
	}


	public void setTasksEvolution(Properties tasksEvolution) {
		this.tasksEvolution = tasksEvolution;
	}

	
}
