package com.TiendaOnline.TiendaOnline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/stocks")
public class StockController {
    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;


    @PostMapping("/modify") //Allows the admin to update the stock of a product
    public String updateStock(Model model, @RequestParam ("productId") long id, @RequestParam("newStock") String newStock,@RequestParam(value="increaseButton", required=false) String increaseButton,@RequestParam(value="reduceButton",required=false) String reduceButton ) {

        if (newStock != "") { //If the new stock is not an empty string
            int realNewStock = Integer.parseInt(newStock);

            if (increaseButton != null) { //If the increase button is clicked
                Stock stock = stockService.getStockById(id); //Get the stock of the product
                realNewStock = realNewStock + stock.getAmount(); //Calculates the new stock
                if (stock.getAmount() <= 0) { //If the old stock was less than 0
                    productService.addProduct(stock.getProduct()); //The product is added again
                }
                stock.setAmount(realNewStock);

            }
            else if (reduceButton != null) { //If the reduce button is clicked
                Stock stock = stockService.getStockById(id); //Get the stock of the product
                realNewStock = stock.getAmount() - realNewStock; //Calculates the new stock
                if (realNewStock <= 0) { //If the new stock is equal to 0, the product is removed from the main page (It can't be less than 0 because of a JavaScript file)
                    productService.removeProduct(stock.getProduct());//The product is removed from the main page
                }
                stock.setAmount(realNewStock);
            }
            List<Stock> stocks = stockService.getStocks();
            model.addAttribute("stocks", stocks);
            return "stocks_template";
    }
        else{
            List<Stock> stocks = stockService.getStocks();
            model.addAttribute("stocks", stocks);
            return "stocks_template";
        }
    }

    @GetMapping("/") //Return to the main page
    public String returnMain(Model model){
        List<Product> products= productService.getProducts();
        model.addAttribute("products", products);
        return "products_template";
    }

}
