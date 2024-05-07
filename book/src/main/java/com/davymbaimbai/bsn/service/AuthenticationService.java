package com.davymbaimbai.bsn.service;

import com.davymbaimbai.bsn.dto.AuthenticationRequest;
import com.davymbaimbai.bsn.dto.AuthenticationResponse;
import com.davymbaimbai.bsn.dto.RegistrationRequest;
import com.davymbaimbai.bsn.entity.Token;
import com.davymbaimbai.bsn.entity.User;
import com.davymbaimbai.bsn.enums.EmailTemplateName;
import com.davymbaimbai.bsn.repository.RoleRepository;
import com.davymbaimbai.bsn.repository.TokenRepository;
import com.davymbaimbai.bsn.repository.UserRepository;
import com.davymbaimbai.bsn.security.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivation(user);

        emailService.sendMail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"

        );
    }

    private String generateAndSaveActivation(User user) {
        String generateToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generateToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generateToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.fullName());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public void activateAccount(String token) throws MessagingException {
        Token tokenSaved = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException(" Invalid Token"));
        if (LocalDateTime.now().isAfter(tokenSaved.getExpiresAt())) {
            sendValidationEmail(tokenSaved.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }
        var user = userRepository.findById(tokenSaved.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        tokenSaved.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(tokenSaved);
    }
}
