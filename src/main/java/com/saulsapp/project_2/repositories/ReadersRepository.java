package com.saulsapp.project_2.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.saulsapp.project_2.models.Reader;

@Repository
public interface ReadersRepository extends JpaRepository<Reader, Integer> {

    @Query(
        value = "SELECT r FROM Reader r LEFT JOIN FETCH r.books WHERE r.id = :id",
        countQuery = "SELECT count(r) FROM Reader r WHERE r.id =:id")
    Reader findFullReader(@Param(value = "id") int id);

    List<Reader> findByFamilyNameStartingWithIgnoreCase(String s, Sort sort);

    List<Reader> findByFamilyName(String familyName);
    
}
