package edu.ucsb.cs156.happiercows.controllers;


import edu.ucsb.cs156.happiercows.testconfig.TestConfig;

import org.springframework.context.annotation.Import;

import edu.ucsb.cs156.happiercows.entities.Student;
import edu.ucsb.cs156.happiercows.entities.Courses;
import edu.ucsb.cs156.happiercows.repositories.StudentRepository;
import edu.ucsb.cs156.happiercows.repositories.UserRepository;
import edu.ucsb.cs156.happiercows.repositories.CoursesRepository;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.ucsb.cs156.happiercows.ControllerTestCase;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentController.class)
@Import(TestConfig.class)
@AutoConfigureDataJpa
public class StudentControllerTests extends ControllerTestCase {

    @MockBean
    StudentRepository studentRepository;  // Mocking the StudentRepository

    @MockBean
    UserRepository userRepository;

    @MockBean
    CoursesRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void admin_can_delete_a_student() throws Exception {
            // arrange

            Student student = new Student();
            student.setId(1L);
            student.setCourseId(1L);
            student.setFname("John");
            student.setLname("Doe");
            student.setStudentId("12345");
            student.setEmail("8TbGZ@example.com");

            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

            // act
            MvcResult response = mockMvc.perform(
                            delete("/api/students?id=1")
                                            .with(csrf()))
                            .andExpect(status().isOk()).andReturn();

            // assert
            verify(studentRepository, times(1)).findById(1L);
            verify(studentRepository, times(1)).delete(any());

            Map<String, Object> json = responseToJson(response);
            assertEquals("Student with id 1 deleted", json.get("message"));
    }
    

    
    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void admin_cannot_delete_non_existant_student()
                    throws Exception {
            // arrange

            when(studentRepository.findById(1L)).thenReturn(Optional.empty());
            mockMvc.perform(delete("/api/students?id=1").with(csrf()))
                .andExpect(status().isNotFound());
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void admin_users_can_get_by_course_id() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setCourseId(1L);
        student1.setFname("John");
        student1.setLname("Doe");
        student1.setStudentId("12345");
        student1.setEmail("8TbGZ@example.com");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setCourseId(1L);
        student2.setFname("Jane");
        student2.setLname("Doe");
        student2.setStudentId("54321");
        student2.setEmail("0yNt9@example.com");

        when(studentRepository.findAllByCourseId(1L)).thenReturn(java.util.Arrays.asList(student1, student2));
        mockMvc.perform(get("/api/students/course?courseId=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].courseId").value(1))
                .andExpect(jsonPath("$[0].fname").value("John"))
                .andExpect(jsonPath("$[0].lname").value("Doe"))
                .andExpect(jsonPath("$[0].studentId").value("12345"))
                .andExpect(jsonPath("$[0].email").value("8TbGZ@example.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].courseId").value(1))
                .andExpect(jsonPath("$[1].fname").value("Jane"))
                .andExpect(jsonPath("$[1].lname").value("Doe"))
                .andExpect(jsonPath("$[1].studentId").value("54321"))
                .andExpect(jsonPath("$[1].email").value("0yNt9@example.com"));
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void admin_users_can_post_student() throws Exception {
        Student student = new Student();
        student.setId(0L);
        student.setCourseId(1L);
        student.setFname("John");
        student.setLname("Doe");
        student.setStudentId("12345");
        student.setEmail("8TbGZ@example.com");

        Courses course = new Courses();
        course.setId(1L);

        when(studentRepository.save(student)).thenReturn(student);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        
        MvcResult response = mockMvc.perform(post("/api/students")
            .param("courseId", "1")
            .param("fname", "John")
            .param("lname", "Doe")
            .param("studentId", "12345")
            .param("email", "8TbGZ@example.com")
            .with(csrf()))
            .andExpect(status().isOk())
            .andReturn();

        verify(studentRepository, times(1)).save(student);

        Map<String, Object> json = responseToJson(response);
        assertEquals("John", json.get("fname"));
        assertEquals("Doe", json.get("lname"));
        assertEquals("12345", json.get("studentId"));
        assertEquals("8TbGZ@example.com", json.get("email"));
        assertEquals(1, (Integer)json.get("courseId"));   
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void post_throws_entity_not_found_exception_when_courseid_does_not_exist() throws Exception {
        Student student = new Student();
        student.setId(0L);
        student.setCourseId(1L);
        student.setFname("John");
        student.setLname("Doe");
        student.setStudentId("12345");
        student.setEmail("8TbGZ@example.com");

        when(studentRepository.save(student)).thenReturn(student);
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/students")
            .param("courseId", "1")
            .param("fname", "John")
            .param("lname", "Doe")  
            .param("studentId", "12345")
            .param("email", "8TbGZ@example.com")
            .with(csrf()))
            .andExpect(status().isNotFound());
    }

}
