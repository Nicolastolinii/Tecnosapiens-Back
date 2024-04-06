package com.dblog.dblog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "blog_user" )
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String user;

    private String correo;

    private String password;

    private String image;

    private String otp;

    private Integer otpRequestCount = 0;

    private LocalDateTime otpGeneratedTime;

    private boolean emailValidated = false;

    private boolean admin= false;

    private boolean isValidated = false;

    @OneToMany(mappedBy = "autorId", cascade = CascadeType.ALL)
    private List<Blog> blogs;

}
