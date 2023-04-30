package com.TiendaOnline.TiendaOnline;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
    @Query("SELECT p FROM OrderEntity o JOIN o.products p WHERE o.id = :orderId AND p.price BETWEEN :minPrice AND :maxPrice ORDER BY p.price ASC")
    List<Product> findProductsByOrderIdAndPriceRange(Long orderId, double minPrice, double maxPrice);
    @Query("SELECT p FROM OrderEntity p WHERE p.name = :name")
    List<OrderEntity> findByName(String name);
    @Query("SELECT p.products FROM OrderEntity p WHERE p.id = :orderId")
    List<OrderEntity> findProductsByOrderId(Long orderId);
    @Query("SELECT p FROM OrderEntity p JOIN p.products pr WHERE pr.name = :name")
    List<OrderEntity> findByProductName(String name);

    List<OrderEntity> OrderByNameAsc();
    List<OrderEntity> OrderByNameDesc();

    List<OrderEntity> OrderByTotalPriceAsc();
    List<OrderEntity> OrderByTotalPriceDesc();

    List<OrderEntity> OrderByStreetAsc();
    List<OrderEntity> OrderByStreetDesc();

    List<OrderEntity> OrderByPayMethodAsc();
    List<OrderEntity> OrderByPayMethodDesc();
    List<OrderEntity> findByPayMethod(String payMethod);
    OrderEntity findByStreet(String street);
    OrderEntity findByTotalPrice(Double totalPrice);
}
