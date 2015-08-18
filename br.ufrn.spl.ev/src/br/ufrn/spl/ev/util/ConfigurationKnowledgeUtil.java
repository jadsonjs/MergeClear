/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureType;

/**
 * Class that have the information about the relationship between feature and assets of our SPL.
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class criation.
 * @since 06/09/2013
 *
 */
public class ConfigurationKnowledgeUtil {

	
	/**
	 *   <p>Get the features associated with the task number passed</p>
	 * 
	 *  <p>Almost all configuration management system have tasks and this tasks have a identify number, so it is possible 
	 * to create a mapping associating this two information to create our Configuration Knowledge </p>
	 * 
	 * <p> Created at:  06/09/2013  </p>
	 *
	 * @param configurationKnowledg
	 * @param taskNumber
	 * @return
	 */
	 public static List<FeatureChangeLog> getFeaturesOfATask(Properties configurationKnowledg, String taskNumber){
		 
		 List<FeatureChangeLog> features = new ArrayList<FeatureChangeLog>();
		 
		 String featuresOfTask = configurationKnowledg.getProperty(taskNumber);
		 
		 if( StringUtils.isNotEmpty(featuresOfTask)){
			 
			 String[] featuresAsArray = featuresOfTask.split(";");
		 
			 for (String featureInformation : featuresAsArray) {
				 // featureInformation = [name, parent, type, description]
				 String[] featuresPart = featuresOfTask.substring(1, featureInformation.length()-1).split(",");
				 features.add( new FeatureChangeLog(featuresPart[0], new FeatureChangeLog(featuresPart[1]), FeatureType.valueOf(featuresPart[2].trim()), featuresPart[3]));
			 }
		 }
		 
		 return features;
	 }
	
}
