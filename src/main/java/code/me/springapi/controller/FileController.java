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

    /**
     * Handles file upload to a specific folder.
     *
     * @param folderId ID of the folder where the file will be uploaded.
     * @param file     The file to be uploaded.
     * @return ResponseEntity containing a success message and the ID of the uploaded file.
     */
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

    /**
     * Handles file download by providing the file's content.
     *
     * @param fileId ID of the file to be downloaded.
     * @return ResponseEntity containing the file content and appropriate content type.
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) {
        MyFile file = fileService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(file.getContent());
    }

    /**
     * Retrieves a list of file DTOs within a specific folder.
     *
     * @param folderId ID of the folder to retrieve files from.
     * @return ResponseEntity containing a list of FileDTOs.
     */
    @GetMapping("/folder/{folderId}")
    public ResponseEntity<List<FileDTO>> getFilesInFolder(@PathVariable Long folderId) {
        List<MyFile> files = fileService.getFilesInFolder(folderId);
        List<FileDTO> fileDTOs = files.stream()
                .map(fileService::convertToFileDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(fileDTOs);
    }
}
