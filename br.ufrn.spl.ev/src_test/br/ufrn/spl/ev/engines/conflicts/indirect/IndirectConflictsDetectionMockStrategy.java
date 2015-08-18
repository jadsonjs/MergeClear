/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.indirect;

import org.eclipse.core.runtime.IProgressMonitor;

import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.exceptions.IndirectConflictsDetectionException;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;

/**
 *  <p>A mock object to the de indirect conflict detection<p>
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 15/10/2013
 *
 */
public class IndirectConflictsDetectionMockStrategy  extends IndirectConflictsDetection{

	
	public IndirectConflictsDetectionMockStrategy(IProgressMonitor monitor, IndirectConflictsAnalysisStrategy analysisStrategy, RepositoryConnector repositoryConnectorSource, RepositoryConnector repositoryConnectorTarget) {
		//super(monitor, analysisStrategy, repositoryConnectorSource, repositoryConnectorTarget);
	}


	/**
	 * 
	 * @see br.ufrn.spl.ev.engines.conflicts.indirect.IndirectConflictsEvolutionDetection#loadData(br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory, br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory, java.lang.String, java.lang.String)
	 */
	@Override
	protected void loadProjectsData(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget, ConflictModel conflictModel, RepositoryConnector sourceConnector, RepositoryConnector targetConnector) {
		this.changeLogHistorySource = changeLogHistorySource;
		this.changeLogHistoryTarget = changeLogHistoryTarget;
		this.conflictModel = conflictModel;
		
	}


	@Override
	public void checkIndirectConflicts(ChangeLogHistory changeLogHistorySource,
			ChangeLogHistory changeLogHistoryTarget,
			ConflictModel conflictModel, short depthAnalysisLevel)
			throws IndirectConflictsDetectionException {
		
	}
	

//	/**
//	 * Mock implementation: Doesn't find the real dependence on some project, makes the asset dependences for "ABC" example (a very simple example for tests) 
//	 * 
//	 * @see br.ufrn.spl.ev.engines.conflicts.indirect.IndirectConflictsEvolutionDetection#getDependenceOfAsset(br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog)
//	 */
//	@Override
//	protected List<AssetChangeLog> getAssetsInTheCallGraphic(IJavaProject javaProject, AssetChangeLog assetChangeLog, List<AssetChangeLog> setOfOtherAssets, 
//			Map<AssetChangeLog,  List<AssetChangeLog>> cacheCallGraphic){
//		
//		List<AssetChangeLog> depedences = new ArrayList<AssetChangeLog>();
//		
//		/* ***********************************************************************
//		 *  Factory of dependences for test
//		 *  The more important here is the signature of assets, for we can compare them.
//		 * ***********************************************************************/
//		
//		/*
//		 * b2 that has not direct conflict depend of "c2" that change on target too, so how "b2" has indirect conflict
//		 * 
//		 * @see{IprojectMinerMock}
//		 */
//		if(assetChangeLog.getName().equals("b2") ){
//			MethodChangeLog method = new MethodChangeLog("c2", "int#c2()", null);
//			method.setClassChangeLog(new ClassChangeLog("C.java", "Target/src/exemple/C.java", "src/exemple/C.java", null, null));
//			depedences.add( method );
//		}
//		
//		/*
//		 * d1 that has not direct conflict depend of "notExistMethod" that is not present on target, so how "d1" has no indirect conflict
//		 * 
//		 * @see{IprojectMinerMock}
//		 */
//		if(assetChangeLog.getName().equals("d1") ){
//			MethodChangeLog method = new MethodChangeLog("notExistMethod", "void#notExistMethod()", null);
//			method.setClassChangeLog(new ClassChangeLog("NotExistClass.java", "Target/src/exemple/NotExistClass.java", "src/exemple/NotExistClass.java", null, null));
//			depedences.add( method );
//		}
//		
//		return depedences;
//		
//	}


	
}
