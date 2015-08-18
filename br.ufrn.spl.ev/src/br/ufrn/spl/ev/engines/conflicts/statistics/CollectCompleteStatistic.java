/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.statistics;

import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_ATOMIC_CLASS_ADDED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_ATOMIC_CLASS_REMOVED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_ATOMIC_CLASS_UPDATED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_ATOMIC_FIELD_ADDED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_ATOMIC_FIELD_REMOVED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_ATOMIC_FIELD_UPDATED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_ATOMIC_METHOD_ADDED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_ATOMIC_METHOD_REMOVED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_ATOMIC_METHOD_UPDATED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_LAYER_BUSINESS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_LAYER_DATA_ACCESS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_LAYER_ENTITY;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_LAYER_OTHERS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_LAYER_WEB;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_TASK_BUG_FIX;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_TASK_NEW_FUNCIONALITY;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_TASK_NEW_USE_CASE;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_TOTAL_EVOLUTIONS_ATOMIC;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_TOTAL_EVOLUTIONS_TASK;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_VARIABILITY_CONDITIONAL_EXEC;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.DIRECT_CONFLICTING_VARIABILITY_PATTERN;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_ATOMIC_CLASS_ADDED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_ATOMIC_CLASS_MODIFIED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_ATOMIC_CLASS_REMOVED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_ATOMIC_FIELD_ADDED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_ATOMIC_FIELD_REMOVED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_ATOMIC_FIELD_UPDATED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_ATOMIC_METHOD_ADDED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_ATOMIC_METHOD_REMOVED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_ATOMIC_METHOD_UPDATED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_ATOMIC_UPDATED_CLASS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_TASK_BUG_FIX;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_TASK_NEW_FUNCIONALITY;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_TASK_NEW_USE_CASE;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_TOTAL_ATOMIC;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_TOTAL_TASK;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_TOTAL_TASK_NO_CONFLICT;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_TOTAL_TASK_VARIABIITY_CONDEXEC;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_TOTAL_TASK_VARIABIITY_CONFLICT;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.EVOLUTIONS_TOTAL_TASK_VARIABIITY_PATTERN;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_ATOMIC_CLASS_ADDED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_ATOMIC_CLASS_REMOVED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_ATOMIC_CLASS_UPDATED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_ATOMIC_FIELD_ADDED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_ATOMIC_FIELD_REMOVED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_ATOMIC_FIELD_UPDATED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_ATOMIC_METHOD_ADDED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_ATOMIC_METHOD_REMOVED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_ATOMIC_METHOD_UPDATED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_LAYER_BUSINESS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_LAYER_DATA_ACCESS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_LAYER_ENTITY;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_LAYER_OTHERS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_LAYER_WEB;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_TASK_BUG_FIX;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_TASK_NEW_FUNCIONALITY;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_TASK_NEW_USE_CASE;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_TOTAL_EVOLUTIONS_ATOMIC;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_TOTAL_EVOLUTIONS_TASK;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_VARIABILITY_CONDITIONAL_EXEC;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.INDIRECT_CONFLICTING_VARIABILITY_PATTERN;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TASK_CONFLICT_DIRECT_AND_INDIRECT;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TASK_CONFLICT_DIRECT_AND_TEXTUAL;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TASK_CONFLICT_INDIRECT_AND_TEXTUAL;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TASK_CONFLICT_INDIRECT_AND_TEXTUAL_DIRECT;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TASK_CONFLICT_ONLY_DIRECT;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TASK_CONFLICT_ONLY_INDIRECT;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TASK_CONFLICT_ONLY_TEXTUAL;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TEXTUAL_CONFLICTING_ATOMIC_CLASS_UPDATED;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TEXTUAL_CONFLICTING_LAYER_BUSINESS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TEXTUAL_CONFLICTING_LAYER_DATA_ACCESS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TEXTUAL_CONFLICTING_LAYER_ENTITY;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TEXTUAL_CONFLICTING_LAYER_OTHERS;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TEXTUAL_CONFLICTING_LAYER_WEB;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TEXTUAL_CONFLICTING_TASK_BUG_FIX;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TEXTUAL_CONFLICTING_TASK_NEW_FUNCIONALITY;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TEXTUAL_CONFLICTING_TASK_NEW_USE_CASE;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TEXTUAL_CONFLICTING_TOTAL_EVOLUTIONS_ATOMIC;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TEXTUAL_CONFLICTING_TOTAL_EVOLUTIONS_TASK;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TEXTUAL_CONFLICTING_VARIABILITY_CONDITIONAL_EXEC;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TEXTUAL_CONFLICTING_VARIABILITY_PATTERN;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TOTAL_EVOLUTIONS_VARIABILITY_CONDITIONAL_EXEC;
import static br.ufrn.spl.ev.engines.conflicts.statistics.CollectCompleteStatisticConstants.TOTAL_EVOLUTIONS_VARIABILITY_PATTERN;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.eclipse.core.runtime.CoreException;

import br.ufrn.spl.ev.engines.merge.engine.Repository;
import br.ufrn.spl.ev.engines.miners.MinerFactory.MergeSide;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogType;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeTypeRepository;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;
import br.ufrn.spl.ev.util.GUIUtils;
import br.ufrn.spl.ev.variability.Variability;
import br.ufrn.spl.ev.variability.VariabilityRepository;

/**
 * <p>
 * Collect complete statistic information
 * </p>
 * 
 * <p>
 * Like the number of evolution, evolutions by task type, evolutions by modules, etc..
 * </p>
 * 
 * @author jadson - jadson@info.ufrn.br
 * @author Gleydson Lima - gleydson.lima@gmail.com
 * 
 * @vesion 1.0 - Class Creation.
 * @since 04/11/2013
 * 
 */
public class CollectCompleteStatistic implements ChangeLogHistoryStatistics {


	/** Keep all the statistics collected on the souce */
	private Map<String, Integer> statisticSource = new TreeMap<String, Integer>();
	
	/** Keep all the statistics collected on the target */
	private Map<String, Integer> statisticTarget = new TreeMap<String, Integer>();
	
	private char statisticMode = 'S'; // S ou T
	
	/** Evolutions Table */
	List<AtomicEvolution> tableSource = new ArrayList<AtomicEvolution>();

	/** Evolutions Table Target */
	List<AtomicEvolution> tableTarget = new ArrayList<AtomicEvolution>();
	
	/** Task Table Source */
	List<TaskStatistic> tableTaskSource = new ArrayList<TaskStatistic>();
	
	/** Task Table Target */
	List<TaskStatistic> tableTaskTarget = new ArrayList<TaskStatistic>();
	
	
	public CollectCompleteStatistic() {
		statisticSource = new HashMap<String, Integer>();
		statisticTarget = new HashMap<String, Integer>();
		
		// Some statistic are collect from the Java Project in the workspace, because of this we need initialize the Repository class.
		//
		try {
			Repository.init(Repository.getSourceProjectName(), Repository.getTargetProjectName());
		} catch (CoreException ex) {
			GUIUtils.addErrorMessage("Error when performing statistic collect: " + ex.toString());
			ex.printStackTrace();
		}
	}

	/**
	 * Collect the statistics in the change log history
	 * 
	 * @see br.ufrn.spl.ev.engines.conflicts.statistics.ChangeLogHistoryStatistics#collectStatistics(br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory)
	 */
	@Override
	public void collectStatistics(ChangeLogHistory changeLogHistory, ChangeLogHistory changeLogHistoryTarget, String filePath) {

		if (changeLogHistory == null)
			return;

		// Search for core or variabilities conflicts
		analizeLPSConflicts(changeLogHistory);
		
		// Global statistic collect
		collectGlobalStatisics(changeLogHistory);
		// Global statistic collect target
		statisticMode = 'T';
		collectGlobalStatisics(changeLogHistoryTarget);
		// create delta evolution table source
		createEvolutionTable(changeLogHistory, tableSource );
		// create delta evolution table source
		createEvolutionTable(changeLogHistoryTarget, tableTarget );
		
		createEvolutionTaskTable(changeLogHistory, tableTaskSource, MergeSide.SOURCE );
		createEvolutionTaskTable(changeLogHistoryTarget, tableTaskTarget, MergeSide.TARGET );
		
		saveStatisticsCollected(filePath);
	}
	
	public void initStatistics() {
		
		init(EVOLUTIONS_TOTAL_TASK);
		init(EVOLUTIONS_TOTAL_ATOMIC);
		init(EVOLUTIONS_TOTAL_TASK_NO_CONFLICT);
		init(EVOLUTIONS_TOTAL_TASK_VARIABIITY_PATTERN);
		init(EVOLUTIONS_TOTAL_TASK_VARIABIITY_CONDEXEC);
		

		init(EVOLUTIONS_TASK_NEW_FUNCIONALITY);
		init(EVOLUTIONS_TASK_BUG_FIX);
		init(EVOLUTIONS_TASK_NEW_USE_CASE);
		init(EVOLUTIONS_ATOMIC_CLASS_ADDED);
		init(EVOLUTIONS_ATOMIC_CLASS_REMOVED);
		
		init(EVOLUTIONS_ATOMIC_FIELD_ADDED);
		init(EVOLUTIONS_ATOMIC_FIELD_UPDATED);
		init(EVOLUTIONS_ATOMIC_FIELD_REMOVED);
		
		init(EVOLUTIONS_ATOMIC_METHOD_ADDED);
		init(EVOLUTIONS_ATOMIC_METHOD_UPDATED);
		init(EVOLUTIONS_ATOMIC_METHOD_REMOVED);
		
		init(DIRECT_CONFLICTING_TOTAL_EVOLUTIONS_ATOMIC);
		init(DIRECT_CONFLICTING_TOTAL_EVOLUTIONS_TASK);
		init(DIRECT_CONFLICTING_TASK_NEW_FUNCIONALITY);
		init(DIRECT_CONFLICTING_TASK_BUG_FIX);
		init(DIRECT_CONFLICTING_TASK_NEW_USE_CASE);
		init(DIRECT_CONFLICTING_ATOMIC_CLASS_ADDED);
		init(DIRECT_CONFLICTING_ATOMIC_CLASS_UPDATED);
		init(DIRECT_CONFLICTING_ATOMIC_CLASS_REMOVED);
		init(DIRECT_CONFLICTING_ATOMIC_FIELD_ADDED);
		init(DIRECT_CONFLICTING_ATOMIC_FIELD_UPDATED);
		init(DIRECT_CONFLICTING_ATOMIC_FIELD_REMOVED);
		

		init(DIRECT_CONFLICTING_ATOMIC_METHOD_ADDED);
		init(DIRECT_CONFLICTING_ATOMIC_METHOD_UPDATED);
		init(DIRECT_CONFLICTING_ATOMIC_METHOD_REMOVED);
			
	
		init(INDIRECT_CONFLICTING_TOTAL_EVOLUTIONS_TASK);
		init(INDIRECT_CONFLICTING_TOTAL_EVOLUTIONS_ATOMIC);

		init(INDIRECT_CONFLICTING_TASK_NEW_FUNCIONALITY);
		init(INDIRECT_CONFLICTING_TASK_BUG_FIX);
		init(INDIRECT_CONFLICTING_TASK_NEW_USE_CASE);

		init(INDIRECT_CONFLICTING_ATOMIC_CLASS_ADDED);
		init(INDIRECT_CONFLICTING_ATOMIC_CLASS_UPDATED);
		init(INDIRECT_CONFLICTING_ATOMIC_CLASS_REMOVED);

		init(INDIRECT_CONFLICTING_ATOMIC_FIELD_ADDED);
		init(INDIRECT_CONFLICTING_ATOMIC_FIELD_REMOVED);

		init(INDIRECT_CONFLICTING_ATOMIC_METHOD_ADDED);
		init(INDIRECT_CONFLICTING_ATOMIC_METHOD_UPDATED);
		init(INDIRECT_CONFLICTING_ATOMIC_METHOD_REMOVED);

		init(TEXTUAL_CONFLICTING_TOTAL_EVOLUTIONS_ATOMIC);
		init(TEXTUAL_CONFLICTING_TOTAL_EVOLUTIONS_TASK);
		
		init(DIRECT_CONFLICTING_LAYER_BUSINESS);
		init(DIRECT_CONFLICTING_LAYER_DATA_ACCESS);
		init(DIRECT_CONFLICTING_LAYER_WEB);
		init(DIRECT_CONFLICTING_LAYER_ENTITY);
		init(DIRECT_CONFLICTING_LAYER_OTHERS);

		init(INDIRECT_CONFLICTING_LAYER_BUSINESS);
		init(INDIRECT_CONFLICTING_LAYER_DATA_ACCESS);
		init(INDIRECT_CONFLICTING_LAYER_WEB);
		init(INDIRECT_CONFLICTING_LAYER_ENTITY);
		init(INDIRECT_CONFLICTING_LAYER_OTHERS);

		init(TEXTUAL_CONFLICTING_LAYER_BUSINESS);
		init(TEXTUAL_CONFLICTING_LAYER_DATA_ACCESS);
		init(TEXTUAL_CONFLICTING_LAYER_WEB);
		init(TEXTUAL_CONFLICTING_LAYER_ENTITY);
		init(TEXTUAL_CONFLICTING_LAYER_OTHERS);

		init(TEXTUAL_CONFLICTING_TASK_NEW_FUNCIONALITY);
		init(TEXTUAL_CONFLICTING_TASK_BUG_FIX);
		init(TEXTUAL_CONFLICTING_TASK_NEW_USE_CASE);
		
		init(TOTAL_EVOLUTIONS_VARIABILITY_PATTERN);
		init(TOTAL_EVOLUTIONS_VARIABILITY_CONDITIONAL_EXEC);

		init(DIRECT_CONFLICTING_VARIABILITY_PATTERN);
		init(DIRECT_CONFLICTING_VARIABILITY_CONDITIONAL_EXEC);
		
		init(INDIRECT_CONFLICTING_VARIABILITY_PATTERN);
		init(INDIRECT_CONFLICTING_VARIABILITY_CONDITIONAL_EXEC);
		
		init(TEXTUAL_CONFLICTING_VARIABILITY_PATTERN);
		init(TEXTUAL_CONFLICTING_VARIABILITY_CONDITIONAL_EXEC);
		
	}
	
		
	/**
	 * Collection a global statics information
	 * @param changeLogHistory
	 */
	private void collectGlobalStatisics(ChangeLogHistory changeLogHistory ) {
		
		
		System.out.println("Collection Global Statictics");
		
		List<FeatureChangeLog> features = changeLogHistory.getFeatures();

		for (FeatureChangeLog feature : features) {

			List<ChangeLog> changeLogs = feature.getChangelogs();

			collectStatistics(EVOLUTIONS_TOTAL_TASK, changeLogs.size());

			for (ChangeLog changeLog : changeLogs) {
				
				boolean variabilityTaskPattern = false, variabilityTaskExecCond = false, variabilityConflict = false;

				if (changeLog.getType() == ChangeLogType.BUG_FIX) {
					
					collectStatistics(EVOLUTIONS_TASK_BUG_FIX);

					if (changeLog.isDirectlyConflicting())
						collectStatistics(DIRECT_CONFLICTING_TASK_BUG_FIX);

					if (changeLog.isIndirectlyConflicting())
						collectStatistics(INDIRECT_CONFLICTING_TASK_BUG_FIX);

					if (changeLog.isTextualConflicting())
						collectStatistics(TEXTUAL_CONFLICTING_TASK_BUG_FIX);
				} else if (changeLog.getType() == ChangeLogType.NEW_USE_CASE) {
					collectStatistics(EVOLUTIONS_TASK_NEW_USE_CASE);

					if (changeLog.isDirectlyConflicting())
						collectStatistics(DIRECT_CONFLICTING_TASK_NEW_USE_CASE);

					if (changeLog.isIndirectlyConflicting())
						collectStatistics(INDIRECT_CONFLICTING_TASK_NEW_USE_CASE);
					
					if (changeLog.isTextualConflicting())
						collectStatistics(TEXTUAL_CONFLICTING_TASK_NEW_USE_CASE);
				} else {
				
					collectStatistics(EVOLUTIONS_TASK_NEW_FUNCIONALITY);
					
					if (changeLog.isDirectlyConflicting())
						collectStatistics(DIRECT_CONFLICTING_TASK_NEW_FUNCIONALITY);

					if (changeLog.isIndirectlyConflicting())
						collectStatistics(INDIRECT_CONFLICTING_TASK_NEW_FUNCIONALITY);
					
					if (changeLog.isTextualConflicting())
						collectStatistics(TEXTUAL_CONFLICTING_TASK_NEW_FUNCIONALITY);
				}

			/* 	if (changeLog.getType() == ChangeLogType.NEW_FUNCIONALITY ||  changeLog.getType() == ChangeLogType.UPGRADING  ||  changeLog.getType() == ChangeLogType.REFACTORY )  {
					collectStatistics(EVOLUTIONS_TASK_NEW_FUNCIONALITY);
			 */

				

				
				if ( !changeLog.isDirectlyConflicting() && !changeLog.isIndirectlyConflicting() && !changeLog.isTextualConflicting())
					collectStatistics(EVOLUTIONS_TOTAL_TASK_NO_CONFLICT);
				
				for (ClassChangeLog _class : changeLog.getClasses()) {
					
					collectStatistics(EVOLUTIONS_ATOMIC_CLASS_MODIFIED);

					if (_class.isAdded()) {
						
						collectStatistics(EVOLUTIONS_ATOMIC_CLASS_ADDED);

						collectStatistics(EVOLUTIONS_TOTAL_ATOMIC);
						
						if (_class.isDirectlyConflicting())
							collectStatistics(DIRECT_CONFLICTING_ATOMIC_CLASS_ADDED );

						if (_class.isIndirectlyConflicting())
							collectStatistics(INDIRECT_CONFLICTING_ATOMIC_CLASS_ADDED );
					}

					if (_class.isDelete()) {
						collectStatistics(EVOLUTIONS_ATOMIC_CLASS_REMOVED);
						
						collectStatistics(EVOLUTIONS_TOTAL_ATOMIC);

						if (_class.isDirectlyConflicting())
							collectStatistics(DIRECT_CONFLICTING_ATOMIC_CLASS_REMOVED );

						if (_class.isIndirectlyConflicting())
							collectStatistics(INDIRECT_CONFLICTING_ATOMIC_CLASS_REMOVED );

					}

					if (_class.isUpdated()) {
						
						collectStatistics(EVOLUTIONS_ATOMIC_UPDATED_CLASS);

						if (_class.isDirectlyConflicting())
							collectStatistics(DIRECT_CONFLICTING_ATOMIC_CLASS_UPDATED);

						if (_class.isIndirectlyConflicting())
							collectStatistics(INDIRECT_CONFLICTING_ATOMIC_CLASS_UPDATED);
						
						if (_class.isTextualConflicting())
							collectStatistics(TEXTUAL_CONFLICTING_ATOMIC_CLASS_UPDATED);
						
					} 

					if (_class.isTextualConflicting()) {
						collectStatistics(TEXTUAL_CONFLICTING_TOTAL_EVOLUTIONS_ATOMIC);
					}



					for (FieldChangeLog field : _class.getFields()) {

						collectStatistics(EVOLUTIONS_TOTAL_ATOMIC);
						
						if (field.isAdded()) {
							collectStatistics(EVOLUTIONS_ATOMIC_FIELD_ADDED );

							if (field.isDirectlyConflicting())
								collectStatistics(DIRECT_CONFLICTING_ATOMIC_FIELD_ADDED);

							if (field.isIndirectlyConflicting())
								collectStatistics(INDIRECT_CONFLICTING_ATOMIC_FIELD_ADDED);
						}

						if (field.isDelete()) {
							collectStatistics(EVOLUTIONS_ATOMIC_FIELD_REMOVED );

							if (field.isDirectlyConflicting())
								collectStatistics(DIRECT_CONFLICTING_ATOMIC_FIELD_REMOVED);

							if (field.isIndirectlyConflicting())
								collectStatistics(INDIRECT_CONFLICTING_ATOMIC_FIELD_REMOVED);

						}

						if (field.isUpdated()) {
							collectStatistics(EVOLUTIONS_ATOMIC_FIELD_UPDATED );

							if (field.isDirectlyConflicting())
								collectStatistics(DIRECT_CONFLICTING_ATOMIC_FIELD_UPDATED);

							if (field.isIndirectlyConflicting())
								collectStatistics(INDIRECT_CONFLICTING_ATOMIC_FIELD_UPDATED);
						}
						
						if ( field.isIndirectlyConflicting() ) 
							collectStatistics(INDIRECT_CONFLICTING_TOTAL_EVOLUTIONS_ATOMIC);
						
						if ( field.isDirectlyConflicting())
							collectStatistics(DIRECT_CONFLICTING_TOTAL_EVOLUTIONS_ATOMIC);

					}

					for (MethodChangeLog method : _class.getMethods()) {

						collectStatistics(EVOLUTIONS_TOTAL_ATOMIC );
						
						if (method.isAdded()) {
							collectStatistics(EVOLUTIONS_ATOMIC_METHOD_ADDED);

							if (method.isDirectlyConflicting())
								collectStatistics(DIRECT_CONFLICTING_ATOMIC_METHOD_ADDED);

							if (method.isIndirectlyConflicting())
								collectStatistics(INDIRECT_CONFLICTING_ATOMIC_METHOD_ADDED);
						}

						if (method.isDelete()) {
							collectStatistics(EVOLUTIONS_ATOMIC_METHOD_REMOVED);

							if (method.isDirectlyConflicting())
								collectStatistics(DIRECT_CONFLICTING_ATOMIC_METHOD_REMOVED);

							if (method.isIndirectlyConflicting())
								collectStatistics(INDIRECT_CONFLICTING_ATOMIC_METHOD_REMOVED);

						}

						if (method.isUpdated()) {
							collectStatistics(EVOLUTIONS_ATOMIC_METHOD_UPDATED );

							if (method.isDirectlyConflicting())
								collectStatistics(DIRECT_CONFLICTING_ATOMIC_METHOD_UPDATED);

							if (method.isIndirectlyConflicting())
								collectStatistics(INDIRECT_CONFLICTING_ATOMIC_METHOD_UPDATED);
						}

						if (method.getVariationPoint() != null && method.getVariationPoint().isCondExec() ) {
							
							collectStatistics(TOTAL_EVOLUTIONS_VARIABILITY_CONDITIONAL_EXEC);
							variabilityTaskExecCond = true;
							
							if (method.isDirectlyConflicting()) {
								collectStatistics(DIRECT_CONFLICTING_VARIABILITY_CONDITIONAL_EXEC);
								variabilityConflict = true;
							}

							if (method.isIndirectlyConflicting()) {
								collectStatistics(INDIRECT_CONFLICTING_VARIABILITY_CONDITIONAL_EXEC);
								variabilityConflict = true;
							}
							
						}
						
						if (method.getVariationPoint() != null && method.getVariationPoint().isPattern()) {
							collectStatistics(TOTAL_EVOLUTIONS_VARIABILITY_PATTERN);
							variabilityTaskPattern = true;
							
							if (_class.isDirectlyConflicting()) {
								collectStatistics(DIRECT_CONFLICTING_VARIABILITY_PATTERN);
								variabilityConflict = true;
							}
							if (_class.isIndirectlyConflicting()) {
								collectStatistics(INDIRECT_CONFLICTING_VARIABILITY_PATTERN);
								variabilityConflict = true;
							}
						}
						
						if ( method.isIndirectlyConflicting() ) 
							collectStatistics(INDIRECT_CONFLICTING_TOTAL_EVOLUTIONS_ATOMIC);
						
						if ( method.isDirectlyConflicting())
							collectStatistics(DIRECT_CONFLICTING_TOTAL_EVOLUTIONS_ATOMIC);
						
						
					}
					
					if (_class.isDirectlyConflicting()) {
						collectStatistics("DIRECT_CONFLICTING_" + getLayerStatInformation(_class));
					}

					if (_class.isIndirectlyConflicting()) {
						collectStatistics("INDIRECT_CONFLICTING_" + getLayerStatInformation(_class));
					}
					
					if (_class.isTextualConflicting() ) {
						collectStatistics("TEXTUAL_CONFLICTING_" + getLayerStatInformation(_class));
					}
					
					
					
				}
				
				if (changeLog.isDirectlyConflicting()) {
					collectStatistics(DIRECT_CONFLICTING_TOTAL_EVOLUTIONS_TASK);
				}
				
				if (changeLog.isIndirectlyConflicting()) {
					collectStatistics(INDIRECT_CONFLICTING_TOTAL_EVOLUTIONS_TASK);
				
				}
				
				if (changeLog.isTextualConflicting()) {
					collectStatistics(TEXTUAL_CONFLICTING_TOTAL_EVOLUTIONS_TASK);
				
				}
				
				if ( variabilityTaskExecCond ) {
					collectStatistics( EVOLUTIONS_TOTAL_TASK_VARIABIITY_CONDEXEC  );
					if ( variabilityConflict )
						collectStatistics( EVOLUTIONS_TOTAL_TASK_VARIABIITY_CONFLICT  );
					
				}
				
				if ( variabilityTaskPattern) {
					collectStatistics( EVOLUTIONS_TOTAL_TASK_VARIABIITY_PATTERN );
					if ( variabilityConflict )
						collectStatistics( EVOLUTIONS_TOTAL_TASK_VARIABIITY_CONFLICT  );
				}
				
				// Analise por cada tipo de conflito
				if (changeLog.isOnlyDirectConflicting()) {
					collectStatistics(TASK_CONFLICT_ONLY_DIRECT);
				}
				
				if (changeLog.isOnlyIndirectConflicting() ) {
					collectStatistics(TASK_CONFLICT_ONLY_INDIRECT);
				}
				
				if (changeLog.isOnlyTextualConflicting() ) {
					collectStatistics(TASK_CONFLICT_ONLY_TEXTUAL);
				}
				
				if (changeLog.isDirectAndIndirect() ) {
					collectStatistics(TASK_CONFLICT_DIRECT_AND_INDIRECT );
				}
				
				if (changeLog.isDirectAndTextual() ) {
					collectStatistics(TASK_CONFLICT_DIRECT_AND_TEXTUAL );
				}
				
				if (changeLog.isIndirectAndTextual() ) {
					collectStatistics(TASK_CONFLICT_INDIRECT_AND_TEXTUAL );
				}
				
				if (changeLog.isDirectAndIndirectAndTextual() ) {
					collectStatistics(TASK_CONFLICT_INDIRECT_AND_TEXTUAL_DIRECT );
				}
				

			}
		}
		
		
	}
	
	
	/**
	 * Create a task table (CSV File task_[source|target].csv)
	 * @param changeLogHistory
	 * @param fileName
	 */
	
	public void createEvolutionTaskTable(ChangeLogHistory changeLogHistory, List<TaskStatistic> table, MergeSide mergeSide  ) {

		if (changeLogHistory == null)
			return;

		List<FeatureChangeLog> features = changeLogHistory.getFeatures();

		for (FeatureChangeLog feature : features) {
			for (ChangeLog changeLog : feature.getChangelogs()) {
				TaskStatistic taskStatistic = new TaskStatistic(changeLog);
				// ConflictRepository.getInstance().fillTaskSummary(taskStatistic, mergeSide);
				table.add( taskStatistic );
			}
		}
		
		
	}
	
	/**
	 * Create a analitic evolution table (CSV File evolutions.csv)
	 * @param changeLogHistory
	 * @param fileName
	 */
	
	public void createEvolutionTable(ChangeLogHistory changeLogHistory, List<AtomicEvolution> table ) {

		if (changeLogHistory == null)
			return;

		System.out.println("Collection Evolution Table ");
		
		List<FeatureChangeLog> features = changeLogHistory.getFeatures();

		for (FeatureChangeLog feature : features) {
			for (ChangeLog changeLog : feature.getChangelogs()) {
				for (ClassChangeLog _class : changeLog.getClasses()) {
					if ( ! _class.getChangeType().equals(ChangeTypeRepository.UPDATED) ) {
						table.add(  new  AtomicEvolution(_class) );
					} else { 
						for (FieldChangeLog field : _class.getFields()) {
							table.add( new AtomicEvolution( field ) );
						}
						for (MethodChangeLog method : _class.getMethods()) {
							table.add( new AtomicEvolution( method ) );
						}
					}
				}
			}
		}
		
		System.out.println("Evolution Table Done.");
	}

	/*
	 * increment one unit to a specific statistic
	 */
	public void collectStatistics(String type) {
		
		Map<String, Integer> statistic = null;
		
		if ( statisticMode == 'S' ) 
			statistic = statisticSource;
		else 
			statistic = statisticTarget;
		
		
		if (!statistic.containsKey(type))
			statistic.put(type, 1);
		else
			statistic.put(type, (statistic.get(type) + 1));
	}

	
	public void init(String type) {
		statisticSource.put(type, 0);
		statisticTarget.put(type, 0);
	}
	
	/*
	 * increment several units to a specific statistic
	 */
	public void collectStatistics(String type, int qtd) {

		Map<String, Integer> statistic = null;
		
		if ( statisticMode == 'S' ) 
			statistic = statisticSource;
		else 
			statistic = statisticTarget;
		
		if (!statistic.containsKey(type))
			statistic.put(type, qtd);
		else
			statistic.put(type, (statistic.get(type) + qtd));
	}

	
	/**
	 * Analize conflicts counting if occurs in core or variabilities.
	 * 
	 * @param changeLogHistory
	 */
	public void analizeLPSConflicts(ChangeLogHistory changeLogHistory) {

		System.out.println(">> Analizing Variabilities.. Please Wait...");
		
		int totalMethod = 0, totalPattern = 0;
		List<FeatureChangeLog> features = changeLogHistory.getFeatures();

		for (FeatureChangeLog feature : features) {
			List<ChangeLog> changeLogs = feature.getChangelogs();

			for (ChangeLog changeLog : changeLogs) {
				String system = changeLogHistory.getSystem();
				for (ClassChangeLog classChangLog : changeLog.getClasses()) {
					try {
						Variability vp = VariabilityRepository.getInstance().findVariabilityInClass(system, classChangLog);
						if (vp != null) {
							classChangLog.setVariationPoint(vp);
							totalPattern++;
							for ( FieldChangeLog fieldSource : classChangLog.getFields() ) {
								fieldSource.setVariationPoint(vp);
							}
							for ( MethodChangeLog methodSource : classChangLog.getMethods() ) {
								methodSource.setVariationPoint(vp);
							}
						}
						// find condExec inside methods
						for (MethodChangeLog methodSource : classChangLog.getMethods()) {
							Variability vpExec = VariabilityRepository.getInstance().findVariabilityInMethod(system, methodSource);
							if (vpExec != null) {
								methodSource.setVariationPoint(vpExec);
								totalMethod++;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		System.out.println("Analizing LPS Variability Evolutions - Done. Method(" + totalMethod + ") Pattern(" + totalPattern + ")" );

	}
	
	/**
	 * 
	 * @see br.ufrn.spl.ev.engines.conflicts.statistics.ChangeLogHistoryStatistics#saveStatisticsCollected(java.lang.String)
	 */
	private void saveStatisticsCollected(String filePath) {

		Properties propertiesSource = new Properties();
		Properties propertiesTarget = new Properties();
		
		FileOutputStream fos = null;
		FileOutputStream fos2 = null;
		BufferedWriter writerSource = null;
		BufferedWriter writerTarget = null;
		BufferedWriter writerTaskSource = null, writerTaskTarget = null;
		
		try {

			for (String type : statisticSource.keySet()) {
				propertiesSource.setProperty(type, String.valueOf(statisticSource.get(type)));
			}

			fos = new FileOutputStream(filePath + "/statistic_source.properties");
			propertiesSource.store(fos, "EVOLUTION STATISTICS PROPERTIES SOURCE");
			fos.close();
			
			// generate target statistic
			for (String type : statisticTarget.keySet()) {
				propertiesTarget.setProperty(type, String.valueOf(statisticTarget.get(type)));
			}

			fos2 = new FileOutputStream(filePath + "/statistic_target.properties");
			propertiesTarget.store(fos2, "EVOLUTION STATISTICS PROPERTIES TARGET");
			fos2.close();
			
			writerSource=  new BufferedWriter(new FileWriter(filePath + "/evolutions_source.csv"));
			writerTarget =  new BufferedWriter(new FileWriter(filePath + "/evolutions_target.csv"));
			writerTaskSource =  new BufferedWriter(new FileWriter(filePath + "/task_source.csv"));
			writerTaskTarget =  new BufferedWriter(new FileWriter(filePath + "/task_target.csv"));
			
			writerSource.write( AtomicEvolution.getColumns()  );
			for ( AtomicEvolution row : tableSource  ) {
				writerSource.write ( row.getCSV() );
			}
			
			writerTarget.write( AtomicEvolution.getColumns()  );
			for ( AtomicEvolution row : tableTarget  ) {
				writerTarget.write ( row.getCSV() );
			}
			
			writerTaskSource.write( TaskStatistic.getColumns()  );
			for ( TaskStatistic row : tableTaskSource  ) {
				writerTaskSource.write ( row.getCSV() );
			}
			
			writerTaskTarget.write( TaskStatistic.getColumns()  );
			for ( TaskStatistic row : tableTaskTarget  ) {
				writerTaskTarget.write ( row.getCSV() );
			}
			
			
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		} finally {
			try {
				fos.close();
				writerSource.close();
				writerTarget.close();
				writerTaskSource.close();
				writerTaskTarget.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Extract software layer from package name pattern
	 * 
	 * @param ccl
	 * @return
	 */
	public String getLayerStatInformation(ClassChangeLog ccl) {

		if (ccl.getAbsolutePath().contains("negocio"))
			return "LAYER_BUSINESS";
		else if (ccl.getAbsolutePath().contains("jsf") || ccl.getAbsolutePath().contains("web"))
			return "LAYER_WEB";
		else if (ccl.getAbsolutePath().contains("dao"))
			return "LAYER_DATA_ACCESS";
		else if (ccl.getAbsolutePath().contains("dominio"))
			return "LAYER_ENTITY";
		else
			return "LAYER_OTHERS";

	}
	
}
