package pl.mb.githubauth.client.github.repos;

import pl.mb.githubauth.model.ghentities.GHRepository;
import pl.mb.githubauth.model.ghentities.GHSimpleUser;

import java.util.List;

public interface ReposApiClient {
    GHRepository getRepo(String owner, String repositoryName);
    List<GHSimpleUser> getRepoStargazers(String owner, String repositoryName);
}
