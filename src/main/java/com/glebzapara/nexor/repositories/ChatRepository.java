package com.glebzapara.nexor.repositories;

import com.glebzapara.nexor.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    List<Chat> findByUsers_Id(Integer userId);
}
