package br.ufrn.spl.ev.models.conflictmodel;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;

public class FeatureConflict {
	
	/**
	 * One of the features in target that are conflict with the feature in source
	 * 
	 * Identify the feature conflict
	 */
	private FeatureChangeLog targetFeature;
	
	/** The list of asset(source,target) the are in conflict and are related with the features. */
	private List<Conflict> listAssetConflicts = new ArrayList<Conflict>();
	
	
	public FeatureConflict() {
		
	}
	
	public FeatureConflict(FeatureChangeLog targetFeature) {
		this.targetFeature = targetFeature;
	}
	
	public void addAssetConflict(Conflict assetConflict) {
		listAssetConflicts.add(assetConflict);
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((targetFeature == null) ? 0 : targetFeature.hashCode());
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
		FeatureConflict other = (FeatureConflict) obj;
		if (targetFeature == null) {
			if (other.targetFeature != null)
				return false;
		} else if (!targetFeature.equals(other.targetFeature))
			return false;
		return true;
	}



	// sets e gets //
	public FeatureChangeLog getTargetFeature() {return targetFeature;}
	public void setTargetFeature(FeatureChangeLog targetFeature) {this.targetFeature = targetFeature;}
	public List<Conflict> getListAssetConflicts() {return listAssetConflicts;}
	public void setListAssetConflicts(List<Conflict> listAssetConflicts) {this.listAssetConflicts = listAssetConflicts;}
	
}
