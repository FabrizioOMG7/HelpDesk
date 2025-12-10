package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.Ticket;
import com.utpintegrador.helpdesk.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    long count();

    long countByEstado(boolean estado);

    long countByUsuario(Usuario usuario);

    long countByUsuarioAndEstado(Usuario usuario, boolean estado);

    @Query("SELECT t FROM Ticket t WHERE " +
            "(:titulo IS NULL OR t.titulo LIKE %:titulo%) AND " +
            "(:catId IS NULL OR t.subCategoria.categoria.codigoCategoria = :catId) AND " +
            "(:prioId IS NULL OR t.prioridad.codigoPrioridad = :prioId)")
    List<Ticket> filtrarTickets(
            @Param("titulo") String titulo,
            @Param("catId") Integer catId,
            @Param("prioId") Integer prioId
    );

}