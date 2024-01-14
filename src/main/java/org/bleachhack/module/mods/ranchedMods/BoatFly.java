
package org.bleachhack.module.mods.ranchedMods;

import net.minecraft.entity.Entity;
import org.bleachhack.event.events.EventTick;
import org.bleachhack.eventbus.BleachSubscribe;
import org.bleachhack.module.Module;
import org.bleachhack.module.ModuleCategory;
import org.bleachhack.setting.module.SettingMode;
import org.bleachhack.setting.module.SettingSlider;

public class BoatFly extends Module {

	public BoatFly() {
		super("BoatFly", KEY_UNBOUND, ModuleCategory.RANCH, "Fly, but in a boat.",
				new SettingSlider("Speed", 0, 5, 1, 1).withDesc("Flight speed."),
				new SettingMode("AntiKick", "Off", "On").withDesc("To bypass \"you have been kicked for flying\""));
	}

	@Override
	public void onDisable(boolean inWorld) {

		super.onDisable(inWorld);
	}

	int kickTimer = 40;

	@BleachSubscribe
	public void onTick(EventTick event) {
		float multiplier = getSetting(0).asSlider().getValueFloat();
		if (mc.player != null && mc.player.hasVehicle()) {
			Entity boat = mc.player.getVehicle();
			int velY;
			if (mc.options.jumpKey.isPressed()) {
				velY = 1;
			}
			else {
				velY = 0;
			}

			if (kickTimer > 0) {
				boat.setVelocity(boat.getVelocity().x + multiplier, velY , boat.getVelocity().z + multiplier);
				kickTimer--;
			}
			else if (velY != 0) {
				boat.setVelocity(boat.getVelocity().x, -.05, boat.getVelocity().z);
				kickTimer = 40;
				boat.setVelocity(boat.getVelocity().x, 0, boat.getVelocity().z);
			}
		}
	}
}
