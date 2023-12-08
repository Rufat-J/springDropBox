package code.me.springapi.service;


import code.me.springapi.exception.FileNotFoundException;
import code.me.springapi.exception.FileStorageException;
import code.me.springapi.model.Folder;
import code.me.springapi.model.MyFile;
import code.me.springapi.repository.FileRepository;
import code.me.springapi.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    @Autowired
    public FileService(FileRepository fileRepository, FolderRepository folderRepository) {
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
    }

    public MyFile storeFile(MultipartFile file, Long folderId) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Fetch the folder where the file will be stored
            Optional<Folder> optionalFolder = folderRepository.findById(folderId);
            Folder folder = optionalFolder.orElseThrow(() -> new RuntimeException("Folder not found"));

            // Create a new file
            MyFile myFile = new MyFile();
            myFile.setName(fileName);
            myFile.setContent(file.getBytes());
            myFile.setFolder(folder);

            // Save the file to the repository
            return fileRepository.save(myFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
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