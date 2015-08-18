/**
 *
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE –- UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA -– DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 *
 */
package br.ufrn.spl.ev.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;

/**
 * helpful class to the GRI of plug-in
 *
 * @author jadson
 *
 * @since 31/03/2013
 * @version 1.0 - Class Creation
 */
public class GUIUtils {

	/* The image of elements clear conflict button */
	private static Image clearConflictImage;
	
	/* The image of elements the are in direct conflict */
	private static Image directConflictImage;
	
	/* The image of elements the are in indirect conflict */
	private static Image indirectConflictImage;
	
	/* The image of elements the are in textual conflict */
	private static Image pseudoConflictImage;
	
	/* The image of statistic analysis */
	private static Image statisticAnalysisImage;
	
	/* The blank image */
	private static Image blankImage;
	
	/* The image of safe template analysis */
	private static Image safeTemplateAnalysisImage;
	
	/* The image dependence analysis */
	private static Image dependenceAnalysisImage;
	
	
	/** Show a information message to the user */
	public static void addInformationMessage(final String informationMessage) {
		
		Display.getDefault().syncExec(new Runnable() {
		    public void run() {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Info", informationMessage);
		    }
		});
		
    }
	
	/** Show a warning message to the user */
	public static void addWarningMessage(final String warningMessage) {
		
		Display.getDefault().syncExec(new Runnable() {
		    public void run() {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), "Warning", warningMessage);
		    }
		});
    }
	
	/** Show a error message to the user */
	public static void addErrorMessage(final String errorMessage) {

		Display.getDefault().syncExec(new Runnable() {
		    public void run() {
				MessageDialog.openError(Display.getDefault().getActiveShell(), "Warning", errorMessage);
		    }
		});
    }
	
	/** Make the shell appears on the middle of screen*/
	public static void centerShell(Shell shell) {

        Rectangle bds = shell.getDisplay().getBounds();

        Point p = shell.getSize();

        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;

        shell.setBounds(nLeft, nTop, p.x, p.y);
    }
	
	
	/** Check all items of a tree that are on the way of the check item to the root (botton-up). */
	public static void checkPathTree(TreeItem parentItem, boolean checked, boolean grayed) {
	    if (parentItem == null) return;
	    if (grayed) {
	        checked = true;
	    } else {
	    	// verify all children, if all are checked, the father are checked, else the father are just grayed
	        int index = 0;
	        TreeItem[] items = parentItem.getItems();
	        while (index < items.length) {
	            TreeItem child = items[index];
	            if (child.getGrayed() || checked != child.getChecked()) {
	                checked = grayed = true;
	                break;
	            }
	            index++;
	        }
	    }
	    parentItem.setChecked(checked);
	    parentItem.setGrayed(grayed);
	    
	    
	    ((AssetChangeLog) parentItem.getData()).setSelected(checked); // select in the model
	    
	    checkPathTree(parentItem.getParentItem(), checked, grayed);
	}
	
	

	/** Check all sub items of the item Checked by the user (top-down) */
	public static void checkItemsTree(TreeItem item, boolean checked) {
	    item.setGrayed(false);
	    item.setChecked(checked);
	    ((AssetChangeLog) item.getData()).setSelected(checked); // select in the model too.
	    TreeItem[] items = item.getItems();
	    // check all children when select the father
	    for (int i = 0; i < items.length; i++) {
	        checkItemsTree(items[i], checked);
	    }
	}
	
	/** draw a red X */
	public static Image drawDirectConflictImage(){
		if(directConflictImage == null){
			try {
				Display display = PlatformUI.getWorkbench().getDisplay();
				URL urlImage = new URL("platform:/plugin/br.ufrn.spl.ev/resources/direct_conflict.gif");
				directConflictImage = new Image (display, urlImage.openConnection().getInputStream());
			
				GC gc = new GC(directConflictImage);
				gc.drawImage(directConflictImage, 16, 16); 
				gc.dispose();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return directConflictImage;
	}
	
	
	/** draw a red X */
	public static Image drawClearConflictImage(){
		if(clearConflictImage == null){
			try {
				Display display = PlatformUI.getWorkbench().getDisplay();
				URL urlImage = new URL("platform:/plugin/br.ufrn.spl.ev/resources/clear_conflict.png");
				clearConflictImage = new Image (display, urlImage.openConnection().getInputStream());
			
				GC gc = new GC(clearConflictImage);
				gc.drawImage(clearConflictImage, 16, 16); 
				gc.dispose();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return clearConflictImage;
	}
	
	/** draw a yellow triangle */
	public static Image drawIndirectConflictImage(){
		if(indirectConflictImage == null){
			try {
				Display display = PlatformUI.getWorkbench().getDisplay();
				URL urlImage = new URL("platform:/plugin/br.ufrn.spl.ev/resources/indirect_conflict.png");
				indirectConflictImage = new Image (display, urlImage.openConnection().getInputStream());
			
				GC gc = new GC(indirectConflictImage);
				gc.drawImage(indirectConflictImage, 16, 16); 
				gc.dispose();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return indirectConflictImage;
	}
	
	/** draw a T triangle */
	public static Image drawPseudoConflictImage(){
		if(pseudoConflictImage == null){
			try {
				Display display = PlatformUI.getWorkbench().getDisplay();
				URL urlImage = new URL("platform:/plugin/br.ufrn.spl.ev/resources/pseudo_conflict.png");
				pseudoConflictImage = new Image (display, urlImage.openConnection().getInputStream());
			
				GC gc = new GC(pseudoConflictImage);
				gc.drawImage(pseudoConflictImage, 16, 16); 
				gc.dispose();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pseudoConflictImage;
	}
	
	
	/** draw the statistic image */
	public static Image drawStatisticAnalysisImage(){
		if(statisticAnalysisImage == null){
			try {
				Display display = PlatformUI.getWorkbench().getDisplay();
			
				URL urlImage = new URL("platform:/plugin/br.ufrn.spl.ev/resources/statistics.png");
				statisticAnalysisImage = new Image (display, urlImage.openConnection().getInputStream());
			
				GC gc = new GC(statisticAnalysisImage);
				gc.drawImage(statisticAnalysisImage, 16, 16);
				gc.dispose();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return statisticAnalysisImage;
	}
	
	/** draw the safe template analysis image */
	public static Image drawSafeTemplateAnalysisImage(){
		if(safeTemplateAnalysisImage == null){
			try {
				Display display = PlatformUI.getWorkbench().getDisplay();
			
				URL urlImage = new URL("platform:/plugin/br.ufrn.spl.ev/resources/safe_templates.png");
				safeTemplateAnalysisImage = new Image (display, urlImage.openConnection().getInputStream());
			
				GC gc = new GC(safeTemplateAnalysisImage);
				gc.drawImage(safeTemplateAnalysisImage, 16, 16);
				gc.dispose();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return safeTemplateAnalysisImage;
	}
	
	
	/** draw the safe template analysis image */
	public static Image drawDependenceAnalysisImage(){
		if(dependenceAnalysisImage == null){
			try {
				Display display = PlatformUI.getWorkbench().getDisplay();
			
				URL urlImage = new URL("platform:/plugin/br.ufrn.spl.ev/resources/dependence_analysis.png");
				dependenceAnalysisImage = new Image (display, urlImage.openConnection().getInputStream());
			
				GC gc = new GC(dependenceAnalysisImage);
				gc.drawImage(dependenceAnalysisImage, 16, 16);
				gc.dispose();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dependenceAnalysisImage;
	}
	
	
	/** draw a blank image */
	public static Image drawBlankImage(){
		if(blankImage == null){
			Display display = PlatformUI.getWorkbench().getDisplay();
			blankImage = new Image (display, 16, 16);
		}
		return blankImage;
	}
	
}