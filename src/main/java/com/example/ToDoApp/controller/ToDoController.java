package com.example.ToDoApp.controller;

import com.example.ToDoApp.model.ToDo;
import com.example.ToDoApp.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class ToDoController {

    @Autowired
    private ToDoService service;

    // UI Controllers
    @GetMapping({"/", "/viewToDoList"})
    public String viewAllToDoItems(Model model, @ModelAttribute("message") String message) {
        model.addAttribute("list", service.getAllToDoItems());
        model.addAttribute("message", message);
        return "ViewToDoList";
    }

    @GetMapping("/updateToDoStatus/{id}")
    public String updateToDoStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (service.updateStatus(id)) {
            redirectAttributes.addFlashAttribute("message", "Update Success");
        } else {
            redirectAttributes.addFlashAttribute("message", "Update Failure");
        }
        return "redirect:/viewToDoList";
    }

    @GetMapping("/addToDoItem")
    public String addToDoItem(Model model) {
        model.addAttribute("todo", new ToDo());
        return "AddToDoItem";
    }

    @PostMapping("/saveToDoItem")
    public String saveToDoItem(ToDo todo, RedirectAttributes redirectAttributes) {
        if (service.saveOrUpdateToDoItem(todo)) {
            redirectAttributes.addFlashAttribute("message", "Save Success");
            return "redirect:/viewToDoList";
        }
        redirectAttributes.addFlashAttribute("message", "Save Failure");
        return "redirect:/addToDoItem";
    }

    @GetMapping("/editToDoItem/{id}")
    public String editToDoItem(@PathVariable Long id, Model model) {
        model.addAttribute("todo", service.getToDoItemById(id));
        return "EditToDoItem";
    }

    @PostMapping("/editSaveToDoItem")
    public String editSaveToDoItem(ToDo todo, RedirectAttributes redirectAttributes) {
        if (service.saveOrUpdateToDoItem(todo)) {
            redirectAttributes.addFlashAttribute("message", "Edit Success");
            return "redirect:/viewToDoList";
        }
        redirectAttributes.addFlashAttribute("message", "Edit Failure");
        return "redirect:/editToDoItem/" + todo.getId();
    }

    @GetMapping("/deleteToDoItem/{id}")
    public String deleteToDoItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (service.deleteToDoItem(id)) {
            redirectAttributes.addFlashAttribute("message", "Delete Success");
        } else {
            redirectAttributes.addFlashAttribute("message", "Delete Failure");
        }
        return "redirect:/viewToDoList";
    }

    // REST API Controllers
    @RestController
    @RequestMapping("/api/todo")
    @CrossOrigin(origins = "*") // allow access from Postman or frontend
    public static class ToDoRestController {

        @Autowired
        private ToDoService service;

        @GetMapping
        public List<ToDo> getAllToDos() {
            return service.getAllToDoItems();
        }

        @GetMapping("/{id}")
        public ToDo getToDoById(@PathVariable Long id) {
            return service.getToDoItemById(id);
        }

        @PostMapping
        public String addToDo(@RequestBody ToDo todo) {
            boolean success = service.saveOrUpdateToDoItem(todo);
            return success ? "Saved successfully" : "Save failed";
        }

        @PutMapping("/{id}")
        public String updateToDo(@PathVariable Long id, @RequestBody ToDo updatedToDo) {
            updatedToDo.setId(id);
            boolean success = service.saveOrUpdateToDoItem(updatedToDo);
            return success ? "Updated successfully" : "Update failed";
        }

        @DeleteMapping("/{id}")
        public String deleteToDo(@PathVariable Long id) {
            boolean success = service.deleteToDoItem(id);
            return success ? "Deleted successfully" : "Delete failed";
        }

        @PatchMapping("/status/{id}")
        public String updateStatusOnly(@PathVariable Long id, @RequestBody Map<String, String> body) {
            String status = body.get("status");
            boolean success = service.updateStatusOnly(id, status);
            return success ? "Status updated" : "Status update failed";
        }
    }
}
