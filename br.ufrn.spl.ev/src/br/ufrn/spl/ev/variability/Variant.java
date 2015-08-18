package br.ufrn.spl.ev.variability;

/**
 * <p><b>A variant is a representation of a variability object within domain artefacts.</b></p> 
 * <br/><br/>
 *<p>
 * <i>
 * Linhas de Produtos de Software: Uma tend�ncia da ind�stria. Fascisco Airton Pereira da Silva et al.<br/><br/>
 * 
 * "Um variante corresponde a uma op��o do conjunto de poss�veis inst�ncias de varia��o que um ponto de variabilidade poder� originar."
 * </i>
 * </p>
 * <br/><br/>
 * <p>
 * <i>
 * Minicurso do Simp�sio Brasileiro de Engenharia de Software - SBES 2009 - Introdu��o a Linhas de Produtos de Software - �S�rgio Soares<br/><br/>
 * 
 * "Variante: representa��o de um objeto da variabilidade em artefatos do dom�nio.<br/>
 * Uma op��o de um ponto de varia��o. ex.: persist�ncia com SGBD relacional."<br/>
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