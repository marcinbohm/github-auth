package pl.mb.githubauth.service.github.repo;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.mb.githubauth.client.github.repos.ReposApiClient;
import pl.mb.githubauth.model.ghentities.GHRepository;
import pl.mb.githubauth.model.ghentities.GHSimpleUser;

import java.util.List;

@Service
public class GitHubRepositoryServiceImpl implements GitHubRepositoryService {

    private final ReposApiClient reposApiClient;

    public GitHubRepositoryServiceImpl(ReposApiClient reposApiClient) {
        this.reposApiClient = reposApiClient;
    }

    @Override
    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "fallback")
    @Bulkhead(name = "defaultBulkhead")
    public GHRepository getGHRepository(String owner, String repositoryName) {
        if (owner == null || repositoryName == null)
            throw new IllegalArgumentException();
        return reposApiClient.getRepo(owner, repositoryName);
    }

    @Override
    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "fallback")
    @Bulkhead(name = "defaultBulkhead")
    public List<GHSimpleUser> getGHRepositoryStargazers(String owner, String repositoryName) {
        if (owner == null || repositoryName == null)
            throw new IllegalArgumentException();
        return reposApiClient.getRepoStargazers(owner, repositoryName);
    }

    private void fallback(Exception ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }
}
