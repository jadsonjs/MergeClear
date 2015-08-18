/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.statistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.dependencemodel.Dependence;
import br.ufrn.spl.ev.models.dependencemodel.DependenceModel;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class DependenceStatisticsBetweenTasks implements DependenceStatistics {

	/** Keep statistics about the amount of dependences
	 * 
	 * Key  = amount of dependents changes logs
	 * value = the value calculated
	 * 
	 */
	private Map<Integer, Integer> amountDependencesSource = new TreeMap<Integer, Integer>();
	
	
	/** Keep statistics about the amount of dependences
	 * 
	 * Key  = amount of dependents changes logs
	 * value = the value calculated
	 * 
	 */
	private Map<Integer, Integer> amountDependencesTarget = new TreeMap<Integer, Integer>();
	
	private int qtdChangeLogWithNoConflicts = 0;
	
	private int qtdChangeLogWithNoConflictsAndNoDepedence = 0;
	
	/**
	 * @see br.ufrn.spl.ev.engines.conflicts.statistics.DependenceStatistics#collectStatistics(br.ufrn.spl.ev.models.dependencemodel.DependenceModel, br.ufrn.spl.ev.models.dependencemodel.DependenceModel, java.lang.String)
	 */
	@Override
	public void collectStatistics(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget, 
									DependenceModel sourceDependenceMap, DependenceModel targetDependenceMap, String filePath) {
		
		
		calculate(amountDependencesSource,  sourceDependenceMap);
		
		calculate(amountDependencesTarget,  targetDependenceMap);
		
		calculateDependenceByConflict(changeLogHistorySource,  sourceDependenceMap);
		
		
		saveCollectedStatistics(filePath);
	}
	
	
	
	
	
	private void calculateDependenceByConflict( ChangeLogHistory changeLogHistorySource, DependenceModel sourceDependenceMap) {
		
		qtdChangeLogWithNoConflictsAndNoDepedence = 0;
		qtdChangeLogWithNoConflicts = 0;
		
		for (FeatureChangeLog feature : changeLogHistorySource.getFeatures()) {
			for (ChangeLog changeLog : feature.getChangelogs()) {
				if( ! changeLog.isDirectlyConflicting() && ! changeLog.isIndirectlyConflicting() ){  // it has no conflict
					
					qtdChangeLogWithNoConflicts++;
					
					// look if has dependence
					Dependence dependence =  sourceDependenceMap.get(changeLog);
					
					if(dependence == null){ // there is not dependence
						qtdChangeLogWithNoConflictsAndNoDepedence++;
					}
				}
			}
		}
		
	}





	private void calculate(Map<Integer, Integer> amountDependences, DependenceModel dependenceMap) {
		for (Dependence dependence : dependenceMap.getDepedencesMap()) {
			if( amountDependences.containsKey(dependence.getAssetDependents().size()) ){
				
				Integer amount = amountDependencesSource.get( dependence.getAssetDependents().size() );
				amountDependences.put(dependence.getAssetDependents().size(), amount+1);
			}else{
				amountDependences.put(dependence.getAssetDependents().size(), 1);
			}
		}
		
	}





	/**
	 * 
	 * @see br.ufrn.spl.ev.engines.conflicts.statistics.ChangeLogHistoryStatistics#saveStatisticsCollected(java.lang.String)
	 */
	private void saveCollectedStatistics(String filePath) {
		
		try{
			File file = new File(filePath+"/dependence_statistic.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
 
			StringBuilder buffer = new StringBuilder();
			
			buffer.append( printDependences(amountDependencesSource, "Source") );
			buffer.append( printDependences(amountDependencesTarget, "Target") );
			
			buffer.append(" \n\n qtdChangeLogWithNoConflictsAndNoDepedence \n ");
			buffer.append(qtdChangeLogWithNoConflictsAndNoDepedence);
			
			buffer.append(" \n\n qtdChangeLogWithNoConflicts \n ");
			buffer.append(qtdChangeLogWithNoConflicts);
			
			
			
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(buffer.toString());
			bw.close();

		 } catch (IOException ex) {
			 ex.printStackTrace();
			 System.out.println(ex.getMessage());
		 }
	}





	private String printDependences(Map<Integer, Integer> amountDependences, String side) {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("\n\n   =====  Dependences in "+side+" ======   \n\n");
		Integer total = 0;
		for (Integer amount : amountDependences.keySet()) {
			
			buffer.append(" "+amountDependences.get(amount)+" "+" change logs with "+amount+" direct dependence \n");
			
			total += amountDependences.get(amount);
		}
		
		buffer.append(" ----- \n");
		buffer.append(" "+total+" "+" total of change logs with direct dependence \n");
		buffer.append(" ========================================== ");
		
		return buffer.toString();
		
	}

}
