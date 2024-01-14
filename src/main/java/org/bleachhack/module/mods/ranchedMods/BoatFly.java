
package org.bleachhack.module.mods.ranchedMods;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.bleachhack.event.events.EventTick;
import org.bleachhack.eventbus.BleachSubscribe;
import org.bleachhack.module.Module;
import org.bleachhack.module.ModuleCategory;
import org.bleachhack.setting.module.SettingMode;
import org.bleachhack.setting.module.SettingSlider;
import org.joml.Vector3f;

import static org.bleachhack.util.Max2.max2;

public class BoatFly extends Module {

	public BoatFly() {
		super("BoatFly", KEY_UNBOUND, ModuleCategory.RANCH, "Fly, but in a boat.",
				new SettingSlider("Speed", 0, 5, 1, 1).withDesc("Flight speed."),
				new SettingMode("Mode", "Normal", "Axis").withDesc("Normal is more slippery but fun"),
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
			Vector3f axis = boat.getVelocity().toVector3f();
			Vector3f outV;
			if (mc.options.forwardKey.isPressed()) {
				outV = axis.normalize(multiplier);
			}
			else {
				outV = axis.normalize(0);
			}
			if (mc.options.jumpKey.isPressed()) {
				velY = 1;
			}
			else if (mc.options.sprintKey.isPressed()) {
				velY = -1;
			}
			else {
				velY = 0;
			}
			if (getSetting(1).asMode().getMode() == 0) {
				Vec3d magV = new Vec3d(max2(boat.getVelocity().x, multiplier), 0, max2(boat.getVelocity().z, multiplier));
				Vec3d multiply = magV.multiply(1.1);
				boat.setVelocity(multiply.x, velY * multiplier, multiply.z);
			} else {
				boat.setVelocity(outV.x * multiplier, velY * (multiplier - .5), outV.z * multiplier);
			}
			if (getSetting(2).asMode().getMode() == 1) {
				kickTimer--;
			}

			if (kickTimer <= 0) {
				boat.setVelocity(boat.getVelocity().x, boat.getVelocity().y - .5, boat.getVelocity().z);
				kickTimer = 40;
				//fix falling boat after few seconds of flying
			}
		}
	}
}
