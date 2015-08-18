/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev.parsers;

import java.io.File;

import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public interface ConflictModelParser {
	
	
	/**
	 * Convert the information of conflict model from file to objects
	 * 
	 * @param changeLogFile
	 * @return
	 */
	public ConflictModel readConflictModelFile(File xmlConflictModelFile);
	
	
	/**
	 * Convert the information of conflict model  from objects to file
	 * 
	 * @param changeLogHistory
	 * @param changeLogFile
	 */
	public void writeConflictModelFile(ConflictModel conflictModel, File xmlConflictModelFile);

}
