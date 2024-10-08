package br.com.api.integrationtests.controller.withyml


import br.com.api.integrationtests.TestConfigs
import br.com.api.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.api.integrationtests.vo.AccountCredentialsVO
import br.com.api.integrationtests.vo.TokenVO
import br.com.api.integrationtests.controller.withyml.mapper.YMLMapper
import io.restassured.RestAssured
import io.restassured.config.EncoderConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerYmlTest : AbstractIntegrationTest(){

    private lateinit var objectMapper: YMLMapper
    private lateinit var tokenVO: TokenVO

    @BeforeAll
    fun setupTests(){
        tokenVO = TokenVO()
        objectMapper = YMLMapper()
    }

    @Test
    @Order(0)
    fun testLogin() {
        val user = AccountCredentialsVO(
            username = "carlos",
            password = "Kotlin@1234"
        )

        tokenVO = RestAssured.given()
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
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .body(user, objectMapper)
            .`when`()
                .post()
                    .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .`as`(TokenVO::class.java, objectMapper)

        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
    }

    @Test
    @Order(1)
    fun testRefreshToken() {

        tokenVO = RestAssured.given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("username", tokenVO.username)
                .header(
                    TestConfigs.HEADER_PARAM_AUTHORIZATION,
          "Bearer ${tokenVO.refreshToken}")
            .`when`()
                .put("{username}")
                    .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .`as`(TokenVO::class.java, objectMapper)

        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
    }
}