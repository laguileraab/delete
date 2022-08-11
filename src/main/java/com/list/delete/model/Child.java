package com.list.delete.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "child")
public class Child implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChild;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "ID_Parent")
    private Parent parent;

    private Integer parentId;

    @Column(name = "ParentID", insertable=false, updatable=false)
    public Integer getParentId() {
        return parentId;
    }
}