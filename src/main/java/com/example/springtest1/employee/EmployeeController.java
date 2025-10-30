package com.example.springtest1.employee;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public String listEmployees(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Employee> employees;
        if (keyword != null && !keyword.isBlank()) {
            employees = employeeRepository.findByNameContainingIgnoreCase(keyword);
        } else {
            employees = employeeRepository.findAll();
        }

        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        return "employees/index";
    }


    @PostMapping
    public String createEmployee(
            @RequestParam("name") String name,
            @RequestParam("age") Integer age,
            @RequestParam("salary") BigDecimal salary,
            RedirectAttributes ra) {

        Employee employee = new Employee();
        employee.setName(name);
        employee.setAge(age);
        employee.setSalary(salary);

        employeeRepository.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    model.addAttribute("employee", employee);
                    return "employees/form";
                })
                .orElseGet(() -> {
                    return "redirect:/employees";
                });
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("employee")) {
            model.addAttribute("employee", new Employee());
        }
        return "employees/form";
    }

    @PostMapping("/{id}")
    public String updateEmployee(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("age") Integer age,
            @RequestParam("salary") BigDecimal salary,
            RedirectAttributes ra) {

        Employee existing = employeeRepository.findById(id).orElse(null);
        if (existing == null) {
            ra.addFlashAttribute("message", "Employee not found.");
            return "redirect:/employees";
        }

        existing.setName(name);
        existing.setAge(age);
        existing.setSalary(salary);
        employeeRepository.save(existing);
        return "redirect:/employees";
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        }
        return "redirect:/employees";
    }
}
