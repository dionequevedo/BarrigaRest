package br.adm.quevedo.rest.tests.refac;

import br.adm.quevedo.rest.core.BaseTest;
import br.adm.quevedo.rest.core.FakeMass;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SaldoTest extends BaseTest {

    FakeMass DataUtils = new FakeMass();

    @Test
    public void deveCalcularSaldoContas() {

        given()
        .when()
                .get("/saldo")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("find{it.conta_id == " + getIdContaPeloNome("Conta para saldo") + "}.saldo", is("534.00"))
        ;
    }

    public Integer getIdContaPeloNome(String nome) {
        return RestAssured.get("/contas?nome=" + nome).then().extract().path("id[0]");
    }
}
