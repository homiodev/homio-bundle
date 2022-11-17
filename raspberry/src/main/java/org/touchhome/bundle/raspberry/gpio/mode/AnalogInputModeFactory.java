package org.touchhome.bundle.raspberry.gpio.mode;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.analog.AnalogInput;
import java.util.function.Consumer;
import org.touchhome.bundle.api.exception.ProhibitedExecution;
import org.touchhome.bundle.api.state.DecimalType;
import org.touchhome.bundle.api.state.State;
import org.touchhome.bundle.raspberry.gpio.GpioProviderIdModel;
import org.touchhome.bundle.raspberry.gpio.GpioState;

public class AnalogInputModeFactory implements GpioModeFactory<AnalogInput> {

  @Override
  public void createGpioState(Context pi4j, GpioState gpioState, GpioProviderIdModel gpioProvidersIdModel) {
    gpioState.setInstance(pi4j.create(AnalogInput.newConfigBuilder(pi4j)
            .name(gpioState.getGpioPin().getName())
            .address(gpioState.getGpioPin().getAddress())
            .provider(gpioProvidersIdModel.getAnalogInputProviderId())
            .build())
        .addListener(event -> {
          DecimalType state = new DecimalType(event.value(), event.oldValue());
          if (state.equalToOldValue()) {
            gpioState.setLastState(state);
            for (Consumer<State> listener : gpioState.getListeners().values()) {
              listener.accept(state);
            }
          }
        }));
  }

  @Override
  public State getState(AnalogInput instance) {
    return new DecimalType(instance.value());
  }

  @Override
  public void setState(AnalogInput instance, State state) {
    throw new ProhibitedExecution();
  }
}
