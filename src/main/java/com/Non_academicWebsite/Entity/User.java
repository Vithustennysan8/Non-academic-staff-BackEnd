package com.Non_academicWebsite.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {

    @Id
    private String id;
    private String first_name;
    private String last_name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_of_birth;
    private String gender;
    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gs\\.pdn\\.ac\\.lk$", message = "Email must be from the gs.pdn.ac.lk domain")
    private String email;
    private String normalEmail;
    private String app_password;
    private Long phone_no;
    @Column(nullable = false)
    private String password;
    private String address;
    private String city;
    private Integer postal_code;
    private String ic_no;
    private String emp_id;
    private String jobType;
    private String department;
    private String faculty;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String image_name;
    private String image_type;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image_data;
    private boolean verified;
    private boolean rejected;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return verified;
    }
}
