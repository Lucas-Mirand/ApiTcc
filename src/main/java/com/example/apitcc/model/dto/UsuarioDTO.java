package com.example.apitcc.model.dto;

public class UsuarioDTO {
    private String id;
    private String email;
    private String telefone;
    private String dataDeNascimento;
    private String senha;
    private String habilidades;
    private String horas;
    private String nome;
    private String foto;
    private String descricao;
    private String dataCadastro;
    
    // Construtor vazio
    public UsuarioDTO() {}
    
    // Construtor completo
    public UsuarioDTO(String id, String email, String telefone, String dataDeNascimento,
                     String senha, String habilidades, String horas, String nome,
                     String foto, String descricao, String dataCadastro) {
        this.id = id;
        this.email = email;
        this.telefone = telefone;
        this.dataDeNascimento = dataDeNascimento;
        this.senha = senha;
        this.habilidades = habilidades;
        this.horas = horas;
        this.nome = nome;
        this.foto = foto;
        this.descricao = descricao;
        this.dataCadastro = dataCadastro;
    }
    
    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getDataDeNascimento() { return dataDeNascimento; }
    public void setDataDeNascimento(String dataDeNascimento) { this.dataDeNascimento = dataDeNascimento; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getHabilidades() { return habilidades; }
    public void setHabilidades(String habilidades) { this.habilidades = habilidades; }
    
    public String getHoras() { return horas; }
    public void setHoras(String horas) { this.horas = horas; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }
}