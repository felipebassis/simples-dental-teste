package br.com.simplesdental.entity.profissional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfissionalRepository extends JpaRepository<Profissional, UUID>, ProfissionalCustomRepository {
    Optional<Profissional> findByIdAndExcluidoFalse(UUID id);
}