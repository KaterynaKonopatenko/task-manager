package com.example.taskmanager;

import com.example.taskmanager.model.Project;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repo.ProjectRepository;
import com.example.taskmanager.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthorizationAndSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectRepository projectRepository;

    // check that one user can't open another user's project just by guessing its ID(IDOR)
    @Test
    void userCannotOpenAnotherProject() throws Exception {
        User test = userService.register("test", "test@test.com", "password");
        userService.register("test2", "test2@test.com", "password");

        Project project = new Project();
        project.setName("test's secret project");
        project.setOwner(test);
        project = projectRepository.save(project);

        mockMvc.perform(get("/projects/" + project.getId())
                        .with(user("test2").roles("USER")))
                .andExpect(status().isNotFound());
    }

    // check that POST request without a CSRF token is rejected(this is what we fixed earline)
    @Test
    void createProjectWithoutCsrfTokenIsRejected() throws Exception {
        userService.register("test3", "test3@test.com", "password");

        mockMvc.perform(post("/projects/create")
                        .with(user("test3").roles("USER"))
                        .param("name", "Test project")
                        .param("description", "desc"))
                .andExpect(status().isForbidden());
    }
}
