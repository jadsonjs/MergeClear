/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.redmine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.engines.connectors.svn.SVNConnector;
import br.ufrn.spl.ev.engines.miners.Miner;
import br.ufrn.spl.ev.engines.miners.repository.RepositoryAnalisisExecutor;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogType;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeTypeRepository;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.util.ConfigurationKnowledgeUtil;
import br.ufrn.spl.ev.util.StringUtils;

import com.taskadapter.redmineapi.bean.Changeset;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Journal;

/**
 * A concrete miner from RedMine system.
 *
 * @see {@link http://www.redmine.org/}
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - criação da classe.
 * @since 29/07/2013
 *
 */
public class RedmineMiner extends Miner{

	/**
	 * Perform the mining
	 * 
	 * @see br.ufrn.spl.ev.engines.miners.Miner#performMining(java.util.Properties)
	 */
	@Override
	public ChangeLogHistory performMining(Properties configurationKnowledge) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		System.out.println(" Reading information of Redmine  1 of 3 ... ");
		RedmineConnector redmineConnector = ( (RedmineConnector) systemConnector);
		redmineConnector.performsRun();
		List<Issue> issues = redmineConnector.getIssuesOfEvolution();
		
		System.out.println(" Reading information of Redmine in "+((System.currentTimeMillis()-startTime)/1000/60)+" minutes ");
		
		System.out.println(" Converting RedMine Isseus To ChangeLogHistory Model  2 of 3 ...  ");
		ChangeLogHistory changeLogHistory = convertRedMineIssuesToChangeLogHistoryModel(issues, configurationKnowledge);
		System.out.println(" Converting RedMine Isseus To ChangeLogHistory Model in "+((System.currentTimeMillis()-startTime)/1000/60)+" minutes ");
		
		System.out.println(" Retrieving Retrieve Changed From Respository  3 of 3 ... ");
		
		// help aqui
		//changeLogHistory = new SVNAnalisisExecutor((SVNConnector) repositoryConnector).retrieveChangedFromRespository(changeLogHistory);
		changeLogHistory = new RepositoryAnalisisExecutor(repositoryConnector).retrieveChangedFromRespository(changeLogHistory);
		System.out.println(" Retrieving Changed From Respository in "+((System.currentTimeMillis()-startTime)/1000/60)+" minutes ");
		
		return changeLogHistory;
	}


	/**
	 * <p>Convert the RedMine information in our Changes Model</p>
	 * 
	 * <p> Created at:  12/09/2013  </p>
	 *
	 * @param issues
	 * @param configurationKnowledge
	 * @return
	 * @throws SVNException
	 */
	private ChangeLogHistory convertRedMineIssuesToChangeLogHistoryModel(List<Issue> issues, Properties configurationKnowledge) throws SVNException {
		
		ChangeLogHistory changeLogHistory = new ChangeLogHistory();
		changeLogHistory.setStartVersion(  ( (RedmineConnector) systemConnector).getTasksEvolution().getProperty("startVersion") );
		changeLogHistory.setBaseVersion(  ( (RedmineConnector) systemConnector).getTasksEvolution().getProperty("baseVersion"));
	
		nextIssue:
		for (Issue issue : issues) {
            
			ChangeLog changeLog = new ChangeLog();
			
			changeLog.setIdentify(String.valueOf(issue.getId()) );
			changeLog.setDescription( issue.getDescription() );
			changeLog.setModule( issue.getProject().getName() );
			
			////  The  type of ChangeLog  (This part is entirely UFS dependent )////
			if(issue.getTracker().getName().equals("Manutenção") || issue.getTracker().getName().equals("Atendimento ao Usuário"))
				changeLog.setType( ChangeLogType.BUG_FIX);
			else
				if(issue.getTracker().getName().equals("Funcionalidade"))
					changeLog.setType( ChangeLogType.NEW_FUNCIONALITY);
				else
					if(issue.getTracker().getName().equals("Reunião"))
						changeLog.setType( ChangeLogType.NEW_USE_CASE);
					else
						changeLog.setType(ChangeLogType.REFACTORY);
			
			
			///  the version where the change was made ///
			changeLog.setVersion(issue.getStartDate().toString());
			
			
			// the high level information of the change //
			for (Journal journal : issue.getJournals()) {
					
				if(journal.getNotes() != null ){
					
					/*
					 * some revision are in the task, not in the ChangeSets objects. this is bad!
					 */
					if(journal.getNotes().contains("Revisão:") ){
						changeLog.addClassesChangeLog( retrieveClassesFromRevisionNumber( StringUtils.extractRevisionNumber( journal.getNotes(), "Revisão:" )  ) );
					}else{
					
						if(journal.getNotes().contains("At revision:") ){
							changeLog.addClassesChangeLog( retrieveClassesFromRevisionNumber( StringUtils.extractRevisionNumber( journal.getNotes(), "At revision:" )  ) );
						}else{
							
							if(journal.getNotes().contains("Revisões:") ){
								List<String> revisions = StringUtils.extractRevisionsNumbers( journal.getNotes(), "Revisões:" );
								for (String revision : revisions) {
									changeLog.addClassesChangeLog( retrieveClassesFromRevisionNumber( revision ) );
								}
							}else{
						
								if( StringUtils.matchRevisionsNumbersTinyForm(journal.getNotes() ) ){
									List<String> revisions = StringUtils.extrectRevisionsNumbersTinyForm( journal.getNotes() );
									for (String revision : revisions) {
										changeLog.addClassesChangeLog( retrieveClassesFromRevisionNumber( revision ) );
									}
								}else{
									
								
									if(changeLog.getChangeInformation() == null){
										changeLog.setChangeInformation( journal.getNotes() );
									}else{
										// Try to eliminate double change logs
										if(! changeLog.getChangeInformation().replaceAll("\\s", "").replaceAll("<br>", "").contains( journal.getNotes().replaceAll("\\s", "").replaceAll("<br>", "") ) ) // eliminates duplicate CHANGELOG
											changeLog.setChangeInformation( changeLog.getChangeInformation( )+" \n "+journal.getNotes());
									}
									
								}
							}
						}
					}
				}
			}
			
			
			// Asset evolutions information. Some revisions come here
			for (Changeset changeset : issue.getChangesets()){
				changeLog.addClassesChangeLog(retrieveClassesFromRevisionNumber(changeset.getRevision()));
			}
			
			
			if (changeLog.getClasses().size() == 0) // some issues of RedMine doesn't have revision number
				continue nextIssue;
			
			// associate with the respective feature, if is not associated, put into the CORE feature
			
			List<FeatureChangeLog> featuresMapping = ConfigurationKnowledgeUtil.getFeaturesOfATask(configurationKnowledge, String.valueOf(issue.getId()));
			
			
			if(featuresMapping.size() == 0 ){ // no feature associate with this task
				
				FeatureChangeLog featureCore =  changeLogHistory.getFeature(ChangeLogHistory.CORE.getName());
				if(featureCore == null){ // if the feature CORE does not exist yet
					featureCore = ChangeLogHistory.createNewCoreFeature();
					featureCore.addChangeLog(changeLog);
					changeLogHistory.addFeature(featureCore);
				}else{
					featureCore.addChangeLog(changeLog);
				}
			}else{
				
				for (FeatureChangeLog featureMapping : featuresMapping) {
					FeatureChangeLog featureTemp =  changeLogHistory.getFeature(featureMapping.getName());
					if(featureTemp == null){  // if the feature  does not exist yet
						featureTemp = new FeatureChangeLog(featureMapping.getName(), featureMapping.getParent(),featureMapping.getType(), featureMapping.getDescription());
						featureTemp.addChangeLog(changeLog);
						changeLogHistory.addFeature(featureTemp);
					}else{
						featureTemp.addChangeLog(changeLog);
					}
				}
			}
        	
        }
		
		return changeLogHistory;
	}


	/**
	 * Retrieve classes from the revision numbers 
	 * 
	 * <p> Created at:  10/09/2013  </p>
	 *
	 * @param changeLog
	 * @param revision
	 * @throws SVNException 
	 */
	private List<ClassChangeLog> retrieveClassesFromRevisionNumber(String revision) throws SVNException {
		
		List<ClassChangeLog> classes = new ArrayList<ClassChangeLog>();
		
		Collection<SVNLogEntry> logEntries = new LinkedList<SVNLogEntry>();
		
	
		
		// read files of a specific revision
		
		((SVNConnector) repositoryConnector).getSVNRepository().log(new String[] {""}, logEntries, Long.valueOf(revision), Long.valueOf(revision), true, true);
	
		for (SVNLogEntry svnLogEntry : logEntries) {
			
			for (String key : svnLogEntry.getChangedPaths().keySet() ){
				
				SVNLogEntryPath SVNpath = svnLogEntry.getChangedPaths().get(key);
				
				String path = SVNpath.getPath();
				
				if(path.endsWith(".java")){ // if is a java class
					
					String changeTYpe = SVNpath.getType() == 'A' ? ChangeTypeRepository.ADDED : ( SVNpath.getType() == 'M' || SVNpath.getType() == 'U' ? ChangeTypeRepository.UPDATED : ( SVNpath.getType() == 'D' ? ChangeTypeRepository.DELETE : "?" ) );
				
					String signature =  StringUtils.createClasseSignature(path, ((RedmineConnector) systemConnector).getSystemName());
					
					classes.add( new ClassChangeLog(StringUtils.extractLastNameAfterSlash(path), path, signature, changeTYpe, String.valueOf(revision) ));
				}
			}
			
		}
		
		
		return classes;
	}


}
