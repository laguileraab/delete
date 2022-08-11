package com.list.delete.model;

import java.io.Serializable;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "ID_Parent")
    private Parent parent;

}
