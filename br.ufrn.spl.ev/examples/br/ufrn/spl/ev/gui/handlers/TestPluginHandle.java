/**
 * 
 */
package br.ufrn.spl.ev.gui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

import br.ufrn.spl.ev.engines.miners.svn.SVNCheckoutTest;

/**
 * Handle to test when we need eclipse infrastructure
 * 
 * @author jadson
 *
 */
public class TestPluginHandle implements IHandler{

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		try {
			
			//IWorkspace workspace = ResourcesPlugin.getWorkspace();
			
			// many ways to load the test project //
			//IPath projectDotProjectFile = new Path("/Users/jadson/programacao/runtime-EclipseApplication/SIGAA" + "/.project");
			//IProjectDescription projectDescription = workspace.loadProjectDescription(projectDotProjectFile);
			//IProject project = workspace.getRoot().getProject("Source");
		

			/* ******************************************************
			 * test print project details just it 
			 * ******************************************************/ 
			//new JDTTest().printProjectInfo(project);
			//System.out.println(" \n ================================ \n");
			//new ASTTest().analyseMethods(project);
			
			/* ******************************************************
			 * test using JDT research engine 
			 * ******************************************************/ 
			//new JDTSearchEngineTest().search(JavaCore.create(project) );
			
			
			/* ******************************************************
			 * test WALA call graph 
			 * ******************************************************/ 
			//new WalaTest().buildCallGraph(project);
			
			/* ******************************************************
			 * test SVN
			 * ******************************************************/
			new SVNCheckoutTest().checkout("/trunk/SIGEvento");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		
	}

}
