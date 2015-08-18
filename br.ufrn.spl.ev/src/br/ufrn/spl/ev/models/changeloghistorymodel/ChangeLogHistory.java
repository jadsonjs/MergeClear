/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE – UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA – DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.models.changeloghistorymodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the history of changes in a Software Product Line.
 * 
 * @author Jadson
 * 
 * @since 04/11/2012
 * @version
 * 
 */
public class ChangeLogHistory {

	/** All change log history have at least the core feature */
	public static final FeatureChangeLog CORE = new FeatureChangeLog("CORE", null, FeatureType.MANDATORY,
			"THE CORE FEATURE");

	/** System of the changelog */
	private String system;

	/**
	 * The number of the version then we want to start to analyze. Because it's
	 * impossible, and sometimes unnecessary, analyze all versions, since the
	 * beginning
	 */
	private String startVersion;
	/** The version number we are making the merge */
	private String baseVersion;

	/**
	 * All change if your approach are grouped by features, because we area
	 * working a a feature oriented approach
	 */
	private List<FeatureChangeLog> features;

	public FeatureChangeLog addFeature(FeatureChangeLog feature) {
		getFeatures().add(feature);
		return feature;
	}

	public FeatureChangeLog getFeature(String featureName) {
		if (features == null || features.indexOf(new FeatureChangeLog(featureName) ) == -1)
			return null;
		return features.get(features.indexOf(new FeatureChangeLog(featureName)));
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder("HistoryChangeLog [startVersion=" + startVersion + ", versionBase="
				+ baseVersion + "]");

		if (features != null)
			for (FeatureChangeLog feature : features) {
				buffer.append(feature.toString());
			}
		return buffer.toString();
	}

	/** Return always a new copy of the CORE feature */
	public static FeatureChangeLog createNewCoreFeature() {
		return new FeatureChangeLog(ChangeLogHistory.CORE.getName(), ChangeLogHistory.CORE.getParent(),
				ChangeLogHistory.CORE.getType(), ChangeLogHistory.CORE.getDescription());
	}

	/** Clear the Transients values that can not be save to the XML File*/
	public void clearTransientsValues() {
		
		for (FeatureChangeLog feature : getFeatures()) {
			
			feature.setSelected(false);
			
			for (ChangeLog changeLog : feature.getChangelogs() ) {
				
				changeLog.setSelected(false);
				
				for (ClassChangeLog classChangeLog : changeLog.getClasses()) {
					
					classChangeLog.setSelected(false);
					
					for (FieldChangeLog fields : classChangeLog.getFields()) {
						fields.setSelected(false);
					}
					
					for (MethodChangeLog methods : classChangeLog.getMethods()) {
						
						methods.setSelected(false);
						
						for (CodePieceChangeLog codePieces : methods.getCodepieces()) {
							codePieces.setSelected(false);
						}
					}
				}
			}
		}
		
	}
	
	
	// ///////// sets e gets ///////////////

	public String getStartVersion() {
		return startVersion;
	}

	public void setStartVersion(String startVersion) {
		this.startVersion = startVersion;
	}

	public String getBaseVersion() {
		return baseVersion;
	}

	public void setBaseVersion(String baseVersion) {
		this.baseVersion = baseVersion;
	}

	public List<FeatureChangeLog> getFeatures() {
		if (features == null)
			features = new ArrayList<FeatureChangeLog>();
		return features;
	}

	public void setFeatures(List<FeatureChangeLog> features) {
		this.features = features;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	

}
