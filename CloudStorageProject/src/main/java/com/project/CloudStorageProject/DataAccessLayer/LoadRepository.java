package com.project.CloudStorageProject.DataAccessLayer;

import com.project.CloudStorageProject.Model.Load;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoadRepository extends JpaRepository<Load, UUID> {
}
