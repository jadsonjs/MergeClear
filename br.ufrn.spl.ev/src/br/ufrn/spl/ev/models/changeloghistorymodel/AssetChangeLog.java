/**
 *
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE –- UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA -– DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 *
 */
package br.ufrn.spl.ev.models.changeloghistorymodel;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.spl.ev.util.StringUtils;
import br.ufrn.spl.ev.variability.Variability;

/**
 * Layer Super Type of all asset to the history change log
 * 
 * @author jadson
 * 
 * @since 26/03/2013
 * @version 1.0 - Class Creation
 */
// @XmlSeeAlso({CodeAssetChangeLog.class})
public abstract class AssetChangeLog implements Comparable<AssetChangeLog> {

	/**
	 * VERY IMPORANT: ALL ASSETS HAVE TO BE A SIGNATURE THAT IS THE UNIQUE
	 * IDENTITY OF THE ASSET INSIDE THE PROJECT. Used to compare if two assets
	 * are equal each other
	 * 
	 * For features: name[type] 
	 * For change logs: identify[type] 
	 * For classes for example: "br/ufrn/example/A.java" ( "Source/br/ufrn/example/A.java" have to be equals to "Target/br/ufrn/example/A.java") 
	 * For fields: ClassSignature + '$' + type:name 
	 * For methods: ClassSignature + '$' +void#a1(double,float) or void#a1() or int#a1() 
	 * For code pieces :ClassSignature + MethodSignature + '@' +methodName_1[begin,end]
	 */
	protected String signature;

	/**
	 * Say if the asset has a direct conflict or not, usually can be not
	 * integrated, use the Source Code DIFF instead.
	 */
	protected boolean directlyConflicting = false;

	/**
	 * Say if the asset has a indirect conflict or not, usually can be
	 * integrated but we will suggest to re test it
	 */
	protected boolean indirectlyConflicting = false;

	/**
	 * Textual conflicting is a "light conflict" that usually occurs when you
	 * are using control version systems like SVN This information is just to
	 * count how much conflict is let to detect using our approach. Just classes
	 * can have textual conflicting
	 */
	protected boolean textualConflicting = false;
	
	
	/** Used to store level here were find the conflict*/
	private int depthLevel = 0;

	
	/** Say if the asset was selected by the user */
	protected boolean selected = false;
	
	
	/********** keep information about what assets this asset is conflicting in the own change log model ************/
	
	private List<AssetChangeLog> directConflicts = new ArrayList<AssetChangeLog>();

	private List<AssetChangeLog> indirectConflicts = new ArrayList<AssetChangeLog>();

	private List<AssetChangeLog> textualConflicts = new ArrayList<AssetChangeLog>();

	/**************************************************************************************************************/
	

	/**
	 * Associated Varitaion Point with this changeLog - setted only in conflicts
	 */
	protected Variability variationPoint;

	
	public abstract String getName();

	public abstract String getFullName();

	
	/** 
	 * <p>Implement this method to get the change log of the asset because it vary depending on the type of asset.</p>
	 */
	public abstract ChangeLog getChangelog();
	
	
	/** Allow order and use assets as collection keys */
	@Override
	public int compareTo(AssetChangeLog other) {
		return this.getSignature().compareTo(other.getSignature());
	}

	public boolean isDirectlyConflicting() {
		return directlyConflicting;
	}

	public void setDirectlyConflicting(boolean directlyConflicting) {
		this.directlyConflicting = directlyConflicting;
	}

	public boolean isIndirectlyConflicting() {
		return indirectlyConflicting;
	}

	public void setIndirectlyConflicting(boolean indirectlyConflicting) {
		this.indirectlyConflicting = indirectlyConflicting;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/** Everybody have to define what is your only signature */
	public abstract String getSignature();

	/**
	 * This method can not be Override, replace all white spaces from the
	 * signature
	 */
	public final void setSignature(String signature) {
		if (StringUtils.isNotEmpty(signature))
			this.signature = signature.replaceAll(" ", "").trim(); // replace all white spaces
		else
			this.signature = signature;
	}

	/**
	 * This method have to be overridden in each asset type to set the conflict
	 * to the asset parents
	 */
	public abstract void setHierarchicalDirectConflict(boolean conflicting);

	/**
	 * This method have to be overridden in each asset type to set the conflict
	 * to the asset parents
	 */
	public abstract void setHierarchicalIndirectConflict(boolean conflicting, int depthLevel);

	public Variability getVariationPoint() {
		return variationPoint;
	}

	public void setVariationPoint(Variability variationPoint) {
		this.variationPoint = variationPoint;
	}

	public boolean isTextualConflicting() {
		return textualConflicting;
	}

	public void setTextualConflicting(boolean textualConflicting) {
		this.textualConflicting = textualConflicting;
	}

	public int getDepthLevel() {
		return depthLevel;
	}

	public void setDepthLevel(int depthLevel) {
		this.depthLevel = depthLevel;
	}

	public List<AssetChangeLog> getDirectConflicts() {
		return directConflicts;
	}

	public void setDirectConflicts(List<AssetChangeLog> directConflicts) {
		this.directConflicts = directConflicts;
	}

	public List<AssetChangeLog> getIndirectConflicts() {
		return indirectConflicts;
	}

	public void setIndirectConflicts(List<AssetChangeLog> indirectConflicts) {
		this.indirectConflicts = indirectConflicts;
	}

	public List<AssetChangeLog> getTextualConflicts() {
		return textualConflicts;
	}

	public void setTextualConflicts(List<AssetChangeLog> textualConflicts) {
		this.textualConflicts = textualConflicts;
	}

	public void addIndirectConflict(AssetChangeLog assetConflicting) {

		if (indirectConflicts == null)
			indirectConflicts = new ArrayList<AssetChangeLog>();

		if (!indirectConflicts.contains(assetConflicting))
			indirectConflicts.add(assetConflicting);

	}

	public void addDirectConflict(AssetChangeLog assetConflicting) {

		if (directConflicts == null)
			directConflicts = new ArrayList<AssetChangeLog>();

		if (!directConflicts.contains(assetConflicting)) {
			directConflicts.add(assetConflicting);
			assetConflicting.setDirectlyConflicting(true);
		}

	}

	public void addTextualConflict(AssetChangeLog assetConflicting) {

		if (textualConflicts == null)
			textualConflicts = new ArrayList<AssetChangeLog>();

		if (!textualConflicts.contains(assetConflicting))
			textualConflicts.add(assetConflicting);

	}	

}