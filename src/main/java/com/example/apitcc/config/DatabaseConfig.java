package com.example.apitcc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.stereotype.Component;

import com.example.apitcc.model.entity.Empresa;
import com.example.apitcc.model.entity.Usuario;
import com.example.apitcc.model.entity.Trabalho;
import com.example.apitcc.model.entity.Match;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * ConfiguraÃ§Ã£o completa de banco de dados para o projeto MatchVoluntario
 * 
 * Para desenvolvimento: utiliza armazenamento em memÃ³ria (listas estÃ¡ticas)
 * Para produÃ§Ã£o: PostgreSQL no Render com operaÃ§Ãµes CRUD completas
 */
@Configuration
public class DatabaseConfig {

    // =================== CONFIGURAÃ‡Ã•ES DE AMBIENTE ===================

    /**
     * ConfiguraÃ§Ã£o para ambiente de desenvolvimento
     * Utiliza armazenamento em memÃ³ria atravÃ©s de listas estÃ¡ticas
     */
    @Configuration
    @Profile({ "dev", "default" })
    public static class DevDatabaseConfig {

        public DevDatabaseConfig() {
            System.out.println("=== AMBIENTE DE DESENVOLVIMENTO ===");
            System.out.println("Usando armazenamento em memÃ³ria");
            System.out.println("Dados serÃ£o perdidos ao reiniciar a aplicaÃ§Ã£o");
        }
    }

    /**
     * ConfiguraÃ§Ã£o para ambiente de produÃ§Ã£o com PostgreSQL
     */
    @Configuration
    @Profile("prod")
    public static class ProdDatabaseConfig {

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

        @Bean
        public DatabaseOperations databaseOperations(JdbcTemplate jdbcTemplate) {
            return new DatabaseOperations(jdbcTemplate);
        }

        public ProdDatabaseConfig() {
            System.out.println("=== AMBIENTE DE PRODUÃ‡ÃƒO ===");
            System.out.println("Conectando ao PostgreSQL no Render");
        }
    }

    // =================== OPERAÃ‡Ã•ES DE BANCO DE DADOS ===================

    /**
     * Classe que contÃ©m todas as operaÃ§Ãµes CRUD para PostgreSQL
     */
    @Component
    @Profile("prod")
    public static class DatabaseOperations {

        private final JdbcTemplate jdbcTemplate;

        public DatabaseOperations(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        // =================== EMPRESA OPERATIONS ===================

        public List<Empresa> findAllEmpresas() {

            // ===== TESTE DE DIAGNÓSTICO =====
            System.out.println("----------------------------------------------------------");
            System.out.println("[DIAGNÓSTICO] MÉTODO findAllEmpresas FOI CHAMADO!");
            System.out.println("----------------------------------------------------------");
            // ===================================

            String sql = """
                    SELECT "IdEmpresa", "nome_Empresa", "email_Empresa", "cnpj",
                           "telefone_Empresa", "endereco_Empresa", "area_atuacao_empresa",
                           "publicoAlvo_Empresa", "senha_Empresa",  "foto_Empresa", "descricao_Empresa"
                    FROM public."Empresa"
                    """;

            // ===== TESTE DE DIAGNÓSTICO =====
            System.out.println("[DIAGNÓSTICO] SQL que será executado: " + sql);
            // ===================================
            return jdbcTemplate.query(sql, new EmpresaRowMapper());
        }

        public Optional<Empresa> findEmpresaById(String id) {
            String sql = """
                    SELECT "IdEmpresa", "nome_Empresa", "email_Empresa", "cnpj",
                           "telefone_Empresa", "endereco_Empresa", "area_atuacao_empresa",
                           "publicoAlvo_Empresa", "senha_Empresa", "descricao_Empresa"
                    FROM public."Empresa"
                    WHERE "IdEmpresa" = ?
                    """;
            List<Empresa> empresas = jdbcTemplate.query(sql, new EmpresaRowMapper(), Integer.parseInt(id));
            return empresas.isEmpty() ? Optional.empty() : Optional.of(empresas.get(0));
        }

        public Optional<Empresa> findEmpresaByEmail(String email) {
            String sql = """
                    SELECT "IdEmpresa", "nome_Empresa", "email_Empresa", "cnpj",
                           "telefone_Empresa", "endereco_Empresa", "area_atuacao_empresa",
                           "publicoAlvo_Empresa", "senha_Empresa", "descricao_Empresa"
                    FROM public."Empresa"
                    WHERE "email_Empresa" = ?
                    """;
            List<Empresa> empresas = jdbcTemplate.query(sql, new EmpresaRowMapper(), email);
            return empresas.isEmpty() ? Optional.empty() : Optional.of(empresas.get(0));
        }

        public Empresa saveEmpresa(Empresa empresa) {
            if (empresa.getId() == null || empresa.getId().isEmpty()) {
                // INSERT - 10 colunas, 10 valores
                String sql = """
                        INSERT INTO public."Empresa"
                        ("nome_Empresa", "email_Empresa", "cnpj", "telefone_Empresa",
                         "endereco_Empresa", "area_atuacao_empresa", "publicoAlvo_Empresa",
                         "senha_Empresa", "foto_Empresa", "descricao_Empresa")
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        RETURNING "IdEmpresa"
                        """;

                Integer id = jdbcTemplate.queryForObject(sql, Integer.class,
                        empresa.getNome(), // 1
                        empresa.getEmail(), // 2
                        empresa.getCnpj(), // 3
                        empresa.getTelefone(), // 4
                        empresa.getEndereco(), // 5
                        empresa.getAreaAtuacao(), // 6 - missao_Empresa
                        empresa.getPublicoAlvo(), // 7
                        empresa.getSenha(), // 8
                        empresa.getFoto() != null ? empresa.getFoto() : "", // 9
                        empresa.getDescricao() // 10 - descricao_Empresa
                );
                empresa.setId(String.valueOf(id));
            } else {
                // UPDATE
                String sql = """
                        UPDATE public."Empresa"
                        SET "nome_Empresa" = ?, "email_Empresa" = ?, "cnpj" = ?,
                            "telefone_Empresa" = ?, "endereco_Empresa" = ?, "area_atuacao_empresa" = ?,
                            "publicoAlvo_Empresa" = ?, "senha_Empresa" = ?,
                            "foto_Empresa" = ?, "descricao_Empresa" = ?
                        WHERE "IdEmpresa" = ?
                        """;
                jdbcTemplate.update(sql,
                        empresa.getNome(),
                        empresa.getEmail(),
                        empresa.getCnpj(),
                        empresa.getTelefone(),
                        empresa.getEndereco(),
                        empresa.getAreaAtuacao(),
                        empresa.getPublicoAlvo(),
                        empresa.getSenha(),
                        empresa.getFoto() != null ? empresa.getFoto() : "",
                        empresa.getDescricao(),
                        Integer.parseInt(empresa.getId()));
            }
            return empresa;
        }

        public boolean deleteEmpresaById(String id) {
            String sql = "DELETE FROM public.\"Empresa\" WHERE \"IdEmpresa\" = ?";
            return jdbcTemplate.update(sql, Integer.parseInt(id)) > 0;
        }

        // =================== USUARIO OPERATIONS ===================

        public List<Usuario> findAllUsuarios() {
            String sql = """
                    SELECT "IdUsuario", "email_Usuario", "telefone_Usuario", "dataDeNascimento_Usuario",
                           "senha_Usuario", "habilidades_Usuario", "horas_Usuario", "nome_Usuario"
                    FROM public."Usuario"
                    """;
            return jdbcTemplate.query(sql, new UsuarioRowMapper());
        }

        public Optional<Usuario> findUsuarioById(String id) {
            String sql = """
                    SELECT "IdUsuario", "email_Usuario", "telefone_Usuario", "dataDeNascimento_Usuario",
                           "senha_Usuario", "habilidades_Usuario", "horas_Usuario", "nome_Usuario"
                    FROM public."Usuario"
                    WHERE "IdUsuario" = ?
                    """;
            List<Usuario> usuarios = jdbcTemplate.query(sql, new UsuarioRowMapper(), Integer.parseInt(id));
            return usuarios.isEmpty() ? Optional.empty() : Optional.of(usuarios.get(0));
        }

        public Optional<Usuario> findUsuarioByEmail(String email) {
            String sql = """
                    SELECT "IdUsuario", "email_Usuario", "telefone_Usuario", "dataDeNascimento_Usuario",
                           "senha_Usuario", "habilidades_Usuario", "horas_Usuario", "nome_Usuario"
                    FROM public."Usuario"
                    WHERE "email_Usuario" = ?
                    """;
            List<Usuario> usuarios = jdbcTemplate.query(sql, new UsuarioRowMapper(), email);
            return usuarios.isEmpty() ? Optional.empty() : Optional.of(usuarios.get(0));
        }

        public Usuario saveUsuario(Usuario usuario) {
            if (usuario.getId() == null || usuario.getId().isEmpty()) {
                String sql = """
                        INSERT INTO public."Usuario" ("email_Usuario", "telefone_Usuario", "dataDeNascimento_Usuario",
                                                    "senha_Usuario", "habilidades_Usuario", "horas_Usuario", "nome_Usuario")
                        VALUES (?, ?, ?::date, ?, ?, ?, ?)
                        RETURNING "IdUsuario"
                        """;
                Double horas = null;
                try {
                    if (usuario.getHoras() != null && !usuario.getHoras().isEmpty()) {
                        horas = Double.parseDouble(usuario.getHoras());
                    }
                } catch (NumberFormatException e) {
                    horas = 0.0;
                }

                Integer id = jdbcTemplate.queryForObject(sql, Integer.class,
                        usuario.getEmail(), usuario.getTelefone(), usuario.getDataDeNascimento(),
                        usuario.getSenha(), usuario.getHabilidades(), horas, usuario.getNome());
                usuario.setId(String.valueOf(id));
            } else {
                String sql = """
                        UPDATE public."Usuario"
                        SET "email_Usuario" = ?, "telefone_Usuario" = ?, "dataDeNascimento_Usuario" = ?::date,
                            "senha_Usuario" = ?, "habilidades_Usuario" = ?, "horas_Usuario" = ?, "nome_Usuario" = ?
                        WHERE "IdUsuario" = ?
                        """;
                Double horas = null;
                try {
                    if (usuario.getHoras() != null && !usuario.getHoras().isEmpty()) {
                        horas = Double.parseDouble(usuario.getHoras());
                    }
                } catch (NumberFormatException e) {
                    horas = 0.0;
                }

                jdbcTemplate.update(sql,
                        usuario.getEmail(), usuario.getTelefone(), usuario.getDataDeNascimento(),
                        usuario.getSenha(), usuario.getHabilidades(), horas, usuario.getNome(),
                        Integer.parseInt(usuario.getId()));
            }
            return usuario;
        }

        public boolean deleteUsuarioById(String id) {
            String sql = "DELETE FROM public.\"Usuario\" WHERE \"IdUsuario\" = ?";
            return jdbcTemplate.update(sql, Integer.parseInt(id)) > 0;
        }

        // =================== TRABALHO OPERATIONS ===================

        public List<Trabalho> findAllTrabalhos() {
            String sql = """
                    SELECT "IdTrabalho", "IdEmpresa", "nomeEmpresa_Trabalho", "descricao_Trabalho",
                           "quantidadeDeVagas_Trabalho", "tipoTrabalho_Trabalho", "habilidadesNecessarias_Trabalho"
                    FROM public.trabalho
                    """;
            return jdbcTemplate.query(sql, new TrabalhoRowMapper());
        }

        public Optional<Trabalho> findTrabalhoById(String id) {
            String sql = """
                    SELECT "IdTrabalho", "IdEmpresa", "nomeEmpresa_Trabalho", "descricao_Trabalho",
                           "quantidadeDeVagas_Trabalho", "tipoTrabalho_Trabalho", "habilidadesNecessarias_Trabalho"
                    FROM public.trabalho
                    WHERE "IdTrabalho" = ?
                    """;
            List<Trabalho> trabalhos = jdbcTemplate.query(sql, new TrabalhoRowMapper(), Integer.parseInt(id));
            return trabalhos.isEmpty() ? Optional.empty() : Optional.of(trabalhos.get(0));
        }

        public List<Trabalho> findTrabalhosByEmpresa(String idEmpresa) {
            String sql = """
                    SELECT "IdTrabalho", "IdEmpresa", "nomeEmpresa_Trabalho", "descricao_Trabalho",
                           "quantidadeDeVagas_Trabalho", "tipoTrabalho_Trabalho", "habilidadesNecessarias_Trabalho"
                    FROM public.trabalho
                    WHERE "IdEmpresa" = ?
                    """;
            return jdbcTemplate.query(sql, new TrabalhoRowMapper(), Integer.parseInt(idEmpresa));
        }

        public Trabalho saveTrabalho(Trabalho trabalho) {
            if (trabalho.getId() == null || trabalho.getId().isEmpty()) {
                String sql = """
                        INSERT INTO public.trabalho ("IdEmpresa", "nomeEmpresa_Trabalho", "descricao_Trabalho",
                                                   "quantidadeDeVagas_Trabalho", "tipoTrabalho_Trabalho", "habilidadesNecessarias_Trabalho")
                        VALUES (?, ?, ?, ?, ?, ?)
                        RETURNING "IdTrabalho"
                        """;
                Integer vagas = null;
                try {
                    if (trabalho.getQuantidadeDeVagas() != null && !trabalho.getQuantidadeDeVagas().isEmpty()) {
                        vagas = Integer.parseInt(trabalho.getQuantidadeDeVagas());
                    }
                } catch (NumberFormatException e) {
                    vagas = 1;
                }

                Integer id = jdbcTemplate.queryForObject(sql, Integer.class,
                        Integer.parseInt(trabalho.getIdEmpresa()), trabalho.getNomeEmpresa(),
                        trabalho.getDescricaoTrabalho(), vagas, trabalho.getTipoTrabalho(),
                        trabalho.getHabilidadesNecessarias());
                trabalho.setId(String.valueOf(id));
            } else {
                String sql = """
                        UPDATE public.trabalho
                        SET "IdEmpresa" = ?, "nomeEmpresa_Trabalho" = ?, "descricao_Trabalho" = ?,
                            "quantidadeDeVagas_Trabalho" = ?, "tipoTrabalho_Trabalho" = ?, "habilidadesNecessarias_Trabalho" = ?
                        WHERE "IdTrabalho" = ?
                        """;
                Integer vagas = null;
                try {
                    if (trabalho.getQuantidadeDeVagas() != null && !trabalho.getQuantidadeDeVagas().isEmpty()) {
                        vagas = Integer.parseInt(trabalho.getQuantidadeDeVagas());
                    }
                } catch (NumberFormatException e) {
                    vagas = 1;
                }

                jdbcTemplate.update(sql,
                        Integer.parseInt(trabalho.getIdEmpresa()), trabalho.getNomeEmpresa(),
                        trabalho.getDescricaoTrabalho(), vagas, trabalho.getTipoTrabalho(),
                        trabalho.getHabilidadesNecessarias(), Integer.parseInt(trabalho.getId()));
            }
            return trabalho;
        }

        public boolean deleteTrabalhoById(String id) {
            String sql = "DELETE FROM public.trabalho WHERE \"IdTrabalho\" = ?";
            return jdbcTemplate.update(sql, Integer.parseInt(id)) > 0;
        }

        // =================== MATCH OPERATIONS ===================

        public List<Match> findAllMatches() {
            String sql = """
                    SELECT "IdMatch", "IdUsuario", "IdTrabalho", "status_Match", "data_Match",
                           "dataInicio_Match", "dataTermino_Match", "horasTrabalhadas_Match", "avaliacao_Match"
                    FROM public."Match"
                    """;
            return jdbcTemplate.query(sql, new MatchRowMapper());
        }

        public Optional<Match> findMatchById(String id) {
            String sql = """
                    SELECT "IdMatch", "IdUsuario", "IdTrabalho", "status_Match", "data_Match",
                           "dataInicio_Match", "dataTermino_Match", "horasTrabalhadas_Match", "avaliacao_Match"
                    FROM public."Match"
                    WHERE "IdMatch" = ?
                    """;
            List<Match> matches = jdbcTemplate.query(sql, new MatchRowMapper(), Integer.parseInt(id));
            return matches.isEmpty() ? Optional.empty() : Optional.of(matches.get(0));
        }

        public List<Match> findMatchesByUsuario(String idUsuario) {
            String sql = """
                    SELECT "IdMatch", "IdUsuario", "IdTrabalho", "status_Match", "data_Match",
                           "dataInicio_Match", "dataTermino_Match", "horasTrabalhadas_Match", "avaliacao_Match"
                    FROM public."Match"
                    WHERE "IdUsuario" = ?
                    """;
            return jdbcTemplate.query(sql, new MatchRowMapper(), Integer.parseInt(idUsuario));
        }

        public List<Match> findMatchesByTrabalho(String idTrabalho) {
            String sql = """
                    SELECT "IdMatch", "IdUsuario", "IdTrabalho", "status_Match", "data_Match",
                           "dataInicio_Match", "dataTermino_Match", "horasTrabalhadas_Match", "avaliacao_Match"
                    FROM public."Match"
                    WHERE "IdTrabalho" = ?
                    """;
            return jdbcTemplate.query(sql, new MatchRowMapper(), Integer.parseInt(idTrabalho));
        }

        public Match saveMatch(Match match) {
            if (match.getId() == null || match.getId().isEmpty()) {
                String sql = """
                        INSERT INTO public."Match" ("IdUsuario", "IdTrabalho", "status_Match", "data_Match",
                                                  "dataInicio_Match", "dataTermino_Match", "horasTrabalhadas_Match", "avaliacao_Match")
                        VALUES (?, ?, ?, ?::date, ?::date, ?::date, ?, ?)
                        RETURNING "IdMatch"
                        """;
                Double horas = null;
                Integer avaliacao = null;

                try {
                    if (match.getHorasTrabalhas() != null && !match.getHorasTrabalhas().isEmpty()) {
                        horas = Double.parseDouble(match.getHorasTrabalhas());
                    }
                } catch (NumberFormatException e) {
                    horas = 0.0;
                }

                try {
                    if (match.getAvaliacao() != null && !match.getAvaliacao().isEmpty()) {
                        avaliacao = Integer.parseInt(match.getAvaliacao());
                    }
                } catch (NumberFormatException e) {
                    avaliacao = null;
                }

                Integer id = jdbcTemplate.queryForObject(sql, Integer.class,
                        Integer.parseInt(match.getIdUsuario()), Integer.parseInt(match.getIdTrabalho()),
                        match.getStatus(), match.getDataMatch(), match.getDataInicio(),
                        match.getDataTermino(), horas, avaliacao);
                match.setId(String.valueOf(id));
            } else {
                String sql = """
                        UPDATE public."Match"
                        SET "IdUsuario" = ?, "IdTrabalho" = ?, "status_Match" = ?, "data_Match" = ?::date,
                            "dataInicio_Match" = ?::date, "dataTermino_Match" = ?::date, "horasTrabalhadas_Match" = ?, "avaliacao_Match" = ?
                        WHERE "IdMatch" = ?
                        """;
                Double horas = null;
                Integer avaliacao = null;

                try {
                    if (match.getHorasTrabalhas() != null && !match.getHorasTrabalhas().isEmpty()) {
                        horas = Double.parseDouble(match.getHorasTrabalhas());
                    }
                } catch (NumberFormatException e) {
                    horas = 0.0;
                }

                try {
                    if (match.getAvaliacao() != null && !match.getAvaliacao().isEmpty()) {
                        avaliacao = Integer.parseInt(match.getAvaliacao());
                    }
                } catch (NumberFormatException e) {
                    avaliacao = null;
                }

                jdbcTemplate.update(sql,
                        Integer.parseInt(match.getIdUsuario()), Integer.parseInt(match.getIdTrabalho()),
                        match.getStatus(), match.getDataMatch(), match.getDataInicio(),
                        match.getDataTermino(), horas, avaliacao, Integer.parseInt(match.getId()));
            }
            return match;
        }

        public boolean deleteMatchById(String id) {
            String sql = "DELETE FROM public.\"Match\" WHERE \"IdMatch\" = ?";
            return jdbcTemplate.update(sql, Integer.parseInt(id)) > 0;
        }
    }

    // =================== ROW MAPPERS ===================

    private static class EmpresaRowMapper implements RowMapper<Empresa> {
        @Override
        public Empresa mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Empresa(
                    String.valueOf(rs.getInt("IdEmpresa")),
                    rs.getString("descricao_Empresa"),
                    rs.getString("nome_Empresa"),
                    rs.getString("email_Empresa"),
                    rs.getString("cnpj"),
                    rs.getString("telefone_Empresa"),
                    rs.getString("endereco_Empresa"),
                    rs.getString("area_atuacao_empresa"),
                    rs.getString("publicoAlvo_Empresa"),
                    rs.getString("senha_Empresa"),
                    "", // foto - BYTEA nÃ£o tratado nesta versÃ£o
                    java.time.LocalDateTime.now().toString() // dataCadastro
            );
        }
    }

    private static class UsuarioRowMapper implements RowMapper<Usuario> {
        @Override
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Usuario(
                    String.valueOf(rs.getInt("IdUsuario")),
                    "", // descriÃ§Ã£o - nÃ£o existe no banco atual
                    rs.getString("email_Usuario"),
                    rs.getString("telefone_Usuario"),
                    rs.getDate("dataDeNascimento_Usuario") != null ? rs.getDate("dataDeNascimento_Usuario").toString()
                            : null,
                    rs.getString("senha_Usuario"),
                    rs.getString("habilidades_Usuario"),
                    String.valueOf(rs.getDouble("horas_Usuario")),
                    rs.getString("nome_Usuario"),
                    "", // foto - BYTEA nÃ£o tratado nesta versÃ£o
                    java.time.LocalDateTime.now().toString() // dataCadastro
            );
        }
    }

    private static class TrabalhoRowMapper implements RowMapper<Trabalho> {
        @Override
        public Trabalho mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Trabalho(
                    String.valueOf(rs.getInt("IdTrabalho")),
                    rs.getString("descricao_Trabalho"),
                    String.valueOf(rs.getInt("IdEmpresa")),
                    rs.getString("nomeEmpresa_Trabalho"),
                    rs.getString("descricao_Trabalho"),
                    String.valueOf(rs.getInt("quantidadeDeVagas_Trabalho")),
                    rs.getString("tipoTrabalho_Trabalho"),
                    rs.getString("habilidadesNecessarias_Trabalho"),
                    java.time.LocalDateTime.now().toString() // dataCadastro
            );
        }
    }

    private static class MatchRowMapper implements RowMapper<Match> {
        @Override
        public Match mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Match(
                    String.valueOf(rs.getInt("IdMatch")),
                    "", // descriÃ§Ã£o - nÃ£o existe no banco atual
                    String.valueOf(rs.getInt("IdUsuario")),
                    String.valueOf(rs.getInt("IdTrabalho")),
                    rs.getString("status_Match"),
                    rs.getDate("data_Match") != null ? rs.getDate("data_Match").toString() : null,
                    rs.getDate("dataInicio_Match") != null ? rs.getDate("dataInicio_Match").toString() : null,
                    rs.getDate("dataTermino_Match") != null ? rs.getDate("dataTermino_Match").toString() : null,
                    String.valueOf(rs.getDouble("horasTrabalhadas_Match")),
                    String.valueOf(rs.getInt("avaliacao_Match")),
                    java.time.LocalDateTime.now().toString() // dataCadastro
            );
        }
    }
}