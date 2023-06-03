package pl.mb.githubauth.controller.github;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mb.githubauth.model.ghentities.GHRepository;
import pl.mb.githubauth.model.ghentities.GHSimpleUser;
import pl.mb.githubauth.service.github.repo.GitHubRepositoryService;

import java.util.List;

@RestController
@RequestMapping("/github/repositories")
public class GHRepositoryController {

    private final GitHubRepositoryService gitHubRepositoryService;

    public GHRepositoryController(GitHubRepositoryService gitHubRepositoryService) {
        this.gitHubRepositoryService = gitHubRepositoryService;
    }


    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping(value = "/{owner}/{repository-name}", produces = "application/json")
    public ResponseEntity<GHRepository> getGHRepository(@PathVariable("owner") String owner,
                                        @PathVariable("repository-name") String repositoryName) {
        return new ResponseEntity<>(gitHubRepositoryService.getGHRepository(owner, repositoryName), HttpStatus.OK);
    }


    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping(value = "/{owner}/{repository-name}/stargazers", produces = "application/json")
    public ResponseEntity<List<GHSimpleUser>> getGHRepositoryStargazers(@PathVariable("owner") String owner,
                                                                       @PathVariable("repository-name") String repositoryName) {
        return new ResponseEntity<>(gitHubRepositoryService.getGHRepositoryStargazers(owner, repositoryName), HttpStatus.OK);
    }
}
