/**
 * 
 */
package br.ufrn.spl.ev.engines.conflicts.statistics;

import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;

/**
 * Interface to implement different methods of analyze conflict statistic
 * @author jadson - jadsonjs@gmail.com
 *
 */
public interface ConflictStatistics {
	
	/** Quantitative information about the conflicts */
	public void collectStatistics(ConflictModel conflictModelSource, String filePath);

}
