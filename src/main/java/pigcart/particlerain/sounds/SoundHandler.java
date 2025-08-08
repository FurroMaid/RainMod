package pigcart.particlerain.sounds;

import net.minecraft.client.Minecraft;
import org.joml.Math;
import pigcart.particlerain.ParticleRainClient;

import static pigcart.particlerain.ParticleRainClient.*;

public class SoundHandler {

    private static LightRainLoopSI activeLightSound;
    private static HeavyRainLoopSI activeHeavySound;
    private static RumblingLoopSI activeRumblingSound;

    /**
     * Called every client tick to update rain sounds.
     */
    public static void onTick() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level != null && mc.player != null) {
            if (mc.level.isRaining()) {
                updateRainSounds(mc);
            } else if (ParticleRainClient.wasRaining) {
                stopAllRainSounds();
            }
        }
    }

    /**
     * Stops all rain-related sound loops with fade-out.
     */
    private static void stopAllRainSounds() {
        if (activeLightSound != null) {
            activeLightSound.startFadeOut();
            activeLightSound = null;
        }
        if (activeHeavySound != null) {
            activeHeavySound.startFadeOut();
            activeHeavySound = null;
        }
        if (activeRumblingSound != null) {
            activeRumblingSound.startFadeOut();
            activeRumblingSound = null;
        }
    }

    /**
     * Updates rain sounds based on rain tick progression and intensity.
     *
     * Handles transitions between light rain, heavy rain, and rumble.
     */
    private static void updateRainSounds(Minecraft client) {
        if (client.level == null || !client.level.isRaining()) return;

        int rainTicks = ParticleRainClient.rainTicks;

        // Rumbling transition (550-750 ticks)
        if (rainTicks >= 550 && rainTicks <= 850) {
            float rainTickDelay = rainTicks - 550;
            float rainIntensity = Math.min(2.0F, rainTickDelay / 200F);

            if (activeRumblingSound == null || !client.getSoundManager().isActive(activeRumblingSound)) {
                activeRumblingSound = new RumblingLoopSI(WEATHER_RUMBLE1, 0.015F, 1.0F, true);
                client.getSoundManager().play(activeRumblingSound);
            } else {
                activeRumblingSound.setVolume(rainIntensity * 0.5F);
            }
        }
        // Light and heavy rain transition (ticks > 750)
        else if (rainTicks > 850) {
            float rainTickDelay = rainTicks - 750;
            float rainIntensity = Math.min(2.0F, rainTickDelay / 200F);

            // Light Rain
            if (rainIntensity < 1.0F) {
                if (activeLightSound == null || !client.getSoundManager().isActive(activeLightSound)) {
                    activeLightSound = new LightRainLoopSI(WEATHER_LIGHTRAIN1, 0.015F, 1.0F, true);
                    client.getSoundManager().play(activeLightSound);
                }
                activeLightSound.setVolume(rainIntensity * 0.5F);
            }

            // Heavy Rain
            if (rainIntensity > 0.995F) {
                if (activeHeavySound == null || !client.getSoundManager().isActive(activeHeavySound)) {
                    activeHeavySound = new HeavyRainLoopSI(WEATHER_HEAVYRAIN1, 0.015F, 1.0F, true);
                    client.getSoundManager().play(activeHeavySound);
                }
                float heavyVolume = rainIntensity * 0.5F - 0.5F;
                activeHeavySound.setVolume(heavyVolume);

                if (activeLightSound != null) {
                    float lightVolume = Math.max(0.0f, activeLightSound.getVolume() - 0.005f);
                    activeLightSound.setVolume(lightVolume);
                }
            }
        }
    }

    private static void rainStart() {
        // Hook for rain start logic if needed
    }
}
