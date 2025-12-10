package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Area;
import com.utpintegrador.helpdesk.model. Rol;
import com.utpintegrador.helpdesk. model.Usuario;
import com. utpintegrador.helpdesk.repository.AreaRepository;
import com.utpintegrador. helpdesk.repository.RolRepository;
import com.utpintegrador.helpdesk.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api. Test;
import org.junit. jupiter.api.DisplayName;
import org.junit. jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java. util.Arrays;
import java. util.List;
import java. util.Optional;

import static org.junit.jupiter.api. Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private AreaRepository areaRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioRequest;
    private Rol rol;
    private Area area;

    @BeforeEach
    void setUp() {
        // Configurar Rol
        rol = new Rol();
        rol.setCodigoRol(1);
        rol.setNombreRol("Administrador");

        // Configurar Area
        area = new Area();
        area.setCodigoArea(1);
        area.setNombreArea("TI");

        // Configurar Usuario base para las pruebas
        usuarioRequest = new Usuario();
        usuarioRequest.setNombres("Juan");
        usuarioRequest.setApellidoPaterno("Pérez");
        usuarioRequest. setApellidoMaterno("García");
        usuarioRequest.setCorreo("juan.perez@email.com");
        usuarioRequest.setPasswoord("password123");
        usuarioRequest. setRol(rol);
        usuarioRequest.setArea(area);
    }

    
    // USER_01: Obtener usuarios
   
    @Test
    @DisplayName("USER_01: Obtener usuarios retorna lista")
    void obtenerUsuarios_RetornaLista() {
        // Arrange
        List<Usuario> listaEsperada = Arrays.asList(new Usuario(), new Usuario());
        when(usuarioRepository.findAll()).thenReturn(listaEsperada);

        // Act
        List<Usuario> resultado = usuarioService.obtenerUsuarios();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(listaEsperada, resultado);
        verify(usuarioRepository, times(1)).findAll();
    }

   
    // USER_02: Obtener usuario por id existente
    
    @Test
    @DisplayName("USER_02: Obtener usuario por id existente retorna Optional con usuario")
    void obtenerUsuarioPorId_IdExistente_RetornaOptionalConUsuario() {
        // Arrange
        Integer usuarioId = 1;
        Usuario usuarioEsperado = new Usuario();
        usuarioEsperado.setNombres("Test");
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioEsperado));

        // Act
        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorId(usuarioId);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(usuarioEsperado, resultado.get());
        verify(usuarioRepository, times(1)).findById(usuarioId);
    }

    
    // USER_03: Crear usuario con datos válidos
   
    @Test
    @DisplayName("USER_03: Crear usuario con datos válidos guarda exitosamente")
    void crearUsuario_DatosValidos_GuardaExitosamente() {
        // Arrange
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(areaRepository.findById(1)).thenReturn(Optional.of(area));
        when(passwordEncoder.encode(anyString())).thenReturn("passwordEncriptado");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setCodigoUsuario(1);
            return u;
        });

        // Act
        Usuario resultado = usuarioService.crearUsuario(usuarioRequest);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombres());
        assertEquals("passwordEncriptado", resultado.getPasswoord());
        assertEquals(rol, resultado.getRol());
        assertEquals(area, resultado.getArea());
        assertTrue(resultado.isEstado());
        assertNotNull(resultado.getFechaCreacion());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    
    // USER_04: Crear usuario sin rol
   
    @Test
    @DisplayName("USER_04: Crear usuario sin rol lanza IllegalArgumentException")
    void crearUsuario_SinRol_LanzaExcepcion() {
        // Arrange
        usuarioRequest.setRol(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearUsuario(usuarioRequest);
        });

        assertEquals("El Rol es obligatorio", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

  
    // USER_05: Crear usuario sin área
 
    @Test
    @DisplayName("USER_05: Crear usuario sin área lanza IllegalArgumentException")
    void crearUsuario_SinArea_LanzaExcepcion() {
        // Arrange
        usuarioRequest.setArea(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearUsuario(usuarioRequest);
        });

        assertEquals("El Área es obligatoria", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

  
    // USER_06: Crear usuario sin contraseña
    
    @Test
    @DisplayName("USER_06: Crear usuario sin contraseña lanza IllegalArgumentException")
    void crearUsuario_SinPassword_LanzaExcepcion() {
        // Arrange
        usuarioRequest.setPasswoord(null);
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(areaRepository.findById(1)).thenReturn(Optional.of(area));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService. crearUsuario(usuarioRequest);
        });

        assertEquals("La contraseña es obligatoria al crear usuario", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

   
    // USER_07: Eliminar usuario (desactivar)
    
    @Test
    @DisplayName("USER_07: Eliminar usuario cambia estado a false")
    void eliminarUsuario_UsuarioExistente_DesactivaUsuario() {
        // Arrange
        Integer usuarioId = 1;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setCodigoUsuario(usuarioId);
        usuarioExistente.setEstado(true);
        usuarioExistente.setFechaEliminacion(null);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = usuarioService.eliminarUsuario(usuarioId);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEstado());
        assertNotNull(resultado.getFechaEliminacion());
        verify(usuarioRepository, times(1)).findById(usuarioId);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

   
    // USER_08: Activar usuario desactivado
    
    @Test
    @DisplayName("USER_08: Activar usuario desactivado cambia estado a true")
    void activarUsuario_UsuarioDesactivado_ActivaUsuario() {
        // Arrange
        Integer usuarioId = 1;
        Usuario usuarioDesactivado = new Usuario();
        usuarioDesactivado.setCodigoUsuario(usuarioId);
        usuarioDesactivado.setEstado(false);
        usuarioDesactivado.setFechaEliminacion(LocalDateTime.now());

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioDesactivado));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = usuarioService.activarUsuario(usuarioId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEstado());
        assertNull(resultado.getFechaEliminacion());
        assertNotNull(resultado.getFechaModificacion());
        verify(usuarioRepository, times(1)).findById(usuarioId);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}