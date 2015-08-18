package br.ufrn.spl.ev.engines.miners.iproject;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;

/**
 * Executa um agrupamento das mudanças por ClassChangeLog.
 *  
 * Durante a mineraçaõ do SVN é natural que a mesma classe seja evoluida várias vezes em decorrência do processo de desenvolvimento,
 * testes e integração, porém, as mudanças em métodos e atributos devem ser agrupadas por Classe.
 * 
 * @author Gleydson
 *
 */
public class ExecuteChangelogGroup {

	/**
	 * execute grouping.
	 * @param changeLogHistory
	 */
	public void group(ChangeLogHistory changeLogHistory ) {
		
		for ( FeatureChangeLog f : changeLogHistory.getFeatures() ) {
			
			for ( ChangeLog cl : f.getChangelogs() ) {
				
				List<ClassChangeLog> groupedList = new ArrayList<ClassChangeLog>();
				
				for ( ClassChangeLog ccl : cl.getClasses() ) {
					
					int idx = groupedList.indexOf(ccl);
				
					if ( idx == -1 ) { 
						groupedList.add(ccl);
					} else {
						
						ClassChangeLog actualChangeLog = groupedList.get(idx);
						
						for ( FieldChangeLog field : ccl.getFields() ) {
							
							if (! actualChangeLog.getFields().contains(field) ) 
								actualChangeLog.getFields().add( field );
							
						}
						
						for ( MethodChangeLog method : ccl.getMethods() ) {
							
							if (! actualChangeLog.getMethods().contains(method) ) 
								actualChangeLog.getMethods().add( method );
						}
						
					}
				}
				
				cl.setClasses(groupedList);
			}
		}
	}
}
