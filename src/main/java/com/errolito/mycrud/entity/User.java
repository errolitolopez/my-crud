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
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @CreatedDate
    @ColumnDefault("NOW()")
    @Column(nullable = false)
    private Instant createdDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(
            name = "user_profile_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_user_profile_id")
    )
    private UserProfile userProfile;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", createdDate=" + createdDate +
                ", userProfile=" + userProfile +
                '}';
    }
}