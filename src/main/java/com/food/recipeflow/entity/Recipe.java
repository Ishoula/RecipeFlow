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

    private Long views = 0L;
    private Long likes = 0L;
    private Long dislikes = 0L;
    private Long comments = 0L;
}