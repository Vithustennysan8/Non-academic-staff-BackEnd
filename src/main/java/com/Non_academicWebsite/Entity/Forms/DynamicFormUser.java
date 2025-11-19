package com.Non_academicWebsite.Entity.Forms;

import com.Non_academicWebsite.Entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DynamicFormUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(nullable = false)
    private DynamicForm dynamicForm;
    private String status;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "dynamicFormUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DynamicFormDetail> dynamicFormDetails;
    @OneToMany(mappedBy = "dynamicFormUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DynamicFormFileDetail> dynamicFormFileDetails;
}
