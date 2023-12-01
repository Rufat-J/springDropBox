package code.me.springapi.controller;


import code.me.springapi.model.User;
import code.me.springapi.security.JwtTokenHandler;
import code.me.springapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    private record UserDTO(String username, String password) {};

    private JwtTokenHandler jwtTokenHandler;

    @Autowired
    public UserController(UserService userService, JwtTokenHandler jwtTokenHandler) {
        this.userService = userService;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO) {
        var result = userService.register(userDTO.username(), userDTO.password());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("login")
    public String login(@RequestBody UserDTO user) {
        var result = userService.login(user.username(), user.password());
        return result;
    }
}
