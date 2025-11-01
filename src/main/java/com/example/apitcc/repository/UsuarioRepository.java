package com.example.apitcc.repository;

import com.example.apitcc.model.entity.Usuario;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UsuarioRepository {
    
    /**
     * Busca todos os usuários
     */
    public List<Usuario> findAll() {
        return Usuario.usuarios;
    }
    
    /**
     * Busca usuário por ID
     */
    public Optional<Usuario> findById(String id) {
        return Usuario.usuarios.stream()
                .filter(usuario -> usuario.getId().equals(id))
                .findFirst();
    }
    
    /**
     * Busca usuário por email
     */
    public Optional<Usuario> findByEmail(String email) {
        return Usuario.usuarios.stream()
                .filter(usuario -> usuario.getEmail() != null && usuario.getEmail().equals(email))
                .findFirst();
    }
    
    /**
     * Busca usuários por habilidades
     */
    public List<Usuario> findByHabilidadesContaining(String habilidade) {
        return Usuario.usuarios.stream()
                .filter(usuario -> usuario.getHabilidades() != null && 
                        usuario.getHabilidades().toLowerCase().contains(habilidade.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Busca usuários por nome
     */
    public List<Usuario> findByNomeContaining(String nome) {
        return Usuario.usuarios.stream()
                .filter(usuario -> usuario.getNome() != null && 
                        usuario.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Busca usuários por telefone
     */
    public Optional<Usuario> findByTelefone(String telefone) {
        return Usuario.usuarios.stream()
                .filter(usuario -> usuario.getTelefone() != null && usuario.getTelefone().equals(telefone))
                .findFirst();
    }
    
    /**
     * Busca usuários por faixa de horas disponíveis
     */
    public List<Usuario> findByHorasGreaterThanEqual(String horasMinimas) {
        return Usuario.usuarios.stream()
                .filter(usuario -> {
                    if (usuario.getHoras() == null) return false;
                    try {
                        float userHours = Float.parseFloat(usuario.getHoras());
                        float minHours = Float.parseFloat(horasMinimas);
                        return userHours >= minHours;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Salva um usuário (adiciona à lista)
     */
    public Usuario save(Usuario usuario) {
        // Se é um novo usuário (sem ID ou ID novo)
        if (usuario.getId() == null || usuario.getId().isEmpty()) {
            usuario.setId(String.valueOf(Usuario.proximoId++));
            Usuario.usuarios.add(usuario);
        } else {
            // Atualiza usuário existente
            Optional<Usuario> existingUsuario = findById(usuario.getId());
            if (existingUsuario.isPresent()) {
                int index = Usuario.usuarios.indexOf(existingUsuario.get());
                Usuario.usuarios.set(index, usuario);
            } else {
                // Se não encontrou, adiciona como novo
                Usuario.usuarios.add(usuario);
            }
        }
        return usuario;
    }
    
    /**
     * Deleta usuário por ID
     */
    public boolean deleteById(String id) {
        return Usuario.usuarios.removeIf(usuario -> usuario.getId().equals(id));
    }
    
    /**
     * Verifica se existe usuário com o ID
     */
    public boolean existsById(String id) {
        return Usuario.usuarios.stream()
                .anyMatch(usuario -> usuario.getId().equals(id));
    }
    
    /**
     * Verifica se existe usuário com o email
     */
    public boolean existsByEmail(String email) {
        return Usuario.usuarios.stream()
                .anyMatch(usuario -> usuario.getEmail() != null && usuario.getEmail().equals(email));
    }
    
    /**
     * Verifica se existe usuário com o telefone
     */
    public boolean existsByTelefone(String telefone) {
        return Usuario.usuarios.stream()
                .anyMatch(usuario -> usuario.getTelefone() != null && usuario.getTelefone().equals(telefone));
    }
    
    /**
     * Conta total de usuários
     */
    public long count() {
        return Usuario.usuarios.size();
    }
    
    /**
     * Deleta todos os usuários (usado para testes)
     */
    public void deleteAll() {
        Usuario.usuarios.clear();
        Usuario.proximoId = 1; // Reset do contador
    }
}