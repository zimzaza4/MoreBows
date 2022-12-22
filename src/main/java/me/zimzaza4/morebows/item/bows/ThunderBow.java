package me.zimzaza4.morebows.item.bows;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.entity.weather.EntityLightningStrike;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.level.particle.LavaDripParticle;
import cn.nukkit.scheduler.Task;
import me.zimzaza4.morebows.item.CustomBowBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ThunderBow extends CustomBowBase {
    public ThunderBow() {
        super("morebows:thunder_bow", null, "thunder_bow");
    }

    @Override
    protected void onTick(EntityProjectile projectile) {
        projectile.level.addParticle(new LavaDripParticle(projectile));
    }

    @Override
    public void onHit(ProjectileHitEvent event) {
        new EntityLightning(event.getEntity().getChunk(), EntityLightning.getDefaultNBT(event.getEntity())).spawnToAll();
        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
            @Override
            public void onRun(int i) {
                event.getEntity().kill();
            }
        }, 1);

    }
}
