/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
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
