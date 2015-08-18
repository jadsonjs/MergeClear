/**
 *
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 *
 */
package br.ufrn.spl.ev.gui.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import br.ufrn.spl.ev.PluginConstants;
import br.ufrn.spl.ev.engines.miners.Miner;
import br.ufrn.spl.ev.gui.swt.ShowEvolutionsUI;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.parsers.ChangeLogHistoryModelParserFactory;
import br.ufrn.spl.ev.util.ChangeLogHistoryUtil;
import br.ufrn.spl.ev.util.GUIUtils;

/**
 * <p>Executes the process of extract evolution</p>
 * 
 * @author jadson
 *
 */
public class ExtractEvolutionInformationManager {

	/** Main GUI of the application */
	private ShowEvolutionsUI evolutionUI;
	
	/** The source miner to be used to mine information */
	private Miner minerSource;
	
	/** The target miner to be used to mine information */
	private Miner minerTarger;
	
	public ExtractEvolutionInformationManager(ShowEvolutionsUI evolutionUI, Miner minerSource, Miner minerTarger){
		this.evolutionUI = evolutionUI;
		this.minerSource = minerSource;
		this.minerTarger = minerTarger;
	}
	
	
	/**
	 * Make the process of extract evolution information from the source and target system, call the respective miner
	 * 
	 * @throws Exception
	 */
	public final void executeEvolutionAnalisis() throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		try{
		
			System.out.println("////////////////////////////////////////////////////////////////////");
			System.out.println("/////  Reading Information about Evolution started at "+new SimpleDateFormat("HH:mm").format(new Date())+"  //////");
			System.out.println("////////////////////////////////////////////////////////////////////");
			
			// Note: Is is static the the source code, but the user have to choose it from a GUI
			
			Properties sourceCK = new Properties();
			Properties targetCK = new Properties(); 
			
	    	try {
	            //load a properties file
	    		sourceCK.load(new FileInputStream(new File(evolutionUI.getWorkDirectoryPath()+PluginConstants.SOURCE_CK_FILE)));
	    	} catch (IOException ex) {
	    		System.err.println("Source Configuration Knowledge file does not exist. All assert will belong to the CORE FEATURE ");
	    		sourceCK = new Properties();
	        }
			
	    	try {
	            //load a properties file
	    		targetCK.load(new FileInputStream(new File(evolutionUI.getWorkDirectoryPath()+PluginConstants.TARGET_CK_FILE)));
	    	} catch (IOException ex) {
	    		System.err.println("Target Configuration Knowledge file does not exist. All assert will belong to the CORE FEATURE ");
	    		targetCK = new Properties();
	        }
	    	
	    	ChangeLogHistory changeLogHistorySource = executeEvolutionSourceSide(startTime, sourceCK);
			
			ChangeLogHistory changeLogHistoryTarget = executeEvolutionTargetSide(startTime, targetCK);
			
			
			if( changeLogHistorySource == null && changeLogHistoryTarget == null ){
				GUIUtils.addErrorMessage("Without evolution information ");
				return;
			}
			
			ChangeLogHistoryUtil.formatChangeLogHistoryToExposure(changeLogHistorySource);
			ChangeLogHistoryUtil.formatChangeLogHistoryToExposure(changeLogHistoryTarget);
			
			// SAVE AND SHOW THE RESULT TO THE USER //
		
			saveAndShowEvolutionResults(changeLogHistorySource, changeLogHistoryTarget);
			
		
		}finally{
			System.out.println("/////////// END EVOLUTION MINER PROCESS IN "+((System.currentTimeMillis()-startTime)/1000/60)+" minutes  ////////////");
		}
		
	}

	/** 
	 * <p> Execute the evolution in the source side using the miner configured to the source side </p> 
	 * 
	 * <p> Created at:  14/10/2013  </p>
	 */
	protected ChangeLogHistory executeEvolutionSourceSide(long startTime, Properties sourceCK) throws Exception {
		
		if( minerSource == null  ) return null;
		
		System.out.println("/////  STARTING SOURCE ANALISIS  //////");
		
		ChangeLogHistory changeLogHistorySource = minerSource.performMining(sourceCK);
	
		System.out.println("///// SOURCE ANALISIS IN "+((System.currentTimeMillis()-startTime)/1000/60)+" minutes //////");
		return changeLogHistorySource;
	}

	/** 
	 * <p> Execute the evolution in the target side using the miner configured to the target side </p> 
	 * 
	 * <p> Created at:  14/10/2013  </p>
	 * 
	 */
	protected ChangeLogHistory executeEvolutionTargetSide(long startTime, Properties targetCK) throws Exception {
		
		if( minerTarger == null  ) return null;
		
		System.out.println("/////  STARTIN TARGET ANALISIS  //////");
		
		ChangeLogHistory changeLogHistoryTarget = minerTarger.performMining(targetCK);
			
		System.out.println("/////  TARGET ANALISIS IN "+((System.currentTimeMillis()-startTime)/1000/60)+" minutes //////");
		return changeLogHistoryTarget;
	}
	
	
	


	/**
	 * <p> Execute the information for the user and show it for the user in the GUI </p>
	 * 
	 * <p> Created at:  14/10/2013  </p>
	 *
	 * @param startTime
	 * @param changeLogHistorySource
	 * @param changeLogHistoryTarget
	 * @return
	 */
	protected void saveAndShowEvolutionResults(ChangeLogHistory changeLogHistorySource, ChangeLogHistory changeLogHistoryTarget) {
		
		System.out.println("Saving evolution in:  "+evolutionUI.getWorkDirectoryPath());
		if( changeLogHistorySource != null )
			ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().writeHistoryChangeLogFile(changeLogHistorySource, new File(evolutionUI.getWorkDirectoryPath()+PluginConstants.HISTORY_CHANGE_LOG_SOURCE_FILE));
		if( changeLogHistoryTarget != null )
			ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().writeHistoryChangeLogFile(changeLogHistoryTarget, new File(evolutionUI.getWorkDirectoryPath()+PluginConstants.HISTORY_CHANGE_LOG_TARGET_FILE));
		
		
		evolutionUI.showEvolutionsData(changeLogHistorySource, changeLogHistoryTarget, null, null);
	}
	
}

