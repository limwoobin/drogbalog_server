package com.drogbalog.server;

import com.drogbalog.server.global.code.Gender;
import com.drogbalog.server.user.domain.entity.UserEntity;
import com.drogbalog.server.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ServerTest {
    @Autowired
    private UserRepository repository;

    @Test
    public void test() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("drogba02@naver.com");
        userEntity.setPassword("zzzz");
        userEntity.setGender(Gender.MALE);
        userEntity.setNickName("drogba");
        repository.save(userEntity);

        UserEntity user = repository.findById(1L);
        assertEquals("drogba" , user.getNickName());
    }

    @Test
    public void optionalTest() {
        Optional<String> optionalS = null;
        System.out.println(optionalS.get());

        optionalS = Optional.of("T");
        System.out.println(optionalS.get());
    }
}
