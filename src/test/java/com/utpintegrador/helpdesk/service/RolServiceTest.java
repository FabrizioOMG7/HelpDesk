package com.utpintegrador.helpdesk.service;

import com.utpintegrador. helpdesk.model.Rol;
import com.utpintegrador.helpdesk.repository. RolRepository;

import org. junit.jupiter.api.Test;
import org.junit.jupiter. api.DisplayName;
import org.junit. jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit. jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

   
    // ROL_01: Listar roles

    @Test
    @DisplayName("ROL_01: Listar roles retorna lista")
    void obtenerRoles_RetornaLista() {
        // Arrange
        Rol rol1 = new Rol();
        rol1.setNombreRol("Administrador");
        Rol rol2 = new Rol();
        rol2.setNombreRol("Técnico");
        Rol rol3 = new Rol();
        rol3.setNombreRol("Usuario");
        List<Rol> listaEsperada = Arrays.asList(rol1, rol2, rol3);

        when(rolRepository.findAll()).thenReturn(listaEsperada);

        // Act
        List<Rol> resultado = rolService.obtenerRoles();

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals(listaEsperada, resultado);
        verify(rolRepository, times(1)).findAll();
    }

  
    // ROL_02: Obtener rol existente
   
    @Test
    @DisplayName("ROL_02: Obtener rol por id existente retorna Optional")
    void obtenerRolPorId_IdExistente_RetornaOptional() {
        // Arrange
        Integer rolId = 1;
        Rol rolEsperado = new Rol();
        rolEsperado.setCodigoRol(rolId);
        rolEsperado.setNombreRol("Administrador");

        when(rolRepository.findById(rolId)).thenReturn(Optional. of(rolEsperado));

        // Act
        Optional<Rol> resultado = rolService. obtenerRolPorId(rolId);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(rolEsperado, resultado.get());
        assertEquals("Administrador", resultado.get().getNombreRol());
        verify(rolRepository, times(1)).findById(rolId);
    }

   
    // ROL_03: Guardar rol

    @Test
    @DisplayName("ROL_03: Guardar rol retorna rol guardado")
    void guardarRol_RolValido_GuardaExitosamente() {
        // Arrange
        Rol rolNuevo = new Rol();
        rolNuevo.setNombreRol("Supervisor");
        rolNuevo.setEstado(true);

        when(rolRepository.save(any(Rol.class))).thenAnswer(invocation -> {
            Rol r = invocation.getArgument(0);
            r.setCodigoRol(1);
            return r;
        });

        // Act
        Rol resultado = rolService.guardarRol(rolNuevo);

        // Assert
        assertNotNull(resultado);
        assertEquals("Supervisor", resultado.getNombreRol());
        assertEquals(1, resultado.getCodigoRol());
        verify(rolRepository, times(1)).save(any(Rol.class));
    }

  
    // ROL_04: Eliminar rol

    @Test
    @DisplayName("ROL_04: Eliminar rol invoca deleteById")
    void eliminarRol_RolExistente_InvocaDeleteById() {
        // Arrange
        Integer rolId = 1;
        doNothing().when(rolRepository).deleteById(rolId);

        // Act
        rolService.eliminarRol(rolId);

        // Assert
        verify(rolRepository, times(1)).deleteById(rolId);
    }
}