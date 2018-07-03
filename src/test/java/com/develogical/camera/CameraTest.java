package com.develogical.camera;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class CameraTest {
    @Test
    public void TestSwitchingTheCameraOnPowersUpTheSensor() {

        Sensor sensor = mock(Sensor.class);
        Camera camera = new Camera(sensor, mock(MemoryCard.class));
        camera.powerOn();
        verify(sensor).powerUp();
    }

    @Test
    public void TestSwitchingTheCameraOffPowersDownTheSensor() {

        Sensor sensor = mock(Sensor.class);
        Camera camera = new Camera(sensor, mock(MemoryCard.class));
        camera.powerOff();
        verify(sensor).powerDown();
    }

    @Test
    public void TestCopyDataWhenPressShutterWhilePowerOn() {
        Sensor sensor = mock(Sensor.class);
        MemoryCard mc = mock(MemoryCard.class);
        Camera camera = new Camera(sensor, mc);
        camera.powerOn();
        camera.pressShutter(sensor, mc);
        verify(sensor).readData();
        verify(mc).write(any(), any());
    }

    @Test
    public void TestShutterWhenPowerOff() {
        Sensor sensor = mock(Sensor.class);
        MemoryCard mc = mock(MemoryCard.class);
        Camera camera = new Camera(sensor, mc);
        camera.powerOff();
        camera.pressShutter(sensor, mc);
        verify(sensor, never()).readData();
        verify(mc, never()).write(any(), any());

    }

    @Test
    public void doesNotPowerOffSensorUntilFinishedWritingData() {
        Sensor sensor = mock(Sensor.class);
        FakeMemoryCard mc = new FakeMemoryCard();
        Camera camera = new Camera(sensor, mc);
        camera.powerOn();
        camera.pressShutter(sensor, mc);
        camera.powerOff();

        verify(sensor, never()).powerDown();

        mc.writeCompleteListener.writeComplete();
        camera.powerOff();
        verify(sensor).powerDown();
    }

    private static class FakeMemoryCard implements MemoryCard {
        public WriteCompleteListener writeCompleteListener;

        @Override
        public void write(byte[] data, WriteCompleteListener writeCompleteListener) {
            this.writeCompleteListener = writeCompleteListener;
        }
    }
}
