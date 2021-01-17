package org.app.controller;

import org.app.entity.Product;
import org.app.repository.ProductRepository;
import org.app.service.PaginationService;
import org.app.service.ValidationMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaginationService paginationService;

    @Autowired
    private ValidationMessagesService validationMessagesService;

    @GetMapping("/start")
    private String findAllActiveProduct(
            @PageableDefault(value = 10) Pageable pageable,
            Model model
    ) {
        Page<Product> products = productRepository.findAllByActiveTrueOrderByName(pageable);
        paginationService.addToModelWithPagination(model, products, pageable);
        model.addAttribute("products", products);

        return "product/start";
    }

    @GetMapping("/add")
    private String addNewProduct(Model model) {
        Product newProduct = new Product();
        model.addAttribute("newProduct", newProduct);
        model.addAttribute("errors", new HashMap<>());

        return "product/add";
    }

    @PostMapping("/add")
    private String addNewProduct(
            @Valid @ModelAttribute Product newProduct,
            BindingResult bindingResult,
            Model model
    ) {
        Product sameProduct = productRepository.findByName(newProduct.getName());
        if (sameProduct != null) {
            bindingResult.addError(
                    new FieldError("product", "name", "Такое название уже существует")
            );
        }
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = new HashMap<>();

            validationMessagesService.createValidationMessages(bindingResult, errors);
            model.addAttribute("errors", errors);
            model.addAttribute("newProduct", newProduct);

            return "product/add";
        }
        productRepository.saveAndFlush(newProduct);

        return "redirect:/product/add";
    }

    @GetMapping("/edit/{id}")
    private String editProduct(
            @PathVariable(name = "id") Product product,
            Model model
    ) {
        if (product == null) {
            throw new RuntimeException("Такого продукта не найдено");
        }
        Map<String, List<String>> errors = new HashMap<>();
        model.addAttribute("product", product);
        model.addAttribute("errors", errors);

        return "product/edit";
    }

    @PostMapping("/save/{id}")
    private String saveEditedProduct(
            @PathVariable(name = "id") Product product,
            @Valid @ModelAttribute Product editedProduct,
            BindingResult bindingResult,
            Model model
    ) {
        if (product == null) {
            throw new RuntimeException("Такого продукта не найдено");
        }
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = new HashMap<>();
            validationMessagesService.createValidationMessages(bindingResult, errors);

            model.addAttribute("errors", errors);
            model.addAttribute("product", editedProduct);

            return "product/edit";
        }
        product.setId(editedProduct.getId());
        product.setName(editedProduct.getName());
        product.setPrice(editedProduct.getPrice());
        product.setCountry(editedProduct.getCountry());
        product.setActive(editedProduct.getActive());

        productRepository.saveAndFlush(product);

        return "redirect:/product/start";
    }

    @GetMapping("/archive")
    private String findAllNotActivePlates(
            @PageableDefault(value = 10) Pageable pageable,
            Model model
    ) {
        Page<Product> products = productRepository.findAllByActiveFalseOrderByName(pageable);
        paginationService.addToModelWithPagination(model, products, pageable);
        model.addAttribute("products", products);

        return "product/archive";
    }

    @GetMapping("/push-to-archive/{id}")
    private String pushToArchive(
            @PathVariable(name = "id") Product product
    ) {
        if (product == null) {
            throw new RuntimeException("Такого продукта не найдено");
        }
        product.setActive(false);
        productRepository.saveAndFlush(product);

        return "redirect:/product/start";
    }

    @GetMapping("/return-to-active/{id}")
    private String returnToActive(
            @PathVariable(name = "id") Product product
    ) {
        if (product == null) {
            throw new RuntimeException("Такого продукта не найдено");
        }
        product.setActive(true);
        productRepository.saveAndFlush(product);

        return "redirect:/product/archive";
    }

    @GetMapping("/delete/{id}")
    private String delete(
            @PathVariable(name = "id") Product product
    ) {
        if (product == null) {
            throw new RuntimeException("Такого продукта не найдено");
        }
        productRepository.delete(product);
        productRepository.flush();

        return "redirect:/product/archive";
    }


}
