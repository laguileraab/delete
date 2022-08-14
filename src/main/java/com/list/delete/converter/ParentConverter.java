package com.list.delete.converter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.list.delete.dto.ParentDto;
import com.list.delete.model.Parent;

@Component
public class ParentConverter {

    public ParentDto parentToParentDto(Parent parent) {
        if (parent != null)
            return ParentDto.builder()
                    .idParent(parent.getIdParent())
                    .name(parent.getName())
                    .uncle(new UncleConverter().uncleToUncleDto(parent.getUncle()))
                    .children(
                            new ChildConverter().childSetToChildSetDto(
                                    parent.getChildren()))
                    .build();
        return null;
    }

    public Parent parentDtoToParent(ParentDto parentDto) {
        if (parentDto != null)
            return Parent.builder()
                    .idParent(parentDto.getIdParent())
                    .name(parentDto.getName())
                    .uncle(new UncleConverter().uncleDtoToUncle(parentDto.getUncle()))
                    .children(
                            new ChildConverter().childDtoSetToChildsSet(
                                    parentDto.getChildren()))
                    .build();
        return null;
    }

    public List<ParentDto> parentsToParentsDto(List<Parent> parents) {
        List<ParentDto> parentsDto = new ArrayList<>();

        parents.stream()
                .forEach(e -> parentsDto.add(parentToParentDto(e)));

        return parentsDto;
    }
}
