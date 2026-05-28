package com.food.recipeflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "recipes")
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
    @NotEmpty
    private List<String> equipments;

    @ElementCollection
    @NotEmpty
    private List<String> ingredients;

    @ElementCollection
    @NotEmpty
    private List<String> instructions;

    @ElementCollection
    @NotEmpty
    private List<String> tags;

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
