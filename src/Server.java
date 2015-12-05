import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by alexd on 03/12/2015.
 */
public class Server {

    public static final int PORT = 5522;                                                            //port number

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        try {
            //setting up Server
            ServerSocket ss = null;                                                                 //initialization of server-side socket
            ss = new ServerSocket(PORT);                                                            //
            System.out.println("Server socket listening to port " + PORT);                          //troubleshooting message (marked with *)


            Socket cs = null;                                                                       //connect with client socket
            cs = ss.accept();                                                                       //
            System.out.println("Connection accepted through port " + PORT);                         //*


//            InputStream in = null;                                                                //for testing purposes
//            in = cs.getInputStream();                                                             //

//            BufferedInputStream in = new BufferedInputStream(cs.getInputStream());                //for testing purposes

            DataInputStream in = new DataInputStream(cs.getInputStream());                          //initialize dta input stream on socket

//            OutputStream out = null;                                                              //for testing purposes
//            out = cs.getOutputStream();                                                           //

//            BufferedOutputStream out = new BufferedOutputStream(cs.getOutputStream());            //for testing purposes

            DataOutputStream out = new DataOutputStream(cs.getOutputStream());                      //initialize data ouput stream on socket

            //server has been set up


//            //test 1                                                                              //for testing purposes
//            byte[] b = new byte[4];                                                               //
//            int t = ByteBuffer.wrap(b).asIntBuffer().get();                                       //
//            System.out.println(t);                                                                //for testing purposes

            int len = in.readInt();                                                                 //receive byte[] length from Clent
            System.out.println("Data length: " +len);                                               //*

            int w = in.readInt(); System.out.println("got width: " + w);                            //receive image width from Client
            int h = in.readInt(); System.out.println("got height: " + h);                           //receive image height from Client

            byte[] bs = new byte[len];                                                              //
            in.readFully(bs);                                                                       //receive byte[]

            Mat ms = Protocol.byteToMat(w, h, bs);                                                  //convert byte[] to Mat
            //Imgcodecs.imwrite("server_received.png", ms);                                           //troubleshooting operation

            Mat msp = Protocol.processImage(ms);                                                    //do the image processing
            //Imgcodecs.imwrite("server_processed.png", msp);                                         //troubleshooting operation

            byte[] bsp = Protocol.matToByte(msp);                                                   //convert processed Mat back to byte[]
            out.write(bsp);                                                                         //send byte[] of processed image
            out.flush();

            out.close();
            in.close();
            ss.close();
            cs.close();

        }catch (IOException e){
            System.out.println("Problem setting up Server: ");
            System.err.println(e.getMessage());
        }


    }//main ends


}//class ends
