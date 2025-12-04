package com.mainapp.service;

import com.mainapp.RoleRequestClient;
import com.mainapp.dto.UserDTO;
import com.mainapp.exceptions.UserException;
import com.mainapp.model.entity.Product;
import com.mainapp.model.entity.User;
import com.mainapp.model.entity.UserRole;
import com.mainapp.repo.ProductRepository;
import com.mainapp.repo.UserRepository;
import com.mainapp.security.UserData;
import jakarta.persistence.EnumType;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRequestClient roleRequestClient;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRequestClient roleRequestClient, PasswordEncoder encoder){
        this.userRepository = userRepository;
        this.roleRequestClient = roleRequestClient;
        this.encoder = encoder;
    }

    @Transactional
    public User getById(UUID id){
        User user = this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("Cant find user by id."));
        user.getOrderedProducts().size();
        return user;
    }


    public void save(User user){
        this.userRepository.save(user);
    }
    public void signup(UserDTO userDTO, String role) {
        if(role.equals("USER")) this.userRepository.save(this.mapUser(userDTO, UserRole.USER));
        else if(role.equals("ADMIN")) this.userRepository.save(this.mapUser(userDTO, UserRole.ADMIN));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Username not found"));



        return new UserData(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.isActive());
    }

    private User mapUser(UserDTO userDTO, UserRole role){
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setActive(true);
        user.setRole(role);
        user.setAge(35);
        user.setCreatedOn(LocalDate.now());
        user.setUpdatedOn(LocalDate.now());

        user.setOrderedProducts(new ArrayList<Product>());
        Product product = new Product("Free Card", 0, user, 100, "/img/free_card_image.jpg", LocalDate.now()
                ,LocalDate.now());
        product.setOrdered(true);

        addOrderedProduct
                (
                        user,
                        product
                );

        return user;
    }

    private void addOrderedProduct(User user, Product product){
        user.getOrderedProducts().add(product);
    }
}
