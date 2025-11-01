package com.example.apitcc.controller;

import com.example.apitcc.model.dto.MatchDTO;
import com.example.apitcc.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/matches")
public class MatchController {
    
    @Autowired
    private MatchService matchService;
    
    /**
     * Listar todos os matches
     * GET /api/matches/listar
     */
    @GetMapping("/listar")
    public ResponseEntity<List<MatchDTO>> listarMatches() {
        List<MatchDTO> matches = matchService.listarTodosMatches();
        return ResponseEntity.ok(matches);
    }
    
    /**
     * Buscar match por ID
     * GET /api/matches/buscar/{id}
     */
    @GetMapping("/buscar/{id}")
    public ResponseEntity<MatchDTO> buscarMatch(@PathVariable String id) {
        MatchDTO match = matchService.buscarMatchPorId(id);
        return ResponseEntity.ok(match);
    }
    
    /**
     * Criar novo match
     * POST /api/matches/criar
     */
    @PostMapping("/criar")
    public ResponseEntity<Map<String, Object>> criarMatch(@RequestBody MatchDTO matchDTO) {
        MatchDTO matchCriado = matchService.criarMatch(matchDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", matchCriado.getId());
        response.put("mensagem", "Match cadastrado com sucesso");
        response.put("match", matchCriado);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Atualizar match
     * PUT /api/matches/{id}/atualizar
     */
    @PutMapping("/{id}/atualizar")
    public ResponseEntity<Map<String, Object>> atualizarMatch(
            @PathVariable String id, 
            @RequestBody MatchDTO matchDTO) {
        
        MatchDTO matchAtualizado = matchService.atualizarMatch(id, matchDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "sucesso");
        response.put("mensagem", "Match atualizado com sucesso");
        response.put("id", id);
        response.put("match", matchAtualizado);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Excluir match
     * DELETE /api/matches/{id}/excluir
     */
    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Map<String, String>> excluirMatch(@PathVariable String id) {
        matchService.excluirMatch(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("sucesso", "Match excluído com sucesso.");
        response.put("id", id);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Buscar matches por usuário
     * GET /api/matches/usuario/{idUsuario}
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<MatchDTO>> buscarMatchesPorUsuario(@PathVariable String idUsuario) {
        List<MatchDTO> matches = matchService.buscarMatchesPorUsuario(idUsuario);
        return ResponseEntity.ok(matches);
    }
    
    /**
     * Buscar matches por trabalho
     * GET /api/matches/trabalho/{idTrabalho}
     */
    @GetMapping("/trabalho/{idTrabalho}")
    public ResponseEntity<List<MatchDTO>> buscarMatchesPorTrabalho(@PathVariable String idTrabalho) {
        List<MatchDTO> matches = matchService.buscarMatchesPorTrabalho(idTrabalho);
        return ResponseEntity.ok(matches);
    }
    
    /**
     * Buscar matches por status
     * GET /api/matches/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<MatchDTO>> buscarPorStatus(@PathVariable String status) {
        List<MatchDTO> matches = matchService.buscarPorStatus(status);
        return ResponseEntity.ok(matches);
    }
    
    /**
     * Buscar matches ativos
     * GET /api/matches/ativos
     */
    @GetMapping("/ativos")
    public ResponseEntity<List<MatchDTO>> buscarMatchesAtivos() {
        List<MatchDTO> matches = matchService.buscarMatchesAtivos();
        return ResponseEntity.ok(matches);
    }
    
    /**
     * Buscar matches finalizados
     * GET /api/matches/finalizados
     */
    @GetMapping("/finalizados")
    public ResponseEntity<List<MatchDTO>> buscarMatchesFinalizados() {
        List<MatchDTO> matches = matchService.buscarMatchesFinalizados();
        return ResponseEntity.ok(matches);
    }
    
    /**
     * Buscar matches por avaliação mínima
     * GET /api/matches/avaliacao/{avaliacaoMinima}
     */
    @GetMapping("/avaliacao/{avaliacaoMinima}")
    public ResponseEntity<List<MatchDTO>> buscarPorAvaliacao(@PathVariable String avaliacaoMinima) {
        List<MatchDTO> matches = matchService.buscarPorAvaliacao(avaliacaoMinima);
        return ResponseEntity.ok(matches);
    }
    
    /**
     * Buscar match específico entre usuário e trabalho
     * GET /api/matches/usuario/{idUsuario}/trabalho/{idTrabalho}
     */
    @GetMapping("/usuario/{idUsuario}/trabalho/{idTrabalho}")
    public ResponseEntity<MatchDTO> buscarMatchPorUsuarioETrabalho(
            @PathVariable String idUsuario, 
            @PathVariable String idTrabalho) {
        MatchDTO match = matchService.buscarMatchPorUsuarioETrabalho(idUsuario, idTrabalho);
        return ResponseEntity.ok(match);
    }
    
    /**
     * Excluir todos os matches de um usuário
     * DELETE /api/matches/usuario/{idUsuario}/excluir-todos
     */
    @DeleteMapping("/usuario/{idUsuario}/excluir-todos")
    public ResponseEntity<Map<String, String>> excluirMatchesPorUsuario(@PathVariable String idUsuario) {
        matchService.excluirMatchesPorUsuario(idUsuario);
        
        Map<String, String> response = new HashMap<>();
        response.put("sucesso", "Todos os matches do usuário foram excluídos com sucesso.");
        response.put("idUsuario", idUsuario);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Excluir todos os matches de um trabalho
     * DELETE /api/matches/trabalho/{idTrabalho}/excluir-todos
     */
    @DeleteMapping("/trabalho/{idTrabalho}/excluir-todos")
    public ResponseEntity<Map<String, String>> excluirMatchesPorTrabalho(@PathVariable String idTrabalho) {
        matchService.excluirMatchesPorTrabalho(idTrabalho);
        
        Map<String, String> response = new HashMap<>();
        response.put("sucesso", "Todos os matches do trabalho foram excluídos com sucesso.");
        response.put("idTrabalho", idTrabalho);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint de status da API
     * GET /api/matches/status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "online");
        status.put("service", "MatchService");
        status.put("version", "1.0.0");
        status.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.ok(status);
    }
}