/**
 *
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE –- UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA -– DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 *
 */
package br.ufrn.spl.ev.models.conflictmodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;

/**
 * Represent a Conflict Model.
 * 
 * @author Gleydson
 * 
 * @version 1.0 - Class Creation
 */
@XmlRootElement
public class ConflictModel {

	
	private List<Conflict> listOfConflicts = new ArrayList<Conflict>();

	
	public void addDirectConflict(AssetChangeLog assetChangeLogSource, AssetChangeLog assetChangeTarget) {
		
		Conflict c = new Conflict(ConflictType.DIRECT, assetChangeLogSource);
		
		if (!exists(c)){
			c.addAssetConflicting(assetChangeTarget);
			listOfConflicts.add(c);
		}else{
			c = getConflict(c);
			c.addAssetConflicting(assetChangeTarget);
		}
	}

	
	public void addIndirectConflictByEvolution(AssetChangeLog assetChangeLogSource, AssetChangeLog assetChangeTarget) {
		Conflict c = new Conflict(ConflictType.INDIRECT, ConflictSubType.INDIRECT_BY_EVOLUTION, assetChangeLogSource);
		
		if (!exists(c)){
			c.addAssetConflicting(assetChangeTarget);
			listOfConflicts.add(c);
		}else{
			c = getConflict(c);
			c.addAssetConflicting(assetChangeTarget);
		}
	}
	
	
	public void addIndirectConflictByRelationShip(AssetChangeLog assetChangeLogSource, AssetChangeLog assetChangeTarget) {
		Conflict c = new Conflict(ConflictType.INDIRECT, ConflictSubType.INDIRECT_BY_RELATIONSHIP, assetChangeLogSource);
		
		if (!exists(c)){
			c.addAssetConflicting(assetChangeTarget);
			listOfConflicts.add(c);
		}else{
			c = getConflict(c);
			c.addAssetConflicting(assetChangeTarget);
		}
	}
	
	
	public void addTextualConflict(ClassChangeLog assetChangeLogSource, ClassChangeLog assetChangeTarget) {
		Conflict c = new Conflict(ConflictType.PSEUDO, assetChangeLogSource);
		
		if (!exists(c)){
			c.addAssetConflicting(assetChangeTarget);
			listOfConflicts.add(c);
		}else{
			c = getConflict(c);
			c.addAssetConflicting(assetChangeTarget);
		}
	
	}
	
	/** Return all conflicts of some part */
	public List<Conflict> getConflictsOfCertainType(ConflictType type) {
		ArrayList<Conflict> list = new ArrayList<Conflict>();
		for (Conflict c : getListOfConflicts()) {
			if (c.getConflictType().equals(type))
				list.add(c);
		}
		return list;
	}
	
	/** Return all conflicts of some part */
	public List<Conflict> getConflictsOfCertainType(ConflictType type, ConflictSubType subType) {
		ArrayList<Conflict> list = new ArrayList<Conflict>();
		for (Conflict c : getListOfConflicts()) {
			if (c.getConflictType().equals(type) && c.getConflictSubType() != null && c.getConflictSubType().equals(subType))
				list.add(c);
		}
		return list;
	}
	
	
	public boolean exists(Conflict conflict) {
		return listOfConflicts.contains(conflict);
	}
	
	private Conflict getConflict(Conflict c) {
		return listOfConflicts.get(listOfConflicts.indexOf(c));
	}

	public boolean isEmpty() {
		return listOfConflicts.size() == 0;
	}

	public void load(List<Conflict> list) {
		this.listOfConflicts = list;
	}

	public List<Conflict> get() {
		return listOfConflicts;
	}

	public List<Conflict> getListOfConflicts() {
		return listOfConflicts;
	}

	public void setListOfConflicts(List<Conflict> listOfConflicts) {
		this.listOfConflicts = listOfConflicts;
	}


	/***
	 * Clear the conflict of a certain type before execute each type of conflict analysis
	 * @param type
	 */
	public void clearConflicts(ConflictType type)  {
		
		Iterator<Conflict> it = listOfConflicts.iterator();
		int count = 0;
		while ( it.hasNext() ) {
			Conflict c = it.next();
			if ( c.getConflictType().equals(type) ) {
				it.remove();
				count++;
			}
		}
		System.out.println("Erased " + count + " " + type + " conflitcs from model") ;
	}
	
}
