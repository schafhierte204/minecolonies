package com.minecolonies.api.research;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.Log;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.minecolonies.api.research.ResearchConstants.*;

/**
 * The implementation of the IResearch interface which represents one type of research.
 */
public class Research implements IResearch
{
    /**
     * The costList of the research.
     */
    private final List<ItemStorage> costList   = new ArrayList<>();

    /**
     * The parent research which has to be completed first.
     */
    private final String parent;

    /**
     * The current research state.
     */
    private ResearchState state;

    /**
     * The string id of the research.
     */
    private final String id;

    /**
     * The research branch.
     */
    private final String branch;

    /**
     * The description of the research.
     */
    private final String desc;

    /**
     * The research effect of this research.
     */
    private final IResearchEffect effect;

    /**
     * The depth level in the tree.
     */
    private final int depth;

    /**
     * The progress of the research.
     */
    private int progress;

    /**
     * Create the new research.
     * @param id it's id.
     * @param parent the parent id of it.
     * @param desc it's description text.
     * @param effect it's effect.
     * @param depth the depth in the tree.
     * @param branch the branch it is on.
     */
    public Research(final String id, final String parent, final String branch, final String desc, final int depth, final IResearchEffect effect)
    {
        this.parent = parent;
        this.id = id;
        this.desc = desc;
        this.effect = effect;
        this.depth = depth;
        this.branch = branch;
    }

    @Override
    public boolean canResearch(final int uni_level)
    {
        return state == ResearchState.NOT_STARTED && canDisplay(uni_level) && ResearchTree.getResearch(parent) != null && ResearchTree.getResearch(parent).getState() == ResearchState.FINISHED;
    }

    @Override
    public boolean canDisplay(final int uni_level)
    {
        return uni_level >= depth;
    }

    @Override
    public void loadCostFromConfig()
    {
        costList.clear();
        try
        {
            final String[] researchCost = (String[]) ResearchConfiguration.class.getField(branch).getClass().getField(id).get(new String[0]);
            for (final String cost : researchCost)
            {
                final String[] tuple = cost.split("/*");
                final Item item = Item.getByNameOrId(tuple[0]);
                if (item == null)
                {
                    Log.getLogger().warn("Couldn't retrieve research costList from config for " + branch + "/" + id + " for item: " + tuple[0]);
                    return;
                }
                costList.add(new ItemStorage(new ItemStack(item, 1), Integer.parseInt(tuple[1]), false));
            }
        }
        catch (final NoSuchFieldException | IllegalAccessException | NumberFormatException e)
        {
            Log.getLogger().warn("Couldn't retrieve research costList from config for " + branch + "/" + id + " !", e);
        }
    }

    @Override
    public boolean hasEnoughResources(final IItemHandler inventory)
    {
        for (final ItemStorage cost: costList)
        {
            final int count = InventoryUtils.getItemCountInItemHandler(inventory, stack -> !ItemStackUtils.isEmpty(stack) && stack.isItemEqual(cost.getItemStack()));
            if (count < cost.getAmount())
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public void startResearch(@NotNull final EntityPlayer player)
    {
        if (state == ResearchState.NOT_STARTED && hasEnoughResources(new InvWrapper(player.inventory)))
        {
            state = ResearchState.IN_PROGRESS;
        }
    }

    @Override
    public void research()
    {
        if (state == ResearchState.IN_PROGRESS)
        {
            progress++;
            if (progress >= BASE_RESEARCH_TIME * depth)
            {
                state = ResearchState.FINISHED;
            }
        }
    }

    @Override
    public int getProgress()
    {
        return (BASE_RESEARCH_TIME * depth)/progress;
    }

    @Override
    public String getDesc()
    {
        return this.desc;
    }

    @Override
    public String getId()
    {
        return this.id;
    }

    @Override
    public ResearchState getState()
    {
        return this.state;
    }
}
