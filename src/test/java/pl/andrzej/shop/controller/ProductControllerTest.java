package pl.andrzej.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.andrzej.shop.model.dao.Product;
import pl.andrzej.shop.model.dto.ProductDto;
import pl.andrzej.shop.repository.ProductRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSaveProduct() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "fileName.png", MediaType.APPLICATION_OCTET_STREAM_VALUE, new byte[1]);
        MockMultipartFile productDtoJson = new MockMultipartFile("product", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(ProductDto.builder()
                .name("Bosh")
                .model("12KF")
                .price(1299.99)
                .stockLevel(15)
                .serialNumber("12987456123")
                .build()));
        mockMvc.perform(multipart("/api/products/")
                        .file(mockMultipartFile)
                        .file(productDtoJson)
                        .with(processor -> {
                            processor.setMethod(HttpMethod.POST.name());
                            return processor;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Bosh"))
                .andExpect(jsonPath("$.model").value("12KF"))
                .andExpect(jsonPath("$.price").value(1299.99))
                .andExpect(jsonPath("$.stockLevel").value(15))
                .andExpect(jsonPath("$.serialNumber").value("12987456123"));
    }

    @Test
    void shouldReturnMismatchWhenSerialNumberDoesNotStartFromTwoLettersFromModel() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "fileName.png", MediaType.APPLICATION_OCTET_STREAM_VALUE, new byte[1]);
        MockMultipartFile productDtoJson = new MockMultipartFile("product", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(ProductDto.builder()
                .name("Bosh")
                .model("12KF")
                .price(1299.99)
                .stockLevel(15)
                .serialNumber("13987456123")
                .build()));
        mockMvc.perform(multipart("/api/products/")
                        .file(mockMultipartFile)
                        .file(productDtoJson)
                        .with(processor -> {
                            processor.setMethod(HttpMethod.POST.name());
                            return processor;
                        }))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(""));
    }

    @Test
    void shouldReturnConflictWhenSerialNumberExists() throws Exception {
        productRepository.save(Product.builder()
                .name("Bosh")
                .model("12KF")
                .price(1299.99)
                .stockLevel(15)
                .serialNumber("12987456123")
                .build());
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "fileName.png", MediaType.APPLICATION_OCTET_STREAM_VALUE, new byte[1]);
        MockMultipartFile productDtoJson = new MockMultipartFile("product", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(ProductDto.builder()
                .name("Bosh")
                .model("12KF")
                .price(1299.99)
                .stockLevel(15)
                .serialNumber("12987456123")
                .build()));
        mockMvc.perform(multipart("/api/products/")
                        .file(mockMultipartFile)
                        .file(productDtoJson)
                        .with(processor -> {
                            processor.setMethod(HttpMethod.POST.name());
                            return processor;
                        }))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnBadRequest() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "fileName.png", MediaType.APPLICATION_OCTET_STREAM_VALUE, new byte[1]);
        MockMultipartFile productDtoJson = new MockMultipartFile("product", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(ProductDto.builder()
                .name("")
                .model("")
                .price(0.00)
                .stockLevel(0)
                .serialNumber("")
                .build()));
        mockMvc.perform(multipart("/api/products/")
                        .file(mockMultipartFile)
                        .file(productDtoJson)
                        .with(processor -> {
                            processor.setMethod(HttpMethod.POST.name());
                            return processor;
                        }))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnProductById() throws Exception {
        Product product = productRepository.save(Product.builder()
                .name("Bosh")
                .model("12KF")
                .price(1299.99)
                .stockLevel(15)
                .serialNumber("12987456123")
                .build());
        mockMvc.perform(get("/api/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.model").value(product.getModel()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.stockLevel").value(product.getStockLevel()))
                .andExpect(jsonPath("$.serialNumber").value(product.getSerialNumber()));
    }

    @Test
    void ShouldReturnNotFoundWhenProductDoesNotExists() throws Exception {
        mockMvc.perform(get("/api/products/" + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void shouldGetPageProduct() throws Exception {
        Product product = productRepository.save(Product.builder()
                .name("Bosh")
                .model("12KF")
                .price(1299.99)
                .stockLevel(15)
                .serialNumber("12987456123")
                .build());
        mockMvc.perform(get("/api/products/")
                .param("page","0")
                .param("size","25"))
                .andExpect(status().isOk());
    }
}
