package org.example.userauthenticationservice_may.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
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

    private SecretKey secretKey;

    public AuthService(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder,SessionRepository sessionRepository,SecretKey secretKey) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sessionRepository = sessionRepository;
        this.secretKey = secretKey;
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
        Long iat = System.currentTimeMillis();
        Long exp = iat+100000000;
        jwtData.put("iat",iat);
        jwtData.put("exp", exp);
        System.out.println("iat = "+ iat);
        System.out.println("exp = "+exp);


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

    public Boolean validateToken(Long userId, String token) {
         Optional<Session> optionalSession = sessionRepository.findByTokenEquals(token);

         if(optionalSession.isEmpty()) {
             return false;
         }

        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
         Claims claims = jwtParser.parseSignedClaims(token).getPayload();

        Long exp = (Long)claims.get("exp");
        System.out.println("exp = "+exp);
        Date expiryInHumanReadableFormat = new Date(exp);


         Date rightNowInHumanReadableFormat = new Date(System.currentTimeMillis());
         System.out.println("exp = "+expiryInHumanReadableFormat);
         System.out.println("time right now ="+rightNowInHumanReadableFormat);
         if(rightNowInHumanReadableFormat.after(expiryInHumanReadableFormat)) {
            return false;
        }

        return true;
    }
}
