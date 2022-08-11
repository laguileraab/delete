package com.list.delete.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.list.delete.converter.ParentConverter;
import com.list.delete.dto.ParentDto;
import com.list.delete.model.Parent;
import com.list.delete.repository.ParentRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class ParentService {
    
    private final ParentRepository parentRepository;

    public Parent findParentById(Long id) {
        return parentRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("El padre con id: " + id + " no se encuentra"));
    }

    public ParentDto findParentDtoById(Long id) {
        return new ParentConverter().parentToParentDto(parentRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("El padre con id: " + id + " no se encuentra")));
    }
    public Parent saveParent(Parent parent) throws JsonProcessingException {
        return parentRepository.save(parent);
    }

}