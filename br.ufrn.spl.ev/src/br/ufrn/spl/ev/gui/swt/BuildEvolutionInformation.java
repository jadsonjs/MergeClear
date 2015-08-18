/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.gui.swt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import br.ufrn.spl.ev.models.changeloghistorymodel.AnnotationChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.CodePieceChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ExceptionChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ImplementationsChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;
import br.ufrn.spl.ev.models.conflictmodel.AssetConflict;
import br.ufrn.spl.ev.models.conflictmodel.Conflict;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;
import br.ufrn.spl.ev.models.conflictmodel.ConflictSubType;
import br.ufrn.spl.ev.models.conflictmodel.ConflictType;
import br.ufrn.spl.ev.models.dependencemodel.AssetDependence;
import br.ufrn.spl.ev.models.dependencemodel.Dependence;
import br.ufrn.spl.ev.models.dependencemodel.DependenceModel;
import br.ufrn.spl.ev.util.GUIUtils;
import br.ufrn.spl.ev.variability.Variability;

/**
 * <p>This class build the information of evolutio of the MAIN UI of the application</p>
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class BuildEvolutionInformation {
	
	
	
	/* The Main Screen of the application */
	private ShowEvolutionsUI showEvolutionsUI;

	
	
	
	/** 
	 * <p>This method shows all information about evolutions </p> 
	 *  
	 * <p> The source change log, the target change log and the  conflicts to the user
	 *  
	 */
	public void showEvolutionsData(final ShowEvolutionsUI showEvolutionsUI, final ChangeLogHistory changeLogHistorySource, final ChangeLogHistory changeLogHistoryTarget
			, final ConflictModel conflictModel, final DependenceModel sourceDependenceModel) {
		
		// prevents "Invalid Thread Access" when updates the UI
		Display.getDefault().syncExec(new Runnable() {
		    public void run() {
				
		    	BuildEvolutionInformation.this.showEvolutionsUI = showEvolutionsUI;
		    	
				showSourceEvolutionsTree(changeLogHistorySource);
				showTargetEvolutionsTree(changeLogHistoryTarget);
				
				showListOfConflictsGrouped(showEvolutionsUI.getTabFolder(), showEvolutionsUI.getConflictsTabItem(), changeLogHistorySource);
				showConflictModel(showEvolutionsUI.getTabFolder(), showEvolutionsUI.getConflictsTabItemPair(), conflictModel);     // show the productLineConflictModel directly accessing the singleton ConflictRepository
				
				if(sourceDependenceModel != null)
					showDependenceTree(showEvolutionsUI.getTabFolder(), showEvolutionsUI.getDependenceTabItem(), sourceDependenceModel);
		    }
		});
		
	}

	
	
	/* Show the Source Evolution Tree */
	private void showSourceEvolutionsTree( final ChangeLogHistory deltaHistoryChangeLog ) {
		buildChangeLogTree(showEvolutionsUI.getTabFolder(), showEvolutionsUI.getSourceEvolutionsTabItem(), deltaHistoryChangeLog, true);
	}

	/* Show the Target Evolution Tree */
	private void showTargetEvolutionsTree( final ChangeLogHistory deltaHistoryChangeLog ) {
		buildChangeLogTree(showEvolutionsUI.getTabFolder(), showEvolutionsUI.getTargetEvolutionsTabItem(), deltaHistoryChangeLog, false);
	}
	
	
	/**
	 * <p> Builder the tree of conflicts to the user GUI. </p>
	 * 
	 * <p> Mark that evolution the are in conflicts and create the events too, like the user select a change log, or what 
	 * happen when the user do a double click on a element. </p>
	 * 
	 * @param shell
	 * @param listOfConflicts
	 */
	private void buildChangeLogTree(final TabFolder tabFolder, final TabItem tab, final ChangeLogHistory changeLogHistory, boolean allowSelection) {

		if(changeLogHistory == null) return;
		
		int style = 0;

		if (allowSelection)
			style = SWT.BORDER | SWT.CHECK | SWT.V_SCROLL | SWT.H_SCROLL;
		else
			style = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;

		final Tree tree = new Tree(tabFolder, style);

		/*
		 * **** Mark the asset children when the user select a determined asset ****
		 */
		tree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					TreeItem item = (TreeItem) event.item;
					boolean checked = item.getChecked();
					GUIUtils.checkItemsTree(item, checked);
					GUIUtils.checkPathTree(item.getParentItem(), checked, false);
				}
			}
		});
		/* ********************************************************************* */

		
		createPopUpMenu(changeLogHistory, tree);

		for (FeatureChangeLog feature : changeLogHistory.getFeatures()) {

			TreeItem root = new TreeItem(tree, SWT.CHECK);
			root.setText("Feature: " + feature.getName() + " [" + (feature.getType() != null ? feature.getType().name() : "") + "] "
					+ (feature.getChangelogs() != null ? "( " + feature.getChangelogs().size() + " changes ) " : ""));

			root.setData(feature);

			/* ********* contrast feature conflict ******** */
			contrastConflicts(feature, root);

			
			for (ChangeLog changeLog : feature.getChangelogs()) {

				TreeItem item = new TreeItem(root, 0);

				item.setText(changeLog.getFullDescription());
				item.setData(changeLog);

				/* ********* contrast changeLog conflict ******** */
				contrastConflicts(changeLog, item);

				for (ClassChangeLog classChangeLog : changeLog.getClasses()) {

					buildTreeOfConflictsForClass(item, classChangeLog);
				}

			}

		}

		// add the tree to the tab
		tab.setControl(tree);

	}
	
	
	
	/**
	 * Load the tree of conflict information for ClassChangeLog, using the "module view" or "file view"
	 * 
	 * <p> Created at:  18/12/2013  </p>
	 *
	 * @param item
	 * @param classChangeLog
	 */
	private void buildTreeOfConflictsForClass(TreeItem item, ClassChangeLog classChangeLog) {
		
		TreeItem subItem = new TreeItem(item, 0);
		subItem.setText(" [" + classChangeLog.getChangeType() + "] " + classChangeLog.printChangesLocation() + " Class: " + classChangeLog.getSignature());
		subItem.setData(classChangeLog);

		/* ********* contrast class conflict ******** */
		contrastConflicts(classChangeLog, subItem);

		if (classChangeLog.getExtension() != null) {
			TreeItem subsubItem = new TreeItem(subItem, 0);
			subsubItem.setText(" [" + classChangeLog.getExtension().getChangeType() + "] "  + " Extension: " + classChangeLog.getExtension().getSignature());
			subsubItem.setData(classChangeLog.getExtension());

			/* ********* contrast field conflict ******** */
			contrastConflicts(classChangeLog.getExtension(), subsubItem);
		}
		
		
		for (AnnotationChangeLog annotation : classChangeLog.getAnnotations()) {	
			TreeItem subsubItem = new TreeItem(subItem, 0);
			subsubItem.setText(" [" + annotation.getChangeType() + "] " + " Annotation: " + annotation.getSignature());
			subsubItem.setData(annotation);

			/* ********* contrast field conflict ******** */
			contrastConflicts(annotation, subsubItem);
		}
		
		for (ImplementationsChangeLog implementation : classChangeLog.getImplementations()) {
			TreeItem subsubItem = new TreeItem(subItem, 0);
			subsubItem.setText(" [" + implementation.getChangeType() + "] " + " Implementation: " + implementation.getSignature());
			subsubItem.setData(implementation);

			/* ********* contrast field conflict ******** */
			contrastConflicts(implementation, subsubItem);
		}
		
		
		for (FieldChangeLog field : classChangeLog.getFields()) {
			TreeItem subsubItem = new TreeItem(subItem, 0);
			subsubItem.setText(" [" + field.getChangeType() + "] "  + field.printChangesLocation() + " Field: " + field.getSignature());
			subsubItem.setData(field);

			/* ********* contrast field conflict ******** */
			contrastConflicts(field, subsubItem);
			
			for (AnnotationChangeLog annotation : field.getAnnotations()) {	
				TreeItem subsubsubItem = new TreeItem(subsubItem, 0);
				subsubsubItem.setText(" [" + annotation.getChangeType() + "] " + " Annotation: " + annotation.getSignature());
				subsubsubItem.setData(annotation);

				/* ********* contrast field conflict ******** */
				contrastConflicts(annotation, subsubsubItem);
			}

		}

		for (MethodChangeLog method : classChangeLog.getMethods()) {
			TreeItem subsubItem = new TreeItem(subItem, 0);
			subsubItem.setText(" [" + method.getChangeType() + "] " + method.printChangesLocation() + " Method: " + method.getSignature());
			subsubItem.setData(method);

			/* ********* contrast method conflict ******** */
			contrastConflicts(method, subsubItem);

			for (AnnotationChangeLog annotation : method.getAnnotations()) {	
				TreeItem subsubsubItem = new TreeItem(subsubItem, 0);
				subsubsubItem.setText(" [" + annotation.getChangeType() + "] " + " Annotation: " + annotation.getSignature());
				subsubsubItem.setData(annotation);

				/* ********* contrast field conflict ******** */
				contrastConflicts(annotation, subsubsubItem);
			}
			
			for (ExceptionChangeLog exception : method.getExceptions()) {	
				TreeItem subsubsubItem = new TreeItem(subsubItem, 0);
				subsubsubItem.setText(" [" + exception.getChangeType() + "] " + " Exception: " + exception.getSignature());
				subsubsubItem.setData(exception);

				/* ********* contrast field conflict ******** */
				contrastConflicts(exception, subsubsubItem);
			}
			
			
			for (CodePieceChangeLog codePiece : method.getCodepieces()) {
				TreeItem subsubsubItem = new TreeItem(subsubItem, 0);
				subsubsubItem.setText(" [" + codePiece.getChangeType() + "] " + codePiece.printChangesLocation() + " Code Piece: " + codePiece.getSignature());
				subsubsubItem.setData(codePiece);

				/* ********* contrast codePiece conflict ******** */
				contrastConflicts(codePiece, subsubsubItem);

			}
		}
	}
	
	
	
	
	/**
	 * This method create a PopUpMenu over the ChangeLogHistory tree, to see the conflicts of a change log
	 * 
	 * @param productLineConflictModel
	 * @param tree
	 */
	private void createPopUpMenu(final ChangeLogHistory changeLogHistory, final Tree tree) {

		final Menu treeMenu = new Menu(showEvolutionsUI.getShell(), SWT.POP_UP);

		/* ****
		 * Make the user select one change log to see conflicts ***
		 */
		tree.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				tree.setMenu(treeMenu);
			}
		});

		
		////////////////////////////////////////////////////////////////////////
		
		MenuItem itemMenuViewConflicts = new MenuItem(treeMenu, SWT.PUSH);
		itemMenuViewConflicts.setText("View Conflicts");

		itemMenuViewConflicts.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				TreeItem[] treeItemsSelected = tree.getSelection();
				AssetChangeLog selectedAsset = (AssetChangeLog) treeItemsSelected[0].getData();
				ShowConflictUI ui = new ShowConflictUI(showEvolutionsUI.getShell());
				ui.showAssetsInConflict(selectedAsset);	
			}
		});
		
		
		
		

		////////////////////////////////////////////////////////////////////////
		
		MenuItem itemMenuViewEvolutionInformations = new MenuItem(treeMenu, SWT.PUSH);
		itemMenuViewEvolutionInformations.setText("View Informations about Evolution");

		itemMenuViewEvolutionInformations.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				TreeItem[] treeItemsSelected = tree.getSelection();

				if (treeItemsSelected.length <= 0 || treeItemsSelected.length > 1) {
					MessageDialog.openError(showEvolutionsUI.getShell(), "Error", "Select just one Change Log!");
				} else {
					
					ChangeLog changeLogTree = null;
					
					if(treeItemsSelected[0].getData() instanceof ChangeLog){
						changeLogTree = (ChangeLog) treeItemsSelected[0].getData();
					}else{
						if(treeItemsSelected[0].getData() instanceof ClassChangeLog){
							changeLogTree = ((ClassChangeLog) treeItemsSelected[0].getData()).getChangelog();
						}else{
							if(treeItemsSelected[0].getData() instanceof MethodChangeLog){
								changeLogTree = ((MethodChangeLog) treeItemsSelected[0].getData()).getClassChangeLog().getChangelog();
							}else{
								if(treeItemsSelected[0].getData() instanceof FieldChangeLog){
									changeLogTree = ((FieldChangeLog) treeItemsSelected[0].getData()).getClassChangeLog().getChangelog();
								}
							}
							
						}
					}
					
					if(changeLogTree != null ){
						
						StringBuilder builder = new StringBuilder();
						builder.append(changeLogTree.getChangeInformation() );
						builder.append(" \n\n\n Published at version: "+changeLogTree.getVersion() );
						// MELHORAR USABILIDADE: adicionar ao change log quem fez a mudança e em que data
						// nao colocar agora para nao atrapalhar os estudos
						builder.append(" \n\n By: "+    " ?????? ???? ???? ?????"  +"  On: "+"??/??/????" );
						
						builder.append("\n\n With Revisions: \n");
						List<Integer> revisonNumbers = new ArrayList<Integer>();
						
						boolean fisrt = true;
						for(ClassChangeLog _class : changeLogTree.getClasses() ){
							String revisionNumber = _class.getRevision();
							if(! revisonNumbers.contains( Integer.parseInt( revisionNumber ) )){
								if(fisrt){
									builder.append(revisionNumber);
									fisrt = false;
								}else{
									builder.append(", "+revisionNumber);
								}
								revisonNumbers.add(Integer.parseInt(revisionNumber ));
							}
						}
						
						MessageDialog.openInformation(showEvolutionsUI.getShell(), "Informations about Evolution", builder.toString() );
						
					}else
						GUIUtils.addErrorMessage("Click on the ChangeLog");
				}
			}
		});
		
		
		MenuItem itemMenuExpandAll = new MenuItem(treeMenu, SWT.PUSH);
		itemMenuExpandAll.setText("Expand All Itens [+] ");

		itemMenuExpandAll.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TreeItem[] treeItemsSelected = tree.getSelection();
				expandAllItems(treeItemsSelected);
			}
		});
		
		MenuItem itemMenuCollapseAll = new MenuItem(treeMenu, SWT.PUSH);
		itemMenuCollapseAll.setText("Collapse All Itens [-]");

		itemMenuCollapseAll.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TreeItem[] treeItemsSelected = tree.getSelection();
				collapseAllItems(treeItemsSelected);
			}
		});
		
		// This Group task does not work very well
//		MenuItem itemMenuGroupChangelogEvolution = new MenuItem(treeMenu, SWT.PUSH);
//		itemMenuGroupChangelogEvolution.setText("Group Changelog Evolution");
//
//		
//		itemMenuGroupChangelogEvolution.addListener(SWT.Selection, new Listener() {
//			public void handleEvent(Event e) {
//
//				changeLogHistorySource = ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().readHistoryChangeLogFile(new File(getWorkDirectoryPath() + "/" + PluginConstants.HISTORY_CHANGE_LOG_SOURCE_FILE));
//				changeLogHistoryTarget = ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().readHistoryChangeLogFile(new File(getWorkDirectoryPath() + "/" + PluginConstants.HISTORY_CHANGE_LOG_TARGET_FILE));
//
//				new ExecuteChangelogGroup().group(changeLogHistorySource);
//				new ExecuteChangelogGroup().group(changeLogHistoryTarget);
//				
//				showEvolutionsAnalisis(changeLogHistorySource, changeLogHistoryTarget, null);
//			}
//		});

	}
	
	
	
	
	
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	/**
	 * <p>Builder the list of conflicts to the user GUI grouped by the module and the place where it has happened.</p>
	 * 
	 * @param shell
	 * @param listOfConflicts
	 */
	public void showListOfConflictsGrouped(final TabFolder tabFolder, final TabItem tabItem, ChangeLogHistory changeLogHistory ) {

		if(changeLogHistory == null) return;
		
		Tree tree = new Tree(tabFolder, SWT.BORDER);
		
		Map<String,List<ChangeLog>> coreConflicts = filterCoreConflicts( changeLogHistory );
		
		TreeItem root = new TreeItem(tree, SWT.CHECK);
		
		List<String> modules = new ArrayList<String>();
		modules.addAll(coreConflicts.keySet());
		Collections.sort(modules );
		
		int total = 0;
	
		for ( String module  :  modules ) {
			
			TreeItem item = new TreeItem(root, SWT.BALLOON);
			item.setText( module + "(" + coreConflicts.get(module).size() + ")");
			
			showChangeLogTree( coreConflicts.get(module), item);
			total += coreConflicts.get(module).size();
			
		}
		
		root.setText( "CONFLICTS IN CORE (" + total + ")" );
		
		Map<Variability,List<ChangeLog>> variabilityConflictsMap = filterVariabilityConflicts( changeLogHistory );
		
		TreeItem variabilityRoot = new TreeItem(tree, SWT.CHECK);
		variabilityRoot.setText( "VARIABILITY CONFLICTS: " );

		for ( Variability variability : variabilityConflictsMap.keySet() ) {
			
			TreeItem item = new TreeItem(variabilityRoot, SWT.BALLOON);
			root.setText( variability.getId() + ": " + variability.getDescription() );
			
			showChangeLogTree( variabilityConflictsMap.get(variability), item);
			
		}
		
		tabItem.setControl(tree);

	}
	
	
	
	/** Used by showListOfConflictsGrouped */
	private void showChangeLogTree(List<ChangeLog> conflicts, TreeItem root) {
		
		for (ChangeLog changeLog : conflicts) {
			
			TreeItem item = new TreeItem(root, 0);

			item.setText(changeLog.getModule() + "[" + changeLog.getType() + "] " + "#" + changeLog.getIdentify() + " - " + changeLog.getDescription());
			item.setData(changeLog);

			/* ********* contrast changeLog conflict ******** */
			contrastConflicts(changeLog, item);

			for (ClassChangeLog classChangeLog : changeLog.getClasses()) {

				TreeItem subItem = new TreeItem(item, 0);
				subItem.setText(" [" + classChangeLog.getChangeType() + "] " + "Class: " + classChangeLog.getPath());
				subItem.setData(classChangeLog);

				/* ********* contrast class conflict ******** */
				contrastConflicts(classChangeLog, subItem);
				
				for (FieldChangeLog field : classChangeLog.getFields()) {
					TreeItem subsubItem = new TreeItem(subItem, 0);
					subsubItem.setText(" [" + field.getChangeType() + "] " + "Field: " + field.getSignature());
					subsubItem.setData(field);

					/* ********* contrast field conflict ******** */
					contrastConflicts(field, subsubItem);
					
					if ( field.getDirectConflicts() != null && field.getDirectConflicts().size() > 0 ) {
						
						TreeItem directConflicts = new TreeItem(subsubItem, 0);
						directConflicts.setText("DIRECT CONFLICTS (" + field.getDirectConflicts().size() + ")" );
						
						for (AssetChangeLog asset : field.getDirectConflicts() ) {
							
							TreeItem subsubsubItem = new TreeItem(directConflicts, 0);
							subsubsubItem.setText( asset.toString() );
						
						}
						
					}
					
					if ( field.getIndirectConflicts() != null && field.getIndirectConflicts().size() > 0 ) {
						
						TreeItem indirectConflicts = new TreeItem(subsubItem, 0);
						indirectConflicts.setText("INDIRECT CONFLICTS (" + field.getIndirectConflicts().size() + ")");
						
						for (AssetChangeLog asset : field.getIndirectConflicts() ) {
							
							TreeItem subsubsubItem = new TreeItem(indirectConflicts, 0);
							subsubsubItem.setText( asset.toString() );
						

						}
						
					}

				}

				for (MethodChangeLog method : classChangeLog.getMethods()) {
					TreeItem subsubItem = new TreeItem(subItem, 0);
					subsubItem.setText(" [" + method.getChangeType() + "] " + "Method: " + method.getSignature());
					subsubItem.setData(method);

					/* ********* contrast method conflict ******** */
					contrastConflicts(method, subsubItem);

					
					if ( method.getDirectConflicts() != null && method.getDirectConflicts().size() > 0 ) {
						
						TreeItem directConflicts = new TreeItem(subsubItem, 0);
						directConflicts.setText("DIRECT CONFLICTS (" + method.getDirectConflicts().size() + ")" );
						
						for (AssetChangeLog asset : method.getDirectConflicts() ) {
							
							TreeItem subsubsubItem = new TreeItem(directConflicts, 0);
							subsubsubItem.setText( asset.toString() );
						
						}
						
					}
					
					if ( method.getIndirectConflicts() != null && method.getIndirectConflicts().size() > 0 ) {
						
						TreeItem indirectConflicts = new TreeItem(subsubItem, 0);
						indirectConflicts.setText("INDIRECT CONFLICTS (" + method.getIndirectConflicts().size() + ")");
						
						for (AssetChangeLog asset : method.getIndirectConflicts() ) {
							
							TreeItem subsubsubItem = new TreeItem(indirectConflicts, 0);
							subsubsubItem.setText( asset.toString() );
						

						}
						
					}
					
				}
				
				if ( classChangeLog.getTextualConflicts() != null && classChangeLog.getTextualConflicts().size() > 0 ) {
					
					TreeItem textualConflicts = new TreeItem(subItem, 0);
					textualConflicts.setText("PSEUDOS CONFLICTS (" + classChangeLog.getTextualConflicts().size() + ")");
					
					for (AssetChangeLog asset : classChangeLog.getTextualConflicts() ) {
						
						TreeItem subsubsubItem = new TreeItem(textualConflicts, 0);
						subsubsubItem.setText( asset.toString() );
					
					}
					
				}
				
				
			}
		}
		
	}
	
	
	
	
	
	


//	/**
//	 * Show default tree visualization
//	 * 
//	 * @param feature
//	 * @param root
//	 */
//	public void showModuleTreeView(FeatureChangeLog feature, TreeItem root) {
//
//		Map<String, List<ChangeLog>> groupByModule = new HashMap<String, List<ChangeLog>>();
//
//		for (ChangeLog changeLog : feature.getChangelogs()) {
//
//			List<ChangeLog> listModule = groupByModule.get(changeLog.getModule());
//			if (listModule == null) {
//				listModule = new ArrayList<ChangeLog>();
//				groupByModule.put(changeLog.getModule(), listModule);
//			}
//
//			listModule.add(changeLog);
//
//		}
//
//		for (String module : groupByModule.keySet()) {
//
//			TreeItem moduleNode = new TreeItem(root, 0);
//			
//			if(StringUtils.isEmpty(module) )
//				moduleNode.setText("INDEFINITE");
//			else
//				moduleNode.setText(module);
//
//			for (ChangeLog changeLog : groupByModule.get(module)) {
//
//				TreeItem item = new TreeItem(moduleNode, 0);
//
//				item.setText(changeLog.getFullDescription());
//				item.setData(changeLog);
//
//				/* ********* contrast changeLog conflict ******** */
//				contrastConflicts(changeLog, item);
//
//				for (ClassChangeLog classChangeLog : changeLog.getClasses()) {
//
//					loadTreeOfConflitcsForClassChangeLog(item, classChangeLog);
//				}
//
//			}
//
//		}
//
//	}

	
	
	
	
	
	
	/**
	 * <p> Return new List of conflicting Changelog associated with the core feature </p>
	 * @param core
	 * @param feature
	 */
	private Map<String,List<ChangeLog>> filterCoreConflicts( ChangeLogHistory history ) {
		
		Map<String,List<ChangeLog>> result = new HashMap<String, List<ChangeLog>>();
		
		for ( FeatureChangeLog feature :  history.getFeatures() ) {
			for (ChangeLog changeLog : feature.getChangelogs()) {
				if ( changeLog.isConflicting() ) {
					if ( changeLog.getVariationPoint() == null ) {
						List<ChangeLog> listVar = result.get( changeLog.getModule() );
						if ( listVar == null ) {
							listVar = new ArrayList<ChangeLog>();
							result.put( changeLog.getModule() , listVar);
						}
						listVar.add(changeLog);
					}
				}
			}
		}
	
		return result;
	}
	
	
	
	/**
	 * <p>Return new List of conflicting Changelog associated with the some feature (variability) in the system.</p>
	 * 
	 * @param feature
	 * @return
	 */
	private Map<Variability,List<ChangeLog>> filterVariabilityConflicts( ChangeLogHistory history ) {
		
		Map<Variability,List<ChangeLog>> result = new HashMap<Variability,List<ChangeLog>>();
		
		for ( FeatureChangeLog feature :  history.getFeatures() ) {
			for (ChangeLog changeLog : feature.getChangelogs()) {
				if ( changeLog.isConflicting() ) {
					if ( changeLog.getVariationPoint() != null ) {
						List<ChangeLog> listVar = result.get( changeLog.getVariationPoint().getId() );
						if ( listVar == null ) {
							listVar = new ArrayList<ChangeLog>();
							result.put(changeLog.getVariationPoint() , listVar);
						}
						listVar.add(changeLog);
					}
				}
			}
		}
		return result;
	}
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	/**
	 * Show information in the conflict model to the user (shows who is conflicting with how).
	 * 
	 * @param shell
	 * @param listOfConflicts
	 */
	public void showConflictModel(final TabFolder tabFolder, final TabItem tabItem, ConflictModel conflictModel) {
		
		if(conflictModel == null) return;

		Tree tree = new Tree(tabFolder, SWT.BORDER);

		
		List<Conflict> directs = conflictModel.getConflictsOfCertainType(ConflictType.DIRECT);
		
		List<Conflict> indirectsByEvolution = conflictModel.getConflictsOfCertainType(ConflictType.INDIRECT, ConflictSubType.INDIRECT_BY_EVOLUTION);
		
		List<Conflict> indirectsByRelation = conflictModel.getConflictsOfCertainType(ConflictType.INDIRECT, ConflictSubType.INDIRECT_BY_RELATIONSHIP);
		
		List<Conflict> pseudos = conflictModel.getConflictsOfCertainType(ConflictType.PSEUDO);
		
		
		buildSubTreeOfConflicts(tree, directs, ConflictType.DIRECT, null, SWT.COLOR_RED);
		
		buildSubTreeOfConflicts(tree, indirectsByEvolution, ConflictType.INDIRECT, ConflictSubType.INDIRECT_BY_EVOLUTION, SWT.COLOR_DARK_YELLOW);
		
		buildSubTreeOfConflicts(tree, indirectsByRelation, ConflictType.INDIRECT, ConflictSubType.INDIRECT_BY_RELATIONSHIP, SWT.COLOR_DARK_YELLOW);
		
		buildSubTreeOfConflicts(tree, pseudos, ConflictType.PSEUDO, null, SWT.COLOR_DARK_GREEN);
		
		
		tabItem.setControl(tree);

	}


	/**
	 * BUild the subTree of conflicts
	 * <p> Created at:  01/04/2015  </p>
	 *
	 * @param tree
	 * @param directs
	 */
	private void buildSubTreeOfConflicts(Tree tree, List<Conflict> conflicts, ConflictType conflictType, ConflictSubType conflictSubType, final int COLOR) {
		TreeItem itemLabel1 = new TreeItem(tree, 0);
		
		itemLabel1.setText(conflictType.toString()+ (conflictSubType != null ? " ["+ conflictSubType.toString() +" ]" : " ") +" ( "+conflicts.size()+" )" );
		itemLabel1.setData(conflictType);
		
		for (Conflict conflict : conflicts) {
					
			TreeItem item = new TreeItem(tree, 0);

			item.setText(conflict.getAssetConflict().toString());
			item.setData(conflict);
			item.setForeground( showEvolutionsUI.getDisplay().getSystemColor(COLOR));

			TreeItem subLabel = new TreeItem(item, 0);

			subLabel.setText("IN DIRECT CONFLICT WITH ("+conflict.getTargetConflictsRelated().size()+"): ");
			subLabel.setData(null);
			
			for (AssetConflict assetConflict : conflict.getTargetConflictsRelated()) {

				TreeItem subItem = new TreeItem(item, 0);
				subItem.setText(assetConflict.toString());
				subItem.setData(assetConflict);
				subItem.setForeground( showEvolutionsUI.getDisplay().getSystemColor(COLOR));
			}
		}
	}
	
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	/**
	 * <p>
	 * Contrast the conflicts in the tree to the user (put colors: red, yellow,
	 * green, purple, etc)
	 * </p>
	 * 
	 * <p>
	 * Created at: 16/10/2013
	 * </p>
	 * 
	 * @param feature
	 * @param root
	 * @param conflictImage
	 * @param systemColor
	 */
	private void contrastConflicts(AssetChangeLog asset, TreeItem root) {
		
		if (asset.isDirectlyConflicting()) {
			root.setImage(GUIUtils.drawDirectConflictImage());
			root.setForeground( showEvolutionsUI.getDisplay().getSystemColor(SWT.COLOR_RED));
		} else { // just contrast other if doesn't have direct conflicts
			if (asset.isIndirectlyConflicting()) {
				root.setImage(GUIUtils.drawIndirectConflictImage());
				root.setForeground( showEvolutionsUI.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW));
			} else { // just contrast other if doesn't have indirect conflicts
				if (asset.isTextualConflicting()) {
					root.setImage(GUIUtils.drawPseudoConflictImage());
					root.setForeground( showEvolutionsUI.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
				}else{
					root.setImage(GUIUtils.drawBlankImage()); // just to align
				}
			}
		}

	}
	
	
	/**
	 * Expand all tree item (information about evolution) under the item selected
	 * @param treeItemsSelected
	 */
	public void expandAllItems(TreeItem[] treeItemsSelected ){
		if (treeItemsSelected != null) {
			for (TreeItem treeItem : treeItemsSelected) {
				treeItem.setExpanded(true);
				expandAllItems(treeItem.getItems());
			}
		}
	}
	
	/**
	 * Collapse all tree item (information about evolution) under the item selected
	 * @param treeItemsSelected
	 */
	public void collapseAllItems(TreeItem[] treeItemsSelected ){
		if (treeItemsSelected != null) {
			for (TreeItem treeItem : treeItemsSelected) {
				treeItem.setExpanded(false);
				collapseAllItems(treeItem.getItems());
			}
		}
	}
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * <p>
	 * Builder the tree that show the dependences between change logs. We can only apply on change if the previous change log was applied 
	 * </p>
	 * 
	 * @param shell
	 * @param listOfConflicts
	 */
	public void showDependenceTree(final TabFolder tabFolder, final TabItem tabItem, final DependenceModel dependenceMap) {

		if(dependenceMap == null) return;
		
		int style = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;

		final Tree tree = new Tree(tabFolder, style);

		for (Dependence dependence : dependenceMap.getDepedencesMap() ) {

			TreeItem root = new TreeItem(tree, SWT.CHECK);
			root.setText(dependence.getKeyChangeLog());

			root.setData(dependence.getKeyChangeLog());
			
			for (AssetDependence assetDepenedence : dependence.getAssetDependents()) {

				TreeItem item = new TreeItem(root, 0);

				item.setText(assetDepenedence.getChangeLogDependentKey());
				item.setData(assetDepenedence.getChangeLogDependentKey());
				
				for (String asset : assetDepenedence.getChangeLogsDependents()) {
					
					TreeItem subItem = new TreeItem(item, 0);

					subItem.setText(asset);
					subItem.setData(asset);
				}
			}

		}

		// add the tree to the tab
		tabItem.setControl(tree);

	}
	
	
	

}
