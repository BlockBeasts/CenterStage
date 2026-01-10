package org.firstinspires.ftc.masters.TeleEx;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelemetryEx extends TelemetryImpl {

    Map<String, Object> dataMap = new HashMap<>();
    String name;
    List<String> header;
    Telemetry telemetry;
    double pastRuntime;

    public TelemetryEx(OpMode opMode, Telemetry telemetry) {
        super(opMode);
        this.telemetry=telemetry;
    }

    LogWriter logWriter = new LogWriter();

    @Override
    public Item addData(String caption, Object value) {
        telemetry.addData(caption, value);

        dataMap.put(caption, value);

        return null;
    }

    @Override
    public boolean update(){
        telemetry.update();

        if(logWriter.log == null){
            try {
                logWriter.newCSVFile(name, header);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {

                ElapsedTime runtime = logWriter.getRuntime();

                dataMap.put("Time", Math.round(runtime.milliseconds()));

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\n");
                for (String header : header){
                    if(dataMap.get(header) == null){
                        stringBuilder.append(",");
                    } else {
                        stringBuilder.append(dataMap.get(header));
                        stringBuilder.append(",");
                    }
                }

                logWriter.writeCSVFile(stringBuilder.substring(0, stringBuilder.length()-1));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    public void setName(String Name) { this.name = Name; }
    public void setHeader(List<String> Header) { this.header = Header; }

}
