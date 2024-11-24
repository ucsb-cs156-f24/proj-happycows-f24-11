package edu.ucsb.cs156.happiercows.controllers;

import edu.ucsb.cs156.happiercows.ControllerTestCase;
import edu.ucsb.cs156.happiercows.entities.Courses;
import edu.ucsb.cs156.happiercows.repositories.UserRepository;
import edu.ucsb.cs156.happiercows.repositories.CoursesRepository;
import edu.ucsb.cs156.happiercows.testconfig.TestConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.awaitility.Awaitility.await;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import lombok.With;
import lombok.extern.slf4j.Slf4j;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

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
    // @Test
    // public void logged_out_users_cannot_get_by_id() throws Exception {
    // mockMvc.perform(get("/api/courses/get?id=1"))
    // .andExpect(status().is(403)); // logged out users can't get by id
    // }

    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void test_that_admin_can_get_by_id_when_the_id_exists() throws
    // Exception {

    // // arrange
    // when(courseRepository.findById(eq(1L))).thenReturn(Optional.of(course1));

    // // act
    // MvcResult response = mockMvc.perform(get("/api/courses/get?id=1"))
    // .andExpect(status().isOk()).andReturn();

    // // assert
    // verify(courseRepository, times(1)).findById(eq(1L));
    // String expectedJson = mapper.writeValueAsString(course1);
    // String responseString = response.getResponse().getContentAsString();
    // assertEquals(expectedJson, responseString);
    // }

    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void test_that_admin_cannot_get_by_id_when_the_id_does_not_exist()
    // throws Exception {

    // // arrange

    // when(courseRepository.findById(eq(7L))).thenReturn(Optional.empty());

    // // act
    // MvcResult response = mockMvc.perform(get("/api/courses/get?id=7"))
    // .andExpect(status().isNotFound()).andReturn();

    // // assert

    // verify(courseRepository, times(1)).findById(eq(7L));
    // Map<String, Object> json = responseToJson(response);
    // assertEquals("EntityNotFoundException", json.get("type"));
    // assertEquals("Course with id 7 not found", json.get("message"));
    // }

    // @WithMockUser(roles = { "USER" })
    // @Test
    // public void test_that_instructor_can_get_by_id_when_the_id_exists() throws
    // Exception {

    // User currentUser = currentUserService.getCurrentUser().getUser();

    // Staff courseStaff1 = Staff.builder()
    // .id(1L)
    // .courseId(course1.getId())
    // .githubId(currentUser.getGithubId())
    // .user(currentUser)
    // .build();

    // when(courseRepository.findById(eq(course1.getId()))).thenReturn(Optional.of(course1));
    // when(courseStaffRepository.findByCourseIdAndGithubId(eq(course1.getId()),
    // eq(courseStaff1.getGithubId()))).thenReturn(Optional.of(courseStaff1));

    // // act
    // MvcResult response = mockMvc.perform(get("/api/courses/get?id=1"))
    // .andExpect(status().isOk()).andReturn();

    // // assert
    // verify(courseRepository, times(1)).findById(eq(1L));
    // verify(courseStaffRepository,
    // times(1)).findByCourseIdAndGithubId(eq(course1.getId()),
    // eq(courseStaff1.getGithubId()));
    // String expectedJson = mapper.writeValueAsString(course1);
    // String responseString = response.getResponse().getContentAsString();
    // assertEquals(expectedJson, responseString);
    // }

    // @WithMockUser(roles = { "USER" })
    // @Test
    // public void
    // test_that_non_admin_non_instructor_cannot_get_an_existing_course() throws
    // Exception {

    // // arrange
    // User currentUser = currentUserService.getCurrentUser().getUser();

    // when(courseRepository.findById(eq(course1.getId()))).thenReturn(Optional.of(course1));

    // // act
    // mockMvc.perform(get("/api/courses/get?id=1"))
    // .andExpect(status().isForbidden());

    // // assert
    // verify(courseRepository, times(1)).findById(eq(1L));
    // }

    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void an_admin_user_can_post_a_new_course() throws Exception {
    // // arrange

    // Course courseBefore = Course.builder()
    // .name("CS16")
    // .school("UCSB")
    // .term("F23")
    // .startDate(LocalDateTime.parse("2023-09-01T00:00:00"))
    // .endDate(LocalDateTime.parse("2023-12-31T00:00:00"))
    // .githubOrg("ucsb-cs16-f23")
    // .build();

    // Course courseAfter = Course.builder()
    // .id(222L)
    // .name("CS16")
    // .school("UCSB")
    // .term("F23")
    // .startDate(LocalDateTime.parse("2023-09-01T00:00:00"))
    // .endDate(LocalDateTime.parse("2023-12-31T00:00:00"))
    // .githubOrg("ucsb-cs16-f23")
    // .build();

    // when(courseRepository.save(eq(courseBefore))).thenReturn(courseAfter);

    // // act
    // MvcResult response = mockMvc.perform(
    // post("/api/courses/post?name=CS16&school=UCSB&term=F23&startDate=2023-09-01T00:00:00&endDate=2023-12-31T00:00:00&githubOrg=ucsb-cs16-f23")
    // .with(csrf()))
    // .andExpect(status().isOk()).andReturn();

    // // assert
    // verify(courseRepository, times(1)).save(courseBefore);
    // String expectedJson = mapper.writeValueAsString(courseAfter);
    // String responseString = response.getResponse().getContentAsString();
    // assertEquals(expectedJson, responseString);
    // }

    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void an_admin_user_can_add_a_staff_member_to_a_course() throws
    // Exception {
    // // arrange

    // User user =
    // User.builder().githubId(12345).githubLogin("scottpchow23").build();

    // Staff courseStaffBefore = Staff.builder()
    // .courseId(course1.getId())
    // .githubId(user.getGithubId())
    // .user(user)
    // .build();

    // Staff courseStaffAfter = Staff.builder()
    // .id(456L)
    // .courseId(course1.getId())
    // .githubId(user.getGithubId())
    // .user(user)
    // .build();

    // when(courseRepository.findById(eq(course1.getId()))).thenReturn(Optional.of(course1));
    // when(userRepository.findByGithubLogin(eq("scottpchow23"))).thenReturn(Optional.of(user));
    // when(courseStaffRepository.save(eq(courseStaffBefore))).thenReturn(courseStaffAfter);

    // // act
    // MvcResult response = mockMvc.perform(
    // post("/api/courses/addStaff?courseId=1&githubLogin=scottpchow23")
    // .with(csrf()))
    // .andExpect(status().isOk()).andReturn();

    // // assert
    // verify(courseStaffRepository, times(1)).save(courseStaffBefore);
    // String expectedJson = mapper.writeValueAsString(courseStaffAfter);
    // String responseString = response.getResponse().getContentAsString();
    // assertEquals(expectedJson, responseString);
    // }

    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void an_admin_user_cannot_add_staff_to_a_non_existing_course() throws
    // Exception {
    // // arrange

    // when(courseRepository.findById(eq(42L))).thenReturn(Optional.empty());
    // // act

    // MvcResult response = mockMvc.perform(
    // post("/api/courses/addStaff?courseId=42&githubLogin=scottpchow23")
    // .with(csrf()))
    // .andExpect(status().isNotFound()).andReturn();

    // // assert

    // Map<String, String> responseMap =
    // mapper.readValue(response.getResponse().getContentAsString(),
    // new TypeReference<Map<String, String>>() {
    // });
    // Map<String, String> expectedMap = Map.of("message", "Course with id 42 not
    // found", "type",
    // "EntityNotFoundException");
    // assertEquals(expectedMap, responseMap);
    // }

    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void
    // an_admin_user_cannot_add_non_existing_user_to_staff_of_an_existing_course()
    // throws Exception {
    // // arrange

    // when(courseRepository.findById(eq(course1.getId()))).thenReturn(Optional.of(course1));
    // when(userRepository.findByGithubLogin(eq("sadGaucho"))).thenReturn(Optional.empty());

    // // act

    // MvcResult response = mockMvc.perform(
    // post("/api/courses/addStaff?courseId=1&githubLogin=sadGaucho")
    // .with(csrf()))
    // .andExpect(status().isNotFound()).andReturn();

    // // assert

    // Map<String, String> responseMap =
    // mapper.readValue(response.getResponse().getContentAsString(),
    // new TypeReference<Map<String, String>>() {
    // });
    // Map<String, String> expectedMap = Map.of("message", "User with id sadGaucho
    // not found", "type",
    // "EntityNotFoundException");
    // assertEquals(expectedMap, responseMap);
    // }

    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void an_admin_user_can_get_staff_for_a_course() throws Exception {
    // // arrange

    // User user1 =
    // User.builder().githubId(12345).githubLogin("scottpchow23").build();
    // User user2 = User.builder().githubId(67890).githubLogin("pconrad").build();

    // Staff courseStaff1 = Staff.builder()
    // .id(111L)
    // .courseId(course1.getId())
    // .githubId(user1.getGithubId())
    // .user(user1)
    // .build();

    // Staff courseStaff2 = Staff.builder()
    // .id(222L)
    // .courseId(course2.getId())
    // .githubId(user2.getGithubId())
    // .user(user2)
    // .build();

    // ArrayList<Staff> expectedCourseStaff = new ArrayList<>();
    // expectedCourseStaff.addAll(Arrays.asList(courseStaff1, courseStaff2));

    // when(courseRepository.findById(eq(course1.getId()))).thenReturn(Optional.of(course1));
    // when(courseStaffRepository.findByCourseId(eq(course1.getId()))).thenReturn(expectedCourseStaff);

    // // act

    // MvcResult response = mockMvc.perform(
    // get("/api/courses/getStaff?courseId=1")
    // .with(csrf()))
    // .andExpect(status().isOk()).andReturn();

    // // assert

    // verify(courseStaffRepository, times(1)).findByCourseId(eq(course1.getId()));
    // String expectedJson = mapper.writeValueAsString(expectedCourseStaff);
    // String responseString = response.getResponse().getContentAsString();
    // assertEquals(expectedJson, responseString);
    // }

    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void an_admin_user_cannot_get_staff_for_a_non_existing_course() throws
    // Exception {
    // // arrange

    // when(courseRepository.findById(eq(42L))).thenReturn(Optional.empty());
    // // act

    // MvcResult response = mockMvc.perform(
    // get("/api/courses/getStaff?courseId=42")
    // .with(csrf()))
    // .andExpect(status().isNotFound()).andReturn();

    // // assert

    // Map<String, String> responseMap =
    // mapper.readValue(response.getResponse().getContentAsString(),
    // new TypeReference<Map<String, String>>() {
    // });
    // Map<String, String> expectedMap = Map.of("message", "Course with id 42 not
    // found", "type",
    // "EntityNotFoundException");
    // assertEquals(expectedMap, responseMap);
    // }

    // // Tests for DELETE /api/staff?id=...

    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void admin_can_delete_a_staff() throws Exception {
    // // arrange

    // User user1 =
    // User.builder().githubId(12345).githubLogin("scottpchow23").build();
    // Staff staff1 = Staff.builder()
    // .id(15L)
    // .courseId(course1.getId())
    // .githubId(user1.getGithubId())
    // .user(user1)
    // .build();

    // when(courseStaffRepository.findById(eq(15L))).thenReturn(Optional.of(staff1));

    // // act
    // MvcResult response = mockMvc.perform(
    // delete("/api/courses/staff?id=15")
    // .with(csrf()))
    // .andExpect(status().isOk()).andReturn();

    // // assert
    // verify(courseStaffRepository, times(1)).findById(15L);
    // verify(courseStaffRepository, times(1)).delete(any());

    // Map<String, Object> json = responseToJson(response);
    // assertEquals("Staff with id 15 is deleted", json.get("message"));
    // }

    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void
    // admin_tries_to_delete_non_existant_course_staff_and_gets_right_error_message()
    // throws Exception {
    // // arrange

    // when(courseStaffRepository.findById(eq(15L))).thenReturn(Optional.empty());

    // // act
    // MvcResult response = mockMvc.perform(
    // delete("/api/courses/staff?id=15")
    // .with(csrf()))
    // .andExpect(status().isNotFound()).andReturn();

    // // assert
    // verify(courseStaffRepository, times(1)).findById(15L);
    // Map<String, Object> json = responseToJson(response);
    // assertEquals("Staff with id 15 not found", json.get("message"));
    // }

    // // admin cannot update non existing course
    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void an_admin_user_cannot_update_non_existing_course() throws
    // Exception {
    // // arrange

    // when(courseRepository.findById(eq(42L))).thenReturn(Optional.empty());
    // // act

    // MvcResult response = mockMvc.perform(
    // put("/api/courses/update?id=42&name=CS16&school=UCSB&term=F23&startDate=2023-09-01T00:00:00&endDate=2023-12-31T00:00:00&githubOrg=ucsb-cs16-f23")
    // .with(csrf()))
    // .andExpect(status().isNotFound()).andReturn();
    // // assert

    // Map<String, String> responseMap =
    // mapper.readValue(response.getResponse().getContentAsString(),
    // new TypeReference<Map<String, String>>() {
    // });
    // Map<String, String> expectedMap = Map.of("message", "Course with id 42 not
    // found", "type",
    // "EntityNotFoundException");
    // assertEquals(expectedMap, responseMap);
    // }

    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void an_admin_user_can_update_a_course() throws Exception {
    // // arrange

    // Course courseBefore = course1;

    // Course courseAfter = course2;
    // courseAfter.setSchool("UCSD");

    // when(courseRepository.findById(eq(courseBefore.getId()))).thenReturn(Optional.of(courseBefore));
    // when(courseRepository.save(eq(courseAfter))).thenReturn(courseAfter);

    // String urlTemplate = String.format(
    // "/api/courses/update?id=%d&name=%s&school=%s&term=%s&startDate=%s&endDate=%s&githubOrg=%s",
    // courseAfter.getId(), courseAfter.getName(), courseAfter.getSchool(),
    // courseAfter.getTerm(),
    // courseAfter.getStartDate().toString(), courseAfter.getEndDate().toString(),
    // courseAfter.getGithubOrg());
    // MvcResult response = mockMvc.perform(
    // put(urlTemplate)
    // .with(csrf()))
    // .andExpect(status().isOk()).andReturn();

    // // assert
    // verify(courseRepository, times(1)).save(courseBefore);
    // String expectedJson = mapper.writeValueAsString(courseAfter);
    // String responseString = response.getResponse().getContentAsString();
    // assertEquals(expectedJson, responseString);
    // }

    // // Instructor can update course if they are staff
    // @WithMockUser(roles = { "INSTRUCTOR", "USER" })
    // @Test
    // public void an_instructor_user_can_update_a_course_if_they_are_staff() throws
    // Exception {
    // // arrange

    // // get current user, make sure that when courseStaffRepository.findByCourseId
    // is
    // // called, it returns the current user

    // Course courseBefore = course1;

    // Course courseAfter = course2;
    // courseAfter.setSchool("UCSD");

    // when(courseRepository.findById(eq(courseBefore.getId()))).thenReturn(Optional.of(courseBefore));
    // when(courseRepository.save(eq(courseAfter))).thenReturn(courseAfter);

    // // get current user
    // User user = userService.getCurrentUser().getUser();
    // // mock user is staff
    // Staff courseStaff =
    // Staff.builder().courseId(courseBefore.getId()).githubId(user.getGithubId()).build();
    // when(courseStaffRepository.findByCourseIdAndGithubId(courseBefore.getId(),
    // user.getGithubId()))
    // .thenReturn(Optional
    // .of(courseStaff));

    // // act
    // // get urlTemplate from courseAfter using string interpolation
    // String urlTemplate = String.format(
    // "/api/courses/update?id=%d&name=%s&school=%s&term=%s&startDate=%s&endDate=%s&githubOrg=%s",
    // courseAfter.getId(), courseAfter.getName(), courseAfter.getSchool(),
    // courseAfter.getTerm(),
    // courseAfter.getStartDate().toString(), courseAfter.getEndDate().toString(),
    // courseAfter.getGithubOrg());
    // MvcResult response = mockMvc.perform(
    // put(urlTemplate)
    // .with(csrf()))
    // .andExpect(status().isOk()).andReturn();

    // // assert
    // verify(courseRepository, times(1)).save(courseBefore);
    // String expectedJson = mapper.writeValueAsString(courseAfter);
    // String responseString = response.getResponse().getContentAsString();
    // assertEquals(expectedJson, responseString);
    // }

    // // Instructor cannot update course if they are not staff
    // @WithMockUser(roles = { "INSTRUCTOR", "USER" })
    // @Test
    // public void an_instructor_user_cannot_update_a_course_if_they_are_not_staff()
    // throws Exception {
    // // arrange

    // Course courseBefore = Course.builder()
    // .id(1L)
    // .name("CS16")
    // .school("UCSB")
    // .term("F23")
    // .startDate(LocalDateTime.parse("2023-09-01T00:00:00"))
    // .endDate(LocalDateTime.parse("2023-12-31T00:00:00"))
    // .githubOrg("ucsb-cs16-f23")
    // .build();

    // Course courseAfter = Course.builder()
    // .id(1L)
    // .name("CS16")
    // .school("UCSB")
    // .term("F23")
    // .startDate(LocalDateTime.parse("2023-09-01T00:00:00"))
    // .endDate(LocalDateTime.parse("2023-12-31T00:00:00"))
    // .githubOrg("ucsb-cs16-f23")
    // .build();

    // when(courseRepository.findById(eq(courseBefore.getId()))).thenReturn(Optional.of(courseBefore));
    // when(courseRepository.save(eq(courseAfter))).thenReturn(courseAfter);
    // // mock user is not staff

    // User user = userService.getCurrentUser().getUser();
    // Integer githubId = user.getGithubId();
    // ArrayList<Staff> notStaff = new ArrayList<>();
    // notStaff.add(Staff.builder().githubId(githubId - 1).build());
    // when(courseStaffRepository.findByCourseId(courseBefore.getId()))
    // .thenReturn(notStaff);
    // // act
    // MvcResult response = mockMvc.perform(
    // put("/api/courses/update?id=1&name=CS16&school=UCSB&term=F23&startDate=2023-09-01T00:00:00&endDate=2023-12-31T00:00:00&githubOrg=ucsb-cs16-f23")
    // .with(csrf()))
    // .andExpect(status().isForbidden()).andReturn();

    // // assert
    // verify(courseRepository, times(0)).save(courseAfter);

    // // verify message is correct
    // Map<String, String> responseMap =
    // mapper.readValue(response.getResponse().getContentAsString(),
    // new TypeReference<Map<String, String>>() {
    // });
    // Map<String, String> expectedMap = Map.of("message", "User is not a staff
    // member for this course", "type",
    // "AccessDeniedException");
    // assertEquals(expectedMap, responseMap);
    // }

    // // User cannot update course at all
    // @WithMockUser(roles = { "USER" })
    // @Test
    // public void a_user_cannot_update_a_course() throws Exception {
    // // arrange

    // Course courseBefore = Course.builder()
    // .id(1L)
    // .name("CS16")
    // .school("UCSB")
    // .term("F23")
    // .startDate(LocalDateTime.parse("2023-09-01T00:00:00"))
    // .endDate(LocalDateTime.parse("2023-12-31T00:00:00"))
    // .githubOrg("ucsb-cs16-f23")
    // .build();

    // Course courseAfter = Course.builder()
    // .id(1L)
    // .name("CS32")
    // .school("UCSB")
    // .term("F23")
    // .startDate(LocalDateTime.parse("2023-09-01T00:00:00"))
    // .endDate(LocalDateTime.parse("2023-12-31T00:00:00"))
    // .githubOrg("ucsb-cs32-f23")
    // .build();

    // when(courseRepository.findById(eq(courseBefore.getId()))).thenReturn(Optional.of(courseBefore));
    // when(courseRepository.save(eq(courseAfter))).thenReturn(courseAfter);

    // // act
    // MvcResult response = mockMvc.perform(
    // put("/api/courses/update?id=1&name=CS32&school=UCSB&term=F23&startDate=2023-09-01T00:00:00&endDate=2023-12-31T00:00:00&githubOrg=ucsb-cs32-f23")
    // .with(csrf()))
    // .andExpect(status().isForbidden()).andReturn();

    // // assert
    // verify(courseRepository, times(0)).save(courseAfter);

    // // verify message is correct
    // Map<String, String> responseMap =
    // mapper.readValue(response.getResponse().getContentAsString(),
    // new TypeReference<Map<String, String>>() {
    // });
    // Map<String, String> expectedMap = Map.of("message", "Access is denied",
    // "type",
    // "AccessDeniedException");
    // assertEquals(expectedMap, responseMap);
    // }

    // // admin user cannot delete non existing course
    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void an_admin_user_cannot_delete_non_existing_course() throws
    // Exception {
    // // arrange

    // when(courseRepository.findById(eq(42L))).thenReturn(Optional.empty());
    // // act

    // MvcResult response = mockMvc.perform(
    // delete("/api/courses/delete?id=42")
    // .with(csrf()))
    // .andExpect(status().isNotFound()).andReturn();
    // // assert

    // Map<String, String> responseMap =
    // mapper.readValue(response.getResponse().getContentAsString(),
    // new TypeReference<Map<String, String>>() {
    // });
    // Map<String, String> expectedMap = Map.of("message", "Course with id 42 not
    // found", "type",
    // "EntityNotFoundException");
    // assertEquals(expectedMap, responseMap);
    // }

    // // admin user can delete course
    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void an_admin_user_can_delete_a_course() throws Exception {
    // // arrange

    // Course courseBefore = Course.builder()
    // .id(1L)
    // .name("CS16")
    // .school("UCSB")
    // .term("F23")
    // .startDate(LocalDateTime.parse("2023-09-01T00:00:00"))
    // .endDate(LocalDateTime.parse("2023-12-31T00:00:00"))
    // .githubOrg("ucsb-cs16-f23")
    // .build();

    // when(courseRepository.findById(eq(courseBefore.getId()))).thenReturn(Optional.of(courseBefore));

    // // act
    // MvcResult response = mockMvc.perform(
    // delete("/api/courses/delete?id=1")
    // .with(csrf()))
    // .andExpect(status().isOk()).andReturn();

    // // assert
    // verify(courseRepository, times(1)).delete(courseBefore);
    // String expectedJson = mapper.writeValueAsString(courseBefore);
    // String responseString = response.getResponse().getContentAsString();
    // assertEquals(expectedJson, responseString);
    // }

    // // Instructor can delete course if they are staff
    // @WithMockUser(roles = { "INSTRUCTOR", "USER" })
    // @Test
    // public void an_instructor_user_can_delete_a_course_if_they_are_staff() throws
    // Exception {
    // // arrange

    // // get current user, make sure that when courseStaffRepository.findByCourseId
    // is
    // // called, it returns the current user

    // Course courseBefore = Course.builder()
    // .id(1L)
    // .name("CS16")
    // .school("UCSB")
    // .term("F23")
    // .startDate(LocalDateTime.parse("2023-09-01T00:00:00"))
    // .endDate(LocalDateTime.parse("2023-12-31T00:00:00"))
    // .githubOrg("ucsb-cs16-f23")
    // .build();

    // when(courseRepository.findById(eq(courseBefore.getId()))).thenReturn(Optional.of(courseBefore));

    // // get current user
    // User user = userService.getCurrentUser().getUser();
    // // mock user is staff
    // Staff courseStaff =
    // Staff.builder().courseId(courseBefore.getId()).githubId(user.getGithubId()).build();
    // when(courseStaffRepository.findByCourseIdAndGithubId(courseBefore.getId(),
    // user.getGithubId()))
    // .thenReturn(Optional
    // .of(courseStaff));
    // // act
    // MvcResult response = mockMvc.perform(
    // delete("/api/courses/delete?id=1")
    // .with(csrf()))
    // .andExpect(status().isOk()).andReturn();

    // // assert
    // verify(courseRepository, times(1)).delete(courseBefore);
    // String expectedJson = mapper.writeValueAsString(courseBefore);
    // String responseString = response.getResponse().getContentAsString();
    // assertEquals(expectedJson, responseString);
    // }

    // // Instructor cannot delete course if they are not staff
    // @WithMockUser(roles = { "INSTRUCTOR", "USER" })
    // @Test
    // public void an_instructor_user_cannot_delete_a_course_if_they_are_not_staff()
    // throws Exception {
    // // arrange

    // Course courseBefore = Course.builder()
    // .id(1L)
    // .name("CS16")
    // .school("UCSB")
    // .term("F23")
    // .startDate(LocalDateTime.parse("2023-09-01T00:00:00"))
    // .endDate(LocalDateTime.parse("2023-12-31T00:00:00"))
    // .githubOrg("ucsb-cs16-f23")
    // .build();

    // when(courseRepository.findById(eq(courseBefore.getId()))).thenReturn(Optional.of(courseBefore));
    // // mock user is not staff

    // User user = userService.getCurrentUser().getUser();
    // Integer githubId = user.getGithubId();
    // ArrayList<Staff> notStaff = new ArrayList<>();
    // notStaff.add(Staff.builder().githubId(githubId - 1).build());
    // when(courseStaffRepository.findByCourseId(courseBefore.getId()))
    // .thenReturn(notStaff);
    // // act
    // MvcResult response = mockMvc.perform(
    // delete("/api/courses/delete?id=1")
    // .with(csrf()))
    // .andExpect(status().isForbidden()).andReturn();

    // // assert
    // verify(courseRepository, times(0)).delete(courseBefore);

    // // verify message is correct
    // Map<String, String> responseMap =
    // mapper.readValue(response.getResponse().getContentAsString(),
    // new TypeReference<Map<String, String>>() {
    // });
    // Map<String, String> expectedMap = Map.of("message", "User is not a staff
    // member for this course", "type",
    // "AccessDeniedException");
    // assertEquals(expectedMap, responseMap);
    // }

    // // User cannot delete course at all
    // @WithMockUser(roles = { "USER" })
    // @Test
    // public void a_user_cannot_delete_a_course() throws Exception {
    // // arrange

    // Course courseBefore = Course.builder()
    // .id(1L)
    // .name("CS16")
    // .school("UCSB")
    // .term("F23")
    // .startDate(LocalDateTime.parse("2023-09-01T00:00:00"))
    // .endDate(LocalDateTime.parse("2023-12-31T00:00:00"))
    // .githubOrg("ucsb-cs16-f23")
    // .build();

    // when(courseRepository.findById(eq(courseBefore.getId()))).thenReturn(Optional.of(courseBefore));

    // // act
    // MvcResult response = mockMvc.perform(
    // delete("/api/courses/delete?id=1")
    // .with(csrf()))
    // .andExpect(status().isForbidden()).andReturn();

    // // assert
    // verify(courseRepository, times(0)).delete(courseBefore);

    // // verify message is correct
    // Map<String, String> responseMap =
    // mapper.readValue(response.getResponse().getContentAsString(),
    // new TypeReference<Map<String, String>>() {
    // });
    // Map<String, String> expectedMap = Map.of("message", "Access is denied",
    // "type",
    // "AccessDeniedException");
    // assertEquals(expectedMap, responseMap);
    // }
}