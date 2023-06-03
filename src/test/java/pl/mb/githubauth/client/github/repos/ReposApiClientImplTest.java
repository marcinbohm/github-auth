package pl.mb.githubauth.client.github.repos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.client.RestTemplate;
import pl.mb.githubauth.client.github.GitHubClient;
import pl.mb.githubauth.client.github.GitHubClientProperties;
import pl.mb.githubauth.model.ghentities.GHRepository;
import pl.mb.githubauth.model.ghentities.GHSimpleUser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ReposApiClientImplTest {

    @Mock
    private GitHubClient gitHubClient;

    @Mock
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Mock
    private GitHubClientProperties gitHubClientProperties;

    @InjectMocks
    private ReposApiClientImpl reposApiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getRepo_shouldReturnGHRepository() {
        // Arrange
        RestTemplate restTemplate = new RestTemplate();
        ReposApiClientImpl reposApiClient = new ReposApiClientImpl(gitHubClientProperties, oAuth2AuthorizedClientService, restTemplate);
        String owner = "marcinbohm";
        String repositoryName = "calc_factory";
        GHRepository mockRepository = new GHRepository();

        when(gitHubClient.executeGithubRequest(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(mockRepository);

        // Act
        GHRepository result = reposApiClient.getRepo(owner, repositoryName);

        // Assert
        assertNotNull(result);
    }

    @Test
    void getRepo_withNullOwner_shouldThrowIllegalArgumentException() {
        // Arrange
        String owner = null;
        String repositoryName = "testRepository";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> reposApiClient.getRepo(owner, repositoryName));
    }

    @Test
    void getRepo_withNullRepositoryName_shouldThrowIllegalArgumentException() {
        // Arrange
        String owner = "testOwner";
        String repositoryName = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> reposApiClient.getRepo(owner, repositoryName));
    }

    @Test
    void getRepoStargazers_shouldReturnListGHSimpleUser() {
        // Arrange
        RestTemplate restTemplate = new RestTemplate();
        ReposApiClientImpl reposApiClient = new ReposApiClientImpl(gitHubClientProperties, oAuth2AuthorizedClientService, restTemplate);
        String owner = "marcinbohm";
        String repositoryName = "calc_factory";

        // Mocking the expected behavior of executeGithubRequest() method
        List<GHSimpleUser> mockStargazers = new ArrayList<>();
        when(gitHubClient.executeGithubRequest(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(mockStargazers);

        // Act
        List<GHSimpleUser> result = reposApiClient.getRepoStargazers(owner, repositoryName);

        // Assert
        assertNotNull(result);
        assertEquals(mockStargazers, result);
    }
}