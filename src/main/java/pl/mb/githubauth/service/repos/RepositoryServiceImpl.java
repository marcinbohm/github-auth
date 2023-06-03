package pl.mb.githubauth.service.repos;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import pl.mb.githubauth.client.github.GitHubClient;
import pl.mb.githubauth.client.github.GitHubClientProperties;
import pl.mb.githubauth.client.github.repos.ReposApiClient;
import pl.mb.githubauth.model.dto.RepositoryDTO;
import pl.mb.githubauth.model.ghentities.GHRepository;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class RepositoryServiceImpl extends GitHubClient implements RepositoryService {

    private final ReposApiClient reposApiClient;

    public RepositoryServiceImpl(GitHubClientProperties gitHubClientProperties,
                                 OAuth2AuthorizedClientService oAuth2AuthorizedClient,
                                 ReposApiClient reposApiClient,
                                 RestTemplate restTemplate) {
        super(gitHubClientProperties, oAuth2AuthorizedClient, restTemplate);
        this.reposApiClient = reposApiClient;
    }

    @Override
    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "fallback")
    @Bulkhead(name = "defaultBulkhead")
    public RepositoryDTO getRepository(String owner, String repositoryName) {
        if (owner == null || repositoryName == null)
            throw new IllegalArgumentException();
        GHRepository ghRepository = reposApiClient.getRepo(owner, repositoryName);
        return Optional.ofNullable(repositoryMapper(ghRepository))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private RepositoryDTO repositoryMapper(GHRepository ghRepository) {
        if (ghRepository != null) {
            RepositoryDTO repositoryDTO = new RepositoryDTO();

            if (ghRepository.getFull_name() != null)
                repositoryDTO.setFullName(ghRepository.getFull_name());

            if (ghRepository.getDescription() != null)
                repositoryDTO.setDescription(ghRepository.getDescription());

            if (ghRepository.getClone_url() != null)
                repositoryDTO.setCloneUrl(ghRepository.getClone_url());

            if (ghRepository.getCreated_at() != null)
                repositoryDTO.setCreatedAt(ghRepository.getCreated_at().format(DateTimeFormatter.ISO_DATE_TIME));

            repositoryDTO.setStars(ghRepository.getStargazers_count());
            return repositoryDTO;
        }

        return null;
    }

    private void fallback(Exception ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }
}
