package pl.mb.githubauth.service.github.repo;

import pl.mb.githubauth.model.ghentities.GHRepository;
import pl.mb.githubauth.model.ghentities.GHSimpleUser;

import java.util.List;

public interface GitHubRepositoryService {

    GHRepository getGHRepository(String owner, String repositoryName);

    List<GHSimpleUser> getGHRepositoryStargazers(String owner, String repositoryName);
}
