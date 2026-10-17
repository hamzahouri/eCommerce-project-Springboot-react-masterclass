package com.ecommerce.project.category.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Addresse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long addressId;

    @NotBlank
    private String street;

    @NotBlank
    private Long buildingName;

    @NotBlank
    private Long city;

    @NotBlank
    private Long state;

    @NotBlank
    private Long country;

    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();


}
