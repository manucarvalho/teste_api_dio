package com.dio.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dio.entity.Cerveja;

public interface CervejaRepository extends JpaRepository<Cerveja, Long> {

	Optional<Cerveja> findByNome(String nome);
}
