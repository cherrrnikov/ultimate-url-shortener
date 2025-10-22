package com.cherrrnikov.urlshortener.service;

import com.cherrrnikov.urlshortener.entity.Link;

import java.util.Optional;

public interface LinkService {
    Optional<Link> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
    Link save(Link link);
}
