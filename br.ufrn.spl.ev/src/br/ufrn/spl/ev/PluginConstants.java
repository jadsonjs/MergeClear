/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�?TICA E MATEM�?TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev;

/**
 * This keep some constants used by plug-in.
 * 
 * @author Jadson - jadson@info.ufrn.br
 * 
 * @since 30/09/2012
 * @version 1.0
 *
 */
public interface PluginConstants {

	/** The name of the property of the Work Directory of the plug-in ( the directory where the plug-in will save the things) */
	public final static String DEFAULT_WORK_DIRECTORY = "DEFAULT_WORK_DIRECTORY";
	
	public static final String HISTORY_CHANGE_LOG_SOURCE_FILE = "changeLogHistorySource.xml"; 
	public static final String HISTORY_CHANGE_LOG_TARGET_FILE = "changeLogHistoryTarget.xml";
	public static final String CONFLICT_MODEL_FILE            = "conflictModel.xml";
	
	public static final String DEPENDENCE_ANALYSIS_SOURCE_FILE = "dependenceAnalysisSource.xml"; 
	public static final String DEPENDENCE_ANALYSIS_TARGET_FILE = "dependenceAnalysisTarget.xml";
	
	public static final String SOURCE_CK_FILE = "sourceCK.properties"; 
	public static final String TARGET_CK_FILE = "targetCK.properties";
	
	public static final String SOURCE_FE_FILE = "sourceFE.properties";
	public static final String TARGET_FE_FILE = "targetFE.properties";
	
	/** The configuration name of the indirect analysis strategy to be used by plug-in*/
	public final static String INDIRECT_ANALYSIS_TYPE = "INDIRECT_ANALYSIS_TYPE";
	
	/** The configuration name of the indirect analysis strategy to be used by plug-in*/
	public final static String DEPTH_ANALYSIS_LEVEL = "DEPTH_ANALYSIS_LEVEL";
	
	/** Constant that contain of the  source side miner */
	public final static String SOURCE_MINER = "SOURCE_MINER";
	
	/** Constant that contain of the target side miner */
	public final static String TARGET_MINER = "TARGET_MINER";
	
	/** Constant that contain the path of the source side project in the repository */
	public final static String SOURCE_REPOSITORY_PATH = "SOURCE_REPOSITORY_PATH";
	
	/** Constant that contain the path of the target side project in the repository */
	public final static String TARGET_REPOSITORY_PATH = "TARGET_REPOSITORY_PATH";
	
	/** The configuration constant that indicates the way that the information about evolution will be extract */
	public final static String EXTRACT_EVOLUTION_MODEL = "EXTRACT_EVOLUTION_MODEL";
	
	/** The configuration constant that indicates the way that the information about evolution will be extract */
	public final static String INDIRECT_CONFLICT_MODEL = "INDIRECT_CONFLICT_MODEL";
	
	/** Tree Visualization Mode */
	//public final static String CHANGELOG_TREE_VIEW = "CHANGELOG_TREE_VIEW";
	
	/** The type of statistic the will be executed */
	public final static String STATISTIC_ANALYSIS_TYPE = "STATISTIC_ANALYSIS_TYPE";
	
	/** The model of statistic the will be executed */
	public final static String STATISTIC_ANALYSIS_MODEL = "STATISTIC_ANALYSIS_MODEL";
	
	/** GIT SUPOORT */
	public final static String REPOSITORY_CONNECTOR_SOURCE = "REPOSITORY_CONNECTOR_SOURCE";
	public final static String REPOSITORY_CONNECTOR_TARGET = "REPOSITORY_CONNECTOR_TARGET";
	
	public final static String CONFLICT_ANALYSIS_LEVEL = "CONFLICT_ANALYSIS_LEVEL";
	
	/** Github support */
	public final static String SOURCE_SYSTEM_URL = "SOURCE_SYSTEM_URL";
	public final static String GITHUB_USER = "GITHUB_USER";
	public final static String GITHUB_REPOSITORY = "GITHUB_REPOSITORY";
	public final static String GITHUB_TOKEN = "GITHUB_TOKEN";

}
