/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev.models.conflictmodel;

/**
 * Sub classification of conflicts
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public enum ConflictSubType {

	/**
	 * Indirect conflict that is detected because the asset is present on the target ChangelogHistory
	 */
	INDIRECT_BY_EVOLUTION, 
	
	/**
	 * Indirect conflict that is detected because the asset is related with the source asset even though the asset on the target does not evolves
	 */
	INDIRECT_BY_RELATIONSHIP
}
