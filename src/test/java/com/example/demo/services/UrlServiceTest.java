package com.example.demo.services;

import com.example.demo.DemoApplication;
import com.example.demo.model.Url;
import com.example.demo.model.User;
import com.example.demo.repository.UrlRepository;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when; //should normally use this one

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class UrlServiceTest {

    @MockBean
    private UrlRepository urlRepository;

    @Autowired
    private UrlService urlService;

    /**
     * Method under test: {@link UrlService#getUrl(User)}
     */
    @Test
    public void testGetUrl() {
        HashSet<Url> urlSet = new HashSet<>();
        when(urlRepository.findByUser(any())).thenReturn(urlSet);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setUrl(new HashSet<>());
        user.setUserId(123L);
        Set<Url> actualUrl = urlService.getUrl(user);
        assertSame(urlSet, actualUrl);
        assertTrue(actualUrl.isEmpty());
        verify(urlRepository).findByUser(any());
    }

    /**
     * Method under test: {@link UrlService#getEncodedUrl(String)}
     */
    @org.junit.jupiter.api.Test
    void testGetEncodedUrl() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setUrl(new HashSet<>());
        user.setUserId(123L);

        Url url = new Url();
        url.setCreationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        url.setExpirationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        url.setOriginalUrl("https://example.org/example");
        url.setShortLink("https://example.org/example");
        url.setUrlId(123L);
        url.setUser(user);
        when(urlRepository.findByShortLink(any())).thenReturn(url);
        assertSame(url, urlService.getEncodedUrl("https://example.org/example"));
        verify(urlRepository).findByShortLink(any());
    }

    @Test
    public void whenUserIsProvided_andUserExist_thenReturnUrls() {
        Set<Url> urls = new HashSet<>();
        User givenUser = new User(12L, "jacops"
                , "handlers", "password",
                "jacop@email.com", new HashSet<>());

        when(urlRepository.findByUser(givenUser)).thenReturn(urls);

        Set<Url> actualUrl = urlService.getUrl(givenUser);
        Assert.assertEquals(actualUrl.size(), 0);
        assertSame(actualUrl, urls);
        verify(urlRepository).findByUser(givenUser);
        assertTrue(actualUrl.isEmpty());
    }

    @Test
    public void whenUserIsProvided_andUserNotExist_thenReturnEmptyUrls() {
        Set<Url> urls = new HashSet<>();
        User givenUser = new User();
        boolean expected = urlRepository.findByUser(givenUser).isEmpty();
        assertTrue(expected);
    }

    @Test
    public void whenUrlIsProvided_andUrlExist_thenSaveTheLink() {
        Url givenUrl = new Url(12L, "www.google.com",
                "google", null,
                null, new User(12L, "jacops"
                , "handlers", "password",
                "jacops@handler.com", new HashSet<>()));
        when(urlRepository.save(givenUrl)).thenReturn(givenUrl);
        Url actualUrl = urlService.persistShortLink(givenUrl);
        Assert.assertEquals(actualUrl.getShortLink(), givenUrl.getShortLink());

    }

    @Test
    public void canDeleteShortLink() {
        Url givenUrl = new Url(12L, "www.google.com",
                "google", null,
                null, new User(12L, "jacops"
                , "handlers", "password",
                "jacop@handler.com", new HashSet<>()));

        when(urlService.persistShortLink(givenUrl)).thenReturn(givenUrl);
        Url actualUrl = urlService.persistShortLink(givenUrl);
        Assert.assertEquals(actualUrl, givenUrl);
        verify(urlRepository).save(givenUrl);

        urlService.deleteShortLink(actualUrl);
        verify(urlRepository).delete(actualUrl);

        //test for exception handling when url is not found
        urlService.deleteShortLink(actualUrl);

        //test for exception handling when you try to remove a url that does not exist
        urlService.deleteShortLink(null);
    }

}
