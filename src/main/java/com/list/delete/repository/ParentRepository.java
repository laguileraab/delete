package com.list.delete.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.list.delete.model.Parent;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long>{
    
}
