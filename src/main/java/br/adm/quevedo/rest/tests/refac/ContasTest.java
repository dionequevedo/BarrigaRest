package br.adm.quevedo.rest.tests.refac;

import br.adm.quevedo.rest.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class ContasTest extends BaseTest {

    @Test
    public void deveIncluirContaComSucesso() {


        given()
                .body("{\"nome\": \"Conta inserida\"}")
                .when()
                .post("/contas")
                .then()
                .log().all()
                .statusCode(201)
        ;
    }

    @Test
    public void deveAlterarContaComSucesso() {

        given()
                .body("{\"nome\": \"Conta alterada\"}")
                .pathParam("id", getIdContaPeloNome("Conta para alterar"))
         .when()
                .put("/contas/{id}")
         .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("nome", is("Conta alterada"));
    }

    @Test
    public void naoDeveInserirContaComMesmoNome() {

        given()
                .body("{\"nome\": \"Conta mesmo nome\"}")
        .when()
                .post("/contas")
        .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("error", is("Já existe uma conta com esse nome!"));
    }

    public Integer getIdContaPeloNome(String nome) {
        return RestAssured.get("/contas?nome=" + nome).then().extract().path("id[0]");
    }
}
