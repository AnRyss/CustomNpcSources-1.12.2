package noppes.npcs.client.gui;



import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.client.gui.util.*;
import noppes.npcs.quests.QuestCordinates;
import noppes.npcs.quests.QuestInterface;

public class SubGuiEditCoordinate extends SubGuiInterface {
    private QuestCordinates coord;
    private GuiNpcTextField nameField, xField, yField, zField, rangeField;

    public SubGuiEditCoordinate(GuiScreen parent, QuestCordinates coord) {
        this.parent = parent;
        this.coord = coord;
        setBackground("menubg.png");
        xSize = 320;
        ySize = 200;
        closeOnEsc = true;
    }


    @Override
    public void initGui() {
        super.initGui();
      //  addTextField(new GuiNpcTextField(55,parent,fontRenderer,guiLeft + 100,guiTop +10,10,""));
        addLabel(new GuiNpcLabel(0, "Редактирование координаты", guiLeft + 100, guiTop + 4));

        int yPos = guiTop + 30;

        // Название
        addLabel(new GuiNpcLabel(1, "Название:", guiLeft + 10, yPos + 5));
        nameField = new GuiNpcTextField(101, this, fontRenderer,
                guiLeft + 70, yPos, 240, 20, coord.name);
        addTextField(nameField);

        yPos += 30;

        // Координаты
        addLabel(new GuiNpcLabel(2, "X:", guiLeft + 10, yPos + 5));
        xField = new GuiNpcTextField(102, this, fontRenderer,
                guiLeft + 30, yPos, 70, 20, String.format(java.util.Locale.US, "%.2f", coord.x));
        xField.numbersOnly = true;
        xField.setCanLoseFocus(false);
        xField.setFocused(true);
        addTextField(xField);

        addLabel(new GuiNpcLabel(3, "Y:", guiLeft + 110, yPos + 5));
        yField = new GuiNpcTextField(103, this, fontRenderer,
                guiLeft + 130, yPos, 70, 20, String.format(java.util.Locale.US, "%.2f", coord.y));
        yField.numbersOnly = true;
        yField.setCanLoseFocus(false);
        addTextField(yField);
        addLabel(new GuiNpcLabel(4, "Z:", guiLeft + 210, yPos + 5));
        zField = new GuiNpcTextField(104, this, fontRenderer,
                guiLeft + 230, yPos, 70, 20, String.format(java.util.Locale.US, "%.2f", coord.z));
        zField.numbersOnly = true;
        zField.setCanLoseFocus(false);
        addTextField(zField);
        yPos += 30;


        // Радиус
        addLabel(new GuiNpcLabel(5, "Радиус завершения:", guiLeft + 10, yPos + 5));
        rangeField = new GuiNpcTextField(105, this, fontRenderer,
                guiLeft + 130, yPos, 70, 20, String.format(java.util.Locale.US, "%.2f",coord.range));
        rangeField.numbersOnly = true;
        addTextField(rangeField);
        yPos += 30;

        // Измерение
        addLabel(new GuiNpcLabel(6, "Измерение:", guiLeft + 10, yPos + 5));
        addButton(new GuiNpcButton(106, guiLeft + 80, yPos, 100, 20,
                new String[]{"Обычный мир", "Ад", "Энд"},
                coord.dimension == 0 ? 0 : coord.dimension == -1 ? 1 : 2));

        // Сохранить/Отмена
        addButton(new GuiNpcButton(66, guiLeft + xSize - 80, guiTop + ySize - 24, 80, 20, "gui.done"));
        addButton(new GuiNpcButton(67, guiLeft + 10, guiTop + ySize - 24, 80, 20, "gui.cancel"));
    }



    @Override
    protected void actionPerformed(GuiButton guibutton) {
        super.actionPerformed(guibutton);

        GuiNpcButton button = (GuiNpcButton) guibutton;

        if (button.id == 106) {
            switch (button.getValue()) {
                case 0: coord.dimension = 0; break;
                case 1: coord.dimension = -1; break;
                case 2: coord.dimension = 1; break;
            }
        }
        else if (button.id == 66) {
            save();
            close();
        }
        else if (button.id == 67) {
            close();
        }
    }

    @Override
    public void save() {
        if (coord == null || nameField == null || xField == null) return;
        coord.name = nameField.getText();

        try {
            coord.x = Double.parseDouble(xField.getText().replace(",", "."));
            coord.y = Double.parseDouble(yField.getText().replace(",", "."));
            coord.z = Double.parseDouble(zField.getText().replace(",", "."));
            coord.range = Double.parseDouble(rangeField.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            // Оставляем старые значения

        }
    }
}