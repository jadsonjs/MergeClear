/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.svn;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeTypeRepository;

/**
 * <p>Test read information form a specific revision</p>
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 01/11/2013
 * 
 */
public class SVNReadRevisionInformationTest {

	public static void main(String args[]){
		
		SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
		
		try {
			
			Properties propertiesTemp = new Properties();
			propertiesTemp.load(new FileInputStream(new File("C:/temp/connections.properties")));
			
			String svnURL = propertiesTemp.getProperty("TARGET_REPOSITORY_URL");
			String svnUser = propertiesTemp.getProperty("TARGET_REPOSITORY_USER");
			String svnPassword = propertiesTemp.getProperty("TARGET_REPOSITORY_PASSWORD");
			
			SVNRepositoryFactoryImpl.setup();
			SVNRepository svnRepository = SVNRepositoryFactoryImpl.create(SVNURL.parseURIEncoded(svnURL));
			ISVNAuthenticationManager authm = SVNWCUtil.createDefaultAuthenticationManager(svnUser, svnPassword);
			svnRepository.setAuthenticationManager(authm);
			
			svnOperationFactory.setAuthenticationManager(authm);
			
			Collection<SVNLogEntry> logEntries = new LinkedList<SVNLogEntry>();
			
			svnRepository.log(new String[] {""}, logEntries, Long.valueOf("45138"), Long.valueOf("45138"), true, true);
			
			for (SVNLogEntry svnLogEntry : logEntries) {
				
				for (String key : svnLogEntry.getChangedPaths().keySet() ){
					
					SVNLogEntryPath SVNpath = svnLogEntry.getChangedPaths().get(key);
					
					String path = SVNpath.getPath();
					
					if(path.endsWith(".java")){ // if is a java class
						
						String changeTYpe = SVNpath.getType() == 'A' ? ChangeTypeRepository.ADDED : ( SVNpath.getType() == 'M' || SVNpath.getType() == 'U' ? ChangeTypeRepository.UPDATED : ( SVNpath.getType() == 'D' ? ChangeTypeRepository.DELETE : "?" ) );
					
						System.out.println("["+changeTYpe+"] "+path);
					}
				}
				
			}
			
			
		    
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    svnOperationFactory.dispose();
		}
	}
	
}


