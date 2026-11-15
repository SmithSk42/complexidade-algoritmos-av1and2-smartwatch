import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClienteApp {
    // Complexidade O(N) para leitura do CSV
    public static void main(String[] args) {
        // Caminho do CSV considerando que você roda o projeto pela raiz do VS Code
        String caminhoCSV = "dados/cadencia_sensores.csv"; 
        Map<Integer, List<String>> dadosPorRelogio = lerDadosDoCSV(caminhoCSV);

        if (dadosPorRelogio.isEmpty()) {
            System.err.println("Nenhum dado encontrado! Verifique se o caminho do CSV está correto.");
            return;
        }

        System.out.println("Iniciando simulação dos Smartwatches...");

        // Complexidade O(T) onde T é o número de relógios (Threads)
        for (Map.Entry<Integer, List<String>> entry : dadosPorRelogio.entrySet()) {
            int idRelogio = entry.getKey();
            List<String> dados = entry.getValue();

            // Dispara uma Thread para cada Smartwatch simulado
            Thread relogioThread = new Thread(new SmartwatchThread(idRelogio, dados));
            relogioThread.start();
        }
    }

    private static Map<Integer, List<String>> lerDadosDoCSV(String caminho) {
        Map<Integer, List<String>> mapaDados = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            br.readLine(); // Pula o cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] colunas = linha.split(",");
                if(colunas.length < 3) continue;
                
                int idRelogio = Integer.parseInt(colunas[0].trim());
                mapaDados.putIfAbsent(idRelogio, new ArrayList<>());
                mapaDados.get(idRelogio).add(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o CSV: " + e.getMessage());
            System.err.println("DICA: Se der erro de caminho, tente usar caminho absoluto ou verificar a pasta raiz.");
        }
        return mapaDados;
    }
}