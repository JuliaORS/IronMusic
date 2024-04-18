package com.ironhack.model;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Podcast extends Audio{
    private String season; //TODO: String?
    private String category; //TODO: enum?
}
