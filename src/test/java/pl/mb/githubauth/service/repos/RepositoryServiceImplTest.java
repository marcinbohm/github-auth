package pl.mb.githubauth.service.repos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import pl.mb.githubauth.client.github.GitHubClientProperties;
import pl.mb.githubauth.client.github.repos.ReposApiClient;
import pl.mb.githubauth.model.dto.RepositoryDTO;
import pl.mb.githubauth.model.ghentities.GHRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RepositoryServiceImplTest {

    @Mock
    OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Mock
    private ReposApiClient reposApiClient;

    @Mock
    private GitHubClientProperties gitHubClientProperties;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RepositoryServiceImpl repositoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        repositoryService = new RepositoryServiceImpl(gitHubClientProperties, oAuth2AuthorizedClientService, reposApiClient, restTemplate);
    }

    @Test
    void getRepository_shouldReturnRepositoryDTO() {
        // Given
        String owner = "testOwner";
        String repositoryName = "testRepository";
        GHRepository mockGHRepository = mock(GHRepository.class);

        // When
        when(reposApiClient.getRepo(owner, repositoryName)).thenReturn(mockGHRepository);

        // Act
        RepositoryDTO result = repositoryService.getRepository(owner, repositoryName);

        // Then
        assertNotNull(result);
    }

    @Test
    void getRepository_withNullOwner_shouldThrowIllegalArgumentException() {
        // Given
        String owner = null;
        String repositoryName = "testRepository";
        GHRepository mockGHRepository = mock(GHRepository.class);

        // When
        when(reposApiClient.getRepo(owner, repositoryName)).thenReturn(mockGHRepository);

        // Act & Then
        assertThrows(IllegalArgumentException.class, () -> {
            repositoryService.getRepository(owner, repositoryName);
        });
    }

    @Test
    void getRepository_withNullRepositoryName_shouldThrowIllegalArgumentException() {
        // Given
        String owner = "testOwner";
        String repositoryName = null;
        GHRepository mockGHRepository = mock(GHRepository.class);

        // When
        when(reposApiClient.getRepo(owner, repositoryName)).thenReturn(mockGHRepository);

        // Act & Then
        assertThrows(IllegalArgumentException.class, () -> {
            repositoryService.getRepository(owner, repositoryName);
        });
    }

    @Test
    void getRepository_withNonexistentRepository_shouldReturnResponseStatusException() {
        // Given
        String owner = "testOwner";
        String repositoryName = "nonexistentRepository";

        // When
        when(reposApiClient.getRepo(owner, repositoryName)).thenReturn(null);

        // Act & Then
        assertThrows(ResponseStatusException.class, () -> {
            repositoryService.getRepository(owner, repositoryName);
        });
    }
}


