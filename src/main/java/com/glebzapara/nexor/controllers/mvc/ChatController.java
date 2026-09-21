package com.glebzapara.nexor.controllers.mvc;

import com.glebzapara.nexor.models.Admin;
import com.glebzapara.nexor.models.Chat;
import com.glebzapara.nexor.models.User;
import com.glebzapara.nexor.security.AdminDetails;
import com.glebzapara.nexor.security.ClientUserDetails;
import com.glebzapara.nexor.services.ChatService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.glebzapara.nexor.services.ClientUserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {
    ClientUserService clientUserService;
    ChatService chatService;

    public ChatController(ClientUserService clientUserService,
                          ChatService chatService) {
        this.clientUserService = clientUserService;
        this.chatService = chatService;
    }

    @GetMapping("/")
    public String homePage(Authentication authentication, Model model) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof ClientUserDetails clientUserDetails) {
            User user = clientUserDetails.getUser();

            model.addAttribute("chats", chatService.findChatsByUserId(user.getId()));

        } else if (principal instanceof AdminDetails adminDetails) {
            Admin admin = adminDetails.getAdmin();

            return "admin-dashboard";
        }

        return "index";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public void register(@ModelAttribute User user) throws Exception {
        user.setRole("ROLE_USER");
        clientUserService.registerUser(user);
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users/{id}/profile")
    public String getUserProfile(@PathVariable Integer id, Model model) {
        User user = clientUserService.findById(id);

        model.addAttribute("user", user);

        return "user-profile";
    }

    @GetMapping("/chats/{id}")
    public String chat(@PathVariable Integer id, Model model, Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof ClientUserDetails clientUserDetails) {
            User user = clientUserDetails.getUser();

            model.addAttribute("chats", chatService.findChatsByUserId(user.getId()));
            model.addAttribute("currentUser", user);
            model.addAttribute("currentChat", chatService.findById(id));
        }

        model.addAttribute("id", id);

        return "chat";
    }

    @GetMapping("/chats/create/{id}")
    public String createChat(@PathVariable Integer id,
                             Authentication authentication) {
        ClientUserDetails clientUserDetails = (ClientUserDetails) authentication.getPrincipal();

        User currentUser = clientUserDetails.getUser();
        User targetUser = clientUserService.findById(id);

        Chat chat = chatService.createChat(currentUser, targetUser);

        return "redirect:/chats/" + chat.getId();
    }

    @PostMapping("/users/search")
    public String searchStudents(@RequestParam("searchTerm") String searchTerm,
                                 Model model) {
        List<User> filtered = new ArrayList<>();

        for (User user : clientUserService.findAllUsers()) {
            String userName = (user.getUserName()).toLowerCase();

            if (userName.contains(searchTerm.toLowerCase())) {
                filtered.add(user);
            }
        }

        model.addAttribute("users", filtered);

        return "search";
    }
}
