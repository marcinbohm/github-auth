package pl.mb.githubauth.controller.github;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mb.githubauth.model.ghentities.GHRepository;
import pl.mb.githubauth.service.github.user.GitHubUserService;

import java.util.List;

@RestController
@RequestMapping("/github/users")
public class GHUserController {

    private final GitHubUserService gitHubUserService;

    public GHUserController(GitHubUserService gitHubUserService) {
        this.gitHubUserService = gitHubUserService;
    }


    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping(value = "/user/repos", produces = "application/json")
    public ResponseEntity<List<GHRepository>> getGHRepositoryStargazers() {
        return new ResponseEntity<>(gitHubUserService.getLoggedUserRepos(), HttpStatus.OK);
    }
}
