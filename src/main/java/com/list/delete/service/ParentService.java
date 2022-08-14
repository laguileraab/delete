package com.list.delete.service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public Parent patchParent(Long id, LinkedHashMap<Object,Object> values) throws JsonProcessingException {
        Parent parent = findParentById(id);
        recursivePatchRequest(parent.getClass().getName(), parent, values);
        return parentRepository.save(parent);
    }

    public <T> void recursivePatchRequest(String clazz, T object, LinkedHashMap<Object, Object> fields)
            throws JsonProcessingException {
        fields.forEach((key, value) -> {
            if (value != null) {
                Field field = null;
                try {
                    field = ReflectionUtils.findField(Class.forName(clazz), (String) key);
                    field.setAccessible(true);
                    Class<?> theClass = Class.forName(field.getType().getName());
                    ObjectMapper objectMapper = new ObjectMapper();
                    if (value instanceof java.util.LinkedHashMap) { // Object
                        recursivePatchRequest(theClass.getName(),
                                objectMapper.convertValue(fields.get((String) key), theClass),
                                (LinkedHashMap<Object, Object>) value);
                    }
                    if (value instanceof java.util.ArrayList) { // List
                        ParameterizedType parentType = ((ParameterizedType) Class.forName(clazz)
                                .getDeclaredField((String) key).getGenericType());
                        ((ArrayList<T>) value).forEach(v -> {
                            Class<?> childClass = null;
                            Type childTtype = parentType.getActualTypeArguments()[0];
                            if (childTtype instanceof Class) {
                                childClass = (Class<?>) childTtype;
                            }
                            try {
                                recursivePatchRequest(childClass.getName(), objectMapper.convertValue(v, childClass),
                                        (LinkedHashMap<Object, Object>) v);
                            } catch (JsonProcessingException | IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    ReflectionUtils.setField(field, object,
                            objectMapper.convertValue(fields.get((String) key), theClass));
                } catch (IllegalArgumentException | JsonProcessingException | ClassNotFoundException
                        | SecurityException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}