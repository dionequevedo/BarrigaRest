package br.adm.quevedo.rest.core;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import static io.restassured.RestAssured.*;

public class BaseTest implements Constantes {
    @BeforeClass
    public static void setUp() throws Exception {
        // SetUp do RestAssured usando as Constantes já definidas
        baseURI = APP_BASE_URL_HTTPS;
        port = APP_PORT_HTTPS;
        basePath = APP_BASE_PATH;

        // Criando um builder para Request Specification
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri(baseURI);
        reqBuilder.setPort(port);
        reqBuilder.setBasePath(basePath);
        reqBuilder.setContentType(APP_CONTENT_TYPE);
        requestSpecification = reqBuilder.build();

        // Criando um builder para o Response Specification
        ResponseSpecBuilder respBuilder = new ResponseSpecBuilder();
        respBuilder.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
        responseSpecification = respBuilder.build();

        // Habilitar log de requisição e response em caso de falha
        enableLoggingOfRequestAndResponseIfValidationFails();
    }

}
