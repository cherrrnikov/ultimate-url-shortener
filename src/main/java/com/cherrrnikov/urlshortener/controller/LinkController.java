package com.cherrrnikov.urlshortener.controller;

import com.cherrrnikov.urlshortener.dto.CreateLinkRequest;
import com.cherrrnikov.urlshortener.dto.LinkResponse;
import com.cherrrnikov.urlshortener.entity.Link;
import com.cherrrnikov.urlshortener.mapper.LinkMapper;
import com.cherrrnikov.urlshortener.repository.LinkRepository;
import com.cherrrnikov.urlshortener.service.LinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
public class LinkController {
    private final LinkRepository linkRepository;
    private final LinkService linkService;
    private final LinkMapper mapper;

    @PostMapping
    public ResponseEntity<LinkResponse> createLink(@RequestBody @Valid CreateLinkRequest request) {
        Link link = mapper.toEntity(request);

        link.setShortCode(createShortCode());

        Link savedLink = linkService.save(link);

        LinkResponse response = mapper.toResponse(savedLink);

        response.setShortUrl("http://localhost:8080/" + savedLink.getShortCode());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable("shortCode") String shortCode) {
        Optional<Link> link = linkRepository.findByShortCode(shortCode);

        if (link.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND).header("Location", link.get().getOriginalUrl()).build();
    }

    private String createShortCode() {
        return Long.toHexString(System.currentTimeMillis());
    }
}
