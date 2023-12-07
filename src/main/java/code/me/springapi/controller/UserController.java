package code.me.springapi.controller;

import code.me.springapi.dto.LoginSuccess;
import code.me.springapi.dto.RegisterSuccess;
import code.me.springapi.model.Folder;
import code.me.springapi.model.User;
import code.me.springapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private record UserDTO(String username, String password){};

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterSuccess> registerUser(@RequestBody UserDTO userDTO){
        var result = userService.register(userDTO.username(), userDTO.password());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginSuccess> login(@RequestBody UserDTO user) {
        var result = userService.login(user.username(), user.password());
        return ResponseEntity.ok().body(result);
    }

}
