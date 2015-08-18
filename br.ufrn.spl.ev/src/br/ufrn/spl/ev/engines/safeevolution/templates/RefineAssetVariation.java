/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.safeevolution.templates;

import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.engines.safeevolution.Template;
import br.ufrn.spl.ev.engines.safeevolution.TemplateAnalyzerChain;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public abstract class  RefineAssetVariation extends TemplateAnalyzerChain{


	public RefineAssetVariation(RepositoryConnector repositoryConnector, String workDirectory) {
		super(Template.RefineAssetVariation, repositoryConnector, workDirectory);
	}
}
