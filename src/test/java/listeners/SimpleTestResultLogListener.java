package listeners;


import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SimpleTestResultLogListener extends TestListenerAdapter {
    private static String TEST_SUITE_START_TEXT = "\n\n\n***************Suite %s started***************\n";
    private static String suiteName = "";
    private static String suiteTestName = "";

    @Override
    public void onStart(ITestContext tr) {
        if (!tr.getSuite().getXmlSuite().getName().equals(suiteName)) {
            suiteName = tr.getSuite().getXmlSuite().getName();
            System.out.println(String.format(TEST_SUITE_START_TEXT, suiteName));
        }
        suiteTestName = tr.getName();
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        writeToLog("Ошибка", tr.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        writeToLog("Пропущенно\t", tr.getThrowable().getMessage());
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        writeToLog("Успех", "");
    }

    public static void writeToLog(String status, String message) {
        try {
            String path = new File("").getAbsolutePath() + "\\src\\test\\resources\\log.txt";
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            String text = reportDate + "\t" + status + "\t" + message + "\n\r";
            BufferedWriter providerFile = new BufferedWriter(new FileWriter(path));
            providerFile.flush();
            providerFile.write(text);
            providerFile.close();
        } catch (IOException ex) {
            System.out.println(ex.toString());
            return;
        }
    }
}