package com.yt.jpa.hospitalManagement.entity;

import com.yt.jpa.hospitalManagement.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }
}

/*
 * Roles assigned to user (authorization concern)

 * Why @ElementCollection?
   - Because roles are a collection of simple values (enum),
     not entities with their own identity or lifecycle.
   - Hibernate cannot store a collection inside a single column,
     so each role is stored as a separate row in a collection table.

 * Why @CollectionTable?
   - Defines the physical table used to persist the collection.
   - 'user_roles' is a join table that links users to their roles.
   - 'user_id' is a foreign key referencing the users table.

 * Table structure created:

     user_roles
     ----------
     user_id  | role

 * Why @Enumerated(EnumType.STRING)?
   - Stores enum names (e.g. ROLE_ADMIN) instead of ordinal values.
   - Prevents data corruption if enum order changes.

   FetchType.EAGER:
   - Roles are required immediately by Spring Security
     during authentication/authorization.
 */
