package com.list.delete.dto;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChildDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idChild;

}
