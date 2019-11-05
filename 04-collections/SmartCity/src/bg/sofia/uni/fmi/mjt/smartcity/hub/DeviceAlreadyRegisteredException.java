package bg.sofia.uni.fmi.mjt.smartcity.hub;

public class DeviceAlreadyRegisteredException extends Exception {
    public DeviceAlreadyRegisteredException(String errorMessage) {
        super(errorMessage);
    }
}
