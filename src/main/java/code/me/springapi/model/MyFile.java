package code.me.springapi.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
public class MyFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Content type for image files (e.g., "image/jpeg", "image/png")
    private String contentType;

    // File content in byte
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    // Folder that belongs to file
    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
}
