package edu.ucsb.cs156.happiercows.repositories;

import edu.ucsb.cs156.happiercows.entities.Student;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
    Optional<Student> findById(Long id);
    Iterable<Student> findAllByCourseId(Long courseId);
}