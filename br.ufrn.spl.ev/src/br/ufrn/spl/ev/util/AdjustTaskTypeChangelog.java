/**
 * 
 */
package br.ufrn.spl.ev.util;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import br.ufrn.spl.ev.engines.miners.iproject.model.IprojectBuildInformation;
import br.ufrn.spl.ev.engines.miners.iproject.model.IprojectTaskInformation;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogType;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;
import br.ufrn.spl.ev.parsers.ChangeLogHistoryModelParserFactory;

/**
 * iProjectTaskInformation Util
 * @author Gleydson
 *
 */
public class AdjustTaskTypeChangelog {

	private static IprojectBuildInformation buildInformations;
	
	private static ChangeLogHistory changeLogHistory;
	
	private static Map<String,String> taskTypes = new HashMap<String,String>();
	
	public static void readFromFile(String fileBuild, String fileHistory) {
		
		try {
			
			JAXBContext contextBuild = JAXBContext.newInstance(IprojectBuildInformation.class);
			
			Unmarshaller umBuild = contextBuild.createUnmarshaller();
			buildInformations = (IprojectBuildInformation) umBuild.unmarshal(new FileReader(fileBuild));
			
			for ( IprojectTaskInformation task : buildInformations.getTasks() ) {
				taskTypes.put( task.getTaskNumber(), task.getTaskType() );
			}
		
			changeLogHistory = ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().readHistoryChangeLogFile(new File(fileHistory));
			
			System.out.println("XML Build file loaded");
			
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static ChangeLogType getCorrectType(String taskNumber) {
		
		String taskType = taskTypes.get(taskNumber);
		
		if(taskType.contains("ERRO") || taskType.contains("SUSTENT") )
			return ChangeLogType.BUG_FIX;
		else if(taskType.contains("APRIMORAMENTO") || taskType.contains("ALTERAÇÃO") || taskType.contains("CUSTOMIZACAO") || taskType.contains("PREPARACAO") || taskType.contains("MERGE") || taskType.contains("AMBIENTE") || taskType.contains("VALID") )
			return ChangeLogType.UPGRADING;
		else if(taskType.contains("CASO DE USO") )
			return ChangeLogType.NEW_USE_CASE;
		else
			return ChangeLogType.UPGRADING;
		
	}
	
	public static void main(String[] args) {
		
		String buildInformation = "C:/Users/Gleydson/Dropbox/Doutoradov2/estudo3/ex4-sigaa-ufpi/buildInformation_SIGAA_3.7.1_3.8.10.ufpi_v2.xml";
		String historyChangeLog = "C:/Users/Gleydson/Dropbox/Doutoradov2/estudo3/ex4-sigaa-ufpi/changeLogHistoryTarget.xml";
		
		readFromFile(buildInformation,historyChangeLog);
		
		for ( FeatureChangeLog fcl : changeLogHistory.getFeatures() ) {
			for ( ChangeLog cl : fcl.getChangelogs() ) {
				cl.setType( getCorrectType(cl.getIdentify() ) );
				
				for ( ClassChangeLog ccl : cl.getClasses()  ) {
					
					if ( ccl.isTextualConflicting() && !ccl.getChangelog().isDirectlyConflicting() && !ccl.isIndirectlyConflicting() )
						ccl.getChangelog().setTextualConflicting(true);
					else
						ccl.getChangelog().setTextualConflicting(false);
					
					for ( MethodChangeLog m : ccl.getMethods() ) {
						if ( m.isIndirectlyConflicting() ) 
							m.setHierarchicalIndirectConflict(true, 0);
					}
					
					for ( FieldChangeLog f : ccl.getFields() ) 
						if ( f.isIndirectlyConflicting() )
							f.setHierarchicalIndirectConflict(true, 0);
						 
				}
				System.out.println(cl.getType());
			}
		}
		
		ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().writeHistoryChangeLogFile(changeLogHistory, new File(historyChangeLog));
		
		
	}
	
}
