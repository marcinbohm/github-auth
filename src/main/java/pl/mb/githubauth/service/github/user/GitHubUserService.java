package pl.mb.githubauth.service.github.user;

import pl.mb.githubauth.model.ghentities.GHRepository;

import java.util.List;

public interface GitHubUserService {

    List<GHRepository> getLoggedUserRepos();
}
