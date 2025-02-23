package ch.fhnw.pizza.data.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pizza")
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "pizza_toppings")
    private String pizzaToppings;

    @Column(name = "pizza_name")
    private String pizzaName;

    //getters, setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPizzaToppings() {
        return pizzaToppings;
    }

    public void setPizzaToppings(String pizzaToppings) {
        this.pizzaToppings = pizzaToppings;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

      
    
}
