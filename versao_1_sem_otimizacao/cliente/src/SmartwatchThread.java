import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class SmartwatchThread implements Runnable {
    private int idRelogio;
    private List<String> dadosParaEnviar;
    
    // Configurações da rede (localhost e porta 8080 que definimos no Servidor)
    private static final String IP_SERVIDOR = "127.0.0.1";
    private static final int PORTA_SERVIDOR = 8080;

    public SmartwatchThread(int idRelogio, List<String> dadosParaEnviar) {
        this.idRelogio = idRelogio;
        this.dadosParaEnviar = dadosParaEnviar;
    }

    @Override
    // Complexidade O(L) onde L é a quantidade de linhas de dados lidas deste relógio
    public void run() {
        try (Socket socket = new Socket(IP_SERVIDOR, PORTA_SERVIDOR);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            
            System.out.println("Relógio " + idRelogio + " conectado ao servidor.");

            for (String dado : dadosParaEnviar) {
                // Envia a linha bruta do CSV (ex: "1,165,1") para o Servidor
                out.println(dado);
                
                // Simula o tempo passando na vida real (100 milissegundos de delay entre envios)
                Thread.sleep(100); 
            }
            
            // Flag que avisa ao servidor que este relógio concluiu o percurso
            out.println("FIM_" + idRelogio); 
            
        } catch (Exception e) {
            System.err.println("Erro no Relógio " + idRelogio + " (Servidor está ligado?): " + e.getMessage());
        }
    }
}