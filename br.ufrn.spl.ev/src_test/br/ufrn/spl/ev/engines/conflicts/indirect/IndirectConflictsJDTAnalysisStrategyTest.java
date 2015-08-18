package br.ufrn.spl.ev.engines.conflicts.indirect;

import org.junit.Assert;
import org.junit.Test;

public class IndirectConflictsJDTAnalysisStrategyTest {

	/**
	 * This signature should not recovery the JDF assets
	 */
	@Test
	public void testIsIgnoreAsset() {
		Assert.assertTrue(new IndirectConflictsJDTAnalysisStrategy(null)
			.isIgnoreAsset("/Users/jadson/experimento_mestrado/diretorio_execucao/ignoredwords.txt", 
					"pesquisa/br/ufrn/sigaa/pesquisa/dominio/GrupoPesquisa.java$int#getId()")); 
	}
	
	
	
	/**
	 * This signature should be recovery the JDF assets
	 */
	@Test
	public void testNotIsIgnoreAsset() {
		Assert.assertFalse(new IndirectConflictsJDTAnalysisStrategy(null)
			.isIgnoreAsset("/Users/jadson/experimento_mestrado/diretorio_execucao/ignoredwords.txt", 
					"pesquisa/br/ufrn/sigaa/pesquisa/dominio/GrupoPesquisa.java$boolean#existeMatriculaComponente(MatriculaComponente)")); 
	}

}
