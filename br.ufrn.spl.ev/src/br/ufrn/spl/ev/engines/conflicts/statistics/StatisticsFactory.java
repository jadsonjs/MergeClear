/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.statistics;

import br.ufrn.spl.ev.PluginConstants;
import br.ufrn.spl.ev.util.ConfigUtils;


/**
 * Factory to collect statistics strategies
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 07/11/2013
 *
 */
public class StatisticsFactory {

	/**
	 * Create and return the Direct conflict strategy
	 * 
	 * @param analysisType @see {@link ANALYSIS_TYPE}
	 */
	public ChangeLogHistoryStatistics getStatisticsAboutEvolution() {
		if("COMPLETE".equals( ConfigUtils.getConfiguration(PluginConstants.STATISTIC_ANALYSIS_TYPE) ) )
			return new CollectCompleteStatistic();
		if("SIMPLE".equals( ConfigUtils.getConfiguration(PluginConstants.STATISTIC_ANALYSIS_TYPE) ) )
			return new CollectSimpleStatistic();
		
		return null; // if the config.propertis file is correct, never will return null
	}
	
	
	public DependenceStatistics getStatisticsAboutDependence() {
		return new DependenceStatisticsBetweenTasks();
	}
	
	/** 
	 * Raise statistics about conflicts  in the conflict model 
	 */
	public ConflictStatistics getStatisticsAboutConflicts() {
		return new DefaultConflictStatistics();
	}
}
