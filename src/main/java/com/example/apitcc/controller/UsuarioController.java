package com.example.apitcc.controller;

import com.example.apitcc.model.dto.UsuarioDTO;
import com.example.apitcc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Listar todos os usuários
     * GET /api/usuarios/listar
     */
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.listarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }
    
    /**
     * Buscar usuário por ID
     * GET /api/usuarios/buscar/{id}
     */
    @GetMapping("/buscar/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuario(@PathVariable String id) {
        UsuarioDTO usuario = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }
    
    /**
     * Criar novo usuário
     * POST /api/usuarios/criar
     */
    @PostMapping("/criar")
    public ResponseEntity<Map<String, Object>> criarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioCriado = usuarioService.criarUsuario(usuarioDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", usuarioCriado.getId());
        response.put("mensagem", "Usuário cadastrado com sucesso");
        response.put("usuario", usuarioCriado);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Atualizar usuário
     * PUT /api/usuarios/{id}/atualizar
     */
    @PutMapping("/{id}/atualizar")
    public ResponseEntity<Map<String, Object>> atualizarUsuario(
            @PathVariable String id, 
            @RequestBody UsuarioDTO usuarioDTO) {
        
        UsuarioDTO usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "sucesso");
        response.put("mensagem", "Usuário atualizado com sucesso");
        response.put("id", id);
        response.put("usuario", usuarioAtualizado);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Excluir usuário
     * DELETE /api/usuarios/{id}/excluir
     */
    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Map<String, String>> excluirUsuario(@PathVariable String id) {
        usuarioService.excluirUsuario(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("sucesso", "Usuário excluído com sucesso.");
        response.put("id", id);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Buscar usuários por habilidades
     * GET /api/usuarios/habilidades/{habilidade}
     */
    @GetMapping("/habilidades/{habilidade}")
    public ResponseEntity<List<UsuarioDTO>> buscarPorHabilidades(@PathVariable String habilidade) {
        List<UsuarioDTO> usuarios = usuarioService.buscarPorHabilidades(habilidade);
        return ResponseEntity.ok(usuarios);
    }
    
    /**
     * Buscar usuário por email
     * GET /api/usuarios/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@PathVariable String email) {
        UsuarioDTO usuario = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(usuario);
    }
    
    /**
     * Buscar usuários por horas disponíveis mínimas
     * GET /api/usuarios/horas/{horasMinimas}
     */
    @GetMapping("/horas/{horasMinimas}")
    public ResponseEntity<List<UsuarioDTO>> buscarPorHorasDisponiveis(@PathVariable String horasMinimas) {
        List<UsuarioDTO> usuarios = usuarioService.buscarPorHorasDisponiveis(horasMinimas);
        return ResponseEntity.ok(usuarios);
    }
    
    /**
     * Endpoint para verificar se email já existe
     * GET /api/usuarios/verificar-email/{email}
     */
    @GetMapping("/verificar-email/{email}")
    public ResponseEntity<Map<String, Boolean>> verificarEmail(@PathVariable String email) {
        // Esta funcionalidade pode ser implementada no service futuramente
        Map<String, Boolean> response = new HashMap<>();
        response.put("existe", false); // Placeholder
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint para verificar se telefone já existe
     * GET /api/usuarios/verificar-telefone/{telefone}
     */
    @GetMapping("/verificar-telefone/{telefone}")
    public ResponseEntity<Map<String, Boolean>> verificarTelefone(@PathVariable String telefone) {
        // Esta funcionalidade pode ser implementada no service futuramente
        Map<String, Boolean> response = new HashMap<>();
        response.put("existe", false); // Placeholder
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint de status da API
     * GET /api/usuarios/status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "online");
        status.put("service", "UsuarioService");
        status.put("version", "1.0.0");
        status.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.ok(status);
    }
}