package com.ecommerce.project.category.model;

import com.ecommerce.project.cart.model.Cart;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "userName"),
        @UniqueConstraint(columnNames = "email")
})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @NotBlank
    @Size(max=50)
    private String userName;

    @NotBlank
    @Size(max=50)
    @Email
    private String email;

    @NotBlank
    @Size(max=120)
    private String password;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.MERGE,CascadeType.PERSIST}, orphanRemoval = true)
    private Cart cart;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
                                  inverseJoinColumns =@JoinColumn(name = "role_id") )
    private Set<Role> roles = new HashSet<>();


    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.MERGE},
    orphanRemoval = true)
    private Set<Product> products;

    @ManyToMany(
               cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_addresses", joinColumns = @JoinColumn(name = "user_id"),
                                       inverseJoinColumns =@JoinColumn(name = "address_id") )
    private List<Addresse> addresses = new ArrayList<>();

    public User(String username, String email, String encode) {
    }
}
