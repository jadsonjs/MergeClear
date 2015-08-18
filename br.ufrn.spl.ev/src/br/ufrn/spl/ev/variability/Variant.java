package br.ufrn.spl.ev.variability;

/**
 * <p><b>A variant is a representation of a variability object within domain artefacts.</b></p> 
 * <br/><br/>
 *<p>
 * <i>
 * Linhas de Produtos de Software: Uma tendência da indústria. Fascisco Airton Pereira da Silva et al.<br/><br/>
 * 
 * "Um variante corresponde a uma opção do conjunto de possíveis instâncias de variação que um ponto de variabilidade poderá originar."
 * </i>
 * </p>
 * <br/><br/>
 * <p>
 * <i>
 * Minicurso do Simpósio Brasileiro de Engenharia de Software - SBES 2009 - Introdução a Linhas de Produtos de Software - ©Sérgio Soares<br/><br/>
 * 
 * "Variante: representação de um objeto da variabilidade em artefatos do domínio.<br/>
 * Uma opção de um ponto de variação. ex.: persistência com SGBD relacional."<br/>
 * </i>
 * </p>
 * 
 * @author Gleydson
 *
 */
public class Variant {

	/** The variant specific value */
	private String value;
	
	/** The variant description */
	private String description;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}