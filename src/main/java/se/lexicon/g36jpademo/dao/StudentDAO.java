package se.lexicon.g36jpademo.dao;

import se.lexicon.g36jpademo.model.Student;

import java.util.Collection;
import java.util.Optional;

public interface StudentDAO {

    Student persist(Student student);
    Optional<Student> findById(String id);
    Optional<Student> findByEmail(String email);
    Collection<Student> findByNameContains(String name);
    Collection<Student> findAll();
    Student update(Student student);
    void remove(String id);

}
