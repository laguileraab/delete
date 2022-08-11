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
                    .children(
                            new ChildConverter().childDtoSetToChildsSet(
                                    parentDto.getChildren()))
                    .build();
        return null;
    }

    public List<ParentDto> requestsToRequestsDto(List<Parent> parents) {
        List<ParentDto> parentsDto = new ArrayList<>();

        parents.stream()
                .forEach((r) -> parentsDto.add(parentToParentDto(r)));

        return parentsDto;
    }
}
