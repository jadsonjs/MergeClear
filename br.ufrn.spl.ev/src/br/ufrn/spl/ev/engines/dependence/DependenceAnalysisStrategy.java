/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
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
