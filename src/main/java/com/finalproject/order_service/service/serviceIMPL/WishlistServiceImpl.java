package com.finalproject.order_service.service.serviceIMPL;

import com.finalproject.order_service.Repo.WishlistRepository;
import com.finalproject.order_service.dto.request.WishlistRequestDto;
import com.finalproject.order_service.dto.response.ProductResponseDto;
import com.finalproject.order_service.dto.response.WishlistResponseDto;
import com.finalproject.order_service.feingClient.ProductClient;
import com.finalproject.order_service.model.Wishlist;
import com.finalproject.order_service.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private  WishlistRepository wishlistRepository;
    @Autowired
    private ProductClient productClient;

    @Autowired
    public WishlistServiceImpl(WishlistRepository wishlistRepository, ProductClient productClient) {
        this.wishlistRepository = wishlistRepository;
        this.productClient = productClient;
    }

    @Override
    public String addToWishlist(WishlistRequestDto wishlistRequestDto) {
        Long userId = wishlistRequestDto.getUserId();
        Integer productId = wishlistRequestDto.getProductId();

        // Fetch product details from Product Microservice
        ProductResponseDto product = productClient.getProductById(productId);

        if (product == null) {
            throw new RuntimeException("Product not found!");
        }

        // Save product to the wishlist
        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        wishlist.setProductId(productId);
        wishlist.setCreatedAt(LocalDateTime.now());
        wishlistRepository.save(wishlist);

        return "Product added to wishlist successfully.";
    }

    @Override
    public List<WishlistResponseDto> getWishlist(Long userId) {
        List<Wishlist> wishlistItems = wishlistRepository.findByUserId(userId);

        return wishlistItems.stream()
                .map(item -> {
                    ProductResponseDto product = productClient.getProductById(item.getProductId());
                    return new WishlistResponseDto(item.getProductId(), product.getProductName(), product.getProductPrice(), item.getCreatedAt());
                })
                .collect(Collectors.toList());
    }

    @Override
    public String removeFromWishlist(WishlistRequestDto wishlistRequestDto) {
        Long userId = wishlistRequestDto.getUserId();
        Integer productId = wishlistRequestDto.getProductId();

        // Find the wishlist item by userId and productId
        Wishlist wishlistItem = wishlistRepository.findByUserIdAndProductId(userId, productId);

        if (wishlistItem == null) {
            return "Item not found in wishlist.";
        }

        // Remove the item from the wishlist
        wishlistRepository.delete(wishlistItem);

        return "Item removed from wishlist successfully.";
    }
}
