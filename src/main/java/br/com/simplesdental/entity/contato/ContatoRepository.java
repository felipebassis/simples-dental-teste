package br.com.simplesdental.entity.contato;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContatoRepository extends JpaRepository<Contato, UUID>, ContatoCustomRepository {
}
