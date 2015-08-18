/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.gui.swt;

import java.util.Properties;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import br.ufrn.spl.ev.util.GUIUtils;
import br.ufrn.spl.ev.util.PropertiesUtil;

/**
 * UI for connection settings, edit the file connections.properties
 * 
 * @author jadson - jadson@info.ufrn.br
 * 
 * @since 21/10/2014
 * @version 1.0 - Class Creation
 *
 */
public class ConnectionSettingsUI {
	
	
	private Shell shell;
	
	private Composite composite;
	
	private ScrolledComposite sc;
	
	private GridData gdHorizontalSpanLabel;
	
	private GridData gdHorizontalSpanValue;

	/** Represent the configuration file of connections values to the tool */
	private Properties properties;
	
	public ConnectionSettingsUI(){ }

	public void configure() {
		
		init();
		
		properties = PropertiesUtil.readPropertiesObject(PropertiesUtil.PluginProperties.CONNECTIONS);
		
		loadPropertiesValues(properties);
		
		Button saveButton = new Button(composite, SWT.PUSH);
		saveButton.setText("Save");
		
		createActions();
		
		show();
		
	}
	
	private void createActions() {
		Button saveButton = new Button(composite, SWT.PUSH);
		saveButton.setText("Save");
		
		saveButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				PropertiesUtil.writePropertiesObject(PropertiesUtil.PluginProperties.CONNECTIONS, properties);
				shell.dispose();
			}
		});
	}
	
	private void loadPropertiesValues(Properties properties) {
		
		Set<Object> set = properties.keySet();
		
		for (Object object : set) {
			String key = (String) object;
		    String val = (String) properties.get(key);
		    
		    Label propertieKey = new Label(composite, SWT.LEFT);
		    propertieKey.setText(key);
		    propertieKey.setLayoutData(gdHorizontalSpanLabel);
		    
		    //sc.setContent(propertieKey);
		    
		    Text propertieValue = new Text(composite, SWT.BORDER);
	        propertieValue.setText(val);
	        propertieValue.setLayoutData(gdHorizontalSpanValue);
	        
	        sc.setContent(composite);
	        
	        sc.setMinSize(400, 400);
		    
		    // Expand both horizontally and vertically
		    sc.setExpandHorizontal(true);
		    sc.setExpandVertical(true);
		}
	}

	
	private void init() {
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		
		Display display = workbench.getDisplay();
		
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 2;
		gridLayout.verticalSpacing = 5;
		gridLayout.marginBottom = 5;
		gridLayout.marginTop = 5;
		
		gdHorizontalSpanLabel = new GridData(SWT.LEFT, SWT.FILL, false, false);
		gdHorizontalSpanLabel.widthHint = 200;
		gdHorizontalSpanLabel.heightHint = 20;
		gdHorizontalSpanLabel.horizontalSpan = 1;
		gdHorizontalSpanLabel.verticalSpan = 1;
		
		gdHorizontalSpanValue = new GridData(SWT.LEFT, SWT.FILL, false, false);
		gdHorizontalSpanValue.widthHint = 500;
		gdHorizontalSpanValue.heightHint = 20;
		gdHorizontalSpanValue.horizontalSpan = 1;
		gdHorizontalSpanValue.verticalSpan = 1;
		
	    shell = new Shell(display);
	    shell.setSize(800, 600);
	    GUIUtils.centerShell(shell);
	    shell.setLayout(gridLayout);
	    
	    sc = new ScrolledComposite(shell, SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL);
	  
	    composite = new Composite(sc, SWT.NONE);
	    composite.setLayout(gridLayout);	
	   
		/*
	     * // Set the absolute size of the child child.setSize(400, 400);
	     */
	    // Set the child as the scrolled content of the ScrolledComposite
	   // sc.setContent(composite);
	    
	  
		
	}
	
	private void show() {
		
	    shell.pack();
	    shell.open();
		
	}


}
