package br.com.api.services

import br.com.api.controller.BookController
import br.com.api.data.vo.v1.BookVO
import br.com.api.exceptions.RequiredObjectIsNullException
import br.com.api.exceptions.ResourceNotFoundException
import br.com.api.mapper.DozerMapper
import br.com.api.model.Book
import br.com.api.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger

@Service
class BookService {

    @Autowired
    private lateinit var repository: BookRepository

    private val logger = Logger.getLogger(BookService::class.java.name)

    fun findAll(): List<BookVO> {
        logger.info("Finding all books!")
        val books =  repository.findAll()
        val vos = DozerMapper.parseListObjects(books, BookVO::class.java)
        for (book in vos) {
            val withSelfRel = linkTo(BookController::class.java).slash(book.id).withSelfRel()
            book.add(withSelfRel)
        }
        return vos
    }

    fun findById(id: Long): BookVO {
        logger.info("Finding one BookVO with ID $id!")
        var book =  repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}
        val bookVO: BookVO = DozerMapper.parseObject(book, BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.id).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    fun create(book: BookVO?) : BookVO{
        if(book == null) throw RequiredObjectIsNullException()
        logger.info("Creating one BookVO with title: ${book.title}!")
        var entity: Book = DozerMapper.parseObject(book, Book::class.java)
        val bookVO: BookVO = DozerMapper.parseObject(repository.save(entity), BookVO::class.java) // Salva e converte para BookVO para retornar reposta
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.id).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    fun update(bookVO: BookVO?) : BookVO {
        if(bookVO == null) throw RequiredObjectIsNullException()

        logger.info("Updating one BookVO with id ${bookVO.id}!")

        var entity = repository.findById(bookVO.id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}

        entity.author = bookVO.author
        entity.title = bookVO.title
        entity.price = bookVO.price
        entity.launchDate = bookVO.launchDate
        val book: BookVO = DozerMapper.parseObject(repository.save(entity), BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.id).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    fun delete(id: Long) {
        logger.info("Deleting one BookVO with id ${id}!")

        var entity =  repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}
        repository.delete(entity)
    }

}