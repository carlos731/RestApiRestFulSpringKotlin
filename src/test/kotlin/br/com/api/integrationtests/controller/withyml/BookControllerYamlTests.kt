package br.com.api.integrationtests.controller.withyml

import br.com.api.integrationtests.TestConfigs
import br.com.api.integrationtests.controller.withyml.mapper.YMLMapper
import br.com.api.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.api.integrationtests.vo.AccountCredentialsVO
import br.com.api.integrationtests.vo.PersonVO
import br.com.api.integrationtests.vo.TokenVO
import br.com.api.integrationtests.vo.wrappers.WrapperPersonVO
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.EncoderConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookControllerYamlTests : AbstractIntegrationTest() {

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: YMLMapper
    private lateinit var person: PersonVO

    @BeforeAll
    fun setupTests(){
        objectMapper = YMLMapper()
        person = PersonVO()
    }

    @Test
    @Order(0)
    fun testLogin() {
        val user = AccountCredentialsVO(
            username = "carlos",
            password = "Kotlin@1234"
        )

        val token = RestAssured.given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .basePath("/auth/signin")
            .port(TestConfigs.SERVER_PORT)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .body(user, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java, objectMapper)
            .accessToken

        specification = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $token")
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
    }

    @Test
    @Order(1)
    fun testCreate() {
        mockPerson()

        val item = RestAssured.given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .body(person, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)

        person = item

        Assertions.assertNotNull(item.id)
        Assertions.assertTrue(item.id > 0)
        Assertions.assertNotNull(item.firstName)
        Assertions.assertNotNull(item.lastName)
        Assertions.assertNotNull(item.address)
        Assertions.assertNotNull(item.gender)
        Assertions.assertEquals("Richard", item.firstName)
        Assertions.assertEquals("Stallman", item.lastName)
        Assertions.assertEquals("New York City, New York, US", item.address)
        Assertions.assertEquals("Male", item.gender)
        Assertions.assertEquals(true, item.enabled)
    }

    @Test
    @Order(2)
    fun testUpdate() {
        person.lastName = "Matthew Stallman"

        val item = RestAssured.given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .body(person, objectMapper)
            .`when`()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)

        person = item

        Assertions.assertNotNull(item.id)
        Assertions.assertNotNull(item.firstName)
        Assertions.assertNotNull(item.lastName)
        Assertions.assertNotNull(item.address)
        Assertions.assertNotNull(item.gender)
        Assertions.assertEquals(person.id, item.id)
        Assertions.assertEquals("Richard", item.firstName)
        Assertions.assertEquals("Matthew Stallman", item.lastName)
        Assertions.assertEquals("New York City, New York, US", item.address)
        Assertions.assertEquals("Male", item.gender)
        Assertions.assertEquals(true, item.enabled)
    }

    @Test
    @Order(3)
    fun testDisablePersonById() {
        val item = RestAssured.given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .pathParam("id", person.id)
            .`when`()
            .patch("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)

        person = item

        Assertions.assertNotNull(item.id)
        Assertions.assertNotNull(item.firstName)
        Assertions.assertNotNull(item.lastName)
        Assertions.assertNotNull(item.address)
        Assertions.assertNotNull(item.gender)
        Assertions.assertEquals(person.id, item.id)
        Assertions.assertEquals("Richard", item.firstName)
        Assertions.assertEquals("Matthew Stallman", item.lastName)
        Assertions.assertEquals("New York City, New York, US", item.address)
        Assertions.assertEquals("Male", item.gender)
        Assertions.assertEquals(false, item.enabled)
    }

    @Test
    @Order(4)
    fun testFindById() {
        val item = RestAssured.given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .pathParam("id", person.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)

        person = item

        Assertions.assertNotNull(item.id)
        Assertions.assertNotNull(item.firstName)
        Assertions.assertNotNull(item.lastName)
        Assertions.assertNotNull(item.address)
        Assertions.assertNotNull(item.gender)
        Assertions.assertEquals(person.id, item.id)
        Assertions.assertEquals("Richard", item.firstName)
        Assertions.assertEquals("Matthew Stallman", item.lastName)
        Assertions.assertEquals("New York City, New York, US", item.address)
        Assertions.assertEquals("Male", item.gender)
        Assertions.assertEquals(false, item.enabled)
    }

    @Test
    @Order(5)
    fun testDelete() {
        RestAssured.given()
            .spec(specification)
            .pathParam("id", person.id)
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)
    }

    @Test
    @Order(6)
    fun testFindAll() {
        val wrapper = RestAssured.given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .queryParams(
                "page", 3,
                "size",12,
                "direction", "asc")
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(WrapperPersonVO::class.java, objectMapper)

        val people = wrapper.embedded!!.persons

        val item1 = people?.get(0)

        Assertions.assertNotNull(item1!!.id)
        Assertions.assertNotNull(item1.firstName)
        Assertions.assertNotNull(item1.lastName)
        Assertions.assertNotNull(item1.address)
        Assertions.assertNotNull(item1.gender)
        Assertions.assertEquals("Allin", item1.firstName)
        Assertions.assertEquals("Otridge", item1.lastName)
        Assertions.assertEquals("09846 Independence Center", item1.address)
        Assertions.assertEquals("Male", item1.gender)
        Assertions.assertEquals(false, item1.enabled)

        val item2 = people[6]

        Assertions.assertNotNull(item2.id)
        Assertions.assertNotNull(item2.firstName)
        Assertions.assertNotNull(item2.lastName)
        Assertions.assertNotNull(item2.address)
        Assertions.assertNotNull(item2.gender)
        Assertions.assertEquals("Alvera", item2.firstName)
        Assertions.assertEquals("MacMillan", item2.lastName)
        Assertions.assertEquals("59929 Loeprich Place", item2.address)
        Assertions.assertEquals("Female", item2.gender)
        Assertions.assertEquals(false, item2.enabled)
    }

    @Test
    @Order(7)
    fun testFindPersonByName() {
        val wrapper = RestAssured.given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .pathParam("firstName", "Ayr")
            .queryParams(
                "page", 0,
                "size", 12,
                "direction", "asc")
            .`when`()["findPersonByName/{firstName}"]
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(WrapperPersonVO::class.java, objectMapper)

        val people = wrapper.embedded!!.persons

        val item1 = people?.get(0)

        Assertions.assertNotNull(item1!!.id)
        Assertions.assertNotNull(item1.firstName)
        Assertions.assertNotNull(item1.lastName)
        Assertions.assertNotNull(item1.address)
        Assertions.assertNotNull(item1.gender)
        Assertions.assertEquals("Ayrton", item1.firstName)
        Assertions.assertEquals("Senna", item1.lastName)
        Assertions.assertEquals("São Paulo", item1.address)
        Assertions.assertEquals("Male", item1.gender)
        Assertions.assertEquals(true, item1.enabled)
    }

    @Test
    @Order(8)
    fun testFindAllWithoutToken() {

        val specificationWithoutToken: RequestSpecification = RequestSpecBuilder()
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

        RestAssured.given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specificationWithoutToken)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .`when`()
            .get()
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()

    }


    @Test
    @Order(7)
    fun testHATEOAS() {
        val content = RestAssured.given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .queryParams(
                "page", 3,
                "size",12,
                "direction", "asc")
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        Assertions.assertTrue(content.contains("""_links":{"self":{"href":"http://localhost:8888/api/person/v1/199"}}}"""))
        Assertions.assertTrue(content.contains("""_links":{"self":{"href":"http://localhost:8888/api/person/v1/797"}}}"""))
        Assertions.assertTrue(content.contains("""_links":{"self":{"href":"http://localhost:8888/api/person/v1/686"}}}"""))
        Assertions.assertTrue(content.contains("""_links":{"self":{"href":"http://localhost:8888/api/person/v1/340"}}}"""))

        Assertions.assertTrue(content.contains("""{"first":{"href":"http://localhost:8888/api/person/v1?direction=asc&page=0&size=12&sort=firstName,asc"}"""))
        Assertions.assertTrue(content.contains(""","prev":{"href":"http://localhost:8888/api/person/v1?direction=asc&page=2&size=12&sort=firstName,asc"}"""))
        Assertions.assertTrue(content.contains(""","self":{"href":"http://localhost:8888/api/person/v1?direction=asc&page=3&size=12&sort=firstName,asc"}"""))
        Assertions.assertTrue(content.contains(""","next":{"href":"http://localhost:8888/api/person/v1?direction=asc&page=4&size=12&sort=firstName,asc"}"""))
        Assertions.assertTrue(content.contains(""","last":{"href":"http://localhost:8888/api/person/v1?direction=asc&page=83&size=12&sort=firstName,asc"}"""))

        Assertions.assertTrue(content.contains(""""page":{"size":12,"totalElements":1007,"totalPages":84,"number":3}}"""))
    }

    private fun mockPerson() {
        person.firstName = "Richard"
        person.lastName = "Stallman"
        person.address = "New York City, New York, US"
        person.gender = "Male"
        person.enabled = true
    }
}