/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.iproject;

import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import br.ufrn.spl.ev.PluginConstants;
import br.ufrn.spl.ev.engines.connectors.Connector;
import br.ufrn.spl.ev.engines.connectors.SystemConnector;
import br.ufrn.spl.ev.engines.miners.iproject.model.IprojectBuildInformation;
import br.ufrn.spl.ev.util.ConfigUtils;
import br.ufrn.spl.ev.util.GUIUtils;
import br.ufrn.spl.ev.util.StringUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * Connector to the UFRN IPROJECT
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - criação da classe.
 * @since 29/07/2013
 *
 */
public class IprojectConnector extends Connector implements SystemConnector{
	
	/** A web Resource of REST/JSON, because the IPROJECT export the information in this format */
	private WebResource webResource;
	
	private IprojectBuildInformation iprojectBuildInformations;
	
	public IprojectConnector() {
		
	}
	
	

	public void performSetupOffLine(){
		// not necessary
	}
	

	/**
	 * perform the miner when you don't have access to the system, using local files
	 * <p> Created at:  14/11/2013  </p>
	 *
	 */
	public void performSetupOnLine() {
		
		if(StringUtils.isEmpty(systemName) ||  StringUtils.isEmpty(startVersion) || StringUtils.isEmpty(endVersion) )
			return;
		
		String urlWebServiceEvolution = StringUtils.isEmpty(moduleName) 
				? ( url+"?system="+this.systemName+"&startVersion="+this.startVersion+"&endVersion="+this.endVersion+"&accessKey="+this.password )
						: ( url+"?system="+this.systemName+"&module="+this.moduleName+"&startVersion="+this.startVersion+"&endVersion="+this.endVersion+"&accessKey="+this.password );
		
		try{
			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
			Client client = Client.create(clientConfig);
			webResource = client.resource(urlWebServiceEvolution);
		}catch(Exception ex){
			ex.printStackTrace();
		}catch(Throwable th){
			th.printStackTrace();
		}
	}
	

	
	
	/**
	 * perform the miner when you have access to the system
	 * <p> Created at:  14/11/2013  </p>
	 *
	 */
	public void performRunOnLine() {
		try {
			if(webResource != null)
				iprojectBuildInformations =  webResource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(IprojectBuildInformation.class);
		} catch (Exception e) {
			e.printStackTrace();
			GUIUtils.addErrorMessage("Perform OnLine Run Exception: "+e.getCause());
		} 
	}
	
	/**
	 * perform the miner when you have access to the system
	 * <p> Created at:  14/11/2013  </p>
	 *
	 */
	public void performRunOffLine() {
		try {
			JAXBContext context = JAXBContext.newInstance(IprojectBuildInformation.class);

			Unmarshaller um = context.createUnmarshaller();
			iprojectBuildInformations = (IprojectBuildInformation) um.unmarshal(new FileReader(ConfigUtils.getConfiguration(PluginConstants.DEFAULT_WORK_DIRECTORY)+offLineEvolutionFile));

			System.out.println("XML Build file loaded");

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error loading XML Build file from performRunOffLine ");
		}
	}
	

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}


	public void setStartVersion(String startVersion) {
		this.startVersion = startVersion;
	}


	public void setEndVersion(String endVersion) {
		this.endVersion = endVersion;
	}
	
	public String getPassword() {return password;}
	
	public void setPassword(String password) {this.password = password;}

	/** return the information about evolution*/
	public IprojectBuildInformation getIprojectBuildInformation() {
		return iprojectBuildInformations;
	}

}
