package ru.gleb.SpringSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gleb.SpringSecurity.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
