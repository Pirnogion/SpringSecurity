package ru.gleb.SpringSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gleb.SpringSecurity.entity.Movie;
import ru.gleb.SpringSecurity.entity.User;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT SUM(box) FROM Movie")
    Long calculateBox();

    List<Movie> findAllByCreatedBy(User user);
}
