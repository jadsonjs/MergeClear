/**
 *
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE –- UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA -– DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 *
 */
package br.ufrn.spl.ev.models.conflictmodel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;

/**
 * <p>Represent a conflict of assets.</p>
 * 
 * <p>So, if this represents a conflict, I ask you. What is a conflict?</p>
 * 
 * <p>Conflict is a asset in the source that are in a conflict of some time with one or several assets in the target.</p>
 * 
 * @author  Gleydson
 * @version 2.0 - Jadson - Change the initial mean of a conflict for the tool.
 * 
 */
@XmlRootElement
public class Conflict {

	/** The type of the conflict */
	private ConflictType conflictType;
	
	/** The sub type of the conflict ( If exists, it is optional ) */
	private ConflictSubType conflictSubType;
	
	/** The asset in the source */
	private AssetConflict assetConflict;

	/** Can have conflicts with several assets in the target */
	private List<AssetConflict> targetConflictsRelated;



	public Conflict() {

	}

	/***
	 * <p>Constructor when we specify just the main conflict type </p>
	 *
	 * Created on 29/03/2015
	 * @param conflictType
	 * @param assetChangeLogSource
	 */
	public Conflict(ConflictType conflictType, AssetChangeLog assetChangeLogSource) {
		this.conflictType = conflictType;
		this.assetConflict = new AssetConflict(assetChangeLogSource.getClass().getSimpleName(), assetChangeLogSource.getSignature()
				, assetChangeLogSource.getChangelog().getIdentify() +" - "+ assetChangeLogSource.getChangelog().getDescription(), assetChangeLogSource.getDepthLevel());
	}

	/**
	 * <p>Constructor when we specify the main the sub conflict type </p>
	 *
	 * Created on 29/03/2015
	 * @param conflictType
	 * @param conflictSubType
	 * @param assetChangeLogSource
	 */
	public Conflict(ConflictType conflictType, ConflictSubType conflictSubType, AssetChangeLog assetChangeLogSource) {
		this.conflictType = conflictType;
		this.conflictSubType = conflictSubType;
		this.assetConflict = new AssetConflict(assetChangeLogSource.getClass().getSimpleName(), assetChangeLogSource.getSignature()
				, assetChangeLogSource.getChangelog().getIdentify() +" - "+ assetChangeLogSource.getChangelog().getDescription(), assetChangeLogSource.getDepthLevel());
	}


	public void addAssetConflicting(AssetChangeLog assetChangeTarget) {
		if(targetConflictsRelated == null)
			targetConflictsRelated = new ArrayList<AssetConflict>();
		
		ChangeLog chageLog = assetChangeTarget.getChangelog();
		
		String chageLogDescription = "";
		
		// here we test if the indirect conflict is not present on change log
		if(chageLog != null)
			chageLogDescription = assetChangeTarget.getChangelog().getIdentify()+" - "+ assetChangeTarget.getChangelog().getDescription();
		
		AssetConflict conflict = new AssetConflict(assetChangeTarget.getClass().getSimpleName(), assetChangeTarget.getSignature()
				, chageLogDescription, assetChangeTarget.getDepthLevel());
		
		if(! targetConflictsRelated.contains(conflict))
			targetConflictsRelated.add(  conflict );
		
	}

	
	
	
	
	/** A conflict is equals to other if is over the same atomicOperationSource and some conflictType */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assetConflict == null) ? 0 : assetConflict.hashCode());
		result = prime * result + ((conflictType == null) ? 0 : conflictType.hashCode());
		return result;
	}

	/** A conflict is equals to other if is over the same atomicOperationSource and same conflictType and conflictSubType */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Conflict other = (Conflict) obj;
		if (assetConflict == null) {
			if (other.assetConflict != null)
				return false;
		} else if (!assetConflict.equals(other.assetConflict))
			return false;
		if (conflictType != other.conflictType)
			return false;
		if (conflictSubType != other.conflictSubType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Conflict [" + "Type= " + conflictType + ", " + " Source= " + assetConflict + " Targe= " + assetConflict+ "]";
	}

	
	/* gets e sets */
	public ConflictType getConflictType() { return conflictType; }
	public void setConflictType(ConflictType conflictType) {this.conflictType = conflictType;}
	public AssetConflict getAssetConflict() {return assetConflict;}
	public void setAssetConflict(AssetConflict assetConflict) {this.assetConflict = assetConflict;}
	public List<AssetConflict> getTargetConflictsRelated() {return targetConflictsRelated;}
	public void setTargetConflictsRelated(List<AssetConflict> targetConflictsRelated) {this.targetConflictsRelated = targetConflictsRelated;}
	public ConflictSubType getConflictSubType() {return conflictSubType;	}
	public void setConflictSubType(ConflictSubType conflictSubType) {this.conflictSubType = conflictSubType;	}

	

}