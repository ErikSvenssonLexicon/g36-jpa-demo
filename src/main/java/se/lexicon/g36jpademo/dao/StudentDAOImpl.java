package se.lexicon.g36jpademo.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.g36jpademo.model.Student;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Optional;

@Repository
public class StudentDAOImpl implements StudentDAO{

    private final EntityManager entityManager;

    @Autowired
    public StudentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Student persist(Student student) {
        entityManager.persist(student);
        return student;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findById(String id) {
        return Optional.ofNullable(entityManager.find(Student.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findByEmail(String email) {
        return entityManager.createQuery("SELECT s FROM Student s WHERE UPPER(s.email) = UPPER(:email)", Student.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Student> findByNameContains(String name) {
        return entityManager.createQuery(
                "SELECT s FROM Student s " +
                "WHERE " +
                "UPPER(s.firstName) LIKE UPPER(CONCAT('%', :name, '%')) " +
                "OR " +
                "UPPER(s.lastName) LIKE UPPER(CONCAT('%', :name, '%'))", Student.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Student> findAll() {
        return entityManager.createQuery("SELECT s FROM Student s", Student.class)
                .getResultList();
    }

    @Override
    @Transactional
    public Student update(Student updated) {
        return entityManager.merge(updated);
    }

    @Override
    @Transactional
    public void remove(String id) {
        Student student = entityManager.find(Student.class, id);
        if(student != null){
            entityManager.remove(student);
        }
    }
}
