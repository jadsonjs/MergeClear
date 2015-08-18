/**
 *
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE –- UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA -– DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 *
 */
package br.ufrn.spl.ev.models.changeloghistorymodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a method from repository
 *
 * @author jadson
 * @author gleydson
 *
 * @since 26/02/2013
 * @version 1.0 - Class Creation
 */
public class MethodChangeLog extends CodeAssetChangeLog {

	/** The name of the method, example: a1*/
	private String name;
	
	private String changeType = ChangeTypeRepository.ADDED;
	
	
	/** The asserts associated with this change. Assets are classes, methods, attributes and code pieces*/
	private List<CodePieceChangeLog> codepieces = new ArrayList<CodePieceChangeLog>();

	/**  A bidirectional relationship with the ClassChangeLog */
	private ClassChangeLog classChangeLog;
	
	/**
	 * Annotations associated with this class the evolved over the time
	 */
	private List<AnnotationChangeLog> annotations = new ArrayList<AnnotationChangeLog>();
	
	
	/**
	 * Annotations associated with this class the evolved over the time
	 */
	private List<ExceptionChangeLog> exceptions = new ArrayList<ExceptionChangeLog>();
	
	
	public MethodChangeLog(){
		
	}
	
	public MethodChangeLog(String name, String signature, String changeType){
		this.name = name;
		setSignature(signature);
		this.changeType = changeType;
	}
	
	
	/** Added a code piece to this method change log*/
	public void addCodePieceChangeLog(CodePieceChangeLog codePieceChangeLog) {
		getCodepieces().add(codePieceChangeLog);
		codePieceChangeLog.setMethodChangeLog(this); // a bidirectional relationship
	}
	

	/** Added a annotation to this method change log */
	public void addAnnotationChangeLog(AnnotationChangeLog annotationChangeLog) {
		getAnnotations().add(annotationChangeLog);
		annotationChangeLog.setMethodChangeLog(this); // a bidirectional relationship
	}
	
	/** Added a exception to this method change log */
	public void addExceptionChangeLog(ExceptionChangeLog exceptionChangeLog) {
		getExceptions().add(exceptionChangeLog);
		exceptionChangeLog.setMethodChangeLog(this); // a bidirectional relationship
	}
	
	
	@Override
	public String toString() {
		return "MethodChangeLog [name=" + name + ", signature=" + signature
				+ ", changeType=" + changeType + ", class=" + ( getClassChangeLog() != null ? getClassChangeLog().getSignature() : "" )
				+ "]";
	}

	/** Two methods are equals if have the same name and signature, and of course, are in the same class */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((classChangeLog == null) ? 0 : classChangeLog.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((signature == null) ? 0 : signature.hashCode());
		return result;
	}
	

	/** Two methods are equals if have the same name and signature, and of course, are in the same class */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodChangeLog other = (MethodChangeLog) obj;
		if (classChangeLog == null) {
			if (other.classChangeLog != null)
				return false;
		} else if (!classChangeLog.equals(other.classChangeLog))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature))
			return false;
		return true;
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
	
	
	// setters and getters ///
	
	public String getName() {
		return name;
	}
	
	public String getFullName() {
		return this.getClass().getSimpleName()+": "+getName();
	}

	public void setName(String name) {
		this.name = name;
	}

	/** Example: /br/ufrn/example/Test.java$void#a1(double,float) */
	@Override
	public String getSignature() {
		return classChangeLog.getSignature()+"$"+ signature;
	}

	
	/*** Method set the conflict in itself and if the father  ClassChangeLog. */
	@Override
	public void setHierarchicalDirectConflict(boolean directlyConflicting) {
		this.directlyConflicting = directlyConflicting;
		if(this.getClassChangeLog() != null)
			this.getClassChangeLog().setHierarchicalDirectConflict(directlyConflicting);
	}
	
	/*** Method set the conflict in itself and if the father  ClassChangeLog. */
	@Override
	public void setHierarchicalIndirectConflict(boolean indirectlyConflicting, int depthLevel) {
		this.indirectlyConflicting = indirectlyConflicting;
		if(this.getClassChangeLog() != null) {
			this.getClassChangeLog().setHierarchicalIndirectConflict(indirectlyConflicting,depthLevel);
			setDepthLevel(depthLevel);
		}
	}
	

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public List<CodePieceChangeLog> getCodepieces() {
		if(codepieces == null)
			codepieces = new ArrayList<CodePieceChangeLog>();
		return codepieces;
	}
	
	public void setCodepieces(List<CodePieceChangeLog> codepieces) {
		this.codepieces = codepieces;
	}
	
	public List<AnnotationChangeLog> getAnnotations() {
		if (annotations == null)
			annotations = new ArrayList<AnnotationChangeLog>();
		return annotations;
	}

	public void setAnnotations(List<AnnotationChangeLog> annotations) {
		this.annotations = annotations;
	}
	
	public List<ExceptionChangeLog> getExceptions() {
		if (exceptions == null)
			exceptions = new ArrayList<ExceptionChangeLog>();
		return exceptions;
	}

	public void setExceptions(List<ExceptionChangeLog> exceptions) {
		this.exceptions = exceptions;
	}

	public ClassChangeLog getClassChangeLog() {
		return classChangeLog;
	}

	public void setClassChangeLog(ClassChangeLog classChangeLog) {
		this.classChangeLog = classChangeLog;
	}
	
	@Override
	public ChangeLog getChangelog() {
		return getClassChangeLog().getChangelog();
	}
}
