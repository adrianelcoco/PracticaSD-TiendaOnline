package com.TiendaOnline.TiendaOnline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private StockService stockService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public String showProducts(Model model) { //Show the main page (list of products)
        List<Product> products= productService.getProducts();
        model.addAttribute("products", products);
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
        productService.updateCountId();
        long newId= productService.getCountId();
        Product newProduct= new Product(newId,name,realPrice);
        productService.addProduct(newProduct);
        stockService.getStocks().add(new Stock(newProduct,realAmount));
        List<Product> products= productService.getProducts();
        model.addAttribute("products", products);
        return "products_template";
    }
    @PostMapping("/stocks") //Show the stocks page
    public String showStocks(Model model){
        List<Stock> stocks= stockService.getStocks();
        model.addAttribute("stocks", stocks);
        return "stocks_template";
    }

    @PostMapping("/cart") //Show the cart page
    public String goCart(Model model){
        List<Product> products= cartService.getProductsInCart();
        model.addAttribute("products", products);
        model.addAttribute("empty",cartService.getEmpty());
        return "cart_template";
    }

    @PostMapping ("/added") //Add a product to a cart
    public String addToCart(Model model, @RequestParam("productId") long id){
        Product product= productService.getProductById(id);
        cartService.addProduct(product);
        cartService.setEmpty(false);
        stockService.reduceStock(product);
        if(stockService.getStock(product).getAmount()==0){ //if after adding the product his stock is 0, it's removed from the main page
            productService.removeProduct(product);
            List<Product> products= productService.getProducts();
            model.addAttribute("products", products);
            return "products_template";
        }
        else{
            List<Product> products= productService.getProducts();
            model.addAttribute("products", products);
            return "products_template";
        }

    }
    @PostMapping ("/modify")//Show the page that allows to modify a product
    public String modifyProduct(Model model, @RequestParam("productId") long id){
        Product product= productService.getProductById(id);
        model.addAttribute("product",product);
        return "modifyProduct_template";
    }
    @PostMapping ("/modified")//Modify a product
    public String modifiedProduct(Model model,@RequestParam("productId") long productId, @RequestParam("name") String name,@RequestParam("price") String price){
        Product product=productService.getProductById(productId);
        if(name.equals("") && price.equals("")){ //If name and price are empty strings, returns to main page with nothing changed
            List<Product> products= productService.getProducts();
            model.addAttribute("products", products);
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
        stockService.updateProduct(productId,product); //Update the information of the stock
        List<Product> products= productService.getProducts();
        model.addAttribute("products", products);
        return "products_template";
    }

    @PostMapping ("/delete") //Delete a product from the main page and his stock
    public String deleteProduct(Model model, @RequestParam("productId") long productId){
        Product product= productService.getProductById(productId);
        productService.removeProduct(product);
        stockService.deleteProduct(productId);
        List<Product> products= productService.getProducts();
        model.addAttribute("products", products);
        return "products_template";
    }

    @PostMapping ("/orders") //Shows the orders list
    public String goOrders(Model model){
        List<Order> orders= orderService.getOrders();
        model.addAttribute("orders",orders);
        return "orders_template";
    }

}