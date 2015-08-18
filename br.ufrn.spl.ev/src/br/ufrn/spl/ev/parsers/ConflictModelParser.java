/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
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
