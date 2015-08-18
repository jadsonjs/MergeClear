package br.ufrn.spl.ev.engines.miners.github;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;

import br.ufrn.spl.ev.PluginConstants;
import br.ufrn.spl.ev.engines.connectors.Connector;
import br.ufrn.spl.ev.engines.connectors.SystemConnector;
import br.ufrn.spl.ev.util.ConfigUtils;
import br.ufrn.spl.ev.util.StringUtils;

public class GithubConnector extends Connector implements SystemConnector {

	private Properties issuesPRsEvolution;
	private List<Issue> issuesOfEvolution;
	private List<PullRequest> pullRequestsOfEvolution;

	private GitHubClient githubClientManager;
	private RepositoryService githubRepositoryService;
	//private IssueService githubIssueService;
	private PullRequestService githubPullRequestService;
	private Repository githubRepository;
	private IRepositoryIdProvider repository_id;
	private CommitService commitService;

	@Override
	public void performSetupOnLine() {
		repository_id = RepositoryId.createFromUrl(url);
		githubClientManager = new GitHubClient().setOAuth2Token(password);
		githubRepositoryService = new RepositoryService();
		githubPullRequestService = new PullRequestService(githubClientManager);
		//githubIssueService = new IssueService(githubClientManager);
		commitService = new CommitService(githubClientManager);
		try {
			githubRepository = githubRepositoryService.getRepository(repository_id);
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

		//As issues do Github não são mais levadas em consideração na análise
		//issuesOfEvolution = new ArrayList<Issue>();
		pullRequestsOfEvolution = new ArrayList<PullRequest>();
		try {
			//List<Integer> idsIssuesEvolution = new ArrayList<Integer>();
			//String issuesEvolutionIdsStr = issuesPRsEvolution.getProperty("issues");
			
			List<Integer> idsRequestsEvolution = new ArrayList<Integer>();
			String requestsEvolutionIdsStr = issuesPRsEvolution.getProperty("pullrequests");

//			if (StringUtils.isNotEmpty(issuesEvolutionIdsStr)) {
//
//				String[] issuesIds = issuesEvolutionIdsStr.split(";");
//
//				for (String id : issuesIds) {
//					idsIssuesEvolution.add(Integer.valueOf(id));
//				}
//			}
			
			if (StringUtils.isNotEmpty(requestsEvolutionIdsStr)) {

				String[] requestsIds = requestsEvolutionIdsStr.split(";");

				for (String id : requestsIds) {
					idsRequestsEvolution.add(Integer.valueOf(id));
				}
			}

//			for (Integer issueId : idsIssuesEvolution) {
//				issuesOfEvolution.add(githubIssueService.getIssue(githubRepository, issueId));
//			}
			
			// Tive que adicionar isso para pegar os PRs apenas do link do Source
			IRepositoryIdProvider repo_id = RepositoryId.createFromUrl(ConfigUtils.getConnection(PluginConstants.SOURCE_SYSTEM_URL));
 			GitHubClient githubClientManager2 = new GitHubClient().setOAuth2Token(password);
			RepositoryService githubRepositoryService2 = new RepositoryService();
			PullRequestService githubPullRequestService2 = new PullRequestService(githubClientManager2);
			Repository repo = githubRepositoryService2.getRepository(repo_id);

			for (Integer requestId : idsRequestsEvolution) {
				pullRequestsOfEvolution.add(githubPullRequestService2.getPullRequest(repo, requestId));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void performRunOffLine() {
		// not necessary
	}

	public List<Issue> getIssuesOfEvolution() {
		return issuesOfEvolution;
	}

	public Properties getIssuesPRsEvolution() {
		return issuesPRsEvolution;
	}

	public void setIssuesPRsEvolution(Properties issuesPRsEvolution) {
		this.issuesPRsEvolution = issuesPRsEvolution;
	}

	public void setIssuesOfEvolution(List<Issue> issuesOfEvolution) {
		this.issuesOfEvolution = issuesOfEvolution;
	}

	public List<PullRequest> getPullRequestsOfEvolution() {
		return pullRequestsOfEvolution;
	}

	public void setPullRequestsOfEvolution(List<PullRequest> pullRequestsOfEvolution) {
		this.pullRequestsOfEvolution = pullRequestsOfEvolution;
	}

	public IRepositoryIdProvider getRepository_id() {
		return repository_id;
	}

	public void setRepository_id(IRepositoryIdProvider repository_id) {
		this.repository_id = repository_id;
	}

	public PullRequestService getGithubPullRequestService() {
		return githubPullRequestService;
	}

	public void setGithubPullRequestService(
			PullRequestService githubPullRequestService) {
		this.githubPullRequestService = githubPullRequestService;
	}

	public Repository getGithubRepository() {
		return githubRepository;
	}

	public void setGithubRepository(Repository githubRepository) {
		this.githubRepository = githubRepository;
	}

	public CommitService getCommitService() {
		return commitService;
	}

	public void setCommitService(CommitService commitService) {
		this.commitService = commitService;
	}
}
