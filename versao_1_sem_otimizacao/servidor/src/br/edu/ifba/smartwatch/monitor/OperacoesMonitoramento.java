package br.edu.ifba.smartwatch.monitor;

/**
 * Interface que define de forma abstrata os contratos para o monitoramento
 * de cadência (SPM) dos 10 smartwatches em corridas de longa distância.
 */

public interface OperacoesMonitoramento {

    // (a) e (b) - Gerar automaticamente os dados dos sensores para qualquer quantidade N
    Smartwatch[] gerarDadosSensores( int quantidadeDispositivos, int quantidadeLeiturasN );

    // (d.1) - Imprimir a lista de coisas sobre os quais ocorre o monitoramento
    void imprimirDispositivos( Smartwatch[] dispositivos );

    // (d.2) - Imprimir a lista das leituras do monitoramento identificando cada dispositivo
    void imprimirLeituras( Smartwatch[] dispositivos );

    // (d.3) - Ordenação crescente dos dados considerando os valores lidos dos sensores
    void ordenarLeiturasCrescente( Smartwatch[] dispositivos );

    // (d.4) - Funcionalidade extra O(N^3): Detecção de tríades de degradação biomacânica
    NivelDesgaste analisarTriadesDegradacao(int[] leiturasCadencia, String idDispositivo, String identificadorCorredor);
}
