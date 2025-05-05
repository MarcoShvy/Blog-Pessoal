package com.aceleramaker.blog.dto;

import com.aceleramaker.blog.model.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {


    private String nome;
    private String usuario;
    private String foto;
    private String senha;
    private TipoUsuario tipoUsuario;


    public UserDTO(long l, String nome, String usuario, String foto, String senha, TipoUsuario tipoUsuario) {
    }
}
