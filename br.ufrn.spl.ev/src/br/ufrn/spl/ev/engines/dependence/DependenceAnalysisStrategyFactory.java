/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.dependence;


/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class DependenceAnalysisStrategyFactory {
	
	/**
	 * Create and return the Direct conflict strategy
	 * 
	 * @param analysisType @see {@link ANALYSIS_TYPE}
	 */
	public static DependenceAnalysisStrategy getDependenceStrategy() {
		return new DependenceBetweenOrderedChangeLogs();
	}

}
