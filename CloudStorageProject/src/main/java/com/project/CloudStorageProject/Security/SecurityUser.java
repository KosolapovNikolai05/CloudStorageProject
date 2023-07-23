package com.project.CloudStorageProject.Security;

import com.project.CloudStorageProject.Model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class SecurityUser implements UserDetails {


    private UUID ID;
    private String AESKey;
    private boolean isActive;

    public SecurityUser(UUID ID , String AESKey , boolean isActive) {
        this.ID = ID;
        this.AESKey = AESKey;
        this.isActive = isActive;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return AESKey;
    }

    @Override
    public String getUsername() {
        return ID.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails getAuthFromUser (User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUserID().toString(),
                user.getAESKey(),
                Set.of(new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return null;
                    }
                })
        );
    }
}
