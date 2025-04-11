/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package iftm.clientechatgui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author augus
 */
public class ChatView extends javax.swing.JFrame implements Runnable{
    private final Socket conexao;
    private static PrintStream saida;
    private static BufferedReader teclado;
    private final ChatView chatView;
    private static SecretKey chave;
//    private static GCMParameterSpec iv;
    
    public ChatView(){
        initComponents();
        this.conexao = null;
        this.chatView = null;
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                saida.println("");
                System.exit(0);
            }
        });
    }
    
    public ChatView(Socket con, ChatView chatView){
        this.conexao = con;
        this.chatView = chatView;
    }
    
    public void geraChave(String palavraChave) throws NoSuchAlgorithmException, InvalidKeySpecException{
        Random secRandom = new SecureRandom(palavraChave.getBytes());

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(palavraChave.toCharArray(), palavraChave.getBytes(), 65536, 256);
        chave = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        
//        byte[] initVector = new byte[12];
//        secRandom.nextBytes(initVector);
//        iv = new GCMParameterSpec(128, initVector);
    }
    
    public String encriptaMsg(String msg) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, chave);
            byte[] msgEncriptada = cipher.doFinal(msg.getBytes());
            
            return Base64.getEncoder().encodeToString(msgEncriptada);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) { 
            Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String decriptaMsg(String msgCifrada) {
        try{
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, chave);
            byte[] msgOriginal = cipher.doFinal(Base64.getDecoder().decode(msgCifrada));

            return new String(msgOriginal);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException ex) {
            Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex){
            return msgCifrada + " (MENSAGEM CRIPTOGRAFADA)";
        }
        return null;
    }
    
    public void iniciaChat(String nome, String palavraChave) {
        try{
            geraChave(palavraChave);
            System.out.println("Chave: " + new String(Base64.getEncoder().encode(chave.getEncoded())));

            Socket con = new Socket("127.0.0.1", 2222);

            saida = new PrintStream(con.getOutputStream());
            saida.println(nome);
//            saida.println(new String(Base64.getEncoder().encode(chave.getEncoded())));
            
            Thread t = new Thread(new ChatView(con, this));

            t.start();
        }catch(IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.printStackTrace();
        }
    }
    
    public void run(){
        try{
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            String linhaRecebida;

            while(true){
                linhaRecebida = entrada.readLine();
                if(linhaRecebida == null){
                    System.out.println("Conexão encerrada!");
                    break;
                }
                System.out.println("Recebendo: " + linhaRecebida);
                String[] partesMsg = linhaRecebida.split(": ");
                
                if(partesMsg.length > 1){
                    String msgRecebida = partesMsg[1];
                    String msgOriginal = decriptaMsg(msgRecebida);
                    this.chatView.atualizaChat(partesMsg[0] + ": " + msgOriginal);
                }else{
                    this.chatView.atualizaChat(partesMsg[0]);                 
                }
            }

            this.conexao.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    private void atualizaChat(String msg) {
        String texto = this.chatBox.getText();
        this.chatBox.setText(texto + msg + "\n");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        chatBox = new javax.swing.JTextArea();
        msgBox = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnEnviar = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        chatBox.setBackground(new java.awt.Color(0, 0, 0));
        chatBox.setColumns(20);
        chatBox.setForeground(new java.awt.Color(255, 255, 255));
        chatBox.setRows(5);
        chatBox.setFocusable(false);
        jScrollPane1.setViewportView(chatBox);

        msgBox.setForeground(new java.awt.Color(102, 102, 102));
        msgBox.setText("Mensagem");
        msgBox.setToolTipText("Mensagem");
        msgBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                msgBoxFocusGained(evt);
            }
        });
        msgBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                msgBoxKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Chat");

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        btnVoltar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnVoltar.setText("<");
        btnVoltar.setFocusable(false);
        btnVoltar.setMargin(new java.awt.Insets(0, 0, 5, 0));
        btnVoltar.setRequestFocusEnabled(false);
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnVoltar)
                        .addGap(137, 137, 137)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(msgBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEnviar)
                        .addGap(6, 6, 6)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(btnVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEnviar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(msgBox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        String msg = this.msgBox.getText();
        String msgEncriptada = encriptaMsg(msg);
        System.out.println("Enviando: " + msgEncriptada);
        
        saida.println(msgEncriptada);
        this.msgBox.setText("");
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void msgBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_msgBoxFocusGained
        if (msgBox.getText().equals("Mensagem")) {
            msgBox.setText("");
            msgBox.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_msgBoxFocusGained

    private void msgBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_msgBoxKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.btnEnviar.doClick();
        }
    }//GEN-LAST:event_msgBoxKeyPressed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        saida.println("");
        this.dispose();
        new ClienteChatGUI().setVisible(true);
    }//GEN-LAST:event_btnVoltarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JTextArea chatBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField msgBox;
    // End of variables declaration//GEN-END:variables
}
