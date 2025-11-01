package com.example.apitcc.model.dto;

public class EmpresaDTO {
    private String id;
    private String nome;
    private String email;
    private String cnpj;
    private String telefone;
    private String endereco;
    private String areaAtuacao;
    private String publicoAlvo;
    private String senha;
    private String foto;
    private String descricao;
    private String dataCadastro;
    
    // Construtor vazio
    public EmpresaDTO() {}
    
    // Construtor completo
    public EmpresaDTO(String id, String nome, String email, String cnpj, String telefone,
                     String endereco, String areaAtuacao, String publicoAlvo, String senha,
                     String foto, String descricao, String dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.endereco = endereco;
        this.areaAtuacao = areaAtuacao;
        this.publicoAlvo = publicoAlvo;
        this.senha = senha;
        this.foto = foto;
        this.descricao = descricao;
        this.dataCadastro = dataCadastro;
    }
    
    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public String getAreaAtuacao() { return areaAtuacao; }
    public void setAreaAtuacao(String areaAtuacao) { this.areaAtuacao = areaAtuacao; }
    
    public String getPublicoAlvo() { return publicoAlvo; }
    public void setPublicoAlvo(String publicoAlvo) { this.publicoAlvo = publicoAlvo; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }
}