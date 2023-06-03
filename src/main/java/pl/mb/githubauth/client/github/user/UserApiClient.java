package pl.mb.githubauth.client.github.user;

import pl.mb.githubauth.model.ghentities.GHRepository;

import java.util.List;

public interface UserApiClient {
    List<GHRepository> getLoggedUserRepos();
}
