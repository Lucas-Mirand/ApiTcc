package com.example.apitcc.repository;

import com.example.apitcc.model.entity.Empresa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class EmpresaRepository {
    
    /**
     * Busca todas as empresas
     */
    public List<Empresa> findAll() {
        return Empresa.empresas;
    }
    
    /**
     * Busca empresa por ID
     */
    public Optional<Empresa> findById(String id) {
        return Empresa.empresas.stream()
                .filter(empresa -> empresa.getId().equals(id))
                .findFirst();
    }
    
    /**
     * Busca empresa por CNPJ
     */
    public Optional<Empresa> findByCnpj(String cnpj) {
        return Empresa.empresas.stream()
                .filter(empresa -> empresa.getCnpj() != null && empresa.getCnpj().equals(cnpj))
                .findFirst();
    }
    
    /**
     * Busca empresa por email
     */
    public Optional<Empresa> findByEmail(String email) {
        return Empresa.empresas.stream()
                .filter(empresa -> empresa.getEmail() != null && empresa.getEmail().equals(email))
                .findFirst();
    }
    
    /**
     * Busca empresas por Ã¡rea de atuaÃ§Ã£o
     */
    public List<Empresa> findByAreaAtuacaoContaining(String areaAtuacao) {
        return Empresa.empresas.stream()
                .filter(empresa -> empresa.getAreaAtuacao() != null && 
                        empresa.getAreaAtuacao().toLowerCase().contains(areaAtuacao.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Busca empresas por pÃºblico alvo
     */
    public List<Empresa> findByPublicoAlvoContaining(String publicoAlvo) {
        return Empresa.empresas.stream()
                .filter(empresa -> empresa.getPublicoAlvo() != null && 
                        empresa.getPublicoAlvo().toLowerCase().contains(publicoAlvo.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Busca empresas por nome
     */
    public List<Empresa> findByNomeContaining(String nome) {
        return Empresa.empresas.stream()
                .filter(empresa -> empresa.getNome() != null && 
                        empresa.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Salva uma empresa (adiciona Ã  lista)
     */
    public Empresa save(Empresa empresa) {
        // Se Ã© uma nova empresa (sem ID ou ID novo)
        if (empresa.getId() == null || empresa.getId().isEmpty()) {
            empresa.setId(String.valueOf(Empresa.proximoId++));
            Empresa.empresas.add(empresa);
        } else {
            // Atualiza empresa existente
            Optional<Empresa> existingEmpresa = findById(empresa.getId());
            if (existingEmpresa.isPresent()) {
                int index = Empresa.empresas.indexOf(existingEmpresa.get());
                Empresa.empresas.set(index, empresa);
            } else {
                // Se nÃ£o encontrou, adiciona como nova
                Empresa.empresas.add(empresa);
            }
        }
        return empresa;
    }
    
    /**
     * Deleta empresa por ID
     */
    public boolean deleteById(String id) {
        return Empresa.empresas.removeIf(empresa -> empresa.getId().equals(id));
    }
    
    /**
     * Verifica se existe empresa com o ID
     */
    public boolean existsById(String id) {
        return Empresa.empresas.stream()
                .anyMatch(empresa -> empresa.getId().equals(id));
    }
    
    /**
     * Verifica se existe empresa com o CNPJ
     */
    public boolean existsByCnpj(String cnpj) {
        return Empresa.empresas.stream()
                .anyMatch(empresa -> empresa.getCnpj() != null && empresa.getCnpj().equals(cnpj));
    }
    
    /**
     * Verifica se existe empresa com o email
     */
    public boolean existsByEmail(String email) {
        return Empresa.empresas.stream()
                .anyMatch(empresa -> empresa.getEmail() != null && empresa.getEmail().equals(email));
    }
    
    /**
     * Conta total de empresas
     */
    public long count() {
        return Empresa.empresas.size();
    }
    
    /**
     * Deleta todas as empresas (usado para testes)
     */
    public void deleteAll() {
        Empresa.empresas.clear();
        Empresa.proximoId = 1; // Reset do contador
    }
}