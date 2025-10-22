package com.cherrrnikov.urlshortener.mapper;

import com.cherrrnikov.urlshortener.dto.CreateLinkRequest;
import com.cherrrnikov.urlshortener.dto.LinkResponse;
import com.cherrrnikov.urlshortener.entity.Link;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LinkMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clickCount", constant = "0L")
    @Mapping(target = "createdAt", ignore = true)
    Link toEntity(CreateLinkRequest request);

    LinkResponse toResponse(Link link);
}
