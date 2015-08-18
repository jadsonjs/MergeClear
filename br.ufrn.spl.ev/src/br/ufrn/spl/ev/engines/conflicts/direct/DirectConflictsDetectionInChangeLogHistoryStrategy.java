/**
 *
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 *
 */
package br.ufrn.spl.ev.engines.conflicts.direct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ImplementationsChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;
import br.ufrn.spl.ev.util.ChangeLogHistoryUtil;

/**
 * <p>
 * This class make the first conflicts analysis in that assets the have direct
 * conflict, in other words, the same asset change in both side (source and
 * target) during the period of time that we are analyzing, ie, appears in both
 * ChangeLogHistory
 * </p>
 * 
 * <p>
 * In the end is generating a product line conflict model, like this:
 * 
 * <pre>
 * Product Line Conflict Model
 *    -> ProductLineConflict
 *        -> sourceFeature
 *        -> FeatureConflict...
 *             -> targetFeature
 *             -> AssetConflict...
 *             		-> assetChangeLogSource
 *             		-> assetChangeLogTarget
 *                  -> ConflictType
 * </pre>
 * 
 * </p>
 * 
 * @author uira - uira@dimap.ufrn.br
 * @author jadson - jadson@info.ufrn.br
 * @author gleydson - gleydson@esig.com.br
 * 
 * @since 13/03/2013
 * @version 1.0 - Class Creation
 */
public class DirectConflictsDetectionInChangeLogHistoryStrategy implements DirectConflictsDetectionStrategy {

	
	/**
	 * <p>
	 * To finish the last part of Diff Engine. Try to identify source code
	 * conflict and put this information (Change Log) on delta model.
	 * </p>
	 * 
	 * <p>
	 * Put all information on target on hash map
	 * </p>
	 * 
	 * <p>
	 * Conflict here is when the same asset (class, method, field) change in
	 * both change log history
	 * </p>
	 * 
	 * @param deltaModel
	 * @throws IOException
	 */
	public ConflictModel checkDirectConflicts(ChangeLogHistory historyChangeLogSource, ChangeLogHistory historyChangeLogTarget, ConflictModel conflictModel) {

		long time = System.currentTimeMillis();
		
		if(conflictModel == null) conflictModel =  new ConflictModel();
		
		if (historyChangeLogSource == null || historyChangeLogTarget == null)
			return conflictModel;

		
		/*
		 * Hash Tables with the assets and the list of change logs were it
		 * appear
		 */
		Map<ClassChangeLog, List<ChangeLog>> hashOfClassesTarget = new TreeMap<ClassChangeLog, List<ChangeLog>>();
		Map<MethodChangeLog, List<ChangeLog>> hashOfMethodsTarget = new TreeMap<MethodChangeLog, List<ChangeLog>>();
		Map<FieldChangeLog, List<ChangeLog>> hashOfFieldsTarget = new TreeMap<FieldChangeLog, List<ChangeLog>>();
		//Map<CodePieceChangeLog, List<ChangeLog>> hashOfCodePieceTarget = new TreeMap<CodePieceChangeLog, List<ChangeLog>>();
		
		ChangeLogHistoryUtil.genarateHashOfAssetsHistoryChangeLog(historyChangeLogTarget, hashOfClassesTarget, hashOfMethodsTarget, hashOfFieldsTarget );

		List<ClassChangeLog> classesTarget = new ArrayList<ClassChangeLog>();
		classesTarget.addAll( hashOfClassesTarget.keySet() );
		
		//
		// run the direct conflict just to the selected asset, to be faster if you don't want to apply all assets
		//
		List<AssetChangeLog> selectedAssetOnSource = ChangeLogHistoryUtil.getSelectedSourceCodeAssets(historyChangeLogSource);
			
			
		Set<ChangeLog> changeLogs = ChangeLogHistoryUtil.getChangeLogOfAssets(selectedAssetOnSource); // get the high level element of the selected asset

		for (ChangeLog changeLogSource : changeLogs) { // for each change log

			boolean changeLogConflicting = checkChangeLogConflicting(changeLogSource.getFeature(), changeLogSource, hashOfClassesTarget, hashOfMethodsTarget, hashOfFieldsTarget,
					classesTarget, conflictModel);

			if (changeLogConflicting)
				changeLogSource.getFeature().setDirectlyConflicting(true);
		}


		//}

		System.out.println(" Conflictings detections spend " + (System.currentTimeMillis() - time) + " ms");

		return conflictModel;

	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * This method check the assets conflicts
	 */
	private boolean checkChangeLogConflicting(FeatureChangeLog featureSource, ChangeLog changeLogSource, Map<ClassChangeLog, List<ChangeLog>> hashOfClassesTarget,
			Map<MethodChangeLog, List<ChangeLog>> hashOfMethodsTarget, Map<FieldChangeLog, List<ChangeLog>> hashOfFieldsTarget,
			List<ClassChangeLog> classesTarget, ConflictModel conflictModel) {

		boolean changeLogHasClassConflicting = false;

		if (changeLogSource.getClasses() != null)
			for (ClassChangeLog classSource : changeLogSource.getClasses()) { // for each class of change log

				boolean classHasFieldsConflicting = classHasFieldsConflicting(classSource, changeLogSource, featureSource, hashOfFieldsTarget, conflictModel );

				boolean classHasMethodsConflicting = classHasMethodsConflicting(classSource, changeLogSource, featureSource, hashOfMethodsTarget, conflictModel );
				
				boolean hasExtensionClassConflict = classHasExtensionConflicting(classSource, changeLogSource, classesTarget, conflictModel );
				
				boolean hasInterfaceClassConflict = classHasInterfaceConflicting(classSource, changeLogSource, classesTarget, conflictModel );

				if (classHasFieldsConflicting || classHasMethodsConflicting || hasExtensionClassConflict || hasInterfaceClassConflict ) {
					// put the class in the conflict model.
					//addInformationClassConflicting(classSource, changeLogSource, featureSource, hashOfClassesTarget );

					classSource.setDirectlyConflicting(true);

					changeLogHasClassConflicting = true;
					
				}

			}

		changeLogSource.setDirectlyConflicting(changeLogHasClassConflicting);

		return changeLogHasClassConflicting;
	}

	/*
	 * Test if the fields of the class are conflicting
	 * 
	 * @param feature
	 * 
	 * @param squidAuxModelSource
	 * 
	 * @param squidAuxModelTarget
	 * 
	 * @param historyChangeLogSource
	 * 
	 * @param historyChangeLogTarget
	 * 
	 * @param _class
	 * 
	 * @return
	 */
	private boolean classHasFieldsConflicting(ClassChangeLog classSource, ChangeLog changeLogSource, FeatureChangeLog featureSource,
			Map<FieldChangeLog, List<ChangeLog>> hashOfFieldsTarget, ConflictModel conflictModel ) {

		boolean classHasFieldsConflicting = false;

		if (classSource.getFields() != null)
			for (FieldChangeLog fieldSource : classSource.getFields()) {

				boolean fieldConfliting = checkFieldConflicting(fieldSource, changeLogSource, featureSource, hashOfFieldsTarget, conflictModel);

				// if at least some of the fields are conflicting
				if (fieldConfliting)
					classHasFieldsConflicting = true;
			}

		return classHasFieldsConflicting;

	}

	/**
	 * Test if the methods of the class are conflicting
	 * 
	 * @param feature
	 * @param squidAuxModelSource
	 * @param squidAuxModelTarget
	 * @param historyChangeLogSource
	 * @param historyChangeLogTarget
	 * @param _class
	 * 
	 * @return
	 */
	private boolean classHasMethodsConflicting(ClassChangeLog classSource, ChangeLog changeLogSource, FeatureChangeLog featureSource,
			Map<MethodChangeLog, List<ChangeLog>> hashOfMethodTarget, ConflictModel conflictModel ) {

		boolean classHasMethodsConflicting = false;

		if (classSource.getMethods() != null)
			for (MethodChangeLog methodSource : classSource.getMethods()) {

				//boolean methodHasCodePieceConflicting = methodHasCodePiecesConflicting(methodSource, changeLogSource, featureSource, hashCodePieceTarget, productLineConflictModel);

				boolean isMethodConflicting = checkMethodConflicting(methodSource, changeLogSource, featureSource, hashOfMethodTarget, conflictModel);

				// If some method is conflicting the father is conflict
				if ( isMethodConflicting  ) {
					classHasMethodsConflicting = true;
				}
			}

		return classHasMethodsConflicting;
	}
	
	/**
	 * Test if source and target have extension evolution in the same time
	 * @param classSource
	 * @param changeLogSource
	 * @param classesTarget
	 * @param productLineConflictModel
	 * @return
	 */
	private boolean classHasExtensionConflicting(ClassChangeLog classSource, ChangeLog changeLogSource, List<ClassChangeLog> classesTarget, ConflictModel conflictModel) {

		if (classSource.getExtension() != null) {
			
			int index = classesTarget.indexOf( classSource );
			if ( index >= 0  && classesTarget.get(index).getExtension() != null ) {
				conflictModel.addDirectConflict(classSource.getExtension(), classesTarget.get(index).getExtension());
				return true;
			}
		}
			
		return false;
	}
	
	/**
	 * Test if source and target have extension evolution in the same time
	 * @param classSource
	 * @param changeLogSource
	 * @param classesTarget
	 * @param productLineConflictModel
	 * @return
	 */
	private boolean classHasInterfaceConflicting(ClassChangeLog classSource, ChangeLog changeLogSource, List<ClassChangeLog> classesTarget, ConflictModel conflictModel) {

		if (classSource.getImplementations() != null && classSource.getImplementations().size() > 0 ) {
			
			int index = classesTarget.indexOf( classSource );
			if ( index >= 0  && classesTarget.get(index).getImplementations() != null && classesTarget.get(index).getImplementations().size() > 0 ) {
				
				for ( ImplementationsChangeLog iclSource : classSource.getImplementations() ) {
					for (  ImplementationsChangeLog iclTarget: classesTarget.get(index).getImplementations() ) {
						conflictModel.addDirectConflict(iclSource, iclTarget);
					}
				}

				return true;
			}
		}
			
		return false;
	}

	// /////////////////////////////////////////////////////////////////////////////
	// ///// forward we have the methods with the logic of conflict detections
	// /////
	// /////////////////////////////////////////////////////////////////////////////

	/*
	 * Get the CHANGELOG on the target side where the class appears and add it
	 * to a conflict model
	 */
//	private boolean addInformationClassConflicting(ClassChangeLog classSource, ChangeLog changeLogSource, FeatureChangeLog featureSource,
//			Map<ClassChangeLog, List<ChangeLog>> hashOfClasseTarget ) {
//
//		List<ChangeLog> changeLogsTarget = hashOfClasseTarget.get(classSource);
//
//		// This class change on target too //
//		if (changeLogsTarget != null && changeLogsTarget.size() > 0) {
//
//			// Find the class on the target //
//			ClassChangeLog classTarget = (ClassChangeLog) findSameAssetsInTarget(hashOfClasseTarget, classSource);
//
//			// Now that you know the asset is conflicting, put this asset in the
//			// conflict model //
//			addConflictAssetToConflictModel(classSource, classTarget, AssetConflictType.CLASS_CONFLICT);
//
//			return true;
//		}
//		return false;
//	}

	/*
	 * check if the method change of target too.
	 */
	private boolean checkMethodConflicting(MethodChangeLog methodSource, ChangeLog changeLogSource, FeatureChangeLog featureSource,
			Map<MethodChangeLog, List<ChangeLog>> hashOfMethodsTarget, ConflictModel conflictModel ) {
		
		List<ChangeLog> changeLogsTarget = hashOfMethodsTarget.get(methodSource);

		// This method change on target too //
		if (changeLogsTarget != null && changeLogsTarget.size() > 0) {

			// Find the method on the target //
			List<AssetChangeLog> methodsTarget = findSameAssetsInTarget(hashOfMethodsTarget, methodSource);
			
			for (AssetChangeLog methodTarget : methodsTarget) {
				
				methodSource.addDirectConflict(methodTarget);
				
				methodTarget.setDirectlyConflicting(true);
				methodTarget.setHierarchicalDirectConflict(true);
				
				// Now that you know the asset is conflicting, put this asset in the
				// conflict model //
				conflictModel.addDirectConflict( methodSource, methodTarget);

			}
			
			methodSource.setDirectlyConflicting(true);

			return true;
		}

		return false;
	}

	/*
	 * check if the field change of target too.
	 */
	private boolean checkFieldConflicting(FieldChangeLog fieldSource, ChangeLog changeLogSource, FeatureChangeLog featureSource,
			Map<FieldChangeLog, List<ChangeLog>> hashOfFieldsTarget, ConflictModel conflictModel ) {

		// if there is a change related with the field
		List<ChangeLog> changeLogsTarget = hashOfFieldsTarget.get(fieldSource);

		// This field change on target too //
		if (changeLogsTarget != null && changeLogsTarget.size() > 0) {

			// Find the class on the target //
			List<AssetChangeLog> fieldsTarget = findSameAssetsInTarget(hashOfFieldsTarget, fieldSource);

			for (AssetChangeLog fieldTarget : fieldsTarget) {
			
				fieldSource.addDirectConflict(fieldTarget);
				
				fieldTarget.setDirectlyConflicting(true);
				fieldTarget.setHierarchicalDirectConflict(true);
	
				// Now that you know the asset is conflicting, put this asset in the
				// conflict model //
				conflictModel.addDirectConflict(fieldSource, fieldTarget);

			}
			fieldSource.setDirectlyConflicting(true);

			return true;
		}

		return false;
	}

	/*
	 * check if the code piece change of target too.
	 *
	private boolean checkCodePieceConflicting(CodePieceChangeLog codePieceSource, ChangeLog changeLogSource, FeatureChangeLog featureSource,
			Map<CodePieceChangeLog, List<ChangeLog>> hashOfCodePiecesTarget, ProductLineConflictModel productLineConflictModel) {

		List<ChangeLog> changeLogsTarget = hashOfCodePiecesTarget.get(codePieceSource);

		// This code piece change on target too //
		if (changeLogsTarget != null && changeLogsTarget.size() > 0) {

			// Find the class on the target //
			CodePieceChangeLog codePieceTarget = (CodePieceChangeLog) findTargetAsset(hashOfCodePiecesTarget, codePieceSource);

			// Now that you know the asset is conflicting, put this asset in the
			// conflict model //
			addConflictAssetToConflictModel(productLineConflictModel, featureSource, codePieceSource, codePieceTarget, changeLogsTarget, ConflictType.CODE_PIECE_CONFLICT);

			codePieceSource.setDirectlyConflicting(true);

			return true;
		}

		return false;
	} */

	

	/*
	 * Return the same asset on target side by the signature. usually are assets in conflict
	 * 
	 * @param hashOfCodePiecesTarget
	 * 
	 * @param codePieceTarget
	 * 
	 * @return <T, List<ChangeLog>>
	 */
	private List<AssetChangeLog> findSameAssetsInTarget(Map<? extends AssetChangeLog, List<ChangeLog>> hashOfAssetTarget, AssetChangeLog assetChangeLogSource) {

		
		List<AssetChangeLog> _return = new ArrayList<AssetChangeLog>();
		
		List<ChangeLog> changeLogsTarget = hashOfAssetTarget.get(assetChangeLogSource);
	
		for (ChangeLog changeLogTarget : changeLogsTarget) {
			
			for(ClassChangeLog _class : changeLogTarget.getClasses()){
				
				if(assetChangeLogSource instanceof FieldChangeLog)
					for(FieldChangeLog field : _class.getFields()){
						if (field.equals(assetChangeLogSource)){
							_return.add(field);
						}
					}
				if(assetChangeLogSource instanceof MethodChangeLog)
					for(MethodChangeLog method : _class.getMethods()){
						if (method.equals(assetChangeLogSource)){
							_return.add(method);
						}
					}
			}
		}
		
		return _return;

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
