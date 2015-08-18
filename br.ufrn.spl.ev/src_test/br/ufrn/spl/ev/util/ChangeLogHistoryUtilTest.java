/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE – UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA – DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufrn.spl.ev.PluginConstants;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.parsers.ChangeLogHistoryModelParserFactory;


/**
 * @author HP
 * 
 * @since 07/11/2012
 * @version
 *
 */
public class ChangeLogHistoryUtilTest {

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

	/**
	 * Test method for {@link br.ufrn.squiddelta.utils.XMLFilesUtil#readHistoryChangeLogFile(java.io.File)}.
	 */
	@Test
	public void testReadHistoryChangeLogFile() {
		try {
			Properties properties = new Properties();
			properties.load(PluginConstants.class.getResourceAsStream("config.properties"));
			
			File file = new File(properties.getProperty(PluginConstants.DEFAULT_WORK_DIRECTORY)+PluginConstants.HISTORY_CHANGE_LOG_SOURCE_FILE);
			
			ChangeLogHistory history = ChangeLogHistoryModelParserFactory.getChangeLogHistoryModelParser().readHistoryChangeLogFile(file);
		
			if(file.exists())
				Assert.assertTrue(history != null);
			else
				Assert.assertEquals(0, history.getFeatures().size());
			
			ChangeLogHistoryUtil.printChangeLog(history, false, false);
	
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

}
