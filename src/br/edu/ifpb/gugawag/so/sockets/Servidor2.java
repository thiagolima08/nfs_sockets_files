package br.edu.ifpb.gugawag.so.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Servidor2 {

    public static void main(String[] args) throws IOException {
        System.out.println("== Servidor ==");

        // Configurando o socket
        ServerSocket serverSocket = new ServerSocket(7001);
        Socket socket = serverSocket.accept();

        // pegando uma referência do canal de saída do socket. Ao escrever nesse canal, está se enviando dados para o
        // servidor
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        // pegando uma referência do canal de entrada do socket. Ao ler deste canal, está se recebendo os dados
        // enviados pelo servidor
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        // laço infinito do servidor
        while (true) {
            System.out.println("Cliente: " + socket.getInetAddress());

            String mensagem = dis.readUTF();

            File raiz = new File("/Users/thiagodasilvalima/Downloads/SistemasDistribuidos");
            String[] files = raiz.list();
            List<String> listaArquivos = Collections.singletonList(Arrays.toString(files));

            if(mensagem.equals("readdir")) {
                dos.writeUTF(String.valueOf(listaArquivos));
            }
            else if(mensagem.contains("rename")){
                String[] msg = mensagem.split(" ");
                File f = new File("/Users/thiagodasilvalima/Downloads/SistemasDistribuidos/"+msg[1]);
                File f1 = new File("/Users/thiagodasilvalima/Downloads/SistemasDistribuidos/"+msg[2]);
                f.renameTo(f1);
                dos.writeUTF("Arquivo renomeado.");
            }
            else if(mensagem.contains("remove")){
                String[] msg = mensagem.split(" ");
                File f = new File("/Users/thiagodasilvalima/Downloads/SistemasDistribuidos/"+msg[1]);
                f.delete();
                dos.writeUTF("Arquivo excluído.");
            }
            else if(mensagem.contains("create")){
                String[] msg = mensagem.split(" ");
                File f = new File("/Users/thiagodasilvalima/Downloads/SistemasDistribuidos/"+msg[1]);
                f.createNewFile();
                dos.writeUTF("Arquivo criado.");
            }
            else{
                dos.writeUTF("Comando não encontrado, tente novamente.");
            }
        }
        /*
         * Observe o while acima. Perceba que primeiro se lê a mensagem vinda do cliente (linha 29, depois se escreve
         * (linha 32) no canal de saída do socket. Isso ocorre da forma inversa do que ocorre no while do Cliente2,
         * pois, de outra forma, daria deadlock (se ambos quiserem ler da entrada ao mesmo tempo, por exemplo,
         * ninguém evoluiria, já que todos estariam aguardando.
         */
    }
}
