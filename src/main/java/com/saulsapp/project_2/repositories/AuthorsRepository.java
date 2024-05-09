package com.saulsapp.project_2.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.saulsapp.project_2.models.Author;

@Repository
public interface AuthorsRepository extends JpaRepository<Author, Integer> {

    List<Author> findByFamilyName(String familyName);    

    @Query("SELECT a FROM Author a LEFT JOIN FETCH a.books WHERE a.id = :id")
    Author findFullById(@Param(value = "id") int id);
    
}
