package com.saulsapp.project_2.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.saulsapp.project_2.models.Author;
import com.saulsapp.project_2.models.Book;

public class BookMapper implements RowMapper<Book>{

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("b_id"));
        book.setName(rs.getString("b_name"));
        book.setYear(rs.getInt("year"));
        // book.setReaderId(rs.getInt("r_id"));
        Author author = new Author();
        author.setId(rs.getInt("a_id"));
        author.setName(rs.getString("a_name"));
        author.setPatronymic(rs.getString("patronymic"));
        author.setFamilyName(rs.getString("family_name"));
        book.setAuthor(author);
        return book;
    }
    
}
