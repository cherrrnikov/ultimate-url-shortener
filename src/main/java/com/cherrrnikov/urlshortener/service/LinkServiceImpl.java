package com.cherrrnikov.urlshortener.service;

import com.cherrrnikov.urlshortener.entity.Link;
import com.cherrrnikov.urlshortener.repository.LinkRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    @Override
    public Optional<Link> findByShortCode(String shortCode) {
        log.debug("Finding link by shortCode: {}", shortCode);
        return linkRepository.findByShortCode(shortCode);
    }

    @Override
    public boolean existsByShortCode(String shortCode) {
        return linkRepository.existsByShortCode(shortCode);
    }

    @Override
    public Link save(Link link) {
        log.info("Saving new link: {} -> {}", link.getShortCode(), link.getOriginalUrl());
        return linkRepository.save(link);
    }
}
