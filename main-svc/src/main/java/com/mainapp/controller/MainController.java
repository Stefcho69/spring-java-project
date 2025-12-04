package com.mainapp.controller;

import com.mainapp.RoleRequestClient;
import com.mainapp.dto.ProductDTO;
import com.mainapp.dto.RoleRequestDTO;
import com.mainapp.dto.UserDTO;
import com.mainapp.model.entity.Product;
import com.mainapp.model.entity.User;
import com.mainapp.security.UserData;
import com.mainapp.service.ProductService;
import com.mainapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping("/")
public class MainController{

    private final UserService userService;
    private final ProductService productService;
    private final RoleRequestClient roleRequestClient;
    public MainController(UserService userService, ProductService productService, RoleRequestClient roleRequestClient) {
        this.userService = userService;
        this.productService = productService;
        this.roleRequestClient = roleRequestClient;
    }

    @GetMapping
    public String getIndexPage(){
        return "index";
    }


    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/signup")
    public String getSignupPage(Model model){
        model.addAttribute("user", new UserDTO());
        return "signup";
    }

    @GetMapping("/all")
    public String getAllProductsForSale(@AuthenticationPrincipal UserData userData, Model model){
        User user = userService.getById(userData.getId());
        model.addAttribute("user", user);
        List<Product> products = productService.getByUnordered();
        Map m = new HashMap<String, List<Product>>();
        m.put("products", products);
        model.addAllAttributes(m);
        return "all";
    }

    @PostMapping("/buy")
    public String orderProduct(@RequestParam("productId") UUID id, @AuthenticationPrincipal UserData userData) {
        log.info("Ordering product...");
        User user = userService.getById(userData.getId());
        productService.createOrder(id, user);
        log.info("Product ordered");
        return "redirect:/all";
    }

    @GetMapping("/role-request")
    public String roleRequestPage(@AuthenticationPrincipal UserData userData, Model model) {
        List<RoleRequestDTO> requests = roleRequestClient.getAllRequests()
                .stream()
                .filter(r -> r.getUserId().equals(userData.getId()))
                .collect(Collectors.toList());

        model.addAttribute("requests", requests);
        return "role-request";
    }

    @GetMapping("/product/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getCreateProductPage(Model model){
        model.addAttribute("product", new ProductDTO());
        return "create-product";
    }

    @PostMapping("/product/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createProduct(@ModelAttribute("product") ProductDTO productDTO)
    {
        productService.createProduct(productDTO);
        log.info("Product was created!");
        return "redirect:/home";
    }


    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("user") UserDTO userDTO){
        userService.signup(userDTO, "USER");
        log.info("You signed up");
        return "login";
    }

    @GetMapping("/admin")
    public String getAdminPage(){
        return "admin-page";
    }

    @GetMapping("/home")
    public String getHomePage(@AuthenticationPrincipal UserData userData, Model model){
        User user = userService.getById(userData.getId());

        Map<String, List<Product>> m = new HashMap<>();
        m.put("products", user.getOrderedProducts());
        model.addAllAttributes(m);
        return "home-page";
    }

    @GetMapping("/info")
    public String getInfoPage(){
        return "info-page";
    }

    @GetMapping("/logout")
    public String getLogoutPage(){
        log.info("You are logging out");
        return "logout";
    }

    @PostMapping("/add-money")
    public String addMoney(@AuthenticationPrincipal UserData userData,
                           @RequestParam double amount) {
        User user = userService.getById(userData.getId());
        user.setMoney(user.getMoney() + amount);
        userService.save(user);
        return "redirect:/all";
    }
}
