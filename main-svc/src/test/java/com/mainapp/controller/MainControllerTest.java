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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class MainControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private RoleRequestClient roleRequestClient;

    @InjectMocks
    private MainController mainController;

    private UserData userData;
    private User user;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(mainController)
                .build();

        user = new User();
        user.setId(UUID.randomUUID());
        user.setMoney(100);
        user.setOrderedProducts(List.of());

        userData = mock(UserData.class);

    }


    @Test
    void testIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testSignupPage() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("signup"));
    }

    @Test
    void testInfoPage() throws Exception {
        mockMvc.perform(get("/info"))
                .andExpect(status().isOk())
                .andExpect(view().name("info-page"));
    }


    @Test
    void testGetAllProducts() throws Exception {
        when(userService.getById(user.getId())).thenReturn(user);
        when(productService.getByUnordered()).thenReturn(List.of(new Product()));

        mockMvc.perform(get("/all")
                        .principal(() -> user.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("products"))
                .andExpect(view().name("all"));
    }

    @Test
    void testBuyProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        when(userService.getById(user.getId())).thenReturn(user);

        mockMvc.perform(post("/buy")
                        .param("productId", productId.toString())
                        .principal(() -> user.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/all"));

        verify(productService).createOrder(eq(productId), eq(user));
    }



    @Test
    void testRoleRequestPage() throws Exception {
        RoleRequestDTO dto = new RoleRequestDTO();
        dto.setUserId(user.getId());

        when(roleRequestClient.getAllRequests()).thenReturn(List.of(dto));

        mockMvc.perform(get("/role-request")
                        .principal(() -> user.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("requests"))
                .andExpect(view().name("role-request"));
    }



    @Test
    void testGetCreateProductPage() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("product"))
                .andExpect(view().name("create-product"));
    }

    @Test
    void testCreateProduct() throws Exception {
        mockMvc.perform(post("/product/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(productService).createProduct(any(ProductDTO.class));
    }


    @Test
    void testProcessSignup() throws Exception {
        mockMvc.perform(post("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

        verify(userService).signup(any(UserDTO.class), eq("USER"));
    }


    @Test
    void testHomePage() throws Exception {
        when(userService.getById(user.getId())).thenReturn(user);

        mockMvc.perform(get("/home")
                        .principal(() -> user.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(view().name("home-page"));
    }

    @Test
    void testAddMoney() throws Exception {
        when(userService.getById(user.getId())).thenReturn(user);

        mockMvc.perform(post("/add-money")
                        .param("amount", "50")
                        .principal(() -> user.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/all"));

        verify(userService).save(any(User.class));
    }
}
