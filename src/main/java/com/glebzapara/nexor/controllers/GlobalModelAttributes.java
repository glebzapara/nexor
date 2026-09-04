package com.glebzapara.nexor.controllers;

import com.glebzapara.nexor.models.Admin;
import com.glebzapara.nexor.models.User;
import com.glebzapara.nexor.security.AdminDetails;
import com.glebzapara.nexor.security.ClientUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addUserToModel(Model model, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return;
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof AdminDetails adminDetails) {
            Admin admin = adminDetails.getAdmin();
            model.addAttribute("currentAdmin", admin);
        } else if (principal instanceof ClientUserDetails clientUserDetails) {
            User user = clientUserDetails.getUser();
            model.addAttribute("currentUser", user);
        }
    }
}