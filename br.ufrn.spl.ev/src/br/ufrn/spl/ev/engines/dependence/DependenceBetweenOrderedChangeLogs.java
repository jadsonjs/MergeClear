/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev.engines.dependence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.ufrn.spl.ev.models.changeloghistorymodel.AssetChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;
import br.ufrn.spl.ev.models.dependencemodel.DependenceModel;
import br.ufrn.spl.ev.parsers.DependenceModelParser;
import br.ufrn.spl.ev.parsers.DependenceModelParserFactory;

/**
 * This class consider that change logs will a identity > the actual change log were made after the actual, so will never exist dependence.
 * 
 * If the change log was made before, if it has the same class, so there is a dependence.
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class DependenceBetweenOrderedChangeLogs implements DependenceAnalysisStrategy {

	/**
	 * @see br.ufrn.spl.ev.engines.dependence.DependenceAnalysisStrategy#mountDependenceSource(br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory)
	 */
	@Override
	public DependenceModel buildMapOfDependences(ChangeLogHistory historyChangeLog, File file) {
		
		long time = System.currentTimeMillis();
		
		DependenceModel map =  new DependenceModel();
		
		if(historyChangeLog == null) return map;
		
		// O^4^4 
		for (FeatureChangeLog feature : historyChangeLog.getFeatures()) {
			for (ChangeLog changeLog : feature.getChangelogs() ) {
				buildDependencesOfChangeLog(map, changeLog, historyChangeLog);
			}
		}
		
		System.out.println( "Mount Dependence Spent: "+((System.currentTimeMillis()-time)/1000 )+" second(s)");
		
		DependenceModelParser parser = DependenceModelParserFactory.getDependenceModelParser();
		parser.writeDependenceFile(map, file);
		return map;
	}

	
	private void buildDependencesOfChangeLog(DependenceModel map, ChangeLog changeLog, ChangeLogHistory historyChangeLog) {
		
		//List<ChangeLog> dependencesFound = new ArrayList<ChangeLog>();
		
		// O^2
		for (FeatureChangeLog feature : historyChangeLog.getFeatures()) {
			forChangeLog:
			for (ChangeLog dependentChangeLog : feature.getChangelogs() ) {
				
				/* Important: Verify just the number of the task is not true, just a approximation !!!!
				 *
				 * We should verify all commits of the asset before of the integration commit
				 * that were not integrated yet and that change the same asset (field or method)
				 * 
				 * We should keep a history of integrated task and commits
				 * 
				 */
				if(Integer.parseInt( dependentChangeLog.getIdentify() ) < Integer.parseInt( changeLog.getIdentify() ) ){
					
					hasSomeComumClass(map, changeLog, dependentChangeLog );
					
				}else{  
					/* if the changeLogToBeAnalyzed is bigger than changeLog, was made after and cannot be a dependence
					 * and how the changelogs are ordered all after this cannot be a dependence too, so break. */
					break forChangeLog;
				}
				
			}
		}
		
		//return dependencesFound;
	}

	private boolean hasSomeComumClass(DependenceModel map, ChangeLog changeLog, ChangeLog dependentChangeLog) {
		for (ClassChangeLog _class1 : changeLog.getClasses() ) {
			for (ClassChangeLog _class2 : dependentChangeLog.getClasses() ) {
				if(_class1.getSignature().equals(_class2.getSignature()) ){ // has at least one class iquals
					
					
					List<MethodChangeLog> comumMethods  = hasSomeComumMethod(_class1, _class2);
					
					List<FieldChangeLog> comumFields  = hasSomeComumField(_class1, _class2);
					
					if( comumFields.size() > 0 || comumMethods.size() > 0 ){
						List<AssetChangeLog> comumAsset = new ArrayList<AssetChangeLog>();
						comumAsset.addAll(comumMethods);
						comumAsset.addAll(comumFields);
						
						map.putAll(changeLog, dependentChangeLog, comumAsset);
					}
				}
			}
		}
		return false;
	}

	
	private List<MethodChangeLog> hasSomeComumMethod(ClassChangeLog _class1, ClassChangeLog _class2) {
		
		List<MethodChangeLog> methods = new ArrayList<MethodChangeLog>();
		
		for (MethodChangeLog method1 : _class1.getMethods() ) {
			for (MethodChangeLog method2 : _class2.getMethods() ) {
				if(method1.getSignature().equals(method2.getSignature())){ // has at least one method
					methods.add(method1);
				}
			}
		}
		return methods;
	}
	
	private List<FieldChangeLog> hasSomeComumField(ClassChangeLog _class1, ClassChangeLog _class2) {
		
		List<FieldChangeLog> fields = new ArrayList<FieldChangeLog>();
		
		for (FieldChangeLog field1 : _class1.getFields() ) {
			for (FieldChangeLog field2 : _class2.getFields() ) {
				if(field1.getSignature().equals(field2.getSignature())){ // has at least one field
					fields.add(field1);
				}
			}
		}
		
		return fields;
	}


}
