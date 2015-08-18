/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.merge;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import br.ufrn.spl.ev.gui.swt.ShowEvolutionsUI;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;
import br.ufrn.spl.ev.models.dependencemodel.DependenceModel;

/**
 * <p>This class claculate how easy or difficult is make a merge of the sistems.</p>
 * 
 * <pre>
 * 		Using the formula:
 * 
 *     (  ( Amount of ChangeLogs Direct Conflicts + Amount of ChangeLogs with Indirect Conflicts ) / Number Total of ChangeLogs  ) * 10
 *     
 *     
 *     10 to 9  = Very Hard
 *     8 to 7   = Hard
 *     6 to 5   = Medium 
 *     4 to 3   = Easy  
 *     2 to 0   = Very Easy
 *     
 * </pre>
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class CalculateIntegrationIndex {

	/** For ours, resolve direct conflict is 10X  more complex than indirect*/
	public final float DIRECT_CONFLICT_WEIGHT = 2.0f;
	
	/** For ours, resolve indirect conflict is 10X  easier than direct*/
	public final float INDIRECT_CONFLICT_WEIGHT = 0.5f;
	
	public final float DEPENDENCE_WEIGHT = 1.5f;
	
	public void calculate(final ShowEvolutionsUI showEvolutionsUI, final ChangeLogHistory changeLogHistorySource, final ChangeLogHistory changeLogHistoryTarget
			, final ConflictModel conflictModel, final DependenceModel dependenceModel) {
		
		float integrationIndex = 0.0f;
		
		int qtdTotalChangeLogs = getQtdChangeLogs(changeLogHistorySource, changeLogHistoryTarget);
		int qtdChangeLogsDirectConflict = getQtdChangeLogsDirectConflict(changeLogHistorySource, changeLogHistoryTarget);
		int qtdChangeLogsIndirectConflict = getQtdChangeLogsIndirectConflict(changeLogHistorySource, changeLogHistoryTarget);
		float qtdDependenceChangeLogs= getQtdDependenceChangeLogs(dependenceModel);
		
		float indexDirectConflict =     (qtdChangeLogsDirectConflict   * DIRECT_CONFLICT_WEIGHT )   / qtdTotalChangeLogs ;
		float indexIndirectConflict =   (qtdChangeLogsIndirectConflict * INDIRECT_CONFLICT_WEIGHT ) / qtdTotalChangeLogs ;
		float indexDependenceConflict = (qtdDependenceChangeLogs       * DEPENDENCE_WEIGHT )        / qtdTotalChangeLogs ;
		
		System.out.println("Direct Conflict Index: "+indexDirectConflict);
		System.out.println("Indirect Conflict Index: "+indexIndirectConflict);
		System.out.println("Dependence Index: "+indexDependenceConflict);
		
		integrationIndex = (float) ( (float) indexDirectConflict + (float) indexIndirectConflict + (float)  indexDependenceConflict );
		
		integrationIndex = (float) integrationIndex * 10;
		
		System.out.println("Integration Index: "+integrationIndex);
		
		final float finalIntegrationIndex = integrationIndex;
		
		// update the screen in a different thread
		Display.getDefault().asyncExec(new Runnable() {
	         public void run() {
	        	 determianteScale(showEvolutionsUI, finalIntegrationIndex);
	         }
	    });

	}

	private float getQtdDependenceChangeLogs(final DependenceModel dependenceModel) {
		if(dependenceModel != null && dependenceModel.getDepedencesMap() != null)
			return (float) ( (float) dependenceModel.getDepedencesMap().size() );
		else
			return 0;
	}

	private void determianteScale(final ShowEvolutionsUI showEvolutionsUI, float facilityMergeLevel) {
		if(facilityMergeLevel >= 9)
			setVeryHardLabel(showEvolutionsUI);
		else{
			if(facilityMergeLevel < 9 && facilityMergeLevel >= 7)
				setHardLabel(showEvolutionsUI);
			else{
				if(facilityMergeLevel < 7 && facilityMergeLevel >= 5)
					setMediumLabel(showEvolutionsUI);
				else{
					if(facilityMergeLevel < 5 && facilityMergeLevel >= 3)
						setEasyLabel(showEvolutionsUI);
					else{
						setVeryEasyLabel(showEvolutionsUI);
					}
				}
			}
		}
	}
	
	private int getQtdChangeLogsIndirectConflict(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget) {
		int total = 0;
		for (FeatureChangeLog feature : changeLogHistorySource.getFeatures() ) {
			for (ChangeLog changeLog : feature.getChangelogs() ) {
				if(changeLog.isIndirectlyConflicting())
					total++;
			}
		}
		for (FeatureChangeLog feature : changeLogHistoryTarget.getFeatures() ) {
			for (ChangeLog changeLog : feature.getChangelogs() ) {
				if(changeLog.isIndirectlyConflicting())
					total++;
			}
		}
		return total;
	}

	private int getQtdChangeLogsDirectConflict( ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget) {
		int total = 0;
		for (FeatureChangeLog feature : changeLogHistorySource.getFeatures() ) {
			for (ChangeLog changeLog : feature.getChangelogs() ) {
				if(changeLog.isDirectlyConflicting())
					total++;
			}
		}
		for (FeatureChangeLog feature : changeLogHistoryTarget.getFeatures() ) {
			for (ChangeLog changeLog : feature.getChangelogs() ) {
				if(changeLog.isDirectlyConflicting())
					total++;
			}
		}
		return total;
	}

	private int getQtdChangeLogs(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget) {
		int total = 0;
		for (FeatureChangeLog feature : changeLogHistorySource.getFeatures() ) {
			total+= feature.getChangelogs().size();
		}
		for (FeatureChangeLog feature : changeLogHistoryTarget.getFeatures() ) {
			total+= feature.getChangelogs().size();
		}
		return total;
	}

	private void setVeryHardLabel(ShowEvolutionsUI showEvolutionsUI){
		showEvolutionsUI.getFacilityMergeLevelLabel().setBackground( showEvolutionsUI.getDisplay().getSystemColor(SWT.COLOR_DARK_RED ) );
		showEvolutionsUI.getFacilityMergeLevelLabel().setText(formatLabelText("VERY HARD"));
	}

	private void setHardLabel(ShowEvolutionsUI showEvolutionsUI){
		showEvolutionsUI.getFacilityMergeLevelLabel().setBackground( showEvolutionsUI.getDisplay().getSystemColor(SWT.COLOR_RED) );
		showEvolutionsUI.getFacilityMergeLevelLabel().setText(formatLabelText("HARD") );
	}
	
	
	private void setMediumLabel(ShowEvolutionsUI showEvolutionsUI){
		showEvolutionsUI.getFacilityMergeLevelLabel().setBackground( showEvolutionsUI.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW) );
		showEvolutionsUI.getFacilityMergeLevelLabel().setText(formatLabelText("MEDIUM"));
	}
	
	private void setEasyLabel(ShowEvolutionsUI showEvolutionsUI){
		showEvolutionsUI.getFacilityMergeLevelLabel().setBackground( showEvolutionsUI.getDisplay().getSystemColor(SWT.COLOR_GREEN) );
		showEvolutionsUI.getFacilityMergeLevelLabel().setText(formatLabelText("EASY"));
	}
	
	private void setVeryEasyLabel(ShowEvolutionsUI showEvolutionsUI){
		showEvolutionsUI.getFacilityMergeLevelLabel().setBackground( showEvolutionsUI.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN) );
		showEvolutionsUI.getFacilityMergeLevelLabel().setText(formatLabelText("VERY EASY"));
	}

	private String formatLabelText(String text) {
		int textSide = text.length();
		int missing = 40 - textSide;
		
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i<  missing/2 ; i++) {
			buffer.append(" "); 
		}
		buffer.append(text);
		for (int i = 0; i<  missing/2 ; i++) {
			buffer.append(" "); 
		}
		
		return buffer.toString();
	}
	
}
