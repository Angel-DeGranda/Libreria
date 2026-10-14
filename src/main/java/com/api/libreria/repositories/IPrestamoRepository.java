package com.api.libreria.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.libreria.models.PrestamoModel;

@Repository
public interface IPrestamoRepository extends JpaRepository<PrestamoModel, Long> {
    ArrayList<PrestamoModel> findBySocioId(Long socioId);
    ArrayList<PrestamoModel> findByLibroIsbn(Long libroIsbn);
    boolean existsBySocioId(Long socioId);
    boolean existsByLibroIsbn(Long libroIsbn);
    boolean existsBySocioIdAndLibroIsbnAndFechaDevolucionIsNull(Long socioId, Long libroIsbn);
    boolean existsBySocioIdAndFechaDevolucionIsNull(Long socioId);
}