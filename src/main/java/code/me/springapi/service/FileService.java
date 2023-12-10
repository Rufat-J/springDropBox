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

    /**
     * Stores an image file in the specified folder.
     *
     * @param file      The image file to be stored.
     * @param folderId  ID of the folder where the file will be stored.
     * @return          The stored file entity.
     * @throws IllegalArgumentException  If the file is not an image.
     * @throws FileStorageException      If there is an issue storing the file.
     */
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

    /**
     * Converts a MyFile entity to a FileDTO.
     *
     * @param myFile    The MyFile entity to be converted.
     * @return          The corresponding FileDTO.
     */
    public FileDTO convertToFileDTO(MyFile myFile) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(myFile.getId());
        fileDTO.setName(myFile.getName());
        fileDTO.setContentType(myFile.getContentType());
        fileDTO.setSize(myFile.getContent().length);
        fileDTO.setUploadDate(myFile.getUploadDate());
        fileDTO.setImageUrl("/files/" + myFile.getId());

        return fileDTO;
    }

    /**
     * Retrieves a file by its ID.
     *
     * @param fileId    The ID of the file to be retrieved.
     * @return          The retrieved file entity.
     * @throws FileNotFoundException    If the file with the specified ID is not found.
     */
    public MyFile getFile(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
    }

    /**
     * Retrieves a list of files within a specific folder.
     *
     * @param folderId  The ID of the folder to retrieve files from.
     * @return          The list of files in the specified folder.
     * @throws RuntimeException    If the folder with the specified ID is not found.
     */
    public List<MyFile> getFilesInFolder(Long folderId) {
        Optional<Folder> optionalFolder = folderRepository.findById(folderId);
        Folder folder = optionalFolder.orElseThrow(() -> new RuntimeException("Folder not found"));

        return fileRepository.findByFolder(folder);
    }
}
