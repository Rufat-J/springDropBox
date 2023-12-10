package code.me.springapi.dto;

import code.me.springapi.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FolderDTO {
    private Long id;
    private String name;
    private User user;
    private List<FileDTO> files;

}
