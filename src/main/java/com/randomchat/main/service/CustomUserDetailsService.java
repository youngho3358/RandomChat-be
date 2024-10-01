package com.randomchat.main.service;

import com.randomchat.main.domain.users.Users;
import com.randomchat.main.dto.CustomUserDetails;
import com.randomchat.main.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Users> user = usersRepository.findByEmail(email);

        if(user.isPresent()) {
            return new CustomUserDetails(user.get());
        }

        return null;
    }
}
