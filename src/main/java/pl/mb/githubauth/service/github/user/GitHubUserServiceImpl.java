package pl.mb.githubauth.service.github.user;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.mb.githubauth.client.github.user.UserApiClient;
import pl.mb.githubauth.model.ghentities.GHRepository;

import java.util.List;

@Service
public class GitHubUserServiceImpl implements GitHubUserService {

    private final UserApiClient userApiClient;

    public GitHubUserServiceImpl(UserApiClient userApiClient) {
        this.userApiClient = userApiClient;
    }


    @Override
    @CircuitBreaker(name = "defaultCircuitBreaker", fallbackMethod = "fallback")
    @Bulkhead(name = "defaultBulkhead")
    public List<GHRepository> getLoggedUserRepos() {
        return userApiClient.getLoggedUserRepos();
    }

    private void fallback(Exception ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }
}
