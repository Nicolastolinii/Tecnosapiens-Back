package com.dblog.dblog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "ipview")
@AllArgsConstructor
@NoArgsConstructor
public class IpView implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ipaddress" ,nullable = false)
    private String ipaddress;


    @Column(nullable = false)
    private Long postid;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

}
