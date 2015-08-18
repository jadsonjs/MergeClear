/**
 *
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE –- UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA -– DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 *
 */
package br.ufrn.spl.ev.gui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * Test a creation of a menu
 *
 * @author jadson - jadson@info.ufrn.br
 *
 * @since 22/04/2013
 * @version 1.0 - Class Creation
 */
public class ShowAMenuInATreeHeader {

	public ShowAMenuInATreeHeader(){

		IWorkbench workbench = PlatformUI.getWorkbench();
		
		final Display display = workbench.getDisplay();
		
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final Tree tree = new Tree(shell, SWT.V_SCROLL|SWT.H_SCROLL| SWT.BORDER);
		tree.setHeaderVisible(true);
		final Menu headerMenu = new Menu(shell, SWT.POP_UP);
		final TreeColumn columnName = new TreeColumn(tree, SWT.NONE);
		columnName.setText("Name");
		columnName.setWidth(150);
		createMenuItem(headerMenu, columnName);
		final TreeColumn columnSize = new TreeColumn(tree, SWT.NONE);
		columnSize.setText("Size");
		columnSize.setWidth(150);
		createMenuItem(headerMenu, columnSize);
		final TreeColumn columnType = new TreeColumn(tree, SWT.NONE);
		columnType.setText("Type");
		columnType.setWidth(150);
		createMenuItem(headerMenu, columnType);
		final TreeColumn columnDate = new TreeColumn(tree, SWT.NONE);
		columnDate.setText("Date");
		columnDate.setWidth(150);
		createMenuItem(headerMenu, columnDate);
		final TreeColumn columnOwner = new TreeColumn(tree, SWT.NONE);
		columnOwner.setText("Owner");
		columnOwner.setWidth(0);
		columnOwner.setResizable(false);
		createMenuItem(headerMenu, columnOwner);
		
		for (int i = 0; i < files.length; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(files[i]);
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setText("node");
		}
		
		final Menu treeMenu = new Menu(shell, SWT.POP_UP);
		MenuItem item = new MenuItem(treeMenu, SWT.PUSH);
		item.setText("Open");
		item = new MenuItem(treeMenu, SWT.PUSH);
		item.setText("Open With");
		new MenuItem(treeMenu, SWT.SEPARATOR);
		item = new MenuItem(treeMenu, SWT.PUSH);
		item.setText("Cut");
		item = new MenuItem(treeMenu, SWT.PUSH);
		item.setText("Copy");
		item = new MenuItem(treeMenu, SWT.PUSH);
		item.setText("Paste");
		new MenuItem(treeMenu, SWT.SEPARATOR);
		item = new MenuItem(treeMenu, SWT.PUSH);
		item.setText("Delete");
		
		tree.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				Point pt = display.map(null, tree, new Point(event.x, event.y));
				Rectangle clientArea = tree.getClientArea();
				boolean header = clientArea.y <= pt.y && pt.y < (clientArea.y + tree.getHeaderHeight());
				tree.setMenu(header ? headerMenu : treeMenu);
			}
		});
		
		/* IMPORTANT: Dispose the menus (only the current menu, set with setMenu(), will be automatically disposed) */
		tree.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event event) {
				headerMenu.dispose();
				treeMenu.dispose();
			}
		});
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
	}
	
	static String[][] files = {
		{"ver.txt", "1 KB", "Text Document", "28/09/2005 9:57 AM", "admin",},
		{"Thumbs.db", "76 KB", "Data Base file", "13/03/2006 3:56 PM", "john",},
		{"daddy.bmp", "148 MB", "Bitmap", "27/10/2008 1:34 PM", "bill",},
		{"io.sys", "48 KB", "File System", "16/12/2008 6:14 AM", "admin",},
		{"Programs", "0 KB", "File Folder", "04/02/2009 12:18 PM", "anne",},
		{"test.rnd", "55 MB", "RND File", "19/02/2009 5:49 PM", "john",},
		{"arial.ttf", "94 KB", "True Type Font", "25/08/2008 1:25 PM", "john",},
	}; 
	
	static void createMenuItem(Menu parent, final TreeColumn column) {
		final MenuItem itemName = new MenuItem(parent, SWT.CHECK);
		itemName.setText(column.getText());
		itemName.setSelection(column.getResizable());
		itemName.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (itemName.getSelection()) {
					column.setWidth(150);
					column.setResizable(true);
				} else {
					column.setWidth(0);
					column.setResizable(false);
				}
			}
		});
	}	
}
