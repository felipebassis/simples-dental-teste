package br.com.simplesdental.service;

import br.com.simplesdental.dto.profissional.AlterProfissionalDto;
import br.com.simplesdental.dto.profissional.NewProfissionalDto;
import br.com.simplesdental.entity.profissional.Cargo;
import br.com.simplesdental.entity.profissional.Profissional;
import br.com.simplesdental.entity.profissional.ProfissionalRepository;
import br.com.simplesdental.exception.ProfissionalNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProfissionalServiceTest {

    private final ProfissionalRepository profissionalRepository = mock(ProfissionalRepository.class);
    private final ProfissionalService profissionalService = new ProfissionalService(profissionalRepository);

    @Test
    @SuppressWarnings("unchecked")
    void shouldFindAllByFilterAndAllFields() {
        when(profissionalRepository.findAllByFields(anyString(), any())).thenReturn(List.of());

        var fields = List.of("nome", "cargo", "dataDeNascimento");
        assertDoesNotThrow(() -> profissionalService.findAllByFilterAndFields("filter", fields));

        ArgumentCaptor<List<String>> fieldsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        var filterArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(profissionalRepository, times(1)).findAllByFields(
                filterArgumentCaptor.capture(),
                fieldsArgumentCaptor.capture()
        );

        assertEquals(fields, fieldsArgumentCaptor.getValue());
        assertEquals("filter", filterArgumentCaptor.getValue());
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldFindAllByFilterAndSomeFields() {
        when(profissionalRepository.findAllByFields(anyString(), any())).thenReturn(List.of());

        var fields = List.of("nome", "dataDeNascimento");
        assertDoesNotThrow(() -> profissionalService.findAllByFilterAndFields("anotherFilter", fields));

        ArgumentCaptor<List<String>> fieldsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        var filterArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(profissionalRepository, times(1)).findAllByFields(
                filterArgumentCaptor.capture(),
                fieldsArgumentCaptor.capture()
        );

        assertEquals(fields, fieldsArgumentCaptor.getValue());
        assertEquals("anotherFilter", filterArgumentCaptor.getValue());
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldFindAllByFilterAndAllFieldsWhenListOfFieldsIsEmpty() {
        when(profissionalRepository.findAllByFields(anyString(), any())).thenReturn(List.of());

        assertDoesNotThrow(() -> profissionalService.findAllByFilterAndFields("yetAnotherFilter", Collections.emptyList()));

        ArgumentCaptor<List<String>> fieldsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        var filterArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(profissionalRepository, times(1)).findAllByFields(
                filterArgumentCaptor.capture(),
                fieldsArgumentCaptor.capture()
        );

        var capturedFields = fieldsArgumentCaptor.getValue();

        assertEquals(3, capturedFields.size());
        assertTrue(List.of("nome", "cargo", "dataDeNascimento").containsAll(capturedFields));
        assertEquals("yetAnotherFilter", filterArgumentCaptor.getValue());
    }

    @Test
    void shouldThrowExceptionWhenListOfFieldsDoesNotContainValidField() {
        var fields = List.of("nome", "notValidField");
        var exceptionThrown = assertThrows(
                IllegalArgumentException.class,
                () -> profissionalService.findAllByFilterAndFields("yetAnotherFilter", fields)
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
        var profissional = mock(Profissional.class);
        when(profissional.getId()).thenReturn(id);
        when(profissional.getNome()).thenReturn("Nome");
        when(profissional.getCargo()).thenReturn(Cargo.DESENVOLVEDOR);
        when(profissional.getDataDeNascimento()).thenReturn(LocalDate.of(1996, Month.MARCH, 28));
        when(profissional.getCreatedDate()).thenReturn(now);
        when(profissionalRepository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.of(profissional));

        var professionalReturned = assertDoesNotThrow(() -> profissionalService.findById(id));

        assertEquals(id, professionalReturned.id());
        assertEquals("Nome", professionalReturned.nome());
        assertEquals(Cargo.DESENVOLVEDOR, professionalReturned.cargo());
        assertEquals(LocalDate.of(1996, Month.MARCH, 28), professionalReturned.dataDeNascimento());
        assertEquals(now, professionalReturned.createdAt());
    }

    @Test
    void shouldThrowExceptionWhenFindByIdDoesNotFindResult() {
        var id = UUID.randomUUID();
        when(profissionalRepository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.empty());

        var exceptionThrown = assertThrows(ProfissionalNotFoundException.class, () -> profissionalService.findById(id));

        assertEquals("Profissional de ID " + id + " não encontrado.", exceptionThrown.getMessage());
    }

    @Test
    void shouldCreateNewProfissional() {
        var professional = mock(Profissional.class);
        when(profissionalRepository.save(any())).thenReturn(professional);
        var newProfissional = new NewProfissionalDto("Nome", Cargo.DESENVOLVEDOR, LocalDate.of(1996, Month.MARCH, 18));

        assertDoesNotThrow(() -> profissionalService.save(newProfissional));

        var professionalCapture = ArgumentCaptor.forClass(Profissional.class);

        verify(professional, times(1)).getId();
        verify(profissionalRepository, times(1)).save(professionalCapture.capture());

        var capturedProfissional = professionalCapture.getValue();

        assertEquals("Nome", capturedProfissional.getNome());
        assertEquals(Cargo.DESENVOLVEDOR, capturedProfissional.getCargo());
        assertEquals(LocalDate.of(1996, Month.MARCH, 18), newProfissional.dataDeNascimento());
    }

    @Test
    void shouldUpdateProfissional() {
        var id = UUID.randomUUID();
        var profissional = mock(Profissional.class);
        var profissionalAlterado = new AlterProfissionalDto("Novo nome", Cargo.DESIGNER);

        when(profissionalRepository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.of(profissional));

        assertDoesNotThrow(() -> profissionalService.update(id, profissionalAlterado));

        verify(profissional, times(1)).setNome("Novo nome");
        verify(profissional, times(1)).setCargo(Cargo.DESIGNER);
        verify(profissionalRepository, times(1)).save(profissional);
    }

    @Test
    void shouldThrowExceptionWhenTryToUpdateAndProfissionalNotFound() {
        var id = UUID.randomUUID();
        when(profissionalRepository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.empty());

        var exceptionThrown = assertThrows(
                ProfissionalNotFoundException.class,
                () -> profissionalService.update(id, new AlterProfissionalDto("name", Cargo.DESIGNER))
        );

        assertEquals("Profissional de ID " + id + " não encontrado.", exceptionThrown.getMessage());
    }

    @Test
    void shouldDeleteProfissional() {
        var id = UUID.randomUUID();
        var profissional = mock(Profissional.class);

        when(profissionalRepository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.of(profissional));

        assertDoesNotThrow(() -> profissionalService.delete(id));

        verify(profissional, times(1)).setExcluido(true);
        verify(profissionalRepository, times(1)).save(profissional);
    }

    @Test
    void shouldThrowExceptionWhenTryToDeleteAndProfissionalNotFound() {
        var id = UUID.randomUUID();
        when(profissionalRepository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.empty());

        var exceptionThrown = assertThrows(ProfissionalNotFoundException.class, () -> profissionalService.delete(id));

        assertEquals("Profissional de ID " + id + " não encontrado.", exceptionThrown.getMessage());
    }
}