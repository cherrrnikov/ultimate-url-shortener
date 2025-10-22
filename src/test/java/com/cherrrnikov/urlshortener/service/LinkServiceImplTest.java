package com.cherrrnikov.urlshortener.service;


import com.cherrrnikov.urlshortener.entity.Link;
import com.cherrrnikov.urlshortener.repository.LinkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LinkServiceImplTest {

    // Создаем mock репозитория (виртуальная база)
    @Mock
    private LinkRepository linkRepository;

    // Создаем реальный Service и внедряем в него mock
    @InjectMocks
    private LinkServiceImpl linkService;

    @Test
    public void shouldFindLinkByShortCode() {
        // 1. Arrange (подготовка)
        String shortCode = "abc123";
        Link expectedLink = new Link();
        expectedLink.setShortCode(shortCode);
        expectedLink.setOriginalUrl("https://google.com");

        // Говорим mock: когда вызовут findByShortCode с abc123 верни expectedLink
        when(linkRepository.findByShortCode(shortCode))
                .thenReturn(Optional.of(expectedLink));

        // 2. Act (действие) - вызываем реальный метод
        Optional<Link> result = linkService.findByShortCode(shortCode);

        // 3. Assert (проверка)
        assertTrue(result.isPresent(), "Link should be founded");
        assertEquals("abc123", result.get().getShortCode());
        assertEquals("https://google.com", result.get().getOriginalUrl());

        // Проверяем что метод репозитория был вызван только 1 раз
        verify(linkRepository, times(1)).findByShortCode(shortCode);
    }

    @Test
    public void shouldReturnTrueWhenLinkExists() {
        // 1. Arrange
        String shortCode = "abc123";

        when(linkRepository.existsByShortCode(shortCode))
                .thenReturn(true);

        // 2. Act
        boolean result = linkService.existsByShortCode(shortCode);

        // 3. Assert
        assertTrue(result, "Link should be founded");
        verify(linkRepository, times(1)).existsByShortCode(shortCode);
    }

    @Test
    public void shouldReturnFalseWhenLinkNotExists() {
        String shortCode = "abc123";

        when(linkRepository.existsByShortCode(shortCode))
                .thenReturn(false);

        boolean result = linkService.existsByShortCode(shortCode);

        assertFalse(result, "Link should NOT be founded");
        verify(linkRepository, times(1)).existsByShortCode(shortCode);
    }

    @Test
    public void shouldSaveLink() {
        String shortCode = "abc123";
        Link linkToSave = new Link();
        linkToSave.setShortCode(shortCode);
        linkToSave.setOriginalUrl("https://google.com");

        Link savedLink = new Link();
        savedLink.setId(UUID.randomUUID());
        savedLink.setShortCode(shortCode);
        savedLink.setOriginalUrl("https://google.com");

        when(linkRepository.save(linkToSave))
                .thenReturn(savedLink);

        Link result = linkService.save(linkToSave);

        assertNotNull(result, "Link should be saved");
        assertNotNull(result.getId());
        assertEquals("abc123", result.getShortCode());

        verify(linkRepository, times(1)).save(linkToSave);
    }

    @Test
    public void shouldHandleEmptyShortCode() {
        String shortCode = "";
        when(linkRepository.findByShortCode(shortCode))
        .thenReturn(Optional.empty());

        Optional<Link> result = linkService.findByShortCode(shortCode);

        assertFalse(result.isPresent(), "Should return empty");
        verify(linkRepository, times(1)).findByShortCode(shortCode);
    }
}
