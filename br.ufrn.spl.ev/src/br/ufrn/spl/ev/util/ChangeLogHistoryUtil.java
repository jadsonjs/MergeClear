/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE – UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA – DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.CodePieceChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;

/**
 * Utility for manipulate history change log
 * 
 * @author jadson
 * 
 * @since 02/12/2012
 * @version 1.0 - Class Creation
 */
public class ChangeLogHistoryUtil {

	

	/**
	 * <p>
	 * Format the ChangeLogHistory to be showed to the user.
	 * </p>
	 * 
	 * <p>
	 * Created at: 10/08/2013
	 * </p>
	 * 
	 * @param changeLogHistory
	 */
	public static void formatChangeLogHistoryToExposure(ChangeLogHistory changeLogHistory) {

		if(changeLogHistory == null) return;
		
		// O(n2)
		List<FeatureChangeLog> features = changeLogHistory.getFeatures();

		if (features != null)
			for (FeatureChangeLog feature : features) {

				if (StringUtils.isNotEmpty(feature.getDescription())) {
					feature.setDescription(feature.getDescription().replaceAll("&lt;", "<"));
					feature.setDescription(feature.getDescription().replaceAll("&gt;", ">"));
				}

				List<ChangeLog> changeLogs = feature.getChangelogs();

				if (changeLogs != null)
					for (ChangeLog changeLog : changeLogs) {
						if (StringUtils.isNotEmpty(changeLog.getDescription())) {
							changeLog.setDescription(changeLog.getDescription().replaceAll("&lt;", "<"));
							changeLog.setDescription(changeLog.getDescription().replaceAll("&gt;", ">"));
						}
					}
			}

	}

	/**
	 * /** Print the whole change log history object
	 * 
	 * @param changeLogHistory
	 * @param justConflicting
	 *            print just the items that are conflicting, if false print all
	 *            items
	 * @param justSelected
	 *            print just the items that are select, if false print all items
	 */
	public static void printChangeLog(ChangeLogHistory changeLogHistory, boolean justConflicting, boolean justSelected) {

		if (changeLogHistory == null)
			return;

		List<FeatureChangeLog> features = changeLogHistory.getFeatures();

		for (FeatureChangeLog feature : features) {

			if (((justSelected && feature.isSelected()) || !justSelected) && ((justConflicting && feature.isDirectlyConflicting()) || !justConflicting))
				System.out.println(feature.getFullName());

			List<ChangeLog> changeLogs = feature.getChangelogs();

			for (ChangeLog changeLog : changeLogs) {

				if (((justSelected && changeLog.isSelected()) || !justSelected) && ((justConflicting && changeLog.isDirectlyConflicting()) || !justConflicting))
					System.out.println("\t" + changeLog.getFullName());

				for (ClassChangeLog _class : changeLog.getClasses()) {

					if (((justSelected && _class.isSelected()) || !justSelected) && ((justConflicting && _class.isDirectlyConflicting()) || !justConflicting))
						System.out.println("\t\t" + _class.getFullName());

					for (FieldChangeLog field : _class.getFields()) {
						if (((justSelected && field.isSelected()) || !justSelected) && ((justConflicting && field.isDirectlyConflicting()) || !justConflicting))
							System.out.println("\t\t\t" + field.getFullName());
					}

					for (MethodChangeLog method : _class.getMethods()) {

						if (((justSelected && method.isSelected()) || !justSelected) && ((justConflicting && method.isDirectlyConflicting()) || !justConflicting))
							System.out.println("\t\t\t" + method.getFullName());

						for (CodePieceChangeLog code : method.getCodepieces()) {
							if (((justSelected && code.isSelected()) || !justSelected) && ((justConflicting && code.isDirectlyConflicting()) || !justConflicting))
								System.out.println("\t\t\t\t" + code.getFullName());
						}
					}
				}

			}
		}
	}
	

	/**
	 * <p>
	 * Return the selected source code assets that the user want to apply to the
	 * target. (Classes, Methods and Fields and CodePieces)
	 * </p>
	 * 
	 * <p>
	 * Features and Change Log are not source code assets
	 * </p>
	 * 
	 * @param changeLogHistory
	 *            The change log with the evolution information
	 */
	public static List<AssetChangeLog> getSelectedSourceCodeAssets(ChangeLogHistory changeLogHistory) {

		if(changeLogHistory == null) return new ArrayList<AssetChangeLog>();
		
		List<AssetChangeLog> selectedAssets = new ArrayList<AssetChangeLog>();


		List<FeatureChangeLog> features = changeLogHistory.getFeatures();

		for (FeatureChangeLog feature : features) {

			if (feature.isSelected()) {

				List<ChangeLog> changeLogs = feature.getChangelogs();

				for (ChangeLog changeLog : changeLogs) {

					if (changeLog.isSelected()) {

						for (ClassChangeLog _class : changeLog.getClasses()) {

							if (_class.isSelected()) {

								selectedAssets.add(_class); // is a source code
															// asset

								for (FieldChangeLog field : _class.getFields()) {
									if (field.isSelected()) {
										selectedAssets.add(field);
									}
								}

								for (MethodChangeLog method : _class.getMethods()) {

									if (method.isSelected()) {
										selectedAssets.add(method);

										for (CodePieceChangeLog codePiece : method.getCodepieces()) {
											if (codePiece.isSelected()) {
												selectedAssets.add(codePiece);
											}
										}
									} // if the method is selected
								}

							} // is the class is selected
						}

					}// change log is selected

				}

			} // feature is select
		}

		return selectedAssets;
	}
	
	
	/**
	 * <p>
	 * Return the ALL source code assets that the user want to apply to the
	 * target. (Classes, Methods and Fields and CodePieces)
	 * </p>
	 * 
	 * <p>
	 * Features and Change Log are not source code assets
	 * </p>
	 * 
	 * @param changeLogHistory
	 *            The change log with the evolution information
	 */
	public static List<AssetChangeLog> getAllSourceCodeAssets(ChangeLogHistory changeLogHistory) {

		if(changeLogHistory == null) return new ArrayList<AssetChangeLog>();
		
		List<AssetChangeLog> selectedAssets = new ArrayList<AssetChangeLog>();


		List<FeatureChangeLog> features = changeLogHistory.getFeatures();

		for (FeatureChangeLog feature : features) {

			List<ChangeLog> changeLogs = feature.getChangelogs();

			for (ChangeLog changeLog : changeLogs) {
				for (ClassChangeLog _class : changeLog.getClasses()) {
					selectedAssets.add(_class); // is a source code asset

					for (FieldChangeLog field : _class.getFields()) {
						selectedAssets.add(field);
					}

					for (MethodChangeLog method : _class.getMethods()) {
						selectedAssets.add(method);

						for (CodePieceChangeLog codePiece : method.getCodepieces()) {						
							selectedAssets.add(codePiece);
						}		
					}		
				}	
			}
		}

		return selectedAssets;
	}

	
	/**
	 * Return just the top level asset selected.
	 * 
	 * For example: If a change log is selected, return just this change log,
	 * don't return all items inside ChangeLog
	 * 
	 * This method supposes that if you selected a change log, all thing that
	 * the change log contains were seleted too.
	 * 
	 * @param changeLogHistory
	 * @return
	 */
	public static List<AssetChangeLog> getSelectedTopLevelAssets(ChangeLogHistory changeLogHistory) {

		List<AssetChangeLog> selectedAssets = new ArrayList<AssetChangeLog>();

		if (changeLogHistory == null)
			return selectedAssets;

		List<FeatureChangeLog> features = changeLogHistory.getFeatures();

		for (FeatureChangeLog feature : features) {

			if (feature.isSelected()) {

				selectedAssets.add(feature);

			} else {

				List<ChangeLog> changeLogs = feature.getChangelogs();

				for (ChangeLog changeLog : changeLogs) {

					if (changeLog.isSelected()) {

						selectedAssets.add(changeLog);

					} else {

						for (ClassChangeLog _class : changeLog.getClasses()) {

							if (_class.isSelected()) {

								selectedAssets.add(_class);

							} else {

								for (FieldChangeLog field : _class.getFields()) {
									if (field.isSelected()) {
										selectedAssets.add(field);
									}
								}

								for (MethodChangeLog method : _class.getMethods()) {

									if (method.isSelected()) {
										selectedAssets.add(method);

									} else {
										for (CodePieceChangeLog codePiece : method.getCodepieces()) {
											if (codePiece.isSelected()) {
												selectedAssets.add(codePiece);
											}
										}
									} // if the method is selected
								}

							} // is the class is selected
						}

					}// change log is selected

				}

			} // feature is select
		}

		return selectedAssets;
	}

	/**
	 * <p>
	 * Crosses all assets of history change log to create 4 hash of assets
	 * presents on change log history, to try to search the assets in a log(n)
	 * asymptotic time.
	 * </p>
	 * 
	 * <p>
	 * This is a very expensive method, in a n^4 asymptotic time, but I think
	 * that worth.
	 * </p>
	 * 
	 */
	public static void genarateHashOfAssetsHistoryChangeLog(ChangeLogHistory historyChangeLogTarget, Map<ClassChangeLog, List<ChangeLog>> hashClasses,
			Map<MethodChangeLog, List<ChangeLog>> hashMethods, Map<FieldChangeLog, List<ChangeLog>> hashFields  ) {

		long time = System.currentTimeMillis();
		
		///ConflictAnalysisLevel level = ConfigUtils.getConflictAnalysisLevel();
		
		// O(n^5)
		List<FeatureChangeLog> features = historyChangeLogTarget.getFeatures();

		if (features != null)

			for (FeatureChangeLog feature : features) {

				List<ChangeLog> changeLogs = feature.getChangelogs();

				if (changeLogs != null) {

					for (ChangeLog changeLog : changeLogs) {

						if (changeLog.getClasses() != null)

							for (ClassChangeLog classChangeLog : changeLog.getClasses()) {

								if (!hashClasses.containsKey(classChangeLog)) {
									List<ChangeLog> list = new ArrayList<ChangeLog>();
									list.add(changeLog);
									hashClasses.put(classChangeLog, list);
								} else {
									List<ChangeLog> list = hashClasses.get(classChangeLog);
									if(! list.contains(changeLog)){
										list.add(changeLog);
									}
								}

								if (classChangeLog.getFields() != null) {

									for (FieldChangeLog fieldChangeLog : classChangeLog.getFields()) {

										/*
										 * Put a reference of the father to the
										 * sons, to allow "doubly linked tree"
										 */
										fieldChangeLog.setClassChangeLog(classChangeLog);

										if (!hashFields.containsKey(fieldChangeLog)) {
											
									
											List<ChangeLog> list = new ArrayList<ChangeLog>();
											list.add(changeLog);
											hashFields.put(fieldChangeLog, list);
										
											
										} else {
											List<ChangeLog> list = hashFields.get(fieldChangeLog);
											
											if(! list.contains(changeLog)){
												list.add(changeLog);
											}
										}
									}

								}

								if (classChangeLog.getMethods() != null) {

									for (MethodChangeLog methodChangeLog : classChangeLog.getMethods()) {

										/*
										 * Put a reference of the father to the
										 * sons, to allow "doubly linked tree"
										 */
										methodChangeLog.setClassChangeLog(classChangeLog);

										if (!hashMethods.containsKey(methodChangeLog)) {
											
											//if ( methodChangeLog.isChangeConsidered(level)) {
												List<ChangeLog> list = new ArrayList<ChangeLog>();
												list.add(changeLog);
												hashMethods.put(methodChangeLog, list);
											//}
											
										} else {
											List<ChangeLog> list =  hashMethods.get(methodChangeLog);
											
											if(! list.contains(changeLog)){
												list.add(changeLog);
											}
										}

									}

								}
							}

					}
				}
			}

		System.out.println(" Crosses all assets of history change log spent " + (System.currentTimeMillis() - time) + " ms");

	}
	
	
	/***
	 * Return the Feature of that AssetChangeLog
	 * @param asset
	 * @return
	 */
	public static Set<ChangeLog> getChangeLogOfAssets(List<AssetChangeLog> assets){
		
		Set<ChangeLog> changeLogs = new HashSet<ChangeLog>();
		
		for (AssetChangeLog asset : assets) {
			if(asset instanceof ClassChangeLog){
				changeLogs.add(( (ClassChangeLog) asset).getChangelog());
			}else{
				if(asset instanceof MethodChangeLog){
					changeLogs.add(( (MethodChangeLog) asset).getChangelog());
				}else{
					if(asset instanceof FieldChangeLog){
						changeLogs.add(( (FieldChangeLog) asset).getChangelog());
					}else{
						if(asset instanceof CodePieceChangeLog ){
							changeLogs.add(( (CodePieceChangeLog) asset).getChangelog());
						}else{
							return null; // next asset
						}
					}
				}
			}
		}
		return changeLogs;
	}
	

//	/**
//	 * <p>
//	 * Crosses all assets of history change log to create 4 tables.
//	 * </p>
//	 * 
//	 */
//	public static void genarateTableOfAssetsHistoryChangeLog(ChangeLogHistory historyChangeLog, Collection<ClassChangeLog> tableClasses,
//			Collection<MethodChangeLog> tableMethods, Collection<FieldChangeLog> tableFields) {
//
//		long time = System.currentTimeMillis();
//
//		List<FeatureChangeLog> features = historyChangeLog.getFeatures();
//		
//		//ConflictAnalysisLevel level = ConfigUtils.getConflictAnalysisLevel();
//
//		if (features != null)
//			for (FeatureChangeLog feature : features) {
//
//				List<ChangeLog> changeLogs = feature.getChangelogs();
//
//				if (changeLogs != null)
//
//					for (ChangeLog changeLog : changeLogs) {
//
//						if (changeLog.getClasses() != null)
//							for (ClassChangeLog classChangeLog : changeLog.getClasses()) {
//
//								
//								tableClasses.add(classChangeLog);
//
//								if (classChangeLog.getFields() != null)
//									for (FieldChangeLog fieldChangeLog : classChangeLog.getFields()) {
//										//if ( fieldChangeLog.isChangeConsidered(level))
//											tableFields.add(fieldChangeLog);
//									}
//
//								if (classChangeLog.getMethods() != null)
//									for (MethodChangeLog methodChangeLog : classChangeLog.getMethods()) {
//										//if ( methodChangeLog.isChangeConsidered(level))
//											tableMethods.add(methodChangeLog);
//
//									}
//							}
//
//					}
//
//			}
//
//		System.out.println(" Crosses all assets of history change log spent " + (System.currentTimeMillis() - time) + " ms");
//
//	}
}
