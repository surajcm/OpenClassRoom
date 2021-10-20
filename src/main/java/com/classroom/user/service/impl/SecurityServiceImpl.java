package com.classroom.user.service.impl;

import com.classroom.user.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("unused")
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private WebSecurityConfigurerAdapter webSecurityConfigurerAdapter;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Override
    public String findLoggedInUsername() {
        var userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (userDetails instanceof UserDetails details) {
            return details.getUsername();
        }
        return null;
    }

    @Override
    public void autologin(final String username, final String password) {
        var userDetails = userDetailsService.loadUserByUsername(username);
        var usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            webSecurityConfigurerAdapter.authenticationManagerBean().authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            logger.debug("Auto login {} successfully!", username);
        }
    }
}
