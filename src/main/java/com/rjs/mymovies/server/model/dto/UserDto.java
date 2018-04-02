package com.rjs.mymovies.server.model.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

public class UserDto {
    @NotNull(message = "First name is required.")
    @NotEmpty(message = "First name is required.")
    private String firstName;
    @NotNull(message = "Last name is required.")
    @NotEmpty(message = "Last name is required.")
    private String lastName;
    @NotNull(message = "Email is required.")
    @NotEmpty(message = "Email is required.")
    @Pattern(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$", message = "Invalid email address.")
    private String email;
    @NotNull(message = "Username is required.")
    @NotEmpty(message = "Username is required.")
    private String username;
    private Set<String> roles;
    @NotNull(message = "Password is required.")
    @NotEmpty(message = "Password is required.")
    private String password;
    private String confirmPassword;
    private boolean newUser;

    public UserDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }
}
