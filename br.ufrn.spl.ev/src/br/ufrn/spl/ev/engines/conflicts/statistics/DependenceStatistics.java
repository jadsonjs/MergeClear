/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.statistics;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.dependencemodel.DependenceModel;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public interface DependenceStatistics {

	
	public void collectStatistics(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget, 
			DependenceModel sourceDependenceMap, DependenceModel targetDependenceMap, String filePath);

}
