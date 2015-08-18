/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.gui.managers;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import br.ufrn.spl.ev.PluginConstants;
import br.ufrn.spl.ev.engines.conflicts.indirect.IndirectConflictsDetection;
import br.ufrn.spl.ev.exceptions.IndirectConflictsDetectionException;
import br.ufrn.spl.ev.gui.swt.ShowEvolutionsUI;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;
import br.ufrn.spl.ev.models.conflictmodel.ConflictType;
import br.ufrn.spl.ev.parsers.ChangeLogHistoryModelParserFactory;
import br.ufrn.spl.ev.parsers.ConflictModelParserFactory;
import br.ufrn.spl.ev.util.GUIUtils;

/**
 *  <p>Executes the process of analyze indirect conflicts</p>
 *
 *  <p>Controls the flow of the tool, call the strategy to make the job, and after call a UI to show the results.</p>
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 09/10/2013
 *
 */
public class AnalyzeIndirectConflictsManager {

	/* Reference to the main UI of the tool */
	private ShowEvolutionsUI showEvolutionsUI;
	
	private ChangeLogHistory changeLogHistorySource;
	private ChangeLogHistory changeLogHistoryTarget;
	
	private ConflictModel conflictModel;
	private List<IndirectConflictsDetection> evolutionsDetections;
	private short depthAnalysisLevel;
	long startTime = 0;
	
	public AnalyzeIndirectConflictsManager(ShowEvolutionsUI showEvolutionsUI){
		this.showEvolutionsUI = showEvolutionsUI;
	}
	
	/**
	 * Make the process the execution of indirect conflicts
	 * 
	 * @throws Exception
	 */
	public ConflictModel executeIndiredConflictsAnalisis(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget, ConflictModel conflictModel, List<IndirectConflictsDetection> evolutionsDetections,  short depthAnalysisLevel) throws Exception{
		
		if(conflictModel == null) 
			conflictModel = new ConflictModel();
		else
			conflictModel.clearConflicts(ConflictType.INDIRECT);
		
		this.changeLogHistorySource = changeLogHistorySource;
		this.changeLogHistoryTarget = changeLogHistoryTarget;
		this.conflictModel = conflictModel;
		this.evolutionsDetections = evolutionsDetections;
		this.depthAnalysisLevel = depthAnalysisLevel;
		
		startTime = System.currentTimeMillis();
		
		
		if(evolutionsDetections == null || evolutionsDetections.size() == 0){
			GUIUtils.addErrorMessage("No analysis to be done!");
			return conflictModel;
		}
		
		if(changeLogHistorySource == null || changeLogHistoryTarget == null){
			GUIUtils.addErrorMessage("No Information about Evolution was passed ! ");
			return conflictModel;
		}
		
		System.out.println("/////  STARTIN INDIRECT CONFLICT ANALYSIS "+((System.currentTimeMillis()-startTime)/1000/60)+" minutes //////");
    	
		Job job = new Job("Indirect Conflict") {
  			@Override
  			protected IStatus run(IProgressMonitor monitor) {
  				
  				monitor.beginTask("INDIRECT CONFLICT ANALYSIS", 100);

  				//time consuming work here
  				try {
					doAnalisis();
					
				} catch (IndirectConflictsDetectionException icde) {
					GUIUtils.addErrorMessage("Error on indirect conflict analysis: "+icde.getMessage());
					monitor.done();
					return Status.CANCEL_STATUS;
				}
  				monitor.done();
  				return Status.OK_STATUS;
  			}

  		};
  		job.setUser(true);
  		job.schedule(); 
  		
  		return conflictModel;
		
	}
	
	
	private void doAnalisis() throws IndirectConflictsDetectionException {
		
		// For each type of indirect conflict Analysis
		// Mark the indirect conflict on the ChangeLogHistory source and reload the same interface to show it to the user
		for (IndirectConflictsDetection indirectConflictsDetection : evolutionsDetections) {
			indirectConflictsDetection.checkIndirectConflicts( changeLogHistorySource,  changeLogHistoryTarget, conflictModel, depthAnalysisLevel);
		}
		
		saveAndShowEvolutionResults( changeLogHistorySource,  changeLogHistoryTarget, conflictModel);
	}
	
	
	/**
	 * <p> Execute the information for the user and show it for the user in the GUI </p>
	 * 
	 * <p> Created at:  14/10/2013  </p>
	 *
	 * @param startTime
	 * @param changeLogHistorySource
	 * @param changeLogHistoryTarget
	 * @return
	 */
	protected void saveAndShowEvolutionResults(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget, ConflictModel conflictModel) {
		
		System.out.println("/////  INDIRECT CONFLICT ANALYSIS IN "+((System.currentTimeMillis()-startTime)/1000/60)+" minutes //////");
		
		ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().writeHistoryChangeLogFile(changeLogHistorySource, new File(showEvolutionsUI.getWorkDirectoryPath()+PluginConstants.HISTORY_CHANGE_LOG_SOURCE_FILE));
		ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().writeHistoryChangeLogFile(changeLogHistoryTarget, new File(showEvolutionsUI.getWorkDirectoryPath()+PluginConstants.HISTORY_CHANGE_LOG_TARGET_FILE));
		ConflictModelParserFactory.getConflictModelParser().writeConflictModelFile( conflictModel, new File(showEvolutionsUI.getWorkDirectoryPath()+PluginConstants.CONFLICT_MODEL_FILE));
		
		
		showEvolutionsUI.showEvolutionsData(changeLogHistorySource, changeLogHistoryTarget, conflictModel, null);
		
	}
	
	
}
