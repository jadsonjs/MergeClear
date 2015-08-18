/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.indirect;

import org.junit.Test;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogType;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeTypeRepository;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;

/**
 * <p>Test the algorithm of indirect conflict detection</p> 
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class IndirectConflictsDetectionStrategyTest {

	
//	/**
//	 * The the method when we pass a analysis level now supported
//	 */
//	@Test
//	public void checkIndirectConflictsOverHeadAnalysisLevel() {
//		try {
//			new IndirectConflictsDetectionMockStrategy(null, new SVNConnector(), new SVNConnector())
//				.checkIndirectConflicts(new ChangeLogHistory(), new ChangeLogHistory(), new ConflictModel(), new Integer(11).shortValue() );
//		} catch (IndirectConflictsDetectionException icde) {
//			if(icde.getMessage().equals("The depth analysis Level need to be between 1 and 10") )
//				Assert.assertTrue(true);  // if throw this exception if correct
//			else
//				Assert.assertTrue(false);
//		}
//		
//	}
	
	

	
	
	/**
	 *  Verify if the algorithm of detect indirect conflicts of an asset that call other is correct
	 */
	@Test
	public void checkIndirectConflictsInTheCallGraphic() {
		
		ChangeLogHistory changeLogHistorySource  = createChangeLogsOfSource();
		ChangeLogHistory changeLogHistoryTarget  = createChangeLogsOfTarget();
		
		// In this mock example, we are simulating that were selected the  "B.java" and "D.java" classes
		for (FeatureChangeLog feature : changeLogHistorySource.getFeatures()) {
			
			for(ChangeLog changeLog : feature.getChangelogs()){
				
				for(ClassChangeLog _class : changeLog.getClasses()){
					
					if(_class.getName().equals("B.java")){
						_class.setSelected(true);
						changeLog.setSelected(true);
						feature.setSelected(true);
						
						for (FieldChangeLog field : _class.getFields()) {
							field.setSelected(true);
						}
						
						for (MethodChangeLog method : _class.getMethods()) {
							method.setSelected(true);
						}
						
						continue;
					}
					if(_class.getName().equals("D.java")){
						_class.setSelected(true);
						changeLog.setSelected(true);
						feature.setSelected(true);
						
						for (FieldChangeLog field : _class.getFields()) {
							field.setSelected(true);
						}
						
						for (MethodChangeLog method : _class.getMethods()) {
							method.setSelected(true);
						}
						
						continue;
					}
				}
			}
		}
		
//		try {
//			new IndirectConflictsDetectionMockStrategy(null, new SVNConnector(), new SVNConnector() )
//					.checkIndirectConflicts(changeLogHistorySource, changeLogHistoryTarget,  new ConflictModel(), new Integer(3).shortValue());
//		} catch (IndirectConflictsDetectionException icde) {
//			Assert.fail(icde.toString());
//		}
		
//		// In this mock example, the method b1 is indirect conflicting
//		for (FeatureChangeLog feature : changeLogHistorySource.getFeatures()) {
//			
//			Assert.assertTrue(feature.isIndirectlyConflicting());
//			
//			for(ChangeLog changeLog : feature.getChangelogs()){
//				
//				if( changeLog.getIdentify().equals("1234560") )
//					Assert.assertTrue(changeLog.isIndirectlyConflicting());
//				else
//					Assert.assertFalse(changeLog.isIndirectlyConflicting());
//				
//				for(ClassChangeLog _class : changeLog.getClasses()){
//					
//					if(_class.getName().equals("B.java")){
//						Assert.assertTrue(_class.isIndirectlyConflicting());
//					
//						for(MethodChangeLog method : _class.getMethods()){
//							if(method.getName().equals("b2"))
//								Assert.assertTrue(method.isIndirectlyConflicting());
//						}
//					}
//					
//					if(_class.getName().equals("D.java")){
//						Assert.assertFalse(_class.isIndirectlyConflicting());
//					
//						for(MethodChangeLog method : _class.getMethods()){
//							if(method.getName().equals("d1"))
//								Assert.assertFalse(method.isIndirectlyConflicting());
//						}
//					}
//					
//					// if were not B.java and D.java class, the other classes are to be not with indirect conflict  //
//					if( !_class.getName().equals("B.java") && ! _class.getName().equals("D.java") ){
//						Assert.assertFalse(_class.isIndirectlyConflicting());
//						
//						for(MethodChangeLog method : _class.getMethods()){
//								Assert.assertFalse(method.isIndirectlyConflicting());
//						}
//					}
//					
//				}
//			}
//		}
	}
	
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Create the set of simple evolution as the "ABC" exemple on Source system
	 * @param feature
	 */
	private ChangeLogHistory createChangeLogsOfSource( ) {

		// *** Indirect conflict  ***
		ClassChangeLog _classB = new ClassChangeLog("B.java", "Source/src/br/ufrn/example/B.java", "src/br/ufrn/example/B.java", ChangeTypeRepository.UPDATED, "123");
		_classB.addMethodChangeLog(new MethodChangeLog("b1", "int#b1(int,int)", ChangeTypeRepository.UPDATED));
		_classB.addMethodChangeLog(new MethodChangeLog("b2", "int#b2(int,int)", ChangeTypeRepository.UPDATED));
		_classB.addFieldChangeLog(new FieldChangeLog("b", "int:b", ChangeTypeRepository.UPDATED));
		
		
		ClassChangeLog _classC = new ClassChangeLog("C.java", "Source/src/br/ufrn/example/C.java", "src/br/ufrn/example/C.java", ChangeTypeRepository.UPDATED, "123");
		_classC.addFieldChangeLog(new FieldChangeLog("cF", "int#cF", ChangeTypeRepository.ADDED));

		ClassChangeLog _classD = new ClassChangeLog("D.java", "Source/src/br/ufrn/example/D.java", "src/br/ufrn/example/D.java", ChangeTypeRepository.ADDED, "123");
		_classD.addMethodChangeLog(new MethodChangeLog("d1", "double#d1(double,double)", ChangeTypeRepository.ADDED));
		
		// direct conflict
		ClassChangeLog _classF = new ClassChangeLog("F.java", "Source/src/br/ufrn/example/F.java", "src/br/ufrn/example/F.java", ChangeTypeRepository.UPDATED, "123");
		_classF.addMethodChangeLog(new MethodChangeLog("f1", "double#f1(double)", ChangeTypeRepository.UPDATED));
		_classF.addFieldChangeLog(new FieldChangeLog("f1", "int#f1", ChangeTypeRepository.DELETE));
		
		
		// direct conflict in variation point
		ClassChangeLog _classG = new ClassChangeLog("G1.java", "Source/src/br/ufrn/example/G1.java", "src/br/ufrn/example/G1.java", ChangeTypeRepository.UPDATED, "Changed G1 implementation");
		_classG.addMethodChangeLog(new MethodChangeLog("g2", "void#g2()", ChangeTypeRepository.UPDATED));
		
		
		// direct conflict in variation point
		ClassChangeLog _classH = new ClassChangeLog("H.java", "Source/src/br/ufrn/example/H.java", "src/br/ufrn/example/H.java", ChangeTypeRepository.UPDATED, "123");
		_classH.addMethodChangeLog(new MethodChangeLog("h1", "void#h1()", ChangeTypeRepository.UPDATED));
		
		
		
		ChangeLogHistory changeLogHistory = new ChangeLogHistory();
		
		changeLogHistory.setBaseVersion("1.0.0");
		changeLogHistory.setStartVersion("2.0.0");
		changeLogHistory.setSystem("mock");
		
		FeatureChangeLog featureCore = ChangeLogHistory.createNewCoreFeature();
		
		//////////////////////////////////
		
		ChangeLog changeLog1 = new ChangeLog();
		
		changeLog1.setIdentify(String.valueOf(1234560) );
		changeLog1.setDescription( "Creating mock evolutions " );
		changeLog1.setType(ChangeLogType.NEW_USE_CASE);
		
		changeLog1.setVersion("1.5.0");
		changeLog1.setChangeInformation("A Change Log created for tests");
		
		
		
		///////////////////////////////
		
		ChangeLog changeLog2 = new ChangeLog();
		
		changeLog2.setIdentify(String.valueOf(1234561) );
		changeLog2.setDescription( "Evolution in Pattern Variation Point" );
		changeLog2.setType(ChangeLogType.UPGRADING);
		
		changeLog2.setVersion("1.5.0");
		changeLog2.setChangeInformation("Evolution in variation point");

		
		/////////////////////////////		
		
		ChangeLog changeLog3 = new ChangeLog();
		
		changeLog3.setIdentify(String.valueOf(1234561) );
		changeLog3.setDescription( "Evolution Update" );
		changeLog3.setType(ChangeLogType.UPGRADING);
		
		changeLog3.setVersion("1.5.0");
		changeLog3.setChangeInformation("Evolution in conditional execution in target");
	
		/////////////////////////////////
		
		
		changeLog1.addClassChangeLog(_classB);
		changeLog1.addClassChangeLog(_classC);
		changeLog1.addClassChangeLog(_classD);
		changeLog1.addClassChangeLog(_classF);
		
		changeLog2.addClassChangeLog(_classG);
		
		changeLog3.addClassChangeLog(_classH);
		
		featureCore.addChangeLog(changeLog1);
		featureCore.addChangeLog(changeLog2);
		featureCore.addChangeLog(changeLog3);
		
		changeLogHistory.addFeature(featureCore);
		
		return changeLogHistory;
		
	}
	
	/**
	 * Create the set of simple evolution as the "ABC" exemple on Target system
	 * @param feature
	 */
	private ChangeLogHistory createChangeLogsOfTarget() {
		
		// direct conflict
		ClassChangeLog _classB = new ClassChangeLog("B.java", "Source/src/br/ufrn/example/B.java", "src/exemple/B.java", ChangeTypeRepository.UPDATED, "123");
		_classB.addMethodChangeLog(new MethodChangeLog("b1", "int#b1(int,int)", ChangeTypeRepository.UPDATED));
		
		// *** Indirect conflict  ***
		ClassChangeLog _classC = new ClassChangeLog("C.java", "Target/src/br/ufrn/example/C.java", "src/exemple/C.java", ChangeTypeRepository.UPDATED, "123");
		_classC.addMethodChangeLog(new MethodChangeLog("c1", "int#c1(double,float)", ChangeTypeRepository.DELETE));
		_classC.addMethodChangeLog(new MethodChangeLog("c2", "int#c2()", ChangeTypeRepository.UPDATED));

		ClassChangeLog _classE = new ClassChangeLog("E.java", "Target/src/br/ufrn/example/E.java", "src/exemple/E.java", ChangeTypeRepository.ADDED, "123");
		_classE.addMethodChangeLog(new MethodChangeLog("e1", "double#e1(double)", ChangeTypeRepository.ADDED));
		
		// direct conflict
		ClassChangeLog _classF = new ClassChangeLog("F.java", "Target/src/br/ufrn/example/F.java", "src/exemple/F.java", ChangeTypeRepository.UPDATED, "123");
		_classF.addMethodChangeLog(new MethodChangeLog("f1", "double#f1(double)", ChangeTypeRepository.UPDATED));
		
		// direct conflict in variation point
		ClassChangeLog _classG = new ClassChangeLog("G1.java", "Target/src/br/ufrn/example/G1.java", "src/br/ufrn/example/G1.java", ChangeTypeRepository.UPDATED, "123");
		_classG.addMethodChangeLog(new MethodChangeLog("g2", "void#g2()", ChangeTypeRepository.UPDATED));
		
		// direct conflict in variation point
		ClassChangeLog _classH = new ClassChangeLog("H.java", "Target/src/br/ufrn/example/H.java", "src/br/ufrn/example/H.java", ChangeTypeRepository.UPDATED, "123");
		_classH.addMethodChangeLog(new MethodChangeLog("h1", "void#h1()", ChangeTypeRepository.UPDATED));
		
		
		
		ChangeLogHistory changeLogHistory = new ChangeLogHistory();

		changeLogHistory.setBaseVersion("1.0.0");
		changeLogHistory.setStartVersion("2.0.0");
		changeLogHistory.setSystem("mock");
		
		FeatureChangeLog featureCore = ChangeLogHistory.createNewCoreFeature();
		
		//////////////////////////////
		
		ChangeLog changeLog1 = new ChangeLog();
		
		changeLog1.setIdentify(String.valueOf(1234560) );
		changeLog1.setDescription( "Creating mock evolutions " );
		changeLog1.setType(ChangeLogType.NEW_USE_CASE);
		
		changeLog1.setVersion("1.5.0");
		changeLog1.setChangeInformation("A Change Log created for tests");
		
		///////////////////////////////
		
		ChangeLog changeLog2 = new ChangeLog();
		
		changeLog2.setIdentify(String.valueOf(1234561) );
		changeLog2.setDescription( "Evolution in variation point in target" );
		changeLog2.setType(ChangeLogType.UPGRADING);
		
		changeLog2.setVersion("1.5.0");
		changeLog2.setChangeInformation("Evolution in variation point in target");
		
		
		//////////////////////////////
		
		ChangeLog changeLog3 = new ChangeLog();
		
		changeLog3.setIdentify(String.valueOf(1234561) );
		changeLog3.setDescription( "Evolution Update" );
		changeLog3.setType(ChangeLogType.UPGRADING);
		
		changeLog3.setVersion("1.5.0");
		changeLog3.setChangeInformation("Evolution in conditional execution in target");
	
		//////////////////////////////
		
		
		changeLog1.addClassChangeLog(_classB);
		changeLog1.addClassChangeLog(_classC);
		changeLog1.addClassChangeLog(_classE);
		changeLog1.addClassChangeLog(_classF);
		
		changeLog2.addClassChangeLog(_classG);
		
		changeLog3.addClassChangeLog(_classH);
		
		featureCore.addChangeLog(changeLog1);
		featureCore.addChangeLog(changeLog2);
		featureCore.addChangeLog(changeLog3);
		
		changeLogHistory.addFeature(featureCore);
		
		return changeLogHistory;
	}
	

}
