package br.adm.quevedo.rest.tests.refac.suite;

import br.adm.quevedo.rest.core.BaseTest;
import br.adm.quevedo.rest.tests.refac.AuthTest;
import br.adm.quevedo.rest.tests.refac.ContasTest;
import br.adm.quevedo.rest.tests.refac.MovimentacoesTest;
import br.adm.quevedo.rest.tests.refac.SaldoTest;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.runners.Suite.*;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
        ContasTest.class,
        MovimentacoesTest.class,
        SaldoTest.class,
        AuthTest.class
})
public class Suite extends BaseTest {

    @BeforeClass
    public static void setupTest(){
        Map<String, String> login = new HashMap<>();
        login.put("email", "test@dionequevedo.com.br");
        login.put("senha", "123456");

        String TOKEN =
                given()
                        .body(login)
                        .when()
                        .post("/signin")
                        .then()
                        .statusCode(200)
                        .extract().path("token");
        RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN);
        RestAssured.get("/reset").then().statusCode(200);
    }
}
