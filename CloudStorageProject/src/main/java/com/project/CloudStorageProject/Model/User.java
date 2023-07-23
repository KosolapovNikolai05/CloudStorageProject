package com.project.CloudStorageProject.Model;

import com.project.CloudStorageProject.Model.Load;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "UserID")
    private UUID UserID;
    @Column(name ="AESKey")
    private String AESKey;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "UserID" , referencedColumnName = "userid")
    private List<Load> uploaded;
    @Transient
    private String NonEncodedAESKey;
    @Transient
    private SecretKey FileSecurityHandlerKey;
    public User () throws NoSuchAlgorithmException {
        this.FileSecurityHandlerKey = generateAESKey();
        this.NonEncodedAESKey = new String(generateAESKey().getEncoded(), StandardCharsets.UTF_16);
        this.AESKey = BCrypt.hashpw(NonEncodedAESKey, BCrypt.gensalt());
        this.uploaded = new ArrayList<>();
    }

    public long uploadedFilesSize() {
        long currentSize = 0L;
        for (Load l : uploaded) {
            currentSize = currentSize + l.getSize();
        }
        return currentSize;
    }

    public SecretKey generateAESKey () throws NoSuchAlgorithmException {
        KeyGenerator generator =  KeyGenerator.getInstance("AES");
        generator.init(256);
        return generator.generateKey();
    }

    public void upload (Load load) {
        this.uploaded.add(load);
    }
}
