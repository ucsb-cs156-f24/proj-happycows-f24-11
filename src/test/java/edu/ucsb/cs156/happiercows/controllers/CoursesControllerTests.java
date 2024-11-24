package edu.ucsb.cs156.happiercows.controllers;

import edu.ucsb.cs156.happiercows.ControllerTestCase;
import edu.ucsb.cs156.happiercows.entities.Courses;
import edu.ucsb.cs156.happiercows.repositories.UserRepository;
import edu.ucsb.cs156.happiercows.repositories.CoursesRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(controllers = CoursesController.class)
@AutoConfigureDataJpa
public class CoursesControllerTests extends ControllerTestCase {

        @MockBean
        UserRepository userRepository;

        @MockBean
        CoursesRepository coursesRepository;

        @Autowired
        ObjectMapper objectMapper;

        // courses/all tests
        @Test
        public void logged_out_users_cannot_get_all() throws Exception {
                mockMvc.perform(get("/api/courses/all"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_users_can_get_all() throws Exception {
                mockMvc.perform(get("/api/courses/all"))
                                .andExpect(status().is(200));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_user_can_get_all_courses() throws Exception {

                Courses course1 = Courses.builder()
                                .id(1L)
                                .name("CS156")
                                .term("F23")
                                .build();

                Courses course2 = Courses.builder()
                                .id(1L)
                                .name("CS148")
                                .term("S24")
                                .build();

                // arrange

                ArrayList<Courses> expectedCourses = new ArrayList<>();
                expectedCourses.addAll(Arrays.asList(course1, course2));

                when(coursesRepository.findAll()).thenReturn(expectedCourses);

                // act
                MvcResult response = mockMvc.perform(get("/api/courses/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(coursesRepository, atLeastOnce()).findAll();
                String expectedJson = mapper.writeValueAsString(expectedCourses);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        // courses/post tests

        @Test
        public void logged_out_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/courses/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_regular_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/courses/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_can_post_a_new_course() throws Exception {
                // arrange

                Courses courseBefore = Courses.builder()
                                .name("CS16")
                                .term("F23")
                                .build();

                Courses courseAfter = Courses.builder()
                                .id(222L)
                                .name("CS16")
                                .term("F23")
                                .build();

                when(coursesRepository.save(eq(courseBefore))).thenReturn(courseAfter);

                // act
                MvcResult response = mockMvc.perform(
                                post("/api/courses/post?name=CS16&school=UCSB&term=F23&startDate=2023-09-01T00:00:00&endDate=2023-12-31T00:00:00&githubOrg=ucsb-cs16-f23")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(coursesRepository, times(1)).save(courseBefore);
                String expectedJson = mapper.writeValueAsString(courseAfter);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        // Tests for GET /api/courses/get?id=...
        @Test
        public void logged_out_users_cannot_get_by_id() throws Exception {
                mockMvc.perform(get("/api/courses/get?id=1"))
                                .andExpect(status().is(403)); // logged out users can't get by id
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void test_that_admin_can_get_by_id_when_the_id_exists() throws Exception {

                Courses course1 = Courses.builder()
                                .name("CS16")
                                .term("F23")
                                .build();

                // arrange
                when(coursesRepository.findById(eq(1L))).thenReturn(Optional.of(course1));

                // act
                MvcResult response = mockMvc.perform(get("/api/courses/get?id=1"))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(coursesRepository, times(1)).findById(eq(1L));
                String expectedJson = mapper.writeValueAsString(course1);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void test_that_admin_cannot_get_by_id_when_the_id_does_not_exist()
                        throws Exception {

                // arrange

                when(coursesRepository.findById(eq(7L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(get("/api/courses/get?id=7"))
                                .andExpect(status().isNotFound()).andReturn();

                // assert

                verify(coursesRepository, times(1)).findById(eq(7L));
                Map<String, Object> json = responseToJson(response);
                assertEquals("EntityNotFoundException", json.get("type"));
                assertEquals("Courses with id 7 not found", json.get("message"));
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_can_post_a_new_course() throws Exception {
                // arrange

                Courses courseBefore = Courses.builder()
                                .name("CS16")
                                .term("F23")
                                .build();

                Courses courseAfter = Courses.builder()
                                .id(222L)
                                .name("CS16")
                                .term("F23")
                                .build();

                when(coursesRepository.save(eq(courseBefore))).thenReturn(courseAfter);

                // act
                MvcResult response = mockMvc.perform(
                                post("/api/courses/post?name=CS16&term=F23")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(coursesRepository, times(1)).save(courseBefore);
                String expectedJson = mapper.writeValueAsString(courseAfter);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        // admin cannot update non existing course
        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_cannot_update_non_existing_course() throws Exception {
                // arrange

                when(coursesRepository.findById(eq(42L))).thenReturn(Optional.empty());
                // act

                MvcResult response = mockMvc.perform(
                                put("/api/courses/update?id=42&name=CS16&term=F23")
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();
                // assert

                Map<String, String> responseMap = mapper.readValue(response.getResponse().getContentAsString(),
                                new TypeReference<Map<String, String>>() {
                                });
                Map<String, String> expectedMap = Map.of("message", "Courses with id 42 not found", "type",
                                "EntityNotFoundException");
                assertEquals(expectedMap, responseMap);
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_can_update_a_course() throws Exception {

                Courses course1 = Courses.builder()
                                .name("CS16")
                                .term("F23")
                                .build();

                Courses course2 = Courses.builder()
                                .name("CS16")
                                .term("F23")
                                .build();
                // arrange

                Courses courseBefore = course1;

                Courses courseAfter = course2;
                courseAfter.setName("CS110");

                when(coursesRepository.findById(eq(courseBefore.getId()))).thenReturn(Optional.of(courseBefore));
                when(coursesRepository.save(eq(courseAfter))).thenReturn(courseAfter);

                String urlTemplate = String.format(
                                "/api/courses/update?id=%d&name=%s&term=%s",
                                courseAfter.getId(), courseAfter.getName(),
                                courseAfter.getTerm());
                MvcResult response = mockMvc.perform(
                                put(urlTemplate)
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(coursesRepository, times(1)).save(courseBefore);
                String expectedJson = mapper.writeValueAsString(courseAfter);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        // User cannot update course at all
        @WithMockUser(roles = { "USER" })
        @Test
        public void a_user_cannot_update_a_course() throws Exception {
                // No arrangement needed since access is denied before controller logic

                // act
                mockMvc.perform(
                                put("/api/courses/update?id=1&name=CS32&term=F23")
                                                .with(csrf()))
                                .andExpect(status().isForbidden());

                // assert
                verifyNoInteractions(coursesRepository);
        }

        // admin user cannot delete non existing course
        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_cannot_delete_non_existing_course() throws Exception {
                // arrange

                when(coursesRepository.findById(eq(42L))).thenReturn(Optional.empty());
                // act

                MvcResult response = mockMvc.perform(
                                delete("/api/courses/delete?id=42")
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();
                // assert

                Map<String, String> responseMap = mapper.readValue(response.getResponse().getContentAsString(),
                                new TypeReference<Map<String, String>>() {
                                });
                Map<String, String> expectedMap = Map.of("message", "Courses with id 42 not found", "type",
                                "EntityNotFoundException");
                assertEquals(expectedMap, responseMap);
        }

        // admin user can delete course
        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_can_delete_a_course() throws Exception {
                // arrange

                Courses courseBefore = Courses.builder()
                                .id(1L)
                                .name("CS16")
                                .term("F23")
                                .build();

                when(coursesRepository.findById(eq(courseBefore.getId()))).thenReturn(Optional.of(courseBefore));

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/courses/delete?id=1")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(coursesRepository, times(1)).delete(courseBefore);
                String expectedJson = mapper.writeValueAsString(courseBefore);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        // User cannot delete course at all
        @WithMockUser(roles = { "USER" })
        @Test
        public void a_user_cannot_delete_a_course() throws Exception {
                // act
                mockMvc.perform(
                                delete("/api/courses/delete?id=1")
                                                .with(csrf()))
                                .andExpect(status().isForbidden());

                // assert
                verifyNoInteractions(coursesRepository);
        }
}