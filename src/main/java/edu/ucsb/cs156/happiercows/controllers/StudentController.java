package edu.ucsb.cs156.happiercows.controllers;

import edu.ucsb.cs156.happiercows.entities.Student;
import edu.ucsb.cs156.happiercows.entities.Courses;
import edu.ucsb.cs156.happiercows.errors.EntityNotFoundException;
import edu.ucsb.cs156.happiercows.repositories.CoursesRepository;
import edu.ucsb.cs156.happiercows.repositories.StudentRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Students")
@RequestMapping("/api/students")
@RestController
@Slf4j
public class StudentController extends ApiController {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CoursesRepository coursesRepository;

    @Operation(summary = "Get student by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public Student getById(@RequestParam Long id) throws EntityNotFoundException
    {
        Student student = studentRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Student.class, id));
        return student;
    }



    @Operation(summary = "Get all students by course id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/course")
    public Iterable<Student> getByCourseId(@RequestParam Long courseId) throws EntityNotFoundException 
    {
        Iterable<Student> students = studentRepository.findAllByCourseId(courseId);
        return students;
    }



    @Operation(summary= "Delete a student")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("")
    public Object deleteArticle(
            @RequestParam Long id) throws EntityNotFoundException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Student.class, id));

        studentRepository.delete(student);
        return genericMessage("Student with id %s deleted".formatted(id));
    }


    @Operation(summary = "Post a new student")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public Student postStudent(
        @Parameter(name = "courseId") @RequestParam Long courseId,
        @Parameter(name = "fname") @RequestParam String fname,
        @Parameter(name = "lname") @RequestParam String lname,
        @Parameter(name = "studentId") @RequestParam String studentId,
        @Parameter(name = "email") @RequestParam String email
    ) {

        coursesRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException(Courses.class, courseId));

        Student student = new Student();
        student.setCourseId(courseId);
        student.setFname(fname);
        student.setLname(lname);
        student.setStudentId(studentId);
        student.setEmail(email);
        
        return studentRepository.save(student);

    }
}

