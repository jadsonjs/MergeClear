/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.safeevolution;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;

/**
 * 
 * <p>Represent represents a parser of safe evolution templates.</p>
 * 
 * <p>
 *   Who implements this interface have to implement the algorithm of identify 
 *   some of the safe evolution templates.
 * </p>
 * 
 * @author jadson - jadson@info.ufrn.br
 * @since 02/02/2014
 */
public abstract class TemplateAnalyzerChain {
	
	/** The Template analyzed */
	protected Template template;
	
	/** Allows do a chain of analyzers */
	protected TemplateAnalyzerChain next;
	
	/** A connector to the repository to recovery the source code of the classes */
	protected RepositoryConnector repositoryConnector;
	
	/** The work directory of the tool, Where it will safe the template information*/
	protected String workDirectory;
	
	
	public TemplateAnalyzerChain(Template template, RepositoryConnector repositoryConnector, String workDirectory){
		this.template = template;
		setRepositoryConnector(repositoryConnector);
		setWorkDirectory(workDirectory);
	}
	
	/**
	 * 
	 * @param template
	 */
	public final void setNext(TemplateAnalyzerChain template) {
		if(next == null)
			next = template;
		else
			next.setNext(template);
    }
	
	public void setRepositoryConnector(RepositoryConnector repositoryConnector) {
		this.repositoryConnector = repositoryConnector;
	}
	
	public void setWorkDirectory(String workDirectory) {
		this.workDirectory = workDirectory;
	}
	
	// Do the analysis
	public final void doChainAbalysis(ChangeLogHistory changeLogHistory){
		System.out.println("Executing "+template+" ..... ");
		long time = System.currentTimeMillis();
		doAnalysis(changeLogHistory);
		System.out.println("End Execution "+template+" "+((System.currentTimeMillis()-time)/1000)+" sec");
		if(next != null)
			next.doChainAbalysis(changeLogHistory);
		else
			System.out.println(" **************** End Chain Analysis **************** ");
	}

	protected abstract void doAnalysis(ChangeLogHistory changeLogHistory);
	
	
	
	/**
	 * This method create the map of information to be collect by or study.
	 * 
	 * @param informationCollected
	 * @param featureName
	 * @param changeLogDescription
	 * @param classPath
	 */
	protected void addInformationCollected(Map<String, Map<String, Set<String>>> informationCollected, String featureName, String changeLogDescription, String classPath) {
		
		if(informationCollected.containsKey(featureName)){
			
			Map<String, Set<String>> changeLogMap = informationCollected.get(featureName);
			
			if(changeLogMap.containsKey(changeLogDescription)){
				
				changeLogMap.get(changeLogDescription).add(classPath);
			}else{
				changeLogMap.put(changeLogDescription, new HashSet<String>());
				changeLogMap.get(changeLogDescription).add(classPath);
			}
			
		}else{
			informationCollected.put(featureName, new HashMap<String, Set<String>>());
			informationCollected.get(featureName).put(changeLogDescription, new HashSet<String>());
			informationCollected.get(featureName).get(changeLogDescription).add(classPath);
		}
	}
	
	/**
	 * Save templates analysis on a text file
	 * 
	 * Map<String, Map<String, Set<String> >> it is a little bit complex but is a map where the first key 
	 * is the feature, the second is the changeLog with a set of classes:
	 * 
	 * Map<Feature, Map<ChangeLog, Set<String> classPath >>
	 *  
	 * @see br.ufrn.spl.ev.engines.conflicts.statistics.ChangeLogHistoryStatistics#saveStatisticsCollected(java.lang.String)
	 */
	protected void saveTemplateStatistics(Template template, Map<String, Map<String, Set<String> >> informationCollected) {
		
        
		FileWriter writer = null;
		
		try {
			
			writer = new FileWriter(new File(workDirectory+"/analysis_"+template+".txt"));
			writer.write("\t\t\t\t\t "+template+" Analysis "+"\n");
			
			int indexTotal = 0;
			for (String feature : informationCollected.keySet() ) {
				writer.write("\n\nFeature: "+feature+"\n");
				for ( String changeLog : informationCollected.get(feature).keySet() ) {
					writer.write("ChangeLog: "+changeLog+"\n");
					int index = 1;
					for ( String _class : informationCollected.get(feature).get(changeLog) ) {
						writer.write("\t"+index+" - "+_class+"\n");
						index++;
						indexTotal++;
					}
				}	
			}
			writer.write("\n\n\t\t ============ Total of Assets using the pattern \""+template+ "\" :  "+indexTotal+" ===============\n\n");
	        
		 } catch (IOException ex) {
			 ex.printStackTrace();
		 }finally{
			 try {
				 writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
	}
	
}
