package com.Non_academicWebsite.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.validation.constraints.Pattern;

import java.util.Collection;
import java.util.Date;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date_of_birth;
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
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date updatedAt;
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
