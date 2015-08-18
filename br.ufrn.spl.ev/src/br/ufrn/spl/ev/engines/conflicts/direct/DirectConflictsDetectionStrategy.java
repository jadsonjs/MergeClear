/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.direct;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;


/**
 * <p>Interface that contains the operation of detect direct conflicts </p>
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 04/11/2013
 *
 */
public interface DirectConflictsDetectionStrategy {

	/** Should implement the algorithm of direct conflicts detection */
	public ConflictModel checkDirectConflicts(ChangeLogHistory historyChangeLogSource, ChangeLogHistory historyChangeLogTarge, ConflictModel conflictModel);
}
