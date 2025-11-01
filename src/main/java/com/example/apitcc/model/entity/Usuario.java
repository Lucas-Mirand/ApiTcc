package com.example.apitcc.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Usuario extends ItemBase {
    private String email;
    private String telefone;
    private String dataDeNascimento;
    private String senha;
    private String habilidades;
    private String horas; // Float como String para manter compatibilidade
    private String nome;
    private String foto; // Base64 ou URL
    
    public Usuario(String id, String descricao, String email, String telefone, 
                  String dataDeNascimento, String senha, String habilidades, 
                  String horas, String nome, String foto, String dataCadastro) {
        super(id, descricao, dataCadastro);
        this.email = email;
        this.telefone = telefone;
        this.dataDeNascimento = dataDeNascimento;
        this.senha = senha;
        this.habilidades = habilidades;
        this.horas = horas;
        this.nome = nome;
        this.foto = foto;
    }
    
    // Getters e Setters
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
    
    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = super.toMap();
        map.put("email", this.email);
        map.put("telefone", this.telefone);
        map.put("dataDeNascimento", this.dataDeNascimento);
        map.put("senha", this.senha);
        map.put("habilidades", this.habilidades);
        map.put("horas", this.horas);
        map.put("nome", this.nome);
        map.put("foto", this.foto);
        return map;
    }
    
    // Lista estática para armazenamento em memória (para desenvolvimento)
    public static List<Usuario> usuarios = new ArrayList<>();
    public static int proximoId = 1;
}