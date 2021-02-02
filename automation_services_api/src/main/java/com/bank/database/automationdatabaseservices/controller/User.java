package com.bank.database.automationdatabaseservices.controller;

import com.bank.database.automationdatabaseservices.config.Config;
import com.bank.database.automationdatabaseservices.model.auth.UserModel;
import com.bank.database.automationdatabaseservices.repository.auth.UserRepository;
import com.bank.database.automationdatabaseservices.security.SecurePassword;
import com.lambdaworks.redis.RedisConnection;
import com.sun.mail.iap.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
public class User {
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserModel user){
        try{
            String pass = userRepository.login(user.getUsername()).getPassword();
            if (SecurePassword.checkMatch(user.getPassword(), pass)){
                UUID uuid = UUID.randomUUID();
                final String apikey = uuid.toString().replace("-", "R");
                if (SET(apikey, userRepository.login(user.getUsername()).toString())){
                    logger.info("Login success by: "+ user.getUsername());
                }

                return ResponseEntity.status(HttpStatus.OK).header("apikey", apikey).body("Login");
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Failed!");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not Found!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserModel user){
        UUID uuid = UUID.randomUUID();
        final String apikey = uuid.toString().replace("-", "R");
        user.setPassword(SecurePassword.bcrypt(user.getPassword()));
        userRepository.save(user);
        if (SET(apikey, user.toString())){
            logger.info("Success registered with user: " + user.getUsername());
        }

        return ResponseEntity.status(HttpStatus.OK).header("apikey", apikey).body("Register");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody Map<String, String> apikey) {
        rDelete(apikey.get("apikey"));
        return ResponseEntity.status(HttpStatus.OK).body("Logout!");
    }

    public boolean SET(String key, String value){
        boolean done;
        try{
            logger.info("Process store to Redis...");
            RedisConnection<String, String> connection = Config.REDISCONF();
            connection.set(key, value);
            done = true;
            logger.info("Process store to Redis success...");
        }catch (Exception e){
            done = false;
            logger.info(e.getMessage());
        }

        return done;
    }

    public boolean rDelete(String key) {
        try{
            logger.info("LogOut Procces..!");
            RedisConnection<String, String> redis = Config.REDISCONF();
            redis.del(key);
            return true;
        }catch (Exception e) {
            logger.error("Logout error with redis..!");
        }
        return false;
    }
}
