package com.errolito.mycrud.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user_profiles")
@EntityListeners(AuditingEntityListener.class)
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String fullName;

    @CreatedDate
    @ColumnDefault("NOW()")
    @Column(nullable = false, insertable = false, updatable = false)
    private Instant createdDate;

    @OneToOne(mappedBy = "userProfile")
    private User user;

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}