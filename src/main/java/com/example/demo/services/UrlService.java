package com.example.demo.services;

import com.example.demo.dto.UrlDto;
import com.example.demo.model.Url;
import com.example.demo.model.User;
import com.example.demo.repository.UrlRepository;
import org.apache.commons.lang3.StringUtils;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Set;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository, UserService userService) {
        this.urlRepository = urlRepository;
    }

    public Set<Url> getUrl(User user){
        return urlRepository.findByUser(user);
    }

    public Url generateShortLink(UrlDto urlDto, User user){
        if (StringUtils.isNotEmpty(urlDto.getUrl())){
        String encodeUrl = encodeUrl(urlDto.getUrl());
        Url urlToPersist = new Url();
        urlToPersist.setShortLink(encodeUrl);
        urlToPersist.setOriginalUrl(urlDto.getUrl());
        urlToPersist.setCreationDate(LocalDateTime.now());
        urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(),urlToPersist.getCreationDate()));
        urlToPersist.setUser(user);
        return persistShortLink(urlToPersist);
    }

        return null;
    }

    private String encodeUrl(String url) {
        String encodeUrl;
        LocalDateTime time = LocalDateTime.now();
        encodeUrl = Hashing.murmur3_32().hashString(url.concat(time.toString()), StandardCharsets.UTF_8).toString();
        return encodeUrl;
    }
    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {
        if (StringUtils.isBlank(expirationDate)){
            return creationDate.plusSeconds(60);
        }
        return LocalDateTime.parse(expirationDate);
    }
    public Url persistShortLink(Url url) {
        return urlRepository.save(url);
    }
    public Url getEncodedUrl(String url) {
        return urlRepository.findByShortLink(url);
    }

    public void deleteShortLink(Url url) {
        try {
            urlRepository.delete(url);
        }catch (Exception e){
            System.out.println(e);
        }


    }
}
