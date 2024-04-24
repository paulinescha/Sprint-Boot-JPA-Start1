package ch.fhnw.pizza.controller;

import ch.fhnw.pizza.business.service.MenuService;
import ch.fhnw.pizza.data.domain.Pizza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/menu")
public class MenuController {

    @Autowired
private MenuService menuService;

    @GetMapping(path="/pizzas/{id}", produces = "application/json")
    public ResponseEntity<Pizza> getPizza(@PathVariable Long id) {
        try{
            Pizza pizza = menuService.findPizzaById(id);
            return ResponseEntity.ok(pizza);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping(path="/pizzas", produces = "application/json")
    public List<Pizza> getPizzaList() {
        
        List<Pizza> pizzaList = menuService.getAllPizzas();
        return pizzaList;
    }

    @PostMapping(path="/pizzas", consumes="application/json", produces = "application/json")
    public ResponseEntity<Pizza> addPizza(@RequestBody Pizza pizza) {
        try{
            pizza = menuService.addPizza(pizza);
            
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
    
        }
        return ResponseEntity.ok(pizza);
        
    }
    
}
