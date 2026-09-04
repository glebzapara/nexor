package com.glebzapara.nexor.config;

import com.glebzapara.nexor.models.User;
import com.glebzapara.nexor.repositories.UserRepository;
import com.glebzapara.nexor.security.ClientUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class LastSeenFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;

    public LastSeenFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof ClientUserDetails clientUserDetails) {
            User user = clientUserDetails.getUser();

            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Kyiv"));

            if (user.getLastSeen() == null ||
                    user.getLastSeen().isBefore(now.minusMinutes(5))) {

                user.setLastSeen(now);
                userRepository.save(user);
            }
        }

        filterChain.doFilter(request, response);
    }
}