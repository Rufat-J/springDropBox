package code.me.springapi.service;


import code.me.springapi.dto.FileDTO;
import code.me.springapi.exception.FileNotFoundException;
import code.me.springapi.exception.FileStorageException;
import code.me.springapi.model.Folder;
import code.me.springapi.model.MyFile;
import code.me.springapi.repository.FileRepository;
import code.me.springapi.repository.FolderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FileService {

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    private final FolderService folderService;

    @Autowired
    public FileService(FileRepository fileRepository, FolderRepository folderRepository, FolderService folderService) {
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.folderService = folderService;
    }

    @Transactional
    public MyFile storeImage(MultipartFile file, Long folderId) {
        // Validate and handle only image files
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed.");
        }

        Folder folder = folderService.getFolderById(folderId);

        try {
            MyFile myFile = new MyFile();
            myFile.setName(file.getOriginalFilename());
            myFile.setContentType(file.getContentType());
            myFile.setContent(file.getBytes());
            myFile.setFolder(folder);
            myFile.setUploadDate(LocalDateTime.now());

            return fileRepository.save(myFile);
        } catch (IOException e) {
            throw new FileStorageException("Failed to store image file.", e);
        }
    }

    public FileDTO convertToFileDTO(MyFile myFile) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(myFile.getId());
        fileDTO.setName(myFile.getName());
        fileDTO.setContentType(myFile.getContentType());
        fileDTO.setSize(myFile.getContent().length);
        fileDTO.setUploadDate(myFile.getUploadDate());

        // Assuming you have an endpoint for serving images, update this accordingly
        fileDTO.setImageUrl("/files/" + myFile.getId()); // Replace with your actual endpoint

        return fileDTO;
    }



    public MyFile getFile(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
    }


    public List<MyFile> getFilesInFolder(Long folderId) {
        Optional<Folder> optionalFolder = folderRepository.findById(folderId);
        Folder folder = optionalFolder.orElseThrow(() -> new RuntimeException("Folder not found"));

        return fileRepository.findByFolder(folder);
    }
}