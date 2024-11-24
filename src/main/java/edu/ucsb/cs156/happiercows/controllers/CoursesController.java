package edu.ucsb.cs156.happiercows.controllers;

import edu.ucsb.cs156.happiercows.entities.Courses;
import edu.ucsb.cs156.happiercows.entities.User;

import edu.ucsb.cs156.happiercows.repositories.CoursesRepository;
import edu.ucsb.cs156.happiercows.repositories.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import edu.ucsb.cs156.happiercows.errors.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.Optional;
import java.util.Collections;

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

    // @Operation(summary = "Get a single course by id")
    // @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    // @GetMapping("/get")
    // public Course getById(
    // @Parameter(name = "id") @RequestParam Long id) {
    // User u = getCurrentUser().getUser();

    // Course course = courseRepository.findById(id)
    // .orElseThrow(() -> new EntityNotFoundException(Course.class, id));

    // if (!u.isAdmin()) {
    // courseStaffRepository.findByCourseIdAndGithubId(id, u.getGithubId())
    // .orElseThrow(() -> new AccessDeniedException(
    // String.format("User %s is not authorized to get course %d",
    // u.getGithubLogin(), id)));
    // }

    // return course;
    // }

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

    // @Operation(summary = "Add a staff member to a course")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    // @PostMapping("/addStaff")
    // public Staff addStaff(
    // @Parameter(name = "courseId") @RequestParam Long courseId,
    // @Parameter(name = "githubLogin") @RequestParam String githubLogin)
    // throws JsonProcessingException {

    // Course course = courseRepository.findById(courseId)
    // .orElseThrow(() -> new EntityNotFoundException(Course.class,
    // courseId.toString()));

    // User user = userRepository.findByGithubLogin(githubLogin)
    // .orElseThrow(() -> new EntityNotFoundException(User.class,
    // githubLogin.toString()));

    // Staff courseStaff = Staff.builder()
    // .courseId(course.getId())
    // .githubId(user.getGithubId())
    // .user(user)
    // .build();

    // courseStaff = courseStaffRepository.save(courseStaff);
    // log.info("courseStaff={}", courseStaff);

    // return courseStaff;
    // }

    // @Operation(summary = "Get Staff for course")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    // @GetMapping("/getStaff")
    // public Iterable<Staff> getStaff(
    // @Parameter(name = "courseId") @RequestParam Long courseId)
    // throws JsonProcessingException {

    // Course course = courseRepository.findById(courseId)
    // .orElseThrow(() -> new EntityNotFoundException(Course.class,
    // courseId.toString()));

    // Iterable<Staff> courseStaff =
    // courseStaffRepository.findByCourseId(course.getId());
    // return courseStaff;
    // }

    // @Operation(summary = "Delete a Course Staff by id")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    // @DeleteMapping("/staff")
    // public Object deleteStaff(
    // @Parameter(name = "id") @RequestParam Long id) {
    // Staff staff = courseStaffRepository.findById(id)
    // .orElseThrow(() -> new EntityNotFoundException(Staff.class, id.toString()));

    // courseStaffRepository.delete(staff);
    // return genericMessage("Staff with id %s is deleted".formatted(id));
    // }

    // @Operation(summary = "Update information for a course")
    // // allow for roles of ADMIN or INSTRUCTOR but only if the user is a staff
    // member
    // // for the course
    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    // @PutMapping("/update")
    // public Course updateCourse(
    // @Parameter(name = "id") @RequestParam Long id,
    // @Parameter(name = "name", description = "course name, e.g. CMPSC 156")
    // @RequestParam String name,
    // @Parameter(name = "school", description = "school abbreviation e.g. UCSB")
    // @RequestParam String school,
    // @Parameter(name = "term", description = "quarter or semester, e.g. F23")
    // @RequestParam String term,
    // @Parameter(name = "startDate", description = "in iso format, i.e.
    // YYYY-mm-ddTHH:MM:SS; e.g. 2023-10-01T00:00:00 see
    // https://en.wikipedia.org/wiki/ISO_8601") @RequestParam("startDate")
    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
    // @Parameter(name = "endDate", description = "in iso format, i.e.
    // YYYY-mm-ddTHH:MM:SS; e.g. 2023-12-31T11:59:59 see
    // https://en.wikipedia.org/wiki/ISO_8601") @RequestParam("endDate")
    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
    // @Parameter(name = "githubOrg", description = "for example ucsb-cs156-f23")
    // @RequestParam String githubOrg)
    // throws JsonProcessingException {

    // Course course = courseRepository.findById(id)
    // .orElseThrow(() -> new EntityNotFoundException(Course.class, id.toString()));

    // // Check if the current user is a staff member for this course or an admin.
    // If
    // // not, throw AccessDeniedException

    // User u = getCurrentUser().getUser();
    // if (!u.isAdmin()) {
    // courseStaffRepository.findByCourseIdAndGithubId(course.getId(),
    // u.getGithubId())
    // .orElseThrow(() -> new AccessDeniedException(
    // "User is not a staff member for this course"));
    // }

    // course.setName(name);
    // course.setSchool(school);
    // course.setTerm(term);
    // course.setStartDate(startDate);
    // course.setEndDate(endDate);
    // course.setGithubOrg(githubOrg);

    // course = courseRepository.save(course);
    // log.info("course={}", course);

    // return course;
    // }

    // // delete a course if the user is an admin or instructor for the course
    // @Operation(summary = "Delete a course")
    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    // @DeleteMapping("/delete")
    // public Course deleteCourse(
    // @Parameter(name = "id") @RequestParam Long id)
    // throws JsonProcessingException {

    // Course course = courseRepository.findById(id)
    // .orElseThrow(() -> new EntityNotFoundException(Course.class, id.toString()));

    // // Check if the current user is a staff member for this course or an admin.
    // If
    // // not, throw AccessDeniedException

    // User u = getCurrentUser().getUser();
    // if (!u.isAdmin()) {
    // courseStaffRepository.findByCourseIdAndGithubId(course.getId(),
    // u.getGithubId())
    // .orElseThrow(() -> new AccessDeniedException(
    // "User is not a staff member for this course"));
    // }

    // courseRepository.delete(course);
    // return course;
    // }

}