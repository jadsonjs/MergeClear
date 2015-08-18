/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.models.changeloghistorymodel;

/**
 * Represents a change on annotation of method, field or class
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 17/12/2013
 *
 */
public class AnnotationChangeLog extends AssetChangeLog {

	/** The name of the class of extension, Example : Override */
	private String name;
	
	/** A bidirectional relationship if it is a annotation of a class. */
	private ClassChangeLog classChangeLog;

	/** A bidirectional relationship if it is a annotation of a method. */
	private MethodChangeLog methodChangeLog;
	
	/** A bidirectional relationship if it is a annotation of a field. */
	private FieldChangeLog fieldChangeLog;
	
	private String changeType = ChangeTypeRepository.ADDED;
	
	public AnnotationChangeLog(String name, String signature, String changeType){
		this.name = name;
		setSignature(signature);
		this.changeType = changeType;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getFullName() {
		return getName();
	}

	/**
	 * Example : java.lang.Override
	 * 
	 * @see br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog#getSignature()
	 */
	@Override
	public String getSignature() {
		return signature;
	}

	@Override
	public void setHierarchicalDirectConflict(boolean directlyConflicting) {
		this.directlyConflicting = directlyConflicting;
		if (this.getClassChangeLog() != null)
			this.getClassChangeLog().setHierarchicalDirectConflict(directlyConflicting);
		if (this.getMethodChangeLog() != null)
			this.getMethodChangeLog().setHierarchicalDirectConflict(directlyConflicting);
		if (this.getFieldChangeLog() != null)
			this.getFieldChangeLog().setHierarchicalDirectConflict(directlyConflicting);
	}

	@Override
	public void setHierarchicalIndirectConflict(boolean indirectlyConflicting, int depthLevel) {
		this.indirectlyConflicting = indirectlyConflicting;
		if (this.getClassChangeLog() != null)
			this.getClassChangeLog().setHierarchicalIndirectConflict(indirectlyConflicting, depthLevel);
		if (this.getMethodChangeLog() != null)
			this.getMethodChangeLog().setHierarchicalIndirectConflict(indirectlyConflicting, depthLevel);
		if (this.getFieldChangeLog() != null)
			this.getFieldChangeLog().setHierarchicalIndirectConflict(indirectlyConflicting, depthLevel);
	}

	public ClassChangeLog getClassChangeLog() {
		return classChangeLog;
	}

	public void setClassChangeLog(ClassChangeLog classChangeLog) {
		this.classChangeLog = classChangeLog;
		this.methodChangeLog = null;
		this.fieldChangeLog = null;
	}

	public MethodChangeLog getMethodChangeLog() {
		return methodChangeLog;
	}

	public void setMethodChangeLog(MethodChangeLog methodChangeLog) {
		this.methodChangeLog = methodChangeLog;
		this.classChangeLog = null;
		this.fieldChangeLog = null;
	}

	public FieldChangeLog getFieldChangeLog() {
		return fieldChangeLog;
	}

	public void setFieldChangeLog(FieldChangeLog fieldChangeLog) {
		this.fieldChangeLog = fieldChangeLog;
		this.classChangeLog = null;
		this.methodChangeLog = null;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	
	@Override
	public ChangeLog getChangelog() {
		return getClassChangeLog().getChangelog();
	}
	
}