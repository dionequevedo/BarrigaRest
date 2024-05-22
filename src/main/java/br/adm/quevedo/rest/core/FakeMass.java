package br.adm.quevedo.rest.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.github.javafaker.Faker;

public class FakeMass {

    Faker faker = new Faker(new Locale("pt-BR"));

    private String DATA_FUTURA = getDataDiferencaDias(5);

    private String NOME_CONTATO = faker.name().firstName();

    public String getNOME_CONTATO() {
        return NOME_CONTATO;
    }

    public String getDATA_FUTURA() {
        return DATA_FUTURA;
    }

    public String getDataDiferencaDias(Integer qtdDias){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, qtdDias);
        return getDataFormatada(cal.getTime());
    }

    public static String getDataFormatada(Date data){
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(data);
    }


}
