package code.me.springapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    // Innehållet i filen som bytes
    @Lob
    private byte[] content;

    // Mapp som filen tillhör
    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;
}
