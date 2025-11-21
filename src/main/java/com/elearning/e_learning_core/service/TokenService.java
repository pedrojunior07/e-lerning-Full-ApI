package com.elearning.e_learning_core.service;

import org.springframework.stereotype.Service;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.elearning.e_learning_core.model.Usr;

@Service
public class TokenService {

    private static final String SECRET_KEY = "eyes-of-the-world-are-watching";

    public String generateToken(Usr user) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            String token = com.auth0.jwt.JWT.create()
                    .withIssuer("e-learning")
                    .withSubject(user.getEmail())
                    .withClaim("userId", user.getId())
                    .withExpiresAt(new java.util.Date(System.currentTimeMillis() + 999999999))
                    .withClaim("role", user.getRole().getRoleName())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            e.printStackTrace();
            throw new RuntimeException("Token generation failed: error");
        }

    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT decodedJWT = com.auth0.jwt.JWT.require(algorithm)
                    .withIssuer("e-learning")
                    .build()
                    .verify(token);

            return decodedJWT.getClaim("sub").asString();

        } catch (JWTVerificationException e) {
            e.printStackTrace();

            throw new RuntimeException("Invalid token: " + e.getMessage());
        }
    }

}
