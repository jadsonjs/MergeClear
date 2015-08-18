/**
 *
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE –- UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA -– DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 *
 */
package br.ufrn.spl.ev.gui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * Test
 *
 * @author jadson
 *
 * @since 21/04/2013
 * @version 1.0 - Class Creation
 */
public class DefaultCheckTree {

	public DefaultCheckTree(){

		IWorkbench workbench = PlatformUI.getWorkbench();
		
		Display display = workbench.getDisplay();
		
	    Shell shell = new Shell(display);
	    Tree tree = new Tree(shell, SWT.BORDER | SWT.CHECK);
	    tree.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	            if (event.detail == SWT.CHECK) {
	                TreeItem item = (TreeItem) event.item;
	                boolean checked = item.getChecked();
	                checkItems(item, checked);
	                checkPath(item.getParentItem(), checked, false);
	            }
	        }
	    });
	    for (int i = 0; i < 4; i++) {
	        TreeItem itemI = new TreeItem(tree, SWT.NONE);
	        itemI.setText("Item " + i);
	        for (int j = 0; j < 4; j++) {
	            TreeItem itemJ = new TreeItem(itemI, SWT.NONE);
	            itemJ.setText("Item " + i + " " + j);
	            for (int k = 0; k < 4; k++) {
	                TreeItem itemK = new TreeItem(itemJ, SWT.NONE);
	                itemK.setText("Item " + i + " " + j + " " + k);
	            }
	        }
	    }
		Rectangle clientArea = shell.getClientArea();
	    tree.setBounds(clientArea.x, clientArea.y, 200, 200);
	    shell.pack();
	    shell.open();
//	    while (!shell.isDisposed()) {
//	        if (!display.readAndDispatch()) display.sleep();
//	    }
//	    display.dispose();
	}
	
	
	
	static void checkPath(TreeItem item, boolean checked, boolean grayed) {
	    if (item == null) return;
	    if (grayed) {
	        checked = true;
	    } else {
	        int index = 0;
	        TreeItem[] items = item.getItems();
	        while (index < items.length) {
	            TreeItem child = items[index];
	            if (child.getGrayed() || checked != child.getChecked()) {
	                checked = grayed = true;
	                break;
	            }
	            index++;
	        }
	    }
	    item.setChecked(checked);
	    item.setGrayed(grayed);
	    checkPath(item.getParentItem(), checked, grayed);
	}

	static void checkItems(TreeItem item, boolean checked) {
	    item.setGrayed(false);
	    item.setChecked(checked);
	    TreeItem[] items = item.getItems();
	    for (int i = 0; i < items.length; i++) {
	        checkItems(items[i], checked);
	    }
	}
	
}
