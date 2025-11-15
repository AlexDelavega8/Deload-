package es.pocketrainer.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorUtil {

	public static String generarExceptionStackTrace(Throwable e) {
		StringWriter stackTrace = new StringWriter();
		e.printStackTrace(new PrintWriter(stackTrace));
		stackTrace.flush();
		return stackTrace.toString();
	}
}
