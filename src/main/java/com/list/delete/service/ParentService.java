package com.list.delete.service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

import javax.json.JsonMergePatch;
import javax.json.JsonValue;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
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

    //////////////////////////////
    // Reflection Patch
    //////////////////////////////

    public Parent patchParentReflection(Long id, LinkedHashMap<Object, Object> values) {
        Parent parent = findParentById(id);
        recursivePatchRequest(parent.getClass().getName(), parent, values);
        return parentRepository.save(parent);
    }

    public <T> void recursivePatchRequest(String clazz, T object, LinkedHashMap<Object, Object> fields) {
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
                                // Se envia cada uno de los objetos dentro del arreglo
                                // sin embargo dado que la lista de entrada no tiene que
                                // coincidir con la de salida no es posible recorrer la lista,
                                // aunque fuera un doble ciclo no se conoce cual objeto
                                // corresponde a cual, por lo que se descarta esta via para los arreglos.
                                // Notar que se envia la clase del objeto y el mismo valor repetido
                                // en los otros dos campos, en donde el primero corresponderia al
                                // objeto que recibe los campos, pero este se desconoce
                                recursivePatchRequest(childClass.getName(), objectMapper.convertValue(v, childClass),
                                        (LinkedHashMap<Object, Object>) v);
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    ReflectionUtils.setField(field, object,
                            objectMapper.convertValue(fields.get((String) key), theClass));
                } catch (IllegalArgumentException | ClassNotFoundException
                        | SecurityException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //////////////////////////////

    //////////////////////////////
    // Mapper Patch
    //////////////////////////////
    public Parent patchParentMapper(Long id, Parent newParent) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ParentDto oldRequest = findParentDtoById(id);
        ObjectReader updater = objectMapper.readerForUpdating(oldRequest);
        String newRequestString = objectMapper.writeValueAsString(newParent);
        ParentDto merged = updater.readValue(newRequestString);
        return parentRepository.save(new ParentConverter()
                .parentDtoToParent(merged));
    }
    //////////////////////////////

    //////////////////////////////
    // Merge JsonPatch
    //////////////////////////////

    public Parent jsonMergePatchRequest(Long id, JsonMergePatch mergePatch) {
        Parent parent = findParentById(id);
        return parentRepository.save(mergePatch(mergePatch, parent, Parent.class));
    }

    public <T> T mergePatch(JsonMergePatch mergePatch, T targetBean, Class<T> beanClass) {
        ObjectMapper objectMapper = new ObjectMapper()
        .setDefaultPropertyInclusion(Include.NON_NULL)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .findAndRegisterModules();
        JsonValue target = objectMapper.convertValue(targetBean, JsonValue.class);
        JsonValue patched = mergePatch.apply(target);
        return objectMapper.convertValue(patched, beanClass);
    }
    //////////////////////////////

}