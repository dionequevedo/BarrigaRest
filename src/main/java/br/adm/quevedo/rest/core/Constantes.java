package br.adm.quevedo.rest.core;

import io.restassured.http.ContentType;

public interface Constantes {
    String APP_BASE_URL_HTTP = "http://barrigarest.wcaquino.me";
    Integer APP_PORT_HTTP = 80;
    String APP_BASE_URL_HTTPS = "https://barrigarest.wcaquino.me";
    Integer APP_PORT_HTTPS = 443;
    String APP_BASE_PATH = "";

    ContentType APP_CONTENT_TYPE = ContentType.JSON;

    Long MAX_TIMEOUT = 4000L;
}
