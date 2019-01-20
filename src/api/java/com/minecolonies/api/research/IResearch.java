package com.minecolonies.api.research;

import com.minecolonies.api.crafting.ItemStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface defining how a research is.
 */
public interface IResearch
{
    /**
     * Check if this research can be executed at this moment.
     * @param uni_level the level of the university.
     * @return true if so.
     */
    boolean canResearch(int uni_level);

    /**
     * Check if this research can be displayed in the GUI.
     * @param uni_level the level of the university.
     * @return true if so.
     */
    boolean canDisplay(int uni_level);

    /**
     * Load the cost for the research from the configuration file.
     */
    void loadCostFromConfig();

    /**
     * Check whether all resources are available to execute the research.
     * @param inventory the inventory to check in.
     * @return true if so
     */
    boolean hasEnoughResources(final IItemHandler inventory);

    /**
     * Start the research.
     * @param player the player starting it.
     */
    void startResearch(@NotNull EntityPlayer player);

    /**
     * Tick the research to execute it.
     */
    void research();

    /**
     * Getter for the progress of the research.
     * @return the progress an int between 0-100.
     */
    int getProgress();

    /**
     * Human readable description of research.
     * @return the description.
     */
    String getDesc();

    /**
     * Getter of the id of the research.
     * @return the String id.
     */
    String getId();

    /**
     * Get the ResearchState of the research.
     * @return the current state.
     */
    ResearchState getState();

    String getParent();

    String getBranch();

    int getDepth();

    List<ItemStorage> getCostList();

    void setState(ResearchState value);

    void setProgress(int progress);

    void setCostList(List<ItemStorage> list);
}
