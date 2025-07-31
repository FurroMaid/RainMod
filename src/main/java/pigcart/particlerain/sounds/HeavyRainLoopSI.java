package pigcart.particlerain.sounds;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class HeavyRainLoopSI extends AbstractTickableSoundInstance {
    private boolean fadeOut;
    private int fadeTicks;

    public HeavyRainLoopSI(SoundEvent soundEvent, float volume, float pitch, boolean loopable) {
        super(soundEvent, SoundSource.WEATHER, RandomSource.create());
        this.volume = volume;
        this.pitch = pitch;
        this.looping = loopable;
        this.delay = 0;
    }

    public void setVolume(float setvolume) {
        this.volume = setvolume;
    }


    @Override
    public void tick() {
        if (fadeOut) {
            fadeTicks++;
            volume -= 0.05f;
            if (volume <= 0.01f) {
                volume = 0f;
                fadeOut = false;
            }
        }
    }

    public void startFadeOut() {
        fadeOut = true;
        fadeTicks = 0;
    }
}
