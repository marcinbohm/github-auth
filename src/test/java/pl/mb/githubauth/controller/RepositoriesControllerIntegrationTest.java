package pl.mb.githubauth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.mb.githubauth.controller.RepositoriesController;
import pl.mb.githubauth.model.dto.RepositoryDTO;
import pl.mb.githubauth.service.repos.RepositoryService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RepositoriesController.class)
public class RepositoriesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepositoryService repositoryService;

    @Test
    public void getRepository_ReturnsRepositoryDTO() throws Exception {
        // Given
        String owner = "testOwner";
        String repositoryName = "testRepository";
        RepositoryDTO expectedRepositoryDTO = new RepositoryDTO();
        expectedRepositoryDTO.setFullName("Test Repository");
        expectedRepositoryDTO.setDescription("This is a test repository");

        given(repositoryService.getRepository(owner, repositoryName)).willReturn(expectedRepositoryDTO);

        // When
        MvcResult result = mockMvc.perform(get("/repositories/{owner}/{repository-name}", owner, repositoryName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String responseContent = result.getResponse().getContentAsString();
        RepositoryDTO actualRepositoryDTO = new ObjectMapper().readValue(responseContent, RepositoryDTO.class);

        assertEquals(expectedRepositoryDTO.getFullName(), actualRepositoryDTO.getFullName());
        assertEquals(expectedRepositoryDTO.getDescription(), actualRepositoryDTO.getDescription());

        verify(repositoryService, times(1)).getRepository(owner, repositoryName);
    }
}