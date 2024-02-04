
package org.bleachhack.module.mods.ranchedMods;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.bleachhack.event.events.EventClientMove;
import org.bleachhack.event.events.EventTick;
import org.bleachhack.eventbus.BleachSubscribe;
import org.bleachhack.module.Module;
import org.bleachhack.module.ModuleCategory;
import org.bleachhack.setting.module.SettingSlider;

public class MovelessMove extends Module {

	public MovelessMove() {
		super("MovelessMove", KEY_UNBOUND, ModuleCategory.RANCH, "Move without triggering playermove",
				new SettingSlider("Ticks", 1, 10, 2, 0).withDesc("Ticks between moves."),
				new SettingSlider("Distance", 50, 100, 70, 0).withDesc("How far to TP when resetting lastpos"));
	}

	@Override
	public void onDisable(boolean inWorld) {
		super.onDisable(inWorld);
	}

	int timer = getSetting(0).asSlider().getValueInt();
	int dist = getSetting(1).asSlider().getValueInt();

	@BleachSubscribe
	public void onClientMove(EventClientMove event) {
		event.setCancelled(true);
	}

	int kickTimer;

	@BleachSubscribe
	public void onTick(EventTick event) {

		double x = 0;
		double y = 0;
		double z = 0;

		double headYaw = mc.player.getHeadYaw();

		double change = .062;

		if ((headYaw < -45 && headYaw > -135) || (headYaw < 135 && headYaw > 45)) {
			if (mc.options.forwardKey.isPressed()) {
				x = change;
			} else if (mc.options.backKey.isPressed()) {
				x = -change;
			} else if (mc.options.leftKey.isPressed()) {
				z = -change;
			} else if (mc.options.rightKey.isPressed()) {
				z = change;
			} else if (mc.options.jumpKey.isPressed()) {
				y = change;
			} else if (mc.options.sneakKey.isPressed()) {
				y = -change;
			}
		} else {
			if (mc.options.forwardKey.isPressed()) {
				z = change;
			} else if (mc.options.backKey.isPressed()) {
				z = -change;
			} else if (mc.options.leftKey.isPressed()) {
				x = change;
			} else if (mc.options.rightKey.isPressed()) {
				x = -change;
			} else if (mc.options.jumpKey.isPressed()) {
				y = change;
			} else if (mc.options.sneakKey.isPressed()) {
				y = -change;
			}
		}

		if ((headYaw < -135 && headYaw > -180) || (headYaw < 180 && headYaw > 45)) {
			x = -x;
			z = -z;
		}

		kickTimer++;

		if (timer == getSetting(0).asSlider().getValueInt()) {
			mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + dist, mc.player.getZ(), true));
		}
		timer--;
		if (timer <= 0 && kickTimer < 60) {
			timer = getSetting(0).asSlider().getValueInt();
			mc.player.setPos(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z);
		}
		if (kickTimer > 60) {
			mc.player.setPos(mc.player.getX() + x, mc.player.getY() - change, mc.player.getZ() + z);
			kickTimer = 0;
		}
	}
}
