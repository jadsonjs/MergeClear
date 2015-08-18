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
public class CheckTreeDetectingEvent {

	
	public CheckTreeDetectingEvent(){
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		
		Display display = workbench.getDisplay();
		Shell shell = new Shell (display);
		Tree tree = new Tree (shell, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		for (int i=0; i<12; i++) {
			TreeItem item = new TreeItem (tree, SWT.NONE);
			item.setText ("Item " + i);
		}
		Rectangle clientArea = shell.getClientArea ();
		tree.setBounds (clientArea.x, clientArea.y, 100, 100);
		tree.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				String string = event.detail == SWT.CHECK ? "Checked" : "Selected";
				System.out.println (event.item + " " + string);
			}
		});
		shell.setSize (200, 200);
		shell.open ();
		//while (!shell.isDisposed()) {
		//	if (!display.readAndDispatch ()) display.sleep ();
		//}
		//display.dispose ();
	}
}
