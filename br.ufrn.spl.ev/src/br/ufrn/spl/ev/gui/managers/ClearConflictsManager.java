/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.gui.managers;

import java.io.File;

import br.ufrn.spl.ev.PluginConstants;
import br.ufrn.spl.ev.gui.swt.ShowEvolutionsUI;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;
import br.ufrn.spl.ev.models.conflictmodel.ConflictType;
import br.ufrn.spl.ev.parsers.ChangeLogHistoryModelParserFactory;
import br.ufrn.spl.ev.parsers.ConflictModelParserFactory;
import br.ufrn.spl.ev.util.GUIUtils;

/**
 * <p>Executes the process of clear all conflicts</p>
 *
 * <p>This eraser the conflicts on the conflict model and all conflicts safe on the the ChangeLogHistory of the source.</p>
 *
 * @author Gleydson Lima - gleydson@esig.com.br
 *
 */
public class ClearConflictsManager {

	/* Reference to the main UI of the tool */
	private ShowEvolutionsUI evolutionUI;
	
	public ClearConflictsManager(ShowEvolutionsUI evolutionUI){
		this.evolutionUI = evolutionUI;
	}
	
	
	/**
	 * Make the process the execution of indirect conflicts
	 * 
	 * @throws Exception
	 */
	public void clearConflicts(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget, ConflictModel model) throws Exception{

	
		clearHistoryChangeLog(changeLogHistorySource);
		clearHistoryChangeLog(changeLogHistoryTarget);
		
		if(model == null){
			model = new ConflictModel();
		}else{
			model.clearConflicts(ConflictType.DIRECT);
			model.clearConflicts(ConflictType.INDIRECT);
			model.clearConflicts(ConflictType.PSEUDO);
		}
		
		saveAndShowEvolutionResults(changeLogHistorySource, changeLogHistoryTarget, null);
		
		System.out.println("///// CLEAN DONE //////");
		
	}
	
	/**
	 * Clean Changelog information
	 * @param changeLogHistory
	 */
	private void clearHistoryChangeLog(ChangeLogHistory changeLogHistory) {
		
		for ( FeatureChangeLog f : changeLogHistory.getFeatures() ) {
			for ( ChangeLog c : f.getChangelogs() ) {
				c.setHierarchicalDirectConflict(false);
				c.setHierarchicalIndirectConflict(false, 0);
				c.setTextualConflicting(false);
				
				for ( ClassChangeLog ccl : c.getClasses() ) {
					ccl.setHierarchicalDirectConflict(false);
					ccl.setTextualConflicting(false);
					ccl.setHierarchicalIndirectConflict(false, 0);
					ccl.getIndirectConflicts().clear();
					ccl.getDirectConflicts().clear();
					ccl.getTextualConflicts().clear();
					
					for ( FieldChangeLog fcl : ccl.getFields() ) {
						fcl.setHierarchicalDirectConflict(false);
						fcl.setHierarchicalIndirectConflict(false, 0);
						fcl.getIndirectConflicts().clear();
						fcl.getDirectConflicts().clear();
						fcl.getTextualConflicts().clear();
					}
					
					for ( MethodChangeLog mcl : ccl.getMethods() ) {
						mcl.setHierarchicalDirectConflict(false);
						mcl.setHierarchicalIndirectConflict(false, 0);
						mcl.getIndirectConflicts().clear();
						mcl.getDirectConflicts().clear();
						mcl.getTextualConflicts().clear();
					}
				}
			}
		}
		
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
		
		System.out.println("Cleaning information:  "+evolutionUI.getWorkDirectoryPath());
		
		if(changeLogHistorySource == null || changeLogHistoryTarget == null) {
			GUIUtils.addErrorMessage("No data to clean conflict analisys");
			return;
		}
		
		ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().writeHistoryChangeLogFile(changeLogHistorySource, new File(evolutionUI.getWorkDirectoryPath()+PluginConstants.HISTORY_CHANGE_LOG_SOURCE_FILE));
		ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().writeHistoryChangeLogFile(changeLogHistoryTarget, new File(evolutionUI.getWorkDirectoryPath()+PluginConstants.HISTORY_CHANGE_LOG_TARGET_FILE));
		
		ConflictModelParserFactory.getConflictModelParser().writeConflictModelFile(conflictModel, new File(evolutionUI.getWorkDirectoryPath()+PluginConstants.CONFLICT_MODEL_FILE));
		
		evolutionUI.showEvolutionsData(changeLogHistorySource, changeLogHistoryTarget, conflictModel, null);
	}
}
