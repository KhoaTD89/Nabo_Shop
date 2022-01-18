package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.model.ProductForm;
import com.example.demo.services.ProductService;
import com.example.demo.utils.FileUploadUtil;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.Multipart;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Value("${upload.path}")
    private String fileUpload;

    @GetMapping("")
    public String getAll(Model model){
        List<Product> products = productService.getAll();
        model.addAttribute("products",products);
        return "product/products-list";
    }

    @GetMapping("/create")
    public String showAddingForm(Model model){
        model.addAttribute("product",new ProductForm());
        return "product/adding-form";
    }

    @PostMapping("/create")
    public RedirectView saveProduct(@ModelAttribute ProductForm productForm) throws IOException {
        Product product = new Product();
        product.setCode(productForm.getCode());
        product.setName(productForm.getName());
        product.setCreateDate(new Date());
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();

        FileCopyUtils.copy(productForm.getImage().getBytes(), new File(this.fileUpload + fileName));

        product.setImage(fileName);
        productService.saveProduct(product);
        return new RedirectView("");
    }

}
