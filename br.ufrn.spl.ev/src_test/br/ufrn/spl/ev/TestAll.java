package br.ufrn.spl.ev;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ufrn.spl.ev.engines.conflicts.direct.DirectConflictsDetectionInChangeLogHistoryStrategyTest;
import br.ufrn.spl.ev.engines.conflicts.indirect.IndirectConflictsDetectionStrategyTest;
import br.ufrn.spl.ev.engines.conflicts.indirect.IndirectConflictsJDTAnalysisStrategyTest;
import br.ufrn.spl.ev.util.UtilsTests;

/**
 * <p>Execute all tests of the tool.</p>
 * 
 * <p>Add all new test case or test suit created to this class.</p>
 * 
 * <p>IMPORTANT: To commit something to the mater repository, all tests should be green. Please, do it.</p>
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	UtilsTests.class,
	DirectConflictsDetectionInChangeLogHistoryStrategyTest.class,
	IndirectConflictsDetectionStrategyTest.class,
	IndirectConflictsJDTAnalysisStrategyTest.class})
public class TestAll {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

}
