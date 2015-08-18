/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.direct;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogType;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeTypeRepository;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.CodePieceChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;

/**
 * <p>Test the algorithm of direct conflicts detection </p>
 * 
 * <p> Direct Conflicts are when  the same asset appears on both side source and target during the mine of evolution.</p>
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class DirectConflictsDetectionInChangeLogHistoryStrategyTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

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
	 * Test method for {@link br.ufrn.spl.ev.engines.evolution.conflicts.CheckSyntacticsSPLConflicts#checkDirectConflicts(br.ufrn.splmerge.models.changeloghistorymodel.ChangeLogHistory, br.ufrn.splmerge.models.changeloghistorymodel.ChangeLogHistory)}.
	 */
	@Test
	public void testNoEvolutions() {
		Assert.assertTrue( new DirectConflictsDetectionInChangeLogHistoryStrategy().checkDirectConflicts(null, null, null).isEmpty() ) ;
	}
	
	/**
	 * Test method for {@link br.ufrn.spl.ev.engines.evolution.conflicts.CheckSyntacticsSPLConflicts#checkDirectConflicts(br.ufrn.splmerge.models.changeloghistorymodel.ChangeLogHistory, br.ufrn.splmerge.models.changeloghistorymodel.ChangeLogHistory)}.
	 */
	@Test
	public void testNoConflicts() {
		
		//////// Evolution on source side //////////
		ChangeLogHistory sourceChanges = new ChangeLogHistory();
		
		sourceChanges.addFeature(ChangeLogHistory.createNewCoreFeature())
			.addChangeLog(new ChangeLog("123456", ChangeLogType.NEW_USE_CASE, "1.2.3", "new CRUD", "This change create a new use case on the system"))
				.addClassChangeLog( new ClassChangeLog("Test.java", "Source/br/ufrn/Test.java", "br/ufrn/Test.java", ChangeTypeRepository.UPDATED, "1.2.3") )
					.addMethodChangeLog(new MethodChangeLog("execute", "void#execute(String comand)", ChangeTypeRepository.UPDATED));
		
		
		////////  Evolution on target side //////////
		
		ChangeLogHistory targetChanges = new ChangeLogHistory();
		
		targetChanges.addFeature(ChangeLogHistory.createNewCoreFeature())
		.addChangeLog(new ChangeLog("123456", ChangeLogType.NEW_USE_CASE, "1.2.3", "other CRUD", "This change create a new CRUD on the system"))
			.addClassChangeLog( new ClassChangeLog("Test2.java", "Target/br/ufrn/Test2.java","br/ufrn/Test2.java", ChangeTypeRepository.UPDATED, "1.2.3") )
				.addMethodChangeLog(new MethodChangeLog("execute2", "void#execute2(String comand)", ChangeTypeRepository.UPDATED));
		
		
		/* *************************************************************************************************************
		 *  check if the ProductLineConflictModel is empty and none of the asset were marked as conflict
		 * **************************************************************************************************************/
		
		new DirectConflictsDetectionInChangeLogHistoryStrategy().checkDirectConflicts(sourceChanges, targetChanges, new ConflictModel());
		
		FeatureChangeLog feature =  sourceChanges.getFeature(ChangeLogHistory.CORE.getName());
		
		org.junit.Assert.assertFalse( feature.isDirectlyConflicting() );
		
		for (ChangeLog changeLog : feature.getChangelogs()) {
			
			Assert.assertFalse( changeLog.isDirectlyConflicting() );
			
			for (ClassChangeLog classes : changeLog.getClasses()) {
				
				for (FieldChangeLog field : classes.getFields()) {
					Assert.assertFalse( field.isDirectlyConflicting() );
				}
				
				for (MethodChangeLog method : classes.getMethods()) {
					
					Assert.assertFalse( method.isDirectlyConflicting() );
					
					for (CodePieceChangeLog codePiece : method.getCodepieces()) {
						Assert.assertFalse( codePiece.isDirectlyConflicting() );
					}
				}
			}
		}
		
	}
	
	/**
	 * Test method for {@link br.ufrn.spl.ev.engines.evolution.conflicts.CheckSyntacticsSPLConflicts#checkDirectConflicts(br.ufrn.splmerge.models.changeloghistorymodel.ChangeLogHistory, br.ufrn.splmerge.models.changeloghistorymodel.ChangeLogHistory)}.
	 */
	@Ignore
	@Test
	public void testFieldConflict() {
		
		////////Evolution on source side //////////
				
		ChangeLogHistory sourceChanges = new ChangeLogHistory();
		
		sourceChanges.addFeature(ChangeLogHistory.createNewCoreFeature())
			.addChangeLog(new ChangeLog("123456", ChangeLogType.NEW_USE_CASE, "1.2.3", "new CRUD", "This change create a new use case on the system"))
				.addClassChangeLog( new ClassChangeLog("Test.java", "Source/br/ufrn/Test.java", "br/ufrn/Test.java", ChangeTypeRepository.UPDATED, "1.2.3") )
					.addFieldChangeLog(new FieldChangeLog("testing", "Boolean#testing", ChangeTypeRepository.UPDATED));
		
		
		////////  Evolution on target side //////////
		
		ChangeLogHistory targetChanges = new ChangeLogHistory();
		
		targetChanges.addFeature(ChangeLogHistory.createNewCoreFeature())
			.addChangeLog(new ChangeLog("123456", ChangeLogType.NEW_USE_CASE, "1.2.3", "new CRUD", "This change create other use case on the system, but change same method"))
				.addClassChangeLog( new ClassChangeLog("Test.java", "Target/br/ufrn/Test.java", "br/ufrn/Test.java", ChangeTypeRepository.UPDATED, "1.2.3") )
					.addFieldChangeLog(new FieldChangeLog("testing", "Boolean#testing", ChangeTypeRepository.UPDATED));
		
		
		new DirectConflictsDetectionInChangeLogHistoryStrategy().checkDirectConflicts(sourceChanges, targetChanges, new ConflictModel() );
		
		/* **********************************************
		 *  Check if the asset were marked as conflicting
		 * **********************************************/
		FeatureChangeLog feature =  sourceChanges.getFeature(ChangeLogHistory.CORE.getName());
		
		Assert.assertTrue( feature.isDirectlyConflicting() );
		
		for (ChangeLog changeLog : feature.getChangelogs()) {
			
			Assert.assertTrue( changeLog.isDirectlyConflicting() );
			
			for (ClassChangeLog classes : changeLog.getClasses()) {
				
				Assert.assertTrue( classes.isDirectlyConflicting() );
				
				for (FieldChangeLog field : classes.getFields()) {
					
					Assert.assertTrue( field.isDirectlyConflicting() );
				}
			}
		}
		
	}
	
	
	
	/**
	 * Test method for {@link br.ufrn.spl.ev.engines.evolution.conflicts.CheckSyntacticsSPLConflicts#checkDirectConflicts(br.ufrn.splmerge.models.changeloghistorymodel.ChangeLogHistory, br.ufrn.splmerge.models.changeloghistorymodel.ChangeLogHistory)}.
	 */
	@Ignore
	@Test
	public void testMethodConflict() {

		////////Evolution on source side //////////
		
		ChangeLogHistory sourceChanges = new ChangeLogHistory();
		
		sourceChanges.addFeature(ChangeLogHistory.createNewCoreFeature())
			.addChangeLog(new ChangeLog("123456", ChangeLogType.NEW_USE_CASE, "1.2.3", "new CRUD", "This change create a new use case on the system"))
				.addClassChangeLog( new ClassChangeLog("Test.java", "Source/br/ufrn/Test.java", "br/ufrn/Test.java", ChangeTypeRepository.UPDATED, "1.2.3") )
					.addMethodChangeLog(new MethodChangeLog("execute", "void#execute(String comand)", ChangeTypeRepository.UPDATED));
		
		
		////////  Evolution on target side //////////
		
		ChangeLogHistory targetChanges = new ChangeLogHistory();
		
		targetChanges.addFeature(ChangeLogHistory.createNewCoreFeature())
			.addChangeLog(new ChangeLog("123456", ChangeLogType.NEW_USE_CASE, "1.2.3", "new CRUD", "This change create other use case on the system, but change same method"))
				.addClassChangeLog( new ClassChangeLog("Test.java", "Target/br/ufrn/Test.java", "br/ufrn/Test.java", ChangeTypeRepository.UPDATED, "1.2.3") )
					.addMethodChangeLog(new MethodChangeLog("execute", "void#execute(String comand)", ChangeTypeRepository.UPDATED));
		
		
		
		new DirectConflictsDetectionInChangeLogHistoryStrategy().checkDirectConflicts(sourceChanges, targetChanges, new ConflictModel() );
		
		
		/* **********************************************
		 *  Check if the asset were marked as conflicting
		 * **********************************************/
		
		FeatureChangeLog feature =  sourceChanges.getFeature(ChangeLogHistory.CORE.getName());
		
		Assert.assertTrue( feature.isDirectlyConflicting() );
		
		for (ChangeLog changeLog : feature.getChangelogs()) {
			
			Assert.assertTrue( changeLog.isDirectlyConflicting() );
			
			for (ClassChangeLog classes : changeLog.getClasses()) {
				
				Assert.assertTrue( classes.isDirectlyConflicting() );
				
				for (MethodChangeLog method : classes.getMethods()) {
					
					Assert.assertTrue( method.isDirectlyConflicting() );
				}
			}
		}
		
	}
	
	
	
}
