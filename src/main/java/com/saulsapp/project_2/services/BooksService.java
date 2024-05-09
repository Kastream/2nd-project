package com.saulsapp.project_2.services;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saulsapp.project_2.models.Book;
import com.saulsapp.project_2.models.Reader;
import com.saulsapp.project_2.repositories.BooksRepository;


@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public Page<Book> getAll(int page, int size, String sort) {
        return booksRepository.findAllWithAuthors(PageRequest.of(page-1, size, Sort.by(sort)));
        
    }

    @Transactional()
    public void relase(int id) {
        Book book = booksRepository.findById(id).orElse(null);
        if (book == null) return;
        book.setTakenOn(null);
        book.setReader(null);
    }

    public Book get(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    public List<Book> search(String s){
        return booksRepository.findByNameContainingIgnoreCase(s);
    }

    @Transactional
    public void assign(int bookId, int readerId) {
        Book book = booksRepository.findById(bookId).orElse(null);
        if (book == null) return;
        book.setTakenOn(LocalDate.now());
        book.setReader(new Reader(readerId));

    }

    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    @Transactional
    public void edit(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public int add(Book book){
        book = booksRepository.save(book);
        return book.getId();
    }

    @Transactional
    public void release(int id) {
        Book book = booksRepository.findById(id).orElse(null);
        if (book != null) {
            book.setReader(null);
        }
        booksRepository.save(book);
    }
}
