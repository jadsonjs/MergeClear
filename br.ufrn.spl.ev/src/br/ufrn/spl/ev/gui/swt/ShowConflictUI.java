/**
 *
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE –- UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA -– DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 *
 */
package br.ufrn.spl.ev.gui.swt;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;

/**
 * Show a conflict of specific change log
 *
 * @author jadson
 *
 * @since 22/04/2013
 * @version 1.0 - Class Creation
 */
public class ShowConflictUI {

	private Shell shell;
	
	public ShowConflictUI(Shell shell){
		this.shell = shell;
	}
	
	
	
	public void showAssetsInConflict(AssetChangeLog selectedAsset){
		
		if(selectedAsset == null) return;
		
		if(! ( selectedAsset instanceof MethodChangeLog ) && ! ( selectedAsset instanceof FieldChangeLog ) ){
			MessageDialog.openError(shell, "Type not supported", "Select a Method or Field" );
			return;
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append(selectedAsset.getSignature() );
		
		if(selectedAsset.isDirectlyConflicting()){
			builder.append(" \n\n\n DIRECT conflict with: \n\n");
			
			for(AssetChangeLog asset : selectedAsset.getDirectConflicts()){
				builder.append(asset.getSignature()+" \n\n");
			}
		}
		
		if(selectedAsset.isIndirectlyConflicting()){
			builder.append(" \n\n\n INDIRECT conflict with: \n\n");
			
			for(AssetChangeLog asset : selectedAsset.getIndirectConflicts()){
				builder.append(asset.getSignature()+" \n\n");
			}
		}
		
		if(selectedAsset.isTextualConflicting()){
			builder.append(" \n\n\n TEXTUAL conflict with: \n\n");
			
			for(AssetChangeLog asset : selectedAsset.getTextualConflicts()){
				builder.append(asset.getSignature()+" \n\n");
			}
		}
		
		MessageDialog.openInformation(shell, "Informations about Conflict", builder.toString() );
	}
	
	
	
	
	
//	public ShowConflictUI(ChangeLog changeLog, ConflictModel plcm){
//
//		IWorkbench workbench = PlatformUI.getWorkbench();
//		
//		Display display = workbench.getDisplay();
//		
//	    Shell shell = new Shell(display);
//	    shell.setSize(800, 600);
//	    
//		Tree tree = new Tree (shell, SWT.BORDER);
//		 
//    	List<Conflict> conflicts = ConflictRepository.getInstance().getModel().get();
//			
//    	if(conflicts != null){
//    	
//			/*Map<String, FeatureConflict> feautureConflicts = productLineConflict.getFeatureConflicts();
//			
//			for (String featureTargetName : feautureConflicts.keySet()) {
//		
//				
//				FeatureConflict  featureConflict = feautureConflicts.get(featureTargetName);
//				
//				TreeItem item = new TreeItem (tree, 0);
//				item.setText ("Target Feature: "+featureTargetName);
//				item.setData (featureConflict);
//				
//				// Third Level: AssetConflict
//				for (Conflict assetConflict : featureConflict.getListAssetConflicts()) {
//					TreeItem subItem = new TreeItem (item, 0);
//					subItem.setText (assetConflict.toString());
//					subItem.setData (assetConflict);
//				}
//			}*/
//    	}
//		
//	    Rectangle clientArea = shell.getClientArea();
//	    tree.setBounds(clientArea.x, clientArea.y, 800, 600);
//	    shell.pack();
//	    shell.open();
//	}
	
	
	
	
}
