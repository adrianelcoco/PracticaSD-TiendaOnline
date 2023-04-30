package com.TiendaOnline.TiendaOnline;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findByNameIgnoreCase(String name);
    Product findByPrice(Double price);

    List<Product> OrderByNameAsc();
    List<Product> OrderByNameDesc();

    List<Product> OrderByPriceAsc();
    List<Product> OrderByPriceDesc();

    List<Product> OrderByStockAsc();
    List<Product> OrderByStockDesc();
    List<Product> findByStockGreaterThan(int stock);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPriceLessThanEqual(double price);
    List<Product> findByStockBetween(int minStock, int maxStock);
    List<Product> findByAmountInCartIsTrue();


}
