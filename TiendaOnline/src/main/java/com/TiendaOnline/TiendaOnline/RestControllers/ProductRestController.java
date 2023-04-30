package com.TiendaOnline.TiendaOnline.RestControllers;

import com.TiendaOnline.TiendaOnline.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductRestController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartService cartService;
    @Autowired
    CartRepository cartRepository;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable long id){
        Optional<Product> product=productRepository.findById(id);
        if(product.isPresent()){
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
    @PostMapping("/createProduct")
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestParam String name, @RequestParam double price,@RequestParam int amount){

        Product product= new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(amount);
        productRepository.save(product);

        return product;
    }
    @PutMapping("/modify/{id}")
    public ResponseEntity<Product> modifyProduct(@PathVariable long id,@RequestParam String name, @RequestParam String price){
        Product product=productRepository.findById(id).get();
        if(product == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            String oldName=product.getName();
            Double oldPrice=product.getPrice();
            if(name.equals("")){
                product.setName(oldName);
                product.setPrice(Double.parseDouble(price));

            }
            else if(price.equals("")){
                product.setName(name);
                product.setPrice(oldPrice);

            }
            else{
                product.setName(name);
                product.setPrice(Double.parseDouble(price));
            }
            productRepository.save(product);
            return new ResponseEntity<>(product,HttpStatus.OK);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable long id){
        Product product=productRepository.findById(id).get();
        if(product == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            if(cartRepository.findAll().size()!=0){
                Cart cart= cartRepository.findAll().get(0);
                List<Product> productsInCart=cart.getProducts();
                for(Product p: productsInCart){
                    if(p.getId()==id){
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
            productRepository.delete(product);
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }

    @GetMapping("/cart")
    public Cart getCart(){
        return cartRepository.findAll().get(0);
    }
    @PostMapping("/cart/created")
    public ResponseEntity<Cart> createCart(){
        if(cartRepository.findAll().isEmpty()){
            Cart cart=new Cart(true);
            cartRepository.save(cart);
            return new ResponseEntity<>(cart,HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/added/{id}")
    public ResponseEntity<Cart> addToCart(@PathVariable long id){
        Optional<Product> product = productRepository.findById(id);
        if(!product.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            Cart cart=cartRepository.findAll().get(0);
            List<Product> productsInCart=cart.getProducts();
            if(!productsInCart.contains(product.get())){
                productsInCart.add(product.get());
            }
            cart.setEmpty(false);
            cartRepository.save(cart);
            product.get().decreaseStock(1);
            product.get().increaseAmountCart(1);
            productRepository.save(product.get());
            return new ResponseEntity<>(cart,HttpStatus.OK);
        }
    }
    @PutMapping("/stocks/modify/{id}")
    private ResponseEntity<Product> updateStock(@PathVariable long id, @RequestParam int newStock, @RequestParam String mode){
        Optional<Product> product = productRepository.findById(id);
        if(!product.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            if(mode.equals("aumentar")){
                newStock = newStock + product.get().getStock();
                product.get().setStock(newStock);
                return new ResponseEntity<>(product.get(),HttpStatus.OK);
            } else if (mode.equals("reducir")) {
                newStock = product.get().getStock() - newStock;

                if (newStock<0) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                product.get().setStock(newStock);
                return new ResponseEntity<>(product.get(),HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

    }


    @GetMapping("/order")
    public List<OrderEntity> getOrders(){
        return orderRepository.findAll();
    }

    @GetMapping("/product/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        Product product = productRepository.findByNameIgnoreCase(name);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/product/price/{price}")
    public ResponseEntity<Product> getProductByPrice(@PathVariable double price) {
        Product product = productRepository.findByPrice(price);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/products/priceLessThan/{price}")
    public List<Product> getByPriceLessThanEqual(@PathVariable double price) {
        return productRepository.findByPriceLessThanEqual(price);

    }

    @GetMapping("")
    public List<Product> getByNameContainingIgnoreCase(@PathVariable String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @GetMapping("/product/byStockBetween/{minStock}{maxStock}")
    public List<Product> getByStockBetween(@PathVariable int minStock, @PathVariable int maxStock) {
        return productRepository.findByStockBetween(minStock, maxStock);
    }


    @GetMapping("/products/amountInCart")
    public List<Product> getProductInCart() {
        return productRepository.findByAmountInCartIsTrue();

    }

    @GetMapping("/products/orderBy/nameAsc")
    public List<Product> getAllProductsOrderByNameAsc() {
        return productRepository.OrderByNameAsc();
    }

    @GetMapping("/products/orderBy/nameDesc")
    public List<Product> getAllProductsOrderByNameDesc() {
        return productRepository.OrderByNameDesc();
    }

    @GetMapping("/products/orderBy/priceAsc")
    public List<Product> getAllProductsOrderByPriceAsc() {
        return productRepository.OrderByPriceAsc();
    }

    @GetMapping("/products/orderBy/priceDesc")
    public List<Product> getAllProductsOrderByPriceDesc() {
        return productRepository.OrderByPriceDesc();
    }

    @GetMapping("/products/orderBy/stockAsc")
    public List<Product> getAllProductsOrderByStockAsc() {
        return productRepository.OrderByStockAsc();
    }

    @GetMapping("/products/orderBy/stockDesc")
    public List<Product> getAllProductsOrderByStockDesc() {
        return productRepository.OrderByStockDesc();
    }

    @GetMapping("/products/stock/greaterThan/{stock}")
    public List<Product> getAllProductsByStockGreaterThan(@PathVariable int stock) {
        return productRepository.findByStockGreaterThan(stock);
    }
}
