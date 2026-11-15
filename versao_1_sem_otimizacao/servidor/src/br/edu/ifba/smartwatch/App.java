package br.edu.ifba.smartwatch;

import br.edu.ifba.smartwatch.impl.MonitoramentoCadenciaImpl;
import br.edu.ifba.smartwatch.monitor.NivelDesgaste;
import br.edu.ifba.smartwatch.monitor.OperacoesMonitoramento;
import br.edu.ifba.smartwatch.monitor.Smartwatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class App {

    private static final int QTD_DISPOSITIVOS = 10;
    
    // Armazena os Smartwatches que estão enviando dados pela rede
    private static final Map<Integer, Smartwatch> mapSmartwatches = new ConcurrentHashMap<>();
    private static int clientesFinalizados = 0;

    /**
     * Método principal (Critério 4.2.c) a partir do qual todas as funcionalidades
     * são executadas e testadas na arquitetura Cliente-Servidor (Versão 1).
     * 
     * COMPLEXIDADE GLOBAL: O(N^3), dominada assintoticamente pela funcionalidade (d.4).
     */
    public static void main(String[] args) {
        OperacoesMonitoramento monitor = new MonitoramentoCadenciaImpl();

        System.out.println("====================================================================");
        System.out.println("   SERVIDOR INICIADO: AGUARDANDO DADOS DOS SMARTWATCHES (PORTA 8080)");
        System.out.println("====================================================================");

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            // O servidor fica em loop infinito escutando conexões
            while (true) {
                Socket clienteSocket = serverSocket.accept();
                
                // Dispara uma Thread no servidor para ouvir cada relógio separadamente
                new Thread(() -> receberDados(clienteSocket, monitor)).start();
            }
        } catch (Exception e) {
            System.err.println("Erro crítico no servidor: " + e.getMessage());
        }
    }

    /**
     * Lê os dados brutos enviados pelo Cliente (SmartwatchThread) via protocolo TCP/IP.
     * Complexidade de leitura da rede: O(L) onde L é o número de leituras do smartwatch.
     */
    private static void receberDados(Socket socket, OperacoesMonitoramento monitor) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String entrada;
            int idRelogioAtual = -1;
            List<Integer> cadenciasTemporarias = new ArrayList<>();
            String nomeAtleta = "Corredor Desconhecido";

            // Loop de recepção linha por linha
            while ((entrada = in.readLine()) != null) {
                if (entrada.startsWith("FIM_")) {
                    idRelogioAtual = Integer.parseInt(entrada.split("_")[1]);
                    System.out.println("[REDE] Relógio ID " + idRelogioAtual + " finalizou o percurso.");
                    break;
                }

                // O cliente envia no formato CSV: "1,160,1" -> idRelogio, cadencia, minuto
                String[] dados = entrada.split(",");
                idRelogioAtual = Integer.parseInt(dados[0].trim());
                int cadencia = Integer.parseInt(dados[1].trim());
                nomeAtleta = "Corredor_" + idRelogioAtual; // Simula um nome baseado no ID

                cadenciasTemporarias.add(cadencia);
                System.out.println("   -> Recebendo: Relógio " + idRelogioAtual + " registrou " + cadencia + " SPM.");
            }

            // Após o relógio finalizar o envio, salva o objeto Smartwatch no mapa da memória do Servidor
            if (idRelogioAtual != -1 && !cadenciasTemporarias.isEmpty()) {
                // Converte ArrayList para array primitivo int[] para encaixar no seu modelo existente
                int[] arrayLeituras = cadenciasTemporarias.stream().mapToInt(i -> i).toArray();
                
                Smartwatch sw = new Smartwatch(idRelogioAtual, nomeAtleta, arrayLeituras);
                mapSmartwatches.put(idRelogioAtual, sw);
                
                registrarFinalizacaoEProcessar(monitor);
            }

        } catch (Exception e) {
            System.err.println("Erro na comunicação com o relógio: " + e.getMessage());
        }
    }

    /**
     * Quando os 10 dispositivos finalizarem o envio via rede, o Servidor executa os 
     * processamentos pesados da Avaliação 1.
     */
    private static synchronized void registrarFinalizacaoEProcessar(OperacoesMonitoramento monitor) {
        clientesFinalizados++;
        
        // Só executa o processamento quando todos os relógios (Threads) terminarem de enviar
        if (clientesFinalizados == QTD_DISPOSITIVOS) {
            System.out.println("\n[SISTEMA] Todos os " + QTD_DISPOSITIVOS + " Smartwatches enviaram os dados com sucesso.");
            
            // Converte o Map de memória em array para os métodos da sua interface
            Smartwatch[] smartwatches = mapSmartwatches.values().toArray(new Smartwatch[0]);

            System.out.println("\n--- PROCESSAMENTO DOS DADOS (Carga no Servidor - Versão 1) ---");

            // (d.1)
            monitor.imprimirDispositivos(smartwatches);

            // (d.2)
            monitor.imprimirLeituras(smartwatches);

            // (d.4) O GARGALO DE COMPLEXIDADE: O(N^3) mantido no Servidor propositalmente
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

            // (d.3) - Ordenação O(N^2)
            monitor.ordenarLeiturasCrescente(smartwatches);
            
            System.out.println(">>> EXIBIÇÃO DAS LEITURAS APÓS A ORDENAÇÃO CRESCENTE (d.3):");
            monitor.imprimirLeituras(smartwatches);
            
            // Reseta a contagem caso decida rodar outra simulação com o servidor aberto
            clientesFinalizados = 0;
            mapSmartwatches.clear();
            System.out.println("\nServidor pronto para uma nova rodada de monitoramentos (Porta 8080)...");
        }
    }
}