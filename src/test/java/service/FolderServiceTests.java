package service;

import code.me.springapi.SpringDropBox;
import code.me.springapi.model.Folder;
import code.me.springapi.model.User;
import code.me.springapi.repository.FolderRepository;
import code.me.springapi.repository.UserRepository;
import code.me.springapi.service.FolderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = SpringDropBox.class)
class FolderServiceTests {

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FolderService folderService;

    @Test
    void getUserFolders_ExistingUser_ReturnsFolders() {
        // Arrange
        Long userId = 1L;

        //mock user
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("testUser");

        Folder folder = new Folder();
        folder.setId(1L);
        folder.setName("Folder1");

        folder.setUser(mockUser);

        Mockito.when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(mockUser));
        Mockito.when(folderRepository.findByUserId(userId)).thenReturn(Collections.singletonList(folder));

        // Act
        List<Folder> actualFolders = folderService.getUserFolders(userId);

        // Assert
        assertEquals(1, actualFolders.size());
        assertEquals("Folder1", actualFolders.get(0).getName());
        assertEquals("testUser", actualFolders.get(0).getUser().getUsername());
    }

    @Test
    void createFolder_ValidUserAndName_ReturnsCreatedFolder() {
        // Arrange
        Long userId = 1L;
        String folderName = "NewFolder";

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("testUser");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        Folder createdFolder = folderService.createFolder(userId, folderName);

        // Assert
        assertEquals(folderName, createdFolder.getName());
        assertEquals(mockUser, createdFolder.getUser());
    }

    @Test
    void getUserFolders_NonExistingUser_ReturnsEmptyList() {
        // Arrange
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        var actualFolders = folderService.getUserFolders(userId);

        // Assert
        assertEquals(0, actualFolders.size());
    }
}

