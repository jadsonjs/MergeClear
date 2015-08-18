/**
 *
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE �- UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA -� DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 *
 */
package br.ufrn.spl.ev.gui.swt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import br.ufrn.spl.ev.PluginConstants;
import br.ufrn.spl.ev.engines.conflicts.indirect.IndirectConflictsAnalysisStrategyFactory;
import br.ufrn.spl.ev.engines.conflicts.indirect.IndirectConflictsAnalysisStrategyFactory.INDIRECT_ANALYSIS_TYPE;
import br.ufrn.spl.ev.engines.conflicts.indirect.IndirectConflictsDetection;
import br.ufrn.spl.ev.engines.conflicts.indirect.IndirectConflictsEvolutionDetection;
import br.ufrn.spl.ev.engines.conflicts.statistics.ChangeLogHistoryStatistics;
import br.ufrn.spl.ev.engines.conflicts.statistics.ConflictStatistics;
import br.ufrn.spl.ev.engines.conflicts.statistics.DependenceStatistics;
import br.ufrn.spl.ev.engines.conflicts.statistics.GroupingAndSortingTasks;
import br.ufrn.spl.ev.engines.conflicts.statistics.StatisticsFactory;
import br.ufrn.spl.ev.engines.dependence.DependenceAnalysisStrategy;
import br.ufrn.spl.ev.engines.dependence.DependenceAnalysisStrategyFactory;
import br.ufrn.spl.ev.engines.merge.CalculateIntegrationIndex;
import br.ufrn.spl.ev.engines.merge.engine.MergeEngine;
import br.ufrn.spl.ev.engines.miners.Miner;
import br.ufrn.spl.ev.engines.miners.MinerFactory;
import br.ufrn.spl.ev.engines.miners.MinerFactory.MergeSide;
import br.ufrn.spl.ev.gui.managers.AnalyzeDirectConflictsManager;
import br.ufrn.spl.ev.gui.managers.AnalyzeIndirectConflictsManager;
import br.ufrn.spl.ev.gui.managers.AnalyzePseudosConflictsManager;
import br.ufrn.spl.ev.gui.managers.ClearConflictsManager;
import br.ufrn.spl.ev.gui.managers.ExtractEvolutionInformationManager;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;
import br.ufrn.spl.ev.models.dependencemodel.DependenceModel;
import br.ufrn.spl.ev.parsers.ChangeLogHistoryModelParserFactory;
import br.ufrn.spl.ev.parsers.ConflictModelParserFactory;
import br.ufrn.spl.ev.parsers.DependenceModelParserFactory;
import br.ufrn.spl.ev.util.ConfigUtils;
import br.ufrn.spl.ev.util.GUIUtils;

/**
 * <p> The main UI of the application, show the evolutions, conflicts and help the
 * user to solve them. </p>
 * 
 * @author jadson - jadson@info.ufrn.br
 * @author Gleydson - gleydson@esig.com.br
 * 
 * @since 19/04/2013
 * @version 1.0 - Class Creation
 * @version 2.0 - jadson - Make a better organization of the class source code
 */
public class ShowEvolutionsUI {

	/* class represents a window */
	private Shell shell;

	/*
	 * class is responsible for managing event loops, for controlling the
	 * communication between the UI thread and other threads and for managing
	 * fonts and colors
	 */
	private Display display;

	/* A main tab to show the evolution. Contains several tabItens to show information about evolution */
	private TabFolder tabFolder;

	/*
	 * Shows the evolution in the source side, that are the evolution we are
	 * interested in apply
	 */
	private TabItem sourceEvolutionsTabItem;

	/* Shows the evolution in the target side */
	private TabItem targetEvolutionsTabItem;

	/* Shows the conflicts between the two sides of evolution */
	private TabItem conflictsTabItem;
	
	/* Shows the conflicts between the two sides of evolution */
	private TabItem conflictsTabItemPair;
	
	/* Shows the dependence between changeLog ( tasks ) for the source*/
	private TabItem dependenceTabItem;

	/* Label to show the merge level */
	private Label facilityMergeLevelLabel;
	
	/*
	 * The path choose the the user where the plugin will work (read and write
	 * the evolution files). Always end with '/' character
	 */
	private String workDirectoryPath = ConfigUtils.getConfiguration(PluginConstants.DEFAULT_WORK_DIRECTORY);

	/* ************************************************************
	 * Keep references to the information about evolution generated in memory.
	 * Theses information is passed to all modules of the tool.
	 */
	private ChangeLogHistory changeLogHistorySource; // changes on source

	private ChangeLogHistory changeLogHistoryTarget; // changes on target


	/* ************************************************************ */

	
	/* Keep Information about conflicts that we have find*/
	private ConflictModel conflictModel;
	
	
	
	/* ************************************************************
	 * Keep references to the information about dependence between tasks.
	 */
	private DependenceModel sourceDependenceModel;
	
	private DependenceModel targetDependenceModel;
	
	
	/* ************************************************************ */
	
	
	/* The sides that we have to execute the evolution process */
	private enum EvolutionSide{SOURCE, TARGET, BOTH};
	
	
	/* By default we will execute in the both side  */
	private EvolutionSide evolutionSide = EvolutionSide.BOTH;
	
	private Miner minerSource = null;
	private Miner minerTarget = null;
		
	
	/**
	 * Constructor of the class: builder the GUI with the list of conflicts
	 * 
	 * @param listOfConflicts
	 */
	public ShowEvolutionsUI() {

		IWorkbench workbench = PlatformUI.getWorkbench();

		display = workbench.getDisplay();
		shell = new Shell(display);

		initComponents();

		// test if workDirectPath end with  "/"
		if ( !workDirectoryPath.endsWith("/") ) 
			workDirectoryPath = workDirectoryPath + "/";
		
	}

	/**
	 * <p>
	 * Initializes the components and the actions of GUI
	 * </p>
	 * 
	 * <p>
	 * <i>Here is the method where all action start, so to understand the code,
	 * you should start the debug from here</i>
	 * </p>
	 */
	public void initComponents() {

		// Create the Layout
		GridLayout gridLayout = new GridLayout(12, false);
		gridLayout.horizontalSpacing = 4;
		gridLayout.verticalSpacing = 4;
		gridLayout.marginBottom = 5;
		gridLayout.marginTop = 5;

		GridData gdHorizontalSpan1 = new GridData(SWT.LEFT, SWT.FILL, false, false);
		gdHorizontalSpan1.widthHint = 60;
		gdHorizontalSpan1.heightHint = 20;
		gdHorizontalSpan1.horizontalSpan = 1;
		
		GridData gdHorizontalSpan2 = new GridData(SWT.FILL, SWT.FILL, false, false);
		gdHorizontalSpan2.widthHint = 200;
		gdHorizontalSpan2.heightHint = 20;
		gdHorizontalSpan2.horizontalSpan = 2;

		GridData gdHorizontalSpan3 = new GridData(SWT.FILL, SWT.FILL, false, false);
		gdHorizontalSpan3.widthHint = 400;
		gdHorizontalSpan3.heightHint = 20;
		gdHorizontalSpan3.horizontalSpan = 3;

		GridData gdHorizontalSpan12 = new GridData(SWT.FILL, SWT.FILL, false, false);
		gdHorizontalSpan12.horizontalSpan = 12;
		gdHorizontalSpan12.grabExcessHorizontalSpace = true; // stretches on horizontal
		gdHorizontalSpan12.grabExcessVerticalSpace = true; // stretches on vertical

		GridData gdHorizontalSpan12_2 = new GridData(SWT.FILL, SWT.FILL, false, false);
		gdHorizontalSpan12_2.horizontalSpan = 12;
		gdHorizontalSpan12_2.verticalSpan = 1;

		
		
		// //////////////////Create the elements from the Screen // ////////////////

		///////  (have to be in the order that will appear in the shell)  /////////
		
		ToolBar toolBar = new ToolBar (shell, SWT.FLAT | SWT.BORDER | SWT.HORIZONTAL);
		toolBar.pack ();
			
		final Label filesDirectoryLabel = new Label(shell, SWT.BORDER);
		filesDirectoryLabel.setText(workDirectoryPath);

		Button chooseFilesDirectory = new Button(shell, SWT.PUSH);
		chooseFilesDirectory.setText("Choose Files Directory");

		Button laodButton = new Button(shell, SWT.PUSH);
		laodButton.setText("Load Evolution Information");

		Button executeNewEvolutionAnalisis = new Button(shell, SWT.PUSH);
		executeNewEvolutionAnalisis.setText("Execute new Evolution Analisis");

		Button buttonExecuteSourceSide = new Button (shell, SWT.RADIO);
		buttonExecuteSourceSide.setText ("Source");
		
		Button buttonExecuteTargetSide = new Button (shell, SWT.RADIO);
		buttonExecuteTargetSide.setText ("Target");
		
		Button buttonExecuteBothSide = new Button (shell, SWT.RADIO);
		buttonExecuteBothSide.setText ("Both");
		buttonExecuteBothSide.setSelection(true);
		
		tabFolder = new TabFolder(shell, SWT.BORDER);
		org.eclipse.swt.graphics.Rectangle clientArea = shell.getClientArea();
		tabFolder.setLocation(clientArea.x, clientArea.y);

		sourceEvolutionsTabItem = new TabItem(tabFolder, SWT.NONE);
		sourceEvolutionsTabItem.setText("Source Evolutions");

		targetEvolutionsTabItem = new TabItem(tabFolder, SWT.NONE);
		targetEvolutionsTabItem.setText("Target Evolutions");

		conflictsTabItemPair = new TabItem(tabFolder, SWT.NONE);
		conflictsTabItemPair.setText("Conflicts");
		
		conflictsTabItem = new TabItem(tabFolder, SWT.NONE);
		conflictsTabItem.setText("Grouped Conflicts");

		dependenceTabItem = new TabItem(tabFolder, SWT.NONE);
		dependenceTabItem.setText("Dependences");
		

		
		facilityMergeLevelLabel = new Label(shell, SWT.BORDER);
		facilityMergeLevelLabel.setForeground( display.getSystemColor(SWT.COLOR_WHITE) );
		facilityMergeLevelLabel.setBackground( display.getSystemColor(SWT.COLOR_GRAY) );
		
		
		// create in the end, to be positioned in the end of screen too //
		Button applySelectedChanges = new Button(shell, SWT.PUSH);
		applySelectedChanges.setText("Apply Selected Changes >> ");
		
		
		// a tool bar items to put actions
		
		ToolItem toolBarDirectConflitctItem = new ToolItem (toolBar, SWT.PUSH);
		toolBarDirectConflitctItem.setImage (GUIUtils.drawDirectConflictImage());
		toolBarDirectConflitctItem.setToolTipText("Direct Conflict Analysis");
		
		ToolItem toolBarInDirectConflitctItem = new ToolItem (toolBar, SWT.PUSH);
		toolBarInDirectConflitctItem.setImage (GUIUtils.drawIndirectConflictImage());
		toolBarInDirectConflitctItem.setToolTipText("Indirect Conflict Analysis");
		
		ToolItem toolBarPseudoConflitctItem = new ToolItem (toolBar, SWT.PUSH);
		toolBarPseudoConflitctItem.setImage (GUIUtils.drawPseudoConflictImage());
		toolBarPseudoConflitctItem.setToolTipText("Pseudo Conflict Analysis");
		
		ToolItem toolBarDependenceAnalyisItem = new ToolItem (toolBar, SWT.PUSH);
		toolBarDependenceAnalyisItem.setImage (GUIUtils.drawDependenceAnalysisImage());
		toolBarDependenceAnalyisItem.setToolTipText("Dependence Analysis");
		
		ToolItem toolBarStatisticsItem = new ToolItem (toolBar, SWT.PUSH);
		toolBarStatisticsItem.setImage (GUIUtils.drawStatisticAnalysisImage());
		toolBarStatisticsItem.setToolTipText("Statistics Analysis");
		
		ToolItem toolBarClearConflitctItem = new ToolItem (toolBar, SWT.PUSH);
		toolBarClearConflitctItem.setImage (GUIUtils.drawClearConflictImage());
		toolBarClearConflitctItem.setToolTipText("Clear Conflict Analysis");
		
		
		// //////////////// Add the events to the buttons ////////////////////

		
		// Change the work directory of the tool
		chooseFilesDirectory.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				display.syncExec(new Runnable() {
					public void run() {

						DirectoryDialog dialog = new DirectoryDialog(shell);
						String path = dialog.open();
						if (path != null) {
							workDirectoryPath = path.replace("\\", "/") + "/";
							filesDirectoryLabel.setText(workDirectoryPath);
						}
					}

				});
			}
		});

		// load information already save
		laodButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				///////////////////// Evolutions    ///////////////////////
				
				changeLogHistorySource = ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().readHistoryChangeLogFile(new File(getWorkDirectoryPath() + "/" + PluginConstants.HISTORY_CHANGE_LOG_SOURCE_FILE));
				changeLogHistoryTarget = ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().readHistoryChangeLogFile(new File(getWorkDirectoryPath() + "/" + PluginConstants.HISTORY_CHANGE_LOG_TARGET_FILE));

				// load and show the conflict model generated, if it exists //
				conflictModel = ConflictModelParserFactory.getConflictModelParser().readConflictModelFile(new File(getWorkDirectoryPath() + "/" + PluginConstants.CONFLICT_MODEL_FILE));
				
				//////////////////// Dependences /////////////////////////////
				
				sourceDependenceModel = DependenceModelParserFactory.getDependenceModelParser().readDependenceFile( new File( getWorkDirectoryPath()+PluginConstants.DEPENDENCE_ANALYSIS_SOURCE_FILE)   );
				targetDependenceModel = DependenceModelParserFactory.getDependenceModelParser().readDependenceFile( new File( getWorkDirectoryPath()+PluginConstants.DEPENDENCE_ANALYSIS_TARGET_FILE)   );
				
				showEvolutionsData(changeLogHistorySource, changeLogHistoryTarget, conflictModel, sourceDependenceModel);
				
				
			}
		});

		boolean systemOnLineModel = ConfigUtils.getConfiguration(PluginConstants.EXTRACT_EVOLUTION_MODEL).equals("ONLINE");
		boolean repositoryOnlineModel = ConfigUtils.getConfiguration(PluginConstants.INDIRECT_CONFLICT_MODEL).equals("ONLINE");
		
		
		minerSource = new MinerFactory().getMiner(ConfigUtils.getConfiguration(PluginConstants.SOURCE_MINER), getWorkDirectoryPath(), MergeSide.SOURCE, systemOnLineModel, repositoryOnlineModel);
		minerTarget = new MinerFactory().getMiner(ConfigUtils.getConfiguration(PluginConstants.TARGET_MINER), getWorkDirectoryPath(), MergeSide.TARGET, systemOnLineModel, repositoryOnlineModel);
		
		
		// executes a new evolution miner
		executeNewEvolutionAnalisis.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {

					new ExtractEvolutionInformationManager(ShowEvolutionsUI.this, 
							evolutionSide == EvolutionSide.SOURCE || evolutionSide == EvolutionSide.BOTH ? minerSource : null, 
							evolutionSide == EvolutionSide.TARGET || evolutionSide == EvolutionSide.BOTH ? minerTarget : null).executeEvolutionAnalisis();

					// if not execute the mine, just load the change log
					if(evolutionSide == EvolutionSide.TARGET ){
						changeLogHistorySource = ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().readHistoryChangeLogFile(new File(getWorkDirectoryPath() + "/" + PluginConstants.HISTORY_CHANGE_LOG_SOURCE_FILE));
					}
					if(evolutionSide == EvolutionSide.SOURCE){
						changeLogHistoryTarget = ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().readHistoryChangeLogFile(new File(getWorkDirectoryPath() + "/" + PluginConstants.HISTORY_CHANGE_LOG_TARGET_FILE));
					}
					

				} catch (Exception ex) {
					ex.printStackTrace();
					GUIUtils.addErrorMessage("Error when performing mining: " + ex.getMessage() + " " + ex.getCause());
				}
			}
		});
		
		
		buttonExecuteSourceSide.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				evolutionSide = EvolutionSide.SOURCE;
			}
		});
		
		buttonExecuteTargetSide.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				evolutionSide = EvolutionSide.TARGET;
			}
		});
		
		buttonExecuteBothSide.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				evolutionSide = EvolutionSide.BOTH;
			}
		});

		// executes the direct conflict analyzes
		toolBarDirectConflitctItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					
					conflictModel = new AnalyzeDirectConflictsManager(ShowEvolutionsUI.this).executeDiredConflictsAnalisis(changeLogHistorySource, changeLogHistoryTarget, conflictModel);

				} catch (Exception ex) {
					GUIUtils.addErrorMessage("Error when performing conflict analysis: " + ex.toString());
					ex.printStackTrace();
				}

			}
		});
		

		
		// executes the indirect conflict analyzes
		toolBarInDirectConflitctItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				Job job = new Job("Indirect Conflict") {
		  			@Override
		  			protected IStatus run(IProgressMonitor monitor) {
		  				
		  				monitor.beginTask("INDIRECT CONFLICT ANALYSIS", 100);

		  				//time consuming work here
		  				try {
							
							// types of indirectConflicts Detections, if we want to use more than one  //
							List<IndirectConflictsDetection> evolutions = new ArrayList<IndirectConflictsDetection>();
							
							IndirectConflictsEvolutionDetection evolutionDetection = new IndirectConflictsEvolutionDetection(
									ShowEvolutionsUI.this,
									monitor,
									IndirectConflictsAnalysisStrategyFactory.getIndirectAnalysisStrategy(
											ShowEvolutionsUI.this,
											INDIRECT_ANALYSIS_TYPE.valueOf(ConfigUtils.getConfiguration(PluginConstants.INDIRECT_ANALYSIS_TYPE)) ),  
											minerSource.getSystemConnector(),
											minerTarget.getSystemConnector(),
											minerSource.getRepositoryConnector(),
											minerTarget.getRepositoryConnector()
									);
							
//							IndirectConflictsRelationShipDetection relationDetection = new IndirectConflictsRelationShipDetection(
//									monitor,
//									IndirectConflictsAnalysisStrategyFactory.getIndirectAnalysisStrategy(
//											INDIRECT_ANALYSIS_TYPE.valueOf(ConfigUtils.getConfiguration(PluginConstants.INDIRECT_ANALYSIS_TYPE)) ),  
//									repositoryConnectorSource,
//									repositoryConnectorTarget
//									);
							
							// OTIMIZATION: copy the cache between indirect conflicts analysis
///							relationDetection.setCacheCallGraphic(evolutionDetection.getCacheCallGraphic());
							
							evolutions.add(evolutionDetection);
//							evolutions.add(relationDetection);  // have to be execute after the evolutionDetection
							
							conflictModel = new AnalyzeIndirectConflictsManager(ShowEvolutionsUI.this).
										executeIndiredConflictsAnalisis(changeLogHistorySource, changeLogHistoryTarget, conflictModel, 
												evolutions, Short.valueOf(ConfigUtils.getConfiguration(PluginConstants.DEPTH_ANALYSIS_LEVEL)));
						
		
						} catch (Exception ex) {
							GUIUtils.addErrorMessage("Error on indirect conflict analysis: "+ex.getMessage());
							monitor.done();
							return Status.CANCEL_STATUS;
						}
			
					
						monitor.done();
						return Status.OK_STATUS;
						
		  			} // run(IProgressMonitor monitor)

				}; // Job job = new Job("Indirect Conflict")
		
				job.setUser(true);
				job.schedule();
			}
		});

		// executes textual conflict analyzes
		toolBarPseudoConflitctItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {

					conflictModel = new AnalyzePseudosConflictsManager(ShowEvolutionsUI.this).executePseudosConflictsAnalisis(changeLogHistorySource, changeLogHistoryTarget, conflictModel);

				} catch (Exception ex) {
					GUIUtils.addErrorMessage("Error when performing conflict analysis: " + ex.toString());
					ex.printStackTrace();
				}

			}
		});
		
		// executes dependence analysis
		toolBarDependenceAnalyisItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {

					DependenceAnalysisStrategy strategy = DependenceAnalysisStrategyFactory.getDependenceStrategy();
					sourceDependenceModel =  strategy.buildMapOfDependences(changeLogHistorySource, new File( getWorkDirectoryPath()+PluginConstants.DEPENDENCE_ANALYSIS_SOURCE_FILE) );
					targetDependenceModel = strategy.buildMapOfDependences(changeLogHistoryTarget, new File( getWorkDirectoryPath()+PluginConstants.DEPENDENCE_ANALYSIS_TARGET_FILE) );

					// we just show to the user the source dependence that are the tasks he is interested in.
					new BuildEvolutionInformation().showDependenceTree(tabFolder, dependenceTabItem, sourceDependenceModel);
					
				} catch (Exception ex) {
					GUIUtils.addErrorMessage("Error when performing dependence analysis: " + ex.toString());
					ex.printStackTrace();
				}

			}
		});

		// executes statistic collection
		toolBarStatisticsItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				/*
				 * Collect statistic about evolution, this is to be executed just for our studies 
				 * Normally the final user won't need this information
				 */
				try {

					ChangeLogHistoryStatistics statistics = new StatisticsFactory().getStatisticsAboutEvolution();
					statistics.collectStatistics(changeLogHistorySource, changeLogHistoryTarget, getWorkDirectoryPath());

					DependenceStatistics statistics2 = new StatisticsFactory().getStatisticsAboutDependence();
					statistics2.collectStatistics(changeLogHistorySource, changeLogHistoryTarget, sourceDependenceModel, targetDependenceModel, getWorkDirectoryPath());
					
					ConflictStatistics statistics3 = new StatisticsFactory().getStatisticsAboutConflicts();
					statistics3.collectStatistics(conflictModel, getWorkDirectoryPath());
					
				} catch (Exception ex) {
					GUIUtils.addErrorMessage("Error when performing statistic collect: " + ex.toString());
					ex.printStackTrace();
				}
				
				new GroupingAndSortingTasks().groupAndSortingTasks(changeLogHistorySource,changeLogHistoryTarget, getWorkDirectoryPath());

			}
		});
		
		
		// reset all data about conflict save before //
		toolBarClearConflitctItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					new ClearConflictsManager(ShowEvolutionsUI.this).clearConflicts(changeLogHistorySource, changeLogHistoryTarget, conflictModel);
				} catch (Exception ex) {
					GUIUtils.addErrorMessage("Error when performing conflict analysis: " + ex.toString());
					ex.printStackTrace();
				}

			}
		});
		
		
		

		// executes the merge on source code of things the will not broke the
		// LPS
		applySelectedChanges.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					new MergeEngine(changeLogHistorySource, changeLogHistoryTarget).execute();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// Configuring layout elements 

		
		toolBar.setLayoutData(gdHorizontalSpan12_2);
		
		filesDirectoryLabel.setLayoutData(gdHorizontalSpan3);

		chooseFilesDirectory.setLayoutData(gdHorizontalSpan2);
		laodButton.setLayoutData(gdHorizontalSpan2);
		executeNewEvolutionAnalisis.setLayoutData(gdHorizontalSpan2);
		
		buttonExecuteSourceSide.setLayoutData(gdHorizontalSpan1);
		buttonExecuteTargetSide.setLayoutData(gdHorizontalSpan1);
		buttonExecuteBothSide.setLayoutData(gdHorizontalSpan1);

		tabFolder.setLayoutData(gdHorizontalSpan12);
		
		facilityMergeLevelLabel.setLayoutData(gdHorizontalSpan2);
		
		applySelectedChanges.setLayoutData(gdHorizontalSpan2);

		shell.setLayout(gridLayout);
	}

	
	/**
	 * This method show the information about evolution to the user in the GUI of the tool
	 * 
	 * @param changeLogHistorySource
	 * @param changeLogHistoryTarget
	 * @param conflictModel
	 * @param sourceDependenceModel
	 */
	public void showEvolutionsData(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget, ConflictModel conflictModel
			, DependenceModel sourceDependenceModel) {
		this.changeLogHistorySource = changeLogHistorySource;
		this.changeLogHistoryTarget = changeLogHistoryTarget;
		this.conflictModel = conflictModel;
		this.sourceDependenceModel = sourceDependenceModel;
		
		new BuildEvolutionInformation().showEvolutionsData(this, changeLogHistorySource, changeLogHistoryTarget, conflictModel, sourceDependenceModel);
		new CalculateIntegrationIndex().calculate(ShowEvolutionsUI.this, changeLogHistorySource, changeLogHistoryTarget, conflictModel, sourceDependenceModel);
	}
	
	
	public void openUI() {
		shell.setSize(1200, 800);
		GUIUtils.centerShell(shell);
		shell.setMaximized(true);
		shell.open();
	}

	public void showUI() {
		shell.setVisible(true);
	}

	public void hideUI() {
		shell.setVisible(false);
	}

	public void closeUI() {
		shell.dispose();
		shell.close();
	}

	public String getWorkDirectoryPath() {
		return workDirectoryPath;
	}

	public void setWorkDirectoryPath(String workDirectoryPath) {
		this.workDirectoryPath = workDirectoryPath;
	}

	public Shell getShell() {
		return shell;
	}

	public Display getDisplay() {
		return display;
	}

	public TabFolder getTabFolder() {
		return tabFolder;
	}

	public TabItem getSourceEvolutionsTabItem() {
		return sourceEvolutionsTabItem;
	}

	public TabItem getTargetEvolutionsTabItem() {
		return targetEvolutionsTabItem;
	}

	public TabItem getConflictsTabItem() {
		return conflictsTabItem;
	}

	public TabItem getConflictsTabItemPair() {
		return conflictsTabItemPair;
	}

	public Label getFacilityMergeLevelLabel() {
		return facilityMergeLevelLabel;
	}
	
	public TabItem getDependenceTabItem() {
		return dependenceTabItem;
	}
	
	

}