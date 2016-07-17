package com.doomengine.system;

public class DoomException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DoomException(String message) {
		super(message);
	}

	public DoomException(String message, Throwable arg1) {
		super(message, arg1);
	}

}
