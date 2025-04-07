package com.aceleramaker.blog.exception;

import com.aceleramaker.blog.exception.postagem.PostagemNaoEncontradaException;
import com.aceleramaker.blog.exception.tema.TemaNaoEncontradoException;
import com.aceleramaker.blog.exception.user.AutenticacaoException;
import com.aceleramaker.blog.exception.user.UsuarioJaCadastradoException;
import com.aceleramaker.blog.exception.user.UsuarioNaoEncontradoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleEntityNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("Entidade não encontrada");
        ResponseEntity<StandardError> response = handler.handleEntityNotFound(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Recurso não encontrado", Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void testHandleValidationErrors() {
        FieldError fieldError = new FieldError("obj", "campo", "mensagem de erro");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidationErrors(ex);
        assertEquals(400, response.getStatusCode().value());
        assertTrue(Objects.requireNonNull(response.getBody()).containsKey("campo"));
    }

    @Test
    void testHandleConstraintViolation() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);

        when(path.toString()).thenReturn("campoInvalido");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("mensagem de erro");

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);

        ConstraintViolationException ex = new ConstraintViolationException(violations);
        ResponseEntity<Map<String, String>> response = handler.handleConstraintViolation(ex);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(Objects.requireNonNull(response.getBody()).containsKey("campoInvalido"));
        assertEquals("mensagem de erro", response.getBody().get("campoInvalido"));
    }

    @Test
    void testHandleGenericException() {
        Exception ex = new Exception("Erro genérico");
        ResponseEntity<StandardError> response = handler.handleGenericException(ex);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Erro interno", Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void testHandleUsuarioJaExiste() {
        UsuarioJaCadastradoException ex = new UsuarioJaCadastradoException("Usuário já existe");
        ResponseEntity<StandardError> response = handler.handleUsuarioJaExiste(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Cadastro inválido", Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void testHandleUsuarioNaoEncontrado() {
        UsuarioNaoEncontradoException ex = new UsuarioNaoEncontradoException("Não encontrado");
        ResponseEntity<StandardError> response = handler.handleNaoEncontrado(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Usuario não encontrado", Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void testHandleAutenticacao() {
        AutenticacaoException ex = new AutenticacaoException("Falha");
        ResponseEntity<StandardError> response = handler.handleAutenticacao(ex);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("Falha de autenticação", Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void testHandleTemaNaoEncontrado() {
        TemaNaoEncontradoException ex = new TemaNaoEncontradoException("Tema não existe");
        ResponseEntity<StandardError> response = handler.handleTemaNaoEncontrado(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Tema não encontrado", Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void testHandlePostagemNaoEncontrada() {
        PostagemNaoEncontradaException ex = new PostagemNaoEncontradaException("Postagem não encontrada");
        ResponseEntity<String> response = handler.handlePostagemNotFound(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Postagem não encontrada", response.getBody());
    }

    @Test
    void testHandleSecurityException() {
        SecurityException ex = new SecurityException("Acesso negado");
        ResponseEntity<String> response = handler.handleSecurityException(ex);

        assertEquals(403, response.getStatusCode().value());
        assertEquals("Acesso negado", response.getBody());
    }
}
