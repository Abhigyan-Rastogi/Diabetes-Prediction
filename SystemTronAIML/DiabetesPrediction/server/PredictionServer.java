package DiabetesPrediction.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.util.StringTokenizer;

import com.sun.net.httpserver.HttpServer;

import DiabetesPrediction.DiabetesPrediction;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import java.net.InetSocketAddress;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.File;
import javax.imageio.ImageIO;

public class PredictionServer {
    static String[][] con_res = {
        {"/", readHtmlFile("DiabetesPrediction/doc/home.html"), "text/html"},
        {"/DiabetesPrediction.html", readHtmlFile("DiabetesPrediction/doc/DiabetesPrediction.html"), "text/html"},
        {"/About.html", readHtmlFile("DiabetesPrediction/doc/About.html"), "text/html"},
        {"/Contact.html", readHtmlFile("DiabetesPrediction/doc/Contact.html"), "text/html"},
        {"/style.css", readHtmlFile("DiabetesPrediction/doc/style.css"), "text/css"},
        {"/images/doctor-image.png", readByteFile("DiabetesPrediction/doc/images/doctor-image.png"), "image/png"},
        {"/script.js", readHtmlFile("DiabetesPrediction/doc/script.js"), "text/javascript"},
        {"/dataexchange", "", "text/results"}
    };
    public static void main(String args[]) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

            for(int i = 0; i < con_res.length; i ++) {
                final int fi = i;
                server.createContext(con_res[i][0]).setHandler((e)->{
                    if(e.getRequestMethod().equals("POST")) {
                        String request = "";
                        InputStream is = e.getRequestBody();
                        int ch;
                        while((ch=is.read())!=-1)
                        request += (char)ch;
                        con_res[fi][1] = postExchange(request);
                    }
                    if(!con_res[fi][2].equals("text/results"))
                        e.getResponseHeaders().set("Content-type", con_res[fi][2]);
                    e.sendResponseHeaders(200, con_res[fi][1].getBytes().length);
                    OutputStream os = e.getResponseBody();
                    System.out.println("fi:" + fi + "," + con_res[fi][2]);
                    if(con_res[fi][2].equals("image/png")){
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        System.out.println("reached");
                        try {
                            ImageIO.write(ImageIO.read(new File("DiabetesPrediction/doc"+con_res[fi][0])), "png", baos);
                        } catch (Exception io) {
                            System.out.println("file error"+io.getMessage());
                        }
                        os.write(baos.toByteArray());
                    }
                    else
                    os.write(con_res[fi][1].getBytes());
                    os.close();
                });
            }
            server.start();
        } catch(IOException e) {
            System.out.println("HttpServer error:" + e.getMessage());
        }    
    }
    public static String readHtmlFile(String fileName) {
        String content = "";
        try {
            FileReader fr = new FileReader(fileName);
            int ch;
            while((ch=fr.read()) != -1) {
                content += (char)ch;
            }
            fr.close();
        } catch(IOException e) {
            System.out.println("Html file read error " + e.getMessage());
        }
        // System.out.println("Sending response:\n"+content);
        return content;
    }
    public static String readByteFile(String fileName) {
        String content="";
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(ImageIO.read(new File(fileName)), "png", baos);
            byte[] bin = baos.toByteArray();
            content = new String(bin);
            System.out.println();
        } catch(Exception e) {
            System.out.println("Byte file read error " + e.getMessage());
            e.printStackTrace();
        }
        return content;
    }
    public static String getPrediction(String data) {
        String res = ""; 
        // "HTTP/1.1 200 OK \n"+
        // "Content-Type: text/html\n\r";
        String trainingSet = "diabetes.csv", testFile = "test_diabetes.csv";
        DiabetesPrediction.resetCSVTestFile(testFile);
        DiabetesPrediction.writeCSVTestFile(testFile, data+"\n");
        String result = DiabetesPrediction.getPrediction(DiabetesPrediction.loadDataset(trainingSet));

        double prediction = Double.parseDouble(result);
        if(prediction < 0.5)
            res += "Negative";
        else
            res += "Positive";
        return res;
    }
    public static String postExchange(String postdata) {
        String response = "";
        StringTokenizer pd = new StringTokenizer(postdata, "&", false);
        while(pd.hasMoreTokens()) {
            // pd.nextToken();
            String cur = pd.nextToken();
            cur = cur.substring(cur.indexOf('=')+1, cur.length());
            response += cur + ",";
        }
        // System.out.println(postdata);
        response = getPrediction(response);
        return response;
    }
}
