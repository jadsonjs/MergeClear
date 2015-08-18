/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.direct;


/**
 * <p>Instantiate the direct conflitct strategy<>
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 04/11/2013
 *
 */
public class DirectConflictsDetectionStrategyFactory {

	
	/**
	 * Create and return the Direct conflict strategy
	 * 
	 * @param analysisType @see {@link ANALYSIS_TYPE}
	 */
	public static DirectConflictsDetectionStrategy getDirectConflictsStrategy() {
		return new DirectConflictsDetectionInChangeLogHistoryStrategy();
	}
}
