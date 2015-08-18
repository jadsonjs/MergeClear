/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.dependence;

import java.io.File;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.dependencemodel.DependenceModel;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public interface DependenceAnalysisStrategy {

	/** Should implement the algorithm of direct conflicts detection */
	public DependenceModel buildMapOfDependences(ChangeLogHistory historyChangeLog, File fileDependence);
	
}
