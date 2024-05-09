package com.saulsapp.project_2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import com.saulsapp.project_2.models.Author;
import com.saulsapp.project_2.models.Book;

@Component
public class AuthorDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Author> getAll() {
        return jdbcTemplate.query("SELECT * FROM author", new BeanPropertyRowMapper<>(Author.class));
    }

    public Author get(int id){
        return jdbcTemplate.queryForStream("SELECT * FROM author WHERE id = ?", new BeanPropertyRowMapper<Author>(Author.class), id).findFirst().orElse(null);
    }

    public List<Book> getBooks(int id){
        return jdbcTemplate.queryForStream("SELECT b.id as b_id, b.name as b_name, year, r.id as r_id, a.id as a_id, a.name as a_name, a.patronymic, a.family_name FROM book b JOIN author a ON b.author_id = a.id LEFT JOIN reader r ON b.reader_id = r.id WHERE a.id = ?", new BookMapper(), id).toList();
    }

    public int add(Author author){
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement("INSERT INTO author (name, patronymic, family_name) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, author.getName());
                statement.setString(2, author.getPatronymic());
                statement.setString(3, author.getFamilyName());
                return statement;
            }
            
        }, holder);
        return (int) holder.getKeys().get("id");
    }

    public void edit(Author author) {
        jdbcTemplate.update("UPDATE author SET name = ?, patronymic = ?, family_name = ? WHERE id = ?", author.getName(), author.getPatronymic(), author.getFamilyName(), author.getId());
    }

    public void delete(int id) throws DataAccessException { 
        jdbcTemplate.update("DELETE FROM author WHERE id = ?", id);
    }

    public boolean checkAvailable(Author author) {
        Author checkAuthor = jdbcTemplate.queryForStream("SELECT * FROM author WHERE name = ? AND patronymic = ? AND family_name = ?", new BeanPropertyRowMapper<Author>(Author.class),author.getName(), author.getPatronymic(), author.getFamilyName()).findFirst().orElse(null);
        if (checkAuthor != null) return false;
        return true;
    }

}
