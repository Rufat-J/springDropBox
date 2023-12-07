package code.me.springapi.controller;

import code.me.springapi.model.Folder;
import code.me.springapi.model.User;
import code.me.springapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/folders")
public class FolderController {

    private final FolderService folderService;
    private final UserService userService;

    @Autowired
    public FolderController(FolderService folderService, UserService userService) {
        this.folderService = folderService;
        this.userService = userService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Map<String, String>> createFolder(@PathVariable Long userId, @RequestParam String folderName) {
        User user = userService.getUserById(userId);
        Folder folder = folderService.createFolder(user, folderName);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Folder created successfully with ID: " + folder.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Folder>> getUserFolders(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<Folder> folders = folderService.getUserFolders(user);
        return ResponseEntity.ok(folders);
    }
}