package org.example.userauthenticationservice_may.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthenticationservice_may.models.Session;
import org.example.userauthenticationservice_may.models.SessionStatus;
import org.example.userauthenticationservice_may.models.User;
import org.example.userauthenticationservice_may.repositories.SessionRepository;
import org.example.userauthenticationservice_may.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.http.HttpHeaders;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private SessionRepository sessionRepository;

    public AuthService(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder,SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public User signup(String email, String password) {
         Optional<User> userOptional = userRepository.findByEmail(email);
         if(userOptional.isPresent()) {
             return userOptional.get();
         }

         User user = new User();
         user.setEmail(email);
         user.setPassword(bCryptPasswordEncoder.encode(password));
         userRepository.save(user);

         return user;
    }

    @Override
    public Pair<User,MultiValueMap<String,String>> login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return null;
        }


        //Generating Token
        //Token Generation
//        String message = "{\n" +
//                "   \"email\": \"anurag@scaler.com\",\n" +
//                "   \"roles\": [\n" +
//                "      \"instructor\",\n" +
//                "      \"buddy\"\n" +
//                "   ],\n" +
//                "   \"expirationDate\": \"2ndJuly2024\"\n" +
//                "}";

        //byte[] content = message.getBytes(StandardCharsets.UTF_8);
        //String token = Jwts.builder().content(content).compact();

        Map<String,Object> jwtData = new HashMap<>();
        jwtData.put("email",user.getEmail());
        jwtData.put("roles",user.getRoles());
        Long nowInMillis = System.currentTimeMillis();
        jwtData.put("iat",new Date(nowInMillis));
        jwtData.put("exp", new Date(nowInMillis+100000));

        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secretKey = algorithm.key().build();
        //String token = Jwts.builder().content(content).signWith(secretKey).compact();
        String token = Jwts.builder().claims(jwtData).signWith(secretKey).compact();

        Session session = new Session();
        session.setUser(user);
        session.setToken(token);
        session.setSessionStatus(SessionStatus.ACTIVE);
        sessionRepository.save(session);

        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE,token);

        return new Pair<User,MultiValueMap<String,String>>(user,headers);
    }
}
