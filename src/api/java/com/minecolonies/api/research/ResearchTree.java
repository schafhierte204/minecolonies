package com.minecolonies.api.research;

import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.minecolonies.api.util.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.minecolonies.api.research.ResearchConstants.TAG_RESEARCH_TREE;

/**
 * The class which contains all research.
 */
public class ResearchTree
{
    /**
     * The map containing all researches by ID.
     */
    private static final Map<String, IResearch> researchTree      = new HashMap<>();

    /**
     * Get a research by id.
     * @param id the id of the research.
     * @return the IResearch object.
     */
    public static IResearch getResearch(final String id)
    {
        return researchTree.get(id);
    }

    /**
     * Add a research to the tree.
     * @param research the research to add.
     */
    public static void addResearch(final IResearch research)
    {
        researchTree.put(research.getId(), research);
    }

    /**
     * Write the research tree to NBT.
     * @return the compound.
     */
    public static NBTTagCompound writeToNBT()
    {
        final NBTTagCompound compound = new NBTTagCompound();
        @NotNull final NBTTagList citizenTagList = researchTree.values().stream().map(research -> StandardFactoryController.getInstance().serialize(research)).collect(NBTUtils.toNBTTagList());
        compound.setTag(TAG_RESEARCH_TREE, citizenTagList);
        return compound;
    }

    /**
     * Read the research tree from NBT.
     * @param compound the compound to read it from.
     */
    public static void readFromNBT(final NBTTagCompound compound)
    {
        researchTree.putAll(NBTUtils.streamCompound(compound.getTagList(TAG_RESEARCH_TREE, Constants.NBT.TAG_COMPOUND))
                          .map(researchCompound -> (IResearch) StandardFactoryController.getInstance().deserialize(researchCompound))
                          .collect(Collectors.toMap(IResearch::getId, iResearch -> iResearch)));
    }
}
