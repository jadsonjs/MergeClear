/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE –- UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA -– DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.util;

import org.junit.After;
import org.junit.Before;

/**
 * @author jadson
 *
 */
public class ProductLineConflictModelUtilTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

//	/**
//	 * Test method for {@link br.ufrn.spl.ev.util.squiddelta.utils.ProductLineConflictModelUtil#readProductLineConflictModelToFile(java.io.File)}.
//	 */
//	@Test
//	public void testReadProductLineConflictModelFile() {
//		
//		try {
//			Properties properties = new Properties();
//			properties.load(PluginConstants.class.getResourceAsStream("config.properties"));
//			
//			File file = new File(properties.getProperty(PluginConstants.DEFAULT_WORK_DIRECTORY)+PluginConstants.PRODUCTI_LINE_CONFLICT_MODEL_FILE);
//			
//			ProductLineConflictModel plcm = ProductLineConflictModelUtil.readProductLineConflictModelToFile(file);
//		
//			if(file.exists())
//				Assert.assertTrue(plcm != null);
//			else
//				Assert.assertEquals(0, plcm.getListOfConflicts().size());
//			
//			
//			ProductLineConflictModelUtil.printProductLineConflictModel(plcm);
//	
//		} catch (IOException e) {
//			Assert.fail(e.getMessage());
//		}
//	}

}
