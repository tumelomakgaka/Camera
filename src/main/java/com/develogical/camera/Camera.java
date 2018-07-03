package com.develogical.camera;

public class Camera {
    private Sensor tempSensor;
    private MemoryCard tempMc;
    private boolean cameraOn=false;
    private boolean isCurrentlyWriting=false;

    public Camera(Sensor sensor, MemoryCard mc) {
        tempSensor=sensor;
        tempMc=mc;
    }

    public void pressShutter(Sensor sensor, MemoryCard mc) {
        if(cameraOn==true)
        {
            isCurrentlyWriting = true;
            mc.write(sensor.readData(), new WriteCompleteListener() {
                @Override
                public void writeComplete() {
                    isCurrentlyWriting=false;

                }
            });
        }

    }



    public void powerOn() {
        tempSensor.powerUp();
        cameraOn=true;
    }

    public void powerOff() {
        if (isCurrentlyWriting == false){
            tempSensor.powerDown();
            cameraOn=false;
        }
    }
}

