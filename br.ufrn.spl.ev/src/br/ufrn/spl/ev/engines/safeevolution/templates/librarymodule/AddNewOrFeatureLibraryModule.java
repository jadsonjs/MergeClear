/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.safeevolution.templates.librarymodule;

import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.engines.safeevolution.templates.AddNewOrFeature;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class AddNewOrFeatureLibraryModule extends AddNewOrFeature{

	public AddNewOrFeatureLibraryModule(RepositoryConnector repositoryConnector,  String workDirectory){
		super(repositoryConnector,  workDirectory);
	}
	
	/**
	 * @see br.ufrn.spl.ev.engines.safeevolution.TemplateAnalyzerChain#doAnalysis()
	 */
	@Override
	protected void doAnalysis(ChangeLogHistory changeLogHistory) {
		// TODO Auto-generated method stub

	}

}
