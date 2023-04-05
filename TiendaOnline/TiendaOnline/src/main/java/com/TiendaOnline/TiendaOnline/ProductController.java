package com.TiendaOnline.TiendaOnline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    private int countId=300;
    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private StockService stockService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public String showProducts(Model model) {
        List<Product> products= productService.getProducts();
        model.addAttribute("products", products);
        return "products_template";
    }
    @GetMapping("/createproduct")
    public String goCreateProduct(Model model) {
        return "newproduct_template";
    }
    @PostMapping("/")
    public String createProduct(Model model,@RequestParam("name") String name, @RequestParam("price") String price, @RequestParam("amount") String amount) {
        double realPrice= Double.parseDouble(price);
        int realAmount= Integer.parseInt(amount);
        int size= productService.getProducts().size();
        long newId= countId+100;
        countId=countId+100;
        Product newProduct= new Product(newId,name,realPrice);
        productService.addProduct(newProduct);
        stockService.getStocks().add(new Stock(newProduct,realAmount));
        List<Product> products= productService.getProducts();
        model.addAttribute("products", products);
        return "products_template";
    }
    @GetMapping("/stocks")
    public String showStocks(Model model){
        List<Stock> stocks= stockService.getStocks();
        model.addAttribute("stocks", stocks);
        return "stocks_template";
    }

    @GetMapping("/cart")
    public String goCart(Model model){
        List<Product> products= cartService.getProductsInCart();
        model.addAttribute("products", products);
        model.addAttribute("empty",cartService.getEmpty());
        return "cart_template";
    }

    @PostMapping ("/added")
    public String addToCart(Model model, @RequestParam("productId") long id){
        Product product= productService.getProductById(id);
        cartService.addProduct(product);
        cartService.setEmpty(false);
        stockService.reduceStock(product);
        if(stockService.getStock(product).getAmount()==0){
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
    @PostMapping ("/modify")
    public String modifyProduct(Model model, @RequestParam("productId") long id){
        Product product= productService.getProductById(id);
        model.addAttribute("product",product);
        return "modifyProduct_template";
    }
    @PostMapping ("/aply")
    public String aplyProduct(Model model,@RequestParam("productId") long productId, @RequestParam("name") String name,@RequestParam("price") String price){
        Product product=productService.getProductById(productId);
        if(name.equals("") && price.equals("")){
            List<Product> products= productService.getProducts();
            model.addAttribute("products", products);
            return "products_template";
        }
        else if(name.equals("")){
            product.setPrice(Double.parseDouble(price));
        }
        else if (price.equals("")) {
            product.setName(name);
        }
        else{
            product.setName(name);
            product.setPrice(Double.parseDouble(price));
        }
        stockService.updateProduct(productId,product);
        List<Product> products= productService.getProducts();
        model.addAttribute("products", products);
        return "products_template";
    }

    @PostMapping ("/delete")
    public String deleteProduct(Model model, @RequestParam("productId") long productId){
        Product product= productService.getProductById(productId);
        productService.removeProduct(product);
        stockService.deleteProduct(productId);
        List<Product> products= productService.getProducts();
        model.addAttribute("products", products);
        return "products_template";
    }

    @GetMapping ("/orders")
    public String goOrders(Model model){
        List<Order> orders= orderService.getOrders();
        model.addAttribute("orders",orders);
        return "orders_template";
    }

}