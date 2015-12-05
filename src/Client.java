import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by alexd on 03/12/2015.
 */


public class Client {

    public static final int PORT = 5522;                                                            //port number
    public static final String HOST = "localhost";                                                  //host name

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        try {
            //setting up client
            Socket s = null;

            s = new Socket(HOST, PORT);                                                             //initialization of client-side socket
            System.out.println("Cient socket OK");                                                  //troubleshooting message (marked with *)

//            InputStream in = s.getInputStream();                                                  //for testing purposes
//            System.out.println("got input stream");                                               //

//            BufferedInputStream in = new BufferedInputStream(s.getInputStream());                 //for testing purposes
//            System.out.println("created buffered input stream");                                  //

            DataInputStream in = new DataInputStream(s.getInputStream());                           //initialization of a data input stream on socket
            System.out.println("created data input stream");                                        //*

//            OutputStream out = s.getOutputStream();                                               //for testing purposes
//            System.out.println("got output stream");                                              //

//            BufferedOutputStream out = new BufferedOutputStream(s.getOutputStream());             //for testing purposes
//            System.out.println("created buffered output stream");                                 //

            DataOutputStream out = new DataOutputStream(s.getOutputStream());                       //initialization of data output stream on socket
            System.out.println("created data output stream");                                       //*
            //client has been set up

//            //test 1                                                                              //for testing purposes
//            int test = 4;                                                                         //
//            byte[] b = ByteBuffer.allocate(4).putInt(test).array();                               //
//            System.out.println(b);                                                                //
//            out.write(b);                                                                         //
//            out.flush();                                                                          //for testing purposes

            Mat mc = Protocol.readFile("F:\\image.png");                                            //read image file from disk into Mat
            System.out.println("Type: " + mc.type());                                               //*
            System.out.println("read file into Mat");                                               //*
//            Imgcodecs.imwrite("client_read.png", mc);                                               //troubleshooting operation

            int w = mc.width();                                                                     //save width of the Mat
            System.out.println("got width: " + w);                                                  //*

            int h = mc.height();                                                                    //save height of the Mat
            System.out.println("got height: " + h);                                                 //*

            byte[] bc = Protocol.matToByte(mc);                                                     //convert the Mat into byte[]
            System.out.println("Converted mat into byte[]" + bc.length);                            //*

//            Mat testm = Protocol.byteToMat(mc.width(),mc.height(), bc);                           //troubleshooting operations
//            System.out.println("mat size " + testm.size());                                       //
//            Imgcodecs.imwrite("testclient.jpg", testm);                                           //troubleshooting operations

            out.writeInt(bc.length);                                                                //send byte[] length to server
            System.out.println("sent length");                                                      //*
            out.flush();

            out.writeInt(w); System.out.println("Sent width");                                      //send image width to server
            out.flush();
            out.writeInt(h); System.out.println("Sent height");                                     //send image height to server
            out.flush();


            out.write(bc);                                                                          //send the byte[] to Server
            System.out.println("wrote to socket");                                                  //*
            out.flush();
            System.out.println("flushed socket");                                                   //*

            //at this time, server-side operations are happening (see Server)

            byte[] bcp = new byte[bc.length];                                                       //receive byte[] of processed image from Server
            in.readFully(bcp);                                                                      //
            System.out.println("read from socket");                                                 //*

            Mat mcp = Protocol.byteToMat(w, h, bcp);                                                //convert the received byte[] to Mat
            //Imgcodecs.imwrite("client_received_proc.png", mcp);                                     //*
            System.out.println("Converted byte[] into mat");                                        //*
            Protocol.writeFile(mcp, "F:\\nucred.png");                                              //write the Mat to file
            System.out.println("wrote Mat to file");                                                //*



        }catch (IOException e){
            System.out.println("Problem setting up client");                                        //*
            System.err.println(e.getMessage());                                                     //*
            System.err.println(e.getCause());                                                       //*
        }


    } //main ends



} //class ends
