/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE – UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA – DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.models.changeloghistorymodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents assets associated with the feature that change.
 * 
 * @author Jadson
 * 
 * @since 04/11/2012
 * @version
 * 
 */
public class ClassChangeLog extends CodeAssetChangeLog {

	/** The name of the asset, Example : Class.java */
	private String name;

	/**
	 * The path since the root of the *** project ****. Example:
	 * /Project/package/sub_package/Class.java
	 */
	private String path;

	private String changeType = ChangeTypeRepository.ADDED;

	
	
	/** The number of revision in SVN associated with the change. */
	private String revision;

	/**
	 * The asserts associated with this change. Assets are classes, methods,
	 * attributes and code pieces
	 */
	private List<FieldChangeLog> fields = new ArrayList<FieldChangeLog>();

	/**
	 * The asserts associated with this change. Assets are classes, methods,
	 * attributes and code pieces
	 */
	private List<MethodChangeLog> methods = new ArrayList<MethodChangeLog>();

	
	/**
	 * Annotations associated with this class the evolved over the time
	 */
	private List<AnnotationChangeLog> annotations = new ArrayList<AnnotationChangeLog>();
	
	
	/**
	 * Implementations associated with this class the evolved over the time
	 */
	private List<ImplementationsChangeLog> implementations = new ArrayList<ImplementationsChangeLog>();
	
	/**
	 * Implementations associated with this class the evolved over the time
	 */
	private ExtensionChangeLog extension;
	
	
	/** A bidirectional relationship with the change log. */
	private ChangeLog changelog;

	

	public ClassChangeLog() {

	}

	public ClassChangeLog(String name, String path, String signature, String changeType, String revision) {
		this.changeType = changeType;
		this.name = name;
		this.path = path;
		setSignature(signature);
		this.revision = revision;
	}

	/** Added a method to this class change log */
	public void addMethodChangeLog(MethodChangeLog methodChangeLog) {
		getMethods().add(methodChangeLog);
		methodChangeLog.setClassChangeLog(this); // a bidirectional relationship
	}

	/** Added a method to this class change log */
	public void addFieldChangeLog(FieldChangeLog fieldChangeLog) {
		getFields().add(fieldChangeLog);
		fieldChangeLog.setClassChangeLog(this); // a bidirectional relationship
	}

	/** Added a annotation to this class change log */
	public void addAnnotationChangeLog(AnnotationChangeLog annotationChangeLog) {
		getAnnotations().add(annotationChangeLog);
		annotationChangeLog.setClassChangeLog(this); // a bidirectional relationship
	}
	
	/** Added a implementation to this class change log */
	public void addImplementationChangeLog(ImplementationsChangeLog implementationChangeLog) {
		getImplementations().add(implementationChangeLog);
		implementationChangeLog.setClassChangeLog(this); // a bidirectional relationship
	}
	
	
	/** Two classes are equals if have the same name and absolute path */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((getAbsolutePath() == null) ? 0 : getAbsolutePath().hashCode());
		return result;
	}

	/** Two classes are equals if have the same name and absolute path */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassChangeLog other = (ClassChangeLog) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (getSignature() == null) {
			if (other.getSignature() != null)
				return false;
		} else if (!getSignature().equals(other.getSignature()))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "ClassChangeLog [name=" + name + ", path=" + path + ", changeType=" + changeType + "]";
	}

	// / setters and getters ///

	public String getName() {
		return name;
	}

	public String getFullName() {
		return this.getClass().getSimpleName() + ": " + getName();
	}

	/**
	 * The path with out the project name Example: path:
	 * trunk/SIGAA/br/ufrn/example/Test.java Signature:
	 * br/ufrn/example/Test.java
	 */
	@Override
	public String getSignature() {
		return signature;
	}

	/*** Class set the conflict in itself and if the father ChangeLog. */
	@Override
	public void setHierarchicalDirectConflict(boolean directlyConflicting) {
		this.directlyConflicting = directlyConflicting;
		if (this.getChangelog() != null)
			this.getChangelog().setHierarchicalDirectConflict(directlyConflicting);
	}

	/*** Class set the conflict in itself and if the father ChangeLog. */
	@Override
	public void setHierarchicalIndirectConflict(boolean indirectlyConflicting, int depthLevel) {
		this.indirectlyConflicting = indirectlyConflicting;
		if (this.getChangelog() != null)
			this.getChangelog().setHierarchicalIndirectConflict(indirectlyConflicting, depthLevel);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	/**
	 * Return the path absolute. This is the path without the name of project.
	 * 
	 * @return if path = /Source/br/ufrn/example/Test.java the absolute path is
	 *         /br/ufrn/example/Test.java
	 */
	public String getAbsolutePath() {
		if (path == null || path.trim().equals(""))
			return path;
		int indexOfSecondSlash = path.indexOf("/", 1);
		if (indexOfSecondSlash <= 0)
			return path;

		StringBuilder abssolutePath = new StringBuilder(path);
		abssolutePath.delete(0, indexOfSecondSlash); // delete from the first'/'
														// until the first '/'
														// starting form the
														// second character.
		return abssolutePath.toString();
	}

	public boolean isAdded() {
		return this.getChangeType().equals(ChangeTypeRepository.ADDED);
	}

	public boolean isDelete() {
		return this.getChangeType().equals(ChangeTypeRepository.DELETE);
	}

	public boolean isUpdated() {
		return this.getChangeType().equals(ChangeTypeRepository.UPDATED);
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public List<FieldChangeLog> getFields() {
		if (fields == null)
			fields = new ArrayList<FieldChangeLog>();
		return fields;
	}

	public void setFields(List<FieldChangeLog> fields) {
		this.fields = fields;
	}

	public List<MethodChangeLog> getMethods() {
		if (methods == null)
			methods = new ArrayList<MethodChangeLog>();
		return methods;
	}
	
	public List<AnnotationChangeLog> getAnnotations() {
		if (annotations == null)
			annotations = new ArrayList<AnnotationChangeLog>();
		return annotations;
	}

	public void setAnnotations(List<AnnotationChangeLog> annotations) {
		this.annotations = annotations;
	}

	public List<ImplementationsChangeLog> getImplementations() {
		if (implementations == null)
			implementations = new ArrayList<ImplementationsChangeLog>();
		return implementations;
	}

	public void setImplementations(List<ImplementationsChangeLog> implementation) {
		this.implementations = implementation;
	}

	public ExtensionChangeLog getExtension() {
		return extension;
	}

	public void setExtension(ExtensionChangeLog extension) {
		this.extension = extension;
	}

	public void setMethods(List<MethodChangeLog> methods) {
		this.methods = methods;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public ChangeLog getChangelog() {
		return changelog;
	}

	public void setChangelog(ChangeLog changelog) {
		this.changelog = changelog;
	}
	
	/**
	 * Return qualified class name
	 * 
	 * @return
	 */
	public String getClassName() {
		int indexSourceFolder = getSignature().indexOf("/") + 1;
		String classFullPath = getSignature().replace("/", ".");
		return classFullPath.substring(indexSourceFolder, classFullPath.length() - ".java".length());
	}

}
