/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.indirect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;

import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.engines.connectors.SystemConnector;
import br.ufrn.spl.ev.exceptions.IndirectConflictsDetectionException;
import br.ufrn.spl.ev.gui.swt.ShowEvolutionsUI;
import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;
import br.ufrn.spl.ev.util.ChangeLogHistoryUtil;
import br.ufrn.spl.ev.util.GUIUtils;

/**
 * <p>The interface of indirect conflicts detection strategy </p>
 * 
 * <p>This class contains the generic algorithm of the indirect dependence. 
 *    It need to be implemented by someone that use a specific technology to find the dependence of assets, like JDT technology or WALA Framework
 * </p>
 * 
 * <p>  
 * <pre>
 *       Indirect Conflicts Detection Algorithm:
 * 
 *       A.2.2 If a "X" asset weren't modified in the target (if it is not on Target HistoryChangeLog )
 *
 *       A.2.2.1 Calculate the dependence of the "X" (assets that call or are called by "X" ) recursively (depth level 1, depth level 2, depth level 3, ...)
 *                   and verify if this elements are in the Target HistoryChangeLog, 
 *                           if true, is a indirect conflict
 *                           if not, is not a indirect conflict, of course.
 *                   
 * 
 * </pre>
 * </p>
 * 
 * @author jadson - jadson@info.ufrn.br
 * @author Gleydson - gleydson@esig.com.br
 * 
 * @version 1.0 - Class Creation
 * @version 2.0 - jadson - jadson@info.ufrn.br -  analyzing the complete full graphic, assets call and called
 */
public class IndirectConflictsEvolutionDetection extends IndirectConflictsDetection{
	
	
	public IndirectConflictsEvolutionDetection(ShowEvolutionsUI evolutionUI, IProgressMonitor monitor, IndirectConflictsAnalysisStrategy analysisStrategy, 
			SystemConnector systemConnectorSource, SystemConnector systemConnectorTarget, 
			RepositoryConnector repositoryConnectorSource, RepositoryConnector repositoryConnectorTarget){
		
		this.monitor = monitor;
		this.systemConnectorSource = systemConnectorTarget;
		this.systemConnectorTarget = systemConnectorTarget;
		this.repositoryConnectorSource = repositoryConnectorSource;
		this.repositoryConnectorTarget = repositoryConnectorTarget;
		this.analysisStrategy = analysisStrategy;
		this.evolutionUI = evolutionUI;
	}
	
	
	/**
	 * <p>Receives a list of assets selected by the user, analyzes the indirect conflicts and return the model of conflict.</p>
	 * 
	 * <p>This is a <b>Template Method pattern</b> with the algorithm of check indirect conflict</p>
	 * 
	 * @param selectedAssets
	 * @return
	 * @throws Exception 
	 */
	public final void checkIndirectConflicts(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget,  ConflictModel conflictModel, short depthAnalysisLevel) throws IndirectConflictsDetectionException{
		
		if(depthAnalysisLevel < 1 || depthAnalysisLevel > 10){
			throw new IndirectConflictsDetectionException("The depth analysis Level need to be between 1 and 10");
		}
		
		this.depthAnalysisLevel = depthAnalysisLevel;
		
		loadProjectsData(changeLogHistorySource, changeLogHistoryTarget, conflictModel, repositoryConnectorSource, repositoryConnectorTarget);
		
		performAnalysis(); // who call this artefact
		
	}
	
	
	

	/**
	 * This method contains the algorithm to verify the indirect conflicting of who is calling the actual artefact
	 * 
	 * <p> Created at:  16/10/2013  </p>
	 *
	 */
	private final void performAnalysis() throws IndirectConflictsDetectionException{
		
		long time = System.currentTimeMillis();
		
	
		cacheCallGraphic = new TreeMap<AssetChangeLog, List<AssetChangeLog>>(); // clean the cache because we will realize a new  analysis
	
		int indirectConflictCount = 0;
		
		//////////       Keep the artefact on the target side   ////////
		
		Map<ClassChangeLog,  List<ChangeLog>> hashOfClassesInTargetChangeLog = new TreeMap<ClassChangeLog, List<ChangeLog>>();
		Map<MethodChangeLog, List<ChangeLog>> hashOfMethodsInTargetChangeLog = new TreeMap<MethodChangeLog, List<ChangeLog>>();
		Map<FieldChangeLog,  List<ChangeLog>> hashOfFieldsInTargetChangeLog  = new TreeMap<FieldChangeLog, List<ChangeLog>>();
		//Map<CodePieceChangeLog, List<ChangeLog>> hashOfCodePieceTarget = new TreeMap<CodePieceChangeLog, List<ChangeLog>>();
		
		ChangeLogHistoryUtil.genarateHashOfAssetsHistoryChangeLog(changeLogHistoryTarget, hashOfClassesInTargetChangeLog, hashOfMethodsInTargetChangeLog, hashOfFieldsInTargetChangeLog );
		
		// Is the changes that the user wants to apply
		List<AssetChangeLog> selectedAssetOnSource = ChangeLogHistoryUtil.getSelectedSourceCodeAssets(changeLogHistorySource);
		
		List<AssetChangeLog> allTargetAsset = ChangeLogHistoryUtil.getAllSourceCodeAssets(changeLogHistoryTarget);
		
		
		if(selectedAssetOnSource.size() == 0){
			GUIUtils.addInformationMessage("No assets were selected to make the indirect analysis. Please select the changes you want to apply!");
			return;
		}
		
		int qtdTotal = selectedAssetOnSource.size();
		int count = 1;
		int percente  = qtdTotal/100; // show the results for each 1%
		
		if(monitor != null) monitor.worked(0);
		
		// For each asset (class, method or field)  find it reference 
		for (AssetChangeLog assetSelectedOnSource : selectedAssetOnSource) {
			
			if( ! assetSelectedOnSource.isDirectlyConflicting() ){ // is is already in direct, we don't need analyze
				
				//System.out.println("analisando dependencias de "+assetSelectedOnSource);
				
				if(count % ( percente > 0  ? percente : 1) == 0) 
					System.out.println(" >>> Analizing Indirect Conflicts Reference "+count+" of "+qtdTotal+"  "+(count*100/qtdTotal)+"% with " + indirectConflictCount + " found. "+"Spent: "+( (System.currentTimeMillis()-time)/1000/60)+" minutes");
				
				if(monitor != null) monitor.worked((count*100/qtdTotal));
				
				/* Just find the asset in the call graphic of asset !!!!! ON TARGET !!!!!
				 *  
				 * OBS.: we have to pass the target project here 
				 */
				List<AssetChangeLog> referencesOnSourceInTarget = new ArrayList<AssetChangeLog>();
				
				
				permformIndirectCallGraphicAnalysisReference(javaTargetProject, assetSelectedOnSource, allTargetAsset, referencesOnSourceInTarget, 1, depthAnalysisLevel); // on target !!!
				
				
				indirectConflictCount = verifyDependenceInTargetHistoryChangeLog(
						indirectConflictCount, hashOfClassesInTargetChangeLog,
						hashOfMethodsInTargetChangeLog,
						hashOfFieldsInTargetChangeLog, assetSelectedOnSource,
						referencesOnSourceInTarget);
			}
			
			count++;
		}
			
		count = 1;
					
		// For each asset (class, method or field)  find it dependences 
		for (AssetChangeLog assetSelectedOnSource : selectedAssetOnSource) {
				
			if( ! assetSelectedOnSource.isDirectlyConflicting() ){ // is is already in direct, we don't need analyze	
		
			
				if(count % ( percente > 0  ? percente : 1) == 0) 
					System.out.println(" >>> Analizing Indirect Conflicts Dependence "+count+" of "+qtdTotal+"  "+(count*100/qtdTotal)+"% with " + indirectConflictCount + " found. "+"Spent: "+( (System.currentTimeMillis()-time)/1000/60)+" minutes");
				
			
				/* Just find the asset in the call graphic of asset !!!!! ON TARGET !!!!!
				 *  
				 * OBS.: we have to pass the target project here 
				 */
				List<AssetChangeLog> dependenceOnSourceInTarget = new ArrayList<AssetChangeLog>();
				
				
				permformIndirectCallGraphicAnalysisDependence(javaTargetProject, assetSelectedOnSource, allTargetAsset, dependenceOnSourceInTarget, 1, depthAnalysisLevel); // on target !!!
				
				
				indirectConflictCount = verifyDependenceInTargetHistoryChangeLog(
						indirectConflictCount, hashOfClassesInTargetChangeLog,
						hashOfMethodsInTargetChangeLog,
						hashOfFieldsInTargetChangeLog, assetSelectedOnSource,
						dependenceOnSourceInTarget);
			}
		
			count++;
		
		}
			
	}

	/**
	 * This is a very important method of the indirect conflict evolution strategy.
	 * 
	 * It is only a indirect conflict if and only if, the assert is present on target history change log.
	 * 
	 * Verify if the dependences are in the target change log, if true so we have a indirect conflict
	 */
	private int verifyDependenceInTargetHistoryChangeLog(
			int indirectConflictCount,
			Map<ClassChangeLog, List<ChangeLog>> hashOfClassesInTargetChangeLog,
			Map<MethodChangeLog, List<ChangeLog>> hashOfMethodsInTargetChangeLog,
			Map<FieldChangeLog, List<ChangeLog>> hashOfFieldsInTargetChangeLog,
			AssetChangeLog assetSelectedOnSource,
			List<AssetChangeLog> referencesOnSourceInTarget) {
	
		
		for (AssetChangeLog assetIndirectDependeceOnTarget : referencesOnSourceInTarget) {
			
			AssetChangeLog assetTarget = changeLogHistoryContainsAsset (assetIndirectDependeceOnTarget, hashOfClassesInTargetChangeLog, hashOfMethodsInTargetChangeLog, hashOfFieldsInTargetChangeLog );
			
			if( assetTarget != null ){
				
				// set the artefact as conflicting
				assetSelectedOnSource.setHierarchicalIndirectConflict(true, assetIndirectDependeceOnTarget.getDepthLevel());
				
				// add how is conflicts in the artefact
				assetSelectedOnSource.addIndirectConflict(assetIndirectDependeceOnTarget);
				
				
				// set the artefact in the as conflicting
				assetTarget.setHierarchicalIndirectConflict(true, assetIndirectDependeceOnTarget.getDepthLevel());
				
				indirectConflictCount++;
				
				// generate the conflict model ///
				conflictModel.addIndirectConflictByEvolution(assetSelectedOnSource, assetTarget );
				
			}else{
				//System.out.println("nao contain referecente on history ");
			}
		}
		return indirectConflictCount;
	}
	
	
	
	/** 
	 * <p>Recursive Algorithm to analyze the dependence of the asset</p>
	 * 
	 * <p> <strong> Can have a exponential asymptotic time O(x^n), so take care to optimize this method!!! </strong> </p>
	 */
	private final void permformIndirectCallGraphicAnalysisReference(IJavaProject javaProject, AssetChangeLog assetChangeLogSource,  List<AssetChangeLog> allTargetAssets, List<AssetChangeLog> referencesOnSourceInTarget,  int actualLevel, final int depthLevel) throws IndirectConflictsDetectionException{
		
		if(assetChangeLogSource == null || actualLevel > depthLevel) // we arrive in the end of analysis
			return;
		
		// ALL asset less the actual
		List<AssetChangeLog> setOfOtherAssets = new ArrayList<AssetChangeLog>(allTargetAssets);
		setOfOtherAssets.remove(assetChangeLogSource);
		
		// dependence find in the this level
		List<AssetChangeLog> listOfReferencesCallGraphicOnTarget = analysisStrategy.getReferencesInTheCallGraphic(javaProject, assetChangeLogSource, setOfOtherAssets, cacheCallGraphic);
		
		// for each asset dependence:  call the dependence of dependence
		for (AssetChangeLog assetDepedenceOnTarget : listOfReferencesCallGraphicOnTarget) {
			
			if( ! referencesOnSourceInTarget.contains(assetDepedenceOnTarget) ){ // the first optimization, if we have already passed for a asset, don't call it again
				
				assetDepedenceOnTarget.setDepthLevel(actualLevel);
				referencesOnSourceInTarget.add(assetDepedenceOnTarget); // is a dependence
				permformIndirectCallGraphicAnalysisReference(javaProject, assetDepedenceOnTarget, allTargetAssets, referencesOnSourceInTarget, actualLevel+1, depthLevel); // recursively to next level
			}
		}
	}
	
	
	
	/** 
	 * <p>Recursive Algorithm to analyze the dependence of the asset</p>
	 * 
	 * <p> <strong> Can have a exponential asymptotic time O(x^n), so take care to optimize this method!!! </strong> </p>
	 */
	private final void permformIndirectCallGraphicAnalysisDependence(IJavaProject javaProject, AssetChangeLog assetChangeLogSource,  List<AssetChangeLog> allTargetAssets, List<AssetChangeLog> referencesOnSourceInTarget,  int actualLevel, final int depthLevel) throws IndirectConflictsDetectionException{
		
		if(assetChangeLogSource == null || actualLevel > depthLevel) // we arrive in the end of analysis
			return;
		
		// ALL asset less the actual
		List<AssetChangeLog> setOfOtherAssets = new ArrayList<AssetChangeLog>(allTargetAssets);
		setOfOtherAssets.remove(assetChangeLogSource);
		
		// dependence find in the this level
		List<AssetChangeLog> listOfDependenceCallGraphicOnTarget = analysisStrategy.getDependenceInTheCallGraphic(javaProject, assetChangeLogSource, setOfOtherAssets, cacheCallGraphic);
		
		// for each asset dependence:  call the dependence of dependence
		for (AssetChangeLog assetDepedenceOnTarget : listOfDependenceCallGraphicOnTarget) {
			
			if( ! referencesOnSourceInTarget.contains(assetDepedenceOnTarget) ){ // the first optimization, if we have already passed for a asset, don't call it again
				
				assetDepedenceOnTarget.setDepthLevel(actualLevel);
				referencesOnSourceInTarget.add(assetDepedenceOnTarget); // is a dependence
				permformIndirectCallGraphicAnalysisDependence(javaProject, assetDepedenceOnTarget, allTargetAssets, referencesOnSourceInTarget, actualLevel+1, depthLevel); // recursively to next level
			}
		}
	}
	
	

	
	/**
	 * Verify if the asset in the list of classes, methods or fields.
	 * 
	 * @param assetIndirectDependece
	 * @param tableOfClasses
	 * @param tableOfMethods
	 * @param tableOfFields
	 * @return
	 */
	private final AssetChangeLog changeLogHistoryContainsAsset(AssetChangeLog assetIndirectDependece, 
			Map<ClassChangeLog,  List<ChangeLog>> hashOfClassesInTargetChangeLog,
			Map<MethodChangeLog, List<ChangeLog>> hashOfMethodsInTargetChangeLog, 
			Map<FieldChangeLog,  List<ChangeLog>> hashOfFieldsInTargetChangeLog ) {
		
		
		if(assetIndirectDependece instanceof ClassChangeLog){
			if( hashOfClassesInTargetChangeLog.containsKey( (ClassChangeLog) assetIndirectDependece) ) {
				List<ClassChangeLog> list = new ArrayList<ClassChangeLog>(hashOfClassesInTargetChangeLog.keySet());
				return list.get(list.indexOf((ClassChangeLog) assetIndirectDependece));
			}
		}
		
		if(assetIndirectDependece instanceof MethodChangeLog){
			
			if( hashOfMethodsInTargetChangeLog.containsKey( (MethodChangeLog) assetIndirectDependece) ){
				List<MethodChangeLog> list = new ArrayList<MethodChangeLog>(hashOfMethodsInTargetChangeLog.keySet());
				return  list.get(list.indexOf((MethodChangeLog) assetIndirectDependece));
			}
		}
		
		if(assetIndirectDependece instanceof FieldChangeLog){
			if( hashOfFieldsInTargetChangeLog.containsKey( (FieldChangeLog) assetIndirectDependece) ){
				List<FieldChangeLog> list = new ArrayList<FieldChangeLog>(hashOfFieldsInTargetChangeLog.keySet());
				return list.get(list.indexOf((FieldChangeLog) assetIndirectDependece));
			}
		}
		
		return null;
	}
	
	
}
