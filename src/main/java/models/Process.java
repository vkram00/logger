package models;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Process {

	private final String processId;
	private final long startTime;
	private long endTime;

	public Process(String pId, long startTime){
		this.processId = pId;
		this.startTime = startTime;
		this.endTime = -1L;
	}

	public void setEndTime(long endTime){
		if(this.endTime < 0)
			this.endTime = endTime;
		else
			throw new IllegalStateException("process already ended!!!");
	}
}
