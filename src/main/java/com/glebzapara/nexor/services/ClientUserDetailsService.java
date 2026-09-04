package com.glebzapara.nexor.services;

import com.glebzapara.nexor.models.User;
import com.glebzapara.nexor.repositories.UserRepository;
import com.glebzapara.nexor.security.ClientUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientUserDetailsService implements UserDetailsService {
    UserRepository userRepository;

    public ClientUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new ClientUserDetails(user);
    }
}
