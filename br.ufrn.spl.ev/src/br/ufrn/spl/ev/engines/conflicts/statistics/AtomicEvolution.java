package br.ufrn.spl.ev.engines.conflicts.statistics;

import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;

/**
 * Class representing a conflict statistic information.
 * 
 * @author Gleydson Lima
 * 
 */
public class AtomicEvolution {

	private String taskNumber;

	private String version;

	private String taskType;

	private String description;

	private int directConflict = 0;
	
	private int indirectConflict = 0;
	
	private int depthLevel = 0;
	
	private int textualConflict = 0;

	private String deltaType;

	private String signature;

	private String variability;

	private String layer;
	
	
	public AtomicEvolution(ClassChangeLog classChangeLog ) {
		
		setChangeLogInformation( classChangeLog.getChangelog() );
		setAssetInformation ( classChangeLog );
		setDeltaType( "CLASS_" + classChangeLog.getChangeType() );
		
	}
	
	public AtomicEvolution(FieldChangeLog fieldChangeLog ) {
		
		setChangeLogInformation( fieldChangeLog.getClassChangeLog().getChangelog() );
		setAssetInformation ( fieldChangeLog );
		setDeltaType( "FIELD_" + fieldChangeLog.getChangeType() );
		setDepthLevel(fieldChangeLog.getDepthLevel());
		
	}
	
	public AtomicEvolution(MethodChangeLog methodChangeLog ) {
		
		setChangeLogInformation( methodChangeLog.getClassChangeLog().getChangelog() );
		setAssetInformation ( methodChangeLog );
		setDeltaType( "METHOD_" + methodChangeLog.getChangeType() );
		setDepthLevel(methodChangeLog.getDepthLevel());
		
	}
	
	public void setChangeLogInformation(ChangeLog changeLog) {
		
		setTaskNumber( changeLog.getIdentify());
		setVersion( changeLog.getVersion() ); 
		setTaskType( changeLog.getType().toString() );
		String originalDescription = changeLog.getDescription();
		if ( originalDescription != null ) 
			setDescription( originalDescription.replace(";", " ") );
		
	}
	
	public void setAssetInformation(AssetChangeLog assetChangeLog) {
		
		setDirectConflict( assetChangeLog.isDirectlyConflicting() ? 1 : 0 );
		setIndirectConflict( assetChangeLog.isIndirectlyConflicting() ? 1 : 0 );
		setTextualConflict(  assetChangeLog.isTextualConflicting() ? 1 : 0 );
		setSignature( assetChangeLog.getSignature() );
		setLayer( getLayerStatInformation(assetChangeLog) );
		if ( assetChangeLog.getVariationPoint() != null )
			setVariability( assetChangeLog.getVariationPoint().getId() );
		
	}
	
	

	public static String getColumns() {
		return "taskNumber;version;taskType;description;directConflict;indirectConflict;depthLevel;textualConflict;deltaType;signature;variability;layer\n";
	}

	public String getCSV() {
		return taskNumber + ";" + version + ";" + taskType + ";" + description + ";" + directConflict + ";"  + indirectConflict + ";" + depthLevel + ";" +  textualConflict + ";" +
				deltaType + ";" + signature + ";" + variability + ";" + layer + "\n";
	}

	
	/**
	 * Extract software layer from package name pattern
	 * 
	 * @param ccl
	 * @return
	 */
	public String getLayerStatInformation(AssetChangeLog asset) {

		if (asset.getSignature().contains("/negocio/"))
			return "BUSINESS";
		else if (asset.getSignature().contains("/jsf/") || asset.getSignature().contains("/web/"))
			return "WEB";
		else if (asset.getSignature().contains("/dao/"))
			return "DATA_ACCESS";
		else if (asset.getSignature().contains("/dominio/"))
			return "ENTITY DOMAIN";
		else
			return "LAYER_OTHERS";

	}
	
	public String getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public int getDirectConflict() {
		return directConflict;
	}

	public void setDirectConflict(int directConflict) {
		this.directConflict = directConflict;
	}

	public int getIndirectConflict() {
		return indirectConflict;
	}

	public void setIndirectConflict(int indirectConflict) {
		this.indirectConflict = indirectConflict;
	}

	public int getTextualConflict() {
		return textualConflict;
	}

	public void setTextualConflict(int textualConflict) {
		this.textualConflict = textualConflict;
	}

	public String getDeltaType() {
		return deltaType;
	}

	public void setDeltaType(String deltaType) {
		this.deltaType = deltaType;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getVariability() {
		return variability;
	}

	public void setVariability(String variability) {
		this.variability = variability;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public int getDepthLevel() {
		return depthLevel;
	}

	public void setDepthLevel(int depthLevel) {
		this.depthLevel = depthLevel;
	}

}
