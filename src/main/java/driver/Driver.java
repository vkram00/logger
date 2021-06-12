package driver;

import interfaces.ILogger;
import models.Logger;
import java.util.concurrent.TimeUnit;

public class Driver {

	public static void main(String[] args) throws InterruptedException {
		ILogger logger = new Logger();
		TimeUnit timeUnit = TimeUnit.SECONDS;
		logger.start("P1", System.currentTimeMillis());
		Thread.sleep(2000);
		logger.print();

		logger.start("P2", System.currentTimeMillis());
		Thread.sleep(2000);
		logger.print();

		logger.start("P3", System.currentTimeMillis());
		Thread.sleep(2000);
		logger.print();

		logger.start("P4", System.currentTimeMillis());
		Thread.sleep(2000);
		logger.print();

		logger.end("P1", System.currentTimeMillis());
		Thread.sleep(5000);
		logger.print();

		logger.end("P2", System.currentTimeMillis());
		Thread.sleep(3000);
		logger.print();

		logger.end("P4", System.currentTimeMillis());
		Thread.sleep(6000);
		logger.print();

		logger.end("P3", System.currentTimeMillis());
		Thread.sleep(3000);
		logger.print();
	}
}
