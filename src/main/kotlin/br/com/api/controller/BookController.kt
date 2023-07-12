package br.com.api.controller

import br.com.api.data.vo.v1.BookVO
import br.com.api.services.BookService
import br.com.api.util.MediaType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicLong

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Books", description = "Endpoints for mapping Books")
class BookController {

    val counter: AtomicLong = AtomicLong()

    @Autowired
    private lateinit var service: BookService
    //var service: BookService = BookService() // Igual ao código de cima so que não precisa mais.

    @GetMapping(
        //method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML]
    )
    @Operation(
        summary = "Finds all Books",
        description = "Recupera todas as Livros do banco.",
        tags = ["Books"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(array = ArraySchema(schema = Schema(implementation = BookVO::class)))
                ],
            ),
            ApiResponse(
                description = "No Content",
                responseCode = "204",
                content = [
                  Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Bad Request",
                responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Unauthorized",
                responseCode = "401",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Not Found",
                responseCode = "404",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Internal Error",
                responseCode = "500",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
        ]
    )
    fun findAll(): List<BookVO> {
        return service.findAll()
    }

    @GetMapping(
        value = ["/{id}"],
        //method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML])
    @Operation(
        summary = "Finds a Book",
        description = "Recupera uma Livros do banco através do ID.",
        tags = ["Books"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(schema = Schema(implementation = BookVO::class))
                ],
            ),
            ApiResponse(
                description = "No Content",
                responseCode = "204",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Bad Request",
                responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Unauthorized",
                responseCode = "401",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Not Found",
                responseCode = "404",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Internal Error",
                responseCode = "500",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
        ]
    )
    fun findById(@PathVariable(value = "id") id: Long): BookVO {
        return service.findById(id)
    }

    @PostMapping(
        //method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML]
    )
    @Operation(
        summary = "Adds a new Book",
        description = "Cadastra uma Livros no banco através do body da requisição.",
        tags = ["Books"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(schema = Schema(implementation = BookVO::class))
                ],
            ),
            ApiResponse(
                description = "Bad Request",
                responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Unauthorized",
                responseCode = "401",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Internal Error",
                responseCode = "500",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
        ]
    )
    fun create(@RequestBody bookVO: BookVO): BookVO {
        return service.create(bookVO)
    }

    /*
    @PostMapping(
        value = ["/v2"],
        //method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML]
    )
    fun createV2(@RequestBody bookVO: BookVOV2): BookVOV2 {
        return service.createV2(bookVO)
    }
    */

    @PutMapping(
        //method = [RequestMethod.PUT],
        consumes = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML]
    )
    @Operation(
        summary = "Update a Book's information",
        description = "Atualiza os dados de uma livro no banco através do ID.",
        tags = ["Books"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(schema = Schema(implementation = BookVO::class))
                ],
            ),
            ApiResponse(
                description = "No Content",
                responseCode = "204",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Bad Request",
                responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Unauthorized",
                responseCode = "401",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Not Found",
                responseCode = "404",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Internal Error",
                responseCode = "500",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
        ]
    )
    fun update(@RequestBody bookVO: BookVO): BookVO {
        return service.update(bookVO)
    }

    @DeleteMapping(
        //method = [RequestMethod.DELETE],
        value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML]
    )
    @Operation(
        summary = "Deletes a Book",
        description = "Deleta uma Livros do banco através do ID.",
        tags = ["Books"],
        responses = [
            ApiResponse(
                description = "No Content",
                responseCode = "204",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Bad Request",
                responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Unauthorized",
                responseCode = "401",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Not Found",
                responseCode = "404",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
            ApiResponse(
                description = "Internal Error",
                responseCode = "500",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ],
            ),
        ]
    )
    fun delete(@PathVariable(value = "id") id: Long) : ResponseEntity<*>{
        service.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }

}