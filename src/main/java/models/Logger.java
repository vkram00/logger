package models;

import interfaces.ILogger;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Logger implements ILogger {

	private final ConcurrentHashMap<String, Process> processMap;
	private final PriorityQueue<Process> processQueue;
	private final ReentrantLock lock;
	private final Condition processEnded;
	private final ExecutorService executorService;
	private final ExecutorService printService;

	public Logger() {
		this.processMap = new ConcurrentHashMap<>();
		this.processQueue = new PriorityQueue<Process>((p1, p2) -> Long.compare(p1.getStartTime(), p2.getStartTime()));
		lock = new ReentrantLock();
		processEnded = lock.newCondition();
		executorService = Executors.newSingleThreadExecutor();
		printService = Executors.newSingleThreadExecutor();
	}

	@Override public void start(String processId, long startTime) {
		executorService.execute(() -> {
			Process process = new Process(processId, startTime);
			processMap.putIfAbsent(processId, process);
			processQueue.add(process);
		});
	}

	@Override public void end(String processId, long endTime) {
		executorService.execute(() -> {
			lock.lock();
			try {
				Process process = processMap.get(processId);
				process.setEndTime(endTime);
				if (!processQueue.isEmpty() && processQueue.peek().getProcessId().equals(processId)) {
					processEnded.signalAll();
				}
			} catch (NullPointerException | IllegalStateException ex) {
				ex.printStackTrace();
			} finally {
				lock.unlock();
			}
		});
	}

	@Override public void print() {
		printService.execute(() -> {
			lock.lock();
			try {
				//while (!processQueue.isEmpty()) {
					if (!processQueue.isEmpty()) {
						if (processQueue.peek().getEndTime() >= 0) {
							Process process = processQueue.poll();
							System.out.println(String.format("%s started at %s and ended at %s", process.getProcessId(),
									process.getStartTime(), process.getEndTime()));
						} else {
							processEnded.await();
						}
					} else {
						System.out.println("no process found in the queue!!!");
					}
				//}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		});
	}

}
