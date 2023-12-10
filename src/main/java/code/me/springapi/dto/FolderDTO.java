package code.me.springapi.dto;

import code.me.springapi.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FolderDTO {
    private Long id;             // Unique identifier for the folder
    private String name;         // Name of the folder
    private User user;           // User to whom the folder belongs
    private List<FileDTO> files; // List of files within the folder
}
