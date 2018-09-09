package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Methods responsible for logging TestNG related events.
 */
public class TestExecutionListener extends TestListenerAdapter {

    private static Logger log = LogManager.getFormatterLogger();

    @Override
    public void onTestStart(ITestResult iTestResult) {
        iTestResult.getStartMillis();
        log.info(buildTestSignature(iTestResult.getMethod()));
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        String testClassName = iTestResult.getMethod().getRealClass().getSimpleName();
        String testMethodName = iTestResult.getMethod().getMethodName();
        log.info("TEST PASSED: %s.%s ", testClassName, testMethodName);
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        Throwable throwable = iTestResult.getThrowable();
        if (throwable != null && throwable instanceof NullPointerException) {
            log.error("NullPointerException - see stacktrace below!");
            throwable.printStackTrace();
        }
        String testClassName = iTestResult.getMethod().getRealClass().getSimpleName();
        String testMethodName = iTestResult.getMethod().getMethodName();
        log.error("TEST FAILED: %s.%s", testClassName, testMethodName);
    }

    private String buildTestSignature(final ITestNGMethod iTestNGMethod) {

        StringBuilder stringBuilder = new StringBuilder();

        if (iTestNGMethod.getDescription() == null) {
            stringBuilder.append("TEST CASE: ")
                    .append(iTestNGMethod.getRealClass().getName())
                    .append(".")
                    .append(iTestNGMethod.getMethodName())
                    .append("\n");

            return stringBuilder.toString();
        } else {
            stringBuilder.append("TEST CASE: ")
                    .append(iTestNGMethod.getRealClass().getName())
                    .append(".")
                    .append(iTestNGMethod.getMethodName())
                    .append("\n")
                    .append("DESCRIPTION: ")
                    .append(iTestNGMethod.getDescription())
                    .append("\n");

            return stringBuilder.toString();
        }
    }

}
