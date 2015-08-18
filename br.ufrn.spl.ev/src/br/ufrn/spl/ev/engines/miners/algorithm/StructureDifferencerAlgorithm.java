/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.algorithm;

import japa.parser.ast.CompilationUnit;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;

/**
 * <p>The algorithm use to get the differences between to versions of the same class.</p>
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public interface StructureDifferencerAlgorithm {
	
	/**
	 * <p>This method generate the differences between two version of the same class in the repository. 
	 * The actual version with the previous version of the class.
	 * </p>
	 *
	 * <p> This method fills out the ClassChangeLog model with fine informations about method, fields, inheritance, etc that was evolved between the
	 * version analyzed.</p>
	 * 
	 * @param featureChangeLog
	 * @param changeLog
	 * @param clazz
	 */
	public void retrieveDifferencesInClass(CompilationUnit actualCompilationUnit, CompilationUnit previousCompilationUnit, FeatureChangeLog featureChangeLog, ChangeLog changeLog, ClassChangeLog clazz);
	
	
}
