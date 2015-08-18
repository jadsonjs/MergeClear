/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.models.changeloghistorymodel;

/**
 * <p>Represents change on the class implementation</p>
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 17/12/2013
 *
 */
public class ImplementationsChangeLog extends AssetChangeLog{

	/** The name of the class of implementation, Example : Comparable */
	private String name;
	
	/** A bidirectional relationship with the change log. */
	private ClassChangeLog classChangeLog;

	private String changeType = ChangeTypeRepository.ADDED;
	
	public ImplementationsChangeLog(String name, String signature, String changeType){
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
	 * Example : java.lang.Comparable
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
	}

	@Override
	public void setHierarchicalIndirectConflict(boolean indirectlyConflicting, int depthLevel) {
		this.indirectlyConflicting = indirectlyConflicting;
		if (this.getClassChangeLog() != null)
			this.getClassChangeLog().setHierarchicalIndirectConflict(indirectlyConflicting, depthLevel);
		
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


	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public ChangeLog getChangelog() {
		return getClassChangeLog().getChangelog();
	}
}
