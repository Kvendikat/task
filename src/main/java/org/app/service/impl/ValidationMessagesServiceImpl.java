package org.app.service.impl;

import org.app.service.ValidationMessagesService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ValidationMessagesServiceImpl implements ValidationMessagesService {
    public void createValidationMessages(BindingResult bindingResult, Map<String, List<String>> errors) {
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            if (!errors.containsKey(fieldError.getField())) {
                errors.put(fieldError.getField(), new ArrayList<>());
            }
            errors.get(fieldError.getField()).add(fieldError.getDefaultMessage());
        }
    }
}
