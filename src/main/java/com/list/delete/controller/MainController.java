package com.list.delete.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.list.delete.converter.ParentConverter;
import com.list.delete.dto.GenericResponseDTO;
import com.list.delete.dto.ParentDto;
import com.list.delete.model.Parent;
import com.list.delete.service.ParentService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RequestMapping("/api/parent")
@RestController
@RequiredArgsConstructor
public class MainController {

    private final ParentService parentService;

    @GetMapping("/{id}")
    public ResponseEntity<ParentDto> listParentById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(parentService.findParentDtoById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/save")
    public ResponseEntity<GenericResponseDTO> saveParent(@RequestBody Parent parent) throws JsonProcessingException {
        ParentDto parentDto = new ParentConverter()
                .parentToParentDto(parentService.saveParent(parent));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new GenericResponseDTO(
                        "Padre guardado exitosamente",
                        parentDto, 201));
    }
}
