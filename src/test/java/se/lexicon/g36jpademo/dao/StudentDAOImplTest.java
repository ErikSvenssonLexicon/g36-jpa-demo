package se.lexicon.g36jpademo.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.g36jpademo.model.Student;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
@DirtiesContext
class StudentDAOImplTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private StudentDAOImpl testObject;

    public List<Student> students(){
        return Arrays.asList(
                new Student("Johny", "Grenander", "johny@gmail.com", LocalDate.parse("1967-01-01")),
                new Student("Erik", "Svensson", "erik@gmail.com", LocalDate.parse("1976-09-11")),
                new Student("Chuck", "Norris", "oskar@gmail.com", LocalDate.parse("1973-05-20")),
                new Student("Olof", "Schylander", "olof@gmail.com", LocalDate.parse("1992-04-20"))
        );
    }

    private List<Student> persistedStudents;

    @BeforeEach
    void setUp() {
        persistedStudents = students().stream()
                .map(em::persist)
                .collect(Collectors.toList());
    }

    @Test
    void persist() {
        Student student = new Student("Peter", "The man", "peter@gmail.com", LocalDate.parse("1967-01-02"));

        Student result = testObject.persist(student);

        assertNotNull(result);
        assertNotNull(result.getStudentId());

        System.out.println(result);
    }

    @Test
    void findById() {
        String johnysId = persistedStudents.get(0).getStudentId();

        Optional<Student> result = testObject.findById(johnysId);

        assertTrue(result.isPresent());
    }

    @Test
    void findByEmail() {
        String email = "erik@gmail.com";

        Optional<Student> result = testObject.findByEmail(email);

        assertTrue(result.isPresent());
    }

    @Test
    void findByNameContains() {
        String query = "IS";
        int expectedSizeOfCollection = 1;

        Collection<Student> result = testObject.findByNameContains(query);

        assertFalse(result.isEmpty());
        assertEquals(expectedSizeOfCollection, result.size());

    }

    @Test
    void findAll() {
        int expectedSize = 4;

        Collection<Student> result = testObject.findAll();

        assertFalse(result.isEmpty());
        assertEquals(expectedSize, result.size());
    }

    @Test
    void update() {
        Student original = persistedStudents.get(2);
        String firstName = "Oskar";
        String lastName = "Wiklund";
        Student detatchedUpdated = new Student(original.getStudentId(), firstName, lastName, original.getEmail(), original.getBirthDate());

        Student result = testObject.update(detatchedUpdated);

        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
    }

    @Test
    void remove() {
        String erikId = persistedStudents.get(1).getStudentId();

        testObject.remove(erikId);

        assertNull(em.find(Student.class, erikId));
    }
}