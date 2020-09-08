package opencv.plakatanıma;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/*
 * @author erensayar
 */
public class OpenCVPlakaTanıma {

    private static Random rng = new Random(12345);

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //Kaynak resmimiz
        Mat araba = Imgcodecs.imread("D:\\SANDISK\\CODES\\Java\\OpenCV-PlakaTanıma\\Images\\Araba1.jpg");

        //Rgb’den gri seviyeli görüntüye dönüştürme işlemi
        Mat arabaGray = new Mat();
        Imgproc.cvtColor(araba, arabaGray, Imgproc.COLOR_RGB2GRAY);
        Imgcodecs.imwrite("D:\\SANDISK\\CODES\\Java\\OpenCV-PlakaTanıma\\Images\\1-Araba1_Gray.jpg", arabaGray);

        //Gri seviyeli resim üzerinde gürültü temizleme
        Mat arabaBilateral = new Mat();
        Imgproc.bilateralFilter(arabaGray, arabaBilateral, 10, 50, 0);
        Imgcodecs.imwrite("D:\\SANDISK\\CODES\\Java\\OpenCV-PlakaTanıma\\Images\\2-Araba1_bilateralFiltered.jpg", arabaBilateral);

        //Histogram eşitleme
        Mat arabaHistogram = new Mat();
        Imgproc.equalizeHist(arabaBilateral, arabaHistogram);
        Imgcodecs.imwrite("D:\\SANDISK\\CODES\\Java\\OpenCV-PlakaTanıma\\Images\\3-Araba1_Histogram.jpg", arabaHistogram);

        //Erosion & Dilatation - Aşındırma & Açma
        Mat arabaMorfolojik = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(30, 30));       //Point p = new Point(5,5);
        Imgproc.morphologyEx(arabaHistogram, arabaMorfolojik, Imgproc.MORPH_OPEN, kernel);
        Imgcodecs.imwrite("D:\\SANDISK\\CODES\\Java\\OpenCV-PlakaTanıma\\Images\\4-Araba1_morfolojikResim.jpg", arabaMorfolojik);

        //Görüntü Çıkarma - Pixel Çıkarma
        Mat arabaFark = new Mat();
        Core.subtract(arabaHistogram, arabaMorfolojik, arabaFark);
        Imgcodecs.imwrite("D:\\SANDISK\\CODES\\Java\\OpenCV-PlakaTanıma\\Images\\5-Araba1_PixelCikarma.jpg", arabaFark);

        //Görüntü Eşikleme        
        Mat arabaEsik = new Mat();
        Imgproc.threshold(arabaFark, arabaEsik, 0, 255, Imgproc.THRESH_OTSU);
        Imgcodecs.imwrite("D:\\SANDISK\\CODES\\Java\\OpenCV-PlakaTanıma\\Images\\6-Araba1_Esikleme.jpg", arabaEsik);

        //Kenar Algılama
        Mat arabaKenar = new Mat();
        Imgproc.Canny(arabaEsik, arabaKenar, 250, 255);
        Imgcodecs.imwrite("D:\\SANDISK\\CODES\\Java\\OpenCV-PlakaTanıma\\Images\\7-Araba1_KenarAlgilama.jpg", arabaKenar);

        //Genişletme İşlemi
        Mat arabaGenisletme = new Mat();
        Mat kernel2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Imgproc.dilate(arabaKenar, arabaGenisletme, kernel2);
        Imgcodecs.imwrite("D:\\SANDISK\\CODES\\Java\\OpenCV-PlakaTanıma\\Images\\8-Araba1_KenarGenisletme.jpg", arabaGenisletme);

        //Kontur Eklenmesi 
        //Burası Düzeltilmeli - This part should be fixed
        /*
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(arabaKenar, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        
        Mat drawing = Mat.zeros(arabaKenar.size(), CvType.CV_8UC3);
        for (int i = 0; i < contours.size(); i++) {
        Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
        Imgproc.drawContours(drawing, contours, i, color, 2, Imgproc.LINE_8, hierarchy, 0, new Point());
        }
        
        Imgcodecs.imwrite("D:\\SANDISK\\CODES\\Java\\OpenCV-PlakaTanıma\\Images\\9-Araba1_Kontur.jpg", drawing);
        */
    }
}
