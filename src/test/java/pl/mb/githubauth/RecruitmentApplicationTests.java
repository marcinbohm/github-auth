package pl.mb.githubauth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import pl.mb.githubauth.controller.AuthorizationController;
import pl.mb.githubauth.controller.RepositoriesController;
import pl.mb.githubauth.controller.github.GHRepositoryController;
import pl.mb.githubauth.controller.github.GHUserController;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class RecruitmentApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RepositoriesController repositoriesController;

    @Autowired
    private AuthorizationController authorizationController;

    @Autowired
    private GHUserController ghUserController;

    @Autowired
    private GHRepositoryController ghRepositoryController;


    @Test
    public void contextLoads() {
        assertThat(applicationContext).isNotNull();
        assertThat(repositoriesController).isNotNull();
        assertThat(authorizationController).isNotNull();
        assertThat(ghRepositoryController).isNotNull();
        assertThat(ghUserController).isNotNull();
    }

}
