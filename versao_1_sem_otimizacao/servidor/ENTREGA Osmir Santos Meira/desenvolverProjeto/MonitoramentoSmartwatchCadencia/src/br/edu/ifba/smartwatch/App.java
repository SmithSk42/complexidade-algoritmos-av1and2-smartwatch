package br.edu.ifba.smartwatch;

import br.edu.ifba.smartwatch.impl.MonitoramentoCadenciaImpl;
import br.edu.ifba.smartwatch.monitor.NivelDesgaste;
import br.edu.ifba.smartwatch.monitor.OperacoesMonitoramento;
import br.edu.ifba.smartwatch.monitor.Smartwatch;

public class App {

    // Critério 4.1: Devem ser simulados os monitoramentos de 10 dispositivos
    private static final int QTD_DISPOSITIVOS = 10;

    /**
     * Método principal (Critério 4.2.c) a partir do qual todas as funcionalidades
     * são executadas e testadas.
     * 
     * - COMPLEXIDADE GLOBAL: O(N^3), dominada assintoticamente pela funcionalidade (d.4).
     */
    public static void main(String[] args) throws Exception {
        // Instanciação partindo da abstração (Interface) para a implementação concreta (impl)
        OperacoesMonitoramento monitor = new MonitoramentoCadenciaImpl();

        // Quantidade N de leituras de cadência (SPM) geradas por cada Smartwatch.
        // Pode ser alterada para qualquer valor N conforme o Critério 4.2.a.
        int quantidadeLeiturasN = 200;

        // ANÁLISE DE CASOS SOBRE O PROCESSAMENTO DOS SENSORES:
        // - CASO LEVE (N pequeno, ex: N = 50): Todas as classes O(1), O(N), O(N^2) e O(N^3)
        //   executam em poucos milissegundos sem diferença perceptível a olho nu.
        // - CASO MODERADO (N médio, ex: N = 200 a 500): As operações O(N) e O(N^2) continuam
        //   instantâneas, mas a operação cúbica O(N^3) já passa a exigir dezenas de milhões
        //   de iterações nos 10 dispositivos.
        // - PIOR CENÁRIO DE ESCALABILIDADE (N muito grande, ex: N >= 5.000): A função O(N^3)
        //   sofre explosão combinatória, demonstrando na prática o gargalo de algoritmos cúbicos.

        // (a) e (b) Gerar dados automaticamente para os 10 smartwatches com coluna de identificação
        Smartwatch[] smartwatches = monitor.gerarDadosSensores(QTD_DISPOSITIVOS, quantidadeLeiturasN);

        // (d.1) Imprimir a lista de coisas sobre os quais ocorre o monitoramento - O(1) em relação a N
        monitor.imprimirDispositivos(smartwatches);

        // (d.2) Imprimir a lista das leituras brutas do monitoramento por dispositivo - O(N)
        monitor.imprimirLeituras(smartwatches);

        // (d.4) Funcionalidade Extra O(N^3): Analisar tríades de degradação biomecânica na ordem temporal
        System.out.println("====================================================================");
        System.out.println(" [d.4] ANÁLISE EXTRA O(N^3) - TRÍADES DE DEGRADAÇÃO BIOMECÂNICA");
        System.out.println("====================================================================");
        for (int i = 0; i < smartwatches.length; i++) {
            NivelDesgaste resultado = monitor.analisarTriadesDegradacao(
                    smartwatches[i].getLeiturasCadencia(),
                    smartwatches[i].getIdDispositivo(),
                    smartwatches[i].getIdentificadorCorredor()
            );

            if (resultado == NivelDesgaste.RISCO_CRITICO_LESAO) {
                System.out.println("    [!] Alerta médico: Atleta com alto índice de fadiga e sobrecarga!");
            }
        }
        System.out.println("====================================================================\n");

        // (d.3) Ordenação crescente dos valores lidos dos sensores de cada dispositivo - O(N^2)
        monitor.ordenarLeiturasCrescente(smartwatches);

        // Impressão final para comprovar as leituras devidamente ordenadas em ordem crescente
        System.out.println(">>> EXIBIÇÃO DAS LEITURAS APÓS A ORDENAÇÃO CRESCENTE (d.3):");
        monitor.imprimirLeituras(smartwatches);
    }
}