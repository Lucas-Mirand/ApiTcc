package com.example.apitcc.repository;

import com.example.apitcc.model.entity.Trabalho;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TrabalhoRepository {
    
    /**
     * Busca todos os trabalhos
     */
    public List<Trabalho> findAll() {
        return Trabalho.trabalhos;
    }
    
    /**
     * Busca trabalho por ID
     */
    public Optional<Trabalho> findById(String id) {
        return Trabalho.trabalhos.stream()
                .filter(trabalho -> trabalho.getId().equals(id))
                .findFirst();
    }
    
    /**
     * Busca trabalhos por ID da empresa
     */
    public List<Trabalho> findByIdEmpresa(String idEmpresa) {
        return Trabalho.trabalhos.stream()
                .filter(trabalho -> trabalho.getIdEmpresa() != null && trabalho.getIdEmpresa().equals(idEmpresa))
                .collect(Collectors.toList());
    }
    
    /**
     * Busca trabalhos por nome da empresa
     */
    public List<Trabalho> findByNomeEmpresaContaining(String nomeEmpresa) {
        return Trabalho.trabalhos.stream()
                .filter(trabalho -> trabalho.getNomeEmpresa() != null && 
                        trabalho.getNomeEmpresa().toLowerCase().contains(nomeEmpresa.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Busca trabalhos por tipo de trabalho
     */
    public List<Trabalho> findByTipoTrabalhoContaining(String tipoTrabalho) {
        return Trabalho.trabalhos.stream()
                .filter(trabalho -> trabalho.getTipoTrabalho() != null && 
                        trabalho.getTipoTrabalho().toLowerCase().contains(tipoTrabalho.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Busca trabalhos por habilidades necessárias
     */
    public List<Trabalho> findByHabilidadesNecessariasContaining(String habilidade) {
        return Trabalho.trabalhos.stream()
                .filter(trabalho -> trabalho.getHabilidadesNecessarias() != null && 
                        trabalho.getHabilidadesNecessarias().toLowerCase().contains(habilidade.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Busca trabalhos por descrição
     */
    public List<Trabalho> findByDescricaoTrabalhoContaining(String descricao) {
        return Trabalho.trabalhos.stream()
                .filter(trabalho -> trabalho.getDescricaoTrabalho() != null && 
                        trabalho.getDescricaoTrabalho().toLowerCase().contains(descricao.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Busca trabalhos com quantidade de vagas maior ou igual
     */
    public List<Trabalho> findByQuantidadeDeVagasGreaterThanEqual(String quantidadeMinima) {
        return Trabalho.trabalhos.stream()
                .filter(trabalho -> {
                    if (trabalho.getQuantidadeDeVagas() == null) return false;
                    try {
                        int vagasTrabalho = Integer.parseInt(trabalho.getQuantidadeDeVagas());
                        int vagasMinimas = Integer.parseInt(quantidadeMinima);
                        return vagasTrabalho >= vagasMinimas;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Salva um trabalho (adiciona à lista)
     */
    public Trabalho save(Trabalho trabalho) {
        // Se é um novo trabalho (sem ID ou ID novo)
        if (trabalho.getId() == null || trabalho.getId().isEmpty()) {
            trabalho.setId(String.valueOf(Trabalho.proximoId++));
            Trabalho.trabalhos.add(trabalho);
        } else {
            // Atualiza trabalho existente
            Optional<Trabalho> existingTrabalho = findById(trabalho.getId());
            if (existingTrabalho.isPresent()) {
                int index = Trabalho.trabalhos.indexOf(existingTrabalho.get());
                Trabalho.trabalhos.set(index, trabalho);
            } else {
                // Se não encontrou, adiciona como novo
                Trabalho.trabalhos.add(trabalho);
            }
        }
        return trabalho;
    }
    
    /**
     * Deleta trabalho por ID
     */
    public boolean deleteById(String id) {
        return Trabalho.trabalhos.removeIf(trabalho -> trabalho.getId().equals(id));
    }
    
    /**
     * Deleta todos os trabalhos de uma empresa
     */
    public boolean deleteByIdEmpresa(String idEmpresa) {
        return Trabalho.trabalhos.removeIf(trabalho -> trabalho.getIdEmpresa() != null && 
                                                      trabalho.getIdEmpresa().equals(idEmpresa));
    }
    
    /**
     * Verifica se existe trabalho com o ID
     */
    public boolean existsById(String id) {
        return Trabalho.trabalhos.stream()
                .anyMatch(trabalho -> trabalho.getId().equals(id));
    }
    
    /**
     * Verifica se existe trabalho para a empresa
     */
    public boolean existsByIdEmpresa(String idEmpresa) {
        return Trabalho.trabalhos.stream()
                .anyMatch(trabalho -> trabalho.getIdEmpresa() != null && trabalho.getIdEmpresa().equals(idEmpresa));
    }
    
    /**
     * Conta total de trabalhos
     */
    public long count() {
        return Trabalho.trabalhos.size();
    }
    
    /**
     * Conta trabalhos de uma empresa
     */
    public long countByIdEmpresa(String idEmpresa) {
        return Trabalho.trabalhos.stream()
                .filter(trabalho -> trabalho.getIdEmpresa() != null && trabalho.getIdEmpresa().equals(idEmpresa))
                .count();
    }
    
    /**
     * Deleta todos os trabalhos (usado para testes)
     */
    public void deleteAll() {
        Trabalho.trabalhos.clear();
        Trabalho.proximoId = 1; // Reset do contador
    }
}