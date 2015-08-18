/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE – UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA – DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.models.changeloghistorymodel;

import java.util.ArrayList;
import java.util.List;


/**
 * Represent a single change on the Software Product Line ( usually each task from a configuration management system become a change)
 * 
 * @author Jadson
 * 
 * @since 04/11/2012
 * @version
 *
 */
public class ChangeLog extends AssetChangeLog {
	
	/** Identify a change, usually is a task number */
	private String identify;
	
	/** The type of the change: BUGFIX, UPGRANDING, NEW_FUNCTIONALITY */
	private ChangeLogType type;
	
	/** The version of the system when this change was made.*/
	private String version;
	
	/** The description of this change. ( usually the a task description )*/
	private String description;
		
	/** Information about the change made (give by the user who made the change ) */
	private String changeInformation;

	/** The feature of this change log are related with. A bidirectional relationship with between feature a change log */
	private FeatureChangeLog feature;
	
	/** System module */
	private String module;
	
	
	/** The asserts associated with this change. Assets are classes, methods, attributes and code pieces*/
	private List<ClassChangeLog> classes;

	/**
	 * The number of revision in SVN associated with the change in release
	 * repository.
	 */
	private List<String> revisionRelease = new ArrayList<String>();
	
	public ChangeLog(){
		
	}
	
	
	public ChangeLog(String identify, ChangeLogType type, String version, String description, String changeInformation) {
		this.identify = identify;
		this.type = type;
		this.version = version;
		this.description = description;
		this.changeInformation = changeInformation;
	}
	

	/** Added a class to this change log*/
	public ClassChangeLog addClassChangeLog(ClassChangeLog classChangeLog) {
		classChangeLog.setChangelog(this); // a bidirectional relationship
		getClasses().add(classChangeLog);
		return classChangeLog;
	}
	
	/** Added a list of classes to this change log*/
	public void addClassesChangeLog(List<ClassChangeLog> classesChangeLog) {
		for (ClassChangeLog classChangeLog : classesChangeLog) {
			addClassChangeLog(classChangeLog);
		}
	}
	
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("[CHANGE LOG] ");
		
		if(this.getClasses() != null)
		for (ClassChangeLog classChangeLog : this.getClasses()) {
			
			buffer.append(classChangeLog.toString());
			
			for (FieldChangeLog fieldChangeLog : classChangeLog.getFields()) {
				buffer.append(fieldChangeLog.toString());
			}
			
			for (MethodChangeLog methodChangeLog : classChangeLog.getMethods()) {
				
				buffer.append(methodChangeLog.toString());
				
				for (CodePieceChangeLog codePieceChangeLog : methodChangeLog.getCodepieces()) {
					buffer.append(codePieceChangeLog.toString());
				}
			}
		}
		return buffer.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((identify == null) ? 0 : identify.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChangeLog other = (ChangeLog) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (identify == null) {
			if (other.identify != null)
				return false;
		} else if (!identify.equals(other.identify))
			return false;
		if (type != other.type)
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	
	
	// setters e getters 


	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}
	
	public ChangeLogType getType() {
		return type;
	}

	public void setType(ChangeLogType type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public List<ClassChangeLog> getClasses() {
		if(classes == null)
			classes = new ArrayList<ClassChangeLog>();
		return classes;
	}

	@Override
	public String getName() {
		return identify;
	}
	
	@Override
	public String getFullName() {
		return identify+" ["+type+"]";
	}
	
	/**
	 * Format the change log to the user
	 * Example: VACATION [U] #12355 - Correct the error in the conclusion 
	 */
	public String getFullDescription(){
		return getModule() + "[" + getType() + "] " + "#" + getIdentify() + " - " + getDescription();
	}
	
	@Override
	public String getSignature() {
		return getFullName().replaceAll(" ", "").trim();
	}
	
	/***  ChangeLog set the conflict in itself and if the father feature. */
	@Override
	public void setHierarchicalDirectConflict(boolean directlyConflicting) {
		this.directlyConflicting = directlyConflicting;
		if(this.getFeature() != null)
			this.getFeature().setHierarchicalDirectConflict(directlyConflicting);
	}
	
	/***  ChangeLog set the conflict in itself and if the father feature.*/
	@Override
	public void setHierarchicalIndirectConflict(boolean indirectlyConflicting, int depthLevel) {
		this.indirectlyConflicting = indirectlyConflicting;
		if(this.getFeature() != null)
			this.getFeature().setHierarchicalIndirectConflict(indirectlyConflicting, depthLevel);
	}
	
	
	public void setClasses(List<ClassChangeLog> classes) { this.classes = classes;}

	public FeatureChangeLog getFeature() { return feature;}

	public void setFeature(FeatureChangeLog feature) { this.feature = feature; }
	
	public String getChangeInformation() {return changeInformation;}

	public void setChangeInformation(String changeInformation) {this.changeInformation = changeInformation;}


	public String getModule() {
		return module;
	}


	public void setModule(String module) {
		this.module = module;
	}


	public List<String> getRevisionRelease() {
		return revisionRelease;
	}


	public void setRevisionRelease(List<String> revisionRelease) {
		this.revisionRelease = revisionRelease;
	}
	
	public boolean isConflicting() {
		return isDirectlyConflicting() || isIndirectlyConflicting() || isTextualConflicting();
	}
	
	@Override
	public ChangeLog getChangelog() {
		return this;
	}
	
	public boolean isOnlyDirectConflicting() {
		return isDirectlyConflicting() && !isTextualConflicting() && !isIndirectlyConflicting();
	}
	
	public boolean isOnlyTextualConflicting() {
		return !isDirectlyConflicting() && isTextualConflicting() && !isIndirectlyConflicting();
	}
	
	public boolean isOnlyIndirectConflicting() {
		return !isDirectlyConflicting() && !isTextualConflicting() && isIndirectlyConflicting();
	}
	
	public boolean isDirectAndTextual() {
		return isDirectlyConflicting() && isTextualConflicting() && !isIndirectlyConflicting();
	}
	
	public boolean isDirectAndIndirect() {
		return isDirectlyConflicting() && !isTextualConflicting() && isIndirectlyConflicting();
	}
	
	public boolean isIndirectAndTextual() {
		return !isDirectlyConflicting() && isTextualConflicting() && isIndirectlyConflicting();
	}
	
	public boolean isDirectAndIndirectAndTextual() {
		return isDirectlyConflicting() && isTextualConflicting() && isIndirectlyConflicting();
	}
	
}