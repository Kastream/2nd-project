package com.saulsapp.project_2.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.saulsapp.project_2.models.Author;
import com.saulsapp.project_2.services.AuthorsService;

@Controller
@RequestMapping(value = "/authors")
public class AuthorsController {
    private final AuthorsService authorsService;

    @Autowired
    public AuthorsController(AuthorsService authorsService) {
        this.authorsService = authorsService;
    }
    
    @GetMapping()
    public String getAllAuthors (Model model, @RequestParam(name = "p", defaultValue = "1") int page, @RequestParam(name="q", defaultValue = "25") int size, @RequestParam(name = "s", defaultValue = "familyName") String sort) {
        model.addAttribute("authors", authorsService.getAll(page, size, sort));
        return "authors/index";
    }

    @GetMapping(value = "/{id}")
    public String getAuthor(Model model, @PathVariable(value = "id") int id, @RequestParam(name = "p", defaultValue = "1") int page, @RequestParam(name="q", defaultValue = "25") int size, @RequestParam(name = "s", defaultValue = "name") String s){
        model.addAttribute("author", authorsService.getFull(id, page, size, s));
        // model.addAttribute("books", authorsService.getBooks(id));
        return "authors/showone";
    }

    @GetMapping(value = "/add")
    public String newForm(Model model){
        model.addAttribute("author", new Author());
        return "authors/new";
    }

    @GetMapping(value = "/edit/{id}")
    public String editForm(Model model, @PathVariable(value="id") int id){
        model.addAttribute("author", authorsService.get(id));
        return "authors/edit";
    }

    @PostMapping(value = "/add")
    public String add(@ModelAttribute(value = "author") @Valid Author author, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "authors/new";
        }
        if (authorsService.checkAvailable(author)){
            int id = authorsService.save(author);
            return "redirect:/authors/" + id;
        }
        return "redirect:/authors";
        

    }

    @PatchMapping(value = "/edit")
    public String edit(@ModelAttribute(value="author") @Valid Author author, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "authors/edit";
        }
        if (!authorsService.checkAvailable(author)) {
            bindingResult.addError(new ObjectError("exists", "Такой автор уже существует"));
            return "authors/edit";
        }
        authorsService.save(author);
        return "redirect:/authors/" + author.getId();
    }

    @DeleteMapping()
    public String delete(Model model, int id) {
        authorsService.delete(id);
        return "redirect:/authors";
    }

}
