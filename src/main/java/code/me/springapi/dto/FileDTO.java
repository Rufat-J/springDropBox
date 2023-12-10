package code.me.springapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FileDTO {
    private Long id;
    private String name;
    private String contentType; // MIME type (e.g., "image/jpeg", "application/pdf")
    private long size; // file size in bytes
    private LocalDateTime uploadDate; // when the file was uploaded
    private String imageUrl; // endpoint or URL for the image
}