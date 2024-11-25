package edu.ucsb.cs156.happiercows.controllers;


import edu.ucsb.cs156.happiercows.testconfig.TestConfig;
import lombok.With;

import org.springframework.context.annotation.Import;

import edu.ucsb.cs156.happiercows.entities.Student;
import edu.ucsb.cs156.happiercows.errors.EntityNotFoundException;
import edu.ucsb.cs156.happiercows.repositories.StudentRepository;
import edu.ucsb.cs156.happiercows.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.ucsb.cs156.happiercows.ControllerTestCase;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = StudentController.class)
@Import(TestConfig.class)
@AutoConfigureDataJpa
public class StudentControllerTests extends ControllerTestCase {

    @MockBean
    StudentRepository studentRepository;  // Mocking the StudentRepository

    @MockBean
    UserRepository userRepository;

    @WithMockUser(roles = { "USER" })
    @Test
    public void non_admin_users_cannot_get_by_id() throws Exception {
        mockMvc.perform(get("/api/students?id=1"))
                .andExpect(status().is(403));
    }
    
    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void admin_users_can_get_by_id() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setCourseId(1L);
        student.setFname("John");
        student.setLname("Doe");
        student.setStudentId("12345");
        student.setEmail("8TbGZ@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        mockMvc.perform(get("/api/students?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.fname").value("John"))
                .andExpect(jsonPath("$.lname").value("Doe"))
                .andExpect(jsonPath("$.studentId").value("12345"))
                .andExpect(jsonPath("$.email").value("8TbGZ@example.com"));
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void admin_users_cannot_get_by_id_that_does_not_exist() throws Exception {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/students?id=1"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = { "ADMIN", "USER" })
    @Test
    public void admin_can_edit_an_existing_student() throws Exception {
        // arrange


        Student student = Student.builder()
            .courseId(1L)
            .fname("John")
            .lname("Doe")
            .studentId("12345")
            .email("8TbGZ@example.com")
            .build();

        Student editedStudent = Student.builder()
            .courseId(2L)
            .fname("Bob")
            .lname("Builder")
            .studentId("67890")
            .email("hehe@example.com")
            .build();

        String requestBody = mapper.writeValueAsString(editedStudent);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // act
        MvcResult response = mockMvc.perform(
                        put("/api/students?id=1")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8")
                                        .content(requestBody)
                                        .with(csrf()))
                        .andExpect(status().isOk()).andReturn();

        // assert
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(editedStudent); // should be saved with correct user
        String responseString = response.getResponse().getContentAsString();
        assertEquals(requestBody, responseString);
    }

    @WithMockUser(roles = { "ADMIN", "USER" })
    @Test
    public void admin_cannot_edit_student_that_does_not_exist() throws Exception {
            
        Student editedStudent = new Student();
        editedStudent.setId(1L);
        editedStudent.setCourseId(1L);
        editedStudent.setFname("John");
        editedStudent.setLname("Doe");
        editedStudent.setStudentId("12345");
        editedStudent.setEmail("8TbGZ@example.com");


        String requestBody = mapper.writeValueAsString(editedStudent);

        when(studentRepository.findById(67L)).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                        put("/api/students?id=67")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8")
                                        .content(requestBody)
                                        .with(csrf()))
                        .andExpect(status().isNotFound()).andReturn();

        // assert
        verify(studentRepository, times(1)).findById(67L);
        Map<String, Object> json = responseToJson(response);
        assertEquals("Student with id 67 not found", json.get("message"));

    }
}