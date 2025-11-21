package com.elearning.e_learning_core.Dtos;

import com.elearning.e_learning_core.model.Role;

public class UsrDto {
    private Long id;
    private String email;
    private boolean enabled;
    private Role role;
    private Long personId;

    public UsrDto() {
    }

    public UsrDto(Long id, String email, boolean enabled, Role role, Long personId) {
        this.id = id;
        this.email = email;
        this.enabled = enabled;
        this.role = role;
        this.personId = personId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }
}
