package pigcart.particlerain.sounds;

import net.minecraft.client.Minecraft;
import org.joml.Math;
import pigcart.particlerain.ParticleRainClient;

import static pigcart.particlerain.ParticleRainClient.*;

public class SoundHandler {

    public static void onTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.player != null) {
            boolean isRaining = mc.level.isRaining();
            if (isRaining) {
//                System.out.println("RainSize: " + rainTicks / 100F * 3F + 0.5F);
//                System.out.println("RainGravity: " + rainTicks / 100F + 0.5F);
                updateRainSounds(mc);
            }else if (ParticleRainClient.wasRaining) {
                rainEnd();
            }
        }
    }


    // World's most janky ass system, but it works for now.

    private static LightRainLoopSI activeLightSound;
    private static HeavyRainLoopSI activeHeavySound;
    private static RumblingLoopSI activeRumblingSound;

    private static void rainStart() {

    }
    private static void rainEnd() {
        if (activeLightSound != null) {
            activeLightSound.startFadeOut();
        }
        if (activeHeavySound != null) {
            activeHeavySound.startFadeOut();
        }
        if (activeRumblingSound != null) {
            activeRumblingSound.startFadeOut();
        }
    }

    private static void updateRainSounds(Minecraft client) {
        boolean isRaining = client.level.isRaining();
        if (client.level != null && isRaining && ParticleRainClient.rainTicks <= 750 && ParticleRainClient.rainTicks >= 550) {
            float rainTickDelay = ParticleRainClient.rainTicks - 550;
            float rainIntensity = Math.min(2.0F, rainTickDelay / 200F);

            if (!client.getSoundManager().isActive(activeRumblingSound)) {
                activeRumblingSound = new RumblingLoopSI(WEATHER_RUMBLE1, 0.015F, 1.0F, true);
                client.getSoundManager().play(activeRumblingSound);
                //System.out.println("Started Rumbling Sound");
            } else {
                activeRumblingSound.setVolume(rainIntensity * 0.5F);
                System.out.println("RU-Volume: " + activeRumblingSound.getVolume());
            }
        } else if (client.level != null && isRaining && ParticleRainClient.rainTicks >= 750) {
            float rainTickDelay = ParticleRainClient.rainTicks - 750;
            float rainIntensity = Math.min(2.0F, rainTickDelay / 200F);

            System.out.println("R-Intensity: " + rainIntensity + " | R-Ticks: " + rainTickDelay);

            if (!client.getSoundManager().isActive(activeLightSound) && rainIntensity < 1.0F) {
                activeLightSound = new LightRainLoopSI(WEATHER_LIGHTRAIN1, 0.015F, 1.0F, true);
                client.getSoundManager().play(activeLightSound);
                System.out.println("Started Light Rain Sound");
            } else if (rainIntensity < 1.0F) {
                activeLightSound.setVolume(rainIntensity * 0.5F);
                System.out.println("LR-Volume: " + activeLightSound.getVolume());
            }

            if (!client.getSoundManager().isActive(activeHeavySound) && rainIntensity > 0.995F) {
                activeHeavySound = new HeavyRainLoopSI(WEATHER_HEAVYRAIN1, 0.015F, 1.0F, true);
                client.getSoundManager().play(activeHeavySound);
                System.out.println("Started Heavy Rain Sound");
            } else if (rainIntensity > 0.995F) {
                activeHeavySound.setVolume(rainIntensity * 0.5F - 0.5F);
                activeLightSound.setVolume(Math.max(0.0f, activeLightSound.getVolume() - 0.005f));
                System.out.println("LR-Volume: " + activeLightSound.getVolume()+" | HR-Volume: " + activeHeavySound.getVolume());
            }
        }
    }
}
