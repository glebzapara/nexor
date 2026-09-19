package com.glebzapara.nexor.repositories;

import com.glebzapara.nexor.models.Chat;
import com.glebzapara.nexor.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    List<Chat> findByUsers_Id(Integer userId);
}
