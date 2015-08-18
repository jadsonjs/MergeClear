/**
 *
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE –- UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA -– DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 *
 */
package br.ufrn.spl.ev.models.changeloghistorymodel;


/**
 * Represents a code piece of the source code that has change.
 *
 * @author jadson
 *
 * @since 26/02/2013
 * @version 1.0 - Class Creation
 */
public class CodePieceChangeLog extends CodeAssetChangeLog{

	/** 
	 * Identify the code piece
	 * The name of the code piece: methodName_1 , methodName_2, ..., methodName_N */
	private String name;
	
	private String changeType = ChangeTypeRepository.ADDED;
	
	
	/**  A bidirectional relationship with the MethodChangeLog */
	private MethodChangeLog methodChangeLog;
	
	
	public CodePieceChangeLog(){
		
	}
	
	public CodePieceChangeLog(String name, String signature, String changeType){
		this.name = name;
		setSignature(signature);
		this.changeType = changeType;
	}
	
	
	@Override
	public String toString() {
		return "CodePieceChangeLog [name=" + name + ", signature=" + signature
				+ ", changeType=" + changeType + "]";
	}
	

	/** Two methods are equals if have the same name, and of course, are in the same method */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((methodChangeLog == null) ? 0 : methodChangeLog.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((signature == null) ? 0 : signature.hashCode());
		return result;
	}


	/** Two code piece are equals if have the same name, and of course, are in the same method */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CodePieceChangeLog other = (CodePieceChangeLog) obj;
		if (methodChangeLog == null) {
			if (other.methodChangeLog != null)
				return false;
		} else if (!methodChangeLog.equals(other.methodChangeLog))
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
	
	
	/** Example: /br/ufrn/example/Test.java$void#a1(double, float)@a1_1[begin, end]*/
	@Override
	public String getSignature() {
		return methodChangeLog.getSignature()+"@"+signature;
	}
	
	/*** CodePiece set the conflict in itself and if the father  MethodChangeLog. */
	@Override
	public void setHierarchicalDirectConflict(boolean directlyConflicting) {
		this.directlyConflicting = directlyConflicting;
		if(this.getMethodChangeLog() != null)
			this.getMethodChangeLog().setHierarchicalDirectConflict(directlyConflicting);
	}
	
	/*** CodePiece set the conflict in itself and if the father  MethodChangeLog. */
	@Override
	public void setHierarchicalIndirectConflict(boolean indirectlyConflicting, int depthLevel) {
		this.indirectlyConflicting = indirectlyConflicting;
		if(this.getMethodChangeLog() != null)
			this.getMethodChangeLog().setHierarchicalIndirectConflict(indirectlyConflicting, depthLevel);
	}
	

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public MethodChangeLog getMethodChangeLog() {
		return methodChangeLog;
	}

	public void setMethodChangeLog(MethodChangeLog methodChangeLog) {
		this.methodChangeLog = methodChangeLog;
	}

	@Override
	public ChangeLog getChangelog() {
		return getMethodChangeLog().getChangelog();
	}
	
	
}
