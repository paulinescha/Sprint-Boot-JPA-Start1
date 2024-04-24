package ch.fhnw.pizza.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.fhnw.pizza.data.domain.Pizza;
import ch.fhnw.pizza.data.repository.PizzaRepository;

@Service
public class MenuService {

    @Autowired // This annotation is used to let Spring know that it should inject
                // an instance of PizzaRepository into this field.
    private PizzaRepository pizzaRepository;

    public Pizza findPizzaById(Long id) {
        Pizza pizza = pizzaRepository.findById(id).get();
        return pizza;
    }
    
    public List<Pizza> getAllPizzas() {
        List<Pizza> pizzaList = pizzaRepository.findAll();
        return pizzaList;
    }

    public Pizza addPizza(Pizza pizza) throws Exception {
        if(pizza.getPizzaName() != null) {
            if (pizzaRepository.findByPizzaName(pizza.getPizzaName()) == null)
                return pizzaRepository.save(pizza);
            throw new Exception("Pizza " + pizza.getPizzaName() + " already exists");
        }
        throw new Exception("Invalid pizza name ");
    }
}
