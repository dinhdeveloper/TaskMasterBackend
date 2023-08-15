package com.dinh.logistics.respository;

import com.dinh.logistics.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileData, Long> {
}

