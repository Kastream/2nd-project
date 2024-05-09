package com.saulsapp.project_2.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.saulsapp.project_2.models.Book;


@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    @Query(
        value = "SELECT b FROM Book b LEFT JOIN FETCH b.author LEFT JOIN FETCH b.reader",
        countQuery = "SELECT count(b) FROM Book b")
    Page<Book> findAllWithAuthors(Pageable pageable);
    
    List<Book> findByAuthorId(int id, Pageable pageable); 

    List<Book> findByNameContainingIgnoreCase(String s);
}
