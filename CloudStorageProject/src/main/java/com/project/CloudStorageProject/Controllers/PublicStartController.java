package com.project.CloudStorageProject.Controllers;

import com.project.CloudStorageProject.DataAccessLayer.UserRepository;
import com.project.CloudStorageProject.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping
public class PublicStartController {

    private final UserRepository userRepository;

    @Autowired
    public PublicStartController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping
    public String welcome() {
        return "welcome.html";
    }



    @GetMapping ("/signup")
    public String showSignUpUserPage() {
        return "signup.html";
    }


    @PostMapping("/signup")
    public void signupUser (Model model) throws NoSuchAlgorithmException {
        createUser(model);
    }

    @GetMapping("/signupprovider")
    public String showSignUpProviderPage() throws NoSuchAlgorithmException {
        return "signupprovider.html";
    }

    @PostMapping("/signupprovider")
    public void signupProvider(Model model) throws NoSuchAlgorithmException {
        createUser(model);
    }

    @GetMapping ("/login")
    public String loginPage() {
        return "login.html";
    }

    public void createUser(Model model) throws NoSuchAlgorithmException {
        User newUser = new User();
        userRepository.save(newUser);
        model.addAttribute("newUser" , newUser);
    }
}
