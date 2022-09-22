package com.at999.util.jdbc.datasource.sakura.access;

public class InitializationException extends RuntimeException{

	public InitializationException(){}

	public InitializationException(String message){
		super(message);
	}

	public InitializationException(Throwable cause){
		super(cause);
	}

	public InitializationException(String message, Throwable cause){
		super(message, cause);
	}

	public InitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
