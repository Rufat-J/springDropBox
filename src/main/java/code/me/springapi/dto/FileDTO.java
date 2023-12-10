package code.me.springapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FileDTO {

    private Long id;               // Unique identifier for the file
    private String name;           // Name of the file
    private String contentType;    // Content type of the file (e.g., "image/jpeg")
    private long size;             // Size of the file in bytes
    private LocalDateTime uploadDate; // Date and time when the file was uploaded
    private String imageUrl;       // URL to access the file, if applicable

}
