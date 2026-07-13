package com.festi_ci.parti_ci.fileManager;

import com.festi_ci.parti_ci.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileModel, String> {

    List<FileModel> findByUploadedBy(Long uploadedBy);

    List<FileModel> findAll();

}