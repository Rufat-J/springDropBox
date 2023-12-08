package code.me.springapi.repository;

import code.me.springapi.model.MyFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<MyFile, Long> {

}
