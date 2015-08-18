package br.ufrn.spl.ev.engines.merge.engine;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Utility class containing Java Project Operations
 * 
 * @author Gleydson
 *
 */
public class ProjectUtils {

	/**
	 * Create a source folder in target project
	 * @param name
	 * @throws CoreException
	 */
	public static void createSourceFolder(String name) throws CoreException {
		
		IProject project = (IProject) Repository.getJavaTargetProject();
		IFolder sourceFolder = project.getFolder(name);
		sourceFolder.create(false, true, null);
	}
	
	/**
	 * Create a Java Package in target project
	 * @param sourceFolder
	 * @param packageName
	 * @return
	 * @throws JavaModelException
	 */
	public static IPackageFragment createPackage(String sourceFolder, String packageName) throws JavaModelException {
		
		IJavaProject project = Repository.getJavaTargetProject();
		
		IPackageFragment pack = project.getPackageFragmentRoot(sourceFolder).createPackageFragment(packageName, false, null);
		
		return pack;
	}
	
	/**
	 * Create a compilation unit in target project
	 * @param pack
	 * @param className
	 * @param source
	 * @return
	 * @throws JavaModelException
	 */
	public static ICompilationUnit createCompilationUnit(IPackageFragment  pack, String className, String source) throws JavaModelException {
		
		ICompilationUnit cu = pack.createCompilationUnit(className + ".java", source, false, null);
		
		return cu;
		
	}

}
