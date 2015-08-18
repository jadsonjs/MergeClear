/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev.engines.safeevolution.templates.librarymodule;

import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.engines.safeevolution.templates.AddNewMandatoryFeature;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class AddNewMandatoryFeatureLibraryModule extends AddNewMandatoryFeature{

	public AddNewMandatoryFeatureLibraryModule(RepositoryConnector repositoryConnector, String workDirectory){
		super(repositoryConnector, workDirectory);
	}
	
	/**
	 * @see br.ufrn.spl.ev.engines.safeevolution.TemplateAnalyzerChain#doAnalysis()
	 */
	@Override
	protected void doAnalysis(ChangeLogHistory changeLogHistory) {
		// TODO Auto-generated method stub
	}

}
