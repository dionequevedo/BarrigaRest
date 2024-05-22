package br.adm.quevedo.rest.tests;

import br.adm.quevedo.rest.core.BaseTest;
import static org.hamcrest.Matchers.*;

import br.adm.quevedo.rest.core.FakeMass;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends BaseTest {
    private static FakeMass massa = new FakeMass();
    private static Integer ID_CONTA;
    private static Integer ID_TRANSACAO;
    private static Float VLR_TRANS = 175.07F;
    private static String DEFAULT_ACCOUNT = massa.getNOME_CONTATO();
    private static String NEW_ACCOUNT = massa.getNOME_CONTATO();
    private static String FUTURE_DATE = massa.getDATA_FUTURA();

    @Test
    public void t01_deveCadastrarContaComSucesso() {

        ID_CONTA =
        given()
                .body("{\"nome\": \"" + DEFAULT_ACCOUNT + "\"}")
        .when()
                .post("/contas")
        .then()
                .log().all()
                .statusCode(201)
                .extract().path("id")
        ;
    }

    @Test
    public void t02_deveAlterarContaComSucesso() {

        given()
                .body("{\"nome\": \"" + NEW_ACCOUNT + "\"}")
                .pathParam("id", ID_CONTA)
        .when()
                .put("/contas/{id}")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("nome", is(NEW_ACCOUNT));
    }

    @Test
    public void t03_naoDeveCadastrarContaComMesmoNome() {

        given()
                .body("{\"nome\": \"conta de teste alterada com sucesso\"}")
        .when()
                .post("/contas")
        .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("error", is("Já existe uma conta com esse nome!"));
    }

    @Test
    public void t04_deveCadastrarUmaMovimentacaoComSucesso() {

        Movimentacao mov = getMovimentacaoValida();
        Movimentacao newMov =
        given()
                .body(mov)
        .when()
                .post("/transacoes")
        .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().body().as(Movimentacao.class)
        ;
        ID_TRANSACAO = newMov.getId();
    }

    @Test
    public void t05_deveValidarCamposObrigatoriosNaMovimentacao() {

        given()
                .body("{}")
        .when()
                .post("/transacoes")
        .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("$", hasSize(8))
                .body("msg", hasItems(
                        "Data da Movimentação é obrigatório",
                        "Data do pagamento é obrigatório",
                        "Descrição é obrigatório",
                        "Interessado é obrigatório",
                        "Valor é obrigatório",
                        "Valor deve ser um número",
                        "Conta é obrigatório",
                        "Situação é obrigatório"))
        ;
    }

    @Test
    public void t06_naoDeveCadastrarUmaMovimentacaoFutura() {

        Movimentacao mov = getMovimentacaoDataFutura();

        given()
                .body(mov)
        .when()
                .post("/transacoes")
        .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("param", hasItem("data_transacao"))
                .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
                .body("value", hasItem(mov.getData_transacao()))
        ;
    }

    @Test
    public void t07_naoDeveExcluirContaComMovimentacoes() {

        given()
                .pathParam("id", ID_CONTA)
        .when()
                .delete("/contas/{id}")
        .then()
                .statusCode(500)
                .contentType(ContentType.JSON)
                .body("name", is("error"))
                .body("constraint", is("transacoes_conta_id_foreign"))
        ;
    }

    @Test
    public void t08_deveCalcularSaldoContas() {

        given()
        .when()
                .get("/saldo")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("find{it.conta_id == " + ID_CONTA + "}.saldo", is("175.07"))
        ;
    }

    @Test
    public void t09_deveRemoverMovimentacaoComSucesso() {

        given()
                .pathParam("id", ID_TRANSACAO)
        .when()
                .delete("/transacoes/{id}")
        .then()
                .statusCode(204)
        ;
    }

    @Test
    public void t10_naoDeveAcessarAPISemToken() {
        FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
        req.removeHeader("Authorization");
        given()
        .when()
                .get("/contas")
        .then()
                .statusCode(401)
        ;
    }

    private Movimentacao getMovimentacaoValida(){
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(ID_CONTA);
        mov.setDescricao("Descricao da Movimentação");
        mov.setEnvolvido("Envolvido na Mov");
        mov.setObservacao("Observacao da Mov");
        mov.setTipo("REC");
        mov.setData_transacao(massa.getDataDiferencaDias(-1));
        mov.setData_pagamento(massa.getDataDiferencaDias(3));
        mov.setValor(VLR_TRANS);
        mov.setStatus(true);
        return mov;
    }

    private Movimentacao getMovimentacaoDataFutura(){
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(ID_CONTA);
        mov.setDescricao("Descricao da Movimentação");
        mov.setEnvolvido("Envolvido na Mov");
        mov.setObservacao("Observacao da Mov");
        mov.setTipo("REC");
        mov.setData_transacao(FUTURE_DATE);
        mov.setData_pagamento("16/05/2024");
        mov.setValor(VLR_TRANS);
        mov.setStatus(true);
        return mov;
    }
}
