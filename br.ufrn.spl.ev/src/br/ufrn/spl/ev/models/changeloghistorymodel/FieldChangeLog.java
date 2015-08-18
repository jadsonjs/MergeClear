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
 * Represents a Field in our change log model
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class FieldChangeLog extends CodeAssetChangeLog{

	/** The name of the asset */
	private String name;
	
	private String changeType = ChangeTypeRepository.ADDED;
	
	
	/**  A bidirectional relationship with the ClassChangeLog */
	private ClassChangeLog classChangeLog;

	
	/**
	 * Annotations associated with this class the evolved over the time
	 */
	private List<AnnotationChangeLog> annotations = new ArrayList<AnnotationChangeLog>();
	
	
	
	public FieldChangeLog(){
		
	}
	
	public FieldChangeLog(String name, String signature, String changeType){
		this.name = name;
		setSignature(signature);
		this.changeType = changeType;
	}
	
	
	@Override
	public String toString() {
		return "FieldChangeLog [name=" + name + ", signature=" + signature
				+ ", changeType=" + changeType + "]";
	}
	
	/** Two fields are equals if have the same name and signature, and of course, are in the same class */
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

	/** Two fields are equals if have the same name and signature, and of course, are in the same class */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FieldChangeLog other = (FieldChangeLog) obj;
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

	
	public boolean isAdded(){
		return this.getChangeType().equals(ChangeTypeRepository.ADDED);
	}
	
	public boolean isDelete(){
		return this.getChangeType().equals(ChangeTypeRepository.DELETE);
	}
	
	public boolean isUpdated(){
		return this.getChangeType().equals(ChangeTypeRepository.UPDATED);
	}

	/** Added a annotation to this class change log */
	public void addAnnotationChangeLog(AnnotationChangeLog annotationChangeLog) {
		getAnnotations().add(annotationChangeLog);
		annotationChangeLog.setFieldChangeLog(this); // a bidirectional relationship
	}
	
	
	// setters and getters ///
	
	public String getName() {
		return name;
	}
	
	public String getFullName() {
		return this.getClass().getSimpleName()+": "+getName();
	}

	/** Example: /br/ufrn/example/Test.java$int:a1 */
	@Override
	public String getSignature() {
		return classChangeLog.getSignature()+"$"+ signature;
	}
	
	
	/*** Field set the conflict in itself and if the father  ClassChangeLog. */
	@Override
	public void setHierarchicalDirectConflict(boolean directlyConflicting) {
		this.directlyConflicting = directlyConflicting;
		if(this.getClassChangeLog() != null)
			this.getClassChangeLog().setHierarchicalDirectConflict(directlyConflicting);
	}
	
	/*** Field set the conflict in itself and if the father  ClassChangeLog. */
	@Override
	public void setHierarchicalIndirectConflict(boolean indirectlyConflicting, int depthLevel) {
		this.indirectlyConflicting = indirectlyConflicting;
		if(this.getClassChangeLog() != null) {
			this.getClassChangeLog().setHierarchicalIndirectConflict(indirectlyConflicting,depthLevel);
			setDepthLevel(depthLevel);
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public ClassChangeLog getClassChangeLog() {
		return classChangeLog;
	}

	public void setClassChangeLog(ClassChangeLog classChangeLog) {
		this.classChangeLog = classChangeLog;
	}

	public String getChangeType() {
		return changeType;
	}
	
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	
	public List<AnnotationChangeLog> getAnnotations() {
		if (annotations == null)
			annotations = new ArrayList<AnnotationChangeLog>();
		return annotations;
	}

	public void setAnnotations(List<AnnotationChangeLog> annotations) {
		this.annotations = annotations;
	}

	@Override
	public ChangeLog getChangelog() {
		return getClassChangeLog().getChangelog();
	}
}
