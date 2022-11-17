package org.touchhome.bundle.raspberry.gpio;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.context.impl.DefaultContext;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.plugin.mock.platform.MockPlatform;
import com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogInputProvider;
import com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogOutputProvider;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInputProvider;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutputProvider;
import com.pi4j.plugin.mock.provider.i2c.MockI2CProvider;
import com.pi4j.plugin.mock.provider.pwm.MockPwmProvider;
import com.pi4j.plugin.mock.provider.serial.MockSerialProvider;
import com.pi4j.plugin.mock.provider.spi.MockSpiProvider;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.service.EntityService.ServiceInstance;
import org.touchhome.bundle.api.state.State;
import org.touchhome.bundle.api.util.BoardInfo;
import org.touchhome.bundle.raspberry.gpio.mode.PinMode;
import org.touchhome.bundle.raspberry.gpio.service.GpioConsolePlugin;

@Log4j2
@RequiredArgsConstructor
public class GPIOService implements ServiceInstance<GpioEntity<?>> {

  private final EntityContext entityContext;
  private final Map<String, MutablePair<Long, Float>> ds18B20Values = new HashMap<>();
  @Getter
  private final Map<Integer, GpioState> state = new ConcurrentHashMap<>();
  private Boolean available;
  @Value("${w1BaseDir:/sys/devices/w1_bus_master1}")
  private Path w1BaseDir;
  @Getter
  private GpioEntity entity;
  @Getter
  private Set<GpioPin> availableGpioPins;

  private final Context pi4j;

  @SneakyThrows
  public GPIOService(EntityContext entityContext, Set<GpioPin> availableGpioPins, GpioEntity entity) {
    this.entity = entity;
    this.entityContext = entityContext;
    this.availableGpioPins = availableGpioPins;
    this.pi4j = createContext();

    GpioUtil.printInfo(pi4j, log);
    createOrUpdateGpioPins(entity);

    this.entityContext.ui().registerConsolePlugin("gpio-console-" + entity.getEntityID(),
        new GpioConsolePlugin(entityContext, this));
  }

  private Context createContext() {
    if (BoardInfo.boardType.equals("UNKNOWN")) {
      return Pi4J.newContextBuilder()
          .add(new MockPlatform())
          .add(MockAnalogInputProvider.newInstance(),
              MockAnalogOutputProvider.newInstance(),
              MockSpiProvider.newInstance(),
              MockPwmProvider.newInstance(),
              MockSerialProvider.newInstance(),
              MockI2CProvider.newInstance(),
              MockDigitalInputProvider.newInstance(),
              MockDigitalOutputProvider.newInstance())
          .build();
    }
    // auto discovery
    return Pi4J.newAutoContext();
  }

  @SneakyThrows
  private void init() {
    if (isGPIOAvailable()) {
      /*for (GpioPin pin : GpioPin.values(PinMode.DIGITAL_INPUT, PullResistance.PULL_DOWN)) {
        if (pin.getOccupied() == null) {
          GpioPinDigital gpioPinDigital = getDigitalInput(pin, null);
          gpioPinDigital.setShutdownOptions(true, PinState.LOW, PullResistance.OFF);
          inputGpioValues.put(pin, UpdatableValue.wrap(gpioPinDigital.isHigh(), pin.name()));
          gpioPinDigital.addListener((GpioPinListenerDigital) event -> digitalListeners.get(pin).forEach(t -> t.consumer.accept(event)));
          digitalListeners.get(pin).add(new PinListener("INPUT_VALUE_LISTENER", event -> inputGpioValues.get(pin).update(event.getState().isHigh())));
        }
      }*/
    }
  }

  public boolean isGPIOAvailable() {
    if (available == null) {
      if (EntityContext.isDevEnvironment()) {
        available = false;
      } else {
        try {
          // GpioFactory.getInstance();
          available = true;
        } catch (Throwable ignore) {
          available = false;
        }
      }
      entityContext.setFeatureState("GPIO", available);
    }
    return available;
  }

  public State getState(int address) {
    GpioState gpioState = getState().get(address);
    return gpioState.getPinMode().getGpioModeFactory().getState(gpioState.getInstance());
  }

  public void setValue(int address, State state) {
    GpioState gpioState = getState().get(address);
    gpioState.getPinMode().getGpioModeFactory().setState(gpioState.getInstance(), state);
  }

  public void addGpioListener(String name, int address, Consumer<State> listener) {
    state.get(address).getListeners().put(name, listener);
  }

  public void removeGpioListener(int address, String name) {
    state.get(address).getListeners().remove(name);
  }

  /* public void addGpioListener(String name, Pin pin, Consumer<PinState> listener) {
     assertDigitalInputPin(pin, PinMode.DIGITAL_INPUT, "Unable to add pin listener for not input pin mode");
     setGpioPinMode(pin, PinMode.DIGITAL_INPUT, null);
     digitalListeners.get(pin).add(new PinListener(name, event -> listener.accept(event.getState())));
   }
 */
 /* private void assertDigitalInputPin(Pin pin, PinMode digitalInput, String s) {
    if (!pin.getSupportedPinModes().contains(digitalInput)) {
      throw new IllegalArgumentException(s);
    }
  }*/

  /*public void setPullResistance(int address, PullResistance pullResistance) {
    GpioState gpioState = state.get(address);
    if (gpioState.getPinMode() == PinMode.DIGITAL_INPUT && gpioState.getPull() != pullResistance) {

    }

    GpioConfig gpioConfig = getGpioConfig(gpioPin);
    pi4j.create(gpioConfig);
    getGpioConfig(gpioPin);
    getDigitalInput(pin, pullResistance);
  }*/

  /*private void setGpioPinMode(Pin pin, PinMode pinMode, PullResistance PullResistance) {
    if (available) {
      GpioPin input = getDigitalInput(pin, PullResistance);
      if (input.getMode() != pinMode) {
        input.setMode(pinMode);
      }
    }
  }*/

  /*  public void addTrigger(HasTriggersEntity hasTriggersEntity, TriggerBaseEntity triggerBaseEntity, GpioPinDigitalInput input, GpioTrigger trigger) {
        log.info("Activate trigger: " + getTriggerName(trigger) + " for input: " + getPINName(input.getPin().getName()) + ". Device owner: " + hasTriggersEntity.getEntityID());
        input.addTrigger(trigger);
        if (!activeTriggers.containsKey(hasTriggersEntity)) {
            activeTriggers.put(hasTriggersEntity, new ArrayList<>());
        }
        List<ActiveTrigger> triggers = activeTriggers.get(hasTriggersEntity);
        triggers.addEnum(new ActiveTrigger(hasTriggersEntity, triggerBaseEntity, trigger, input));
    }*/

    /*public SpiDevice spiOpen(SpiChannel channel, int speed, SpiMode mode) throws IOException {
        return SpiFactory.getInstance(channel, speed, mode);
    }*/

  /*public void delay(int howLong) {
    Gpio.delay(howLong);
  }

  public byte[] spiXfer(SpiDevice handle, byte txByte) throws IOException {
    return handle.write(txByte);
  }

  public int spiXfer(SpiDevice handle, byte[] txBytes, byte[] data) throws IOException {
    byte[] bytes = handle.write(txBytes);
    for (int i = 0; i < data.length; i++) {
      if (bytes.length < i) {
        break;
      }
      data[i] = bytes[i];
    }
    return bytes.length;
  }*/

  /*public Set<String> getIButtons() throws IOException {
    return Files.list(w1BaseDir)
        .map(path -> path.getFileName().toString())
        .filter(path -> path.startsWith("01-")).collect(Collectors.toSet());
  }*/

  public Float getDS18B20Value(String sensorID) {
    MutablePair<Long, Float> pair = ds18B20Values.get(sensorID);
    if (pair != null) {
      if (System.currentTimeMillis() - pair.getKey() < entity.getOneWireInterval() * 1000L) {
        return pair.getValue();
      }
    } else {
      pair = MutablePair.of(-1L, -1F);
      ds18B20Values.put(sensorID, pair);
    }
    pair.setLeft(System.currentTimeMillis());

    List<String> rawDataAsLines = getRawDataAsLines(sensorID);
    float value = -1;
    if (rawDataAsLines != null) {
      String line = rawDataAsLines.get(1);
      value = Float.parseFloat(line.substring(line.indexOf("t=") + "t=".length())) / 1000;
    }
    pair.setValue(value);

    return pair.getValue();
  }

  private List<String> getRawDataAsLines(String sensorID) {
    if (EntityContext.isDevEnvironment()) {
      Random r = new Random(System.currentTimeMillis());
      return Arrays.asList("", "sd sd sd sd ff zz cc vv aa t=" + (10000 + r.nextInt(40000)));
    }

    Path path = w1BaseDir.resolve(sensorID).resolve("w1_slave");
    try {
      return FileUtils.readLines(path.toFile(), Charset.defaultCharset());
    } catch (IOException e) {
      log.error("Error while get RawData for sensor with id: " + sensorID);
      return null;
    }
  }

  @SneakyThrows
  public List<String> getDS18B20() {
    if (EntityContext.isDevEnvironment()) {
      return Collections.singletonList("28-test000011");
    }
    if (SystemUtils.IS_OS_LINUX && Files.exists(w1BaseDir.resolve("w1_master_slaves"))) {
      return Files.readAllLines(w1BaseDir.resolve("w1_master_slaves")).stream()
          .filter(sensorID -> sensorID != null && sensorID.startsWith("28-"))
          .collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }

/*
  public GpioPin getGpioPin(GpioPin gpioPin) {
    return GpioFactory.getInstance().getProvisionedPin(gpioPin.getPin());
  }
*/

  @Override
  public void entityUpdated(GpioEntity entity) {
    this.entity = entity;
    createOrUpdateGpioPins(entity);
  }

  @Override
  public void destroy() {

  }

  @Override
  public void testService() {

  }

  private void createOrUpdateGpioPins(GpioEntity entity) {
    Set<GpioPinEntity> gpioPinEntities = entity.getGpioPinEntities();
    for (GpioPinEntity gpioPin : gpioPinEntities) {
      createOrUpdateState(gpioPin.getGpioPin(), gpioPin.getMode(), gpioPin.getPull());
    }
  }

  private synchronized void createOrUpdateState(@NotNull GpioPin gpioPin, @NotNull PinMode mode, @Nullable PullResistance pull) {
    GpioState gpioState = state.get(gpioPin.getAddress());
    boolean chaned = gpioState != null && (gpioState.getPinMode() != mode || gpioState.getPull() != pull);
    if (gpioState == null || chaned) {
      if (chaned) {
        log.debug("Shutdown pin: <{}>" + gpioState.getGpioPin().getName());
        gpioState.getInstance().shutdown(pi4j);
        DefaultContext defaultContext = (DefaultContext) pi4j;
        defaultContext.shutdown(gpioState.getInstance().id());
      }
      gpioState = new GpioState(log, gpioPin, mode, pull);
      mode.getGpioModeFactory().createGpioState(pi4j, gpioState, entity.getGpioProvider());
      log.info("Created gpio interface: {}", gpioState);
      state.put(gpioPin.getAddress(), gpioState);
    }
  }
}
