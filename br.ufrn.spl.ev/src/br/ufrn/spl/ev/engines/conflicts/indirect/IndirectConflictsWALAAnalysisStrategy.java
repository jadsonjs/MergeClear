/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.indirect;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IJavaProject;

import br.ufrn.spl.ev.exceptions.IndirectConflictsDetectionException;
import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;


/**
 * <p>Implements the analysis of conflict with WALA. </p>
 * 
 * <p> <i>The T. J. Watson Libraries for Analysis (WALA) provide static analysis 
 * capabilities for Java bytecode and related languages and for JavaScript. </i> </p>
 * 
 * {@link http://wala.sourceforge.net}
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class IndirectConflictsWALAAnalysisStrategy implements IndirectConflictsAnalysisStrategy{

	/** return the dependence of some asset using the specific technology defined by who extends this class */
	public List<AssetChangeLog> getReferencesInTheCallGraphic(IJavaProject javaProject, AssetChangeLog assetChangeLog, 
			List<AssetChangeLog> setOfOtherAssets, Map<AssetChangeLog,  List<AssetChangeLog>> cacheCallGraphic) throws IndirectConflictsDetectionException{
		return null;
	}
	
	
	/** return the dependence of some asset using the specific technology defined by who extends this class */
	public List<AssetChangeLog> getDependenceInTheCallGraphic(IJavaProject javaProject, AssetChangeLog assetChangeLog, 
			List<AssetChangeLog> setOfOtherAssets, Map<AssetChangeLog,  List<AssetChangeLog>> cacheCallGraphic) throws IndirectConflictsDetectionException{
		return null;
	}
	

}
