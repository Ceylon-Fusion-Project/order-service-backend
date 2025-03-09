package com.finalproject.order_service.service.serviceIMPL;

import com.finalproject.order_service.Repo.WishlistRepository;
import com.finalproject.order_service.dto.request.WishlistRequestDto;
import com.finalproject.order_service.dto.response.WishlistResponseDto;
import com.finalproject.order_service.model.Wishlist;
import com.finalproject.order_service.service.WishlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    private static final Logger log = LoggerFactory.getLogger(WishlistServiceImpl.class);

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    public WishlistServiceImpl(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public String addToWishlist(WishlistRequestDto wishlistRequestDto) {
        Long userId = wishlistRequestDto.getUserId();
        Integer productId = wishlistRequestDto.getProductId();
        log.info("Adding product to wishlist: " + productId);

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
                .map(item -> new WishlistResponseDto(item.getProductId()))
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
