/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.exceptions;


/**
 * A exception to the part of indirect conflict detection
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class IndirectConflictsDetectionException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2531577642452985923L;

	public IndirectConflictsDetectionException(String message){
		super(message);
	}
	
}
