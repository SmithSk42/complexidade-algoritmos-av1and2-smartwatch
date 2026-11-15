package br.edu.ifba.smartwatch.monitor;

/**
 * Representação de cada coisa monitorada no mini-mundo (Critério 4.2.b):
 * Smartwatch acoplado a um corredor de longa distância com seu identificador
 * e o vetor de leituras de cadência (Passos por Minuto - SPM).
 */

public class Smartwatch {
    private String idDispositivo;
    private String identificadorCorredor;
    private int[] leiturasCadencia;

/**
     * Construtor que inicializa o dispositivo monitorado e aloca o vetor de leituras.
     * 
     * - COMPLEXIDADE: O(N), onde N é a quantidade de leituras (quantidadeLeiturasN).
     * - JUSTIFICATIVA: As atribuições de texto são O(1), porém a instrução
     *   new int[quantidadeLeiturasN] na JVM aloca um bloco contíguo de memória e
     *   inicializa todas as N posições com o valor padrão zero (0), exigindo N passos.
     * - CONSEQUÊNCIAS PARA ENTRADAS MUITO GRANDES: Se N for excessivamente grande,
     *   a alocação contígua de memória pode falhar lançando OutOfMemoryError.
     */

    public Smartwatch(String idDispositivo, String identificadorCorredor, int quantidadeLeiturasN) {
        this.idDispositivo = idDispositivo;
        this.identificadorCorredor = identificadorCorredor;
        this.leiturasCadencia = new int[quantidadeLeiturasN];
    }

    /**
     * Retorna o identificador único do Smartwatch.
     * - COMPLEXIDADE: O(1) - Constante.
     * - JUSTIFICATIVA: Realiza apenas o retorno direto de uma referência em memória,
     *   sem laços ou desvios condicionais.
     * - CONSEQUÊNCIAS PARA ENTRADAS MUITO GRANDES: Nenhuma. O tempo de resposta é
     *   instantâneo independentemente do tamanho de N.
     */
    public String getIdDispositivo() {
        return idDispositivo;
    }

    /**
     * Retorna a identificação do corredor que utiliza o Smartwatch.
     * - COMPLEXIDADE: O(1) - Constante.
     * - JUSTIFICATIVA: Acesso direto ao atributo de instância da classe em apenas
     *   uma instrução primitiva.
     * - CONSEQUÊNCIAS PARA ENTRADAS MUITO GRANDES: Nenhuma. Não sofre qualquer
     *   impacto com o aumento do número de leituras N.
     */
    public String getIdentificadorCorredor() {
        return identificadorCorredor;
    }

    /**
     * Retorna a referência para o vetor de leituras de cadência do sensor.
     * - COMPLEXIDADE: O(1) - Constante.
     * - JUSTIFICATIVA: Devolve apenas o endereço de memória (referência) do array já
     *   alocado, sem copiar nem percorrer os N elementos.
     * - CONSEQUÊNCIAS PARA ENTRADAS MUITO GRANDES: Nenhuma para o método em si, pois
     *   o tempo de retorno da referência permanece constante para qualquer N.
     */
    public int[] getLeiturasCadencia() {
        return leiturasCadencia;
    }

    public Smartwatch(int idDispositivo, String identificadorCorredor, int[] leiturasCadencia) {
        this.idDispositivo = String.valueOf(idDispositivo);
        this.identificadorCorredor = identificadorCorredor;
        this.leiturasCadencia = leiturasCadencia;
    }
}