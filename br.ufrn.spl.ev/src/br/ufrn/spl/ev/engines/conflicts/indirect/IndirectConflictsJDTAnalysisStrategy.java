/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.indirect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

import br.ufrn.spl.ev.exceptions.IndirectConflictsDetectionException;
import br.ufrn.spl.ev.gui.swt.ShowEvolutionsUI;
import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;
import br.ufrn.spl.ev.util.FileUtil;
import br.ufrn.spl.ev.util.StringUtils;


/**
 * <p>Implements the analysis of conflict with Eclipse Java development tools (JDT). </p>
 * 
 * <p> <i>The JDT project provides the tool plug-ins that implement a Java IDE supporting the development 
 * of any Java application, including Eclipse plug-ins. It adds a Java project nature and Java perspective 
 * to the Eclipse Workbench as well as a number of views, editors, wizards, builders, and code merging and 
 * refactoring tools. The JDT project allows Eclipse to be a development environment for itself. </i> </p>
 * 
 * {@link http://http://www.eclipse.org/jdt/}
 * 
 * @author jadson - jadson@info.ufrn.br
 * 
 * @version 1.0 - Class Creation
 * @version 2.0 - jadson -  completing the full call graphic for one asset. Who it call and who is calling it.
 */
public class IndirectConflictsJDTAnalysisStrategy implements IndirectConflictsAnalysisStrategy{

	
	private ShowEvolutionsUI showEvolutionsUI;
	
	
	public IndirectConflictsJDTAnalysisStrategy(ShowEvolutionsUI showEvolutionsUI) {
		this.showEvolutionsUI = showEvolutionsUI;
	}



	/**
	 * <p>Find the dependence of asset using JDT. Who is calling and who it call.</p> 
	 * 
	 * @see br.ufrn.spl.ev.engines.conflicts.indirect.IndirectConflictsEvolutionDetection#getDependenceOfAsset(br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog)
	 */
	@Override
	public  List<AssetChangeLog> getReferencesInTheCallGraphic(IJavaProject javaProject, AssetChangeLog assetChangeLog, List<AssetChangeLog> setOfOtherAssets,  
			Map<AssetChangeLog,  List<AssetChangeLog>> cacheCallGraphic) throws IndirectConflictsDetectionException{
		
		
		// who is calling it
		return getReferencesOfAsset(javaProject, assetChangeLog, cacheCallGraphic);
		
	}
	
	
	
	/**
	 * <p>Find the dependence of asset using JDT. Who is calling and who it call.</p> 
	 * 
	 * @see br.ufrn.spl.ev.engines.conflicts.indirect.IndirectConflictsEvolutionDetection#getDependenceOfAsset(br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog)
	 */
	@Override
	public  List<AssetChangeLog> getDependenceInTheCallGraphic(IJavaProject javaProject, AssetChangeLog assetChangeLog, List<AssetChangeLog> setOfOtherAssets,  
			Map<AssetChangeLog,  List<AssetChangeLog>> cacheCallGraphic) throws IndirectConflictsDetectionException{
		
		return getDependenceOfAsset(javaProject, assetChangeLog, setOfOtherAssets, cacheCallGraphic);
		
	}
	
	
	/** 
	 * Return the assets that make reference to the actual asset. Simple JDT search. 
	 *
	 * @param javaProject
	 * @param assetChangeLog
	 * @return
	 * @throws IndirectConflictsDetectionException
	 */
	private  List<AssetChangeLog> getReferencesOfAsset(IJavaProject javaProject, AssetChangeLog assetChangeLog, 
			Map<AssetChangeLog,  List<AssetChangeLog>> cacheCallGraphic) throws IndirectConflictsDetectionException{
		
		
		/* 
		 * OTIMIZATION 
		 * 
		 * If we have already look for a reference of some asset, it is a bad idea search again. So we just return theses 
		 * references that we keep on the change.
		 * 
		 */
		if(cacheCallGraphic != null && cacheCallGraphic.containsKey(assetChangeLog)){
			return cacheCallGraphic.get(assetChangeLog);
		}
		
		/*
		 * just assets like "getId()", because getId() this retrieves a lot of false positive
		 */
		String ignoredFileName = showEvolutionsUI.getWorkDirectoryPath()+"/"+"ignoredwords.txt";
		if( isIgnoreAsset(ignoredFileName,  assetChangeLog.getSignature() )  ){
			return new ArrayList<AssetChangeLog>(); 
		}
		
		
		final List<Object> result = new ArrayList<Object>();
		
		IJavaElement javaElement = getJDTJavaElement(javaProject, assetChangeLog);
			
		if(javaElement != null) {
			
			//  step 1: search how call this java element
			SearchPattern pattern = SearchPattern.createPattern(javaElement, IJavaSearchConstants.REFERENCES);
			
			
			// step 2: Create search scope
			IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
			
			
			// step3: define a result collector
			SearchRequestor requestor = new SearchRequestor() {
				public void acceptSearchMatch(SearchMatch match) {
					if(! match.isInsideDocComment()  ) // ignore references in comments 
						result.add(match.getElement());
				}
			};
			
			// step4: start searching
			SearchEngine searchEngine = new SearchEngine();
			try {
				searchEngine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope, requestor, null /* progress monitor is not used here */);
			} catch (CoreException e) {
				e.printStackTrace();
			} catch (Exception ex) {
				// if exception, print it and go to the next
				ex.printStackTrace();
				return new  ArrayList<AssetChangeLog>(); // if error, return no dependences
			}
			
			List<AssetChangeLog> references = convertJDTJavaElementToChangeLogHistoryModel(javaProject.getElementName(), result);
			
			cacheCallGraphic.put(assetChangeLog, references);  // Save in the cache !!!!!
			
			return references;
			
		}
		
		return new ArrayList<AssetChangeLog>();
	}
	
	
	/*
	 * Some abstract assets bring a lot of false positive like getId(), bacause getId() is used in many places.
	 * So this method ignored and does not recover reference of this assets
	 */
	public boolean isIgnoreAsset(String ignoredWordsFileName, String signature) {
		if(signature == null) return false;
		
		try {
			List<String> content =  FileUtil.readFileContent(ignoredWordsFileName);
			
			for (String line : content) {
				if(line != null){
					String[] ignoredAssets = line.split(";");
					for (String ignored : ignoredAssets) {
						if(signature.endsWith(ignored))
							return true;
					}
				}
			}
			
		} catch (IOException e) {
			return false;
		}
		
		return false;
	}



	/**
	 * Return the assets the actual asset depends. How JDT does not have this search, what we are doing is get all other assets that evolved and
	 * see if someone reference the asset we are analyzing, if true, the asset call this one. It is a more complicate that find the references.
	 *
	 * <p>This is a very heavy method, so take care about it. cacheCallGraphic it is already an optimization</p>
	 *
	 * @param javaProject  - usually the target project
	 * @param assetChangeLog - the asset we are investigating
	 * @param setOfOtherAssets - usually the other assets the evolved less the actual ( ALL - assetChangeLog )
	 * @return
	 * @throws IndirectConflictsDetectionException
	 */
	private  List<AssetChangeLog> getDependenceOfAsset(IJavaProject javaProject, AssetChangeLog assetChangeLog, List<AssetChangeLog> setOfOtherAssets, Map<AssetChangeLog,  List<AssetChangeLog>> cacheCallGraphic) throws IndirectConflictsDetectionException{
		
		List<AssetChangeLog> dependences = new ArrayList<AssetChangeLog>();
		
		// for each asset that is not the assetChangeLog, get the reference
		for (AssetChangeLog otherAsset : setOfOtherAssets) {
			
			 List<AssetChangeLog> references = getReferencesOfAsset(javaProject, otherAsset, cacheCallGraphic);
			 
			 for (AssetChangeLog reference : references) {
			
				/*
				 * The "cat jump" is make the oposite way, if a reference of otherAssets is the assetChangeLog, 
				 * so the "otherAssets" is a dependence of assetChangelog
				 */
				if ( reference.equals( assetChangeLog ) ) {
					dependences.add(otherAsset);
				}
			}
		}
		
		return dependences;
	}
	
	
	
	/**
	 * This method get the Java element (of JavaModel) correspondent in the HistoryChangeLogModel
	 * 
	 * @param javaProject
	 * @param assetChangeLog
	 * @return
	 */
	private IJavaElement getJDTJavaElement(IJavaProject javaProject, AssetChangeLog assetChangeLog) throws IndirectConflictsDetectionException{
		
		// for now just fields and methods are analyzed
		FieldChangeLog fieldChangeLog =  null;
		MethodChangeLog methodChangeLog  =  null;
		
		if(assetChangeLog instanceof FieldChangeLog)
			fieldChangeLog = (FieldChangeLog) assetChangeLog;
		else
			if(assetChangeLog instanceof MethodChangeLog)
				methodChangeLog = (MethodChangeLog) assetChangeLog;
			else
				return null;
		
		ClassChangeLog _class = methodChangeLog != null ? methodChangeLog.getClassChangeLog() : fieldChangeLog.getClassChangeLog();
		
		try{
			// find the method we want: VERY EXPENSIVE O(N^4)
			IPackageFragment[] packages = javaProject.getPackageFragments(); // all packages
			
			for (IPackageFragment mypackage : packages) {
				
				if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) { // just source class
					
					for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
						
						if( unit.getElementName().equals( _class.getName())     ) {  // class with same name
						
							IType[] allTypes = unit.getAllTypes();
							for (IType type : allTypes) {
								
								if(fieldChangeLog != null){
									IField[] fields = type.getFields();
									for (IField field : fields) {
			
										if(field.getElementName().equals( fieldChangeLog.getName() )){
											return field;  // we find what we are look for
										}
									}
								}
								
								if(methodChangeLog != null){
									IMethod[] methods = type.getMethods();
									for (IMethod method : methods) {
			
										if(method.getElementName().equals( methodChangeLog.getName() )){
											return method; // we find what we are look for
										}
									}
								}
							}
						}
					}
				}
			}
			
			return null;
			
		}catch(JavaModelException jme){
			jme.printStackTrace();
			throw new IndirectConflictsDetectionException(jme.toString());	
		}
	}
	
	
	
	
	/** Convert the Java elements of the JDT model found to the ChangeLogHistory elements again */
	@SuppressWarnings("restriction")
	private List<AssetChangeLog> convertJDTJavaElementToChangeLogHistoryModel(String projectName, List<Object> result) {
		
		//
		// A not optimized way to get the asset
		//
		List<AssetChangeLog> assetOfTarget =  new ArrayList<AssetChangeLog>();
		
		for (Object object : result) {
			
			if(object instanceof org.eclipse.jdt.internal.core.ResolvedSourceField){ // its a field
				
				
				/*
				 * Here I don't know a way to transform this objects, so a process it's information 
				 * to create a AssetChangeLog signature that we need to compare evolution objects 
				 */
				
				org.eclipse.jdt.internal.core.ResolvedSourceField fieldResolved = (org.eclipse.jdt.internal.core.ResolvedSourceField) object;
			
				// int:a1
				String fieldSignature = fieldResolved.toString();
				fieldSignature = fieldSignature.substring(0, fieldSignature.indexOf("{")-1); // until the first "{"
				if(fieldSignature.contains(" "))
					fieldSignature = fieldSignature.replace(" ", ":"); // the return type
				
				fieldSignature = fieldSignature.replaceAll(" ", "").trim();
				
				// returnInformation[0] = projectName
				// returnInformation[1] = className
				// returnInformation[2] = classPath
				String[] returnInformation = generatedClassChangeLogSignature(fieldResolved.getParent());
				
				// Bug correction, to the signature, we can not use the local project name, we have to use the real project name
				//String projectName = returnInformation[0];  
				String className = returnInformation[1];
				String classPath = returnInformation[2];
				
				/*
				 *  The more important here is we have the correct signature , to compare with the assetChangeLog 
				 *  on the ChangeLogHistory source and target.
				 *  
				 *  Information about the type of evolution is not necessary
				 */
				FieldChangeLog field = new FieldChangeLog(fieldResolved.getElementName(), fieldSignature, null);
				try{
					field.setClassChangeLog(new ClassChangeLog(className, classPath, StringUtils.createClasseSignature(classPath, projectName) , null, ""));
					assetOfTarget.add(  field ); 
					
				}catch(IllegalArgumentException iae){
					// the class is in other project, not supported by our approach yet, we want references just in the project
					//String classSignature = classPath.substring(classPath.indexOf("/")+1, classPath.length());
					//field.setClassChangeLog(new ClassChangeLog(className, classPath, classSignature, null, ""));
					// JUST TO DEBUG
					//System.out.println("Project name: \""+projectName+"\""+ " classpath: "+"\""+classPath+"\""+ " using "+classSignature+" like class signature. ");
				}
				
			}
			
			if(object instanceof org.eclipse.jdt.internal.core.ResolvedSourceMethod){ // its a method
				
				/*
				 * Here I don't know a way to transform this objects, so a process it's information 
				 * to create a AssetChangeLog signature that we need to compare evolution objects 
				 */
				
				org.eclipse.jdt.internal.core.ResolvedSourceMethod methodResolved = (org.eclipse.jdt.internal.core.ResolvedSourceMethod) object;
				
				// void#a1(double, float)
				String methodSignature = methodResolved.toString();
				methodSignature = methodSignature.substring(0, methodSignature.indexOf(")")+1); // until the first ")"
				
				if(methodSignature.contains(", "))
					methodSignature = methodSignature.replaceAll(", ", ",");
				
				if(methodSignature.contains(" ")) /// the white space between return type and method name
					methodSignature = methodSignature.replace(" ", "#"); // replace with the divisor character
				else
					methodSignature = "void#"+methodSignature;
				
				methodSignature = methodSignature.replaceAll(" ", "").trim();
				
				/* Very Important: In the changelogHistory we don'tt use '<' and '>' because it generate a error when save to XML file
				 * So here have to change <> by HTML code representation
				 */
				methodSignature = methodSignature.replaceAll("<", "&lt;");
				methodSignature = methodSignature.replaceAll(">", "&gt;");
				
				// returnInformation[0] = projectName
				// returnInformation[1] = className
				// returnInformation[2] = classPath
				String[] returnInformation = generatedClassChangeLogSignature(methodResolved.getParent());
				
				// Bug correction, to the signature, we can not use the local project name, we have to use the real project name
				//String projectName = returnInformation[0];
				String className = returnInformation[1];
				String classPath = returnInformation[2];
				
				
				/*
				 *  The more important here is we have the correct signature, to compare with the assetChangeLog 
				 *  on the ChangeLogHistory source and target.
				 *  
				 *  Information about the type of evolution is not necessary
				 */
				
				MethodChangeLog method = new MethodChangeLog(methodResolved.getElementName(), methodSignature, null);
				try{
					method.setClassChangeLog(new ClassChangeLog(className, classPath, StringUtils.createClasseSignature(classPath, projectName) , null, ""));
					assetOfTarget.add(  method );
					
				}catch(IllegalArgumentException iae){
					// the class is in other project, not supported by our approach yet, we want references just in the project
					// the class is in other project, not supported by our approach yet
					//String classSignature = classPath.substring(classPath.indexOf("/")+1, classPath.length());
					//method.setClassChangeLog(new ClassChangeLog(className, classPath, classSignature, null, ""));
					// JUST TO DEBUG
					//System.out.println("Project name: \""+projectName+"\""+ " classpath: "+"\""+classPath+"\""+ " using "+classSignature+" like class signature. ");
				}
				
				
			}
			
			
		}
		return assetOfTarget;
	}
	
	
	/**
	 * Generated ClassChangeLog Signature form information return by the JDT search engine
	 * 
	 * <p> Created at:  23/10/2013  </p>
	 *
	 * @param IJavaElement root
	 * @return  returnInformation[0] = projectName, returnInformation[1] = className, returnInformation[2] = classPath
	 */
	@SuppressWarnings("restriction")
	private String[] generatedClassChangeLogSignature(IJavaElement parent){
		
		// returnInformation[0] = projectName
		// returnInformation[1] = className
		// returnInformation[2] = classPath
		String[] returnInformation = new String[3];
		
		while(parent != null){
			if( parent instanceof org.eclipse.jdt.internal.core.JavaModel  ) // we arrive in the root 
				break;
			
			if( parent instanceof org.eclipse.jdt.internal.core.JavaProject  )
				returnInformation[0] = parent.getElementName();
			
			if( parent instanceof org.eclipse.jdt.internal.core.CompilationUnit  )
				returnInformation[1] = parent.getElementName();
			
			// assemble this information: "Project/package/sub_package/sub_sub_package/Class.java"
			if(! (parent instanceof org.eclipse.jdt.internal.core.SourceType) ){ // jump this, it is not necessary
				returnInformation[2] = ( StringUtils.isEmpty(returnInformation[2]) ?  parent.getElementName() : ( parent.getElementName() +"/"+ returnInformation[2])  );
			}
			
			parent = parent.getParent();
		}
		
		if(returnInformation[2].contains(".")){
			returnInformation[2] = returnInformation[2].replaceAll("\\.", "/");
			returnInformation[2] = returnInformation[2].replaceAll("/java", ".java");
		}
		
		return returnInformation;
	}

}
