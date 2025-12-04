package com.mainapp.service;

import com.mainapp.dto.ProductDTO;
import com.mainapp.exceptions.NotEnoughMoneyException;
import com.mainapp.model.entity.Product;
import com.mainapp.model.entity.User;
import com.mainapp.repo.ProductRepository;
import com.mainapp.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.rmi.server.LogStream.log;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    @Autowired
    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Cacheable("unordered_products")
    public List<Product> getByUnordered() throws RuntimeException{
        return this.productRepository.findAllByIsOrderedFalse().orElseThrow(() ->
                new RuntimeException("Cant find the unordered products"));
    }

    @CacheEvict(value = "unordered_products", allEntries = true)
    public void createProduct(ProductDTO productDTO){
      this.productRepository.save(mapToProduct(productDTO));
    };

    @Scheduled(cron = "*/5 * * * * ?")
    public void logRunning() {
        log("The system is running");
    }

    @Scheduled(fixedDelay = 5000)
    public void tick() {
        System.out.println("tick tick tick...");
    }

    public Product mapToProduct(ProductDTO productDTO){
        Product product = new Product();
        product.setOrdered(false);
        product.setCreatedOn(LocalDate.now());
        product.setUpdatedOn(LocalDate.now());
        product.setName(productDTO.getName());
        product.setImage("");
        product.setPrice(productDTO.getPrice());
        product.setQuality(productDTO.getQuality());
        return product;
    }

    public String updateProduct(){
        return "couldnt update product";
    }

    public String deleteProduct(){
        return "Couldnt delete product";
    }

    @CacheEvict(value = "unordered_products", allEntries = true)
    public String createOrder(UUID id, User user) throws NotEnoughMoneyException{
        Product product = productRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Cant find the product"));

        if(product.getPrice() > user.getMoney()){
            throw new NotEnoughMoneyException("User doesnt have enough money");
        }
        else{
            user.setMoney(user.getMoney() - product.getPrice());
            product.setOrdered(true);
            product.setUser(user);
            user.getOrderedProducts().add(product);

            productRepository.save(product);
            userRepository.save(user);
        }
        return "you dont have enough money to buy the product";
    }
}
