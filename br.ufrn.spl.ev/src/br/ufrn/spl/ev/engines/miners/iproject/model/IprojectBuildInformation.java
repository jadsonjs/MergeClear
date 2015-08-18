/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.iproject.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Information about a build in the IProject exported using an WebService. 
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - criação da classe.
 * @since 30/07/2013
 *
 */
@XmlRootElement
public class IprojectBuildInformation {

	/** The system of the evolution */
	private String system;
	
	/** The number of the version then we want to start to analyze */
	private String startVersion;
	
	/** The version number we are making the merge */
	private String baseVersion;
	
	/** The logs of evolutions*/
	private List<IprojectTaskInformation> tasks = new ArrayList<IprojectTaskInformation>();

	public List<IprojectTaskInformation> getTasks() {
		return tasks;
	}

	public void setTasks(List<IprojectTaskInformation> tasks) {
		this.tasks = tasks;
	}

	public String getStartVersion() {
		return startVersion;
	}

	public void setStartVersion(String startVersion) {
		this.startVersion = startVersion;
	}

	public String getBaseVersion() {
		return baseVersion;
	}

	public void setBaseVersion(String baseVersion) {
		this.baseVersion = baseVersion;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}
	
	
}
