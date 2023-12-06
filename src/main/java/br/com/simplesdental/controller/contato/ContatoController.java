package br.com.simplesdental.controller.contato;

import br.com.simplesdental.controller.ErrorResponse;
import br.com.simplesdental.dto.contato.AlterContatoDto;
import br.com.simplesdental.dto.contato.ContatoDto;
import br.com.simplesdental.dto.contato.NewContatoDto;
import br.com.simplesdental.dto.contato.NewContatoResponseDto;
import br.com.simplesdental.entity.ResultSet;
import br.com.simplesdental.service.ContatoService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "contatos")
@Tag(name = "Contatos", description = "Operações envolvendo contatos")
public class ContatoController {

    private final ContatoService contatoService;

    @Operation(description = "Busca contatos filtrando por os que são similares ao parâmetro " +
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
    public ResponseEntity<List<ResultSet>> getAllByFields(
            @Parameter(
                    description = "Texto a filtrar contato por nome e conteúdo"
            )
            @RequestParam("q") String filter,
            @Parameter(
                    description = "Lista de campos que devem ser retornados. Campos permitidos: 'nome', 'contato'"
            )
            @RequestParam("fields") List<String> fields) {

        var result = contatoService.findAllByFilterAndFields(filter, fields);
        if (result.isEmpty()) {
            return ResponseEntity.noContent()
                    .build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(
            description = "Busca contato filtrando por ID",
            responses = {
                    @ApiResponse(
                            description = "Quando contato com o ID especificado não for encontrado",
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
                                    schema = @Schema(implementation = ContatoDto.class)
                            )
                    )
            }
    )
    @GetMapping("{id}")
    public ContatoDto getById(
            @Parameter(description = "Id do contato a ser buscado")
            @PathVariable("id") UUID id
    ) {
        return contatoService.findById(id);
    }

    @Operation(description = "Cria um novo contato atribuído a um profissional existente",
            responses = {
                    @ApiResponse(
                            description = "Quando o contato foi criado com sucesso",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewContatoDto.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Quando o profissional atribuído ao contato não existe",
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewContatoResponseDto newContact(@RequestBody NewContatoDto newContato) {
        return contatoService.save(newContato);
    }

    @Operation(description = "Altera um contato existente",
            responses = {
                    @ApiResponse(
                            description = "Quando o contato foi alterado com sucesso",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Quando o contato não existe",
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
    public void alterContact(
            @Parameter(description = "Id do contato a ser alterado")
            @PathVariable("id") UUID id,
            @RequestBody AlterContatoDto alterContatoDto) {
        contatoService.update(id, alterContatoDto);
    }


    @Operation(description = "Remove um contato existente",
            responses = {
                    @ApiResponse(
                            description = "Quando o contato foi removido com sucesso",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Quando o contato não existe",
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
    public void removeContact(
            @Parameter(description = "Id do contato a ser removido")
            @PathVariable("id") UUID id) {
        contatoService.delete(id);
    }
}
