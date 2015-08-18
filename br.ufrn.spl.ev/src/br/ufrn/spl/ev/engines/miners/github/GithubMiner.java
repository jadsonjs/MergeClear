package br.ufrn.spl.ev.engines.miners.github;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.jgit.revwalk.RevCommit;

import br.ufrn.spl.ev.PluginConstants;
import br.ufrn.spl.ev.engines.connectors.git.GitConnector;
import br.ufrn.spl.ev.engines.miners.Miner;
import br.ufrn.spl.ev.engines.miners.repository.RepositoryAnalisisExecutor;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogType;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeTypeRepository;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.util.ConfigUtils;
import br.ufrn.spl.ev.util.ConfigurationKnowledgeUtil;
import br.ufrn.spl.ev.util.StringUtils;

public class GithubMiner extends Miner {
	
	IRepositoryIdProvider repository_id;
	GithubConnector githubConnector;
	GitConnector gitConnector;
	String repoOwner;
	@Override
	public ChangeLogHistory performMining(Properties configurationKnowledge) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		System.out.println(" Reading information of Github  1 of 3 ... ");
		// Nao esta generico ainda.
		githubConnector = ((GithubConnector) systemConnector);
		gitConnector = ((GitConnector) repositoryConnector);
		githubConnector.performsRun();
		gitConnector.performsRun();
		repository_id = githubConnector.getRepository_id();
		//List<Issue> issues = githubConnector.getIssuesOfEvolution();
		List<PullRequest> pullRequests = githubConnector.getPullRequestsOfEvolution();
		List<RevCommit> commitsInRange = gitConnector.getCommitsInRange();
		repoOwner = githubConnector.getGithubRepository().getOwner().getLogin();
		System.out.println(" Reading information of Github in "+((System.currentTimeMillis()-startTime)/1000/60)+" minutes ");
		
		System.out.println(" Converting Github Isseus To ChangeLogHistory Model  2 of 3 ...  ");
		ChangeLogHistory changeLogHistory = convertGithubIssuesToChangeLogHistoryModel(commitsInRange, pullRequests, configurationKnowledge);
		System.out.println(" Converting Github Isseus To ChangeLogHistory Model in "+((System.currentTimeMillis()-startTime)/1000/60)+" minutes ");
		
		System.out.println(" Retrieving Retrieve Changed From Respository  3 of 3 ... ");
				
		changeLogHistory = new RepositoryAnalisisExecutor(repositoryConnector).retrieveChangedFromRespository(changeLogHistory);
		System.out.println(" Retrieving Changed From Respository in "+((System.currentTimeMillis()-startTime)/1000/60)+" minutes ");
		
		return changeLogHistory;
	}
	
	private ChangeLogHistory convertGithubIssuesToChangeLogHistoryModel(List<RevCommit> commitsInRange, List<PullRequest> pullRequests, Properties configurationKnowledge){
		ChangeLogHistory changeLogHistory = new ChangeLogHistory();
		changeLogHistory.setStartVersion(githubConnector.getStartVersion());
		changeLogHistory.setBaseVersion(githubConnector.getEndVersion());
		List<ClassChangeLog> classChangeLog;
		
			nextRevCommit:
				for (RevCommit commit : commitsInRange) {
					ChangeLog changeLog = new ChangeLog();
					
					changeLog.setIdentify(commit.abbreviate(7).name());
					//Tratando a msg do github por eventuais /n ou caracteres especiais
					String msg_formatada = commit.getFullMessage().replaceAll("\\r\\n|\\r|\\n", " ");
					msg_formatada.replaceAll("[^a-zA-Z]+","");
					changeLog.setDescription(msg_formatada);
					changeLog.setModule(githubConnector.getGithubRepository().getName());
					
					///  the version where the change was made ///
					changeLog.setVersion(commit.abbreviate(7).name());
					
					// Alterei aqui
					classChangeLog = retrieveClassesFromRevisionNumber(commit.getId().getName()); 
					if( classChangeLog != null)
						changeLog.addClassesChangeLog(classChangeLog);
						
					if (changeLog.getClasses().size() == 0) // some issues doesn't have revision number
						continue nextRevCommit;					

					// VER QUE ARQUIVO DE KNOWLEDGE É ESSE
					List<FeatureChangeLog> featuresMapping = ConfigurationKnowledgeUtil.getFeaturesOfATask(configurationKnowledge, commit+"");
					
					if(featuresMapping.size() == 0 ){ // no feature associate with this task
						
						FeatureChangeLog featureCore =  changeLogHistory.getFeature(ChangeLogHistory.CORE.getName());
						if(featureCore == null){ // if the feature CORE does not exist yet
							featureCore = ChangeLogHistory.createNewCoreFeature();
							featureCore.addChangeLog(changeLog);
							changeLogHistory.addFeature(featureCore);
						}else{
							featureCore.addChangeLog(changeLog);
						}
					}else{
						for (FeatureChangeLog featureMapping : featuresMapping) {
							FeatureChangeLog featureTemp =  changeLogHistory.getFeature(featureMapping.getName());
							if(featureTemp == null){  // if the feature  does not exist yet
								featureTemp = new FeatureChangeLog(featureMapping.getName(), featureMapping.getParent(),featureMapping.getType(), featureMapping.getDescription());
								featureTemp.addChangeLog(changeLog);
								changeLogHistory.addFeature(featureTemp);
							}else{
								featureTemp.addChangeLog(changeLog);
							}
						}
					}
			}
		
		nextRequest:
			for (PullRequest pullRequest : pullRequests) {
				System.out.println("PR: " + pullRequest.getNumber());
				ChangeLog changeLog = new ChangeLog();
				
				changeLog.setIdentify(String.valueOf(pullRequest.getNumber()) );
				changeLog.setDescription( pullRequest.getBodyText());
				changeLog.setModule(githubConnector.getGithubRepository().getName());
				
				// o PR não retorna o label na API
				changeLog.setType(ChangeLogType.REFACTORY);				
				///  the version where the change was made ///
				changeLog.setVersion(pullRequest.getCreatedAt().toString());
				
				try {
					
					// Tive que adicionar isso para pegar os PRs apenas de um link, pois os PRs pertencem apenas a um repositorio
					IRepositoryIdProvider repo_id = RepositoryId.createFromUrl(ConfigUtils.getConnection(PluginConstants.SOURCE_SYSTEM_URL));
 					GitHubClient githubClientManager2 = new GitHubClient().setOAuth2Token("28ca0d6f2e8c3b1e87447c13c4a2d93d40fcfd47");
					RepositoryService githubRepositoryService2 = new RepositoryService();
					PullRequestService githubPullRequestService2 = new PullRequestService(githubClientManager2);
					Repository repo = githubRepositoryService2.getRepository(repo_id);
					
					for (RepositoryCommit commit : (githubPullRequestService2.getCommits(repo_id, pullRequest.getNumber()))){
						classChangeLog = retrieveClassesFromRevisionNumber(commit.getSha()); 
						if( classChangeLog != null)
							changeLog.addClassesChangeLog(classChangeLog);
					}
//					for (RepositoryCommit commit : (githubConnector.getGithubPullRequestService().getCommits(repository_id, pullRequest.getNumber()))){
//						changeLog.addClassesChangeLog(retrieveClassesFromRevisionNumber(commit.getSha()));
//					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				if (changeLog.getClasses().size() == 0)
					continue nextRequest;
				
				// associate with the respective feature, if is not associated, put into the CORE feature
				
				List<FeatureChangeLog> featuresMapping = ConfigurationKnowledgeUtil.getFeaturesOfATask(configurationKnowledge, String.valueOf(pullRequest.getId()));
				
				
				if(featuresMapping.size() == 0 ){ // no feature associate with this task
					
					FeatureChangeLog featureCore =  changeLogHistory.getFeature(ChangeLogHistory.CORE.getName());
					if(featureCore == null){ // if the feature CORE does not exist yet
						featureCore = ChangeLogHistory.createNewCoreFeature();
						featureCore.addChangeLog(changeLog);
						changeLogHistory.addFeature(featureCore);
					}else{
						featureCore.addChangeLog(changeLog);
					}
				}else{
					
					for (FeatureChangeLog featureMapping : featuresMapping) {
						FeatureChangeLog featureTemp =  changeLogHistory.getFeature(featureMapping.getName());
						if(featureTemp == null){  // if the feature  does not exist yet
							featureTemp = new FeatureChangeLog(featureMapping.getName(), featureMapping.getParent(),featureMapping.getType(), featureMapping.getDescription());
							featureTemp.addChangeLog(changeLog);
							changeLogHistory.addFeature(featureTemp);
						}else{
							featureTemp.addChangeLog(changeLog);
						}
					}
				}
	        	
	        }
		return changeLogHistory;
	}
	
	private List<ClassChangeLog> retrieveClassesFromRevisionNumber(String revision) {
		RepositoryCommit commit;
		List<CommitFile> commitFiles = null;
		List<ClassChangeLog> classes = null;
		try {
			commit = (githubConnector.getCommitService().getCommit(repository_id, revision));
			
				System.out.println("Getting changes from commit: " + commit.getSha());
				//String commitAuthor = commit.getAuthor().getLogin();

				//if (repoOwner.equals(commitAuthor)) {
					// Commit pertence ao mesmo lado em análise
					
					commitFiles = commit.getFiles();
					classes = new ArrayList<ClassChangeLog>();

					for (CommitFile commitFile : commitFiles) {
						String path = commitFile.getFilename().toString();
						if (path.endsWith(".java")) {
							String status = commitFile.getStatus();
							String changeType = status.equalsIgnoreCase("added") ? ChangeTypeRepository.ADDED
									: status.equalsIgnoreCase("modified") ? ChangeTypeRepository.UPDATED
											: status.equalsIgnoreCase("removed") ? ChangeTypeRepository.DELETE
													: "?";
							/*
							 *  Em alguns casos, o path não vem com o nome do usuario/projeto do github, então a remoção de parte path
							 *  em #createClasseSignature  provoca um erro na verificação dos conflitos indiretos por o path não estar de acordo
							 */
							
							String projectName = ((GithubConnector) systemConnector).getSystemName();
							String signature = "";
							if(path.contains(projectName)){
								signature = StringUtils.createClasseSignature(path, projectName);
							}else
								signature = path;
							
							classes.add(new ClassChangeLog(StringUtils.extractLastNameAfterSlash(path), path, signature, changeType, String.valueOf(revision)));
						}
					}
				//}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classes;
	}
}
