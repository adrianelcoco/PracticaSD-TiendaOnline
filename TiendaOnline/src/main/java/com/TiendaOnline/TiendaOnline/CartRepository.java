package com.TiendaOnline.TiendaOnline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("SELECT p FROM Cart p JOIN p.products pr WHERE pr.name = :name")
    Cart findByProductName(String name);
    @Query("SELECT p FROM Cart c JOIN c.products p WHERE p.price > :price")
    List<Product>findByPriceGreaterThan(double price);

    @Query("SELECT p FROM Cart c JOIN c.products p ORDER BY p.price ASC")
    List<Product> findAllOrderByPriceAsc();

    @Query("SELECT p FROM Cart c JOIN c.products p ORDER BY p.price DESC")
    List<Product> findAllOrderByPriceDesc();

    @Query("SELECT p FROM Cart c JOIN c.products p ORDER BY p.amountInCart ASC")
    List<Product> findAllOrderByAmountAsc();

    @Query("SELECT p FROM Cart c JOIN c.products p ORDER BY p.amountInCart DESC")
    List<Product> findAllOrderByAmountDesc();
}

