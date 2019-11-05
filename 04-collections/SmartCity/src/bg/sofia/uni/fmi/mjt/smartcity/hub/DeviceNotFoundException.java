package bg.sofia.uni.fmi.mjt.smartcity.hub;

public class DeviceNotFoundException extends Exception {
    public DeviceNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
