package dev.aryank.promptcanvas.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Column(unique = true)
    String stripePriceId;

    Integer maxProjects;
    Integer maxTokensPerDay;
    Integer maxPreviews; // max number of previews allowed per plan
    Boolean unlimitedAi; // unlimited access to LLM, ignore maxTokensPerDay is this is true

    Boolean active;


}
