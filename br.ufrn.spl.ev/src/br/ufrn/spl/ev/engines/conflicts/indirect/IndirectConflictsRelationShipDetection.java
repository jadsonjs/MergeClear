/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.indirect;


/**
 * @author jadson - jadson@info.ufrn.br
 * @deprecated return a huge number false positives, it is not a good strategy 
 */
//public class IndirectConflictsRelationShipDetection extends IndirectConflictsDetection{
//
//	
//	public IndirectConflictsRelationShipDetection(IProgressMonitor monitor, IndirectConflictsAnalysisStrategy analysisStrategy, RepositoryConnector repositoryConnectorSource, RepositoryConnector repositoryConnectorTarget){
//		this.monitor = monitor;
//		this.repositoryConnectorSource = repositoryConnectorSource;
//		this.repositoryConnectorTarget = repositoryConnectorTarget;
//		this.analysisStrategy = analysisStrategy;
//	}
//	
//	
//	/**
//	 * <p>Receives a list of assets selected by the user, analyzes the indirect conflicts and return the model of conflict.</p>
//	 * 
//	 * <p>This is a <b>Template Method pattern</b> with the algorithm of check indirect conflict</p>
//	 * 
//	 * @param selectedAssets
//	 * @return
//	 * @throws Exception 
//	 */
//	public final void checkIndirectConflicts(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget,  ConflictModel conflictModel, short depthAnalysisLevel) throws IndirectConflictsDetectionException{
//		
//		if(depthAnalysisLevel < 1 || depthAnalysisLevel > 10){
//			throw new IndirectConflictsDetectionException("The depth analysis Level need to be between 1 and 10");
//		}
//		
//		this.depthAnalysisLevel = depthAnalysisLevel;
//		
//		loadProjectsData(changeLogHistorySource, changeLogHistoryTarget, conflictModel, repositoryConnectorSource, repositoryConnectorTarget);
//		
//		performAnalysis(); // who call this artefact
//		
//	}
//	
//
//	
//	
//
//	/**
//	 * This method contains the algorithm to verify the indirect conflicting of who is calling the actual artefact
//	 * 
//	 * <p> Created at:  16/10/2013  </p>
//	 *
//	 */
//	private final void performAnalysis() throws IndirectConflictsDetectionException{
//		
//		long time = System.currentTimeMillis();
//		
//		cacheCallGraphic = new TreeMap<AssetChangeLog, List<AssetChangeLog>>();
//		
//		int indirectConflictCount = 0;
//		
//		//////////       Keep the artefact on the target side   ////////
//		
//		Map<ClassChangeLog,  List<ChangeLog>> hashOfClassesInTargetChangeLog = new TreeMap<ClassChangeLog, List<ChangeLog>>();
//		Map<MethodChangeLog, List<ChangeLog>> hashOfMethodsInTargetChangeLog = new TreeMap<MethodChangeLog, List<ChangeLog>>();
//		Map<FieldChangeLog,  List<ChangeLog>> hashOfFieldsInTargetChangeLog  = new TreeMap<FieldChangeLog, List<ChangeLog>>();
//		//Map<CodePieceChangeLog, List<ChangeLog>> hashOfCodePieceTarget = new TreeMap<CodePieceChangeLog, List<ChangeLog>>();
//		
//		ChangeLogHistoryUtil.genarateHashOfAssetsHistoryChangeLog(changeLogHistoryTarget, hashOfClassesInTargetChangeLog, hashOfMethodsInTargetChangeLog, hashOfFieldsInTargetChangeLog );
//		
//		// Is the changes that the user wants to apply
//		List<AssetChangeLog> selectedAssetOnSource = ChangeLogHistoryUtil.getSelectedSourceCodeAssets(changeLogHistorySource);
//		
//		List<AssetChangeLog> allTargetAsset = ChangeLogHistoryUtil.getAllSourceCodeAssets(changeLogHistoryTarget);
//		
//		
//		if(selectedAssetOnSource.size() == 0){
//			GUIUtils.addInformationMessage("No assets were selected to make the indirect analysis. Please select the changes you want to apply!");
//			return;
//		}
//		
//		int qtdTotal = selectedAssetOnSource.size();
//		int count = 1;
//		int percente  = qtdTotal/100; // show the results for each 1%
//		
//		if(monitor != null) monitor.worked(0);
//		
////		System.out.println(" artefatos selecionados =>  ");
////		for (AssetChangeLog assetSelectedOnSource : selectedAssetOnSource) {
////			System.out.println(" - "+assetSelectedOnSource);
////		}
//		
//		// For each asset (class, method or field)  find it dependences 
//		for (AssetChangeLog assetSelectedOnSource : selectedAssetOnSource) {
//			
//			if( ! assetSelectedOnSource.isDirectlyConflicting() && ! assetSelectedOnSource.isIndirectlyConflicting()  ){ // is is already in direct or in indirect conflict with some target asset, we don't need analyze
//				
//				//System.out.println("analisando dependencias de "+assetSelectedOnSource);
//				
//				if(count % ( percente > 0  ? percente : 1) == 0) 
//					System.out.println(" >>> Analizing Indirect Conflicts "+count+" of "+qtdTotal+"  "+(count*100/qtdTotal)+"% with " + indirectConflictCount + " found. "+"Spent: "+( (System.currentTimeMillis()-time)/1000/60)+" minutes");
//				
//				if(monitor != null) monitor.worked((count*100/qtdTotal));
//				
//				/* Just find the asset in the call graphic of asset !!!!! ON TARGET !!!!!
//				 *  
//				 * OBS.: we have to pass the target project here 
//				 */
//				List<AssetChangeLog> callGraphicOnSourceInTarget = new ArrayList<AssetChangeLog>();
//				
//				// have we analyze just the first level of relationship
//				permformIndirectCallGraphicAnalysis(javaTargetProject, assetSelectedOnSource, allTargetAsset, callGraphicOnSourceInTarget, 1, 1); // on target !!!
//				
//				
//				/*
//				 * If no in indirect conflict and with a indirect conflict with some asset on target
//				 * 
//				 * All asserts related with the change assert on source can be a potential indirect conflict.
//				 */
//				for (AssetChangeLog assetIndirectDependeceOnTarget : callGraphicOnSourceInTarget) {
//					
//					/*
//					 *  por enquanto apenas adiciona ao modelo de conflitos, mas não marca como conflito no change log,
//					 *  porque a quantidade desse tipo é muito grande e a possíblidade de ser falso positivo também é grande.
//					 */
//					
//					// set the artefact as conflicting
//					// assetSelectedOnSource.setHierarchicalIndirectConflict(true, assetIndirectDependeceOnTarget.getDepthLevel());
//					
//					// add how is conflicts in the artefact
//					// assetSelectedOnSource.addIndirectConflict(assetIndirectDependeceOnTarget);
//					
//					
//					// set the artefact in the as conflicting
//					// assetTarget.setHierarchicalIndirectConflict(true, assetIndirectDependeceOnSource.getDepthLevel());
//					
//					indirectConflictCount++;
//					
//					// generate the conflict model ///
//					conflictModel.addIndirectConflictByRelationShip(assetSelectedOnSource, assetIndirectDependeceOnTarget );
//						
//				}
//			}
//			
//			count++;
//			
//		}
//	}
//	
//	
//	
//	/** 
//	 * <p>Recursive Algorithm to analyze the dependence of the asset</p>
//	 * 
//	 * <p> <strong> Can have a exponential asymptotic time O(x^n), so take care to optimize this method!!! </strong> </p>
//	 */
//	private final void permformIndirectCallGraphicAnalysis(IJavaProject javaProject, AssetChangeLog assetChangeLogSource,  List<AssetChangeLog> allTargetAssets, List<AssetChangeLog> dependences,  int actualLevel, final int depthLevel) throws IndirectConflictsDetectionException{
//		
//		if(assetChangeLogSource == null || actualLevel > depthLevel) // we arrive in the end of analysis
//			return;
//		
//		// ALL asset less the actual
//		List<AssetChangeLog> setOfOtherAssets = new ArrayList<AssetChangeLog>(allTargetAssets);
//		setOfOtherAssets.remove(assetChangeLogSource);
//		
//		// dependence find in the this level
//		List<AssetChangeLog> listOfAssetsCallGraphicOnTarget = analysisStrategy.getAssetsInTheCallGraphic(javaProject, assetChangeLogSource, setOfOtherAssets, cacheCallGraphic);
//		
////		System.out.println(" ====== CallGraphic Nível =====> "+actualLevel);
////		for (AssetChangeLog assetDepedenceOnTarget : listOfAssetsCallGraphicOnTarget) {
////			System.out.println(" - "+assetDepedenceOnTarget);
////		}
//		
//		// for each asset dependence:  call the dependence of dependence
//		for (AssetChangeLog assetDepedenceOnTarget : listOfAssetsCallGraphicOnTarget) {
//			
//			if( ! dependences.contains(assetDepedenceOnTarget) ){ // the first optimization, if we have already passed for a asset, don't call it again
//				
//				//System.out.println("Achamos conflitos: "+assetDepedenceOnTarget+" no nível "+actualLevel);
//				
//				assetDepedenceOnTarget.setDepthLevel(actualLevel);
//				dependences.add(assetDepedenceOnTarget); // is a dependence
//				permformIndirectCallGraphicAnalysis(javaProject, assetDepedenceOnTarget, allTargetAssets, dependences, actualLevel+1, depthLevel); // recursively to next level
//			}
//		}
//	}
//	
//	
//}
