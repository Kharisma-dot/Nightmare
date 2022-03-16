package net.minecraft.block;

import net.minecraft.util.BlockPos;

public class BlockEventData
{
    private BlockPos position;
    private Block blockType;

    /** Different for each blockID */
    private int eventID;
    private int eventParameter;

    public BlockEventData(BlockPos pos, Block blockType, int eventId, int param)
    {
        this.position = pos;
        this.eventID = eventId;
        this.eventParameter = param;
        this.blockType = blockType;
    }

    public BlockPos getPosition()
    {
        return this.position;
    }

    /**
     * Get the Event ID (different for each BlockID)
     */
    public int getEventID()
    {
        return this.eventID;
    }

    public int getEventParameter()
    {
        return this.eventParameter;
    }

    public Block getBlock()
    {
        return this.blockType;
    }

    public boolean equals(Object object)
    {
        if (!(object instanceof BlockEventData))
            return false;
        else
        {
            BlockEventData blockeventdata = (BlockEventData)object;
            return this.position.equals(blockeventdata.position) && this.eventID == blockeventdata.eventID && this.eventParameter == blockeventdata.eventParameter && this.blockType == blockeventdata.blockType;
        }
    }

    public String toString()
    {
        return "TE(" + this.position + ")," + this.eventID + "," + this.eventParameter + "," + this.blockType;
    }
}
