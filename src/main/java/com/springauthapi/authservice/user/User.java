package com.springauthapi.authservice.user;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // generate equals/hashcode methods on specific fields
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include // automatically generate equals and hashcode methods
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    /**
     * Role Column:
     * - simple solution
     * - in larger/complex applications it may be a good idea to move roles to
     * seperate table
     */
    @Column(name = "role")
    @Builder.Default
    @Enumerated(EnumType.STRING) // store value as a string in the db
    private Role role = Role.USER;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "email = " + email + ")";
    }
}
