/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.parsers;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class DependenceModelParserFactory {

	
	public static DependenceModelParser getDependenceModelParser(){
		return new DependenceModelXStreamParser();
	}
	
}
