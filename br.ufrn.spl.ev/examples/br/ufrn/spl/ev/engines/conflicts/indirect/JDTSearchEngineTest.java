/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.indirect;

import java.util.ArrayList;
import java.util.List;

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

/**
 * @author jadson - jadson@info.ufrn.br
 * @see http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Fguide%2Fjdt_api_search.htm
 */
public class JDTSearchEngineTest {

	
	public List<Object> search(IJavaProject javaProject) throws JavaModelException{
		
		System.out.println("Run Dependences Analisys with JDT");
		
		final List<Object> result = new ArrayList<Object>();
		
		IMethod methodSearch = null;
		IField  fieldSearch = null;
		IJavaElement classSearch = null;
		
		// find the method we want: very expensive O(n^4)
		IPackageFragment[] packages = javaProject.getPackageFragments();
		for (IPackageFragment mypackage : packages) {
			
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
					
					if( unit.getElementName().equals("A.java")) {
							classSearch = unit.getType("A").getTypeRoot();
					}
					
					IType[] allTypes = unit.getAllTypes();
					for (IType type : allTypes) {
						IField[] fields = type.getFields();
						for (IField field : fields) {

							if(field.getElementName().equals("a")){
								fieldSearch = field;
								if( fieldSearch != null && methodSearch != null ) break;
							}
						}
						
						IMethod[] methods = type.getMethods();
						for (IMethod method : methods) {

							if(method.getElementName().equals("a1")){
								methodSearch = method;
								if( fieldSearch != null && methodSearch != null ) break;
							}
						}
					}
				}
			}
		}
		
		if(methodSearch != null){
		
			SearchPattern pattern1 = null;
			SearchPattern pattern2 = null;
			SearchPattern pattern3 = null;
			
			//  step 1: search methods having "a1" as name
			if(classSearch != null)
				pattern1 = SearchPattern.createPattern(classSearch, IJavaSearchConstants.REFERENCES);
			if(methodSearch != null)
				pattern2 = SearchPattern.createPattern(methodSearch, IJavaSearchConstants.REFERENCES);
			if(fieldSearch != null)
				pattern3 = SearchPattern.createPattern(fieldSearch, IJavaSearchConstants.REFERENCES);
			
			// step 2: Create search scope
			IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
			
			// step3: define a result collector
			SearchRequestor requestor = new SearchRequestor() {
				public void acceptSearchMatch(SearchMatch match) {
					result.add(match.getElement());
				}
			};
	
			// step4: start searching
			SearchEngine searchEngine = new SearchEngine();
			try {
				if(pattern1 != null)
					searchEngine.search(pattern1, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope, requestor, null /* progress monitor is not used here */);
				if(pattern2 != null)
					searchEngine.search(pattern2, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope, requestor, null /* progress monitor is not used here */);
				if(pattern3 != null)
					searchEngine.search(pattern3, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope, requestor, null /* progress monitor is not used here */);
			} catch (CoreException e) {
				e.printStackTrace();
			}
	
			for (Object object : result) {
				System.out.println(object);
			}
			
			System.out.println(result.size());
		}
		
		return result;
		
	}
	
}
