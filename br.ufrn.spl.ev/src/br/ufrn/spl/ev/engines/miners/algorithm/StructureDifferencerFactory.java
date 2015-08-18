/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.algorithm;

/**
 * Create and return the algorithm used to detect evolution on the source code.
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class StructureDifferencerFactory {

	/**
	 * <p>Choose in this method the type o algorithm we you like to use, to analyze source code evolution.</p>
	 * 
	 * @return
	 */
	public static StructureDifferencerAlgorithm getStructureDifferencer(){
		return new DefaultStructureDifferencer(); // to change the type to DefaultStructureDifferencer
		//return new ChangeDistillerStructureDifferencer(); // to change the type to ChangeDistillerStructureDifferencer
	}
}
