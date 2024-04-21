# Classroom Exercise Instructions

### Pre-requisites
- Import this repository into your own GitHub account.
- Open the repository in Codespaces.
- Import Java Projects.
- Change the visibility of port 8080 to public after starting the application.

## Step 1 - Add Data Access Layer

Notice the project folder structure:
- controller - contains all controller classes
- data > domain - contains all domain entities
- data > repository - contains all repository classes  
- business - contains all (business) service classes

### 1. Create Entity
- Edit the JPA Entity class Pizza inside the domain package
- Add annotations:
    - Add a class-level annotation to indicate that the class is and entity
    - Add a class-level annotation to 
    - Add properties, getter and setters
    ```Java
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
    }
    ```
### 2. Create Repository
- Edit the PizzaRepository.java interface inside the repository package
    - Extend JpaRepository
    - Add annotation @Repository
    - Add custom functions
    ```Java
    @Repository
    //JpaRepository should be typed to the domain class and an ID type
    public interface PizzaRepository extends JpaRepository<Pizza, Long> {
        Pizza findByPizzaName(String pizzaName);
        List<Pizza> findAllByPizzaToppingsContainsIgnoreCase(String topping);
    }
    ```
**Recommendation: Use the IntelliJ plugin <a href="https://www.baeldung.com/jpa-buddy">JPA Buddy</a> to quickly and easily create JPA Entities and Repositories**

## Step 2 - Add Service Layer
For this exercise, the service layer will act as a simple bridge between the data access layer and the controller layer.
- Edit the class MenuService.java inside the business.service package
    - Add annotation @Service
    - Add a dependency injection to PizzaRepository object (pizzaRepository)
    ```Java
    @Service
    public class MenuService {

        @Autowired
        private PizzaRepository pizzaRepository;
    }
    ```
    - Add service methods to add and read data from the DB using PizzaRepository: 
    a. Method findPizzaById: 
    ```Java
    public Pizza findPizzaById(Long id) {
        Pizza pizza = pizzaRepository.findById(id).get();
        return pizza;
    }
    ```
    b. Method getAllPizzas:
    ```Java
    public List<Pizza> getAllPizzas() {
        List<Pizza> pizzaList = pizzaRepository.findAll();
        return pizzaList;
    }
    ```
    c. Method addPizza:
    ```Java
    public Pizza addPizza(Pizza pizza) throws Exception {
        if(pizza.getPizzaName() != null) {
            if (pizzaRepository.findByPizzaName(pizza.getPizzaName()) == null)
                return pizzaRepository.save(pizza);
            throw new Exception("Pizza " + pizza.getPizzaName() + " already exists");
        }
        throw new Exception("Invalid pizza name ");
    }
    ```

    ## Step 3 - Modify the Controller(s)
    - Adapt the methods in the MenuController.java to accept input from HTTP requests and read/add data from/to the database
    - Add dependency injection to MenuService object (menuService)
    ```Java
    @Autowired
    private MenuService menuService;
    ```
    - Modify method getPizza
    ```Java
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
    ```
    - Modify method getPizzaList
    ```Java
    @GetMapping(path="/pizzas", produces = "application/json")
    public List<Pizza> getPizzaList() {
        List<Pizza> pizzaList = menuService.getAllPizzas();

        return pizzaList;
    }
    ```
    - Modify method addPizza
    ```Java
    @PostMapping(path="/pizzas", consumes="application/json", produces = "application/json")
    public ResponseEntity<Pizza> addPizza(@RequestBody Pizza pizza) {
        try{
            pizza = menuService.addPizza(pizza);
            
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        }
        return ResponseEntity.ok(pizza);
        
    }
    ```
    - Modify other methods updatePizza and deletePizza

    ## Step 4 - Add Mapping Between Entities
    - Create a new Entity called Menu.java inside the domain package
        - No CRUD operations will be performed, hence no JpaRepository needed
        - Add an id of type Long and appropriate annotations
        - Add a List property pizzaList and corresponding getter setter methods
        - Add one-to-many mapping between Menu and Pizza entities
    - Add annotations
    ```Java
    @Entity
    public class Menu {

        @Id
        @JsonIgnore
        private Long id;

        @OneToMany(mappedBy = "menu")
        private List<Pizza> pizzaList;

        //Getters/setters
    }
    ```
    ```Java
    @Entity
    @Table(name = "pizza")
    public class Pizza {

        //other properties

        @ManyToOne
        private Menu menu;

        //Getters/setters
    }
    ```
        

    ## Step 5 - Add Business Logic
    - In the entity Menu:
        - Add a String property currentOffer and corresponding getter setter methods
        ```Java
        @Entity
        public class Menu {

            //other properties

            private String currentOffer;

            //Getters/setters
        ```
    - In MenuService:
        - Add a private method getCurrentOffer in MenuService that defines location-specific offers:
        ```Java
        //Business Logic to get current offer according to the location of the user requesting the menu
        private String getCurrentOffer(String location) {
            String currentOffer = "No special offer";
            if("Basel".equalsIgnoreCase(location))
                currentOffer = "10% off on all large pizzas!!!";
            else if("Brugg".equalsIgnoreCase(location))
                currentOffer = "Two for the price of One on all small pizzas!!!";
            return currentOffer;
        }
        ```
        - Add a public method getMenuByLocation to include all pizzas and the current offer in the Menu object:
        ```Java
        public Menu getMenuByLocation(String location) {
        String currentOffer = getCurrentOffer(location);
        List<Pizza> pizzaList = getAllPizzas();
        Menu menu = new Menu();
        menu.setPizzaList(pizzaList);
        menu.setCurrentOffer(currentOffer);
        return menu;
        }
        ```
    - In MenuController:
        - Add a method with GET mapping and location as the Request Parameter:
        ```Java
        @GetMapping(path="", produces = "application/json")
        public Menu getMenu(@RequestParam String location) {

            return menuService.getMenuByLocation(location);
        }
        ```
