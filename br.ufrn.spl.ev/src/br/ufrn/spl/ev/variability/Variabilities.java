package br.ufrn.spl.ev.variability;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a collection of variation points of the Software Produtct Line.
 * 
 * @author Gleydson Lima
 * 
 */
@XmlRootElement
public class Variabilities {

	/** The name of the system */
	private String system;

	private String description;

	private List<Variability> variability = new ArrayList<Variability>();

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Variability> getVariability() {
		return variability;
	}

	public void setVariability(List<Variability> variability) {
		this.variability = variability;
	}

}