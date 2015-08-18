/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.svn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * A test class to the SVN Connector
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class criation.
 * @since 11/09/2013
 *
 */
public class SVNConnectorTest {

	/**
	 * Test how to read files from a specific revision
	 * 
	 * <p> Created at:  11/09/2013  </p>
	 *
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws SVNException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, SVNException {
		Properties propertiesTemp = new Properties();
		propertiesTemp.load(new FileInputStream(new File("C:/temp/Repository/connections.properties")));
		
		String svnURL = propertiesTemp.getProperty("SOURCE_REPOSITORY_URL");
		String svnUser = propertiesTemp.getProperty("SOURCE_REPOSITORY_USER");
		String svnPassword = propertiesTemp.getProperty("SOURCE_REPOSITORY_PASSWORD");
		
		SVNRepositoryFactoryImpl.setup();
		SVNRepository svnRepository = SVNRepositoryFactoryImpl.create(SVNURL.parseURIEncoded(svnURL));
		ISVNAuthenticationManager authm = SVNWCUtil.createDefaultAuthenticationManager(svnUser, svnPassword);
		svnRepository.setAuthenticationManager(authm);
		
		SVNClientManager svnClientManager = SVNClientManager.newInstance();
		svnClientManager.setAuthenticationManager(authm);
		
		
		Collection<SVNLogEntry> logEntries = new LinkedList<SVNLogEntry>();
		
		// read files of a specific revision
		svnRepository.log(new String[] {""}, logEntries, 46266, 46266, true, true);
		
		for (SVNLogEntry svnLogEntry : logEntries) {
			
			for (String key : svnLogEntry.getChangedPaths().keySet() ){
				SVNLogEntryPath path = svnLogEntry.getChangedPaths().get(key);
				System.out.println("["+path.getType()+"] "+path.getPath());
			}
			
		}
	}

}
