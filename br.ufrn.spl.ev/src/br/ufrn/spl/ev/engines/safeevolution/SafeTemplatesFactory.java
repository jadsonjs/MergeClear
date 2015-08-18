/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.safeevolution;

import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.engines.safeevolution.templates.AddNewMandatoryFeature;
import br.ufrn.spl.ev.engines.safeevolution.templates.AddNewOptionalFeature;
import br.ufrn.spl.ev.engines.safeevolution.templates.AddNewOrFeature;
import br.ufrn.spl.ev.engines.safeevolution.templates.RefineAsset;
import br.ufrn.spl.ev.engines.safeevolution.templates.RefineAssetVariation;
import br.ufrn.spl.ev.engines.safeevolution.templates.ReplaceFeatureExpression;
import br.ufrn.spl.ev.engines.safeevolution.templates.SplitAsset;
import br.ufrn.spl.ev.engines.safeevolution.templates.librarymodule.AddNewMandatoryFeatureLibraryModule;
import br.ufrn.spl.ev.engines.safeevolution.templates.librarymodule.AddNewOptionalFeatureLibraryModule;
import br.ufrn.spl.ev.engines.safeevolution.templates.librarymodule.AddNewOrFeatureLibraryModule;
import br.ufrn.spl.ev.engines.safeevolution.templates.librarymodule.RefineAssetLibraryModule;
import br.ufrn.spl.ev.engines.safeevolution.templates.librarymodule.RefineAssetVariationLibraryModule;
import br.ufrn.spl.ev.engines.safeevolution.templates.librarymodule.ReplaceFeatureExpressionLibraryModule;
import br.ufrn.spl.ev.engines.safeevolution.templates.librarymodule.SplitAssetLibraryModule;


/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class SafeTemplatesFactory {
	
	/**
	 * A return a parser of a specific safe evolution template.
	 * 
	 * @return
	 */
	public TemplateAnalyzerChain getSafeTemplateAnalysis(RepositoryConnector repositoryConnector, String workDirectory) {
		
		
		SplitAsset template = getSplitAssetImpl(repositoryConnector, workDirectory);
		template.setNext(   getRefineAssetImpl(repositoryConnector, workDirectory)                 );
		template.setNext(   getRefineAssetVariationImpl(repositoryConnector, workDirectory)       );
		template.setNext(   getAddNewMandatoryFeatureImpl(repositoryConnector, workDirectory)     );
		template.setNext(   getAddNewOptionalFeatureImpl(repositoryConnector, workDirectory)      );
		template.setNext(   getAddNewOrFeatureImpl(repositoryConnector, workDirectory)            );
		template.setNext(   getReplaceFeatureExpressionImpl(repositoryConnector, workDirectory)   );
		return template;
				
	}
	
	//////////// specifics methods for the ufrn LPS  /////////////

	private SplitAsset getSplitAssetImpl(RepositoryConnector repositoryConnector, String workDirectory) {
		return new SplitAssetLibraryModule(repositoryConnector, workDirectory);
	}
	
	private RefineAsset getRefineAssetImpl(RepositoryConnector repositoryConnector, String workDirectory) {
		return new RefineAssetLibraryModule(repositoryConnector, workDirectory);
	}
	
	private RefineAssetVariation getRefineAssetVariationImpl(RepositoryConnector repositoryConnector, String workDirectory) {
		return new RefineAssetVariationLibraryModule(repositoryConnector, workDirectory);
	}
	
	private AddNewMandatoryFeature getAddNewMandatoryFeatureImpl(RepositoryConnector repositoryConnector, String workDirectory) {
		return new AddNewMandatoryFeatureLibraryModule(repositoryConnector, workDirectory);
	}
	
	private AddNewOptionalFeature getAddNewOptionalFeatureImpl(RepositoryConnector repositoryConnector, String workDirectory) {
		return new AddNewOptionalFeatureLibraryModule(repositoryConnector, workDirectory);
	}
	
	private AddNewOrFeature getAddNewOrFeatureImpl(RepositoryConnector repositoryConnector, String workDirectory) {
		return new AddNewOrFeatureLibraryModule(repositoryConnector, workDirectory);
	}
	
	private ReplaceFeatureExpression getReplaceFeatureExpressionImpl(RepositoryConnector repositoryConnector, String workDirectory) {
		return new ReplaceFeatureExpressionLibraryModule(repositoryConnector, workDirectory);
	}

}
