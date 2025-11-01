package com.example.apitcc.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Empresa extends ItemBase {
    private String nome;
    private String email;
    private String cnpj;
    private String telefone;
    private String endereco;
    private String areaAtuacao;
    private String publicoAlvo;
    private String senha;
    private String foto; // Base64 ou URL
    
    public Empresa(String id, String descricao, String nome, String email, String cnpj, 
                  String telefone, String endereco, String areaAtuacao, String publicoAlvo, 
                  String senha, String foto, String dataCadastro) {
        super(id, descricao, dataCadastro);
        this.nome = nome;
        this.email = email;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.endereco = endereco;
        this.areaAtuacao = areaAtuacao;
        this.publicoAlvo = publicoAlvo;
        this.senha = senha;
        this.foto = foto;
    }
    
    // Getters e Setters
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
    
    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = super.toMap();
        map.put("nome", this.nome);
        map.put("email", this.email);
        map.put("cnpj", this.cnpj);
        map.put("telefone", this.telefone);
        map.put("endereco", this.endereco);
        map.put("areaAtuacao", this.areaAtuacao);
        map.put("publicoAlvo", this.publicoAlvo);
        map.put("senha", this.senha);
        map.put("foto", this.foto);
        return map;
    }
    
    // Lista estÃ¡tica para armazenamento em memÃ³ria (para desenvolvimento)
    public static List<Empresa> empresas = new ArrayList<>();
    public static int proximoId = 1;
}