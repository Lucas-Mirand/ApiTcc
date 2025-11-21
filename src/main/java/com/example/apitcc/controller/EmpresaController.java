package com.example.apitcc.controller;

import com.example.apitcc.exception.ResourceNotFoundException;
import com.example.apitcc.model.dto.EmpresaDTO;
import com.example.apitcc.model.entity.Empresa;
import com.example.apitcc.repository.EmpresaRepository;
import com.example.apitcc.service.EmpresaService;
import com.example.apitcc.util.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/empresas")

public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private EmpresaRepository empresaRepository;

    /**
     * Listar todas as empresas
     * GET /api/empresas/listar
     */
    @GetMapping("/listar")
    public ResponseEntity<List<EmpresaDTO>> listarEmpresas() {
        List<EmpresaDTO> empresas = empresaService.listarTodasEmpresas();
        return ResponseEntity.ok(empresas);
    }

    /**
     * Buscar empresa por ID
     * GET /api/empresas/buscar/{id}
     */
    @GetMapping("/buscar/{id}")
    public ResponseEntity<EmpresaDTO> buscarEmpresa(@PathVariable String id) {
        EmpresaDTO empresa = empresaService.buscarEmpresaPorId(id);
        return ResponseEntity.ok(empresa);
    }

    /**
     * Criar nova empresa
     * POST /api/empresas/criar
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<EmpresaDTO> buscarPorEmail(@PathVariable String email) {
        EmpresaDTO empresa = empresaService.buscarPorEmail(email);
        return ResponseEntity.ok(empresa);
    }

    @PostMapping("/criar")
    public ResponseEntity<Map<String, Object>> criarEmpresa(@RequestBody EmpresaDTO empresaDTO) {
        EmpresaDTO empresaCriada = empresaService.criarEmpresa(empresaDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("id", empresaCriada.getId());
        response.put("mensagem", "Empresa cadastrada com sucesso");
        response.put("empresa", empresaCriada);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualizar empresa
     * PUT /api/empresas/{id}/atualizar
     */
    @PutMapping("/{id}/atualizar")
    public ResponseEntity<Map<String, Object>> atualizarEmpresa(
            @PathVariable String id,
            @RequestBody EmpresaDTO empresaDTO) {

        EmpresaDTO empresaAtualizada = empresaService.atualizarEmpresa(id, empresaDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "sucesso");
        response.put("mensagem", "Empresa atualizada com sucesso");
        response.put("id", id);
        response.put("empresa", empresaAtualizada);

        return ResponseEntity.ok(response);
    }

    /**
     * Excluir empresa
     * DELETE /api/empresas/{id}/excluir
     */
    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Map<String, String>> excluirEmpresa(@PathVariable String id) {
        empresaService.excluirEmpresa(id);

        Map<String, String> response = new HashMap<>();
        response.put("sucesso", "Empresa excluÃ­da com sucesso.");
        response.put("id", id);

        return ResponseEntity.ok(response);
    }

    /**
     * Buscar empresas por Ã¡rea de atuaÃ§Ã£o
     * GET /api/empresas/area/{area}
     */
    @GetMapping("/area/{area}")
    public ResponseEntity<List<EmpresaDTO>> buscarPorArea(@PathVariable String area) {
        List<EmpresaDTO> empresas = empresaService.buscarPorAreaAtuacao(area);
        return ResponseEntity.ok(empresas);
    }

    /**
     * Buscar empresa por CNPJ
     * GET /api/empresas/cnpj/{cnpj}
     */

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<EmpresaDTO> buscarPorCnpj(@PathVariable String cnpj) {
        // OPÃ‡ÃƒO 1: Manter sua implementaÃ§Ã£o atual (funciona apenas em dev)
        Empresa empresa = empresaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa nÃ£o encontrada com CNPJ: " + cnpj));
        return ResponseEntity.ok(ModelMapper.toEmpresaDTO(empresa));
    }

    /**
     * Endpoint para verificar se email jÃ¡ existe
     * GET /api/empresas/verificar-email/{email}
     */
    @GetMapping("/verificar-email/{email}")
    public ResponseEntity<Map<String, Boolean>> verificarEmail(@PathVariable String email) {
        // Esta funcionalidade pode ser implementada no service futuramente
        Map<String, Boolean> response = new HashMap<>();
        response.put("existe", false); // Placeholder
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para verificar se CNPJ jÃ¡ existe
     * GET /api/empresas/verificar-cnpj/{cnpj}
     */
    @GetMapping("/verificar-cnpj/{cnpj}")
    public ResponseEntity<Map<String, Boolean>> verificarCnpj(@PathVariable String cnpj) {
        // Esta funcionalidade pode ser implementada no service futuramente
        Map<String, Boolean> response = new HashMap<>();
        response.put("existe", false); // Placeholder
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de status da API
     * GET /api/empresas/status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "online");
        status.put("service", "EmpresaService");
        status.put("version", "1.0.0");
        status.put("timestamp", java.time.LocalDateTime.now());

        return ResponseEntity.ok(status);
    }
}
