/**
 * 
 */
package br.ufrn.spl.ev.util;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import br.ufrn.spl.ev.engines.miners.iproject.IprojectLogUtils;
import br.ufrn.spl.ev.engines.miners.iproject.model.IprojectBuildInformation;
import br.ufrn.spl.ev.engines.miners.iproject.model.IprojectTaskInformation;

/**
 * iProjectTaskInformation Util
 * @author Gleydson
 *
 */
public class TaskInformationUtil {

	private static IprojectBuildInformation buildInformations;
	
	private static Map<String,Integer> sumMap = new HashMap<String,Integer>();
	
	public static void printTaskSummary() {
		
		for ( IprojectTaskInformation task : buildInformations.getTasks() ) {
			
			String groupType = getTypeGroup(task.getTaskType());
			
			Integer count = sumMap.get( groupType);
			if ( count == null ) {
				sumMap.put(groupType, 1);
			} else {
				sumMap.put(groupType, count+1);
			}
		}

		for ( String type : sumMap.keySet() ) {
			System.out.println(type + ":" + sumMap.get(type));
		}
		
		System.out.println("Total: " +  buildInformations.getTasks().size());
	}
	
	public static void printRevisions() {
		
		for ( IprojectTaskInformation task : buildInformations.getTasks() ) {
			
			for ( String log : task.getLogs() ) {
				System.out.println(IprojectLogUtils.extractClassesFromLog(log, "SIPAC"));
			}
		}
	}
	
	public static void readFromFile(String file) {
		
		try {
			JAXBContext context = JAXBContext.newInstance(IprojectBuildInformation.class);

			Unmarshaller um = context.createUnmarshaller();
			buildInformations = (IprojectBuildInformation) um.unmarshal(new FileReader(file));

			System.out.println("XML Build file loaded");
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static String getTypeGroup(String taskType) {
		
		if(taskType.contains("ERRO") || taskType.contains("SUSTENT") )
			return "BUG_FIX";
		else if(taskType.contains("APRIMORAMENTO") || taskType.contains("ALTERAÇÃO") || taskType.contains("CUSTOMIZACAO") || taskType.contains("PREPARACAO") || taskType.contains("MERGE") || taskType.contains("AMBIENTE") || taskType.contains("VALID") )
			return "UPGRADING";
		else if(taskType.contains("CASO DE USO") )
			return "NEW_USE_CASE";
		else
			return "OTHER";
		
	}
	
	public static void main(String[] args) {
		
		readFromFile("C:/Users/Gleydson/Dropbox/Doutoradov2/estudo3/ex1-sipac-ufla/buildInformation_SIPAC_4.4.419_4.4.19_s38.ufla_v2.xml");
		printTaskSummary();
		
		//printRevisions();
		
	}
	
}
