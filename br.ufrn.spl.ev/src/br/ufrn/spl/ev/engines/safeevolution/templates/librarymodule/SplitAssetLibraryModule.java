/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.safeevolution.templates.librarymodule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.engines.safeevolution.Template;
import br.ufrn.spl.ev.engines.safeevolution.templates.SplitAsset;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;

/**
 * <p>The automatically identify SplitAsset template of SPL evolution</p>
 * 
 * <p>This type of evolution usually first involved tracking the code related to the feature 
 *    and extracting it to other artifacts, like aspects, property files  or Eclipse RCP plug-in extensions.</p>
 * 
 * <p> To generalize the cases where a part of an asset that is related to a specific feature 
 * is extracted to another asset, we derived the SPLIT ASSET </p>
 * 
 * <p>For instance, the precondition specifies that we can only split an asset a into two other assets a' and a'' when the set of assets a'a''
 *  refines (preserves the behavior of) asset a.</p>
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class SplitAssetLibraryModule extends SplitAsset{

	public SplitAssetLibraryModule(RepositoryConnector repositoryConnector, String workDirectory){
		super(repositoryConnector, workDirectory);
	}
	
	/**
	 * @see br.ufrn.spl.ev.engines.safeevolution.TemplateAnalyzerChain#doAnalysis()
	 */
	@Override
	protected void doAnalysis(ChangeLogHistory changeLogHistory) {
		try {
			Set<String> revisions = new HashSet<String>();
			
			boolean useConditionalExecution = false;
			
			for (FeatureChangeLog feature : changeLogHistory.getFeatures()) {
				for (ChangeLog changeLog : feature.getChangelogs()) {
					for (ClassChangeLog _class : changeLog.getClasses()) {
						if(_class.getName().equals("ParametrosBiblioteca.java")){
							System.out.println("Sefe evolution using "+Template.SplitAsset+" template "
						      +"\n "+changeLog.getFullName()+" - "+changeLog.getDescription()
						      +"\n on feature "+feature.getFullName());
							useConditionalExecution = true;
							revisions.add(_class.getRevision());
						}
					}
				}
			}
			
			if(useConditionalExecution){
			
				for (String revision : revisions) {
					
					List<String> filesPath = repositoryConnector.getFilesOfRevision(revision);
					
					for (String path : filesPath) {
						if(path.endsWith(".java")){ 
							System.out.println(filesPath);
							
							//CompilationUnit c = repositoryConnector.getClassContentInTheRevision(path, new Long(revision));
							//System.out.println(c);
						}
					}
				
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}

}
