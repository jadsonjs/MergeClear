package br.ufrn.spl.ev.engines.merge.asset;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Representation of Java Type incorporating features to abstract JDT
 * manipulation complexity.
 * 
 * @author Gleydson Lima
 * 
 */
public class Type {

	private String packageName;

	private String sourceFolder;

	private IType type;

	private List<Method> methods = new ArrayList<Method>();

	private List<Field> fields = new ArrayList<Field>();

	private List<TypeReference> referencias = new ArrayList<TypeReference>();

	private TypeDeclaration typeDeclaration;

	public Type(IType type) {
		if (type == null)
			throw new IllegalArgumentException("Invalid argument: JDT Type is null");
		this.type = type;
		packageName = type.getPackageFragment().getElementName();
		IPackageFragmentRoot packageRoot = (IPackageFragmentRoot) type.getPackageFragment().getParent();
		IFolder folder = (IFolder) packageRoot.getResource();
		sourceFolder = folder.getName();
		// sourceFolder = type.getfo
	}

	public IType getJDTType() {
		return type;
	}

	/**
	 * Add methods to your type
	 * 
	 * @param m
	 */
	public void addMethods(MethodDeclaration[] m) {

		for (int i = 0; i < m.length; i++) {
			methods.add(new Method(m[i]));
		}
	}

	/**
	 * Return method with given name
	 * 
	 * @param name
	 * @return
	 */
	public Method getMethod(String name) {

		for (Method m : methods) {
			if (m.getName().equals(name)) {
				return m;
			}
		}

		return null;
	}

	/**
	 * Return the last method declarated in the type
	 * 
	 * @return
	 */
	public Method getLastMethod() {
		if (methods.size() > 0)
			return methods.get(methods.size() - 1);
		else
			return null;
	}

	/**
	 * Return the last field declarated in the type
	 * 
	 * @return
	 */
	public Field getLastField() {
		if (fields.size() > 0)
			return fields.get(fields.size() - 1);
		else
			return null;
	}

	/**
	 * add fields to your type
	 * 
	 * @param f
	 */
	public void addFields(FieldDeclaration[] f) {

		for (int i = 0; i < f.length; i++) {
			fields.add(new Field(f[i]));
		}
	}

	/**
	 * Return field with given name
	 * 
	 * @param name
	 * @return
	 */
	public Field getField(String name) {

		for (Field f : fields) {
			if (f.getName().equals(name)) {
				return f;
			}
		}

		return null;
	}

	/**
	 * Add import declaration
	 * 
	 * @param node
	 */
	public void addImport(ImportDeclaration node) {

		TypeReference tr = new TypeReference(node);
		referencias.add(tr);

	}

	public String getName() {
		return type.getElementName();
	}

	public String getFullName() {
		return getPackageName() + "." + getName();
	}

	public String getSourceCode() throws JavaModelException {
		return type.getCompilationUnit().getSource();
	}

	public String getPackageName() {
		return packageName;
	}

	public String getSourceFolder() {
		return sourceFolder;
	}

	public TypeDeclaration getTypeDeclaration() {
		return typeDeclaration;
	}

	public void setTypeDeclaration(TypeDeclaration typeDeclaration) {
		this.typeDeclaration = typeDeclaration;
	}

}