package br.ufrn.spl.ev.models.changeloghistorymodel;

import java.util.ArrayList;
import java.util.List;


public class FeatureChangeLog extends AssetChangeLog{
	
	/** The name of the feature */
	private String name;
	
	/** The name of the parent of this feature, if have one.*/
	private FeatureChangeLog parent;
	
	/** The type of a features */
	private FeatureType type;
	
	/** The  description of a features, it's give by the developer */
	private String description;
	
	
	/** A set of changes the are associated with this feature   */
	private List<ChangeLog> changelogs;
	

	public ChangeLog addChangeLog(ChangeLog changeLog) {
		changeLog.setFeature(this); // a bidirectional relationship
		getChangelogs().add(changeLog);
		return changeLog;
	}
	
	
	/**
	 * 
	 */
	public FeatureChangeLog() {
	}
	
	/**
	 * @param name
	 */
	public FeatureChangeLog(String name) {
		this.name = name;
	}
	
	
	/**
	 * @param name
	 * @param parent
	 * @param type
	 */
	public FeatureChangeLog(String name, FeatureChangeLog parent, FeatureType type) {
		this.name = name;
		this.parent = parent;
		this.type = type;
	}
	
	/**
	 * @param name
	 * @param parent
	 * @param type
	 */
	public FeatureChangeLog(String name, FeatureChangeLog parent, FeatureType type, String description) {
		this.name = name;
		this.parent = parent;
		this.type = type;
		this.description = description;
	}
	
	
	@Override
	public String toString() {
		return "Feature [name=" + name + ", parent=" + parent + ", type="+ type + ", ]";
	}



	/** Two Features are equals if they have the same name */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	
	/** Two Features are equals if they have the same name */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeatureChangeLog other = (FeatureChangeLog) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	

	public FeatureChangeLog getParent() {
		return parent;
	}

	public void setParent(FeatureChangeLog parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	
	@Override
	public String getFullName() {
		return name+" ["+type+"]";
	}
	
	@Override
	public String getSignature() {
		return getFullName().replaceAll(" ", "").trim();
	}

	public void setName(String name) {
		this.name = name;
	}
	public FeatureType getType() {
		return type;
	}
	public void setType(FeatureType type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public List<ChangeLog> getChangelogs() {
		if(changelogs == null)
			changelogs = new ArrayList<ChangeLog>();
		return changelogs;
	}

	public void setChangelogs(List<ChangeLog> changelogs) {
		this.changelogs = changelogs;
	}
	
	
	/*** Feature is in the top level, so just set the conflict in itself */
	@Override
	public void setHierarchicalDirectConflict(boolean directlyConflicting) {
		this.directlyConflicting = directlyConflicting;
		
	}
	
	/*** Feature is in the top level, so just set the conflict in itself */
	@Override
	public void setHierarchicalIndirectConflict(boolean indirectlyConflicting, int depthLevel) {
		this.indirectlyConflicting = indirectlyConflicting;
	}


	@Override
	public ChangeLog getChangelog() {
		return null;
	}

}
