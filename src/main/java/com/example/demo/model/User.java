package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
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
    private Set<Url> url;




}
