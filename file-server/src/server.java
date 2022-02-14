import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class server {

    static ArrayList<myFile>myFiles = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        int fileID = 0;

        JFrame jframe = new JFrame("server side");
        jframe.setSize(400,450);
        jframe.setLayout(new BoxLayout(jframe.getContentPane(),BoxLayout.Y_AXIS));
        jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);

        JPanel jpanel = new JPanel();
        jpanel.setLayout(new BoxLayout(jpanel,BoxLayout.Y_AXIS));

        JScrollPane jscroll = new JScrollPane(jpanel);
        jscroll.setVerticalScrollBarPolicy(jscroll.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel jtitle = new JLabel("File receiver");
        jtitle.setFont(new Font("Helvetica", Font.BOLD,25));
        jtitle.setBorder(new EmptyBorder(20,0,10,0));
        jtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        jframe.add(jtitle);
        jframe.add(jscroll);
        jframe.setVisible(true);

        //for accepting connections form client
        ServerSocket serverSocket = new ServerSocket(8080);

        while(true){
            try{
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                int filenamelength = dataInputStream.readInt();

                if(filenamelength > 0){
                    byte []filenamebyte = new byte[filenamelength];
                    dataInputStream.readFully(filenamebyte,0,filenamelength);
                    String filename = new String(filenamebyte);

                    int filecontentlength = dataInputStream.readInt();

                    if(filecontentlength>0){
                        byte []filecontentbyte = new byte[filecontentlength];
                         dataInputStream.readFully(filecontentbyte,0,filecontentlength);

                         JPanel jfilerow = new JPanel();
                         jfilerow.setLayout(new BoxLayout(jfilerow,BoxLayout.Y_AXIS));

                         JLabel jfilename = new JLabel(filename);
                         jfilename.setFont(new Font("Helvetica",Font.BOLD,20));
                         jfilename.setBorder(new EmptyBorder(10,0,10,0));

                         if(getFileExt(filename).equalsIgnoreCase("txt")){

                             jfilerow.setName(String.valueOf(fileID));

                             jfilerow.addMouseListener(getMouseListener());

                             jfilerow.add(jfilename);
                             jpanel.add(jfilerow);
                             jframe.validate();

                         }else{
                             jfilerow.setName(String.valueOf(fileID));
                             jfilename.addMouseListener(getMouseListener());

                             jfilerow.add(jfilename);
                             jpanel.add(jfilerow);

                             jframe.validate();
                         }
                        myFiles.add(new myFile(fileID,filename,filecontentbyte,getFileExt(filename)));

                    }

                }

            }catch(IOException error){
                error.printStackTrace();
            }
        }
    }

    public static String getFileExt(String fileName){
        int i = fileName.lastIndexOf('.');

        if(i>0){
            return fileName.substring(i+1);
        }else{
            return "NO EXTENSION FOUND";
        }
    }

    public static MouseListener getMouseListener(){

        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                JPanel jpanel = (JPanel) e.getSource();

                int fileID = Integer.parseInt(jpanel.getName());

                for(myFile myfile : myFiles){
                    if(myfile.getId()==fileID){
                        JFrame jpreview = createFrame(myfile.getName(),myfile.getData(),myfile.getFileExt());
                        jpreview.setVisible(true);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }

    public static JFrame createFrame(String filename, byte[] filedata, String fileExt){

        JFrame jframe = new JFrame("file downloader");
        jframe.setSize(400,450);

        JPanel jpanel =  new JPanel();
        jpanel.setLayout(new BoxLayout(jpanel,BoxLayout.Y_AXIS));

        JLabel jtitle = new JLabel("file downloader");
        jtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        jtitle.setFont(new Font("Helvetica",Font.BOLD,20));
        jtitle.setBorder(new EmptyBorder(20,0,10,0));

        JLabel jprompt = new JLabel("are you sure you want to download the selected file");
        jprompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        jprompt.setFont(new Font("Helvetica",Font.BOLD,20));
        jprompt.setBorder(new EmptyBorder(20,0,10,0));

        JButton jYes = new JButton("YES");
        jYes.setPreferredSize(new Dimension(150,75));
        jYes.setFont(new Font("Helvetica",Font.BOLD,20));

        JButton jNo = new JButton("NO");
        jNo.setPreferredSize(new Dimension(150,75));
        jNo.setFont(new Font("Helvetica",Font.BOLD,20));

        JLabel jfilecontent = new JLabel();
        jfilecontent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButton = new JPanel();
        jpButton.setBorder(new EmptyBorder(20,0,10,0));
        jpButton.add(jYes);
        jpButton.add(jNo);

        if(fileExt.equalsIgnoreCase("txt")){
            jfilecontent.setText("<html>"+ new String(filedata)+ "</html>");
        }else{
            jfilecontent.setIcon(new ImageIcon(filedata));
        }

        jYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File filedownload = new File(filename);
                try{
                    FileOutputStream fileOutputStream = new FileOutputStream(filedownload);

                    fileOutputStream.write(filedata);
                    fileOutputStream.close();


                }catch(IOException error){
                    error.printStackTrace();
                }
            }
        });

        jNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jframe.dispose();
            }
        });

        jpanel.add(jtitle);
        jpanel.add(jprompt);
        jpanel.add(jfilecontent);
        jpanel.add(jpButton);

        jframe.add(jpanel);

        return jframe;
    }

}
