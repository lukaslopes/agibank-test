package br.com.agibank.seguros.domain.enums;

import lombok.Getter;

@Getter
public enum TipoSeguro {
    BRONZE("Bronze", 50.0, 2.0, "Coberturas básicas"),
    PRATA("Prata", 100.0, 1.5, "Coberturas básicas e intermediárias"),
    OURO("Ouro", 200.0, 1.2, "Coberturas completas");

    private final String descricao;
    private final double valorBase;
    private final double fatorMultiplicador;
    private final String cobertura;

    TipoSeguro(String descricao, double valorBase, double fatorMultiplicador, String cobertura) {
        this.descricao = descricao;
        this.valorBase = valorBase;
        this.fatorMultiplicador = fatorMultiplicador;
        this.cobertura = cobertura;
    }
}
