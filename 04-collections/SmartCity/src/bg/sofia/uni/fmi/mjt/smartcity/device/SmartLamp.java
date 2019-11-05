package bg.sofia.uni.fmi.mjt.smartcity.device;

import java.time.LocalDateTime;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;


public class SmartLamp extends SmartDeviceImp {
    private static final DeviceType DEVICE_TYPE = DeviceType.LAMP;

    private static long deviceCount = 0;

    public SmartLamp(String name, double powerConsumption, LocalDateTime installationDateTime) {
        String id = DEVICE_TYPE.getShortName() + "-" + name + "-" + deviceCount;
        deviceCount += 1;

        this.id = id;
        this.name = name;
        this.powerConsumption = powerConsumption;
        this.installationDateTime = installationDateTime;
        this.type = DEVICE_TYPE;
    }
}
