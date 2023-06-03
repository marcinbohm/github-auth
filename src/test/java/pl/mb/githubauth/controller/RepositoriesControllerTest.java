package pl.mb.githubauth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;
import pl.mb.githubauth.client.github.GitHubClient;
import pl.mb.githubauth.model.dto.RepositoryDTO;
import pl.mb.githubauth.service.repos.RepositoryService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RepositoriesControllerTest {

    @Mock
    private GitHubClient gitHubClient;

    @Mock
    private RepositoryService repositoryService;

    private RepositoriesController repositoriesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        repositoriesController = new RepositoriesController(repositoryService);
    }

    @Test
    void getRepository_shouldReturnRepositoryDTO() {
        // Given
        String owner = "testOwner";
        String repositoryName = "testRepository";
        RepositoryDTO mockRepositoryDTO = new RepositoryDTO();

        // When
        when(repositoryService.getRepository(owner, repositoryName)).thenReturn(mockRepositoryDTO);

        // Act
        ResponseEntity<RepositoryDTO> response = repositoriesController.getRepository(owner, repositoryName);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRepositoryDTO, response.getBody());
    }

    @Test
    public void repositories_ReturnsOkStatus() throws Exception {
        // Arrange
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(repositoriesController).build();

        // Act & Assert
        mockMvc.perform(get("/repositories"))
                .andExpect(status().isOk());
    }

    @Test
    public void getRepository_ReturnsRepositoryDTO() throws Exception {
        // Arrange
        String owner = "testOwner";
        String repositoryName = "testRepository";
        RepositoryDTO expectedRepositoryDTO = new RepositoryDTO();
        expectedRepositoryDTO.setFullName("Test Repository");
        expectedRepositoryDTO.setDescription("This is a test repository");

        when(repositoryService.getRepository(owner, repositoryName)).thenReturn(expectedRepositoryDTO);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(repositoriesController).build();

        // Act & Assert
        mockMvc.perform(get("/repositories/{owner}/{repository-name}", owner, repositoryName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value(expectedRepositoryDTO.getFullName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedRepositoryDTO.getDescription()));
    }

    @Test
    public void getRepository_ReturnsNotFoundStatus_WhenRepositoryNotFound() throws Exception {
        // Arrange
        String owner = "notExistingOwner";
        String repositoryName = "notExistingRepo";

        when(repositoryService.getRepository(owner, repositoryName)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(repositoriesController).build();

        // Act & Assert
        mockMvc.perform(get("/repositories/{owner}/{repository-name}", owner, repositoryName))
                .andExpect(status().isNotFound());
    }

}