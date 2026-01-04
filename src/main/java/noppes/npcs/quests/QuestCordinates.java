package noppes.npcs.quests;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;// Внутренний класс для хранения координат
public class QuestCordinates {
    public int dimension;
    public double x, y, z;
    public double range = 10.0;
    public String name = "";
    public int color = 0x00FF00; // Зеленый

    public QuestCordinates() {}

    public QuestCordinates(double x, double y, double z, int dimension) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;

    }

    public NBTTagCompound toNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setDouble("NavX", x);
        compound.setDouble("NavY", y);

        compound.setDouble("NavZ", z);
        compound.setInteger("NavDimension", dimension);
        compound.setDouble("NavRange", range);
        compound.setString("Name", name);
        compound.setInteger("Color", color);
        return compound;
    }

    public static QuestCordinates fromNBT(NBTTagCompound compound) {
        QuestCordinates coord = new QuestCordinates();
        coord.x = compound.getDouble("NavX");
        coord.y = compound.getDouble("NavY");
        coord.z = compound.getDouble("NavZ");
        coord.dimension = compound.getInteger("NavDimension");
        coord.range = compound.getDouble("NavRange");
        coord.name = compound.getString("Name");
        coord.color = compound.getInteger("Color");
        return coord;
    }

    public double getDistanceTo(EntityPlayer player) {
        if (player.dimension != this.dimension) {
            return Double.MAX_VALUE;
        }
        double dx = x - player.posX;
        double dy = y - (player.posY + player.getEyeHeight());
        double dz = z - player.posZ;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

}