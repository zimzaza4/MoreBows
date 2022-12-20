package me.zimzaza4.morebows.listeners;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.ProjectileHitEvent;
import me.zimzaza4.morebows.item.data.ArrowMetadata;

public class ProjectileListener implements Listener {
    
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity().hasMetadata("morebows:custom_arrow_data")) {
            e.getEntity().getMetadata("morebows:custom_arrow_data").forEach(data -> {
                if (data instanceof ArrowMetadata ad) {
                    ad.getBow().onHit(e);
                }
            });
        }
    }
}
