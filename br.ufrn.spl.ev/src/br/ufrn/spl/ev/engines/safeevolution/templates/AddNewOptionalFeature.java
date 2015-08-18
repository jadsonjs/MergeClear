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
 * <p>This class try to detect the <strong>RefineAsset template </strong> </p>
 * 
 * <p>It is useful when a developer needs to introduce an optional feature without changing existing assets. </p>
 * 
 * <p> 
 * The template states that it is possible to introduce a new optional feature O together with a new asset a, as 
 * long as this asset is only selected whenever one selects O. 
 * This restriction assures that the new asset appears only present in products that have feature O; therefore, 
 * as the rest of the CK and asset mapping do not change, products built without the new 
 * feature correspond exactly to the original product line products. 
 * </p>
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public abstract class  AddNewOptionalFeature extends TemplateAnalyzerChain{


	public AddNewOptionalFeature(RepositoryConnector repositoryConnector, String workDirectory) {
		super(Template.AddNewOptionFeaure, repositoryConnector, workDirectory);
	}
}
