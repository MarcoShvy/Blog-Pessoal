package com.AceleraMaker.Blog.dto;

import com.AceleraMaker.Blog.model.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String nome;
    private String usuario;
    private String foto;
    private String senha;
    private TipoUsuario tipoUsuario;
}
