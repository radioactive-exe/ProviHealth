package com.provismet.provihealth.interfaces;

import com.provismet.provihealth.util.HealthContainer;
import net.minecraft.text.Text;

import java.util.List;

public interface IMixinEntityRenderState {
    void provi_Health$setShouldRenderHealth (boolean value);
    void provi_Health$setHealth (float value);
    void provi_Health$setMountHealth (float value);
    void provi_Health$setIsLiving (boolean value);
    void provi_Health$setTitles (List<Text> titles);
    void provi_Health$setShouldRenderLabel (boolean value);

    boolean provi_Health$shouldRenderHealth ();
    HealthContainer provi_Health$getHealth ();
    HealthContainer provi_Health$getMountHealth ();
    boolean provi_Health$isLiving ();
    List<Text> provi_Health$getTitles ();
    boolean provi_Health$shouldRenderLabel ();
}
