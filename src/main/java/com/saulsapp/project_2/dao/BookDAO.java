package com.saulsapp.project_2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import com.saulsapp.project_2.models.Book;

@Component
public class BookDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Book> getAll() {
        return jdbcTemplate.query("SELECT b.id as b_id, b.name as b_name, year, r.id as r_id, a.id as a_id, a.name as a_name, a.patronymic, a.family_name FROM book b JOIN author a ON b.author_id = a.id LEFT JOIN reader r ON b.reader_id = r.id", new BookMapper());
    }

    public void release(int id) {
        jdbcTemplate.update("UPDATE book SET reader_id = NULL WHERE id = ?", id);
    }

    public Book get(int id) {
        Book book = jdbcTemplate.queryForStream("SELECT b.id as b_id, b.name as b_name, year, r.id as r_id, a.id as a_id, a.name as a_name, a.patronymic, a.family_name FROM book b JOIN author a ON b.author_id = a.id LEFT JOIN reader r ON b.reader_id = r.id WHERE b.id = ?", new BookMapper(), id).findFirst().orElse(null);
        return book;
    }

    public void assign(int bookId, int readerId) {
        jdbcTemplate.update("UPDATE book SET reader_id = ? WHERE id = ?", readerId, bookId);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE id= ?", id);
    }

    public void edit(Book book) {
        jdbcTemplate.update("UPDATE book SET name = ?, year = ?, author_id = ? WHERE id = ?", book.getName(), book.getYear(), book.getAuthor().getId(), book.getId());
    }

    public int add(Book book) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement("INSERT INTO book (name, year, author_id) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, book.getName());
                statement.setInt(2, book.getYear());
                statement.setInt(3, book.getAuthor().getId());
                return statement;
            }
            
        }, holder);
        return (int) holder.getKeys().get("id");
    }
    
}
