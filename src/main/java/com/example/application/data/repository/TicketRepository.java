package com.example.application.data.repository;

import com.example.application.data.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query(value = "select t from ticket t where lower(t.description) like lower(concat('%', :searchTerm, '%')) and t.customer_id=:id", nativeQuery = true)
    List<Ticket> search(String searchTerm, Integer id);

    @Query(value = "SELECT count(*) FROM ticket where customer_id=:id and description=:description", nativeQuery = true)
    Integer existsByDescription(Integer id, String description);
    @Query(value = "SELECT * FROM ticket where customer_id=:id", nativeQuery = true)
    List<Ticket> findTicketsByUser(Integer id);
}

