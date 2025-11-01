package com.example.apitcc.controller;

import com.example.apitcc.model.dto.TrabalhoDTO;
import com.example.apitcc.service.TrabalhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/trabalhos")
public class TrabalhoController {
    
    @Autowired
    private TrabalhoService trabalhoService;
    
    /**
     * Listar todos os trabalhos
     * GET /api/trabalhos/listar
     */
    @GetMapping("/listar")
    public ResponseEntity<List<TrabalhoDTO>> listarTrabalhos() {
        List<TrabalhoDTO> trabalhos = trabalhoService.listarTodosTrabalhos();
        return ResponseEntity.ok(trabalhos);
    }
    
    /**
     * Buscar trabalho por ID
     * GET /api/trabalhos/buscar/{id}
     */
    @GetMapping("/buscar/{id}")
    public ResponseEntity<TrabalhoDTO> buscarTrabalho(@PathVariable String id) {
        TrabalhoDTO trabalho = trabalhoService.buscarTrabalhoPorId(id);
        return ResponseEntity.ok(trabalho);
    }
    
    /**
     * Criar novo trabalho
     * POST /api/trabalhos/criar
     */
    @PostMapping("/criar")
    public ResponseEntity<Map<String, Object>> criarTrabalho(@RequestBody TrabalhoDTO trabalhoDTO) {
        TrabalhoDTO trabalhoCriado = trabalhoService.criarTrabalho(trabalhoDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", trabalhoCriado.getId());
        response.put("mensagem", "Trabalho cadastrado com sucesso");
        response.put("trabalho", trabalhoCriado);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Atualizar trabalho
     * PUT /api/trabalhos/{id}/atualizar
     */
    @PutMapping("/{id}/atualizar")
    public ResponseEntity<Map<String, Object>> atualizarTrabalho(
            @PathVariable String id, 
            @RequestBody TrabalhoDTO trabalhoDTO) {
        
        TrabalhoDTO trabalhoAtualizado = trabalhoService.atualizarTrabalho(id, trabalhoDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "sucesso");
        response.put("mensagem", "Trabalho atualizado com sucesso");
        response.put("id", id);
        response.put("trabalho", trabalhoAtualizado);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Excluir trabalho
     * DELETE /api/trabalhos/{id}/excluir
     */
    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Map<String, String>> excluirTrabalho(@PathVariable String id) {
        trabalhoService.excluirTrabalho(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("sucesso", "Trabalho excluído com sucesso.");
        response.put("id", id);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Buscar trabalhos por empresa
     * GET /api/trabalhos/empresa/{idEmpresa}
     */
    @GetMapping("/empresa/{idEmpresa}")
    public ResponseEntity<List<TrabalhoDTO>> buscarTrabalhosPorEmpresa(@PathVariable String idEmpresa) {
        List<TrabalhoDTO> trabalhos = trabalhoService.buscarTrabalhosPorEmpresa(idEmpresa);
        return ResponseEntity.ok(trabalhos);
    }
    
    /**
     * Buscar trabalhos por tipo
     * GET /api/trabalhos/tipo/{tipoTrabalho}
     */
    @GetMapping("/tipo/{tipoTrabalho}")
    public ResponseEntity<List<TrabalhoDTO>> buscarPorTipoTrabalho(@PathVariable String tipoTrabalho) {
        List<TrabalhoDTO> trabalhos = trabalhoService.buscarPorTipoTrabalho(tipoTrabalho);
        return ResponseEntity.ok(trabalhos);
    }
    
    /**
     * Buscar trabalhos por habilidades necessárias
     * GET /api/trabalhos/habilidades/{habilidade}
     */
    @GetMapping("/habilidades/{habilidade}")
    public ResponseEntity<List<TrabalhoDTO>> buscarPorHabilidades(@PathVariable String habilidade) {
        List<TrabalhoDTO> trabalhos = trabalhoService.buscarPorHabilidadesNecessarias(habilidade);
        return ResponseEntity.ok(trabalhos);
    }
    
    /**
     * Buscar trabalhos por quantidade mínima de vagas
     * GET /api/trabalhos/vagas/{quantidadeMinima}
     */
    @GetMapping("/vagas/{quantidadeMinima}")
    public ResponseEntity<List<TrabalhoDTO>> buscarPorQuantidadeVagas(@PathVariable String quantidadeMinima) {
        List<TrabalhoDTO> trabalhos = trabalhoService.buscarPorQuantidadeVagas(quantidadeMinima);
        return ResponseEntity.ok(trabalhos);
    }
    
    /**
     * Buscar trabalhos por descrição
     * GET /api/trabalhos/descricao/{descricao}
     */
    @GetMapping("/descricao/{descricao}")
    public ResponseEntity<List<TrabalhoDTO>> buscarPorDescricao(@PathVariable String descricao) {
        List<TrabalhoDTO> trabalhos = trabalhoService.buscarPorDescricao(descricao);
        return ResponseEntity.ok(trabalhos);
    }
    
    /**
     * Excluir todos os trabalhos de uma empresa
     * DELETE /api/trabalhos/empresa/{idEmpresa}/excluir-todos
     */
    @DeleteMapping("/empresa/{idEmpresa}/excluir-todos")
    public ResponseEntity<Map<String, String>> excluirTrabalhosPorEmpresa(@PathVariable String idEmpresa) {
        trabalhoService.excluirTrabalhosPorEmpresa(idEmpresa);
        
        Map<String, String> response = new HashMap<>();
        response.put("sucesso", "Todos os trabalhos da empresa foram excluídos com sucesso.");
        response.put("idEmpresa", idEmpresa);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint de status da API
     * GET /api/trabalhos/status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "online");
        status.put("service", "TrabalhoService");
        status.put("version", "1.0.0");
        status.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.ok(status);
    }
}