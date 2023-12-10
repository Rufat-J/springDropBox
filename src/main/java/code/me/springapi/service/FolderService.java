package code.me.springapi.service;

import code.me.springapi.exception.UserNotFoundException;
import code.me.springapi.model.Folder;
import code.me.springapi.model.User;
import code.me.springapi.repository.FolderRepository;
import code.me.springapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;


    @Autowired
    public FolderService(FolderRepository folderRepository, UserRepository userRepository) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }


    public Folder createFolder(Long userId, String name) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Användaren kunde inte hittas"));

        Folder folder = new Folder();
        folder.setName(name);
        folder.setUser(user);

        folderRepository.save(folder);

        return folder;
    }

    public Folder getFolderById(Long folderId) {
        return folderRepository.findById(folderId)
                .orElse(null); // You can modify this to throw an exception if the folder is not found
    }

    public List<Folder> getUserFolders(Long userId) {
        // Retrieve folders based on the user ID
        return folderRepository.findByUserId(userId);
    }
}