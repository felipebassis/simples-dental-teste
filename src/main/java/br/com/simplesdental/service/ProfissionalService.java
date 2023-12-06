package br.com.simplesdental.service;

import br.com.simplesdental.dto.profissional.AlterProfissionalDto;
import br.com.simplesdental.dto.profissional.NewProfissionalDto;
import br.com.simplesdental.dto.profissional.NewProfissionalResponseDto;
import br.com.simplesdental.dto.profissional.ProfissionalDto;
import br.com.simplesdental.entity.ResultSet;
import br.com.simplesdental.entity.profissional.Profissional;
import br.com.simplesdental.entity.profissional.ProfissionalRepository;
import br.com.simplesdental.exception.ProfissionalNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private static final Set<String> AVAILABLE_FIELDS = Set.of("nome", "cargo", "dataDeNascimento");
    private final ProfissionalRepository profissionalRepository;

    public List<ResultSet> findAllByFilterAndFields(String filter, List<String> fields) {
        if (!AVAILABLE_FIELDS.containsAll(fields)) {
            throw new IllegalArgumentException("Parâmetro \"fields\" contem valores invalidos.");
        }
        if (fields.isEmpty()) {
            fields = AVAILABLE_FIELDS.stream()
                    .toList();
        }
        return profissionalRepository.findAllByFields(filter, fields);
    }

    public ProfissionalDto findById(UUID id) {
        return profissionalRepository.findByIdAndExcluidoFalse(id)
                .map(this::toDto)
                .orElseThrow(() -> new ProfissionalNotFoundException("Profissional de ID " + id + " não encontrado."));
    }

    public NewProfissionalResponseDto save(NewProfissionalDto newProfissional) {
        var profissional = new Profissional(newProfissional.nome(), newProfissional.cargo(), newProfissional.dataDeNascimento());

        var profissionalSaved = profissionalRepository.save(profissional);

        return new NewProfissionalResponseDto(profissionalSaved.getId());
    }

    public void update(UUID id, AlterProfissionalDto alterProfissionalDto) {
        var profissional = profissionalRepository.findByIdAndExcluidoFalse(id)
                .orElseThrow(() -> new ProfissionalNotFoundException("Profissional de ID " + id + " não encontrado."));
        profissional.setNome(alterProfissionalDto.nome());
        profissional.setCargo(alterProfissionalDto.cargo());

        profissionalRepository.save(profissional);
    }

    public void delete(UUID id) {
        var profissional = profissionalRepository.findByIdAndExcluidoFalse(id)
                .orElseThrow(() -> new ProfissionalNotFoundException("Profissional de ID " + id + " não encontrado."));
        profissional.setExcluido(true);
        profissionalRepository.save(profissional);
    }

    private ProfissionalDto toDto(Profissional profissional) {
        return new ProfissionalDto(
                profissional.getId(),
                profissional.getNome(),
                profissional.getCargo(),
                profissional.getDataDeNascimento(),
                profissional.getCreatedDate()
        );
    }
}
