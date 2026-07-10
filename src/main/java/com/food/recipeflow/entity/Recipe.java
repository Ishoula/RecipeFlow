package com.food.recipeflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "recipes", indexes = {
        @Index(name = "idx_recipe_category", columnList = "category"),
        @Index(name = "idx_recipe_user_id", columnList = "user_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String time;

    @NotBlank
    private String category;

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @NotEmpty
    private Set<String> equipments = new LinkedHashSet<>();

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @NotEmpty
    private Set<String> ingredients = new LinkedHashSet<>();

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @NotEmpty
    private Set<String> instructions = new LinkedHashSet<>();

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @NotEmpty
    private Set<String> tags = new LinkedHashSet<>();

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long views = 0L;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long likes = 0L;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long dislikes = 0L;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long comments = 0L;

    @PrePersist
    @PreUpdate
    private void defaultCounters() {
        if (views == null) {
            views = 0L;
        }
        if (likes == null) {
            likes = 0L;
        }
        if (dislikes == null) {
            dislikes = 0L;
        }
        if (comments == null) {
            comments = 0L;
        }
    }
}
