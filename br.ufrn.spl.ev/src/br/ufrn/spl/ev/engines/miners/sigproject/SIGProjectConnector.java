/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.sigproject;

import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import br.ufrn.spl.ev.PluginConstants;
import br.ufrn.spl.ev.engines.connectors.Connector;
import br.ufrn.spl.ev.engines.connectors.SystemConnector;
import br.ufrn.spl.ev.engines.miners.iproject.model.IprojectBuildInformation;
import br.ufrn.spl.ev.util.ConfigUtils;

/**
 * Connector to the SIG Software PROJECT
 * 
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - criação da classe.
 * @since 29/07/2013
 * 
 */
public class SIGProjectConnector extends Connector implements SystemConnector{

	private IprojectBuildInformation iprojectBuildInformations;

	public SIGProjectConnector() {

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

	/** return the information about evolution */
	public IprojectBuildInformation getIprojectBuildInformation() {
		return iprojectBuildInformations;
	}

	@Override
	public void performSetupOnLine() {
		// not necessary
	}

	@Override
	public void performSetupOffLine() {
		// not necessary
		
	}

	@Override
	public void performRunOnLine() {
		// not necessary
		
	}

	@Override
	public void performRunOffLine() {
		if (offLineEvolutionFile == null)
			throw new IllegalArgumentException("File not found");

		try {
			JAXBContext context = JAXBContext.newInstance(IprojectBuildInformation.class);

			Unmarshaller um = context.createUnmarshaller();
			iprojectBuildInformations = (IprojectBuildInformation) um.unmarshal(new FileReader(ConfigUtils.getConfiguration(PluginConstants.DEFAULT_WORK_DIRECTORY)+offLineEvolutionFile));

			System.out.println("XML Build file loaded");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}