package com.example.demo.repository;

import com.example.demo.model.Url;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UrlRepository extends JpaRepository<Url, Double> {

    Set<Url> findByUser(User user);
    public Url findByShortLink(String shortLink);

}
