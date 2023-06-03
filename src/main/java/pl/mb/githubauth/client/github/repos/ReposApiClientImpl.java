package pl.mb.githubauth.client.github.repos;

import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.mb.githubauth.client.github.GitHubClient;
import pl.mb.githubauth.client.github.GitHubClientProperties;
import pl.mb.githubauth.model.ghentities.GHRepository;
import pl.mb.githubauth.model.ghentities.GHSimpleUser;

import java.util.List;

@Service
public class ReposApiClientImpl extends GitHubClient implements ReposApiClient {

    private final String ENDPOINT = "https://api.github.com/repos";

    private final Log logger = LogFactory.getLog(this.getClass());

    public ReposApiClientImpl(GitHubClientProperties gitHubClientProperties,
                              OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
                              @Qualifier("githubRestTemplate") RestTemplate restTemplate) {
        super(gitHubClientProperties, oAuth2AuthorizedClientService, restTemplate);
    }

    @Override
    @Retry(name = "defaultRetry")
    @TimeLimiter(name = "defaultTimeLimiter")
    public GHRepository getRepo(String owner, String repositoryName) {
        if (owner == null || repositoryName == null)
            throw new IllegalArgumentException();
        logger.info("Getting repo information for: " + ENDPOINT + "/" +owner + "/" + repositoryName);
        return
                executeGithubRequest(
                        HttpMethod.GET, new HttpEntity<>(createHttpHeaders()), GHRepository.class,
                        ENDPOINT, null, owner, repositoryName);
    }

    @Override
    @Retry(name = "defaultRetry")
    @TimeLimiter(name = "defaultTimeLimiter")
    public List<GHSimpleUser> getRepoStargazers(String owner, String repositoryName) {
        final String STARGAZERS = "stargazers";
        if (owner == null || repositoryName == null)
            throw new IllegalArgumentException();
        logger.info("Getting repo stargazers information for: " + ENDPOINT + "/" + owner + "/" + repositoryName + "/" + STARGAZERS);
        return
                executeGithubRequest(
                        HttpMethod.GET, new HttpEntity<>(createHttpHeaders()), new ParameterizedTypeReference<List<GHSimpleUser>>(){},
                        ENDPOINT, null, owner, repositoryName, STARGAZERS);
    }
}
