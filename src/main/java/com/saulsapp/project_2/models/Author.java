package com.saulsapp.project_2.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;


@Entity
@Table(name = "author")
public class Author {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    
    @Pattern(regexp = "[А-Я][а-я]{1,14}", message="Имя с большой буквы, от 2 до 15 символов")
    private String name;
    
    @Pattern(regexp = "[А-Я][а-я]{4,14}|^(?![\\s\\S])", message="Отчество с большой буквы, от 5 до 15 символов или пустое")
    private String patronymic;

    @Column(name = "family_name")
    @Pattern(regexp = "[А-Я][а-я]{1,14}", message="Фамилия с большой буквы, от 2 до 15 символов")
    private String familyName;

    @OneToMany(mappedBy = "author")
    private List<Book> books;

    
    public Author (){
        
    }
    
    public String getShortName() {
        return this.name.substring(0, 1) + (this.patronymic.length() > 0? ("." + this.patronymic.substring(0, 1)): "") + ". " + this.familyName;
    }
    
    public String getFullName() {
        return this.name + " " + (this.patronymic.length() > 0? (this.patronymic + " " ): "") + this.familyName;
    }
    
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
    
    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
}
