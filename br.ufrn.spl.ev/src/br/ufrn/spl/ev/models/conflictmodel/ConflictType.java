package br.ufrn.spl.ev.models.conflictmodel;

/**
 * Types Conflicts the the tool can detect.
 * 
 * @author Jadson - jadson - jadson@info.ufrn.br
 *
 */
public enum ConflictType {

	/**
	 * The the same artefact was changed in the both side of the evolution (source and target), in other words 
	 * the same artefact appears in the source changeloghistory and target changeloghistory.
	 */
	DIRECT, 

	/**
	 * When the artefact change in the source side and does not change in the target side, 
	 * by someone that is in the call graphic (someone that it call or call it. ) of this artefact change.
	 */
	INDIRECT, 

	
	/**
	 * The the a class was changed in the both side (source and target), but has not direct or indirect conflict.
	 */
	PSEUDO;
}
