/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev.models.dependencemodel;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;

/**
 * Simulate a map of dependences
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class DependenceModel {

	/** Represents a Map */
	private List<Dependence> depedencesMap = new ArrayList<Dependence>();

	
	public Dependence get(ChangeLog key) {
		for (Dependence dependence : depedencesMap) {
			if( dependence.getKeyChangeLog().equals(key.getFullDescription()) )
				return dependence;
		}
		return null;
	}

	
	public void put(ChangeLog key, ChangeLog key2, AssetChangeLog asset) {
		Dependence d = get(key);
		if(d == null)
			depedencesMap.add( new Dependence(key, key2, asset) );
		else{
			d.put(key2, asset);
		}
	}

	
	public void putAll(ChangeLog key, ChangeLog key2, List<AssetChangeLog> values) {
		Dependence d = get(key);
		if(d == null){
			depedencesMap.add( new Dependence(key, key2, values) );
		}else{
			d.putAll(key2, values);
		}
	}

	public List<Dependence> getDepedencesMap() {
		return depedencesMap;
	}
	
}
