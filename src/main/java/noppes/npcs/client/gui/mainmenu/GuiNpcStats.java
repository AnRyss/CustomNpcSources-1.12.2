package noppes.npcs.client.gui.mainmenu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.SubGuiNpcMeleeProperties;
import noppes.npcs.client.gui.SubGuiNpcProjectiles;
import noppes.npcs.client.gui.SubGuiNpcRangeProperties;
import noppes.npcs.client.gui.SubGuiNpcResistanceProperties;
import noppes.npcs.client.gui.SubGuiNpcRespawn;
import noppes.npcs.client.gui.util.GuiButtonBiDirectional;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataAI;
import noppes.npcs.entity.data.DataDisplay;
import noppes.npcs.entity.data.DataInventory;
import noppes.npcs.entity.data.DataStats;

public class GuiNpcStats
extends GuiNPCInterface2
implements ITextfieldListener, IGuiData {
	
	private DataAI ais;
	private DataDisplay display;
	private DataInventory inventory;
	private DataStats stats;

	public GuiNpcStats(EntityNPCInterface npc) {
		super(npc, 2);
		this.stats = npc.stats;
		this.display = npc.display;
		this.ais = npc.ais;
		this.inventory = npc.inventory;
		Client.sendData(EnumPacketServer.MainmenuStatsGet);
		// New
		Client.sendData(EnumPacketServer.MainmenuDisplayGet);
		Client.sendData(EnumPacketServer.MainmenuAIGet);
		Client.sendData(EnumPacketServer.MainmenuInvGet);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		switch (button.id) {
			case 0: {
				this.setSubGui(new SubGuiNpcRespawn(this.stats));
				break;
			}
			case 2: {
				this.setSubGui(new SubGuiNpcMeleeProperties(this.stats.melee));
				break;
			}
			case 3: {
				this.setSubGui(new SubGuiNpcRangeProperties(this.stats));
				break;
			}
			case 4: {
				this.stats.immuneToFire = (button.getValue() == 1);
				break;
			}
			case 5: {
				this.stats.canDrown = (button.getValue() == 1);
				break;
			}
			case 6: {
				this.stats.burnInSun = (button.getValue() == 1);
				break;
			}
			case 7: {
				this.stats.noFallDamage = (button.getValue() == 1);
				break;
			}
			case 8: {
				this.stats.creatureType = EnumCreatureAttribute.values()[button.getValue()];
				break;
			}
			case 9: {
				this.setSubGui(new SubGuiNpcProjectiles(this.stats.ranged));
				break;
			}
			case 15: {
				this.setSubGui(new SubGuiNpcResistanceProperties(this.stats.resistances));
				break;
			}
			case 17: {
				this.stats.potionImmune = ((GuiNpcButtonYesNo) guibutton).getBoolean();
				break;
			}
			case 22: {
				this.stats.ignoreCobweb = (button.getValue() == 0);
				break;
			}
			case 40: {
				this.save();
				break;
			}
		}
	}

	// New
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (!CustomNpcs.showDescriptions) { return; }
		if (this.getTextField(0)!=null && this.getTextField(0).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.max.health").getFormattedText());
		} else if (this.getTextField(1)!=null && this.getTextField(1).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.aggro").getFormattedText());
		} else if (this.getTextField(14)!=null && this.getTextField(14).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.health.regen").getFormattedText());
		} else if (this.getTextField(16)!=null && this.getTextField(16).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.health.combat").getFormattedText());
		} else if (this.getButton(0)!=null && this.getButton(0).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.respawn").getFormattedText());
		} else if (this.getButton(2)!=null && this.getButton(2).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.melee").getFormattedText());
		} else if (this.getButton(3)!=null && this.getButton(3).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.range").getFormattedText());
		} else if (this.getButton(4)!=null && this.getButton(4).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.resist.fire").getFormattedText());
		} else if (this.getButton(5)!=null && this.getButton(5).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.water").getFormattedText());
		} else if (this.getButton(6)!=null && this.getButton(6).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.resist.sun").getFormattedText());
		} else if (this.getButton(7)!=null && this.getButton(7).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.fall").getFormattedText());
		} else if (this.getButton(8)!=null && this.getButton(8).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.type").getFormattedText());
		} else if (this.getButton(9)!=null && this.getButton(9).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.arrow").getFormattedText());
		} else if (this.getButton(15)!=null && this.getButton(15).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.resists").getFormattedText());
		} else if (this.getButton(17)!=null && this.getButton(17).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.potion").getFormattedText());
		} else if (this.getButton(22)!=null && this.getButton(22).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.web").getFormattedText());
		} else if (this.getButton(44)!=null && this.getButton(44).isMouseOver()) {
			this.setHoverText(new TextComponentTranslation("stats.hover.battle").getFormattedText());
		}
	}

    @Override
	public void initGui() {
		super.initGui();
		int y = this.guiTop + 10;
		this.addLabel(new GuiNpcLabel(0, "stats.health", this.guiLeft + 5, y + 5));
		this.addTextField(new GuiNpcTextField(0, this, this.guiLeft + 85, y, 50, 18, this.stats.maxHealth + ""));
		this.getTextField(0).numbersOnly = true;
		this.getTextField(0).setMinMaxDefault(0, Integer.MAX_VALUE, 20);
		this.addLabel(new GuiNpcLabel(1, "stats.aggro", this.guiLeft + 275, y + 5));
		this.addTextField(new GuiNpcTextField(1, this, this.fontRenderer, this.guiLeft + 355, y, 50, 18,
				this.stats.aggroRange + ""));
		this.getTextField(1).numbersOnly = true;
		this.getTextField(1).setMinMaxDefault(1, 64, 2);

		this.addLabel(new GuiNpcLabel(34, "stats.creaturetype", this.guiLeft + 140, y + 5));
		this.addButton(new GuiButtonBiDirectional(8, this.guiLeft + 217, y, 56, 20,
				new String[] { "stats.normal", "stats.undead", "stats.arthropod" }, this.stats.creatureType.ordinal()));
		((GuiButtonBiDirectional) this.getButton(8)).cheakWidth = false;
		y += 22;
		this.addButton(new GuiNpcButton(0, this.guiLeft + 82, y, 56, 20, "selectServer.edit"));
		this.addLabel(new GuiNpcLabel(2, "stats.respawn", this.guiLeft + 5, y + 5));
		y += 22;
		this.addButton(new GuiNpcButton(2, this.guiLeft + 82, y, 56, 20, "selectServer.edit"));
		this.addLabel(new GuiNpcLabel(5, "stats.meleeproperties", this.guiLeft + 5, y + 5));
		y += 22;
		this.addButton(new GuiNpcButton(3, this.guiLeft + 82, y, 56, 20, "selectServer.edit"));
		this.addLabel(new GuiNpcLabel(6, "stats.rangedproperties", this.guiLeft + 5, y + 5));
		this.addButton(new GuiNpcButton(9, this.guiLeft + 217, y, 56, 20, "selectServer.edit"));
		this.addLabel(new GuiNpcLabel(7, "stats.projectileproperties", this.guiLeft + 140, y + 5));
		y += 34;
		this.addButton(new GuiNpcButton(15, this.guiLeft + 82, y, 56, 20, "selectServer.edit"));
		this.addLabel(new GuiNpcLabel(15, "effect.resistance", this.guiLeft + 5, y + 5));
		y += 34;
		this.addButton(new GuiNpcButton(4, this.guiLeft + 82, y, 56, 20, new String[] { "gui.no", "gui.yes" },
				(this.stats.immuneToFire ? 1 : 0)));
		this.addLabel(new GuiNpcLabel(10, "stats.fireimmune", this.guiLeft + 5, y + 5));
		this.addButton(new GuiNpcButton(5, this.guiLeft + 217, y, 56, 20, new String[] { "gui.no", "gui.yes" },
				(this.stats.canDrown ? 1 : 0)));
		this.addLabel(new GuiNpcLabel(11, "stats.candrown", this.guiLeft + 140, y + 5));
		this.addTextField(new GuiNpcTextField(14, this, this.guiLeft + 355, y, 56, 20, this.stats.healthRegen + "")
				.setNumbersOnly());
		this.addLabel(new GuiNpcLabel(14, "stats.regenhealth", this.guiLeft + 275, y + 5));
		y += 22;
		this.addTextField(new GuiNpcTextField(16, this, this.guiLeft + 355, y, 56, 20, this.stats.combatRegen + "").setNumbersOnly());
		this.addLabel(new GuiNpcLabel(16, "stats.combatregen", this.guiLeft + 275, y + 5));
		this.addButton(new GuiNpcButton(6, this.guiLeft + 82, y, 56, 20, new String[] { "gui.no", "gui.yes" },
				(this.stats.burnInSun ? 1 : 0)));
		this.addLabel(new GuiNpcLabel(12, "stats.burninsun", this.guiLeft + 5, y + 5));
		this.addButton(new GuiNpcButton(7, this.guiLeft + 217, y, 56, 20, new String[] { "gui.no", "gui.yes" },
				(this.stats.noFallDamage ? 1 : 0)));
		this.addLabel(new GuiNpcLabel(13, "stats.nofalldamage", this.guiLeft + 140, y + 5));
		y += 22;
		this.addButton(new GuiNpcButtonYesNo(17, this.guiLeft + 82, y, 56, 20, this.stats.potionImmune));
		this.addLabel(new GuiNpcLabel(17, "stats.potionImmune", this.guiLeft + 5, y + 5));
		this.addLabel(new GuiNpcLabel(22, "ai.cobwebAffected", this.guiLeft + 140, y + 5));
		this.addButton(new GuiNpcButton(22, this.guiLeft + 217, y, 56, 20, new String[] { "gui.no", "gui.yes" },
				(this.stats.ignoreCobweb ? 0 : 1)));
		//this.getButton(40).setEnabled(false);
		this.addLabel(new GuiNpcLabel(42, "stats.level", this.guiLeft + 139, this.guiTop + 37));
		this.addButton(new GuiNpcButton(43, this.guiLeft + 217, this.guiTop + 54, 56, 20,
				new String[] { "stats.rarity.normal", "stats.rarity.elite", "stats.rarity.boss" },
				this.stats.getRarity())); // rarity
		this.addLabel(new GuiNpcLabel(44, "stats.rarity", this.guiLeft + 140, this.guiTop + 61));

		this.addLabel(new GuiNpcLabel(45, "stats.calmdown", this.guiLeft + 275, this.guiTop + 40));
		this.addButton(new GuiNpcButton(44, this.guiLeft + 355, this.guiTop + 37, 50, 20,
				new String[] { "gui.no", "gui.yes" }, (this.stats.calmdown ? 1 : 0)));
	}

	@Override
	public void save() {
		Client.sendData(EnumPacketServer.MainmenuStatsSave, this.stats.writeToNBT(new NBTTagCompound()));
		// New
		Client.sendData(EnumPacketServer.MainmenuDisplaySave, this.display.writeToNBT(new NBTTagCompound()));
		Client.sendData(EnumPacketServer.MainmenuAISave, this.ais.writeToNBT(new NBTTagCompound()));
		Client.sendData(EnumPacketServer.MainmenuInvSave, this.inventory.writeEntityToNBT(new NBTTagCompound()));
	}

    @Override
	public void setGuiData(NBTTagCompound compound) {
		if (compound.hasKey("CreatureType", 3)) {
			this.stats.readToNBT(compound);
		} // Change
			// New
		else if (compound.hasKey("MarkovGeneratorId", 3)) {
			this.display.readToNBT(compound);
		} else if (compound.hasKey("NpcInv", 9)) {
			this.inventory.readEntityFromNBT(compound);
		} else if (compound.hasKey("MovementType", 3)) {
			this.ais.readToNBT(compound);
		}
		this.initGui();
	}

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if (textfield.getId() == 0) {
			this.stats.maxHealth = textfield.getInteger();
			this.npc.heal((float) this.stats.maxHealth);
		} else if (textfield.getId() == 1) {
			this.stats.aggroRange = textfield.getInteger();
		} else if (textfield.getId() == 14) {
			this.stats.healthRegen = textfield.getInteger();
		} else if (textfield.getId() == 16) {
			this.stats.combatRegen = textfield.getInteger();
		}
	}
	
}
