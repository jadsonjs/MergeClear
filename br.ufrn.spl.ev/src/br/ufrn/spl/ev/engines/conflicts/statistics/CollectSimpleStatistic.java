/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.statistics;

import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.ADDED_CLASS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.ADDED_CLASS_DIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.ADDED_CLASS_INDIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.ADDED_CLASS_TEXTUAL_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.ADDED_FIELD;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.ADDED_FIELD_DIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.ADDED_FIELD_INDIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.ADDED_METHOD;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.ADDED_METHOD_DIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.ADDED_METHOD_INDIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.BUG_FIX;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.BUG_FIX_DIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.BUG_FIX_INDIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.BUG_FIX_TEXTUAL_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.CLASS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.FIELD;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.METHOD;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.NEW_FUNCIONALITY;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.NEW_FUNCIONALITY_DIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.NEW_FUNCIONALITY_INDIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.NEW_FUNCIONALITY_TEXTUAL_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.REMOVED_CLASS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.REMOVED_CLASS_DIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.REMOVED_CLASS_INDIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.REMOVED_CLASS_TEXTUAL_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.REMOVED_FIELD;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.REMOVED_FIELD_DIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.REMOVED_FIELD_INDIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.REMOVED_METHOD;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.REMOVED_METHOD_DIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.REMOVED_METHOD_INDIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.TOTAL_EVOLUTIONS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.TOTAL_EVOLUTIONS_DIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.TOTAL_EVOLUTIONS_INDIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.TOTAL_EVOLUTIONS_TEXTUAL_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.TOTAL_EVOLUTIONS_WITHOUT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.UPDATED_CLASS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.UPDATED_CLASS_DIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.UPDATED_CLASS_INDIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.UPDATED_CLASS_TEXTUAL_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.UPDATED_FIELD;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.UPDATED_FIELD_DIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.UPDATED_FIELD_INDIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.UPDATED_METHOD;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.UPDATED_METHOD_DIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.UPDATED_METHOD_INDIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.UPGRADING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.UPGRADING_DIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.UPGRADING_INDIRECT_CONFLICTING;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectSimpleStatisticConstants.UPGRADING_TEXTUAL_CONFLICTING;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.TreeSet;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogType;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;

/**
 * <p>Collect simple statistic information</p>
 *
 * <p>Like the number of evolution, evolutions by task type, </p>
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 04/11/2013
 *
 */
public class CollectSimpleStatistic implements ChangeLogHistoryStatistics{

	
	
	/** Keep all the statistics collected*/
	private Map<String, Integer> statistic = new TreeMap<String, Integer>();
	
	
	public CollectSimpleStatistic(){
		 statistic = new HashMap<String, Integer>();
	}
	
	
	
	/**
	 * Collect the statistics in the change log history
	 * 
	 * @see br.ufrn.spl.ev.engines.conflicts.statistics.ChangeLogHistoryStatistics#collectStatistics(br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory)
	 */
	@Override
	public void collectStatistics(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget, String filePath) {
		
		long time = System.currentTimeMillis();
		
		collectStatistics(changeLogHistorySource, filePath+"simple_statistic_source.properties");
		
		statistic = new TreeMap<String, Integer>();
		
		collectStatistics(changeLogHistoryTarget, filePath+"simple_statistic_target.properties");
		
		System.out.println(" Collect Statistics spend " + (System.currentTimeMillis() - time) + " ms");
	}
	
	
	public void collectStatistics(ChangeLogHistory changeLogHistory, String fileName) {
		
		if(changeLogHistory == null) return;
		
		List<FeatureChangeLog> features = changeLogHistory.getFeatures();
		
		for(FeatureChangeLog feature : features){
		
			List<ChangeLog> changeLogs =  feature.getChangelogs();
			
			//collectStatistics(TOTAL_EVOLUTIONS, changeLogs.size());
			
			for (ChangeLog changeLog : changeLogs ) {
				
				collectStatistics(TOTAL_EVOLUTIONS);
				
				if( changeLog.getType() == ChangeLogType.NEW_FUNCIONALITY || changeLog.getType() == ChangeLogType.NEW_USE_CASE){
					collectStatistics(NEW_FUNCIONALITY);
					
					if(changeLog.isDirectlyConflicting())
						collectStatistics(NEW_FUNCIONALITY_DIRECT_CONFLICTING);
					
					if(changeLog.isIndirectlyConflicting())
						collectStatistics(NEW_FUNCIONALITY_INDIRECT_CONFLICTING);
					
					if(changeLog.isTextualConflicting())
						collectStatistics(NEW_FUNCIONALITY_TEXTUAL_CONFLICTING);
				}else{
				

					if( changeLog.getType() == ChangeLogType.UPGRADING || changeLog.getType() == ChangeLogType.REFACTORY){
						collectStatistics(UPGRADING);
						
						if(changeLog.isDirectlyConflicting())
							collectStatistics(UPGRADING_DIRECT_CONFLICTING);
						
						if(changeLog.isIndirectlyConflicting())
							collectStatistics(UPGRADING_INDIRECT_CONFLICTING);
						
						if(changeLog.isTextualConflicting())
							collectStatistics(UPGRADING_TEXTUAL_CONFLICTING);
						
					}else{
					
						if( changeLog.getType() == ChangeLogType.BUG_FIX){
							collectStatistics(BUG_FIX);
							
							if(changeLog.isDirectlyConflicting())
								collectStatistics(BUG_FIX_DIRECT_CONFLICTING);
							
							if(changeLog.isIndirectlyConflicting())
								collectStatistics(BUG_FIX_INDIRECT_CONFLICTING);
							
							if(changeLog.isTextualConflicting())
								collectStatistics(BUG_FIX_TEXTUAL_CONFLICTING);
							
						}else{
							System.out.println(" >>>>> "+changeLog.getType());
							collectStatistics("UNKNOW");
						}
					}
				
				}
				
// 				not use this any more, together with NEW_FUNCIONALITY 
//				if( changeLog.getType() == ChangeLogType.NEW_USE_CASE){
//					collectStatistics(NEW_USE_CASE);
//					
//					if(changeLog.isDirectlyConflicting())
//						collectStatistics(NEW_USE_CASE_DIRECT_CONFLICTING);
//					
//					if(changeLog.isIndirectlyConflicting())
//						collectStatistics(NEW_USE_CASE_INDIRECT_CONFLICTING);
//					
//					if(changeLog.isTextualConflicting())
//						collectStatistics(NEW_USE_CASE_TEXTUAL_CONFLICTING);
//				}
				
				
				if(changeLog.isDirectlyConflicting())
					collectStatistics(TOTAL_EVOLUTIONS_DIRECT_CONFLICTING);
				if(changeLog.isIndirectlyConflicting())
					collectStatistics(TOTAL_EVOLUTIONS_INDIRECT_CONFLICTING);
				if(changeLog.isTextualConflicting())
					collectStatistics(TOTAL_EVOLUTIONS_TEXTUAL_CONFLICTING);
				
				if( ! changeLog.isDirectlyConflicting() && ! changeLog.isIndirectlyConflicting() && ! changeLog.isTextualConflicting())
					collectStatistics(TOTAL_EVOLUTIONS_WITHOUT_CONFLICTING);
				
				for (ClassChangeLog _class : changeLog.getClasses() ) {
				
					collectStatistics(CLASS);
					
					if( _class.isAdded() ){
						collectStatistics(ADDED_CLASS);
						
						if(_class.isDirectlyConflicting())
							collectStatistics(ADDED_CLASS_DIRECT_CONFLICTING);
						
						if(_class.isIndirectlyConflicting())
							collectStatistics(ADDED_CLASS_INDIRECT_CONFLICTING);
						
						if(_class.isTextualConflicting())
							collectStatistics(ADDED_CLASS_TEXTUAL_CONFLICTING);
					}
						
					if( _class.isDelete() ){
						collectStatistics(REMOVED_CLASS);
						
						if(_class.isDirectlyConflicting())
							collectStatistics(REMOVED_CLASS_DIRECT_CONFLICTING);
						
						if(_class.isIndirectlyConflicting())
							collectStatistics(REMOVED_CLASS_INDIRECT_CONFLICTING);
						
						if(_class.isTextualConflicting())
							collectStatistics(REMOVED_CLASS_TEXTUAL_CONFLICTING);
						
					}
						
					if( _class.isUpdated() ){
						collectStatistics(UPDATED_CLASS);
						
						if(_class.isDirectlyConflicting())
							collectStatistics(UPDATED_CLASS_DIRECT_CONFLICTING);
						
						if(_class.isIndirectlyConflicting())
							collectStatistics(UPDATED_CLASS_INDIRECT_CONFLICTING);
						
						if(_class.isTextualConflicting())
							collectStatistics(UPDATED_CLASS_TEXTUAL_CONFLICTING);
					}
					
					
					for (FieldChangeLog field : _class.getFields() ) {
						
						collectStatistics(FIELD);
						
						if( field.isAdded() ){
							collectStatistics(ADDED_FIELD);
							
							if(field.isDirectlyConflicting())
								collectStatistics(ADDED_FIELD_DIRECT_CONFLICTING);
							
							if(field.isIndirectlyConflicting())
								collectStatistics(ADDED_FIELD_INDIRECT_CONFLICTING);	
						}
							
						if( field.isDelete() ){
							collectStatistics(REMOVED_FIELD);
							
							if(field.isDirectlyConflicting())
								collectStatistics(REMOVED_FIELD_DIRECT_CONFLICTING);
							
							if(field.isIndirectlyConflicting())
								collectStatistics(REMOVED_FIELD_INDIRECT_CONFLICTING);	
							
						}
							
						if( field.isUpdated() ){
							collectStatistics(UPDATED_FIELD);
							
							if(field.isDirectlyConflicting())
								collectStatistics(UPDATED_FIELD_DIRECT_CONFLICTING);
							
							if(field.isIndirectlyConflicting())
								collectStatistics(UPDATED_FIELD_INDIRECT_CONFLICTING);
						}
						
					
						
					}
					
					for (MethodChangeLog method : _class.getMethods() ) {
						
						collectStatistics(METHOD);
						
						if( method.isAdded() ){
							collectStatistics(ADDED_METHOD);
							
							if(method.isDirectlyConflicting())
								collectStatistics(ADDED_METHOD_DIRECT_CONFLICTING);
							
							if(method.isIndirectlyConflicting())
								collectStatistics(ADDED_METHOD_INDIRECT_CONFLICTING);	
						}
							
						if( method.isDelete() ){
							collectStatistics(REMOVED_METHOD);
							
							if(method.isDirectlyConflicting())
								collectStatistics(REMOVED_METHOD_DIRECT_CONFLICTING);
							
							if(method.isIndirectlyConflicting())
								collectStatistics(REMOVED_METHOD_INDIRECT_CONFLICTING);	
							
						}
							
						if( method.isUpdated() ){
							collectStatistics(UPDATED_METHOD);
							
							if(method.isDirectlyConflicting())
								collectStatistics(UPDATED_METHOD_DIRECT_CONFLICTING);
							
							if(method.isIndirectlyConflicting())
								collectStatistics(UPDATED_METHOD_INDIRECT_CONFLICTING);
						}
					}
				}
				
			}
		}
	
		saveStatisticsCollected(fileName);
		
	}
	

	
	
	/*
	 * increment one unit to a specific statistic
	 */
	public void collectStatistics(String type){
		if(! statistic.containsKey(type))
			statistic.put(type, 1);
		else
			statistic.put(type, (statistic.get(type)+1));
	}
	
	/*
	 * increment several units to a specific statistic
	 */
	public void collectStatistics(String type, int qtd){
		if(! statistic.containsKey(type))
			statistic.put(type, qtd);
		else
			statistic.put(type, (statistic.get(type)+qtd));
	}
	
	
	
	/**
	 * 
	 * @see br.ufrn.spl.ev.engines.conflicts.statistics.ChangeLogHistoryStatistics#saveStatisticsCollected(java.lang.String)
	 */
	public void saveStatisticsCollected(String fileName) {
		
		// Create a sorted properties file //
		Properties properties = new Properties() {
			private static final long serialVersionUID = 1L;

			@Override
            public synchronized Enumeration<Object> keys() {
                return Collections.enumeration(
                          new TreeSet<Object>(super.keySet())
                 );
            }
        };
        
		FileOutputStream fos = null;
		
		try {
			 
			for (String key : statistic.keySet()) {
				properties.setProperty(key, String.valueOf(statistic.get( key ) ) );
			}
			         
	        fos = new FileOutputStream(fileName);
	        properties.store(fos, "EVOLUTION STATISTICS PROPERTIES");
	        fos.close();
		 } catch (IOException ex) {
			 ex.printStackTrace();
			 System.out.println(ex.getMessage());
		 }finally{
			 try {
				 fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
	}

}