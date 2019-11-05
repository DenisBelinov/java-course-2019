package bg.sofia.uni.fmi.mjt.smartcity.hub;

import bg.sofia.uni.fmi.mjt.smartcity.device.SmartDevice;
import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class SmartCityHub {

    private Map<SmartDevice, String> devices;
    private Map<DeviceType, Integer> deviceCounts;

    public SmartCityHub() {
        devices = new LinkedHashMap();

        //init device counts
        deviceCounts = new HashMap<>();
        for (DeviceType type : DeviceType.values())
            deviceCounts.put(type, 0);
    }

    private static Comparator<SmartDevice> consumedPowerComparator = new Comparator<SmartDevice>() {
        @Override
        public int compare(SmartDevice s1, SmartDevice s2) {
            LocalDateTime now = LocalDateTime.now();
            double consumedPower1 = Duration.between(now, s1.getInstallationDateTime()).toHours() * s1.getPowerConsumption();
            double consumedPower2 = Duration.between(now, s2.getInstallationDateTime()).toHours() * s2.getPowerConsumption();

            return (int) (consumedPower1 - consumedPower2);
        }
    };
    /**
     * Adds a @device to the SmartCityHub.
     *
     * @throws IllegalArgumentException in case @device is null.
     * @throws DeviceAlreadyRegisteredException in case the @device is already registered.
     */
    public void register(SmartDevice device) throws DeviceAlreadyRegisteredException {
        if (device == null)
            throw new IllegalArgumentException("Cannot add null SmartDevice.");

        if (devices.putIfAbsent(device, device.getId()) != null) {
            String errMsg = String.format("Device %s already exists in the Hub.", device.getId());
            throw new DeviceAlreadyRegisteredException(errMsg);
        }

        // update counts
        deviceCounts.put(device.getType(), deviceCounts.get(device.getType()) + 1);
    }

    /**
     * Removes the @device from the SmartCityHub.
     *
     * @throws IllegalArgumentException in case null is passed.
     * @throws DeviceNotFoundException in case the @device is not found.
     */
    public void unregister(SmartDevice device) throws DeviceNotFoundException {
        if (device == null)
            throw new IllegalArgumentException("Cannot remove null SmartDevice.");

        if (devices.remove(device) == null) {
            String errMsg = String.format("Device %s does not exists in the Hub. Cannot remove.", device.getId());
            throw new DeviceNotFoundException(errMsg);
        }

        // update counts
        deviceCounts.put(device.getType(), deviceCounts.get(device.getType()) - 1);
    }

    /**
     * Returns a SmartDevice with an ID @id.
     *
     * @throws IllegalArgumentException in case @id is null.
     * @throws DeviceNotFoundException in case device with ID @id is not found.
     */
    public SmartDevice getDeviceById(String id) throws DeviceNotFoundException {
        if (id == null)
            throw new IllegalArgumentException("Cannot get null SmartDevice.");

        for (SmartDevice device : devices.keySet()) {
            if (device.getId().equals(id)) {
                return device;
            }
        }

        String errMsg = String.format("Device with id %s does not exists in the Hub.", id);
        throw new DeviceNotFoundException(errMsg);
    }

    /**
     * Returns the total number of devices with type @type registered in SmartCityHub.
     *
     * @throws IllegalArgumentException in case @type is null.
     */
    public int getDeviceQuantityPerType(DeviceType type) {
        if (type == null)
            throw new IllegalArgumentException("Null type passed.");

        return deviceCounts.get(type);
    }

    /**
     * Returns a collection of IDs of the top @n devices which consumed
     * the most power from the time of their installation until now.
     *
     * The total power consumption of a device is calculated by the hours elapsed
     * between the two LocalDateTime-s multiplied by the stated power consumption of the device.
     *
     * If @n exceeds the total number of devices, return all devices available sorted by the given criterion.
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public Collection<String> getTopNDevicesByPowerConsumption(int n) {
        if (n < 0)
            throw new IllegalArgumentException("Negative number passed.");

        Map<SmartDevice, String> sortedByPower = new TreeMap<>(consumedPowerComparator);
        sortedByPower.putAll(devices);

        Collection<String> result = new ArrayList<>();
        int count = 0;

        for (String id : sortedByPower.values()) {
            if (count == n)
                break;

            result.add(id);
            count += 1;
        }

        return result;
    }

    /**
     * Returns a collection of the first @n registered devices, i.e the first @n that were added
     * in the SmartCityHub (registration != installation).
     *
     * If @n exceeds the total number of devices, return all devices available sorted by the given criterion.
     *
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public Collection<SmartDevice> getFirstNDevicesByRegistration(int n) {
        if (n < 0)
            throw new IllegalArgumentException("Negative number passed.");

        Collection<SmartDevice> result = new ArrayList<>();
        int count = 0;
        for (SmartDevice device : devices.keySet()) {
            if (count == n)
                break;

            result.add(device);
            count += 1;
        }

        return result;
    }
}
