package com.saulsapp.project_2.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.saulsapp.project_2.models.Reader;
import com.saulsapp.project_2.services.ReadersService;

@Controller
@RequestMapping(value = "/readers")
public class ReadersController{

    private final ReadersService readersService;

    @Autowired
    private ReadersController(ReadersService readersService){
        this.readersService = readersService;
    } 

    @GetMapping()
    public String getAllReaders(Model model, @RequestParam(name = "p", defaultValue = "1") int page, @RequestParam(name="q", defaultValue = "25") int size, @RequestParam(name = "s", defaultValue = "familyName") String s) {
        Page<Reader> readers = readersService.getAll(page, size, s);
        model.addAttribute("page", page);
        model.addAttribute("total", readers.getTotalPages());
        model.addAttribute("readers", readers.getContent());
        return "readers/index";
    }

    @GetMapping("/add")
    public String addReaderForm(Model model){
        model.addAttribute("reader", new Reader());
        return "readers/new";
    }

    @GetMapping("/{id}")
    public String getReader(Model model, @PathVariable(value = "id") int id) {
        model.addAttribute("reader", readersService.getFull(id));
        return "readers/showone";
    }

    @GetMapping("/edit/{id}")
    public String editReader(Model model, @PathVariable(value="id") int id) {
        model.addAttribute("reader", readersService.get(id));
        return "readers/edit";
    }

    @PostMapping("/add")
    public String addReaderForm(@ModelAttribute("reader") @Valid Reader reader, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "readers/new";
        }
        if (readersService.checkAvailable(reader)){
            readersService.addReader(reader);
            return "redirect:/readers";
        }
        return "redirect:/readers";
    }

    @PatchMapping("/edit")
    public String editReader(@ModelAttribute("reader") @Valid Reader reader, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "readers/edit";
        }
        if (!readersService.checkAvailable(reader)){
            bindingResult.addError(new ObjectError("exists", "Такой читатель уже существует"));
            return "readers/edit";
        }
        readersService.addReader(reader);
        return "redirect:/readers/" + reader.getId();
    }

    @DeleteMapping()
    public String deleteReader(Model model, @RequestParam(value = "id") int id) {
        readersService.delete(id);
        
        return "redirect:/readers/";
    }

}