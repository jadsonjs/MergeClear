/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.redmine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import br.ufrn.spl.ev.engines.connectors.Connector;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManager.INCLUDE;
import com.taskadapter.redmineapi.bean.Changeset;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Journal;
import com.taskadapter.redmineapi.bean.JournalDetail;

/**
 * A test class to access REDMINE WebServices.
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class RedmineConnectorTest extends Connector {

	/**
	 * FOR TESTS
	 * 
	 * <p> Created at:  05/09/2013  </p>
	 *
	 * @param args
	 * @throws RedmineException
	 * @throws IOException 
	 */
	public static void main(String[] args) throws RedmineException, IOException {
			
		String side = "TARGET";
		
		Properties propConnections = new Properties();
		propConnections.load(new FileInputStream(new File("C:/temp/connections.properties")));
		
		String redmineHost = propConnections.getProperty(side+"_SYSTEM_URL");
		String apiAccessKey = propConnections.getProperty(side+"_SYSTEM_PASSWORD");
		
		//String projectKey = "98";  // espaco fisico
		//Integer queryId = null; // any
		
		RedmineManager mgr = new RedmineManager(redmineHost, apiAccessKey);
		
		readingSpecificIssueInformation(mgr);
		
		readingIssuesInformationForAProjectKey(mgr);
	}


	private static void readingIssuesInformationForAProjectKey(RedmineManager mgr) throws RedmineException {
	    // Doesn't not work, recovery just open issues
		List<Issue> issues =  mgr.getIssues("98", null, INCLUDE.changesets, INCLUDE.journals, INCLUDE.relations);
		 
		System.out.println("\n\n######### "+issues.size()+" #########\n\n");
		for (Issue issue : issues) {
			printRedMineIssueInformations(issue);
		}
	}
	
	
	private static void readingSpecificIssueInformation(RedmineManager mgr) throws RedmineException {
		
		 Issue issue = mgr.getIssueById(9682, INCLUDE.changesets, INCLUDE.journals, INCLUDE.relations);
	     
		 printRedMineIssueInformations(issue);
	}


	private static void printRedMineIssueInformations(Issue issue) {
		System.out.println(issue.toString());
		 
		 System.out.println("*** Projeto ***: "+issue.getProject().getId()+" "+issue.getProject().getName());
		 
		 System.out.println("descricao: "+issue.getDescription());
		 System.out.println("statu: "+issue.getStatusName());
		 System.out.println("categoria: "+issue.getCategory());
		 System.out.println("Done Ration: "+issue.getDoneRatio());
		 
		 System.out.println("===== change set =====");
		 for (Changeset changeset : issue.getChangesets()) {
			 System.out.println("resision: "+changeset.getRevision());
			 System.out.println("coments: "+changeset.getComments());
			 System.out.println("coomited on: "+changeset.getCommitedOn());
		 }
		 
		 System.out.println("notes: "+issue.getNotes());
		 
		 System.out.println("--- journals ---");
		 int i = 0;
		 for (Journal journal : issue.getJournals()) {
			
			 System.out.println("\n--- journal "+i+" ---");
			 System.out.println("notas: "+journal.getNotes());
			 System.out.println("\n--- fim notas de journal "+i+" ---");
			 
			 //what was change
			 for (JournalDetail journalDetail : journal.getDetails()) {
				 System.out.println("journalDetail: "+journalDetail.getName());
			 }
			 i++;
		 }
	}


	@Override
	public void performSetupOnLine() {
		
	}


	@Override
	public void performSetupOffLine() {
		
	}


	@Override
	public void performRunOnLine() {
			
	}


	@Override
	public void performRunOffLine() {
		
	}



}