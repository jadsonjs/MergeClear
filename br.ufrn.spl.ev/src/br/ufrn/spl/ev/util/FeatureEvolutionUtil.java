/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The is a util class from FeatureEvolution
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class criation.
 * @since 10/09/2013
 *
 */
public class FeatureEvolutionUtil {

	/**
	 *  <p>Get the features that that has evolved between two specific version of the system. </p>
	 * 
	 *  <p>This information is give to us when we don't have how get it from the configurations management systems CMS. </p>
	 * 
	 * <p> Created at:  10/09/2013  </p>
	 *
	 * @param configurationKnowledg
	 * @param taskNumber
	 * @return
	 */
	 public static List<Integer> getEvolutionTasks(final String TEMP_FE_FILE){
		
		List<Integer> tasksEvolutionIds = new ArrayList<Integer>(); 
		 
		Properties featureEvolutionMapping = new Properties();
			
    	try {
               //load a properties file
    		featureEvolutionMapping.load(new FileInputStream(new File(TEMP_FE_FILE)));
    	
		 
			 String tasksEvolutionIdsStr = featureEvolutionMapping.getProperty("tasks");
			 
			 if( StringUtils.isNotEmpty(tasksEvolutionIdsStr)){
				 
				 String[] tasksIds = tasksEvolutionIdsStr.split(";");
			 
				 for (String taskIds : tasksIds) {
					 tasksEvolutionIds.add( Integer.valueOf(taskIds));
				 }
			 }
			 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }finally{
        	
        }
		 
    	return tasksEvolutionIds;
		 
	 }
	 
	 
	 /**
	 *  <p>Get the startVersion of the evolution. </p>
	 * <p> Created at:  10/09/2013  </p>
	 *
	 * @param configurationKnowledg
	 * @param taskNumber
	 * @return
	 */
	 public static String getStartVersion(final String TEMP_FE_FILE){
		
		 Properties featureEvolutionMapping = new Properties();
		 
		 try {
             //load a properties file
			 featureEvolutionMapping.load(new FileInputStream(new File(TEMP_FE_FILE)));
			 return  featureEvolutionMapping.getProperty("startVersion");
		 } catch (IOException ex) {
			 ex.printStackTrace();
			 return "";
		 }
	 }
	 
	 /**
	 *  <p>Get the startVersion of the evolution. </p>
	 * <p> Created at:  10/09/2013  </p>
	 *
	 * @param configurationKnowledg
	 * @param taskNumber
	 * @return
	 */
	 public static String getBaseVersion(final String TEMP_FE_FILE){
		
		 Properties featureEvolutionMapping = new Properties();
		 
		 try {
             //load a properties file
			 featureEvolutionMapping.load(new FileInputStream(new File(TEMP_FE_FILE)));
			 return  featureEvolutionMapping.getProperty("baseVersion");
		 } catch (IOException ex) {
			 ex.printStackTrace();
			 return "";
		 }
	 }
	
}
