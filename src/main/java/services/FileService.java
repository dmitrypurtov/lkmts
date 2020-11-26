package services;

import containers.User;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FileService {

    public static List<User> readUseAccesses(String path) throws IOException {
        List<User> users = new ArrayList<>();
        String absoluteUrlCsv = new File("").getAbsolutePath() + path;
        BufferedReader br = new BufferedReader(new FileReader(absoluteUrlCsv));
        String line;
        while ((line = br.readLine()) != null) {
            String[] arrString = line.split(",");
            User user = new User();
            try {
                user.login = arrString[0];
                user.password = arrString[1];
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new IOException("Error: incorrect parameters in csv, array out of range");
            }
            users.add(user);
        }
        return users;
    }

    public static void writeToDetailLog(String message) {
        try {
            String path = new File("").getAbsolutePath() + "\\src\\test\\resources\\log_detail.txt";
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            String text = reportDate + "\t" + message + "\n\r";
            BufferedWriter providerFile = new BufferedWriter(new FileWriter(path));
            providerFile.flush();
            providerFile.write(text);
            providerFile.close();
            System.out.println(text);
        } catch (IOException ex) {
            System.out.println(ex.toString());
            return;
        }
    }
}
