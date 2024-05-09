package com.saulsapp.project_2.services;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saulsapp.project_2.models.Author;
import com.saulsapp.project_2.models.Book;
import com.saulsapp.project_2.repositories.AuthorsRepository;
import com.saulsapp.project_2.repositories.BooksRepository;

@Service
@Transactional(readOnly = true)
public class AuthorsService {
    private final AuthorsRepository authorsRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public AuthorsService(AuthorsRepository authorsRepository, BooksRepository booksRepository) {
        this.authorsRepository = authorsRepository;
        this.booksRepository = booksRepository;
    }

    public List<Author> getAll() {
        return authorsRepository.findAll();
    }
    
    public List<Author> getAll(int page, int size, String sort) {
        return authorsRepository.findAll(PageRequest.of(page-1, size, Sort.by(sort))).getContent();
    }

    public Author get(int id) {
        return authorsRepository.findById(id).orElse(null);
    }

    public Author getFull(int id, int page, int size, String sort){
        Author author = authorsRepository.findById(id).orElse(null);
        if (author == null) return null;
        author.setBooks(booksRepository.findByAuthorId(id, PageRequest.of(page-1, size, Sort.by(sort))));
        return author;
    }

    public List<Book> getBooks(int id) {
        Author author = authorsRepository.findById(id).orElse(null);
        if (author == null) return null;

        for (Book b: author.getBooks()){
            Hibernate.initialize(b);
        }

        return author.getBooks();

    }

    @Transactional
    public int save(Author author) {
        author = authorsRepository.save(author);
        return author.getId();
    }

    @Transactional
    public void delete(int id) {
        authorsRepository.deleteById(id);
    }

    public boolean checkAvailable(Author author){
        List<Author> authors =  authorsRepository.findByFamilyName(author.getFamilyName());
        for (Author a : authors) {
            if (a.getFullName().equals(author.getFullName())){
                if (a.getId() != author.getId()) return false;
            }
        }
        return true;
    }
}
