/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.models.dependencemodel;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class AssetDependence {
	
	/** The change log where we have the dependence */
	private String changeLogDependentKey;
	
	private String identify;
	
	/** The change logs that depend of key change log */
	private List<String> assetsDependents;
	
	
	public AssetDependence(ChangeLog changeLog, AssetChangeLog asset) {
		this.changeLogDependentKey = changeLog.getFullDescription();
		this.identify = changeLog.getIdentify();
		assetsDependents = new ArrayList<String>();
		assetsDependents.add(asset.getSignature());
	}

	public AssetDependence(ChangeLog changeLog, List<AssetChangeLog> dependences) {
		this.changeLogDependentKey = changeLog.getFullDescription();
		assetsDependents = new ArrayList<String>();
		
		for (AssetChangeLog asset : dependences) {
			assetsDependents.add(asset.getSignature());
		}
	}
	
	
	public void addAsset(AssetChangeLog asset) {
		if(assetsDependents == null)
			assetsDependents = new ArrayList<String>();
		assetsDependents.add(asset.getSignature());
	}
	
	public void addAllAsset(List<AssetChangeLog> asset) {
		if(assetsDependents == null)
			assetsDependents = new ArrayList<String>();
		for (AssetChangeLog assetChangeLog : asset) {
			assetsDependents.add(assetChangeLog.getSignature());
		}
		
	}

	public String getChangeLogDependentKey() {
		return changeLogDependentKey;
	}

	public List<String> getChangeLogsDependents() {
		return assetsDependents;
	}

	public String getIdentify() {
		return identify;
	}
	
}
