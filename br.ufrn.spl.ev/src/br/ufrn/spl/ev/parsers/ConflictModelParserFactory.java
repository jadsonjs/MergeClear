/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev.parsers;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class ConflictModelParserFactory {

	public static ConflictModelParser getConflictModelParser(){
		return new ConflictModelXStreamParser();
		//return new ConflictModelJAXBParser();
	}
}
