package pl.mb.githubauth.client.github;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import pl.mb.githubauth.client.RegisteredClients;
import pl.mb.githubauth.client.Requester;
import pl.mb.githubauth.utils.ClientUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GitHubClient {

    private final GitHubClientProperties gitHubClientProperties;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    private final RestTemplate restTemplate;

    private final Log logger = LogFactory.getLog(this.getClass());

    public GitHubClient(GitHubClientProperties gitHubClientProperties,
                        OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
                        @Qualifier("githubRestTemplate") RestTemplate restTemplate) {
        this.gitHubClientProperties = gitHubClientProperties;
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
        this.restTemplate = restTemplate;
    }

    public <T> T executeGithubRequest(HttpMethod httpMethod, HttpEntity<?> entity, Class<T> clazz, String uri, MultiValueMap<String, String> params, String... pathSegments) {
        UriComponents comp = UriComponentsBuilder.fromHttpUrl(uri).queryParams(params)
                .pathSegment(pathSegments).build();
        return Requester.sendRestRequest(restTemplate, comp, httpMethod, entity, clazz);
    }

    protected <T> List<T> executeGithubRequest(HttpMethod httpMethod, HttpEntity<?> entity, ParameterizedTypeReference<List<T>> typeReference, String uri, MultiValueMap<String, String> params, String... pathSegments) {
        UriComponents comp = UriComponentsBuilder.fromHttpUrl(uri).queryParams(params)
                .pathSegment(pathSegments).build();
        return Requester.sendRestRequest(restTemplate, comp, httpMethod, entity, typeReference);
    }
    protected HttpHeaders createHttpHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.ACCEPT, GitHubHttpHeaders.ACCEPT_VALUE);

        if (!StringUtils.isEmpty(gitHubClientProperties.getApiVersion()))
            headers.set(GitHubHttpHeaders.API_VERSION, gitHubClientProperties.getApiVersion());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
            if (clientRegistrationId.equals(RegisteredClients.GITHUB)) {
                OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(
                        clientRegistrationId, oauthToken.getName());
                Optional<OAuth2AccessToken> authToken = Optional.ofNullable(client.getAccessToken());
                authToken.ifPresent(oAuth2AccessToken -> headers.add(HttpHeaders.AUTHORIZATION, ClientUtil.createBearerToken(oAuth2AccessToken.getTokenValue())));
            }
        }
        return headers;
    }
}
