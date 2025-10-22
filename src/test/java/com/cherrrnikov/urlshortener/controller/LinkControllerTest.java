package com.cherrrnikov.urlshortener.controller;

import com.cherrrnikov.urlshortener.config.TestSecurityConfig;
import com.cherrrnikov.urlshortener.dto.CreateLinkRequest;
import com.cherrrnikov.urlshortener.dto.LinkResponse;
import com.cherrrnikov.urlshortener.entity.Link;
import com.cherrrnikov.urlshortener.mapper.LinkMapper;
import com.cherrrnikov.urlshortener.repository.LinkRepository;
import com.cherrrnikov.urlshortener.service.LinkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LinkController.class)
@Import(TestSecurityConfig.class)
class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LinkService linkService;

    @MockitoBean
    private LinkRepository linkRepository;

    @MockitoBean
    private LinkMapper linkMapper;

    @Test
    void shouldCreateShortLink() throws Exception {
        CreateLinkRequest request = new CreateLinkRequest();
        request.setOriginalUrl("https://google.com");

        Link link = new Link();
        link.setOriginalUrl("https://google.com");

        Link savedLink = new Link();
        savedLink.setId(UUID.randomUUID());
        savedLink.setOriginalUrl("https://google.com");
        savedLink.setShortCode("abc123");

        LinkResponse response = LinkResponse.builder()
                        .originalUrl("https://google.com")
                                .shortUrl("http://localhost:8080/abc123")
                                        .createdAt(LocalDateTime.now())
                                                .clickCount(0L)
                                                        .build();

        when(linkMapper.toEntity(any())).thenReturn(link);
        when(linkService.save(any(Link.class))).thenReturn(savedLink);
        when(linkMapper.toResponse(any())).thenReturn(response);

        mockMvc.perform(post("/api/links")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortUrl").exists())
                .andExpect(jsonPath("$.originalUrl").value("https://google.com"));
    }

    @Test
    void shouldRedirectToOriginalUrl() throws Exception {
        Link link = new Link();
        link.setOriginalUrl("https://google.com");
        link.setShortCode("abc123");

        when(linkRepository.findByShortCode("abc123")).thenReturn(Optional.of(link));

        mockMvc.perform(get("/api/links/abc123"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://google.com"));
    }

    @Test
    void shouldReturn404WhenShortCodeNotFound() throws Exception {
        when(linkRepository.findByShortCode("invalid")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/links/invalid"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenCreateLinkWithInvalidUrl() throws Exception {
        CreateLinkRequest request = new CreateLinkRequest();
        request.setOriginalUrl("invalid-url");

        mockMvc.perform(post("/api/links")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }
}