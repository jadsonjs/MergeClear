/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.miners;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import br.ufrn.spl.ev.engines.connectors.ConnectorFactory;
import br.ufrn.spl.ev.engines.miners.github.GithubMiner;
import br.ufrn.spl.ev.engines.miners.iproject.IprojectMiner;
import br.ufrn.spl.ev.engines.miners.redmine.RedmineMiner;
import br.ufrn.spl.ev.engines.miners.sigproject.SIGProjectMiner;
import br.ufrn.spl.ev.util.StringUtils;

/**
 * Instantiates a concrete Miner from the support ones.
 * 
 * @author jadson - jadsonjs@gmail.com
 * @vesion 1.0 - criação da classe.
 * @since 29/07/2013
 *
 */
public class MinerFactory {

	public enum MergeSide{
		SOURCE, TARGET;
	}
	
	/**
	 * Create a concrete miner to extract the informations of evolution. Each supported system have to have your own miner, IPROJECT, REDMINE, SIGPRoject, etc...
	 * 
	 * @param systemName
	 * @return
	 */
	public Miner getMiner(String minerName, String workDirectoryPath, MergeSide mergeSide, boolean systemOnLineModel, boolean repositoryOnlineModel){
		
		if(StringUtils.isEmpty(minerName)) return null; // support half side miner
		
		Properties prop = new Properties();
		try {
			// I think that this also should be in the config file, this is hardcode
			URL url = new URL("platform:/plugin/br.ufrn.spl.ev/src/br/ufrn/spl/ev/miners.properties");
			InputStream inputStream = url.openConnection().getInputStream();
			
			prop.load(inputStream);
			Miner miner =  (Miner) Class.forName(prop.getProperty(minerName)).newInstance();
			
			
			
			// OBSERVATION.: RepositoryConnector is always "true" in the miner, we don't find out a way to do this offline
			
			if(miner instanceof IprojectMiner){
				miner.setSystemConnector( new ConnectorFactory().getSystemConnector("IPROJECT", workDirectoryPath, mergeSide, systemOnLineModel));
				miner.setRepositoryConnector(new ConnectorFactory().getRepositoryConnector(mergeSide, repositoryOnlineModel));
			}
			
			if(miner instanceof RedmineMiner){
				miner.setSystemConnector(new ConnectorFactory().getSystemConnector("REDMINE", workDirectoryPath, mergeSide, systemOnLineModel));
				miner.setRepositoryConnector(new ConnectorFactory().getRepositoryConnector(mergeSide, repositoryOnlineModel));
			}
			
			if(miner instanceof SIGProjectMiner){
				miner.setSystemConnector(new ConnectorFactory().getSystemConnector("SIGPROJECT", workDirectoryPath, mergeSide, systemOnLineModel));
				miner.setRepositoryConnector(new ConnectorFactory().getRepositoryConnector( mergeSide, repositoryOnlineModel));
				
			}
			
			if(miner instanceof GithubMiner){
				miner.setSystemConnector(new ConnectorFactory().getSystemConnector("GITHUB", workDirectoryPath, mergeSide, systemOnLineModel));
				miner.setRepositoryConnector(new ConnectorFactory().getRepositoryConnector( mergeSide, repositoryOnlineModel));
				
			}
			
			return miner;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
