/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev.parsers;

import java.io.File;

import br.ufrn.spl.ev.models.dependencemodel.DependenceModel;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public interface DependenceModelParser {
	
	/**
	 * Convert the information of change log from file to objects
	 * 
	 * @param changeLogFile
	 * @return
	 */
	public DependenceModel readDependenceFile(File dependenceFile);
	
	
	/**
	 * Convert the information of change log from objects to a file
	 * 
	 * @param changeLogHistory
	 * @param changeLogFile
	 */
	public void writeDependenceFile(DependenceModel dependence, File dependenceFile);

}
