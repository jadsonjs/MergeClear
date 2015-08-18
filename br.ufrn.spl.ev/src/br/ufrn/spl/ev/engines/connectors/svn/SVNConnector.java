/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.connectors.svn;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNFileRevision;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc2.SvnCheckout;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnTarget;

import br.ufrn.spl.ev.engines.connectors.Connector;
import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.util.FileUtil;
import br.ufrn.spl.ev.util.GUIUtils;
import br.ufrn.spl.ev.util.StringUtils;

/**
 * Connector to the SVN of IProject
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - cria��o da classe.
 * @since 29/07/2013
 *
 */
public class SVNConnector extends Connector implements RepositoryConnector{

	private ISVNAuthenticationManager authm;
	
	private SVNRepository svnRepository;
	private SVNClientManager svnClientManager;
	private SvnOperationFactory svnOperationFactory;
	
	private long lastRevision;
	
	/** Used to make checkout of the projects analyzed, to not access remote files*/
	protected String repositoryPath;
	
	
	@Override
	public void performSetupOnLine() {
		try{
			SVNRepositoryFactoryImpl.setup();
			authm = SVNWCUtil.createDefaultAuthenticationManager(user, password);
			
			svnRepository = SVNRepositoryFactoryImpl.create(SVNURL.parseURIEncoded(url));
			svnRepository.setAuthenticationManager(authm);
			
			svnClientManager = SVNClientManager.newInstance();
			svnClientManager.setAuthenticationManager(authm);
			
			lastRevision = svnRepository.getLatestRevision();
			
		}catch(SVNException svne){
			//throw new MiningException("Erro na comunicacao do repositorio SVN: " + svne.getMessage());
			System.out.println("Erro na comunicacao do repositorio SVN: " + svne.getMessage());
			svne.printStackTrace();
		}
	}

	@Override
	public void performSetupOffLine() {
		// not necessary
	}

	@Override
	public void performRunOnLine() {
		try {
			
			svnOperationFactory = new SvnOperationFactory();
			svnOperationFactory.setAuthenticationManager(authm);
			
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			
			String projectLocalName = StringUtils.extractAnalyzedProjectName(repositoryPath, side);
			
			String projectOriginalName = StringUtils.extractLastNameAfterSlash(repositoryPath);
			
			// The path where the project will be safe locally /workspace/ProjectName_SOURCE or  /workspace/ProjectName_TARGET
			String localPath = workspace.getRoot().getLocation().toString()+"/"+projectLocalName;
			
			File file = new File(localPath);
			if( ! file.exists() ){
				System.out.println("Creating a local project: "+localPath);
				new File(localPath).mkdir();
				
				 SvnCheckout checkout = svnOperationFactory.createCheckout();
				 checkout.setSource(SvnTarget.fromURL(org.tmatesoft.svn.core.SVNURL.parseURIEncoded( url+repositoryPath ) ));
				 checkout.setSingleTarget(SvnTarget.fromFile(  file ) );
				    
				System.out.println("Checkout project, please wait..... ");
				checkout.run();
				
				System.out.println("Rename local project configuration file. ");
				
				FileUtil.replaceAFileText(localPath+"/.project", "<name>"+projectOriginalName+"</name>", "<name>"+projectLocalName+"</name>");
				
				System.out.println("End checkout \""+projectOriginalName+"\" to \""+localPath+"\"");
				
			}else{
				
				// will not update to be faster with the tests, but is necessary !!!!!
				 System.out.println("Updating the local project "+url+ StringUtils.extractAnalyzedProjectName(repositoryPath, side) +" ..... "); 
				 //SvnUpdate update = svnOperationFactory.createUpdate();
				 //update.setSingleTarget(SvnTarget.fromFile( file ));
		         //update.run();
		         //System.out.println("End Update  !!!!! ");
			}
		
		} catch (Exception ex) {
			ex.printStackTrace();
			GUIUtils.addErrorMessage("Erro performs checkout of the project "+repositoryPath);
		} finally {
		    svnOperationFactory.dispose();
		}
	}
	
	@Override
	public void performRunOffLine() {
		// not necessary
	}
	
	
	/**
	 * @throws SVNException 
	 * @throws NumberFormatException 
	 * @see br.ufrn.spl.ev.engines.connectors.RepositoryConnector#getFilesOfRevision(java.lang.String)
	 */
	@Override
	public  List<String> getFilesOfRevision(String revision) throws NumberFormatException, SVNException {
		
		 List<String> filesPath = new ArrayList<String>();
		
		Collection<SVNLogEntry> logEntries = new LinkedList<SVNLogEntry>();
		

		// read files of a specific revision
		
		getSVNRepository().log(new String[] {""}, logEntries, Long.valueOf(revision), Long.valueOf(revision), true, true);
	
		for (SVNLogEntry svnLogEntry : logEntries) {
			
			for (String key : svnLogEntry.getChangedPaths().keySet() ){
				
				SVNLogEntryPath SVNpath = svnLogEntry.getChangedPaths().get(key);
				
				filesPath.add( SVNpath.getPath() );
			}
		}
		
		return filesPath;
		
	}
	
	/***
	 * 'Checkout" the content of a class in revision. In other words, the
	 * content that the class had in the revision.
	 * 
	 * <p>
	 * Created at: 13/08/2013
	 * </p>
	 * 
	 * @return
	 * @throws ParseException
	 * @throws SVNException
	 */
	public CompilationUnit getClassContentInTheRevision(String classPath, String revisionNumber) throws ParseException, SVNException {

		SVNWCClient wcclient = getClientManager().getWCClient();

		ByteArrayOutputStream content = new ByteArrayOutputStream();

		// Mudei aqui
		SVNRevision revision = SVNRevision.create(Long.valueOf(revisionNumber));
		String repoUrl = getUrl();
		SVNURL svnRepoUrl = SVNURL.parseURIEncoded(repoUrl + "/" + classPath);

		wcclient.doGetFileContents(svnRepoUrl, revision, revision, true, content);

		InputStream inContent = new ByteArrayInputStream(content.toByteArray());
		return JavaParser.parse(inContent);
	}
	
	
	/**
	 * Get the previous revisions.
	 * 
	 * @param path
	 * @param sourceRevision
	 * @return
	 * @throws SVNException
	 */
	public String getPreviousRevision(String path, String actualRevision) throws SVNException {

		SVNRevision actualRevisionSVN = SVNRevision.create(Long.valueOf(actualRevision));
		
		// Take all revision from the zero to the actual revision //
		List<SVNFileRevision> revisions = new ArrayList<SVNFileRevision>();
		getSVNRepository().getFileRevisions(path, revisions, 0, actualRevisionSVN.getNumber());

		// find where is the source revision in order to get the revision before
		int indexOfSourceRevision = 0;
		SVNFileRevision revisionBefore = null;

		for (SVNFileRevision fileRev : revisions) {
			if (fileRev.getRevision() == actualRevisionSVN.getNumber()) {
				indexOfSourceRevision = revisions.indexOf(fileRev);
				break; // don't need to continue
			}
		}

		// if the index is 0, then the revision is the first and do not have a
		// before revision
		if (indexOfSourceRevision == 0) {
			return ""+actualRevisionSVN.getNumber();
		} else {
			revisionBefore = revisions.get(indexOfSourceRevision - 1);
		}

		// SVNFileRevision revision = revisions.get(revisions.size()-2);
		return ""+revisionBefore.getRevision();
	}
	
	
	////// sets e gets ///////
	
	public SVNRepository getSVNRepository() {
		return svnRepository;
	}

	public void setSVNRepository(SVNRepository svnRepository) {
		this.svnRepository = svnRepository;
	}

	public SVNClientManager getClientManager() {
		return svnClientManager;
	}

	public void setClientManager(SVNClientManager svnClientManager) {
		this.svnClientManager = svnClientManager;
	}

	public long getLastRevision() {
		return lastRevision;
	}

	public void setLastRevision(long lastRevision) {
		this.lastRevision = lastRevision;
	}

	public String getRepositoryPath() {
		return repositoryPath;
	}

	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}
}
