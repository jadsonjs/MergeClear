/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.safeevolution.templates.librarymodule;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.engines.safeevolution.templates.RefineAsset;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.util.GUIUtils;

/**
 * <p>This class try to detect the <strong>RefineAsset template </strong> </p>
 * 
 * <p> 
 * We can always modify an asset _a_, transforming it into asset a', as long as the new asset a' refines the original 
 * asset _a_, and the FM and CK are not changed. 
 * </p>
 * 
 * <p>
 * We assure product line refinement because the behavior of each product that contained asset _a_ is preserved 
 * by a corresponding refined product that contains asset a'.
 * </p>
 * 
 *  <p>
 *      Similarly to SPLIT ASSET, REFINE ASSET relies on asset transformation templates. For example, we could use REFINE ASSET
 *  combined with existing program refactoring. 
 *      So, as code asset transformations might change more than one asset, we could have variations of REFINE ASSET
 *  to deal with simultaneous changes to more than one asset. 
 *      For example, the variation of REFINE ASSET to simultaneously change assets a and b would have the precondition a b [= a' b'
 *  and an extra requirement that the names associated to a and b appear together in K.
 * </p>
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class RefineAssetLibraryModule extends RefineAsset {

	public RefineAssetLibraryModule(RepositoryConnector repositoryConnector, String workDirectory){
		super(repositoryConnector, workDirectory);
	}
	
	/**
	 * @see br.ufrn.spl.ev.engines.safeevolution.TemplateAnalyzerChain#doAnalysis()
	 */
	@Override
	public void doAnalysis(ChangeLogHistory changeLogHistory) {
		
		final String CK_CLASS = "ParametrosBiblioteca.java";
		
		Map<String, Map<String, Set<String> >>  informationCollected = new HashMap<String, Map<String, Set<String> >>();
		
		try {
			
			boolean useConditionalExecution = false;
			
			for (FeatureChangeLog feature : changeLogHistory.getFeatures()) {
				
				for (ChangeLog changeLog : feature.getChangelogs()) {
					
					forClass:
					for (ClassChangeLog _class : changeLog.getClasses()) {
						if(_class.getName().equals(CK_CLASS) && ( _class.isAdded() || _class.isUpdated() ) ){
							useConditionalExecution = true;
							break forClass;
						}
					}
					// if the CK e FM don't change, so all change asset are for the RefineAsset pattern
					if( ! useConditionalExecution){
						for (ClassChangeLog _class : changeLog.getClasses()) {
							addInformationCollected(informationCollected, feature.getName(), changeLog.getIdentify()+ " - " + changeLog.getDescription(), _class.getPath());
						}
					}
					
					useConditionalExecution = false;
				}
			
			}
			
			saveTemplateStatistics(template, informationCollected);
			
		} catch (Exception e) {
			e.printStackTrace();
			GUIUtils.addErrorMessage("Error processing Refine Asset");
		}

	}

}
