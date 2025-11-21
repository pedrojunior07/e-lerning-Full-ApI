package com.elearning.e_learning_core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Repository.UserRepository;
import com.elearning.e_learning_core.Repository.VerificationTokenRepository;
import com.elearning.e_learning_core.model.Usr;
import com.elearning.e_learning_core.model.VerificationToken;

@Service
public class UsrService implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository tokenRepository;

    public UsrService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public Usr getUser(String verificationToken) {
        Usr user = tokenRepository.findByToken(verificationToken).getUser();
        return user;
    }

    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    public void saveRegisteredUser(Usr user) {
        userRepository.save(user);
    }

    public ApiResponse<?> getByUserAuthenticated(Long id) {
        Usr user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ApiResponse<>("error", "User not found", 404, null);
        }
        return new ApiResponse<>("success", "User found", 200, user);

    }

    public void createVerificationToken(Usr user, String token) { 
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationToken.setExpiryDate(
                VerificationToken.calculateExpiryDate(60 * 24));

        tokenRepository.save(verificationToken);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usr user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

}
