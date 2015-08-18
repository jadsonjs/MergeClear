/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.indirect;

import br.ufrn.spl.ev.gui.swt.ShowEvolutionsUI;


/**
 * <p>Instantiates the indirect analysis strategies supported by our approach.</p>
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class IndirectConflictsAnalysisStrategyFactory {

	/** The types of indirect analysis supported */
	public enum INDIRECT_ANALYSIS_TYPE{JDT, WALA};
	
	
	/**
	 * Create and return the indirect conflict strategy
	 * 
	 * @param analysisType @see {@link ANALYSIS_TYPE}
	 */
	public static IndirectConflictsAnalysisStrategy getIndirectAnalysisStrategy(ShowEvolutionsUI showEvolutionsUI, INDIRECT_ANALYSIS_TYPE analysisType ) {
		
		if(analysisType == INDIRECT_ANALYSIS_TYPE.JDT)
			return new IndirectConflictsJDTAnalysisStrategy(showEvolutionsUI);
		if(analysisType == INDIRECT_ANALYSIS_TYPE.WALA)
			return new IndirectConflictsWALAAnalysisStrategy();
		
		
		throw new IllegalArgumentException("Is the a valid indirect conflicts strategy ");
	}

}
