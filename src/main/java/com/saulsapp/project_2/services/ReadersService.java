package com.saulsapp.project_2.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saulsapp.project_2.models.Book;
import com.saulsapp.project_2.models.Reader;
import com.saulsapp.project_2.repositories.ReadersRepository;

@Service
@Transactional(readOnly = true)
public class ReadersService {
    private final ReadersRepository readersRepository;
    private final long maxDaysTaken = 7;

    @Autowired
    public ReadersService(ReadersRepository readersRepository) {
        this.readersRepository = readersRepository;
    }
    
    public Page<Reader> getAll(int page, int size, String sort) {
        return readersRepository.findAll(PageRequest.of(page-1, size, Sort.by(sort)));
    }
    
    @Transactional()
    public void addReader(Reader reader) {
        readersRepository.save(reader);
    }

    public Reader getFull(int id) {
        Reader reader = readersRepository.findFullReader(id);
        if (reader == null) return reader;
        reader.getBooks().sort((Book b1, Book b2) -> b1.getName().compareToIgnoreCase(b2.getName()));
        LocalDate today = LocalDate.now();
        for (Book b: reader.getBooks()) {
            if (ChronoUnit.DAYS.between(b.getTakenOn(), today) > maxDaysTaken){
                b.setOverdue(true);
            };
        }

        return reader;
    }

    public Reader get(int id) {
        return readersRepository.findById(id).orElse(null);
    }

    @Transactional
    public void delete(int id) {
        readersRepository.deleteById(id);
    }

    public List<Reader> findByFamilyNameStartingWith(String s, Sort sort){
        return readersRepository.findByFamilyNameStartingWithIgnoreCase(s, sort);
    } 

    public boolean checkAvailable(Reader reader){
        List<Reader> readers = readersRepository.findByFamilyName(reader.getFamilyName());
        for (Reader r : readers) {
            if (r.getFullName().equals(reader.getFullName())) {
                if (r.getBirthDate().equals(reader.getBirthDate())){
                    if (r.getId() != reader.getId()) {return false;}
                }
            }
        }
        return true;
    }


}
