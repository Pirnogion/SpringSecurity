package ru.gleb.SpringSecurity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "visible", nullable = false)
    private Boolean visible = false;

    @Column(name = "box", nullable = false)
    private Long box = 0L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "movies_actors",
            joinColumns = { @JoinColumn(name = "movie_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "actor_id", referencedColumnName = "id") }
    )
    private List<Actor> actors = new ArrayList<>();
}
