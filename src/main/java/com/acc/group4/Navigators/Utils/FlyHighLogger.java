package com.acc.group4.Navigators.Utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FlyHighLogger {
        private static Logger logger = Logger.getLogger("shubham_project");
        private static FileHandler fh;
        public static Logger initLogger() throws SecurityException, IOException {
        	fh = new FileHandler("navigators.log",true);
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			return logger;
        }
}
