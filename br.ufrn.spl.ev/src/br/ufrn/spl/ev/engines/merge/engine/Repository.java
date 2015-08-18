package br.ufrn.spl.ev.engines.merge.engine;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;

import br.ufrn.spl.ev.PluginConstants;
import br.ufrn.spl.ev.engines.miners.MinerFactory.MergeSide;
import br.ufrn.spl.ev.util.ConfigUtils;
import br.ufrn.spl.ev.util.PropertiesUtil;
import br.ufrn.spl.ev.util.PropertiesUtil.PluginProperties;
import br.ufrn.spl.ev.util.StringUtils;

/**
 * <p>Java projects information repository.</p> 
 * 
 * <p>Keep the information about the project the are being analyzed ( Source and Target )</p>
 * 
 * @author Gleydson
 * 
 */
public class Repository {

	/*
	 * Java Source Project on the workspace
	 */
	private static IJavaProject source;

	/*
	 * Java target Project on the workspace
	 */
	private static IJavaProject target;

	/*
	 * Eclipse Source Project on the workspace (used to create a Java Project)
	 */
	private static IProject projectSource;

	/*
	 * Eclipse target Project on the workspace (used to create a Java Project)
	 */
	private static IProject projectTarget;

	/*
	 * All folders of the source project, usually are models on the UFRN system (graduação, biblioteca, extensão, pesquisa, etc) 
	 */
	private static List<String> sourceFolders = new ArrayList<String>();

	/*
	 * All folders of the target project, usually are models on the UFRN system (graduação, biblioteca, extensão, pesquisa, etc) 
	 */
	private static List<String> targetFolders = new ArrayList<String>();
	
	
	/**
	 * Initialize repository information with java projects source and target
	 * 
	 * @param source
	 * @param target
	 * @throws CoreException 
	 */
	public static void init(IProject source, IProject target) throws CoreException {
		
		Repository.projectSource = source;
		Repository.projectTarget = target;

		Repository.source = JavaCore.create(source);
		Repository.target = JavaCore.create(target);

		// process source folders from source and target
		processSourceFolders(Repository.source, sourceFolders);
		processSourceFolders(Repository.target, targetFolders);
		
		// refresh projetct resources
		source.refreshLocal(IResource.DEPTH_INFINITE, null);
		target.refreshLocal(IResource.DEPTH_INFINITE, null);

	}
	
	/**
	 * Initialize project from your name, open the Java Project that should be on the workspace of eclipse.
	 * 
	 * @param projectSource
	 * @param projectTarget
	 * @throws CoreException 
	 */
	public static void init(String projectSource, String projectTarget)  throws CoreException {
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		
		IPath sourceProjectDotProjectFile = new Path(workspace.getRoot().getLocation().toString()+"/"+projectSource + "/.project");
		IProjectDescription sourceProjectDescription = workspace.loadProjectDescription(sourceProjectDotProjectFile);
		IProject sourceProject = workspace.getRoot().getProject(sourceProjectDescription.getName());
		JavaCapabilityConfigurationPage.createProject(sourceProject, sourceProjectDescription.getLocationURI(),null);
		
		source = JavaCore.create(sourceProject);
		
		IPath targetProjectDotProjectFile = new Path(workspace.getRoot().getLocation().toString()+"/" + projectTarget + "/.project");
		IProjectDescription targetProjectDescription = workspace.loadProjectDescription(targetProjectDotProjectFile);
		IProject targetProject = workspace.getRoot().getProject(targetProjectDescription.getName());
		JavaCapabilityConfigurationPage.createProject(targetProject, targetProjectDescription.getLocationURI(), null);
		
		target = JavaCore.create(targetProject);
		
		
		init(sourceProject,targetProject);
		
	}

	/**
	 * Register project source folders
	 * 
	 * @param project
	 * @throws JavaModelException
	 */
	private static void processSourceFolders(IJavaProject project, List<String> listFolder) throws JavaModelException {

		IPackageFragmentRoot[] roots = project.getPackageFragmentRoots();
		for (int i = 0; i < roots.length; i++) {
			if (roots[i].getResource() instanceof IFolder) {
				IFolder folder = (IFolder) roots[i].getResource();
				if (folder != null)
					listFolder.add(folder.getName());
			}
		}

	}

	/**
	 * Find class in source project
	 * 
	 * @param name
	 * @return
	 */
	private static IType findClass(String name, IJavaProject project) {

		if (project == null)
			throw new RuntimeException("Source Project not setted");
		try {
			IType type = project.findType(name);
			return type;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static IType findClassInSource(String name) {
		return findClass(name, source);
	}

	public static IType findClassInTarget(String name) {
		return findClass(name, target);
	}

	/**
	 * Find package in target project
	 * 
	 * @param name
	 * @return
	 * @throws JavaModelException
	 */
	public static IPackageFragment findPackageInTarget(String packageName, String sourceFolder)
			throws JavaModelException {

		String projectName = projectTarget.getName();
		packageName = packageName.replace(".", "/");

		Path path = new Path("/" + projectName + "/" + sourceFolder + "/" + packageName);

		return target.findPackageFragment(path);

	}
	
	
	/**
	 * Find package in source project
	 * 
	 * @param name
	 * @return
	 * @throws JavaModelException
	 */
	public static IPackageFragment findPackageInSource(String packageName, String sourceFolder)
			throws JavaModelException {

		String projectName = projectSource.getName();
		packageName = packageName.replace(".", "/");

		Path path = new Path("/" + projectName + "/" + sourceFolder + "/" + packageName);

		return target.findPackageFragment(path);

	}

	/**
	 * Test with a given source folder exists in target project
	 * 
	 * @param name
	 * @return
	 */
	public static boolean existsSourceFolderInTarget(String name) {
		return targetFolders.contains(name);
	}

	public static IJavaProject getJavaSourceProject() {
		return source;
	}

	public static IJavaProject getJavaTargetProject() {
		return target;
	}

	/**
	 * Get the source project name the is begin analyzed
	 * @return
	 */
	public static String getSourceProjectName() {
		
		String sourceProjectName = null;
		
		if( "DIRECT".equals( ConfigUtils.getConfiguration(PluginConstants.STATISTIC_ANALYSIS_MODEL) )  ) {
			sourceProjectName =  PropertiesUtil.readProperty(PluginProperties.CONFIG, "SOURCE_PROJECT_TO_ANALYSIS"); // the name is direct in the file connections.properties
		}
		// if chose indirect or the the direct model is empty //
		if("INDIRECT".equals( ConfigUtils.getConfiguration(PluginConstants.STATISTIC_ANALYSIS_MODEL) ) || StringUtils.isEmpty(sourceProjectName)  ){
			String repositorySource = PropertiesUtil.readProperty(PluginProperties.CONNECTIONS, PluginConstants.SOURCE_REPOSITORY_PATH); // the name is extract from the project checked out from repository
			sourceProjectName = StringUtils.extractAnalyzedProjectName(repositorySource, MergeSide.SOURCE);
		}
		
		return sourceProjectName;
	}
	
	/**
	 * Get the target project name the is begin analyzed
	 * @return
	 */
	public static String getTargetProjectName() {
		
		String targetProjectName = null;
		
		if( "DIRECT".equals( ConfigUtils.getConfiguration(PluginConstants.STATISTIC_ANALYSIS_MODEL) ) ){
			targetProjectName =   PropertiesUtil.readProperty(PluginProperties.CONFIG, "TARGET_PROJECT_TO_ANALYSIS"); // the name is direct in the file connections.properties
		}
		// if chose indirect or the the direct model is empty //
		if("INDIRECT".equals( ConfigUtils.getConfiguration(PluginConstants.STATISTIC_ANALYSIS_MODEL) ) || StringUtils.isEmpty(targetProjectName) ){
			String repositoryTarget = PropertiesUtil.readProperty(PluginProperties.CONNECTIONS, PluginConstants.TARGET_REPOSITORY_PATH); // the name is extract from the project checked out from repository
			targetProjectName =  StringUtils.extractAnalyzedProjectName(repositoryTarget, MergeSide.TARGET);
		}
		
		return targetProjectName;
	}
	
}
