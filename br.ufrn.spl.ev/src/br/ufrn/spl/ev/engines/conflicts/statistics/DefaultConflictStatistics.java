/**
 * 
 */
package br.ufrn.spl.ev.engines.conflicts.statistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufrn.spl.ev.models.conflictmodel.AssetConflict;
import br.ufrn.spl.ev.models.conflictmodel.Conflict;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;
import br.ufrn.spl.ev.models.conflictmodel.ConflictSubType;
import br.ufrn.spl.ev.models.conflictmodel.ConflictType;

/**
 * @author jadson
 *
 */
public class DefaultConflictStatistics implements ConflictStatistics {

	/** Keep Information about conflict to be printed in the file */
	private StringBuilder buffer = new StringBuilder();
	
	/**
	 * @see br.ufrn.spl.ev.engines.conflicts.statistics.ConflictStatistics#collectStatistics(br.ufrn.spl.ev.models.conflictmodel.ConflictModel, br.ufrn.spl.ev.models.conflictmodel.ConflictModel, java.lang.String)
	 */
	@Override
	public void collectStatistics(ConflictModel conflictModel, String filePath) {
	
		List<Conflict> direct = new ArrayList<Conflict>();
		List<Conflict> indirect = new ArrayList<Conflict>();
		List<Conflict> pseudo = new ArrayList<Conflict>();
		
		for(Conflict conflict : conflictModel.getListOfConflicts()){
			if(conflict.getConflictType() == ConflictType.DIRECT){
				direct.add(conflict);
			}
			if(conflict.getConflictType() == ConflictType.INDIRECT){
				indirect.add(conflict);
			}
			if(conflict.getConflictType() == ConflictType.PSEUDO){
				pseudo.add(conflict);
			}
		}
		
		analyseDirectConflict(direct);
		analyseIndirectConflict(indirect);
		analysePseudoConflict(pseudo);
		
		saveStatisticsCollected(filePath);
	}



	private void analyseDirectConflict(List<Conflict> direct) {
		buffer.append(" \n\n\t==== DIRECT ====\n\n");
		
		int totalOfConflicts = 0;
		for(Conflict conflict : direct){
			totalOfConflicts += conflict.getTargetConflictsRelated().size();
		}
		
		buffer.append(" Total of Direct conflict: "+totalOfConflicts);
	}

	
	private void analyseIndirectConflict(List<Conflict> indirect) {
		buffer.append(" \n\n\t==== INDIRECT ====\n\n");
		
		// used to group the conflicts by call level //
		Map<Integer, Integer> callLevelMap = new HashMap<Integer, Integer>();
		
		int totalOfConflicts = 0;
		for(Conflict conflict : indirect){
			
			if(conflict.getConflictSubType() == ConflictSubType.INDIRECT_BY_EVOLUTION){ // just by evolution we are investigating
					
					totalOfConflicts += conflict.getTargetConflictsRelated().size();
					
					for(AssetConflict asset : conflict.getTargetConflictsRelated()){
						
						int callLevel = asset.getCallLevel(); // the level where we find the conflict in the call graphic
						
						if(callLevelMap.containsKey(callLevel))
							callLevelMap.put(callLevel, callLevelMap.get(callLevel)+1); // plus 1
						else
							callLevelMap.put(callLevel, 1); // the first conflict in the call level
						
					}
			}
		}
		
		for (Integer callLevels : callLevelMap.keySet()){
			buffer.append(" Total of Indirect conflict in level ("+callLevels+"):  "+callLevelMap.get(callLevels)+" \n");
			buffer.append(" conflicts in the ("+callLevels+") level represents: "+  ( String.format("%.2f", (double) ( (double) callLevelMap.get(callLevels)/totalOfConflicts)*100 ) )+" % \n");
		}
		
		buffer.append(" \nTotal of Indirect conflict: "+totalOfConflicts+" \n");
		
	}
	
	private void analysePseudoConflict(List<Conflict> pseudo) {
		buffer.append(" \n\n\t==== PSEUDO ====\n\n");
		
		int totalOfConflicts = 0;
		for(Conflict conflict : pseudo){
			totalOfConflicts += conflict.getTargetConflictsRelated().size();
		}
		
		buffer.append(" Total of Pseudo conflict: "+totalOfConflicts);
		
	}




	/**
	 * 
	 * @see br.ufrn.spl.ev.engines.conflicts.statistics.ChangeLogHistoryStatistics#saveStatisticsCollected(java.lang.String)
	 */
	public void saveStatisticsCollected(String filePath) {
		
		try{
			File file = new File(filePath+"/conflict_statistic.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(buffer.toString());
			bw.close();

		 } catch (IOException ex) {
			 ex.printStackTrace();
			 System.out.println(ex.getMessage());
		 }
		 
	}
}
