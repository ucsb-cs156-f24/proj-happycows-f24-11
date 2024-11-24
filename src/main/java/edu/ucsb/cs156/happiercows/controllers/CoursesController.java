package edu.ucsb.cs156.happiercows.controllers;

import edu.ucsb.cs156.happiercows.entities.Courses;

import edu.ucsb.cs156.happiercows.repositories.CoursesRepository;
import edu.ucsb.cs156.happiercows.repositories.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.ucsb.cs156.happiercows.errors.EntityNotFoundException;

@Tag(name = "Courses")
@RequestMapping("/api/courses")
@RestController
@Slf4j
public class CoursesController extends ApiController {

    @Autowired
    CoursesRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Operation(summary = "List all courses")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<Courses> allCourses() {
        Iterable<Courses> courses = courseRepository.findAll();
        return courses;
    }

    @Operation(summary = "Get a single course by id")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/get")
    public Courses getById(
            @Parameter(name = "id") @RequestParam Long id) {

        Courses course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Courses.class, id));

        return course;
    }

    @Operation(summary = "Create a new course")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public Courses postCourse(
            @Parameter(name = "name", description = "course name, e.g. CMPSC 156") @RequestParam String name,
            @Parameter(name = "term", description = "quarter or semester, e.g. F23") @RequestParam String term)
            throws JsonProcessingException {

        Courses course = Courses.builder()
                .name(name)
                .term(term)
                .build();

        Courses savedCourse = courseRepository.save(course);

        return savedCourse;
    }

    @Operation(summary = "Update information for a course")
    // allow for roles of ADMIN
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public Courses updateCourse(
            @Parameter(name = "id") @RequestParam Long id,
            @Parameter(name = "name", description = "course name, e.g. CMPSC 156") @RequestParam String name,
            @Parameter(name = "term", description = "quarter or semester, e.g. F23") @RequestParam String term)
            throws JsonProcessingException {

        Courses course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Courses.class,
                        id.toString()));

        course.setName(name);
        course.setTerm(term);

        course = courseRepository.save(course);
        log.info("course={}", course);

        return course;
    }

    // delete a course if the user is an admin for the course
    @Operation(summary = "Delete a course")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public Courses deleteCourse(
            @Parameter(name = "id") @RequestParam Long id)
            throws JsonProcessingException {

        Courses course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Courses.class, id.toString()));

        courseRepository.delete(course);
        return course;
    }

}