package br.adm.quevedo.rest.tests.refac;

import br.adm.quevedo.rest.core.BaseTest;
import br.adm.quevedo.rest.core.FakeMass;
import br.adm.quevedo.rest.tests.Movimentacao;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovimentacoesTest extends BaseTest {

    FakeMass DataUtils = new FakeMass();

    @Test
    public void deveInserirUmaMovimentacaoComSucesso() {
        Movimentacao mov = getMovimentacaoValida();

        given()
                .body(mov)
        .when()
                .post("/transacoes")
        .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
        ;

    }

    @Test
    public void deveValidarCamposObrigatoriosNaMovimentacao() {

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
    public void naoDeveCadastrarUmaMovimentacaoFutura() {

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
    public void naoDeveExcluirContaComMovimentacoes() {

        given()
                .pathParam("id", getIdContaPeloNome("Conta com movimentacao"))
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
    public void deveRemoverMovimentacaoComSucesso() {

        given()
                .pathParam("id", getIdMovPelaDescr("Movimentacao para exclusao"))
                .when()
                .delete("/transacoes/{id}")
                .then()
                .statusCode(204)
        ;
    }

    public Integer getIdContaPeloNome(String nome) {
        return RestAssured.get("/contas?nome=" + nome).then().extract().path("id[0]");
    }

    public Integer getIdMovPelaDescr(String desc) {
        return RestAssured.get("/transacoes?descricao=" + desc).then().extract().path("id[0]");
    }

    private Movimentacao getMovimentacaoValida(){
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(getIdContaPeloNome("Conta para movimentacoes"));
        mov.setDescricao("Descricao da Movimentação");
        mov.setEnvolvido("Envolvido na Mov");
        mov.setObservacao("Observacao da Mov");
        mov.setTipo("REC");
        mov.setData_transacao(DataUtils.getDataDiferencaDias(-1));
        mov.setData_pagamento(DataUtils.getDataDiferencaDias(3));
        mov.setValor(175.94F);
        mov.setStatus(true);
        return mov;
    }

    private Movimentacao getMovimentacaoDataFutura(){
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(getIdContaPeloNome("Conta para movimentacoes"));
        mov.setDescricao("Descricao da Movimentação");
        mov.setEnvolvido("Envolvido na Mov");
        mov.setObservacao("Observacao da Mov");
        mov.setTipo("REC");
        mov.setData_transacao(DataUtils.getDataDiferencaDias(2));
        mov.setData_pagamento(DataUtils.getDataDiferencaDias(4));
        mov.setValor(243.27F);
        mov.setStatus(true);
        return mov;
    }
}
