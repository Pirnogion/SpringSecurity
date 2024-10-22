package ru.gleb.SpringSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gleb.SpringSecurity.entity.Actor;
import ru.gleb.SpringSecurity.entity.User;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

    List<Actor> findAllByCreatedBy(User user);
}
