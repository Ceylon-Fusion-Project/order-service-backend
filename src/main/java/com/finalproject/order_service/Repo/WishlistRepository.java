package com.finalproject.order_service.Repo;

import com.finalproject.order_service.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    List<Wishlist> findByUserId(Long userId);
    Wishlist findByUserIdAndProductId(Long userId, Long productId);

}
