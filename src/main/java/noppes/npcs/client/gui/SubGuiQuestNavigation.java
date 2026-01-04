package noppes.npcs.client.gui;



import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.client.gui.util.*;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.quests.QuestCordinates;

import java.util.ArrayList;
import java.util.List;

public class SubGuiQuestNavigation extends SubGuiInterface implements ICustomScrollListener {
    private Quest quest;
    private GuiCustomScroll scroll;
    private List<QuestCordinates> coordinates;

    public SubGuiQuestNavigation(GuiScreen parent, Quest quest) {
        this.parent = parent;
        this.quest = quest;
        this.coordinates = quest.questInterface.getNavigationCoordinates();
        setBackground("menubg.png");
        xSize = 350;
        ySize = 220;
        closeOnEsc = true;
    }

    @Override
    public void initGui() {
        super.initGui();

        addLabel(new GuiNpcLabel(0, "Навигационные координаты", guiLeft + 100, guiTop + 4));

        // Список координат
        scroll = new GuiCustomScroll(this, 0);
        scroll.setSize(330, 120);
        scroll.guiLeft = guiLeft + 10;
        scroll.guiTop = guiTop + 25;
        updateScrollList();
        addScroll(scroll);

        int yPos = guiTop + 150;

        // Кнопки управления
        addButton(new GuiNpcButton(1, guiLeft + 10, yPos, 100, 20, "gui.add"));
        addButton(new GuiNpcButton(2, guiLeft + 115, yPos, 100, 20, "gui.edit"));
        addButton(new GuiNpcButton(3, guiLeft + 220, yPos, 100, 20, "gui.remove"));

        // Кнопка "Текущая позиция"
        addButton(new GuiNpcButton(4, guiLeft + 10, guiTop + 175, 150, 20, "quest.use.current.pos"));

        // Кнопки сохранить/отмена
        addButton(new GuiNpcButton(66, guiLeft + xSize - 80, guiTop + 175, 80, 20, "gui.done"));
        addButton(new GuiNpcButton(67, guiLeft + 170, guiTop + 175, 80, 20, "gui.cancel"));
    }

    private void updateScrollList() {
        List<String> list = new ArrayList<>();

        if (coordinates == null || coordinates.isEmpty()) {
            list.add("Нет координат");
        } else {
            for (int i = 0; i < coordinates.size(); i++) {
                QuestCordinates coord = coordinates.get(i);
                String entry = String.format("%d. %s (X: %.1f, Y: %.1f, Z: %.1f)",
                        i + 1,
                        coord.name.isEmpty() ? "Без названия" : coord.name,
                        coord.x, coord.y, coord.z);
                list.add(entry);
            }
        }

        scroll.setList(list);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        // ...
        switch (guibutton.id) {
            case 66:
                save();
                close();
                break;
            case 67:
                close();
                break;
            case 1: // Добавить
                QuestCordinates newCoord = new QuestCordinates();
                newCoord.x = 0;
                newCoord.y = 64;
                newCoord.z = 0;
                newCoord.dimension = 0;
                newCoord.range = 10.0;
                newCoord.name = "Новая координата";

                quest.questInterface.addNavigationCoordinate(newCoord);
                updateScrollList();
                break;

            case 3: // Удалить
                if (scroll.hasSelected() && !scroll.getSelected().equals("Нет координат")) {
                    int index = getSelectedIndex();
                    quest.questInterface.removeNavigationCoordinate(index);
                    updateScrollList();
                }
                break;
            case 2: // Редактировать
                if (scroll.hasSelected() && !scroll.getSelected().equals("Нет координат")) {
                    int index = scroll.selected; // лучше использовать scroll.selected напрямую!
                    QuestCordinates coord = coordinates.get(index);
                    setSubGui(new SubGuiEditCoordinate(this, coord));
                }
                break;

            case 4: // Текущая позиция
                EntityPlayer player = Minecraft.getMinecraft().player;
                if (player != null) {
                    QuestCordinates current = new QuestCordinates(
                            player.posX,
                            player.posY + player.getEyeHeight(),
                            player.posZ,
                            player.dimension
                    );
                    current.name = "Текущая позиция игрока";
                    current.range = 10.0;

                    quest.questInterface.addNavigationCoordinate(current);
                    updateScrollList();
                }
                break;
        }
    }

    private int getSelectedIndex() {
        String selected = scroll.getSelected();
        if (selected == null || selected.equals("Нет координат")) {
            return -1;
        }

        // Извлекаем номер из строки "1. Название (X: 10.0, Y: 64.0, Z: 10.0)"
        try {
            String numberPart = selected.substring(0, selected.indexOf('.'));
            return Integer.parseInt(numberPart.trim()) - 1;
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void scrollClicked(int mouseX, int mouseY, int ticks, GuiCustomScroll scroll) {
        if (scroll.id == 0) {
            // Просто обновляем выделение
        }
    }

    @Override
    public void scrollDoubleClicked(String select, GuiCustomScroll scroll) {

    }

    @Override
    public void save() {
        // Данные уже сохранены в списке coordinates
        // При закрытии подменю они останутся в quest.questInterface
    }
}