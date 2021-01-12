package com.example.userapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/users"})
public class UserController {

    private UserRepository repository;

    UserController(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody User user) {
        if(!user.isValidCpf()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CPF Inválido");
        }
        final User existing = repository.findByCpfOrEmail(user.getCpf(), user.getEmail());
        if (existing != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(existing);
        }
        final User created = repository.save(user);
        return ResponseEntity.ok().body(created);
    }
}
