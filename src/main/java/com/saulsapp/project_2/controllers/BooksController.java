package com.saulsapp.project_2.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.saulsapp.project_2.models.Book;
import com.saulsapp.project_2.models.Reader;
import com.saulsapp.project_2.services.AuthorsService;
import com.saulsapp.project_2.services.BooksService;
import com.saulsapp.project_2.services.ReadersService;

@Controller
@RequestMapping(value = "/books")
public class BooksController {
    private final AuthorsService authorsService;
    private final BooksService booksService;
    private final ReadersService readersService;
    private Map<String, String> sortFields = new HashMap<>();
    
    @PostConstruct
    private void init() {
        sortFields.put("Год печати", "year");
        sortFields.put("Название", "name");
        sortFields.put("Автор", "author");
    }

    @Autowired
    public BooksController(AuthorsService authorsService, BooksService booksService, ReadersService readersService) {
        this.authorsService = authorsService;
        this.booksService = booksService;
        this.readersService = readersService;
    }
    

    @GetMapping()
    public String getAllBooks(Model model, @RequestParam(name = "p", defaultValue = "1") int page, @RequestParam(name="q", defaultValue = "25") int size, @RequestParam(name = "s", defaultValue = "name") String sort) {
        Page<Book> bookPage = booksService.getAll(page, size, sort);
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("page", page);
        model.addAttribute("total", bookPage.getTotalPages());
        model.addAttribute("sort", sortFields);
        model.addAttribute("sortedBy", sort);
        return "books/index";

    }

    @GetMapping(value="/{id}")
    public String getBook(Model model, @PathVariable(value = "id") int id) {
        model.addAttribute("book", booksService.get(id));
        return "books/showone";
    }

    @GetMapping(value = "/assign/{id}")
    public String assignForm(Model model, @PathVariable(value="id") int id, @ModelAttribute(value = "reader") Reader reader, @RequestParam(value = "search", defaultValue = "") String search) {
        if (search.length()>0){
            model.addAttribute("readers", readersService.findByFamilyNameStartingWith(search, Sort.by("familyName")));
        }
        model.addAttribute("book", booksService.get(id));
        return "books/assign";
    }

    @GetMapping(value = "/edit/{id}")
    public String editForm(Model model, @PathVariable(value="id") int id){
        model.addAttribute("authors", authorsService.getAll());
        model.addAttribute("book", booksService.get(id));
        return "books/edit";
    }

    @GetMapping(value = "/add")
    public String newForm(Model model){
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorsService.getAll());
        return "books/new";
    }
    
    @GetMapping(value = "/add/{id}")
    public String newFormId(Model model, @PathVariable(value="id") int id){
        Book book = new Book();
        book.setAuthor(authorsService.get(id));
        model.addAttribute("book", book);
        model.addAttribute("a_id", id);
        return "books/new";
    }

    @GetMapping(value="/search")
    public String searchForm(Model model) {
        return "books/search";
    }

    @PatchMapping(value = "/edit")
    public String edit(Model model, @ModelAttribute(value = "book") @Valid Book book, @RequestParam(value="readerId", required = false) Integer r_id, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getFieldErrors());
            return "books/edit";
        }
        if (r_id != null) {
            book.setReader(new Reader(r_id));
        }
        booksService.edit(book);
        return "redirect:/books/" + book.getId();
    }
    
    @PatchMapping("/releaseread/{id}")
    public String releaseBookReader (@PathVariable(value = "id") int id, @RequestParam(value = "readerId") int readerId) {
        booksService.release(id);
        return "redirect:/readers/" + readerId;
    }
    
    @PatchMapping("/releasebook/{id}")
    public String releaseBookBook (@PathVariable(value = "id") int id) {
        booksService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/assign")
    public String assign(@ModelAttribute(value = "reader") Reader reader, @RequestParam(value = "b_id") int id){
        booksService.assign(id, reader.getId());
        return "redirect:/books/" + id;
    }

    @PostMapping(value = "/add")
    public String add(@ModelAttribute(value="book") @Valid Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "books/new";
        }
        int b_id = booksService.add(book);
        return "redirect:/books/" + b_id;
    }

    @PostMapping(value = "/search")
    public String search(Model model, @RequestParam(value = "search") String search) {
        model.addAttribute("books", booksService.search(search));
        return "books/search";
    }

    @DeleteMapping()
    public String delete(@RequestParam(value="id") int id){
        booksService.delete(id);
        return "redirect:/books";
    }
}
