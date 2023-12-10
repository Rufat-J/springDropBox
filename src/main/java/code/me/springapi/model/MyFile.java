package code.me.springapi.model;// Import necessary classes
import code.me.springapi.model.Folder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Add annotations
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

    // Innehållet i filen som bytes
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    // Mapp som filen tillhör
    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
}
