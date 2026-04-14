package com.errolito.mycrud.shared;

import org.mapstruct.MappingTarget;

/**
 * Generic mapper contract for converting between Request, Entity, and Response.
 *
 * @param <R> Request type
 * @param <E> Entity type
 * @param <T> Response type
 */
public interface BaseMapper<R, E, T> {

    /**
     * Convert entity to response DTO.
     */
    T toResponse(E entity);

    /**
     * Convert request DTO to entity.
     */
    E toEntity(R request);

    /**
     * Copy request values into existing entity.
     */
    void fromRequest(R source, @MappingTarget E target);
}