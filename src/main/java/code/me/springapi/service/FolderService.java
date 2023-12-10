package code.me.springapi.service;

import code.me.springapi.exception.FolderNotFoundException;
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

    /**
     * Creates a new folder for a user.
     *
     * @param userId ID of the user for whom the folder is created.
     * @param name   Name of the folder.
     * @return       The created folder.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    public Folder createFolder(Long userId, String name) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Folder folder = new Folder();
        folder.setName(name);
        folder.setUser(user);

        folderRepository.save(folder);

        return folder;
    }

    /**
     * Retrieves a folder by its ID.
     *
     * @param folderId ID of the folder to be retrieved.
     * @return         The retrieved folder or null if not found (you can modify this to throw an exception).
     */

    public Folder getFolderById(Long folderId) {
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new FolderNotFoundException("Folder not found with id " + folderId));
    }


    /**
     * Retrieves all folders for a user based on the user ID.
     *
     * @param userId ID of the user.
     * @return       List of folders for the specified user.
     */
    public List<Folder> getUserFolders(Long userId) {
        return folderRepository.findByUserId(userId);
    }
}
