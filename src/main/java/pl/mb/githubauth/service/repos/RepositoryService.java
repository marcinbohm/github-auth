package pl.mb.githubauth.service.repos;

import pl.mb.githubauth.model.dto.RepositoryDTO;

public interface RepositoryService {
    RepositoryDTO getRepository(String owner, String repositoryName);
}
