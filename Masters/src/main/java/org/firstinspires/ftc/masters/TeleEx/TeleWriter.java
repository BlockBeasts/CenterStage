package org.firstinspires.ftc.masters.TeleEx;

import android.os.Environment;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TeleWriter {

    public FileWriter logWriter;
    ElapsedTime runtime;
    public File log;

    public void newCSVFile(String file, List<String> header) throws IOException {

        runtime = new ElapsedTime();

        DateTime currentDateAndTime = DateTime.now();

        if(file == null) {
            log = new File(Environment.getExternalStorageDirectory().getPath() + "/PowerLogging/" + currentDateAndTime.getMillis() + ".csv");
        } else {
            log = new File(Environment.getExternalStorageDirectory().getPath() + "/PowerLogging/" + file + ".csv");
        }

        logWriter = new FileWriter(log);

        header.add("Time");
        logWriter.write(String.join(",", header));

    }

    public void writeCSVFile(String data) throws IOException {

        logWriter.write(data);

    }

    public ElapsedTime getRuntime() {
        return runtime;
    }
}
