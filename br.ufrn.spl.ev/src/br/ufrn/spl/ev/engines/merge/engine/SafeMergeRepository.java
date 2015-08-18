package br.ufrn.spl.ev.engines.merge.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * During merge operation source code is modified and can´t by undone. This
 * class have a responsability to make safe merge.
 * 
 * @author Gleydson Lima
 * 
 */
public class SafeMergeRepository {

	/**
	 * Map type and original source code
	 */
	private Map<String, String> sourceMap = new HashMap<String, String>();

	/**
	 * Register the source code state previous the merge
	 * 
	 * @param type
	 * @throws JavaModelException
	 */
	public void registerState(IType type) throws JavaModelException {
		sourceMap.put(type.getFullyQualifiedName(), type.getCompilationUnit().getSource());
	}

	/**
	 * Undo changelog merge operation
	 * 
	 * @throws JavaModelException
	 */
	public void undoMerge() throws JavaModelException {

		System.out.println("Undo merge");
		
		Set<String> classes = sourceMap.keySet();
		for (String c : classes) {

			IType t = Repository.findClassInSource(c);
			recoverPreviousState(t);

		}
	}

	/**
	 * Recovery the source state
	 * 
	 * @param type
	 * @throws JavaModelException
	 */
	public void recoverPreviousState(IType type) throws JavaModelException {

		ICompilationUnit unit = type.getCompilationUnit();

		String originalSource = sourceMap.get(type.getFullyQualifiedName());

		unit.getBuffer().setContents(originalSource);

		unit.save(null, true);

	}

}