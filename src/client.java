import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;


public class client {

    public static void main(String[] args) {

        final File []fileToSend = new File[1];

        JFrame jframe = new JFrame("My Client");
        jframe.setSize(400, 450);

        jframe.setLayout(new BoxLayout(jframe.getContentPane(), BoxLayout.Y_AXIS));
        jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);

        JLabel jlabel = new JLabel("file - file");
        jlabel.setFont(new Font("Helvetica", Font.BOLD, 25));
        jlabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel jlfilename = new JLabel("Choose a file");
        jlfilename.setFont(new Font("Helvetica", Font.BOLD, 20));
        jlfilename.setBorder(new EmptyBorder(50, 0, 0, 0));
        jlfilename.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jbutton = new JPanel();
        jbutton.setBorder(new EmptyBorder(75, 0, 10, 0));

        JButton jsend = new JButton("send file");
        jsend.setPreferredSize(new Dimension(150, 25));
        jsend.setFont(new Font("Helvetica", Font.BOLD, 20));

        JButton jchoose = new JButton("Choose file");
        jchoose.setPreferredSize(new Dimension(150, 25));
        jchoose.setFont(new Font("Helvetica", Font.BOLD, 20));

        jbutton.add(jsend);
        jbutton.add(jchoose);

        //events
        jchoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfchoose = new JFileChooser();
                jfchoose.setDialogTitle("Choose file to send");

                if(jfchoose.showOpenDialog(null)==jfchoose.APPROVE_OPTION){
                    fileToSend[0] = jfchoose.getSelectedFile();
                    jlfilename.setText("The file you want to send: "+fileToSend[0].getName());

                }
            }
        });

        jsend.addActionListener(new ActionListener() {
            @Override
            public  void actionPerformed(ActionEvent e) {
                if(fileToSend[0]==null){
                    jlfilename.setText("Please choose a file");
                }else{
                    try {
                        FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                        //connecting with the server
                        Socket socket = new Socket("localhost", 1234);

                        //output client->server
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                        String filename = fileToSend[0].getName();
                        byte[] filenamebyte = filename.getBytes();

                        byte[] filecontentbyte = new byte[(int) fileToSend[0].length()];
                        fileInputStream.read(filecontentbyte);

                        dataOutputStream.writeInt(filenamebyte.length);
                        dataOutputStream.write(filenamebyte);

                        dataOutputStream.writeInt(filecontentbyte.length);
                        dataOutputStream.write(filecontentbyte);
                    }catch (IOException error){
                        error.printStackTrace();
                    }
                }

            }
        });

        jframe.add(jlabel);
        jframe.add(jlfilename);
        jframe.add(jbutton);
        jframe.setVisible(true);
    }
}
