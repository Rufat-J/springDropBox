package code.me.springapi.controller;

import code.me.springapi.dto.FolderDTO;
import code.me.springapi.model.Folder;
import code.me.springapi.model.User;
import code.me.springapi.security.JwtTokenProvider;
import code.me.springapi.service.FolderService;
import code.me.springapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/folders")
public class FolderController {
    private final FolderService folderService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public FolderController(FolderService folderService, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.folderService = folderService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    /**
     * Handles the creation of a new folder.
     *
     * @param token Authorization token containing user information.
     * @param folderName The name of the folder to be created.
     * @return ResponseEntity containing a success message and the ID of the created folder.
     *
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createFolder(
            @RequestHeader("Authorization") String token,
            @RequestParam String folderName) {
        try {
            // Extract user ID from JWT token
            Long userId = extractUserIdFromToken(token);

            // Create a new folder
            Folder folder = folderService.createFolder(userId, folderName);

            // Prepare response
            Map<String, String> response = new HashMap<>();
            response.put("message", "Folder created successfully with ID: " + folder.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves a list of folder DTOs for a specific user.
     *
     * @param userId User ID for whom folders are retrieved.
     * @param token   Authorization token containing user information.
     * @return ResponseEntity containing a list of FolderDTOs.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<FolderDTO>> getUserFolders(@PathVariable Long userId, @RequestHeader("Authorization") String token) {
        try {
            // Get folders for the specified user
            List<Folder> folders = folderService.getUserFolders(userId);

            // Convert folders to FolderDTOs for response
            List<FolderDTO> folderDTOs = folders.stream()
                    .map(folder -> {
                        FolderDTO folderDTO = new FolderDTO();
                        folderDTO.setId(folder.getId());
                        folderDTO.setName(folder.getName());
                        folderDTO.setUser(folder.getUser());
                        // Map other fields or use nested DTOs as needed
                        return folderDTO;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(folderDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Helper method to extract user ID from JWT token
    private Long extractUserIdFromToken(String token) {
        return jwtTokenProvider.getUserIdFromToken(token);
    }
}
