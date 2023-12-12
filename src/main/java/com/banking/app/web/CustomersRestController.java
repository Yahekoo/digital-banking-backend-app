package com.banking.app.web;

import com.banking.app.DTO.CustomerDTO;
import com.banking.app.entities.Customer;
import com.banking.app.exceptions.CustomerNotFoundException;
import com.banking.app.services.BankingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor @Slf4j
@CrossOrigin("*")
public class CustomersRestController {

    private BankingService bankingService;

    @GetMapping("/customers/searchCustomer")
    public List<CustomerDTO> searchCustomers(@RequestParam(name="keyword", defaultValue = "") String keyword) throws CustomerNotFoundException {
        return bankingService.getCustomerByName(keyword);
    }

    @GetMapping("/customers")
    public List<CustomerDTO> customers() {
        return bankingService.getCustomers();
    }
    @GetMapping("/customers/{id}")
    public CustomerDTO customer(@PathVariable Long id) throws CustomerNotFoundException {
        return bankingService.getCustomer(id);
    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody  CustomerDTO customer) {
        return bankingService.saveCustomer(customer);
    }
    @PutMapping("/customers/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        customerDTO.setId(id);
        return bankingService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        bankingService.deleteCustomer(id);
    }
}
