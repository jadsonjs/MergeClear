/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.gui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.util.ChangeLogHistoryUtil;
import br.ufrn.spl.ev.util.GUIUtils;

/**
 * <p>GUI the start the process of analyze if the artifacts chosen by the user can be save applied.</p>
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class AnalyzeIndirectConflictsUI {

	/** class represents a window */
	private Shell shell;
	
	/** class is responsible for managing event loops, for controlling the communication between 
	 * the UI thread and other threads and for managing fonts and colors */
	private Display display; 
	
	
	/* keep a reference to the main UI */
	private ShowEvolutionsUI showEvolutionsUI;
	
	/**
	 * Constructor of the class: builder the GUI with the list of conflicts 
	 * 
	 * @param listOfConflicts
	 */
	public AnalyzeIndirectConflictsUI(ShowEvolutionsUI showEvolutionsUI, ChangeLogHistory changeLogHistorySource) {
		
		ChangeLogHistoryUtil.printChangeLog(changeLogHistorySource, true, false);
		
		this.showEvolutionsUI = showEvolutionsUI;
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		display = workbench.getDisplay();
		shell = new Shell (display);
		
		initComponents();
	}

	
//	private void printIndirectConflits(Map<AssetChangeLog, List<AssetChangeLog>> assetsConflicts) {
//		for (AssetChangeLog assetChangeLog : assetsConflicts.keySet()) {
//			System.out.println(assetChangeLog.getFullName() +" in conflict with: ");
//			List<AssetChangeLog> assetsConflict = assetsConflicts.get(assetChangeLog);
//			if(assetsConflict.size() == 0){
//				System.out.println("There is not indirect conflict");
//			}else{
//				for (AssetChangeLog assetConflict : assetsConflict) {
//					System.out.println("\t"+assetConflict.getFullName());
//				} 
//			}
//			
//		}
//	}
	

	/** 
	 * Initializes the components of GUI
	 */
	public void initComponents() {
		
		// Create the Layout
		GridLayout gridLayout = new GridLayout(5, true);
		gridLayout.horizontalSpacing = 4;
		gridLayout.verticalSpacing = 4;
		gridLayout.marginBottom = 5;
		gridLayout.marginTop = 5;
		
        GridData gdHorizontalSpan2 = new GridData(SWT.FILL, SWT.FILL, false, false);
        gdHorizontalSpan2.widthHint = 250;
        gdHorizontalSpan2.heightHint = 20;
        gdHorizontalSpan2.horizontalSpan = 2;
        
		
		Button returnEvolutionUIButton = new Button(shell, SWT.PUSH);
		returnEvolutionUIButton.setText("<< Return ");
		
		returnEvolutionUIButton.setLayoutData(gdHorizontalSpan2);
        
        shell.setLayout(gridLayout);
		
		returnEvolutionUIButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	AnalyzeIndirectConflictsUI.this.closeUI();
            	ShowEvolutionsUI evolutionUI = new ShowEvolutionsUI();	
        		evolutionUI.openUI();
        		return;
            }
        });
		
	}
	
	public void openUI(){
		if(shell != null){
			shell.setSize(1200, 800);
		    GUIUtils.centerShell(shell);
		    shell.setMaximized(true);
			shell.open();    
		}
	}
	
	public void showUI(){
		if(shell != null){
			shell.setVisible(true);
		}
	}
	
	public void hideUI(){
		if(shell != null){
			shell.setVisible(false);
			showEvolutionsUI.openUI();
		}
	}
	
	public void closeUI(){
		if(shell != null){
			shell.dispose();
			shell.close();    
		}
	}
	
	public boolean isCreated(){
		return shell != null && ! shell.isDisposed();
	}
}
