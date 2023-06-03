package pl.mb.githubauth.client.github.user;

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

import java.util.List;

@Service
public class UserApiClientImpl extends GitHubClient implements UserApiClient {

    private final String ENDPOINT = "https://api.github.com//user";

    private final Log logger = LogFactory.getLog(this.getClass());

    public UserApiClientImpl(GitHubClientProperties gitHubClientProperties,
                             OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
                             @Qualifier("githubRestTemplate") RestTemplate restTemplate) {
        super(gitHubClientProperties, oAuth2AuthorizedClientService, restTemplate);
    }

    @Override
    @Retry(name = "defaultRetry")
    @TimeLimiter(name = "defaultTimeLimiter")
    public List<GHRepository> getLoggedUserRepos() {
        final String REPOS = "repos";
        logger.info("Getting logged user repos list for: " + ENDPOINT + "/" + REPOS);
        return
                executeGithubRequest(
                        HttpMethod.GET, new HttpEntity<>(createHttpHeaders()), new ParameterizedTypeReference<List<GHRepository>>(){},
                        ENDPOINT, null, REPOS);
    }
}
