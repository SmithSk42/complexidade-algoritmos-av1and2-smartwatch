package br.edu.ifba.smartwatch.impl;

import java.util.Random;
import br.edu.ifba.smartwatch.monitor.NivelDesgaste;
import br.edu.ifba.smartwatch.monitor.OperacoesMonitoramento;
import br.edu.ifba.smartwatch.monitor.Smartwatch;

/**
 * Implementação concreta dos algoritmos de sensoriamento de Cadência (SPM)
 * em Smartwatches para corridas de longa distância.
 */
public class MonitoramentoCadenciaImpl implements OperacoesMonitoramento {

    /**
     * FUNCIONALIDADE (a) e (b): Geração automática e randômica dos dados dos sensores
     * para qualquer quantidade N de leituras por dispositivo.
     * 
     * - COMPLEXIDADE: O(M * N), onde M é a quantidade de smartwatches (10) e N é a
     *   quantidade de leituras por dispositivo. Como M é constante (10), a complexidade
     *   assintótica em relação a N é Linear: O(N).
     * - JUSTIFICATIVA: Possui dois laços aninhados onde o externo roda M vezes (10 vezes)
     *   e o interno roda N vezes para sortear valores de cadência entre 135 e 195 SPM.
     *   O total de atribuições cresce de maneira diretamente proporcional a N (10 * N).
     * - CONSEQUÊNCIAS PARA ENTRADAS MUITO GRANDES: O tempo de execução cresce linearmente,
     *   sendo bastante eficiente em CPU, mas se N for extremamente alto (ex: centenas de
     *   milhões de leituras), o consumo de memória RAM (Heap) crescerá na ordem O(N),
     *   podendo estourar a memória disponível (OutOfMemoryError).
     */
    @Override
    public Smartwatch[] gerarDadosSensores(int quantidadeDispositivos, int quantidadeLeiturasN) {
        Smartwatch[] dispositivos = new Smartwatch[quantidadeDispositivos];
        Random random = new Random();

        for (int i = 0; i < quantidadeDispositivos; i++) {
            String id = "SW-CORRIDA-" + (i + 1);
            String atleta = "Corredor Fundista #" + (i + 1);
            dispositivos[i] = new Smartwatch(id, atleta, quantidadeLeiturasN);

            int[] leituras = dispositivos[i].getLeiturasCadencia();
            for (int j = 0; j < quantidadeLeiturasN; j++) {
                // Gera leituras de cadência entre 135 e 195 Passos Por Minuto (SPM)
                leituras[j] = 135 + random.nextInt(61);
            }
        }
        return dispositivos;
    }

    /**
     * FUNCIONALIDADE (d.1): Imprimir a lista de coisas sobre os quais ocorre o monitoramento.
     * 
     * - COMPLEXIDADE: O(M), onde M = 10 dispositivos. Em função da quantidade de leituras N
     *   dos sensores, a complexidade é Constante: O(1).
     * - JUSTIFICATIVA: O método percorre apenas o vetor de dispositivos (tamanho fixo 10)
     *   com um único laço simples, imprimindo o ID do smartwatch e do corredor sem iterar
     *   pelas N leituras de cadência.
     * - CONSEQUÊNCIAS PARA ENTRADAS MUITO GRANDES: Nenhuma degradação. Mesmo que cada
     *   smartwatch possua milhões de leituras N, o tempo de execução deste método permanece
     *   exatamente o mesmo (constante e instantâneo).
     */
    @Override
    public void imprimirDispositivos(Smartwatch[] dispositivos) {
        System.out.println("====================================================================");
        System.out.println(" [d.1] LISTA DE COISAS MONITORADAS (10 SMARTWATCHES DE CORRIDA)");
        System.out.println("====================================================================");
        for (int i = 0; i < dispositivos.length; i++) {
            System.out.println(" -> ID Sensor: " + dispositivos[i].getIdDispositivo()
                    + " | Atleta: " + dispositivos[i].getIdentificadorCorredor()
                    + " | Leituras Capturadas: " + dispositivos[i].getLeiturasCadencia().length);
        }
        System.out.println("====================================================================\n");
    }

    /**
     * FUNCIONALIDADE (d.2): Imprimir a lista das leituras do monitoramento identificando
     * claramente a qual dispositivo cada leitura pertence.
     * 
     * - COMPLEXIDADE: O(M * N), equivalente à classe Linear O(N) considerando M = 10 constante.
     * - JUSTIFICATIVA: O laço externo percorre os 10 smartwatches e o laço interno visita
     *   cada uma das N leituras do vetor exatamente uma vez para imprimi-la no terminal
     *   junto à identificação do dispositivo.
     * - CONSEQUÊNCIAS PARA ENTRADAS MUITO GRANDES: Para um N muito grande, embora o número
     *   de passos cresça de forma linear O(N), a operação de escrita no console (I/O) é
     *   custosa, fazendo o terminal demorar vários segundos ou minutos para rolar todas
     *   as linhas impressas na tela.
     */
    @Override
    public void imprimirLeituras(Smartwatch[] dispositivos) {
        System.out.println("====================================================================");
        System.out.println(" [d.2] LISTA DE LEITURAS DE CADÊNCIA (SPM) POR DISPOSITIVO");
        System.out.println("====================================================================");
        for (int i = 0; i < dispositivos.length; i++) {
            Smartwatch sw = dispositivos[i];
            int[] leituras = sw.getLeiturasCadencia();
            System.out.println("Dispositivo [" + sw.getIdDispositivo() + " - " + sw.getIdentificadorCorredor() + "]:");

            for (int j = 0; j < leituras.length; j++) {
                System.out.print(" [" + sw.getIdDispositivo() + " | Seg " + (j + 1) + ": " + leituras[j] + " SPM]");
                if ((j + 1) % 4 == 0 || j == leituras.length - 1) {
                    System.out.println();
                }
            }
            System.out.println("--------------------------------------------------------------------");
        }
        System.out.println();
    }

    /**
     * FUNCIONALIDADE (d.3): Ordenação crescente dos dados considerando os valores lidos
     * dos sensores para cada coisa monitorada (Algoritmo Bubble Sort / Troca Direta manual).
     * 
     * - COMPLEXIDADE: O(M * N^2), sendo da classe Quadrática O(N^2) em função de N.
     * - JUSTIFICATIVA: Para cada smartwatch, há dois laços dependentes aninhados que
     *   comparam pares adjacentes e realizam trocas quando fora de ordem. O número de
     *   comparações por dispositivo é N*(N-1)/2, cujo termo de maior grau é (N^2)/2,
     *   enquadrando o algoritmo na ordem O(N^2).
     * - CONSEQUÊNCIAS PARA ENTRADAS MUITO GRANDES: Como o crescimento é quadrático O(N^2),
     *   multiplicar a entrada N por 10 faz o número de operações aumentar 100 vezes. Para
     *   entradas grandes (ex: N > 100.000 leituras), a ordenação sofrerá lentidão severa,
     *   levando dezenas de segundos para concluir os 10 dispositivos.
     */
    @Override
    public void ordenarLeiturasCrescente(Smartwatch[] dispositivos) {
        System.out.println("====================================================================");
        System.out.println(" [d.3] ORDENAÇÃO CRESCENTE DAS LEITURAS POR SMARTWATCH - O(N^2)");
        System.out.println("====================================================================");
        for (int i = 0; i < dispositivos.length; i++) {
            int[] vetor = dispositivos[i].getLeiturasCadencia();
            int n = vetor.length;

            // Ordenação manual sem uso de bibliotecas da linguagem (Critério 4.3)
            for (int j = 0; j < n - 1; j++) {
                for (int k = 0; k < n - 1 - j; k++) {
                    if (vetor[k] > vetor[k + 1]) {
                        int aux = vetor[k];
                        vetor[k] = vetor[k + 1];
                        vetor[k + 1] = aux;
                    }
                }
            }
            System.out.println(" -> " + dispositivos[i].getIdDispositivo() + " ordenado! Menor cadência: "
                    + vetor[0] + " SPM | Maior cadência: " + vetor[n - 1] + " SPM");
        }
        System.out.println("====================================================================\n");
    }

    /**
     * FUNCIONALIDADE EXTRA (d.4): Análise de Tríades de Degradação Biomecânica e Sobrecarga.
     * Verifica todas as combinações de três instantes distintos do percurso (t1 < t2 < t3)
     * onde o corredor apresentava alta cadência em t1 (>= 180 SPM), sofreu queda severa por
     * fadiga em t2 (queda >= 30 SPM) e forçou um arranque abrupto em t3 (subida >= 35 SPM),
     * configurando padrão biomecânico de risco de lesão articular.
     * 
     * - COMPLEXIDADE: O(N^3) - Classe de Complexidade Cúbica para cada dispositivo analisado.
     * - JUSTIFICATIVA: O algoritmo é completamente distinto das funções anteriores e utiliza
     *   três laços de repetição estritamente aninhados (t1, t2 e t3) que percorrem o vetor de
     *   tamanho N formando todas as combinações ternárias possíveis: N*(N-1)*(N-2)/6.
     *   Polinomialmente, o termo dominante é N^3, determinando a complexidade O(N^3).
     * - CONSEQUÊNCIAS PARA ENTRADAS MUITO GRANDES: A complexidade cúbica O(N^3) é altamente
     *   crítica para entradas grandes. Se a quantidade N de leituras dobrar, o tempo de
     *   execução multiplica por 8 (2^3). Se N aumentar 10 vezes, o tempo multiplica por 1.000!
     *   Para valores altos de N (ex: N = 50.000 segundos), o número de iterações chega na casa
     *   dos trilhões, inviabilizando a resposta em tempo hábil em um processador convencional.
     */
    @Override
    public NivelDesgaste analisarTriadesDegradacao(int[] leiturasCadencia, String idDispositivo, String identificadorCorredor) {
        int n = leiturasCadencia.length;
        long totalTriadesCriticas = 0;

        for (int t1 = 0; t1 < n - 2; t1++) {
            for (int t2 = t1 + 1; t2 < n - 1; t2++) {
                for (int t3 = t2 + 1; t3 < n; t3++) {
                    boolean altaCadenciaInicial = leiturasCadencia[t1] >= 180;
                    boolean quedaPorExaustao = (leiturasCadencia[t1] - leiturasCadencia[t2]) >= 30;
                    boolean sprintSobrecarga = (leiturasCadencia[t3] - leiturasCadencia[t2]) >= 35;

                    if (altaCadenciaInicial && quedaPorExaustao && sprintSobrecarga) {
                        totalTriadesCriticas++;
                    }
                }
            }
        }

        NivelDesgaste diagnostico = NivelDesgaste.ESTAVEL;
        if (totalTriadesCriticas > 500) {
            diagnostico = NivelDesgaste.RISCO_CRITICO_LESAO;
        } else if (totalTriadesCriticas > 0) {
            diagnostico = NivelDesgaste.ALERTA_MODERADO;
        }

        System.out.println(" -> Dispositivo " + idDispositivo + " | Tríades O(N^3) detectadas: "
                + totalTriadesCriticas + " | Diagnóstico: " + diagnostico);

        return diagnostico;
    }
}