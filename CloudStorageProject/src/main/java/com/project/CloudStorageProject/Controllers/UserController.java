package com.project.CloudStorageProject.Controllers;
import com.project.CloudStorageProject.DataAccessLayer.LoadRepository;
import com.project.CloudStorageProject.DataAccessLayer.UserRepository;
import com.project.CloudStorageProject.Model.Load;
import com.project.CloudStorageProject.Model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/f")
public class UserController {
    private final LoadRepository loadRepository;
    private final UserRepository userRepository;

    private final List<Load> uploaded = new ArrayList<>();

    @Autowired
    public UserController(LoadRepository loadRepository, UserRepository userRepository , HttpServletRequest request) {

        this.loadRepository = loadRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/account")
    public String signUp(Model model , HttpServletRequest request) throws IOException {
        model.addAttribute("loadsList" , defineUser(request).getUploaded());
        request.getSession().setAttribute("loadsList" , defineUser(request).getUploaded());
        return "account.html";
    }

    @GetMapping("/loadList/{LoadID}")
    public String showLoad(@PathVariable(value = "LoadID") String LoadID , HttpServletRequest request , Model model) throws IOException {
        LoadID = LoadID.replace("$" , "");
        List<Load> loads = (List<Load>) request.getSession().getAttribute("loadsList");
        Load l = null;
        for (Load fl : loads) {
            if (fl.getLoadID().toString().equals(LoadID)) {
                l = fl;
            }
        }
        request.getSession().setAttribute("load" , l);
        return "showLoad.html";
    }

    @PostMapping("/downloadFiles")
    public String uploadFiles(@RequestParam("files") MultipartFile[] files, HttpServletRequest request, Model model) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        for (MultipartFile mf : files) {
            uploadFile(mf , request);
            request.getSession().setAttribute(mf.getOriginalFilename() , mf.getResource());
        }
        Load newLoad = new Load(files);
        loadRepository.save(newLoad);
        User currentUser = defineUser(request);
        currentUser.upload(newLoad);
        userRepository.save(currentUser);
        model.addAttribute("loadsList" , currentUser.getUploaded());
        request.getSession().setAttribute("loadsList" , currentUser.getUploaded());
        return "account.html";
    }

    public User defineUser (HttpServletRequest request) {
        String currentUserID = request.getUserPrincipal().getName();
        return userRepository.findAll().stream().filter(user -> user.getUserID().toString().equals(currentUserID)).findFirst().get();
    }

    public void uploadFile(MultipartFile file , HttpServletRequest request) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        String filePath = ("C:\\Users\\user\\Downloads\\CloudStorageProject\\CloudStorageProject\\src\\main\\resources\\files");
        File f1 = new File(filePath+"/"+file.getOriginalFilename());
        file.transferTo(f1);
    }


    @GetMapping("/loadList/download/{resourceFilename}")
    public ResponseEntity<Object> downloadFile (@PathVariable(value = "resourceFilename") String
        resourceFilename, HttpServletRequest request) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        resourceFilename = resourceFilename.replace("$", "");
        String filePath = ("C:\\Users\\user\\Downloads\\CloudStorageProject\\CloudStorageProject\\src\\main\\resources\\files\\");
        File file = new File(filePath+resourceFilename);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(headers)
                .body(Files.readAllBytes(file.toPath()));
        }



    @GetMapping("/loadList/seeFile/{resourceFilename}")
    public ResponseEntity<Object> seeFile (@PathVariable(value = "resourceFilename") String
                                                        resourceFilename, HttpServletRequest request) throws IOException {
        resourceFilename = resourceFilename.replace("$", "");
        String filePath = ("C:\\Users\\user\\Downloads\\CloudStorageProject\\CloudStorageProject\\src\\main\\resources\\files\\");
        File file = new File(filePath+resourceFilename);
        return ResponseEntity.ok()
                .body(Files.readAllBytes(file.toPath()));

    }
}
