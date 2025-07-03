package org.example.springsecurity4.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Role() {
    }

    public Role(RoleType name) {
    }

    //region getter, setter, toString
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "ROLE_" + name.name();
    }
    //endregion

    @Override
    public String getAuthority() {
        return name.name();
    }
}