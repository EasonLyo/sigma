package net.oooops.sigma.core;

import ch.qos.logback.classic.*;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ConfiguratorRank;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.status.NopStatusListener;
import ch.qos.logback.core.util.FileSize;

@ConfiguratorRank()
public class SigmaLogbackConfiguration extends ContextAwareBase implements Configurator {

    public SigmaLogbackConfiguration() {
    }

    @Override
    public ExecutionStatus configure(LoggerContext loggerContext) {
        addInfo("Setting up Sigma logback configuration.");

        // listener
        NopStatusListener nopStatusListener = new NopStatusListener();
        loggerContext.getStatusManager().add(nopStatusListener);

        // console
        ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<>();
        ca.setContext(context);
        ca.setName("console");
        // encoder
        LayoutWrappingEncoder<ILoggingEvent> console_encoder = new LayoutWrappingEncoder<>();
        console_encoder.setContext(context);
        // layout
        PatternLayout consolePatternLayout = new PatternLayout();
        consolePatternLayout.setPattern("""
                %d{yyyy-MM-dd HH:mm:ss.SSS} %highlight(%-5level) --- [%20.20thread] %cyan(%-40.40logger{39}) : %msg%n""");
        consolePatternLayout.setContext(context);
        consolePatternLayout.start();
        console_encoder.setLayout(consolePatternLayout);
        ca.setEncoder(console_encoder);
        ca.start();

        // file
        RollingFileAppender<ILoggingEvent> rfa = new RollingFileAppender<>();
        rfa.setContext(context);
        rfa.setName("file");
        String fileName = STR."\{System.getProperty("user.dir")}/log/sigma.log";
        rfa.setFile(fileName);

        // encoder
        LayoutWrappingEncoder<ILoggingEvent> file_encoder = new LayoutWrappingEncoder<>();
        file_encoder.setContext(context);
        // layout
        PatternLayout filePatternLayout = new PatternLayout();
        filePatternLayout.setContext(context);
        filePatternLayout.setPattern("""
                %d{yyyy-MM-dd HH:mm:ss.SSS} %-5level --- [%20.20thread] %-40.40logger{39} : %msg%n""");
        filePatternLayout.start();
        file_encoder.setLayout(filePatternLayout);
        rfa.setEncoder(file_encoder);

        // policy
        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rp = new SizeAndTimeBasedRollingPolicy<>();
        rp.setContext(context);
        rp.setFileNamePattern(STR."""
        \{fileName}.%d{yyyy-MM-dd}.%i.gz""");
        rp.setCleanHistoryOnStart(false);
        rp.setMaxFileSize(FileSize.valueOf("10MB"));
        rp.setMaxHistory(7);
        rp.setParent(rfa);
        rp.start();
        rfa.setRollingPolicy(rp);
        rfa.start();

        // async
        AsyncAppender asyncConsole = new AsyncAppender();
        asyncConsole.setContext(context);
        asyncConsole.setName("async_console");
        asyncConsole.addAppender(ca);
        asyncConsole.start();
        // async file
        AsyncAppender asyncFile = new AsyncAppender();
        asyncFile.setContext(context);
        asyncFile.setName("async_file");
        asyncFile.addAppender(rfa);
        asyncFile.start();

        // TODO 获取环境变量以及根据环境进行日志输出
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.DEBUG);
        rootLogger.addAppender(asyncConsole);
        rootLogger.addAppender(asyncFile);

        // let the caller decide
        return ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY;
    }
}