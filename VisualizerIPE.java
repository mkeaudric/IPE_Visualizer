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
public class VisualizerIPE {
    private static final Path template = Paths.get("template1.txt");

    // ngitung manual, posisi nulis
    // TODO : nantinya diganti XMLUtil buat nyari line kemunculan line pertama yang mau diedit
    private static int styleSheet = 6;
    private static int layerEdit = styleSheet + 308; // awal di 314
    private static int dataEdit = layerEdit + 1; // awal di 316
    
    public static void initiateDrawing(Scanner sc, int c2, String filename) throws IOException {
        File output = new File(filename);
        if(output.createNewFile()){
            System.out.println("File " + filename + " berhasil dibuat.");
        } else {
            int c3;
            while(true){
                try {
                    System.out.print("File sudah ada.\nTimpa isi file?\n1. Ya\n2. Tidak\nPilihan: ");
                    c3 = sc.nextInt();
                    if(c3 == 1) {
                        break;
                    }
                    else if(c3 == 2) {
                        return; // Batal eksekusi
                    } else {
                        System.out.println("Pilih 1 atau 2.");
                    }
                } catch (InputMismatchException e){
                    System.out.println("Input harus berupa angka bulat (integer).");
                    sc.nextLine();
                    continue;
                }
            }
        }

        Path outputFile = Paths.get(filename);

        // Langsung copy template 1
        Files.copy(template, outputFile, StandardCopyOption.REPLACE_EXISTING);
        
        System.out.print("Masukan n, jumlah titik: ");
        int n = sc.nextInt();
        PointSet P = new PointSet();

        for(int i = 0; i < n; i++){
            double x = sc.nextDouble();
            double y = sc.nextDouble();
            Point p = new Point(x, y);
            P.addPoint(p);
        }

        // Hitung canvas satu kali saja di sini, berlaku untuk algoritma apapun
        String canvas = calculateLayout(P.getMin_x(), P.getMax_x(), P.getMin_y(), P.getMax_y(), 30);

        if(c2 == 1){ // DnC CH
            // TODO: implementasi DnC CH
            System.out.println("Fitur Divide and Conquer Convex Hull belum dibuat...");
        } else { // SEC
            SEC solver = new SEC();
            Circle res = solver.miniDisc(P);
            InsertLine ins = new InsertLine();

            // Masukin dari paling bawah dulu (Reverse Insertion) biar offset ga berantakan
            ins.insertTextAtLine(outputFile, dataEdit, solver.getData()); 
            ins.insertTextAtLine(outputFile, layerEdit, solver.getNewLayer());
            ins.insertTextAtLine(outputFile, styleSheet, canvas);
            
            System.out.println("Visualisasi SEC selesai dan disimpan ke " + filename);
        }
    }

    public static String calculateLayout(double min_x, double max_x, double min_y, double max_y, int padding){
        double canvasMin_x = 0;
        double canvasMax_x = 800;
        double canvasMin_y = 0;
        double canvasMax_y = 600;
        
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
        int c2;
        
        while(true){
            try{
                System.out.print("Pilih visualisasi:\n1. DnC CH\n2. SEC\nPilihan: ");
                c2 = sc.nextInt();
                if(c2 == 1 || c2 == 2) {
                    break;
                } else {
                    System.out.println("Pilih 1 atau 2.");
                }
            } catch (InputMismatchException e){
                System.out.println("Input harus berupa angka bulat (integer).");
                sc.nextLine();
                continue;
            }
        }

        System.out.print("Nama file: ");
        filename = sc.next();
        while(!filename.endsWith(".ipe") || filename.length() <= 4){
            System.out.print("Ekstensi file harus .ipe\nNama file: ");
            filename = sc.next();
        }

        try {
            initiateDrawing(sc, c2, filename);
        } catch (IOException e) {
            System.out.println("Gagal menulis file: " + e.getMessage());
        }
    }
}