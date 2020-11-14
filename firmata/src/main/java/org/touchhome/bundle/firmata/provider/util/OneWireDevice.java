package org.touchhome.bundle.firmata.provider.util;

import lombok.Getter;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;

@Getter
public class OneWireDevice {
    byte[] device;
    long address;
    boolean validCrc;

    public OneWireDevice(byte[] device) {
        this.device = device;
        this.validCrc = OneWireUtils.crc8(device) == device[7];
        this.address = ByteBuffer.wrap(device).getLong(0);
    }

    public static ByteBuffer toByteArray(long address) {
        return (ByteBuffer) ByteBuffer.allocate(Long.BYTES).putLong(address).position(0);
    }

    @Override
    public String toString() {
        return Hex.encodeHexString(this.device, false).replaceAll("..", "$0 ")
                + " / CRC " + (validCrc ? "valid" : "invalid");
    }

    public boolean isFamily(int family) {
        return this.device[0] == family;
    }
}
