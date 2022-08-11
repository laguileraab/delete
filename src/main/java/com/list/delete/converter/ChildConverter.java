package com.list.delete.converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.list.delete.dto.ChildDto;
import com.list.delete.model.Child;

@Component
public class ChildConverter {

    ModelMapper modelMapper = new ModelMapper();

    public ChildDto childToChildDto(Child child) {

        if (child != null)
            return modelMapper.map(child, ChildDto.class);

        return null;
    }

    public Child childDtoToChild(ChildDto childDto) {

        if (childDto != null)
            return modelMapper.map(childDto, Child.class);

        return null;
    }

    public List<ChildDto> childsToChildsDto(List<Child> childs) {
        List<ChildDto> childsDto = new ArrayList<>();
        if(childs != null)
            childs
                .forEach((e) -> childsDto.add(childToChildDto(e)));
        return childsDto;
    }

    public Set<ChildDto> childSetToChildSetDto(Set<Child> childs) {
        Set<ChildDto> childsDto = new HashSet<>();
        if(childs != null)
            childs
                .forEach((e) -> childsDto.add(childToChildDto(e)));
        return childsDto;
    }

    public Set<Child> childDtoSetToChildsSet(Set<ChildDto> childsDto) {
        Set<Child> childs = new HashSet<>();
        if(childsDto != null)
            childsDto.forEach((e) -> childs.add(childDtoToChild(e)));
        return childs;
    }
}