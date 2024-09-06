package com.example.demo.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User_model;
import com.example.demo.model.userRoles;
import com.example.demo.repository.User_repository;
import org.springframework.security.core.userdetails.User;

@Service
public class newUserDetailsService implements UserDetailsService {
    @Autowired
    

      

private User_repository userRepository;

    @Autowired
    public newUserDetailsService(User_repository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User_model user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new  User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getUserroles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<userRoles> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }


    









}
