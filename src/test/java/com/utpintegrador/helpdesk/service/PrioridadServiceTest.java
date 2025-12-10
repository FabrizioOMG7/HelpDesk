package com.utpintegrador.helpdesk. service;

import com.utpintegrador.helpdesk.model.Prioridad;
import com.utpintegrador.helpdesk.repository.PrioridadRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit. jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito. Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter. api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org. mockito. Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrioridadServiceTest {

    @Mock
    private PrioridadRepository prioridadRepository;

    @InjectMocks
    private PrioridadService prioridadService;

   
    // PRI_01: Listar prioridades
    
    @Test
    @DisplayName("PRI_01: Listar prioridades retorna lista")
    void obtenerPrioridades_RetornaLista() {
        // Arrange
        Prioridad prio1 = new Prioridad();
        prio1.setNombrePrioridad("Alta");
        Prioridad prio2 = new Prioridad();
        prio2.setNombrePrioridad("Media");
        Prioridad prio3 = new Prioridad();
        prio3.setNombrePrioridad("Baja");
        List<Prioridad> listaEsperada = Arrays.asList(prio1, prio2, prio3);

        when(prioridadRepository.findAll()).thenReturn(listaEsperada);

        // Act
        List<Prioridad> resultado = prioridadService.obtenerPrioridades();

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado. size());
        assertEquals(listaEsperada, resultado);
        verify(prioridadRepository, times(1)).findAll();
    }

    
    // PRI_02: Obtener prioridad existente
    
    @Test
    @DisplayName("PRI_02: Obtener prioridad por id existente retorna Optional")
    void obtenerPrioridadPorId_IdExistente_RetornaOptional() {
        // Arrange
        Integer prioridadId = 1;
        Prioridad prioridadEsperada = new Prioridad();
        prioridadEsperada. setCodigoPrioridad(prioridadId);
        prioridadEsperada.setNombrePrioridad("Alta");

        when(prioridadRepository.findById(prioridadId)).thenReturn(Optional. of(prioridadEsperada));

        // Act
        Optional<Prioridad> resultado = prioridadService. obtenerPrioridadPorId(prioridadId);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(prioridadEsperada, resultado.get());
        assertEquals("Alta", resultado.get().getNombrePrioridad());
        verify(prioridadRepository, times(1)).findById(prioridadId);
    }

   
    // PRI_03: Guardar prioridad
   
    @Test
    @DisplayName("PRI_03: Guardar prioridad nueva asigna estado true y guarda")
    void guardarPrioridad_PrioridadNueva_GuardaConEstadoTrue() {
        // Arrange
        Prioridad prioridadNueva = new Prioridad();
        prioridadNueva.setNombrePrioridad("Urgente");
        // codigoPrioridad es null (nueva prioridad)

        when(prioridadRepository.save(any(Prioridad.class))).thenAnswer(invocation -> {
            Prioridad p = invocation.getArgument(0);
            p.setCodigoPrioridad(1);
            return p;
        });

        // Act
        Prioridad resultado = prioridadService.guardarPrioridad(prioridadNueva);

        // Assert
        assertNotNull(resultado);
        assertEquals("Urgente", resultado. getNombrePrioridad());
        assertTrue(resultado.isEstado());
        verify(prioridadRepository, times(1)).save(any(Prioridad.class));
    }

   
    // PRI_04: Eliminar prioridad (desactivar)
    
    @Test
    @DisplayName("PRI_04: Eliminar prioridad cambia estado a false")
    void eliminarPrioridad_PrioridadExistente_DesactivaPrioridad() {
        // Arrange
        Integer prioridadId = 1;
        Prioridad prioridadExistente = new Prioridad();
        prioridadExistente.setCodigoPrioridad(prioridadId);
        prioridadExistente.setNombrePrioridad("Alta");
        prioridadExistente.setEstado(true);

        when(prioridadRepository.findById(prioridadId)).thenReturn(Optional. of(prioridadExistente));
        when(prioridadRepository.save(any(Prioridad.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Prioridad resultado = prioridadService.eliminarPrioridad(prioridadId);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEstado());
        verify(prioridadRepository, times(1)).findById(prioridadId);
        verify(prioridadRepository, times(1)).save(any(Prioridad.class));
    }
}