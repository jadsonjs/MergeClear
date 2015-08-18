/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.models.changeloghistorymodel;

/**
 * <p>Represents a change on the Exceptions</p>
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 17/12/2013
 *
 */
public class ExceptionChangeLog extends AssetChangeLog{

	/** The name of the class of exception, Example : RuntimeException */
	private String name;
	
	/** A bidirectional relationship with the change log. */
	private MethodChangeLog methodChangeLog;

	
	private String changeType = ChangeTypeRepository.ADDED;
	
	
	public ExceptionChangeLog(String name, String signature, String changeType){
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
	 * Example : java.lang.RuntimeException
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
		if (this.getMethodChangeLog() != null)
			this.getMethodChangeLog().setHierarchicalDirectConflict(directlyConflicting);
	}

	@Override
	public void setHierarchicalIndirectConflict(boolean indirectlyConflicting, int depthLevel) {
		this.indirectlyConflicting = indirectlyConflicting;
		if (this.getMethodChangeLog() != null)
			this.getMethodChangeLog().setHierarchicalIndirectConflict(indirectlyConflicting, depthLevel);
		
	}

	public MethodChangeLog getMethodChangeLog() {
		return methodChangeLog;
	}

	public void setMethodChangeLog(MethodChangeLog methodChangeLog) {
		this.methodChangeLog = methodChangeLog;
	}


	public String getChangeType() {
		return changeType;
	}


	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}


	public void setName(String name) {
		this.name = name;
	}


	@Override
	public ChangeLog getChangelog() {
		return getMethodChangeLog().getChangelog();
	}
	
	
}
