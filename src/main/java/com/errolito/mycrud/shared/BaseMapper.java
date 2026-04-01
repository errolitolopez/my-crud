package com.errolito.mycrud.shared;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface BaseMapper<REQUEST, ENTITY, RESPONSE> {
    List<ENTITY> toResponses(List<RESPONSE> responses);

    RESPONSE toResponse(ENTITY ENTITY);

    ENTITY toEntity(REQUEST REQUEST);

    void fromRequest(REQUEST source, @MappingTarget ENTITY target);
}