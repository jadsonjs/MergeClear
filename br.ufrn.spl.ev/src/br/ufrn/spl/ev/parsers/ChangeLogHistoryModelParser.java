/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev.parsers;

import java.io.File;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;

/**
 * This interface define the parser methods to story change log model.
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public interface ChangeLogHistoryModelParser {
	
	/**
	 * Convert the information of change log from file to objects
	 * 
	 * @param changeLogFile
	 * @return
	 */
	public ChangeLogHistory readHistoryChangeLogFile(File changeLogFile);
	
	
	/**
	 * Convert the information of change log from objects to a file
	 * 
	 * @param changeLogHistory
	 * @param changeLogFile
	 */
	public void writeHistoryChangeLogFile(ChangeLogHistory changeLogHistory, File changeLogFile);

}
