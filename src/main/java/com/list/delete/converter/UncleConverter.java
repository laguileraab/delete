package com.list.delete.converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.list.delete.dto.ChildDto;
import com.list.delete.dto.UncleDto;
import com.list.delete.model.Child;
import com.list.delete.model.Uncle;

@Component
public class UncleConverter {

    ModelMapper modelMapper = new ModelMapper();

    public UncleDto uncleToUncleDto(Uncle uncle) {

        if (uncle != null)
            return modelMapper.map(uncle, UncleDto.class);

        return null;
    }

    public Uncle uncleDtoToUncle(UncleDto uncleDto) {

        if (uncleDto != null)
            return modelMapper.map(uncleDto, Uncle.class);

        return null;
    }

    public List<UncleDto> unclesToUnclesDto(List<Uncle> uncles) {
        List<UncleDto> unclesDto = new ArrayList<>();
        if (uncles != null)
            uncles
                    .forEach(e -> unclesDto.add(uncleToUncleDto(e)));
        return unclesDto;
    }

    public Set<UncleDto> uncleSetToUncleSetDto(Set<Uncle> uncles) {
        Set<UncleDto> unclesDto = new HashSet<>();
        if (uncles != null)
            uncles
                    .forEach(e -> unclesDto.add(uncleToUncleDto(e)));
        return unclesDto;
    }

    public Set<Uncle> uncleDtoSetToChildsSet(Set<UncleDto> unclesDto) {
        Set<Uncle> uncles = new HashSet<>();
        if (unclesDto != null)
            unclesDto.forEach(e -> uncles.add(uncleDtoToUncle(e)));
        return uncles;
    }
}