package br.com.api.services

import br.com.api.data.vo.v1.PersonVO
import br.com.api.data.vo.v2.PersonVO as PersonVOV2
import br.com.api.exceptions.ResourceNotFoundException
import br.com.api.mapper.DozerMapper
import br.com.api.mapper.custom.PersonMapper
import br.com.api.model.Person
import br.com.api.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
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
        return DozerMapper.parseListObjects(persons, PersonVO::class.java)
    }

    fun findById(id: Long): PersonVO {
        logger.info("Finding one PersonVO!")
        var person =  repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}
        return DozerMapper.parseObject(person, PersonVO::class.java)
    }

    fun create(personVO: PersonVO) : PersonVO{
        logger.info("Creating one PersonVO with name ${personVO.firstName}!")
        var entity: Person = DozerMapper.parseObject(personVO, Person::class.java)
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java) // Salva e converte para personVO para retornar reposta
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
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
    }

    fun delete(id: Long) {
        logger.info("Deleting one personVO with id ${id}!")

        var entity =  repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}
        repository.delete(entity)
    }

}