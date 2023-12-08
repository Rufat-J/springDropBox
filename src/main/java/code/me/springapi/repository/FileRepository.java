package code.me.springapi.repository;

import code.me.springapi.model.Folder;
import code.me.springapi.model.MyFile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FileRepository extends JpaRepository<MyFile, Long> {
    List<MyFile> findByFolder(Folder folder);
}
