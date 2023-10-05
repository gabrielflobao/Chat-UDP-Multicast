import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class BatePapo extends Thread {
    private static String usuario = null;
    private static InetAddress endereco;
    private static int porta;

    @Override
    public void run() {

        try{
        byte [] msg = new byte[128];
        MulticastSocket socket = new MulticastSocket(porta);
        socket.joinGroup(endereco);
        while(true){
            DatagramPacket dgpacket = new DatagramPacket(msg,msg.length);
            socket.receive(dgpacket);
            String mensagem = new String(dgpacket.getData());
            if(!mensagem.contains(usuario)){
                System.out.println("\n"+mensagem +"\n");
                System.out.print("Digite a mensagem: ");
            }
            msg = new byte[128];
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if(args.length!=2) {
            System.out.println("Parâmetros estão incorretos!\n java BatePapo");
            System.exit(1);
        }
        try{
            porta = Integer.parseInt(args[1]);
            endereco = InetAddress.getByName(args[0]);
            BatePapo bp = new BatePapo();
            bp.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Digite seu nome : ");
            usuario = br.readLine();
            MulticastSocket socket = new MulticastSocket();
            socket.joinGroup(endereco);
            byte [] msg = new byte[128];
            while (true) {
                System.out.print("Digite a mensagem: ");
                String mensagem = br.readLine();
                if(mensagem.equals("sair")) {
                    System.exit(0);
                }
                mensagem = usuario +" diz: "+mensagem;
                msg = mensagem.getBytes();
                DatagramPacket dgpacket = new DatagramPacket(msg,msg.length,endereco,porta);
               socket.send(dgpacket);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}