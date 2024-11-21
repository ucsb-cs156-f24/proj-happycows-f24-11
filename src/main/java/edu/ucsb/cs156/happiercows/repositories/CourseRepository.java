package edu.ucsb.cs156.happiercows.repositories;

import edu.ucsb.cs156.happiercows.entities.Course;
import java.util.Optional;

// import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {

    public Optional<Course> findById(Long id);

    // @Query("select c from courses c where c.id in (select cs.courseId from staff
    // cs where cs.githubId = :githubId)")
    // public Iterable<Coursses> findCoursesStaffedByUser(Integer githubId);
}