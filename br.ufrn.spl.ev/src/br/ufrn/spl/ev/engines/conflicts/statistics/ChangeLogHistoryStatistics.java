/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.statistics;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;

/**
 * <p>Define the operations to collect statistics of conflicts in the tool</p>
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 04/11/2013
 *
 */
public interface ChangeLogHistoryStatistics {

	/** Register a statistic of evolution from the Change Log History */
	public void collectStatistics(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogTarget, String filePath);
	
}
