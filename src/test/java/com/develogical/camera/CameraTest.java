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
        camera.pressShutter();
        verify(sensor).readData();
        verify(mc).write(any(), any());
    }

    @Test
    public void TestShutterWhenPowerOff() {
        Sensor sensor = mock(Sensor.class);
        MemoryCard mc = mock(MemoryCard.class);
        Camera camera = new Camera(sensor, mc);
        camera.powerOff();
        camera.pressShutter();
        verify(sensor, never()).readData();
        verify(mc, never()).write(any(), any());

    }

    @Test
    public void doesNotPowerOffSensorUntilFinishedWritingData() {
        Sensor sensor = mock(Sensor.class);
        ArgumentCaptor<WriteCompleteListener> writeCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(WriteCompleteListener.class);
        MemoryCard memoryCard = mock(MemoryCard.class);
        Camera camera = new Camera(sensor, memoryCard);
        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();

        verify(memoryCard).write(any(), writeCompleteListenerArgumentCaptor.capture());

        verify(sensor, never()).powerDown();

        writeCompleteListenerArgumentCaptor.getValue().writeComplete();

        verify(sensor).powerDown();
    }
}
