package br.com.simplesdental.service;

import br.com.simplesdental.dto.contato.AlterContatoDto;
import br.com.simplesdental.dto.contato.ContatoDto;
import br.com.simplesdental.dto.contato.NewContatoDto;
import br.com.simplesdental.dto.contato.NewContatoResponseDto;
import br.com.simplesdental.entity.ResultSet;
import br.com.simplesdental.entity.contato.Contato;
import br.com.simplesdental.entity.contato.ContatoRepository;
import br.com.simplesdental.entity.profissional.ProfissionalRepository;
import br.com.simplesdental.exception.ContatoNotFoundException;
import br.com.simplesdental.exception.ProfissionalNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ContatoService {

    private static final Set<String> AVAILABLE_FIELDS = Set.of("nome", "contato");
    private final ContatoRepository contatoRepository;
    private final ProfissionalRepository profissionalRepository;

    public List<ResultSet> findAllByFilterAndFields(String filter, List<String> fields) {
        if (!AVAILABLE_FIELDS.containsAll(fields)) {
            throw new IllegalArgumentException("Parâmetro \"fields\" contem valores invalidos.");
        }
        if (fields.isEmpty()) {
            fields = AVAILABLE_FIELDS.stream()
                    .toList();
        }
        return contatoRepository.findAllByFields(filter, fields);
    }

    public ContatoDto findById(UUID id) {
        return contatoRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ContatoNotFoundException("Contato de ID " + id + " não encontrado."));
    }

    public NewContatoResponseDto save(NewContatoDto newContato) {
        var profissional = profissionalRepository.findByIdAndExcluidoFalse(newContato.profissionalId())
                .orElseThrow(() -> new ProfissionalNotFoundException(
                        "Profissional de ID " + newContato.profissionalId() + " não encontrado.")
                );
        var contato = new Contato(
                newContato.nome().trim(),
                newContato.contato().trim(),
                profissional
        );

        var contatoSaved = contatoRepository.save(contato);

        return new NewContatoResponseDto(contatoSaved.getId());
    }

    public void update(UUID id, AlterContatoDto alterContatoDto) {
        var contato = contatoRepository.findById(id)
                .orElseThrow(() -> new ContatoNotFoundException("Contato de ID " + id + " não encontrado."));
        contato.setNome(alterContatoDto.nome().trim());
        contato.setContato(alterContatoDto.contato().trim());

        contatoRepository.save(contato);
    }

    public void delete(UUID id) {
        contatoRepository.deleteById(id);
    }

    private ContatoDto toDto(Contato contato) {
        return new ContatoDto(contato.getId(), contato.getNome(), contato.getContato(), contato.getCreatedDate());
    }
}
