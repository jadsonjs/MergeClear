package br.ufrn.spl.ev.engines.merge.asset;


import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ImportDeclaration;

/**
 * Type reference is a import declaration representing a a dependency (direct ou indirect) of the
 * imported type
 * 
 * @author Gleydson
 *
 */
public class TypeReference {

	private String name;

	private IType type;

	public TypeReference(ImportDeclaration id) {

		this.name = id.getName().toString();
		try {
			//this.type = MergeEngine.getTarget().findType(name);
			//TODO: Fazer isso depois de trazer  todas as classes
		} catch (Exception e) {

		}

	}

	public String getName() {
		return name;
	}

	public IType getType() {
		return type;
	}
}
