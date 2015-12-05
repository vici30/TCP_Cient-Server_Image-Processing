import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.opencv.core.CvType.CV_8S;
import static org.opencv.core.CvType.CV_8SC3;
import static org.opencv.core.CvType.CV_8UC3;

/**
 * Created by alexd on 03/12/2015.
 */
public class Protocol {

    public static Mat readFile(String dir)                                      //read from file into Mat method
    {
        Mat m = new Mat();
        m = Imgcodecs.imread(dir);
        return m;
    }//method ends

    public static void writeFile(Mat m, String dir)                             //write Mat to new file method
    {
        Imgcodecs.imwrite(dir, m);
    }//method ends


    public static byte[] matToByte(Mat m)                                       //convert Mat to byte[] method
    {
        byte[] buffer = new byte[(int)m.width()*(int)m.height()*m.channels()];
        m.get(0, 0, buffer);
        return buffer;
    }//method ends

    public static Mat byteToMat (int width, int height, byte[] b)               //convert byte[] to Mat
    {
        Mat m = new Mat(height, width, CvType.CV_8UC3);
        m.put(0, 0, b);
        return m;
    }//method ends

//    public static byte[] matToByte(Mat m)                                     //convert Mat to bye[] manual method
//    {
//        byte[] buffer = new byte[(int)m.width()*(int)m.height()*m.channels()];
//        for (int i =0; i < m.height(); i++){
//            for(int j = 0; j<m.width();j++){
//                for(int k = 0; k < m.channels(); k++){
//                    buffer[i*m.width()*m.channels()+j*m.channels()+k] = (byte)m.get(i, j)[k];
//                }
//            }
//        }
//        return buffer;
//    }//method ends
//
//    public static Mat byteToMat (int width, int height, byte[] b)             //convert byte[] to Mat manual method
//    {
//        Mat m = new Mat(height, width, 16);
//
//        for (int i =0; i < height; i++){
//            for(int j = 0; j< width;j++){
//                byte[] value = new byte[3];
//                for(int k = 0; k < 3; k++){
//                    value[k] = (byte)(b[i*width*3+j*3+k]);
//                }
//                m.put(i,j,value);
//            }
//        }
//        return m;
//    }//method ends

    public static Mat processImage(Mat m)                                   //image processing method
    {
        int rows = m.rows();                                                // returns number of rows
        int cols = m.cols();                                                // returns number of columns
        //int ch   = m.channels();                                            //returns number of channels (grayscale=1, RGB=3 etc.)
        Mat mout = new Mat(m.size(), CvType.CV_8UC1);                       //creates single channel Mat for binary image

        for (int i=0; i<rows; i++)
        {
            for (int j=0; j<cols; j++)
            {
                double[] data = m.get(i, j);                                //Stores current element in an array
                if(data[0] > 128 && data[1]>128 &&data[2]>128){             //thresholds values for the three channels of the image
                    mout.put(i,j, 255);                                     //puts threshold elements into binary image
                }else mout.put(i,j, 0);                                     //

//                for (int k = 0; k < ch; k++)        //Runs for the available number of channels
//                {
//
////                    data[k] = data[k] * 2;          //Pixel modification done here
//
//                }
//                m.put(i, j, data);              //Puts element back into matrix
            }
        }
        Mat output = new Mat(mout.size(), CvType.CV_8UC3);                  //
        Imgproc.cvtColor(mout, output, Imgproc.COLOR_GRAY2BGR, 3);          //converts single channel binary image into 3 channel
                                                                            // (this is done to prevent the necessity of a different transfer protocol
        return output;                                                      // for single channel images)
    }//method ends



}//class ends
