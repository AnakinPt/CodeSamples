package pt.com.hugodias.sample.entities;

import org.springframework.util.StopWatch.TaskInfo;

public final class GenericResponse<T> {
	private T result;
	private Error error;
	private TaskInfo[] metrics;
	private String errorMessage;
	
	public GenericResponse(T result, Error error, TaskInfo[] metrics) {
		super();
		this.result = result;
		this.error = error;
		this.metrics = metrics;
		this.errorMessage = null;
	}

	public GenericResponse(T result, Error error, TaskInfo[] metrics, String errorMessage) {
		super();
		this.result = result;
		this.error = error;
		this.metrics = metrics;
		this.errorMessage = errorMessage;
	}

	public T getResult() {
		return result;
	}
	public Error getError() {
		return error;
	}
	public TaskInfo[] getMetrics() {
		return metrics;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	
}
