/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.indirect;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;

import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.engines.connectors.SystemConnector;
import br.ufrn.spl.ev.engines.merge.engine.Repository;
import br.ufrn.spl.ev.engines.miners.MinerFactory.MergeSide;
import br.ufrn.spl.ev.exceptions.IndirectConflictsDetectionException;
import br.ufrn.spl.ev.gui.swt.ShowEvolutionsUI;
import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;
import br.ufrn.spl.ev.util.StringUtils;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public abstract class IndirectConflictsDetection {

	/* ***** The data necessary to do the analysis ***** */
	protected ChangeLogHistory changeLogHistorySource;    // evolution on source
	protected ChangeLogHistory changeLogHistoryTarget;    // evolution on target
	protected IJavaProject javaSourceProject;             // the source project the do the dependence analysis 
	protected IJavaProject javaTargetProject;             // the target project the do the dependence analysis 
	/*  ***************************************************  */
	

	
	
	/* Model that have the conflicts generated */
	protected ConflictModel conflictModel;
	
	
	/** the depth level of the analysis, if this value were more than 1, the dependence method will be called recursively */
	protected short depthAnalysisLevel = 1;
	
	/*
	 * This cache save the references of one asset change log already search.
	 * We need to keep this for not search twice a reference of one asset that we already have find.
	 * 
	 * Keep the cache of references that are used by reference e dependence search
	 */
	protected Map<AssetChangeLog,  List<AssetChangeLog>> cacheCallGraphic = new TreeMap<AssetChangeLog, List<AssetChangeLog>>();
	
	/* Monitor from the GUI */
	protected IProgressMonitor monitor;

	protected SystemConnector systemConnectorSource;
	protected SystemConnector systemConnectorTarget;
	
	protected RepositoryConnector repositoryConnectorSource = null;
	protected RepositoryConnector repositoryConnectorTarget = null;
	
	/** Strategy used to make the analysis */
	protected IndirectConflictsAnalysisStrategy analysisStrategy;
	
	/** Necessary to generated the classes classpath from elements */
	protected String realProjectName;
	
	/** Main GUI of the application */
	protected ShowEvolutionsUI evolutionUI;
	
	/**
	 * Load Java projects data
	 */
	protected void loadProjectsData(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget, ConflictModel conflictModel, RepositoryConnector sourceConnector, RepositoryConnector targetConnector) throws IndirectConflictsDetectionException{
		this.changeLogHistorySource = changeLogHistorySource;
		this.changeLogHistoryTarget = changeLogHistoryTarget;
		this.conflictModel = conflictModel;
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		
		sourceConnector.performsRun(); // carry out the checkout of the source project configured in this connector
		targetConnector.performsRun(); // carry out the checkout of the target project configured in this connector
		
		// Open the project that were check out to the workspace //
		
		
		String sourceProjectName = Repository.getSourceProjectName();
		if ( sourceProjectName == null )
			sourceProjectName = StringUtils.extractAnalyzedProjectName(sourceConnector.getRepositoryPath(), MergeSide.SOURCE);
		
		
		String targetProjectName = Repository.getTargetProjectName();
		if ( targetProjectName == null )
			targetProjectName =	StringUtils.extractAnalyzedProjectName(targetConnector.getRepositoryPath(), MergeSide.TARGET);
		
		try {
			// Open the checked out projects // 
			
			IPath sourceProjectDotProjectFile = new Path( workspace.getRoot().getLocation().toString() +"/" + sourceProjectName + "/.project");
			IProjectDescription sourceProjectDescription = workspace.loadProjectDescription(sourceProjectDotProjectFile);
			IProject sourceProject = workspace.getRoot().getProject(sourceProjectDescription.getName());
			JavaCapabilityConfigurationPage.createProject(sourceProject, sourceProjectDescription.getLocationURI(),null);
			
			javaSourceProject = JavaCore.create(sourceProject);
			
			IPath targetProjectDotProjectFile = new Path(  workspace.getRoot().getLocation().toString() +"/" + targetProjectName + "/.project");
			IProjectDescription targetProjectDescription = workspace.loadProjectDescription(targetProjectDotProjectFile);
			IProject targetProject = workspace.getRoot().getProject(targetProjectDescription.getName());
			JavaCapabilityConfigurationPage.createProject(targetProject, targetProjectDescription.getLocationURI(), null);
			
			javaTargetProject = JavaCore.create(targetProject);
			
			
		} catch (CoreException ce) {
			ce.printStackTrace();
			throw new IndirectConflictsDetectionException(ce.toString());
		}
		
	}
	
	public abstract void checkIndirectConflicts(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget,  ConflictModel conflictModel, short depthAnalysisLevel) throws IndirectConflictsDetectionException;

	
	public Map<AssetChangeLog, List<AssetChangeLog>> getCacheCallGraphic() {
		return cacheCallGraphic;
	}

	public  void setCacheCallGraphic(Map<AssetChangeLog, List<AssetChangeLog>> cacheCallGraphic) {
		this.cacheCallGraphic = cacheCallGraphic;
	}
	
	
}
