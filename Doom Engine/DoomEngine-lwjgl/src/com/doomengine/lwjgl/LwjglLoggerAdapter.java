package com.doomengine.lwjgl;

import java.util.Date;

import com.doomengine.system.ILoggerAdapter;

public class LwjglLoggerAdapter implements ILoggerAdapter {

	@Override public void log(String msg) {
		Date date = new Date();

		System.out.println("[" + date + "]" + ": " + msg);
		System.out.flush();
	}

}
