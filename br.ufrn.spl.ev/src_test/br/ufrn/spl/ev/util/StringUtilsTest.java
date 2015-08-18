package br.ufrn.spl.ev.util;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.ufrn.spl.ev.engines.miners.MinerFactory.MergeSide;



public class StringUtilsTest {

	@Test
	public void testCompareStringWithOutSpacesEquals() {		
		assertEquals(StringUtils.equalsWithOutSpaces(" b1 (int , int ) ", "b1(int,int)"), true);
	}
	
	@Test
	public void testCompareStringWithOutSpacesDifferentes() {		
		assertEquals(StringUtils.equalsWithOutSpaces(" b2 (int , int ) ", "b1(int,int)"), false);
	}
	
	@Test
	public void testCompareStringWithOutSpacesAndExtensionsEquals() {		
		assertEquals(StringUtils.equalsWithOutSpacesAndExtensions(" Classe . java ", "Classe"), true);
	}
	
	@Test
	public void testCompareStringWithOutSpacesAndExtensionsDifferentes() {		
		assertEquals(StringUtils.equalsWithOutSpacesAndExtensions("Classe.java", "Classe1.java"), false);
	}
	
	/**
	 * Teste the method that create  the class signature from the classe path
	 * 
	 * This method is very important, if the class signature was not generated correctly, the direct and indirect conflict
	 * analysis will not work.
	 */
	@Test
	public void createClasseSignatureTest() {		
		assertEquals( 
				StringUtils.createClasseSignature("trunk/SIGAA/graduacao/br/ufrn/sigaa/ensino/graduacao/negocio/ProcessadorAproveitamentoAutomatico.java", "SIGAA")
				, "graduacao/br/ufrn/sigaa/ensino/graduacao/negocio/ProcessadorAproveitamentoAutomatico.java");
		
		assertEquals( 
				StringUtils.createClasseSignature("/branches/ufs/branches/SIGAA/espaco_fisico/br/ufrn/sigaa/espacofisico/jsf/ReservaEspacoFisicoMBean.java","SIGAA")
				, "espaco_fisico/br/ufrn/sigaa/espacofisico/jsf/ReservaEspacoFisicoMBean.java");
		
		assertEquals( 
				StringUtils.createClasseSignature("trunk/SIPAC/dao/br/ufrn/sipac/arq/dao/restaurante/RestauranteDAO.java", "SIPAC")
				, "dao/br/ufrn/sipac/arq/dao/restaurante/RestauranteDAO.java");
		
		assertEquals( 
				StringUtils.createClasseSignature("/branches/ufs/branches/SIGAA/diplomas/br/ufrn/sigaa/diploma/negocio/ProcessadorRegistroDiplomaColetivo.java", "SIGAA")
				, "diplomas/br/ufrn/sigaa/diploma/negocio/ProcessadorRegistroDiplomaColetivo.java");
		
		
		assertEquals( 
				StringUtils.createClasseSignature("trunk/SIGAA/ensino/br/ufrn/sigaa/ensino/negocio/ProcessadorInscricaoSelecao.java", "SIGAA")
				, "ensino/br/ufrn/sigaa/ensino/negocio/ProcessadorInscricaoSelecao.java");
		
		assertEquals( 
				StringUtils.createClasseSignature("branches/sigref/branches/ufopa/SIGAA/arq_dao/br/ufopa/sigaa/arq/dao/graduacao/CursoUFOPADao.java", "SIGAA")
				, "arq_dao/br/ufopa/sigaa/arq/dao/graduacao/CursoUFOPADao.java");
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void createClasseSignatureWithErroTest() {		
		
		assertEquals( 
				StringUtils.createClasseSignature("trunk/SIGAA/ensino/br/ufrn/sigaa/ensino/negocio/ProcessadorInscricaoSelecao.java", "SIGAA_3.8.0")
				, "ensino/br/ufrn/sigaa/ensino/negocio/ProcessadorInscricaoSelecao.java");
		
	}
	
	
	@Test
	public void testIsEmpty() {		
		assertEquals(StringUtils.isEmpty("            "), true);
		
		assertEquals(StringUtils.isEmpty(""), true);
		
		assertEquals(StringUtils.isEmpty(null), true);
		
		assertEquals(StringUtils.isEmpty("       a     "), false);
	}
	
	
	@Test
	public void testExtractLastNameAfterSlash() {		
		
		assertEquals(StringUtils.extractLastNameAfterSlash(null), "");
		
		assertEquals(StringUtils.extractLastNameAfterSlash(""), "");
		
		assertEquals(StringUtils.extractLastNameAfterSlash("sjflsdlfldksflk"), "sjflsdlfldksflk");
		
		assertEquals(StringUtils.extractLastNameAfterSlash("Classe.java"), "Classe.java");
		
		assertEquals(StringUtils.extractLastNameAfterSlash("/branches/ufs/branches/SIGAA/espaco_fisico/br/ufrn/sigaa/espacofisico/jsf/BuscaEspacoFisicoMBean.java"), "BuscaEspacoFisicoMBean.java");
	
		assertEquals(StringUtils.extractLastNameAfterSlash("/branches/ufs/branches/SIGAA/app/sigaa.ear/sigaa.war/infra_fisica/reserva/mapa_horarios.jsp"), "mapa_horarios.jsp");
	}
	
	@Test
	public void testExtractAnalyzedProjectName() {		
		
		assertEquals(StringUtils.extractAnalyzedProjectName(null, MergeSide.SOURCE), "_SOURCE");
		
		assertEquals(StringUtils.extractAnalyzedProjectName("", MergeSide.SOURCE), "_SOURCE");
		
		assertEquals(StringUtils.extractAnalyzedProjectName("sjflsdlfldksflk", MergeSide.SOURCE), "sjflsdlfldksflk_SOURCE");
		
		assertEquals(StringUtils.extractAnalyzedProjectName("SIGAA", MergeSide.TARGET), "SIGAA_TARGET");
		
		assertEquals(StringUtils.extractAnalyzedProjectName("/branches/ufs/branches/SIGAA", MergeSide.SOURCE), "SIGAA_SOURCE");
	
		assertEquals(StringUtils.extractAnalyzedProjectName("/branches/ufs/branches/SIGAA", MergeSide.TARGET), "SIGAA_TARGET");
	}
	
	
	@Test
	public void testExtractResivionNumbrer() {		
		
		assertEquals(StringUtils.extractRevisionNumber(
				"https://version.info.ufrn.br/cooperacao/branches/ufs/branches/SIGAA/app/sigaa.ear/sigaa.war/extensao/DiscenteExtensao/inscricao_discente.jsp"+
						"https://version.info.ufrn.br/cooperacao/branches/ufs/branches/SIGAA/extensao/br/ufrn/sigaa/extensao/jsf/SelecaoDiscenteExtensaoMBean.java"+
						"Completed: At revision: 25094", "At revision:"), "25094");
		
		assertEquals(StringUtils.extractRevisionNumber(
				"Revisão: 27326 "+
				" Caminho: https://version.info.ufrn.br/cooperacao/branches/ufs/branches/SIGAA_Atualizacao/extensao/br/ufrn/sigaa/extensao/negocio/AtividadeExtensaoValidator.java", "Revisão:"), "27326");
		
		Assert.assertFalse(StringUtils.extractRevisionNumber(
				"Revisão: 27326 "+
				" Caminho: https://version.info.ufrn.br/cooperacao/branches/ufs/branches/SIGAA_Atualizacao/extensao/br/ufrn/sigaa/extensao/negocio/AtividadeExtensaoValidator.java", "Revisão:").equals("2732"));
		
		Assert.assertFalse( StringUtils.extractRevisionNumber(
				"https://version.info.ufrn.br/cooperacao/branches/ufs/branches/SIGAA/app/sigaa.ear/sigaa.war/extensao/DiscenteExtensao/inscricao_discente.jsp"+
						"https://version.info.ufrn.br/cooperacao/branches/ufs/branches/SIGAA/extensao/br/ufrn/sigaa/extensao/jsf/SelecaoDiscenteExtensaoMBean.java"+
						"Completed: At revision: 25094", "At revision:").equals("5094"));
		
	}

	
	@Test
	public void testExtractResivionsNumbers(){
		List<String> revisions = StringUtils.extractRevisionsNumbers("Revisões:\n\r25412, 25392 e 25353", "Revisões:");
		
		for (String string : revisions) {
			System.out.println(string);
		}
		
		Assert.assertTrue(revisions.size() == 3);
		Assert.assertTrue(revisions.get(0).equals("25412"));
		Assert.assertTrue(revisions.get(1).equals("25392"));
		Assert.assertTrue(revisions.get(2).equals("25353"));
	}
	
	
	@Test
	public void testExtractResivionsNumbersRegularExpression(){
		
		List<String> revisions = StringUtils.extrectRevisionsNumbersTinyForm("r45138 : modificar busca para não trazer registros inativos  r45022 r44875 23444");
		
		Assert.assertTrue(revisions.size() == 3);
		Assert.assertTrue(revisions.get(0).equals("45138"));
		Assert.assertTrue(revisions.get(1).equals("45022"));
		Assert.assertTrue(revisions.get(2).equals("44875"));
		
		List<String> revisions2 = StringUtils.extrectRevisionsNumbersTinyForm("45138 :  modificar busca para não trazer registros inativos  23444");
		
		Assert.assertTrue(revisions2.size() == 0);
	}
	
	@Test
	public void testMatchRegularExpression(){
		
		Assert.assertTrue(StringUtils.matchRevisionsNumbersTinyForm("r45138"));
		
		Assert.assertTrue(StringUtils.matchRevisionsNumbersTinyForm("r45138 : modificar busca para não trazer registros inativos  r45022 r44875 23444"));
		
		Assert.assertFalse(StringUtils.matchRevisionsNumbersTinyForm("45138 :  modificar busca para não trazer registros inativos  23444"));
		
		Assert.assertFalse(StringUtils.matchRevisionsNumbersTinyForm(""));
		
		Assert.assertFalse(StringUtils.matchRevisionsNumbersTinyForm(null));
	}
	
}
