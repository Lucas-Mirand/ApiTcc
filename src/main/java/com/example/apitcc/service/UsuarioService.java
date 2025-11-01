package com.example.apitcc.service;

import com.example.apitcc.config.DatabaseConfig;
import com.example.apitcc.model.dto.UsuarioDTO;
import com.example.apitcc.model.entity.Usuario;
import com.example.apitcc.model.entity.ItemBase;
import com.example.apitcc.exception.ResourceNotFoundException;
import com.example.apitcc.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

@Service
public class UsuarioService {
    
    @Autowired(required = false)
    private DatabaseConfig.DatabaseOperations databaseOperations;
    
    // Regex para validação de email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    public List<UsuarioDTO> listarTodosUsuarios() {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            return databaseOperations.findAllUsuarios().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            // Desenvolvimento: Memória
            return Usuario.usuarios.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }
    
    public UsuarioDTO buscarUsuarioPorId(String id) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            Usuario usuario = databaseOperations.findUsuarioById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
            return convertToDTO(usuario);
        } else {
            // Desenvolvimento: Memória
            Usuario usuario = Usuario.usuarios.stream()
                    .filter(u -> u.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
            return convertToDTO(usuario);
        }
    }
    
    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        // Validações
        validarDadosUsuario(usuarioDTO);
        
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            // Verificar se email já existe
            Optional<Usuario> usuarioExistente = databaseOperations.findUsuarioByEmail(usuarioDTO.getEmail());
            if (usuarioExistente.isPresent()) {
                throw new BadRequestException("Email já cadastrado no sistema");
            }
            
            Usuario usuario = new Usuario(
                    null, // ID será gerado pelo banco
                    usuarioDTO.getDescricao() != null ? usuarioDTO.getDescricao() : "",
                    usuarioDTO.getEmail(),
                    usuarioDTO.getTelefone() != null ? usuarioDTO.getTelefone() : "",
                    usuarioDTO.getDataDeNascimento() != null ? usuarioDTO.getDataDeNascimento() : "",
                    usuarioDTO.getSenha(),
                    usuarioDTO.getHabilidades() != null ? usuarioDTO.getHabilidades() : "",
                    usuarioDTO.getHoras() != null ? usuarioDTO.getHoras() : "0",
                    usuarioDTO.getNome(),
                    usuarioDTO.getFoto() != null ? usuarioDTO.getFoto() : "",
                    ItemBase.getCurrentDateTime()
            );
            
            Usuario usuarioSalvo = databaseOperations.saveUsuario(usuario);
            return convertToDTO(usuarioSalvo);
            
        } else {
            // Desenvolvimento: Memória
            // Verificar se email já existe
            boolean emailExiste = Usuario.usuarios.stream()
                    .anyMatch(u -> u.getEmail() != null && u.getEmail().equals(usuarioDTO.getEmail()));
            
            if (emailExiste) {
                throw new BadRequestException("Email já cadastrado no sistema");
            }
            
            // Verificar se telefone já existe (se fornecido)
            if (usuarioDTO.getTelefone() != null && !usuarioDTO.getTelefone().trim().isEmpty()) {
                boolean telefoneExiste = Usuario.usuarios.stream()
                        .anyMatch(u -> u.getTelefone() != null && u.getTelefone().equals(usuarioDTO.getTelefone()));
                
                if (telefoneExiste) {
                    throw new BadRequestException("Telefone já cadastrado no sistema");
                }
            }
            
            String id = String.valueOf(Usuario.proximoId++);
            String dataCadastro = ItemBase.getCurrentDateTime();
            
            Usuario usuario = new Usuario(
                    id,
                    usuarioDTO.getDescricao() != null ? usuarioDTO.getDescricao() : "",
                    usuarioDTO.getEmail(),
                    usuarioDTO.getTelefone() != null ? usuarioDTO.getTelefone() : "",
                    usuarioDTO.getDataDeNascimento() != null ? usuarioDTO.getDataDeNascimento() : "",
                    usuarioDTO.getSenha(),
                    usuarioDTO.getHabilidades() != null ? usuarioDTO.getHabilidades() : "",
                    usuarioDTO.getHoras() != null ? usuarioDTO.getHoras() : "0",
                    usuarioDTO.getNome(),
                    usuarioDTO.getFoto() != null ? usuarioDTO.getFoto() : "",
                    dataCadastro
            );
            
            Usuario.usuarios.add(usuario);
            return convertToDTO(usuario);
        }
    }
    
    public UsuarioDTO atualizarUsuario(String id, UsuarioDTO usuarioDTO) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            Usuario usuarioExistente = databaseOperations.findUsuarioById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
            
            // Validar email se fornecido
            if (usuarioDTO.getEmail() != null && !usuarioDTO.getEmail().isEmpty()) {
                if (!EMAIL_PATTERN.matcher(usuarioDTO.getEmail()).matches()) {
                    throw new BadRequestException("Email inválido");
                }
                
                Optional<Usuario> usuarioComEmail = databaseOperations.findUsuarioByEmail(usuarioDTO.getEmail());
                if (usuarioComEmail.isPresent() && !usuarioComEmail.get().getId().equals(id)) {
                    throw new BadRequestException("Email já cadastrado para outro usuário");
                }
            }
            
            // Validar formato de horas se fornecido
            if (usuarioDTO.getHoras() != null && !usuarioDTO.getHoras().isEmpty()) {
                try {
                    float horas = Float.parseFloat(usuarioDTO.getHoras());
                    if (horas < 0) {
                        throw new BadRequestException("Horas disponíveis não pode ser negativo");
                    }
                } catch (NumberFormatException e) {
                    throw new BadRequestException("Formato de horas inválido");
                }
            }
            
            // Atualizar campos
            atualizarCamposUsuario(usuarioExistente, usuarioDTO);
            
            Usuario usuarioAtualizado = databaseOperations.saveUsuario(usuarioExistente);
            return convertToDTO(usuarioAtualizado);
            
        } else {
            // Desenvolvimento: Memória
            Usuario usuarioExistente = Usuario.usuarios.stream()
                    .filter(u -> u.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
            
            // Validar email se fornecido
            if (usuarioDTO.getEmail() != null && !usuarioDTO.getEmail().isEmpty()) {
                if (!EMAIL_PATTERN.matcher(usuarioDTO.getEmail()).matches()) {
                    throw new BadRequestException("Email inválido");
                }
                
                boolean emailExiste = Usuario.usuarios.stream()
                        .anyMatch(u -> !u.getId().equals(id) && 
                                 u.getEmail() != null && 
                                 u.getEmail().equals(usuarioDTO.getEmail()));
                
                if (emailExiste) {
                    throw new BadRequestException("Email já cadastrado para outro usuário");
                }
            }
            
            if (usuarioDTO.getTelefone() != null && !usuarioDTO.getTelefone().isEmpty()) {
                boolean telefoneExiste = Usuario.usuarios.stream()
                        .anyMatch(u -> !u.getId().equals(id) && 
                                 u.getTelefone() != null && 
                                 u.getTelefone().equals(usuarioDTO.getTelefone()));
                
                if (telefoneExiste) {
                    throw new BadRequestException("Telefone já cadastrado para outro usuário");
                }
            }
            
            // Validar formato de horas se fornecido
            if (usuarioDTO.getHoras() != null && !usuarioDTO.getHoras().isEmpty()) {
                try {
                    float horas = Float.parseFloat(usuarioDTO.getHoras());
                    if (horas < 0) {
                        throw new BadRequestException("Horas disponíveis não pode ser negativo");
                    }
                } catch (NumberFormatException e) {
                    throw new BadRequestException("Formato de horas inválido");
                }
            }
            
            // Atualizar campos
            atualizarCamposUsuario(usuarioExistente, usuarioDTO);
            
            return convertToDTO(usuarioExistente);
        }
    }
    
    public void excluirUsuario(String id) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            boolean removido = databaseOperations.deleteUsuarioById(id);
            if (!removido) {
                throw new ResourceNotFoundException("Usuário não encontrado com ID: " + id);
            }
        } else {
            // Desenvolvimento: Memória
            boolean removido = Usuario.usuarios.removeIf(u -> u.getId().equals(id));
            if (!removido) {
                throw new ResourceNotFoundException("Usuário não encontrado com ID: " + id);
            }
        }
    }
    
    public List<UsuarioDTO> buscarPorHabilidades(String habilidade) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL - implementar query específica se necessário
            return databaseOperations.findAllUsuarios().stream()
                    .filter(u -> u.getHabilidades() != null && 
                                u.getHabilidades().toLowerCase().contains(habilidade.toLowerCase()))
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            // Desenvolvimento: Memória
            return Usuario.usuarios.stream()
                    .filter(u -> u.getHabilidades() != null && 
                                u.getHabilidades().toLowerCase().contains(habilidade.toLowerCase()))
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }
    
    public UsuarioDTO buscarPorEmail(String email) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            Usuario usuario = databaseOperations.findUsuarioByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com email: " + email));
            return convertToDTO(usuario);
        } else {
            // Desenvolvimento: Memória
            Usuario usuario = Usuario.usuarios.stream()
                    .filter(u -> u.getEmail() != null && u.getEmail().equals(email))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com email: " + email));
            return convertToDTO(usuario);
        }
    }
    
    public List<UsuarioDTO> buscarPorHorasDisponiveis(String horasMinimas) {
        try {
            float minHours = Float.parseFloat(horasMinimas);
            
            if (databaseOperations != null) {
                // Produção: PostgreSQL
                return databaseOperations.findAllUsuarios().stream()
                        .filter(u -> {
                            if (u.getHoras() == null) return false;
                            try {
                                return Float.parseFloat(u.getHoras()) >= minHours;
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        })
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            } else {
                // Desenvolvimento: Memória
                return Usuario.usuarios.stream()
                        .filter(u -> {
                            if (u.getHoras() == null) return false;
                            try {
                                return Float.parseFloat(u.getHoras()) >= minHours;
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        })
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("Formato de horas inválido");
        }
    }
    
    private void atualizarCamposUsuario(Usuario usuario, UsuarioDTO dto) {
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            usuario.setEmail(dto.getEmail());
        }
        if (dto.getTelefone() != null) {
            usuario.setTelefone(dto.getTelefone());
        }
        if (dto.getDataDeNascimento() != null) {
            usuario.setDataDeNascimento(dto.getDataDeNascimento());
        }
        if (dto.getSenha() != null && !dto.getSenha().trim().isEmpty()) {
            usuario.setSenha(dto.getSenha());
        }
        if (dto.getHabilidades() != null) {
            usuario.setHabilidades(dto.getHabilidades());
        }
        if (dto.getHoras() != null) {
            usuario.setHoras(dto.getHoras());
        }
        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getFoto() != null) {
            usuario.setFoto(dto.getFoto());
        }
        if (dto.getDescricao() != null) {
            usuario.setDescricao(dto.getDescricao());
        }
    }
    
    private void validarDadosUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getNome() == null || usuarioDTO.getNome().trim().isEmpty()) {
            throw new BadRequestException("Nome do usuário é obrigatório");
        }
        
        if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Email é obrigatório");
        }
        
        if (!EMAIL_PATTERN.matcher(usuarioDTO.getEmail()).matches()) {
            throw new BadRequestException("Email inválido");
        }
        
        if (usuarioDTO.getSenha() == null || usuarioDTO.getSenha().trim().isEmpty()) {
            throw new BadRequestException("Senha é obrigatória");
        }
        
        if (usuarioDTO.getSenha().length() < 6) {
            throw new BadRequestException("Senha deve ter pelo menos 6 caracteres");
        }
        
        // Validar formato de horas se fornecido
        if (usuarioDTO.getHoras() != null && !usuarioDTO.getHoras().isEmpty()) {
            try {
                float horas = Float.parseFloat(usuarioDTO.getHoras());
                if (horas < 0) {
                    throw new BadRequestException("Horas disponíveis não pode ser negativo");
                }
            } catch (NumberFormatException e) {
                throw new BadRequestException("Formato de horas inválido");
            }
        }
    }
    
    private UsuarioDTO convertToDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getDataDeNascimento(),
                usuario.getSenha(),
                usuario.getHabilidades(),
                usuario.getHoras(),
                usuario.getNome(),
                usuario.getFoto(),
                usuario.getDescricao(),
                usuario.getDataCadastro()
        );
    }
}