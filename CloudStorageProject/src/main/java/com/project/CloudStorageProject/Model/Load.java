package com.project.CloudStorageProject.Model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "Loads")
public class Load {
    @Id
    @GeneratedValue
    @Column(name ="LoadID")
    private UUID LoadID;
    @Column(name ="size")
    private long size;
    @Column(name ="date")
    private LocalDateTime date;
    @Transient
    private List<MultipartFile> files =  new ArrayList<>();
    @Transient
    private List<String> fileNames = new ArrayList<>();

    public Load (MultipartFile[] files) throws IOException {
        for (MultipartFile mf : files) {
            this.size = this.size + mf.getSize();
            fileNames.add(mf.getOriginalFilename());
        }
        this.files.addAll(List.of(files));
        this.date = LocalDateTime.now();
    }

    public Load() {
        this.date = LocalDateTime.now();
    }
}
