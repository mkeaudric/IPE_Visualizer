import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.InputMismatchException;
import java.util.Scanner;

import Algo.SEC;
import GeoUtil.Circle;
import GeoUtil.Point;
import GeoUtil.PointSet;
import XMLUtil.InsertLine;

@SuppressWarnings("unused")
public class VisualizerIPE{
    private static final Path T1 = Paths.get("template1.txt");
    private static final Path T2 = Paths.get("template2.txt");

    // ngitung manual, posisi nulis
    // TODO : nantinya diganti XMLUtil buat nyari line kemunculan line pertama yang mau diedit
    // lalu dihitung offset?
    private static int styleSheetT1 = 6;
    private static int layerEditT1 = styleSheetT1 + 308; // awal di 314
    private static int dataEditT1 = layerEditT1 + 1; // awal di 316
    
    // private static int styleSheetT2 = 0;
    // private static int layerEditT2 = 9767; // buat nambah layer disini
    // private static int dataEditT2 = 9780;
    
    public static void initiateDrawing(Scanner sc, int c1, int c2, String filename) throws IOException {
        File output = new File(filename);
        if(output.createNewFile()){
            System.out.println("file " + filename + " dibuat");
        } else{
            int c3;
            System.out.print("File sudah ada.\nTimpa isi file?\n");
            while(true){
                try{
                    System.out.print("File sudah ada.\nTimpa isi file?\n1. ya 1\n2. tidak\n");
                    c3 = sc.nextInt();
                    if(c3 == 1) {
                        break;
                    }
                    else if(c3 == 2) {
                        return;
                    } else {
                        System.out.println("pilih 1 / 2");
                    }
                } catch (InputMismatchException e){
                    System.out.println("Input harus int");
                    sc.nextLine();
                    continue;
                }
            }
        }

        Path outputFile = Paths.get(filename);

        if(c1 == 1){ // template 1
            Files.copy(T1, outputFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.print("Masukan n, jumlah titik: ");
            int n = sc.nextInt();
            PointSet P = new PointSet();

            int i;
            for(i = 0; i < n; i++){
                double x = sc.nextDouble();
                double y = sc.nextDouble();
                Point p = new Point(x, y);
                P.addPoint(p);
            }

            String canvas = calculateLayout(P.getMin_x(), P.getMax_x(), P.getMin_y(), P.getMax_y(), 30);

            if(c2 == 1){ // DnC CH
                double min_x = P.getMin_x();
                double max_x = P.getMax_x();
                double min_y = P.getMin_y();
                double max_y = P.getMax_y();
                // implementasi DnC CH
                System.out.println("Fitur belum dibuat...");
            } else{ // SEC
                double min_x = P.getMin_x();
                double max_x = P.getMax_x();
                double min_y = P.getMin_y();
                double max_y = P.getMax_y();

                SEC solver = new SEC();

                Circle res = solver.miniDisc(P);

                InsertLine ins = new InsertLine();

                // masukin dari paling bawah dulu
                ins.insertTextAtLine(outputFile, dataEditT1, solver.getData()); // lompat 1, ada baris buat active layer
                ins.insertTextAtLine(outputFile, layerEditT1, solver.getNewLayer());
                ins.insertTextAtLine(outputFile, styleSheetT1, canvas);
            }
        } else{ // template 2
            Files.copy(T2, outputFile, StandardCopyOption.REPLACE_EXISTING);

            if(c2 == 1){ // DnC CH
                System.out.println("Fitur belum dibuat...");
            } else{ // SEC
                System.out.println("Fitur belum dibuat...");
            }
        }
    }

    public static String calculateLayout(double min_x, double max_x, double min_y, double max_y, int padding){
        // default 1024 * 768
        double canvasMin_x = 0;
        double canvasMax_x = 1024;
        double canvasMin_y = 0;
        double canvasMax_y = 768;
        
        if(min_x < canvasMin_x) canvasMin_x = min_x - padding;
        if(max_x > canvasMax_x) canvasMax_x = max_x + padding;
        if(min_y < canvasMin_y) canvasMin_y = min_y - padding;
        if(max_y > canvasMax_y) canvasMax_y = max_y + padding;

        double width = canvasMax_x - canvasMin_x;
        double height = canvasMax_y - canvasMin_y;

        return String.format(
            "<layout paper=\"%.4f %.4f\" origin=\"%.4f %.4f\" frame=\"%.4f %.4f\"/>",
            width, height, canvasMin_x, canvasMin_y, width, height
        );
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String filename;
        int c1, c2;

        while(true){
            try{
                System.out.print("Pilih template:\n1. template 1\n2. template 2 (+ background PPT)\n");
                c1 = sc.nextInt();
                if(c1 == 1) {
                    break;
                }
                else if(c1 == 2) {
                    break;
                } else {
                    System.out.println("pilih 1 / 2");
                }
            } catch (InputMismatchException e){
                System.out.println("Input harus int");
                sc.nextLine(); // buang isi c1
                continue;
            }
        }
        
        while(true){
            try{
                System.out.print("Pilih visualisasi:\n1. DnC CH\n2. SEC\n");
                c2 = sc.nextInt();
                if(c2 == 1) {
                    break;
                }
                else if(c2 == 2) {
                    break;
                } else {
                    System.out.println("pilih 1 / 2");
                }
            } catch (InputMismatchException e){
                System.out.println("Input harus int");
                sc.nextLine();
                continue;
            }
        }

        System.out.print("Nama file: ");
        filename = sc.next();
        while(!filename.endsWith(".ipe") || !(filename.length() > 4)){
            System.out.print("Ekstensi file harus .ipe\nNama file: ");
            filename = sc.next();
        }

        try {
            initiateDrawing(sc, c1, c2, filename);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}