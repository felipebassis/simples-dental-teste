package br.com.simplesdental.controller.profissional;

import br.com.simplesdental.controller.ErrorResponse;
import br.com.simplesdental.dto.profissional.AlterProfissionalDto;
import br.com.simplesdental.dto.profissional.NewProfissionalDto;
import br.com.simplesdental.dto.profissional.NewProfissionalResponseDto;
import br.com.simplesdental.dto.profissional.ProfissionalDto;
import br.com.simplesdental.entity.ResultSet;
import br.com.simplesdental.service.ProfissionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "profissionais")
@Tag(name = "Profissionais", description = "Operações envolvendo profissionais")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @Operation(description = "Busca profissionais filtrando por os que são similares ao parâmetro " +
            "retornando apenas os campos especificados.",
            responses = {
                    @ApiResponse(
                            description = "Quando valores no campo 'fields' forem inválidos",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )

                    ),
                    @ApiResponse(
                            description = "Quando não possui resultados",
                            responseCode = "204",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "[]")
                            )
                    ),
                    @ApiResponse(
                            description = "Quando possui resultados",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResultSet.class))
                            )
                    )
            }
    )
    @GetMapping
    public List<ResultSet> getAllByFields(
            @Parameter(description = "Texto a filtrar contato por nome e conteúdo")
            @RequestParam("q") String filter,
            @Parameter(
                    description = "Lista de campos que devem ser retornados. Campos permitidos: 'nome', 'cargo', 'dataDeNascimento'"

            )
            @RequestParam(value = "fields", required = false) List<String> fields) {
        if (fields == null) {
            fields = Collections.emptyList();
        }
        return profissionalService.findAllByFilterAndFields(filter, fields);
    }

    @Operation(
            description = "Busca profissional filtrando por ID",
            responses = {
                    @ApiResponse(
                            description = "Quando profissional com o ID especificado não for encontrado",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Quando possui resultado",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProfissionalDto.class)
                            )
                    )
            }
    )
    @GetMapping("{id}")
    public ProfissionalDto getById(
            @Parameter(description = "Id do profissional a ser buscado")
            @PathVariable("id") UUID id
    ) {
        return profissionalService.findById(id);
    }

    @Operation(description = "Cria um novo profissional",
            responses = {
                    @ApiResponse(
                            description = "Quando o profissional foi criado com sucesso",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewProfissionalDto.class)
                            )
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewProfissionalResponseDto newProfissional(@RequestBody NewProfissionalDto newContato) {
        return profissionalService.save(newContato);
    }

    @Operation(description = "Altera um profissional existente",
            responses = {
                    @ApiResponse(
                            description = "Quando o profissional foi alterado com sucesso",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Quando o profissional não existe",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    )
            }
    )
    @PutMapping("{id}")
    public void alterProfissional(
            @Parameter(description = "Id do profissional a ser alterado")
            @PathVariable("id") UUID id,
            @RequestBody AlterProfissionalDto alterContactDto) {
        profissionalService.update(id, alterContactDto);
    }

    @Operation(description = "Remove um profissional existente",
            responses = {
                    @ApiResponse(
                            description = "Quando o profissional foi removido com sucesso",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Quando o profissional não existe",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("{id}")
    public void removeProfissional(
            @Parameter(description = "Id do profissional a ser removido")
            @PathVariable("id") UUID id) {
        profissionalService.delete(id);
    }
}
