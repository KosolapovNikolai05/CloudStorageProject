package com.project.CloudStorageProject.Security;

import com.project.CloudStorageProject.DataAccessLayer.UserRepository;
import com.project.CloudStorageProject.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String ID) throws UsernameNotFoundException {
        User user = userRepository.findById(UUID.fromString(ID)).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return SecurityUser.getAuthFromUser(user);
    }
}
