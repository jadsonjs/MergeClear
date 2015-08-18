/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.statistics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogType;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;

/**
 * <p>This class group and short the task, to facilitate our qualitative analysis Not in the more optimized, 
 * but in the easer way, making several "fors".</p>
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class GroupingAndSortingTasks {

	
	
	
	/** Register a statistic of evolution from the Change Log History */
	public GroupingAndSortingTasks groupAndSortingTasks(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget, String filePath){
		
		
		try {
			 
			BufferedWriter buffWrite = new BufferedWriter(new FileWriter(filePath+"/tasks_analisis.txt"));
			buffWrite.append("========================== TasksGrouped And Sorted ==========================");
			
			groupingAndSortingSourceTask(buffWrite, changeLogHistorySource);
			groupingAndSortingTargetTask(buffWrite, changeLogHistoryTarget);
			
			
			buffWrite.close();
			
		} catch (IOException ex) {
			 ex.printStackTrace();
		}
		return this;
	}
	
	
	private void groupingAndSortingSourceTask(BufferedWriter buffWrite, ChangeLogHistory changeLogHistorySource) throws IOException {
		buffWrite.append("\n\n-------------------------- Source Tasks -------------------------- ");
		groupingAndSortingByKindOfTask(buffWrite, changeLogHistorySource);
	}
	
	private void groupingAndSortingTargetTask(BufferedWriter buffWrite, ChangeLogHistory changeLogHistoryTarget) throws IOException {
		buffWrite.append("\n\n-------------------------- Target Tasks -------------------------- ");
		groupingAndSortingByKindOfTask(buffWrite, changeLogHistoryTarget);
	}


	private void groupingAndSortingByKindOfTask(BufferedWriter buffWrite, ChangeLogHistory changeLogHistory) throws IOException {
		
		List<ChangeLog> new_use_cases = new ArrayList<ChangeLog>();
		
		for (FeatureChangeLog feature : changeLogHistory.getFeatures()) {
			for (ChangeLog changeLog : feature.getChangelogs()) {
				if( changeLog.getType() == ChangeLogType.NEW_USE_CASE || changeLog.getType() == ChangeLogType.NEW_FUNCIONALITY){
					new_use_cases.add(changeLog);
				}
				
			}
		}
		
		buffWrite.append("\n\n                === NEW_USE_CASE TASKS ("+new_use_cases.size()+") ===           ");
		
		groupingAndSortingByKindOfConflict(buffWrite, new_use_cases);
		
		
		List<ChangeLog> upgrades = new ArrayList<ChangeLog>();
		
		for (FeatureChangeLog feature : changeLogHistory.getFeatures()) {
			for (ChangeLog changeLog : feature.getChangelogs()) {
				if(changeLog.getType() == ChangeLogType.UPGRADING || changeLog.getType() == ChangeLogType.REFACTORY){
					upgrades.add(changeLog);
				}
				
			}
		}
		
		buffWrite.append("\n\n                === UPGRADING TASKS ("+upgrades.size()+") ===                ");
		
		groupingAndSortingByKindOfConflict(buffWrite, upgrades);
		
		
		
		List<ChangeLog> bugs = new ArrayList<ChangeLog>();
		
		for (FeatureChangeLog feature : changeLogHistory.getFeatures()) {
			for (ChangeLog changeLog : feature.getChangelogs()) {
				if(changeLog.getType() == ChangeLogType.BUG_FIX){
					bugs.add(changeLog);
				}
			}
		}
		
		buffWrite.append("\n\n                === BUG_FIX TASKS ("+bugs.size()+") ===                ");
		
		groupingAndSortingByKindOfConflict(buffWrite, bugs);
		
		
		
	}
	


	private void groupingAndSortingByKindOfConflict(BufferedWriter buffWrite, List<ChangeLog> kindOfTask) throws IOException {
		
		List<ChangeLog> noConflicts = new ArrayList<ChangeLog>();
		
		for (ChangeLog changeLog : kindOfTask)
			if( ! changeLog.isDirectlyConflicting() && ! changeLog.isIndirectlyConflicting() && ! changeLog.isTextualConflicting() ){
				noConflicts.add(changeLog);
			}
		
		buffWrite.append("\n\n                ---  No Conflitcs  ("+noConflicts.size()+")  ---          \n");		
		printChangeLogs(buffWrite, noConflicts);

		
		//////////////////////////////////////////////////////
		
		List<ChangeLog> direct = new ArrayList<ChangeLog>();
		
		
		for (ChangeLog changeLog : kindOfTask)
			if( changeLog.isDirectlyConflicting() ){
				direct.add(changeLog);
			}
		
		
		buffWrite.append("\n\n                ---  Direct Conflitcs ("+direct.size()+")  ---         \n");
		printChangeLogs(buffWrite, direct);
		
		//////////////////////////////////////////////////////
		
		
		List<ChangeLog> inDirect = new ArrayList<ChangeLog>();
		
		for (ChangeLog changeLog : kindOfTask)
			if( ! changeLog.isDirectlyConflicting() && changeLog.isIndirectlyConflicting() ){
				inDirect.add(changeLog);
			}
		
		buffWrite.append("\n\n                ---  InDirect Conflitcs ("+inDirect.size()+")  ---         \n");
		printChangeLogs(buffWrite, inDirect);
		
		///////////////////////////////////////////////////
		
		List<ChangeLog> pseudo = new ArrayList<ChangeLog>();

		for (ChangeLog changeLog : kindOfTask)
			if( ! changeLog.isDirectlyConflicting() && ! changeLog.isIndirectlyConflicting() && changeLog.isTextualConflicting() ){
				pseudo.add(changeLog);
			}
		
		buffWrite.append("\n\n                ---  Pseudo Conflitcs ("+pseudo.size()+")  ---         \n");		
		printChangeLogs(buffWrite, pseudo);
		
	}
	
	
	private void printChangeLogs(BufferedWriter buffWrite, List<ChangeLog> changeLogs )  throws IOException{
		
		int qtdClasses = 0;
		
		for (ChangeLog changeLog : changeLogs) {
			
			String modulo = null;
			boolean containsClassesDifferentModules = false;

			// See is there were changes in different modules in the task
			for (ClassChangeLog _class : changeLog.getClasses()) {
				String[] tokens = _class.getSignature().split("/");
				
				if(modulo == null){
					modulo = tokens[0];
				}else{
					if( ! modulo.equalsIgnoreCase(tokens[0]) ){
						containsClassesDifferentModules = true;
						break;
					}
				}
			}
			
			buffWrite.append("\n"+changeLog.getIdentify() + " - " + changeLog.getDescription() 
					+"        ( SIZE: "+changeLog.getClasses().size()+";"+" Different Modules? "+containsClassesDifferentModules+")"  );
			
			qtdClasses += changeLog.getClasses().size();
			
		}	
		
		buffWrite.append("\n\n"+" AVARAGE OF TASK SIZE: "+ ( (float) qtdClasses / changeLogs.size() )+"  \n\n "  );
		
	}
	
}
