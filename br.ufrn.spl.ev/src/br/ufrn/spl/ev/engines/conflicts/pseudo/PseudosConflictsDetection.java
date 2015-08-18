/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.pseudo;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.ufrn.spl.ev.exceptions.IndirectConflictsDetectionException;
import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;
import br.ufrn.spl.ev.util.ChangeLogHistoryUtil;

/**
 * <p>
 * The interface of textual conflicts algoritm. This class implements directly
 * conflits analisys
 * </p>
 * 
 * 
 * <p>
 * 
 * <pre>
 *      Textual Conflicts Detection Algorithm:
 * 
 *       A.2.2 If a "X" asset were modified in the source (if it is not on Target HistoryChangeLog )
 * 
 *       A.2.2.1 If a "X" asset were modified in the target (if it is not on Target HistoryChangeLog ), but
 *       there is no direct and indirect conflict detected.
 * 
 * </pre>
 * 
 * </p>
 * 
 * @author Gleydson Lima - gleydson.lima@gmail.com
 * 
 * @version 1.0 - Class Creation
 */
public class PseudosConflictsDetection {

	private ChangeLogHistory changeLogHistorySource;
	private ChangeLogHistory changeLogHistoryTarget;

	/* *************************************************** */

	/**
	 * <p>
	 * Receives a list of assets selected by the user, analyzes the indirect
	 * conflicts and return the model of conflict.
	 * </p>
	 * 
	 * <p>
	 * This is a <b>Template Method pattern</b> with the algorithm of check
	 * indirect conflict
	 * </p>
	 * 
	 * @param selectedAssets
	 * @return
	 * @throws Exception
	 */
	public ConflictModel checkPseudosConflicts(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget, ConflictModel conflictModel) throws IndirectConflictsDetectionException {

		this.changeLogHistorySource = changeLogHistorySource;
		this.changeLogHistoryTarget = changeLogHistoryTarget;

		// loadData(changeLogHistorySource, changeLogHistoryTarget); - data was
		// loaded by direct analisis
		performAnalysis(conflictModel);
		
		return conflictModel;
	}

	/**
	 * This method contains the algorithm of analysis describe in the class
	 * comment.
	 * 
	 */
	private final void performAnalysis(ConflictModel conflictModel) throws IndirectConflictsDetectionException {

		
		System.out.println("TEXTUAL CONFLICTING ANALISYS");
		Set<ClassChangeLog> assetTarget = new TreeSet<ClassChangeLog>();

		// perform analisys in the target

		for (FeatureChangeLog fTarget : changeLogHistoryTarget.getFeatures()) {
			for (ChangeLog changeLogTarget : fTarget.getChangelogs()) {
				for (ClassChangeLog classChangeLog : changeLogTarget.getClasses()) {
					assetTarget.add( classChangeLog );
				}
			}
		}

		int totalTextual = 0;
		
		//
		// run the direct conflict just to the selected asset, to be faster if you don't want to apply all assets
		//
		List<AssetChangeLog> selectedAssetOnSource = ChangeLogHistoryUtil.getSelectedSourceCodeAssets(changeLogHistorySource);
		
		
		Set<ChangeLog> changeLogs = ChangeLogHistoryUtil.getChangeLogOfAssets(selectedAssetOnSource);
		
		for (ChangeLog changeLogSource : changeLogs) {
			for (ClassChangeLog classChangeLog : changeLogSource.getClasses()) {
				if (classChangeLog.isSelected()){
					if (!classChangeLog.isDirectlyConflicting()) {
						for ( ClassChangeLog classTarget :  assetTarget ) {
							
							if (classTarget.getSignature().equals(classChangeLog.getSignature())) {
									classChangeLog.setTextualConflicting(true);
									changeLogSource.setTextualConflicting(true);
									classChangeLog.addTextualConflict(classTarget);
									classTarget.setTextualConflicting(true);
									classTarget.getChangelog().setTextualConflicting(true);
									totalTextual++;
									
									conflictModel.addTextualConflict(classChangeLog, classTarget);
							}		
						}		
					}
				}
			}
		}


		System.out.println("TEXTUAL ANALISYS ENDED: " + totalTextual);
	}

}
