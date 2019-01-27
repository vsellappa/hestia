package hortonworks.hdf.streaming;

import org.testng.ISuite;
import org.testng.ISuiteListener;

public class SmokeTestListener implements ISuiteListener {
    @Override
    public void onStart(ISuite suite) {
        System.out.println("Starting Smoke Tests............");
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("Ending Smoke Tests............");
    }
}
