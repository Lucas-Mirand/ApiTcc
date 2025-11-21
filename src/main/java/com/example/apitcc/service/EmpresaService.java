package com.example.apitcc.service;

import com.example.apitcc.config.DatabaseConfig;
import com.example.apitcc.model.dto.EmpresaDTO;
import com.example.apitcc.model.entity.Empresa;
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
public class EmpresaService {

    @Autowired(required = false)
    private DatabaseConfig.DatabaseOperations databaseOperations;

    // Regex para validaÃ§Ã£o de email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public List<EmpresaDTO> listarTodasEmpresas() {
        if (databaseOperations != null) {
            // ProduÃ§Ã£o: PostgreSQL
            return databaseOperations.findAllEmpresas().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            // Desenvolvimento: MemÃ³ria
            return Empresa.empresas.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }

    public EmpresaDTO buscarEmpresaPorId(String id) {
        if (databaseOperations != null) {
            // ProduÃ§Ã£o: PostgreSQL
            Empresa empresa = databaseOperations.findEmpresaById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa nÃ£o encontrada com ID: " + id));
            return convertToDTO(empresa);
        } else {
            // Desenvolvimento: MemÃ³ria
            Empresa empresa = Empresa.empresas.stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa nÃ£o encontrada com ID: " + id));
            return convertToDTO(empresa);
        }
    }

    public EmpresaDTO criarEmpresa(EmpresaDTO empresaDTO) {
        // ValidaÃ§Ãµes
        validarDadosEmpresa(empresaDTO);

        if (databaseOperations != null) {
            // ProduÃ§Ã£o: PostgreSQL
            // Verificar se email jÃ¡ existe
            Optional<Empresa> empresaExistente = databaseOperations.findEmpresaByEmail(empresaDTO.getEmail());
            if (empresaExistente.isPresent()) {
                throw new BadRequestException("Email jÃ¡ cadastrado no sistema");
            }

            Empresa empresa = new Empresa(
                    null, // ID serÃ¡ gerado pelo banco
                    empresaDTO.getDescricao() != null ? empresaDTO.getDescricao() : "",
                    empresaDTO.getNome(),
                    empresaDTO.getEmail(),
                    empresaDTO.getCnpj() != null ? empresaDTO.getCnpj() : "",
                    empresaDTO.getTelefone() != null ? empresaDTO.getTelefone() : "",
                    empresaDTO.getEndereco() != null ? empresaDTO.getEndereco() : "",
                    empresaDTO.getAreaAtuacao() != null ? empresaDTO.getAreaAtuacao() : "",
                    empresaDTO.getPublicoAlvo() != null ? empresaDTO.getPublicoAlvo() : "",
                    empresaDTO.getSenha(),
                    empresaDTO.getFoto() != null ? empresaDTO.getFoto() : "",
                    ItemBase.getCurrentDateTime());

            Empresa empresaSalva = databaseOperations.saveEmpresa(empresa);
            return convertToDTO(empresaSalva);

        } else {
            // Desenvolvimento: MemÃ³ria
            // Verificar se email jÃ¡ existe
            boolean emailExiste = Empresa.empresas.stream()
                    .anyMatch(e -> e.getEmail() != null && e.getEmail().equals(empresaDTO.getEmail()));

            if (emailExiste) {
                throw new BadRequestException("Email jÃ¡ cadastrado no sistema");
            }

            String id = String.valueOf(Empresa.proximoId++);
            String dataCadastro = ItemBase.getCurrentDateTime();

            Empresa empresa = new Empresa(
                    id,
                    empresaDTO.getDescricao() != null ? empresaDTO.getDescricao() : "",
                    empresaDTO.getNome(),
                    empresaDTO.getEmail(),
                    empresaDTO.getCnpj() != null ? empresaDTO.getCnpj() : "",
                    empresaDTO.getTelefone() != null ? empresaDTO.getTelefone() : "",
                    empresaDTO.getEndereco() != null ? empresaDTO.getEndereco() : "",
                    empresaDTO.getAreaAtuacao() != null ? empresaDTO.getAreaAtuacao() : "",
                    empresaDTO.getPublicoAlvo() != null ? empresaDTO.getPublicoAlvo() : "",
                    empresaDTO.getSenha(),
                    empresaDTO.getFoto() != null ? empresaDTO.getFoto() : "",
                    dataCadastro);

            Empresa.empresas.add(empresa);
            return convertToDTO(empresa);
        }
    }

    public EmpresaDTO atualizarEmpresa(String id, EmpresaDTO empresaDTO) {
        if (databaseOperations != null) {
            // ProduÃ§Ã£o: PostgreSQL
            Empresa empresaExistente = databaseOperations.findEmpresaById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa nÃ£o encontrada com ID: " + id));

            // Validar email se fornecido
            if (empresaDTO.getEmail() != null && !empresaDTO.getEmail().isEmpty()) {
                if (!EMAIL_PATTERN.matcher(empresaDTO.getEmail()).matches()) {
                    throw new BadRequestException("Email invÃ¡lido");
                }

                Optional<Empresa> empresaComEmail = databaseOperations.findEmpresaByEmail(empresaDTO.getEmail());
                if (empresaComEmail.isPresent() && !empresaComEmail.get().getId().equals(id)) {
                    throw new BadRequestException("Email jÃ¡ cadastrado para outra empresa");
                }
            }

            // Atualizar campos
            atualizarCamposEmpresa(empresaExistente, empresaDTO);

            Empresa empresaAtualizada = databaseOperations.saveEmpresa(empresaExistente);
            return convertToDTO(empresaAtualizada);

        } else {
            // Desenvolvimento: MemÃ³ria
            Empresa empresaExistente = Empresa.empresas.stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa nÃ£o encontrada com ID: " + id));

            // Validar email se fornecido
            if (empresaDTO.getEmail() != null && !empresaDTO.getEmail().isEmpty()) {
                if (!EMAIL_PATTERN.matcher(empresaDTO.getEmail()).matches()) {
                    throw new BadRequestException("Email invÃ¡lido");
                }

                boolean emailExiste = Empresa.empresas.stream()
                        .anyMatch(e -> !e.getId().equals(id) &&
                                e.getEmail() != null &&
                                e.getEmail().equals(empresaDTO.getEmail()));

                if (emailExiste) {
                    throw new BadRequestException("Email jÃ¡ cadastrado para outra empresa");
                }
            }

            // Atualizar campos
            atualizarCamposEmpresa(empresaExistente, empresaDTO);

            return convertToDTO(empresaExistente);
        }
    }

    public void excluirEmpresa(String id) {
        if (databaseOperations != null) {
            // ProduÃ§Ã£o: PostgreSQL
            boolean removido = databaseOperations.deleteEmpresaById(id);
            if (!removido) {
                throw new ResourceNotFoundException("Empresa nÃ£o encontrada com ID: " + id);
            }
        } else {
            // Desenvolvimento: MemÃ³ria
            boolean removido = Empresa.empresas.removeIf(e -> e.getId().equals(id));
            if (!removido) {
                throw new ResourceNotFoundException("Empresa nÃ£o encontrada com ID: " + id);
            }
        }
    }

    public List<EmpresaDTO> buscarPorAreaAtuacao(String area) {
        if (databaseOperations != null) {
            // ProduÃ§Ã£o: PostgreSQL - implementar query especÃ­fica se necessÃ¡rio
            return databaseOperations.findAllEmpresas().stream()
                    .filter(e -> e.getAreaAtuacao() != null &&
                            e.getAreaAtuacao().toLowerCase().contains(area.toLowerCase()))
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            // Desenvolvimento: MemÃ³ria
            return Empresa.empresas.stream()
                    .filter(e -> e.getAreaAtuacao() != null &&
                            e.getAreaAtuacao().toLowerCase().contains(area.toLowerCase()))
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }

    public EmpresaDTO buscarPorEmail(String email) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            Empresa empresa = databaseOperations.findEmpresaByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com email: " + email));
            return convertToDTO(empresa);
        } else {
            // Desenvolvimento: Memória
            Empresa empresa = Empresa.empresas.stream()
                    .filter(e -> e.getEmail() != null && e.getEmail().equals(email))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com email: " + email));
            return convertToDTO(empresa);
        }
    }

    private void atualizarCamposEmpresa(Empresa empresa, EmpresaDTO dto) {
        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            empresa.setNome(dto.getNome());
        }
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            empresa.setEmail(dto.getEmail());
        }
        if (dto.getCnpj() != null) {
            empresa.setCnpj(dto.getCnpj());
        }
        if (dto.getTelefone() != null) {
            empresa.setTelefone(dto.getTelefone());
        }
        if (dto.getEndereco() != null) {
            empresa.setEndereco(dto.getEndereco());
        }
        if (dto.getAreaAtuacao() != null) {
            empresa.setAreaAtuacao(dto.getAreaAtuacao());
        }
        if (dto.getPublicoAlvo() != null) {
            empresa.setPublicoAlvo(dto.getPublicoAlvo());
        }
        if (dto.getSenha() != null && !dto.getSenha().trim().isEmpty()) {
            empresa.setSenha(dto.getSenha());
        }
        if (dto.getFoto() != null) {
            empresa.setFoto(dto.getFoto());
        }
        if (dto.getDescricao() != null) {
            empresa.setDescricao(dto.getDescricao());
        }
    }

    private void validarDadosEmpresa(EmpresaDTO empresaDTO) {
        if (empresaDTO.getNome() == null || empresaDTO.getNome().trim().isEmpty()) {
            throw new BadRequestException("Nome da empresa Ã© obrigatÃ³rio");
        }

        if (empresaDTO.getEmail() == null || empresaDTO.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Email Ã© obrigatÃ³rio");
        }

        if (!EMAIL_PATTERN.matcher(empresaDTO.getEmail()).matches()) {
            throw new BadRequestException("Email invÃ¡lido");
        }

        if (empresaDTO.getSenha() == null || empresaDTO.getSenha().trim().isEmpty()) {
            throw new BadRequestException("Senha Ã© obrigatÃ³ria");
        }

        if (empresaDTO.getSenha().length() < 6) {
            throw new BadRequestException("Senha deve ter pelo menos 6 caracteres");
        }
    }

    private EmpresaDTO convertToDTO(Empresa empresa) {
        return new EmpresaDTO(
                empresa.getId(),
                empresa.getNome(),
                empresa.getEmail(),
                empresa.getCnpj(),
                empresa.getTelefone(),
                empresa.getEndereco(),
                empresa.getAreaAtuacao(),
                empresa.getPublicoAlvo(),
                empresa.getSenha(),
                empresa.getFoto(),
                empresa.getDescricao(),
                empresa.getDataCadastro());
    }
}
