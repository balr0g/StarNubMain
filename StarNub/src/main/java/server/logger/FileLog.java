package server.logger;

import lombok.Getter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.codehome.utilities.files.GetFileSize;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import server.StarNub;

public class FileLog {


    private final String FILE_DIRECTORY_STRING;


    private volatile String actualFile;


    private volatile int fileCount;


    private FileLogWriter fileWriter;

    public FileLog(String FILE_DIRECTORY_STRING) {
        this.FILE_DIRECTORY_STRING = FILE_DIRECTORY_STRING;
        this.fileCount = 0;
        this.actualFile = FILE_DIRECTORY_STRING + new DateTime().toString(DateTimeFormat.forPattern("dd-MMM-yy")) + "_" + fileCount + ".log";
        this.fileWriter = new FileLogWriter(actualFile);
        this.fileCount++;
    }

    public void startNewLog(){
        this.actualFile = FILE_DIRECTORY_STRING + new DateTime().toString(DateTimeFormat.forPattern("dd-MMM-yy")) + "_" + fileCount + ".log";
        this.fileWriter = new FileLogWriter(actualFile);
    }

    public void closeLog(){
        this.fileWriter.closeFileLogWriter();
        this.fileCount = 0;
    }

    private void rotateLogFile(){
        FileLogWriter oldLogFile = fileWriter;
        this.actualFile = FILE_DIRECTORY_STRING + new DateTime().toString(DateTimeFormat.forPattern("dd-MMM-yy")) + "_" + fileCount + ".log";
        this.fileWriter = new FileLogWriter(actualFile);
        oldLogFile.closeFileLogWriter();
    }

    public int logRotateNewDay(int dayOfMonth) {
        int today = new DateTime().getDayOfMonth();
        if (dayOfMonth != today) {
            this.fileCount = 0;
            rotateLogFile();
            return today;
        }
        return 0;
    }

    public void logRotateFileSize(double maxFileSize, String fileMeasure) {
        try {
            if (new GetFileSize().getFileSize(actualFile, fileMeasure) > maxFileSize) {
                this.fileCount++;
                rotateLogFile();
            }
        } catch (Exception e) {
            StarNub.getLogger().cFatPrint("StarNub", ExceptionUtils.getMessage(e));
        }
    }

    public int logRotateNewDateFileSize(int dayOfMonth, double maxFileSize, String fileMeasure) {
        int logRotate = logRotateNewDay(dayOfMonth);
        if (logRotate == 0) {
            logRotateFileSize(maxFileSize, fileMeasure);
        }
        return logRotate;
    }
}