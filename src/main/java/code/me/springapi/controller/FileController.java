package code.me.springapi.controller;

import code.me.springapi.dto.FileDTO;
import code.me.springapi.model.MyFile;
import code.me.springapi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload/{folderId}")
    public ResponseEntity<Map<String, String>> uploadFile(
            @PathVariable Long folderId,
            @RequestParam("file") MultipartFile file) {

        // Check if the file is an image
        if (!file.getContentType().startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only image files are allowed."));
        }

        MyFile uploadedFile = fileService.storeImage(file, folderId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Image uploaded successfully with ID: " + uploadedFile.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) {
        MyFile file = fileService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(file.getContent());
    }

    @GetMapping("/folder/{folderId}")
    public ResponseEntity<List<FileDTO>> getFilesInFolder(@PathVariable Long folderId) {
        List<MyFile> files = fileService.getFilesInFolder(folderId);
        List<FileDTO> fileDTOs = files.stream()
                .map(fileService::convertToFileDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(fileDTOs);
    }
}