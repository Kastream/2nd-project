package com.saulsapp.project_2.dao;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.saulsapp.project_2.models.Reader;

@Component
public class ReaderDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReaderDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Reader> getAll(){
        List<Reader> readers = jdbcTemplate.query("SELECT * FROM reader ORDER BY family_name", new ReaderMapper());
        return readers;
    } 

    public void addReader(Reader reader){
        jdbcTemplate.update("INSERT INTO reader (name, patronymic, family_name, birth_year) VALUES (?, ?, ?, ?)", reader.getName(), reader.getPatronymic(), reader.getFamilyName(), reader.getBirthDate());
    }

    public Reader getFull(int id) {
        Reader reader = jdbcTemplate.queryForStream("SELECT * FROM reader WHERE id = " + id, new ReaderMapper()).findFirst().orElse(null);

        if (reader != null){
            reader.setBooks(jdbcTemplate.query("SELECT b.id as b_id, b.name as b_name, year, r.id as r_id, a.id as a_id, a.name as a_name, a.patronymic, a.family_name FROM book b JOIN author a ON b.author_id = a.id LEFT JOIN reader r ON b.reader_id = r.id WHERE r.id =" + id , new BookMapper()));
        }
        return reader;
    }

    public Reader get(int id) {
        Reader reader = jdbcTemplate.queryForStream("SELECT * FROM reader WHERE id = " + id, new ReaderMapper()).findFirst().orElse(null);
        return reader;
    }

    public void update(Reader reader) {
        jdbcTemplate.update("UPDATE reader SET name = ?, patronymic = ?, family_name = ?, birth_year = ? WHERE id = ?", reader.getName(), reader.getPatronymic(), reader.getFamilyName(), reader.getBirthDate(), reader.getId());
    }

    public void delete(int id) throws DataAccessException {
        jdbcTemplate.update("DELETE FROM reader WHERE id = ?", id);
    }

    public boolean checkAvailable(Reader reader) {
        Reader checkReader = jdbcTemplate.queryForStream("SELECT * FROM reader WHERE name = ? AND patronymic = ? AND family_name = ? AND birth_year = ?", new ReaderMapper(), reader.getName(), reader.getPatronymic(), reader.getFamilyName(), reader.getBirthDate()).findFirst().orElse(null);
        if (checkReader != null) return false;
        return true;
    }


}
