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

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@RequestParam String correo, @RequestParam String contrasena) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(correo, contrasena));

        // Generar el token JWT
        String token = tokenProvider.generateToken(authentication.getName());

        // Crear el modelo de respuesta JWT
        JwtResponse jwtResponse = new JwtResponse(token);

        // Crear el modelo de respuesta estándar
        ApiResponse<JwtResponse> response = new ApiResponse<>("success", "Inicio de sesión exitoso", jwtResponse);

        return ResponseEntity.ok(response); // Devolver el token en un cuerpo de respuesta estándar
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody Usuario user) {
        if (userService.existsByCorreo(user.getCorreo())) {
            ApiResponse<String> response = new ApiResponse<>("error", "El correo ya está registrado.", null);
            return ResponseEntity.badRequest().body(response);
        }

        userService.save(user);
        ApiResponse<String> response = new ApiResponse<>("success", "Usuario registrado con éxito.", null);
        return ResponseEntity.ok(response);
    }
}