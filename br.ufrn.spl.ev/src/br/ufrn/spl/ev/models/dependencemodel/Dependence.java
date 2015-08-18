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
public class Dependence {
	
	/** The change log we are analysing */
	private String keyChangeLog;
	
	private String identify;
	
	/** The change logs that depend of key change log */
	private List<AssetDependence> assetsDependences;
	
	
	public Dependence(ChangeLog key, ChangeLog key2, AssetChangeLog asset) {
		this.keyChangeLog = key.getFullDescription();
		this.identify = key.getIdentify();
		assetsDependences = new ArrayList<AssetDependence>();
		assetsDependences.add( new AssetDependence(key2, asset) );
	}


	public Dependence(ChangeLog key, ChangeLog key2, List<AssetChangeLog> values) {
		this.keyChangeLog = key.getFullDescription();
		assetsDependences = new ArrayList<AssetDependence>();
		assetsDependences.add( new AssetDependence(key2, values) );
	}


	private AssetDependence get(ChangeLog key) {
		for (AssetDependence asset : assetsDependences) {
			if( asset.getChangeLogDependentKey().equals(key.getFullDescription() ) )
				return asset;
		}
		return null;
	}
	
	
	public Object put(ChangeLog key2, AssetChangeLog assetChangeLog) {
		AssetDependence assetDependence = get(key2);
		if(assetDependence == null)
			assetsDependences.add( new AssetDependence(key2, assetChangeLog) );
		else{
			assetDependence.addAsset(assetChangeLog);
		}
		return null;
	}

	
	public Object putAll(ChangeLog key2, List<AssetChangeLog> values) {
		AssetDependence assetDependence = get(key2);
		if(assetDependence == null){
			assetsDependences.add( new AssetDependence(key2, values) );
		}else{
			assetDependence.addAllAsset(values);
		}
		return null;
	}
	
	
	public String getKeyChangeLog() {
		return keyChangeLog;
	}

	public List<AssetDependence> getAssetDependents() {
		return assetsDependences;
	}
	
	public String getIdentify() {
		return identify;
	}

}
