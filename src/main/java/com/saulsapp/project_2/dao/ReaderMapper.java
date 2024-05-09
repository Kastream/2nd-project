package com.saulsapp.project_2.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.saulsapp.project_2.models.Reader;

public class ReaderMapper implements RowMapper<Reader>{

    @Override
    public Reader mapRow(ResultSet rs, int rowNum) throws SQLException {
        Reader reader = new Reader();
        reader.setId(rs.getInt("id"));
        reader.setName(rs.getString("name"));
        reader.setPatronymic(rs.getString("patronymic"));
        reader.setFamilyName(rs.getString("family_name"));
        // reader.setBirthYear(rs.getInt("birth_year"));
        return reader;
        //TO-DO add book count
    }

    
}
