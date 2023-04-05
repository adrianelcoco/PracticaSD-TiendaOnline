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


    @PostMapping("/modificar")
    public String increaseStock(Model model, @RequestParam ("productId") long id, @RequestParam("newStock") String newStock,@RequestParam(value="increaseButton", required=false) String increaseButton,@RequestParam(value="reduceButton",required=false) String reduceButton ){
        if(newStock != "") {
            int realNewStock = Integer.parseInt(newStock);
            List<Stock> stocks = stockService.getStocks();

            if (increaseButton != null) {
                for (Stock s : stocks) {
                    if (s.getProduct().getId() == id) {
                        realNewStock = realNewStock + s.getAmount();
                        if(s.getAmount() <= 0){
                            productService.addProduct(s.getProduct());
                        }
                        s.setAmount(realNewStock);
                    }
                }
            } else if (reduceButton != null) {
                for (Stock s : stocks) {
                    if (s.getProduct().getId() == id) {
                        realNewStock = s.getAmount() - realNewStock;
                        if(realNewStock <= 0){
                            productService.removeProduct(s.getProduct());
                        }
                        s.setAmount(realNewStock);
                    }
                }
            }
            model.addAttribute("stocks", stocks);
            return "stocks_template";
        }
        else{
            List<Stock> stocks = stockService.getStocks();
            model.addAttribute("stocks", stocks);
            return "stocks_template";
        }
    }

    @GetMapping("/")
    public String returnMain(Model model){
        List<Product> products= productService.getProducts();
        model.addAttribute("products", products);
        return "products_template";
    }

}
