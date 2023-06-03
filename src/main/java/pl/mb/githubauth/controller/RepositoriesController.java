package pl.mb.githubauth.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mb.githubauth.model.dto.RepositoryDTO;
import pl.mb.githubauth.service.repos.RepositoryService;

@RestController
@RequestMapping("/repositories")
public class RepositoriesController {

    private final RepositoryService githubApiService;

    public RepositoriesController(RepositoryService githubApiService) {
        this.githubApiService = githubApiService;
    }

    @GetMapping
    public void repositories() {
    }


    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping(value = "/{owner}/{repository-name}", produces = "application/json")
    public ResponseEntity<RepositoryDTO> getRepository(@PathVariable("owner") String owner,
                                                       @PathVariable("repository-name") String repositoryName) {
        return new ResponseEntity<>(githubApiService.getRepository(owner, repositoryName), HttpStatus.OK);
    }
}
