/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.models.changeloghistorymodel;

/**
 * <p>Indicates the type of change that happen.</p>
 *
 * <p>Not all changes area applicable for all assets, some are just to methods, other just to class, and so on.</p>
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 12/12/2013
 *
 */
public enum ChangeLocation {

	/** If the name of a method change*/                                         NAME,
	/** If the body of a method change*/                                         BODY,
	/** If the parameters of a method change*/                                   PARAMETERS,
	/** If the modifiers of a method change*/                                    MODIFIERS,
	/** If the annotation of a method or class or field change*/                 ANNOTATIONS,
	/** If the exception of a method change*/                                    EXCEPTION,
	/** If the inheritance of a class change*/                                   INHERITANCE;
	
}
