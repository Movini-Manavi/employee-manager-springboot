package com.example.employee_manager.controller;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Web pages (Thymeleaf): the form + table, and the edit page. */
@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // http://localhost:8080  ->  go straight to the employees page
    @GetMapping("/")
    public String home() {
        return "redirect:/employees";
    }

    // READ: show the form and the table (optional ?keyword= for Task 11)
    @GetMapping("/employees")
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("employee", new Employee());
        addTableData(model, keyword);
        return "employees";
    }

    // CREATE: save a new employee
    @PostMapping("/employees")
    public String create(@Valid @ModelAttribute("employee") Employee employee,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {

        if (!result.hasFieldErrors("email") && employeeService.emailExists(employee.getEmail(), null)) {
            result.rejectValue("email", "duplicate", "This email is already registered");
        }
        if (result.hasErrors()) {
            addTableData(model, null);
            return "employees"; // show the same page with red error messages
        }

        employeeService.save(employee);
        redirect.addFlashAttribute("success", "Employee saved successfully");
        return "redirect:/employees"; // Post-Redirect-Get
    }

    // Show the edit form filled with the employee's data
    @GetMapping("/employees/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        return "edit-employee";
    }

    // UPDATE: save the changes
    @PostMapping("/employees/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("employee") Employee employee,
                         BindingResult result,
                         RedirectAttributes redirect) {

        employee.setId(id);
        if (!result.hasFieldErrors("email") && employeeService.emailExists(employee.getEmail(), id)) {
            result.rejectValue("email", "duplicate", "This email is already registered");
        }
        if (result.hasErrors()) {
            return "edit-employee";
        }

        employeeService.update(id, employee);
        redirect.addFlashAttribute("success", "Employee updated successfully");
        return "redirect:/employees";
    }

    // DELETE: remove the employee
    @PostMapping("/employees/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        employeeService.delete(id);
        redirect.addFlashAttribute("success", "Employee deleted successfully");
        return "redirect:/employees";
    }

    private void addTableData(Model model, String keyword) {
        model.addAttribute("employees", employeeService.search(keyword));
        model.addAttribute("count", employeeService.count());
        model.addAttribute("keyword", keyword);
    }
}