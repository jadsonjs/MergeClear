package br.ufrn.spl.ev.engines.connectors.git;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
//import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import br.ufrn.spl.ev.engines.connectors.Connector;
import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.util.GUIUtils;
import br.ufrn.spl.ev.util.StringUtils;

public class GitConnector extends Connector implements RepositoryConnector{
	
	private GitHubClient githubClientManager;
	private RepositoryService githubRepositoryService;
	private CommitService commitService;
	private IRepositoryIdProvider repository_id;
	
	protected String repositoryPath;
	protected String lastRevision;
	String localPath;
	private Iterable<RevCommit> commitsInRange;
	private List<RevCommit> commitsOnRange;
	
	@Override
	public void performSetupOnLine() {
		repository_id = RepositoryId.createFromUrl(url);
		githubClientManager = new GitHubClient().setOAuth2Token(password);
		githubRepositoryService = new RepositoryService();
		commitService = new CommitService(githubClientManager);
		try {
			lastRevision = commitService.getCommits(repository_id).get(0).getSha();
		} catch (IOException e) {
			System.out.println("Erro na comunicacao do repositorio Github: ");
			e.printStackTrace();
		}
	}
	
	@Override
	public void performSetupOffLine() {
		// not necessary	
	}

	@Override
	public void performRunOnLine() {
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String projectLocalName = StringUtils.extractAnalyzedProjectName(repositoryPath, side);
		localPath = workspace.getRoot().getLocation().toString()+"/"+projectLocalName;
		File file = new File(localPath);
		if( ! file.exists() ){
			System.out.println("Creating a local project: "+localPath);
			new File(localPath).mkdir();
			
			try {
				Git.cloneRepository()
				.setURI(githubRepositoryService.getRepository(repository_id).getCloneUrl())
				//.setBranch(this.startVersion) para o caso de usar as tags 
				.setDirectory(file)
				.call();
				
				FileRepositoryBuilder builder = new FileRepositoryBuilder();
				Repository repository = builder.setWorkTree(file).readEnvironment().findGitDir().build();
				ObjectId start = repository.resolve(this.startVersion);
				ObjectId end = repository.resolve(this.endVersion);
				//System.out.println("START VERSION "+start.getName()+" | "+"END VERSION "+ end.getName());

				commitsInRange = new Git(repository).log().addRange(start,end).call();	
				commitsOnRange = new ArrayList<RevCommit>();
				Iterable<RevCommit> start_only = new Git(repository).log().add(start).setMaxCount(1).call();
				
				for (RevCommit commit : start_only) {
					//System.out.println(commit.abbreviate(7).name() + " " + commit.getShortMessage());
					commitsOnRange.add(commit);
				}
				
				for (RevCommit commit : commitsInRange) {
					//System.out.println(commit.abbreviate(7).name() + " " + commit.getShortMessage());
					commitsOnRange.add(commit);
				}

				repository.close();
				
				// Para projetos não eclipse, não se tem o arquivo .project, tem que colocar manualmente
				//System.out.println("Rename local project configuration file. ");
				//FileUtil.replaceAFileText(localPath+"/.project", "<name>"+projectOriginalName+"</name>", "<name>"+projectLocalName+"</name>");
				//System.out.println("End checkout \""+projectOriginalName+"\" to \""+localPath+"\"");
				
			} catch (GitAPIException | IOException e) {
				e.printStackTrace();
				GUIUtils.addErrorMessage("Error on performing checkout of the project");
			}
			System.out.println("End checkout \""+projectLocalName+"\". " + " to \""+localPath+"\"");
		}else{
			System.out.println("Updating the local project..."); 
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			Repository repository;
			try {
				// TODO - Remover código repetido
				repository = builder.setWorkTree(file).readEnvironment().findGitDir().build();
				ObjectId start = repository.resolve(this.startVersion);
				ObjectId end = repository.resolve(this.endVersion);
				System.out.println("START VERSION "+start.getName()+" | "+"END VERSION "+ end.getName());
				commitsInRange = new Git(repository).log().addRange(start,end).call();
				commitsOnRange = new ArrayList<RevCommit>();
				Iterable<RevCommit> start_only = new Git(repository).log().add(start).setMaxCount(1).call();
				
				for (RevCommit commit : start_only) {
					commitsOnRange.add(commit);
				}
				for (RevCommit commit : commitsInRange) {
					commitsOnRange.add(commit);
				}
			} catch (IOException | GitAPIException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void performRunOffLine() {
	}
	
	@Override
	public void performsRun() {
		performSetupOnLine();
		performRunOnLine();
	}
	
	@Override
	public List<String> getFilesOfRevision(String Revision) throws Exception {
		List<String> commitFilesString = new ArrayList<String>();
		RepositoryCommit lastCommit = commitService.getCommit(repository_id, Revision);
		List<CommitFile> commitFiles = lastCommit.getFiles();
		
		for (CommitFile commitFile : commitFiles) {
			commitFilesString.add(commitFile.getFilename().toString());
		}
		return commitFilesString;
	}
	
	@Override
	public CompilationUnit getClassContentInTheRevision(String classPath, String revisionNumber) throws Exception {
		// Adicionei pois não consegui encontrar as chamadas de setup para o GitConnector
//		performSetupOnLine();
//		performRunOnLine();
		
		InputStream in = null;
		File file = new File(localPath+"/.git");
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(file).readEnvironment().findGitDir().build();
		ObjectId commitId = repository.resolve(revisionNumber);
		RevWalk revWalk = new RevWalk(repository);
		RevCommit commit = revWalk.parseCommit(commitId);
		RevTree tree = commit.getTree();

		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		treeWalk.setFilter(PathFilter.create(classPath));
		//System.out.println("Walk para o commit: " + revisionNumber + "Classe: " + classPath);
		if (!treeWalk.next()) {
			throw new IllegalStateException("is not a file in revision " + classPath);
		}
		ObjectId objectId = treeWalk.getObjectId(0);
		ObjectLoader loader = repository.open(objectId);

		in = loader.openStream();
		return JavaParser.parse(in);
	}
	
	@Override
	public String getPreviousRevision(String path, String actualRevision)
			throws Exception {
		
		RepositoryCommit commit = commitService.getCommit(repository_id, actualRevision);
		//System.out.print("Pais: ");
//		for (Commit p : commit.getParents()) {
//			System.out.println(p.getSha()+ " - ");
//		}
		return commit.getParents().get(0).getSha();
	}
	
	@Override
	public String toString() {
		return lastRevision;
	}

	@Override
	public String getRepositoryPath() {
		return repositoryPath;
	}

	public List<RevCommit> getCommitsInRange(){
		return commitsOnRange;
	}
	public GitHubClient getGithubClientManager() {
		return githubClientManager;
	}

	public void setGithubClientManager(GitHubClient githubClientManager) {
		this.githubClientManager = githubClientManager;
	}

	public RepositoryService getGithubRepositoryService() {
		return githubRepositoryService;
	}

	public void setGithubRepositoryService(RepositoryService githubRepositoryService) {
		this.githubRepositoryService = githubRepositoryService;
	}

	public CommitService getCommitService() {
		return commitService;
	}

	public void setCommitService(CommitService commitService) {
		this.commitService = commitService;
	}

	public IRepositoryIdProvider getRepository_id() {
		return repository_id;
	}

	public void setRepository_id(IRepositoryIdProvider repository_id) {
		this.repository_id = repository_id;
	}

	public void setRepositoryPath(String property) {
		this.repositoryPath = property;
	}

	public String getLastRevision() {
		return lastRevision;
	}

	public void setLastRevision(String lastRevision) {
		this.lastRevision = lastRevision;
	}
}
