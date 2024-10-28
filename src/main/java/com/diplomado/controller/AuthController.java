package com.diplomado.controller;

import com.diplomado.model.ApiResponse;
import com.diplomado.model.JwtResponse;
import com.diplomado.model.Usuario;
import com.diplomado.security.JwtTokenProvider;
import com.diplomado.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody Usuario user) {
        // Verificar si el correo ya está registrado
        if (userService.existsByCorreo(user.getCorreo())) {
            ApiResponse<String> response = new ApiResponse<>("error", "El correo ya está registrado.", null);
            return ResponseEntity.badRequest().body(response);
        }

        // Codificar la contraseña antes de guardarla
        user.setContrasena(passwordEncoder.encode(user.getContrasena()));

        // Guardar el usuario en la base de datos
        userService.save(user);

        // Respuesta de éxito
        ApiResponse<String> response = new ApiResponse<>("success", "Usuario registrado con éxito.", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@RequestBody Usuario user) {
        // Autenticar al usuario con correo y contraseña
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getCorreo(), user.getContrasena()));

        // Generar el token JWT
        String token = tokenProvider.generateToken(authentication.getName());

        // Crear el modelo de respuesta JWT
        JwtResponse jwtResponse = new JwtResponse(token);

        // Crear el modelo de respuesta estándar
        ApiResponse<JwtResponse> response = new ApiResponse<>("success", "Inicio de sesión exitoso", jwtResponse);

        return ResponseEntity.ok(response);
    }
}