package me.zimzaza4.morebows.item.bows;

import cn.nukkit.Nukkit;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import me.zimzaza4.morebows.item.CustomBowBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AutomaticBow extends CustomBowBase {
    public AutomaticBow() {
        super("morebows:automatic_bow", "连发弓", "automatic_bow");
    }

    @Override
    public boolean onRelease(Player player, int ticksUsed) {
        boolean shoot = findAndShoot(player, ticksUsed);
        if (shoot) {
            Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {
                int t = 0;

                @Override
                public void onRun(int i) {
                    t++;
                    if (t > 5) {
                        this.cancel();
                    }
                    findAndShoot(player, ticksUsed);
                }
            }, 10);
        }
        return shoot;
    }
}
