package com.example.demo.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column( nullable = false, length = 20 )
    private String firstName;

    @Column( nullable = false, length = 20 )
    private String lastName;

    @Column( nullable = false, length = 64 )
    private String password;

    @Column( nullable = false, unique = true, length = 45 )
    private String email;

    @OneToMany( fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @ToString.Exclude
    private Set<Url> url;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return userId != null && Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
