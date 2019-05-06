package com.minecolonies.coremod.entity.pathfinding;

import com.minecolonies.coremod.entity.pathfinding_old.PathNavigate;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class Navigator extends PathNavigate
{
    /**
     * Instantiates the navigation of an ourEntity.
     *
     * @param entity the ourEntity.
     * @param world  the world it is in.
     */
    public Navigator(@NotNull final EntityLiving entity, final World world)
    {
        super(entity, world);
    }


}
