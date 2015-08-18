/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.connectors;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import br.ufrn.spl.ev.PluginConstants;
import br.ufrn.spl.ev.engines.connectors.git.GitConnector;
import br.ufrn.spl.ev.engines.connectors.svn.SVNConnector;
import br.ufrn.spl.ev.engines.miners.MinerFactory.MergeSide;
import br.ufrn.spl.ev.engines.miners.github.GithubConnector;
import br.ufrn.spl.ev.engines.miners.iproject.IprojectConnector;
import br.ufrn.spl.ev.engines.miners.redmine.RedmineConnector;
import br.ufrn.spl.ev.engines.miners.sigproject.SIGProjectConnector;
import br.ufrn.spl.ev.util.PropertiesUtil;
import br.ufrn.spl.ev.util.PropertiesUtil.PluginProperties;
import br.ufrn.spl.ev.util.StringUtils;

/**
 * A factory design patters to create connectors classes. 
 * This connectors classes have information about how to connect to external informations.
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - criação da classe.
 * @since 31/07/2013
 *
 */
public class ConnectorFactory {

	
	public SystemConnector getSystemConnector(String connectorName, String workDirectoryPath, MergeSide mergeSide, boolean onLineModel) throws Exception{
			
		if(StringUtils.isEmpty(connectorName)) return null; // support half side miner
		
		Properties propConnector = PropertiesUtil.readPropertiesObject(PluginProperties.CONNECTOR);
		Properties propConnections = PropertiesUtil.readPropertiesObject(PluginProperties.CONNECTIONS);
	
		SystemConnector connector = (SystemConnector) Class.forName(propConnector.getProperty(connectorName)).newInstance();
		
		String side = ( mergeSide == MergeSide.SOURCE ? "SOURCE" : "TARGET");
		
		if(connector instanceof IprojectConnector){
			IprojectConnector iprojectConnector = (IprojectConnector) connector;
			iprojectConnector.setType(ConnectorType.SYSTEM);
			iprojectConnector.setUrl(propConnections.getProperty(side+"_SYSTEM_URL"));
			iprojectConnector.setPassword(propConnections.getProperty(side+"_SYSTEM_PASSWORD"));
			iprojectConnector.setSystemName(propConnections.getProperty(side+"_SYSTEM_NAME"));
			iprojectConnector.setModuleName(propConnections.getProperty(side+"_MODULE_NAME"));
			iprojectConnector.setStartVersion(propConnections.getProperty(side+"_SYSTEM_START_VERSION"));
			iprojectConnector.setEndVersion(propConnections.getProperty(side+"_SYSTEM_END_VERSION"));
			iprojectConnector.setOffLineEvolutionFile(propConnections.getProperty(side + "_SYSTEM_OFF_LINE_EVOLUTION_FILE"));
			
			iprojectConnector.performSetup(mergeSide, onLineModel); // just after all the data be initialized 
		
		}
		
		if(connector instanceof SIGProjectConnector){
			SIGProjectConnector sigProjectConnector = (SIGProjectConnector) connector;
			sigProjectConnector.setType(ConnectorType.SYSTEM);
			sigProjectConnector.setSystemName(propConnections.getProperty(side+"_SYSTEM_NAME"));
			sigProjectConnector.setStartVersion(propConnections.getProperty(side+"_SYSTEM_START_VERSION"));
			sigProjectConnector.setEndVersion(propConnections.getProperty(side+"_SYSTEM_END_VERSION"));
			sigProjectConnector.setOffLineEvolutionFile(propConnections.getProperty(side + "_SYSTEM_OFF_LINE_EVOLUTION_FILE"));
			
			sigProjectConnector.performSetup(mergeSide, onLineModel); // just after all the data be initialized 
		
		}
		
		if(connector instanceof RedmineConnector){
		
			RedmineConnector redmineConnector = (RedmineConnector) connector;
			redmineConnector.setType(ConnectorType.SYSTEM);
			redmineConnector.setUrl(propConnections.getProperty(side+"_SYSTEM_URL"));
			redmineConnector.setPassword(propConnections.getProperty(side+"_SYSTEM_PASSWORD"));
			redmineConnector.setSystemName(propConnections.getProperty(side+"_SYSTEM_NAME"));
			redmineConnector.setStartVersion(propConnections.getProperty(side+"_SYSTEM_START_VERSION"));
			redmineConnector.setEndVersion(propConnections.getProperty(side+"_SYSTEM_END_VERSION"));
			
			Properties featureEvolution = new Properties();
			
	    	try {
	            String fileFE = ( mergeSide == MergeSide.SOURCE ? workDirectoryPath+PluginConstants.SOURCE_FE_FILE : workDirectoryPath+PluginConstants.TARGET_FE_FILE);
	    		
	    		//load a properties file
	    		featureEvolution.load(new FileInputStream(new File(fileFE)));
	    		redmineConnector.setTasksEvolution(featureEvolution);
	    	} catch (IOException ex) {
	    		ex.printStackTrace();
	    		redmineConnector.setTasksEvolution(featureEvolution);
	        }
			
			redmineConnector.performSetup(mergeSide, onLineModel); // just after all the data be initialized 
		}
	
		if(connector instanceof GithubConnector){
			
			GithubConnector githubConnector = (GithubConnector) connector;
			githubConnector.setType(ConnectorType.SYSTEM);
			githubConnector.setUrl(propConnections.getProperty(side+"_SYSTEM_URL"));
			githubConnector.setUser(propConnections.getProperty(side+"_SYSTEM_USER"));
			githubConnector.setPassword(propConnections.getProperty(side+"_SYSTEM_PASSWORD"));
			githubConnector.setSystemName(propConnections.getProperty(side+"_SYSTEM_NAME"));
			githubConnector.setStartVersion(propConnections.getProperty(side+"_SYSTEM_START_VERSION"));
			githubConnector.setEndVersion(propConnections.getProperty(side+"_SYSTEM_END_VERSION"));
			
			Properties featureEvolution = new Properties();
			
	    	try {
	            String fileFE = ( mergeSide == MergeSide.SOURCE ? workDirectoryPath+PluginConstants.SOURCE_FE_FILE : workDirectoryPath+PluginConstants.TARGET_FE_FILE);
	    		
	    		//load a properties file
	    		featureEvolution.load(new FileInputStream(new File(fileFE)));
	    		githubConnector.setIssuesPRsEvolution(featureEvolution);
	    	} catch (IOException ex) {
	    		ex.printStackTrace();
	    		githubConnector.setIssuesPRsEvolution(featureEvolution);
	        }
			
	    	githubConnector.performSetup(mergeSide, onLineModel); // just after all the data be initialized 
		}
		
		//connector.setWorkDirectoryPath(workDirectoryPath);
		
		return connector;
		
	}

	
	
	public RepositoryConnector getRepositoryConnector(MergeSide mergeSide, boolean onLineModel) throws Exception{
		
		// Agora o connector do repositório pode variar entre SNV e GIT, então o nome é recuperado do arquivo de configuração
		// Não é mais fixo como era antes
		Properties propConfig = PropertiesUtil.readPropertiesObject(PluginProperties.CONFIG);
		
		String connectorName =  ( mergeSide == MergeSide.SOURCE ?  propConfig.getProperty("REPOSITORY_CONNECTOR_SOURCE") : propConfig.getProperty("REPOSITORY_CONNECTOR_TARGET"));
		
		if(StringUtils.isEmpty(connectorName)) return null; // support half side miner
		
		Properties propConnector = PropertiesUtil.readPropertiesObject(PluginProperties.CONNECTOR);
		Properties propConnections =  PropertiesUtil.readPropertiesObject(PluginProperties.CONNECTIONS);
	
		RepositoryConnector connector = (RepositoryConnector) Class.forName(propConnector.getProperty(connectorName)).newInstance();
		
		String side = ( mergeSide == MergeSide.SOURCE ? "SOURCE" : "TARGET");
		
		if(connector instanceof SVNConnector){
			
			SVNConnector svnConnector = (SVNConnector) connector;
			svnConnector.setType(ConnectorType.REPOSITORY);
			svnConnector.setUrl(propConnections.getProperty(side+"_REPOSITORY_URL"));
			svnConnector.setRepositoryPath(propConnections.getProperty(side+"_REPOSITORY_PATH"));
			svnConnector.setUser(propConnections.getProperty(side+"_REPOSITORY_USER"));
			svnConnector.setPassword(propConnections.getProperty(side+"_REPOSITORY_PASSWORD"));
			
			svnConnector.performSetup(mergeSide, onLineModel); // just after all the data be initialized 
			
			//svnConnector.setWorkDirectoryPath(workDirectoryPath);
			
		} 
		else if(connector instanceof GitConnector){
			GitConnector gitConnector = (GitConnector) connector;
			gitConnector.setType(ConnectorType.REPOSITORY);
			gitConnector.setUrl(propConnections.getProperty(side+"_REPOSITORY_URL"));
			gitConnector.setRepositoryPath(propConnections.getProperty(side+"_REPOSITORY_PATH"));
			gitConnector.setUser(propConnections.getProperty(side+"_REPOSITORY_USER"));
			gitConnector.setPassword(propConnections.getProperty(side+"_REPOSITORY_PASSWORD"));
			gitConnector.setStartVersion(propConnections.getProperty(side+"_SYSTEM_START_VERSION"));
			gitConnector.setEndVersion(propConnections.getProperty(side+"_SYSTEM_END_VERSION"));
			gitConnector.performSetup(mergeSide, onLineModel); // just after all the data be initialized 
			
			//gitConnector.setWorkDirectoryPath(workDirectoryPath);
		}
		
		return connector;
		
	}
	
}
