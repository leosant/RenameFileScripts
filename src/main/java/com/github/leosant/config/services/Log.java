package com.github.leosant.config.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    public static Logger local(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
