/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.gui.managers;

import java.io.File;

import br.ufrn.spl.ev.PluginConstants;
import br.ufrn.spl.ev.engines.conflicts.pseudo.PseudosConflictsDetection;
import br.ufrn.spl.ev.gui.swt.ShowEvolutionsUI;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;
import br.ufrn.spl.ev.models.conflictmodel.ConflictType;
import br.ufrn.spl.ev.parsers.ChangeLogHistoryModelParserFactory;
import br.ufrn.spl.ev.parsers.ConflictModelParserFactory;
import br.ufrn.spl.ev.util.GUIUtils;

/**
 * <p>
 * Executes the process of analyze textual conflicts
 * </p>
 * 
 * <p>
 * Controls the flow of the tool, call the strategy to make the job, and after
 * call a UI to show the results.
 * </p>
 * 
 * @author Gleydson Lima
 * @vesion 1.0 - Class Creation.
 * @since 16/11/2013
 * 
 */
public class AnalyzePseudosConflictsManager {

	/* Reference to the main UI of the tool */
	private ShowEvolutionsUI evolutionUI;

	public AnalyzePseudosConflictsManager(ShowEvolutionsUI evolutionUI) {
		this.evolutionUI = evolutionUI;
	}

	/**
	 * Make the process the execution of indirect conflicts
	 * 
	 * @throws Exception
	 */
	public ConflictModel executePseudosConflictsAnalisis(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget, ConflictModel conflictModel)	throws Exception {

		if(conflictModel == null) 
			conflictModel = new ConflictModel();
		else
			conflictModel.clearConflicts(ConflictType.PSEUDO);
		
		if (changeLogHistorySource == null) {
			GUIUtils.addErrorMessage("Please, run direct Conflict analisis first!");
		} else {
	
			conflictModel = new PseudosConflictsDetection().checkPseudosConflicts(changeLogHistorySource, changeLogHistoryTarget, conflictModel);

			saveAndShowEvolutionResults(changeLogHistorySource, changeLogHistoryTarget, conflictModel);

			System.out.println("/////  TEXTUAL CONFLICT ANALYSIS DONE ");
		}
		
		return conflictModel;
	}

	/**
	 * <p>
	 * Execute the information for the user and show it for the user in the GUI
	 * </p>
	 * 
	 * <p>
	 * Created at: 14/10/2013
	 * </p>
	 * 
	 * @param startTime
	 * @param changeLogHistorySource
	 * @param changeLogHistoryTarget
	 * @return
	 */
	protected void saveAndShowEvolutionResults(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget, ConflictModel conflictModel) {

		System.out.println("Saving textual conflict information in:  "+evolutionUI.getWorkDirectoryPath());
		
		ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().writeHistoryChangeLogFile(changeLogHistorySource, new File(evolutionUI.getWorkDirectoryPath()+PluginConstants.HISTORY_CHANGE_LOG_SOURCE_FILE));
		ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().writeHistoryChangeLogFile(changeLogHistoryTarget, new File(evolutionUI.getWorkDirectoryPath()+PluginConstants.HISTORY_CHANGE_LOG_TARGET_FILE));
		
		ConflictModelParserFactory.getConflictModelParser().writeConflictModelFile(conflictModel, new File(evolutionUI.getWorkDirectoryPath()+PluginConstants.CONFLICT_MODEL_FILE));
		
		evolutionUI.showEvolutionsData(changeLogHistorySource, changeLogHistoryTarget, null, null);

	}
}
