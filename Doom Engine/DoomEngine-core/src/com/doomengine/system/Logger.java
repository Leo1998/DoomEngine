package com.doomengine.system;

public final class Logger {

	private static ILoggerAdapter adapter;

	public static void setAdapter(ILoggerAdapter arg0) {
		adapter = arg0;
	}

	public static void log(String msg) {
		adapter.log(msg);
	}

	public static void log(Object o) {
		adapter.log(o.toString());
	}

}
