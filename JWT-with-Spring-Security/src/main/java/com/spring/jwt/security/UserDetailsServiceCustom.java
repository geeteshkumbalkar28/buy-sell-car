package com.spring.jwt.security;

import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsServiceCustom implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetailsCustom userDetailsCustom = getUserDetails(username);

        if(ObjectUtils.isEmpty(userDetailsCustom)){
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid username or password!" );
        }

        return userDetailsCustom;
    }

    private UserDetailsCustom getUserDetails(String username) {
        User user = userRepository.findByEmail(username);



        if (ObjectUtils.isEmpty(user)) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid username or password!");
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        String firstName = null;
        String dealerId = null;
        String userId = null;
        String userProfileId = null;

        if (authorities.contains(new SimpleGrantedAuthority("DEALER"))) {
            Dealer dealer = user.getDealer();
            if (dealer != null) {
                firstName = dealer.getFirstname();
                dealerId = String.valueOf(dealer.getId());
            }
        } else if (authorities.contains(new SimpleGrantedAuthority("USER"))) {
            firstName = user.getProfile().getFirstName();
            userProfileId = String.valueOf(user.getProfile().getId());
            userId = String.valueOf(user.getId());
        }

        return new UserDetailsCustom(
                user.getEmail(),
                user.getPassword(),
                firstName,
                dealerId,
                userId,
                userProfileId,
                authorities
        );

    }

}

