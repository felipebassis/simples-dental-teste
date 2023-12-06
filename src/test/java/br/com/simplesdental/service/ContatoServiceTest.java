package br.com.simplesdental.service;

import br.com.simplesdental.dto.contato.AlterContatoDto;
import br.com.simplesdental.dto.contato.NewContatoDto;
import br.com.simplesdental.entity.contato.Contato;
import br.com.simplesdental.entity.contato.ContatoRepository;
import br.com.simplesdental.entity.profissional.Profissional;
import br.com.simplesdental.entity.profissional.ProfissionalRepository;
import br.com.simplesdental.exception.ContatoNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ContatoServiceTest {

    private final ProfissionalRepository profissionalRepository = mock(ProfissionalRepository.class);
    private final ContatoRepository contatoRepository = mock(ContatoRepository.class);
    private final ContatoService contatoService = new ContatoService(contatoRepository, profissionalRepository);

    @Test
    @SuppressWarnings("unchecked")
    void shouldFindAllByFilterAndAllFields() {
        when(contatoRepository.findAllByFields(anyString(), any())).thenReturn(List.of());

        var fields = List.of("nome", "contato");
        assertDoesNotThrow(() -> contatoService.findAllByFilterAndFields("filter", fields));

        ArgumentCaptor<List<String>> fieldsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        var filterArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(contatoRepository, times(1)).findAllByFields(
                filterArgumentCaptor.capture(),
                fieldsArgumentCaptor.capture()
        );

        assertEquals(fields, fieldsArgumentCaptor.getValue());
        assertEquals("filter", filterArgumentCaptor.getValue());
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldFindAllByFilterAndSomeFields() {
        when(contatoRepository.findAllByFields(anyString(), any())).thenReturn(List.of());

        var fields = List.of("nome");
        assertDoesNotThrow(() -> contatoService.findAllByFilterAndFields("anotherFilter", fields));

        ArgumentCaptor<List<String>> fieldsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        var filterArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(contatoRepository, times(1)).findAllByFields(
                filterArgumentCaptor.capture(),
                fieldsArgumentCaptor.capture()
        );

        assertEquals(fields, fieldsArgumentCaptor.getValue());
        assertEquals("anotherFilter", filterArgumentCaptor.getValue());
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldFindAllByFilterAndAllFieldsWhenListOfFieldsIsEmpty() {
        when(contatoRepository.findAllByFields(anyString(), any())).thenReturn(List.of());

        var fields = List.of("nome", "contato");
        assertDoesNotThrow(() -> contatoService.findAllByFilterAndFields("yetAnotherFilter", Collections.emptyList()));

        ArgumentCaptor<List<String>> fieldsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        var filterArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(contatoRepository, times(1)).findAllByFields(
                filterArgumentCaptor.capture(),
                fieldsArgumentCaptor.capture()
        );

        var capturedFields = fieldsArgumentCaptor.getValue();
        assertEquals(fields.size(), capturedFields.size());
        assertTrue(fields.containsAll(capturedFields));
        assertEquals("yetAnotherFilter", filterArgumentCaptor.getValue());
    }

    @Test
    void shouldThrowExceptionWhenListOfFieldsDoesNotContainValidField() {
        var fields = List.of("nome", "notValidField");
        var exceptionThrown = assertThrows(
                IllegalArgumentException.class,
                () -> contatoService.findAllByFilterAndFields("yetAnotherFilter", fields)
        );

        verify(profissionalRepository, never()).findAllByFields(
                anyString(),
                any()
        );

        assertEquals("Parâmetro \"fields\" contem valores invalidos.", exceptionThrown.getMessage());
    }

    @Test
    void shouldFindById() {
        var id = UUID.randomUUID();
        var now = LocalDateTime.now();
        var contato = mock(Contato.class);
        when(contato.getId()).thenReturn(id);
        when(contato.getNome()).thenReturn("Nome");
        when(contato.getContato()).thenReturn("Contato");
        when(contato.getCreatedDate()).thenReturn(now);
        when(contatoRepository.findById(id)).thenReturn(Optional.of(contato));

        var contatoReturned = assertDoesNotThrow(() -> contatoService.findById(id));

        assertEquals(id, contatoReturned.id());
        assertEquals("Nome", contatoReturned.nome());
        assertEquals("Contato", contatoReturned.contato());
        assertEquals(now, contatoReturned.createdAt());
    }

    @Test
    void shouldThrowExceptionWhenFindByIdDoesNotFindResult() {
        var id = UUID.randomUUID();
        when(profissionalRepository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.empty());

        var exceptionThrown = assertThrows(ContatoNotFoundException.class, () -> contatoService.findById(id));

        assertEquals("Contato de ID " + id + " não encontrado.", exceptionThrown.getMessage());
    }

    @Test
    void shouldCreateNewContato() {
        var contato = mock(Contato.class);
        var profissionalId = UUID.randomUUID();
        var professional = mock(Profissional.class);
        when(profissionalRepository.findByIdAndExcluidoFalse(profissionalId)).thenReturn(Optional.ofNullable(professional));
        when(contatoRepository.save(any())).thenReturn(contato);

        var newContato = new NewContatoDto("Nome", "Contato", profissionalId);
        assertDoesNotThrow(() -> contatoService.save(newContato));

        var contatoCaptor = ArgumentCaptor.forClass(Contato.class);

        verify(contato, times(1)).getId();
        verify(contatoRepository, times(1)).save(contatoCaptor.capture());

        var capturedContato = contatoCaptor.getValue();

        assertEquals("Nome", capturedContato.getNome());
        assertEquals("Contato", capturedContato.getContato());
        assertEquals(professional, capturedContato.getProfissional());
    }

    @Test
    void shouldUpdateContato() {
        var id = UUID.randomUUID();
        var contato = mock(Contato.class);
        var contatoAlterado = new AlterContatoDto("Novo nome", "Novo contato");

        when(contatoRepository.findById(id)).thenReturn(Optional.of(contato));

        assertDoesNotThrow(() -> contatoService.update(id, contatoAlterado));

        verify(contato, times(1)).setNome("Novo nome");
        verify(contato, times(1)).setContato("Novo contato");
        verify(contatoRepository, times(1)).save(contato);
    }

    @Test
    void shouldThrowExceptionWhenTryToUpdateAndContatoNotFound() {
        var id = UUID.randomUUID();
        when(contatoRepository.findById(id)).thenReturn(Optional.empty());

        var exceptionThrown = assertThrows(
                ContatoNotFoundException.class,
                () -> contatoService.update(id, new AlterContatoDto("name", "contact"))
        );

        assertEquals("Contato de ID " + id + " não encontrado.", exceptionThrown.getMessage());
    }

    @Test
    void shouldDeleteContato() {
        var id = UUID.randomUUID();

        assertDoesNotThrow(() -> contatoService.delete(id));

        verify(contatoRepository, times(1)).deleteById(id);
    }
}