package listeners;


import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private static final int MAX_TRY_COUNT = 3;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess() && count < MAX_TRY_COUNT) {
            count++;
            return true;                                 //Tells TestNG to re-run the scripts.lkmts
        }
        return false;
    }
}
