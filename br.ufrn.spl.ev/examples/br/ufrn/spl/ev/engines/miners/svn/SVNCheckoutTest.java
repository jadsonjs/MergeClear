/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.svn;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc2.SvnCheckout;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnTarget;
import org.tmatesoft.svn.core.wc2.SvnUpdate;

/**
 * test checkout a project to the current work space
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 17/10/2013
 *
 */
public class SVNCheckoutTest {

	public void checkout(String projectPath){
		
		SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
		
		try {
			
			Properties propertiesTemp = new Properties();
			propertiesTemp.load(new FileInputStream(new File("C:/temp/connections.properties")));
			
			String svnURL = propertiesTemp.getProperty("TARGET_REPOSITORY_URL");
			String svnUser = propertiesTemp.getProperty("TARGET_REPOSITORY_USER");
			String svnPassword = propertiesTemp.getProperty("TARGET_REPOSITORY_PASSWORD");
			
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
		
			SVNRepositoryFactoryImpl.setup();
			SVNRepository svnRepository = SVNRepositoryFactoryImpl.create(SVNURL.parseURIEncoded(svnURL));
			ISVNAuthenticationManager authm = SVNWCUtil.createDefaultAuthenticationManager(svnUser, svnPassword);
			svnRepository.setAuthenticationManager(authm);
			
			svnOperationFactory.setAuthenticationManager(authm);
			
			String localPath = workspace.getRoot().getLocation().toString()+"/SIGEvento_TEST";
			
			File file = new File(localPath);
			if( ! file.exists() ){
				new File(localPath).mkdir();
				System.out.println("Criando o diretório: "+localPath);
				
				
			    final SvnCheckout checkout = svnOperationFactory.createCheckout();
			    checkout.setSource(SvnTarget.fromURL(org.tmatesoft.svn.core.SVNURL.parseURIEncoded( svnURL+projectPath ) ));
			    checkout.setSingleTarget(SvnTarget.fromFile(  file ) );
			    
			    System.out.println("Fazendo checkout ..... ");
			    checkout.run();
			    System.out.println("Fim checkout  !!!!! ");
			}else{
				// já existe o projeto no workspace, será que dá para apenas atualizar ?
				
				 SvnUpdate update = svnOperationFactory.createUpdate();
				 update.setSingleTarget(SvnTarget.fromFile( file ));
		         System.out.println("Atualizando "+svnURL+projectPath+" ..... ");
		         update.run();
		         System.out.println("Fim atualização  !!!!! ");
			}
			
			
		    
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    svnOperationFactory.dispose();
		}
	}
	
	
}
