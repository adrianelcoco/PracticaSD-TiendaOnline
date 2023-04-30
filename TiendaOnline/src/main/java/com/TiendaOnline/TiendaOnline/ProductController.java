package com.TiendaOnline.TiendaOnline;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;
    @PostConstruct
    public void init() {

    }
    @GetMapping("/")
    public String showProducts(Model model) { //Show the main page (list of products)
        List<Product> products= productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("cart",cartRepository.findAll());
        return "products_template";
    }
    @PostMapping ("/createproduct") //Show the create products page
    public String goCreateProduct() {
        return "newproduct_template";
    }
    @PostMapping("/") //Create a new product and return to the main page
    public String createProduct(Model model,@RequestParam("name") String name, @RequestParam("price") String price, @RequestParam("amount") String amount) {
        double realPrice= Double.parseDouble(price);
        int realAmount= Integer.parseInt(amount);
        Product newProduct= new Product(name,realPrice,realAmount);
        productRepository.save(newProduct);
        List<Product> products= productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("cart",cartRepository.findAll());
        return "products_template";
    }
    @PostMapping("/stocks") //Show the stocks page
    public String showStocks(Model model){
        List<Product> products= productRepository.findAll();
        model.addAttribute("products", products);
        return "stocks_template";
    }

    @PostMapping("/cart") //Show the cart page
    public String goCart(Model model){
        Cart cart= cartRepository.findAll().get(0);
        List<Product> products= cart.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("empty",cart.getEmpty());
        return "cart_template";
    }
    @PostMapping("/cart/created") //Show the cart page
    public String createCart(Model model){
        Cart cart= new Cart();
        cart.setEmpty(true);
        cartRepository.save(cart);
        List<Product> products= cart.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("empty",cart.getEmpty());
        return "cart_template";
    }


    @PostMapping ("/added") //Add a product to a cart
    public String addToCart(Model model, @RequestParam("productId") long id){
        Product product= productRepository.findById(id).get();
        Cart cart= cartRepository.findAll().get(0);
        if(!cart.getProducts().contains(product)){
            cart.getProducts().add(product);
        }
        cart.setEmpty(false);
        cartRepository.save(cart);
        product.decreaseStock(1);
        product.increaseAmountCart(1);
        productRepository.save(product);
        List<Product> products= productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("cart",cartRepository.findAll());
        return "products_template";
    }
    @PostMapping ("/modify")//Show the page that allows to modify a product
    public String modifyProduct(Model model, @RequestParam("productId") long id){
        Product product= productRepository.findById(id).get();
        model.addAttribute("product",product);
        return "modifyProduct_template";
    }
    @PostMapping ("/modified")//Modify a product
    public String modifiedProduct(Model model,@RequestParam("productId") long productId, @RequestParam("name") String name,@RequestParam("price") String price){
        Product product= productRepository.findById(productId).get();
        if(name.equals("") && price.equals("")){ //If name and price are empty strings, returns to main page with nothing changed
            List<Product> products= productRepository.findAll();
            model.addAttribute("products", products);
            model.addAttribute("cart",cartRepository.findAll());
            return "products_template";
        }
        else if(name.equals("")){ //If name is an empty string, only changes the price
            product.setPrice(Double.parseDouble(price));
        }
        else if (price.equals("")) { //If price is an empty string, only changes the name
            product.setName(name);
        }
        else{ //Change both attributes
            product.setName(name);
            product.setPrice(Double.parseDouble(price));
        }
        productRepository.save(product);
        List<Product> products= productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("cart",cartRepository.findAll());
        return "products_template";
    }

    @PostMapping ("/delete") //Delete a product from the main page and his stock
    public String deleteProduct(Model model, @RequestParam("productId") long productId){
        Product product=productRepository.findById(productId).get();
        if(cartRepository.findAll().size()!=0){
            Cart cart= cartRepository.findAll().get(0);
            List<Product> productsInCart=cart.getProducts();
            for(Product p: productsInCart){
                if(p.getId()==productId){
                    productsInCart.remove(p);
                    break;
                }
            }
            if(productsInCart.size()==0){
                cart.setEmpty(true);
            }
            cartRepository.save(cart);
        }
        if(orderRepository.findAll().size()!=0){
            List<OrderEntity> orders=orderRepository.findAll();
            for(OrderEntity o:orders){
                if(o.getProducts().contains(product)){
                    int i=o.getProducts().indexOf(product);
                    o.getProducts().remove(product);
                    o.getAmountProducts().remove(i);
                }
            }
        }
        productRepository.deleteById(productId);
        List<Product> products= productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("cart",cartRepository.findAll());
        return "products_template";
    }

    @PostMapping ("/orders") //Shows the orders list
    public String goOrders(Model model){
        List<OrderEntity> orders= orderRepository.findAll();
        model.addAttribute("orders",orders);
        return "orders_template";
    }
    @PostMapping("/stocks/modify") //Allows the admin to update the stock of a product
    public String updateStock(Model model, @RequestParam ("productId") long id, @RequestParam("newStock") String newStock,@RequestParam(value="increaseButton", required=false) String increaseButton,@RequestParam(value="reduceButton",required=false) String reduceButton ) {

        if (newStock != "") { //If the new stock is not an empty string
            int realNewStock = Integer.parseInt(newStock);

            if (increaseButton != null) { //If the increase button is clicked
                Product product=productRepository.findById(id).get();

                realNewStock = realNewStock + product.getStock(); //Calculates the new stock
                product.setStock(realNewStock);
                productRepository.save(product);
            }
            else if (reduceButton != null) { //If the reduce button is clicked
                Product product=productRepository.findById(id).get();
                realNewStock = product.getStock() - realNewStock; //Calculates the new stock
                product.setStock(realNewStock);
                productRepository.save(product);
            }
            List<Product> products = productRepository.findAll();
            model.addAttribute("products", products);
            return "stocks_template";
        }
        else{
            List<Product> products = productRepository.findAll();
            model.addAttribute("products", products);
            return "stocks_template";
        }
    }
}