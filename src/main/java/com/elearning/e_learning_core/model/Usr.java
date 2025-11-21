package com.elearning.e_learning_core.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;

@Entity
public class Usr extends BaseEntity implements UserDetails {

    private String email;
    private String password;
    private boolean enabled = false;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JsonManagedReference
    private Person person;

    public Usr() {
        super();
        this.enabled = false;
    }

    public Usr(String email, String password, boolean isActive, Role role, Person person) {
        this.email = email;
        this.password = password;
        this.enabled = isActive;
        this.role = role;
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null)
            return null;
        return java.util.Collections.singletonList(() -> role.getRoleName());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return this.role;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
