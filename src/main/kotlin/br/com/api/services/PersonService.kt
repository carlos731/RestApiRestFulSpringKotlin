package br.com.api.services

import br.com.api.controller.PersonController
import br.com.api.data.vo.v1.PersonVO
import br.com.api.data.vo.v2.PersonVO as PersonVOV2
import br.com.api.exceptions.ResourceNotFoundException
import br.com.api.mapper.DozerMapper
import br.com.api.mapper.custom.PersonMapper
import br.com.api.model.Person
import br.com.api.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger

@Service
class PersonService {

    @Autowired
    private lateinit var repository: PersonRepository

    @Autowired
    private lateinit var mapper: PersonMapper

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(): List<PersonVO> {
        logger.info("Finding all people!")
        val persons =  repository.findAll()
        val vos = DozerMapper.parseListObjects(persons, PersonVO::class.java)
        for (person in vos) {
            val withSelfRel = linkTo(PersonController::class.java).slash(person.id).withSelfRel()
            person.add(withSelfRel)
        }
        return vos
    }

    fun findById(id: Long): PersonVO {
        logger.info("Finding one PersonVO with ID $id!")
        var person =  repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}
        val personVO: PersonVO = DozerMapper.parseObject(person, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.id).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun create(person: PersonVO) : PersonVO{
        logger.info("Creating one PersonVO with name ${person.firstName}!")
        var entity: Person = DozerMapper.parseObject(person, Person::class.java)
        val personVO: PersonVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java) // Salva e converte para personVO para retornar reposta
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.id).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun createV2(personVO: PersonVOV2) : PersonVOV2{
        logger.info("Creating one PersonVO with name ${personVO.firstName}!")
        var entity: Person = mapper.mapVOToEntity(personVO)
        return mapper.mapEntityToVO(repository.save(entity)) // Salva e converte para personVO para retornar reposta
    }

    fun update(personVO: PersonVO) : PersonVO {
        logger.info("Updating one PersonVO with id ${personVO.id}!")

        var entity = repository.findById(personVO.id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}

        entity.firstName = personVO.firstName
        entity.lastName = personVO.lastName
        entity.address = personVO.address
        entity.gender = personVO.gender
        val person: PersonVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.id).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun delete(id: Long) {
        logger.info("Deleting one personVO with id ${id}!")

        var entity =  repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}
        repository.delete(entity)
    }

}