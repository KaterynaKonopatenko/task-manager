package com.example.taskmanager;

import com.example.taskmanager.model.Project;
import com.example.taskmanager.model.User;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.Comment;
import com.example.taskmanager.repo.CommentRepository;
import com.example.taskmanager.service.CommentService;
import com.example.taskmanager.service.TaskService;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

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

    @Autowired
    private TaskService taskService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

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

    // check that a project with a blank name is rejected by server-side validation and not saved
    @Test
    void creatingProjectWithBlankNameIsRejected() throws Exception {
        User owner = userService.register("test4", "test4@test.com", "password");

        mockMvc.perform(post("/projects/create")
                        .with(user("test4").roles("USER"))
                        .with(csrf())
                        .param("name", "")
                        .param("description", "desc"))
                .andExpect(status().is3xxRedirection());

        assertTrue(projectRepository.findByOwnerId(owner.getId()).isEmpty());
    }

    // check that setting a task status to a value tahat isn't real enum constant
    // return 400 Bad Request (invalid user input) not generic 500 error
    @Test
    void invalidTaskStatusReturnsBadRequest() throws Exception {
        User owner = userService.register("test5", "test5@test.com", "password");

        Project project = new Project();
        project.setName("Test's 5 project");
        project.setOwner(owner);
        project = projectRepository.save(project);

        Task task = taskService.create("Task task", "desc", "MEDIUM", null, project, owner);

        mockMvc.perform(post("/tasks/" + task.getId() + "/status")
                        .with(user("test5").roles("USER"))
                        .with(csrf())
                        .param("status", "NOT_A_REAL_STATUS"))
                .andExpect(status().isBadRequest());
    }

    // check that a user can't delete someone else's comment by sumbitting
    // their own taskId together with a comment id that belongs to a different task
    @Test
    void userCannotDeleteAnotherUsersComment() throws Exception {
        User owner1 = userService.register("onwer1", "owner1@test.com", "password");
        Project project1 = new Project();
        project1.setName("owner1's project");
        project1.setOwner(owner1);
        project1 = projectRepository.save(project1);
        Task task1 = taskService.create("task1", "desc", "MEDIUM", null, project1, owner1);
        Comment comment1 = commentService.add("owner1's comment", task1, owner1);

        User owner2 = userService.register("onwer2", "owner2@test.com", "password");
        Project project2 = new Project();
        project2.setName("owner2's project");
        project2.setOwner(owner2);
        project2 = projectRepository.save(project2);
        Task task2 = taskService.create("task2", "desc", "MEDIUM", null, project2, owner2);

        // owner2 own task2 but tries to delete owner1's comment throught it
        mockMvc.perform(post("/tasks/" + task2.getId() + "/comments/" + comment1.getId() + "/delete")
                        .with(user("owner2").roles("USER"))
                        .with(csrf()))
                .andExpect(status().isNotFound());

        // the comment must still exist untouched
        assertTrue(commentRepository.findById(comment1.getId()).isPresent());
    }
}
