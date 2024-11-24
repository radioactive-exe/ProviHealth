package com.provismet.provihealth.interfaces;

import com.provismet.provihealth.util.HealthContainer;

public interface IMixinLivingEntity {
    HealthContainer provi_Health$getHealthContainer ();
    HealthContainer provi_Health$getMountHealthContainer ();
}
