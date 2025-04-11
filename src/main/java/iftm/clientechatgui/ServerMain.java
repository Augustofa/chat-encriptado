package iftm.clientechatgui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ServerMain extends Thread{
    private static Vector clientes;
    private final Socket conexao;
    private String meuNome;

    public ServerMain(Socket con){
        this.conexao = con;
    }

    public static void main(String[] args) {
        clientes = new Vector();

        try{
            ServerSocket ss = new ServerSocket(2222);

            while(true){
                System.out.println("Aguardando uma conexão...");
                Socket con = ss.accept();
                System.out.println("Conexão realizada");

                Thread t = new ServerMain(con);
                t.start();

            }
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public void run(){
        try{
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            PrintStream saida = new PrintStream(this.conexao.getOutputStream());

            this.meuNome = entrada.readLine();
            
            if(this.meuNome == null){
                return;
            }

            clientes.add(saida);
            
            enviarParaTodos(saida, " entrou", " no chat");

            String linha = entrada.readLine();

            while(linha != null && !(linha.trim().isEmpty())){
                enviarParaTodos(saida, ": ", linha);
                linha = entrada.readLine();
            }
            enviarParaTodos(saida, " saiu", " do chat");

            this.conexao.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public void enviarParaTodos(PrintStream saida, String acao, String linha){
        Enumeration e = clientes.elements();

        while(e.hasMoreElements()){
            PrintStream chat = (PrintStream) e.nextElement();
            if(chat != saida){
                chat.println(this.meuNome + acao + linha);
            }else{
                chat.println("Você" + acao + linha);
            }
        }
    }
}
