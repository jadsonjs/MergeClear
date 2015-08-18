/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.models.changeloghistorymodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains what is common for all asset of code (Classes, Methods, Fields, etc.)
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 18/12/2013
 *
 */
//@XmlSeeAlso({MethodChangeLog.class, FieldChangeLog.class,ClassChangeLog.class})
public abstract class CodeAssetChangeLog extends AssetChangeLog {

	/** Indicates where the changes happen*/
	protected List<ChangeLocation> changesLocation = new ArrayList<ChangeLocation>();
	
	/** Added a change that happen in this class */
	public void addChangeLocation(ChangeLocation changeLocation) {
		if(! changesLocation.contains(changeLocation))
		changesLocation.add(changeLocation);
	}
	
	/** Print the changes that this class have suffered */
	public String printChangesLocation() {
		if( changesLocation != null && changesLocation.size() > 0 ){
			StringBuilder b = new StringBuilder();
			b.append("[");
			boolean fisrt = true;
			for (ChangeLocation c : changesLocation) {
				if(fisrt)
					b.append(c);
				else
					b.append(", "+c);
				fisrt = false;
			}
			b.append("]");
			return b.toString();
		}else 
			return "";
	}

	/** Print the changes that this class have suffered */
	public List<ChangeLocation> getChangesLocation() {
		return changesLocation;
	}
	
//	/**
//	 * Test if changelog will be considered in conflict analysis
//	 * @param level
//	 * @return
//	 */
//	public boolean isChangeConsidered(ConflictAnalysisLevel level) {
//		
//		if ( level.equals(ConflictAnalysisLevel.ALL))
//			return true;
//		else if ( getChangesLocation().contains(ChangeLocation.BODY) )
//			return true;
//		else
//			return false;
//		
//	}
	
}
