package noppes.npcs.quests;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.QuestData;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public class QuestArrowRenderer {

    // Текстура не нужна, если рисуем цветом, но переменную можно оставить
    private static final ResourceLocation ARROW_TEXTURE = new ResourceLocation("customnpcs", "textures/quest_arrow.png");

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (player == null || mc.gameSettings.hideGUI) {
            return;
        }

        PlayerData playerData = PlayerData.get(player);
        if (playerData == null || playerData.questData == null) {
            return;
        }

        for (QuestData questData : playerData.questData.activeQuests.values()) {
            if (questData == null || questData.quest == null) continue;

            QuestInterface quest = questData.quest.questInterface;
            if (!quest.toRenderArrow) continue;
            if (quest.hasNavigation() && !quest.isCompleted(player)) {
                renderArrowForQuest(quest, player, event.getPartialTicks());
            }
        }
    }

    private void renderArrowForQuest(QuestInterface quest, EntityPlayer player, float partialTicks) {
        List<QuestCordinates> coords = quest.getNavigationCoordinates();
        if (coords == null || coords.isEmpty()) return;

        for (QuestCordinates coord : coords) {
            if (coord.dimension != player.dimension) continue;

            double dx = coord.x - player.posX;
            double dy = coord.y - (player.posY + player.getEyeHeight());
            double dz = coord.z - player.posZ;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

            // Проверяем радиус (если игрок уже внутри точки, эту конкретную стрелку не рисуем)
            if (dist <= coord.range) continue;

            // Рендерим текущую координату (coord)
            renderSingleArrow(coord, dist, player, partialTicks);
        }

    }
public void renderSingleArrow(QuestCordinates coord, double dist, EntityPlayer player, float partialTicks){
    Minecraft mc = Minecraft.getMinecraft();

    // Получаем позицию камеры рендера
    double rPx = mc.getRenderManager().viewerPosX;
    double rPy = mc.getRenderManager().viewerPosY;
    double rPz = mc.getRenderManager().viewerPosZ;

    // Координаты цели (центр блока + 0.5)
    // Обычно квестовые координаты указывают на угол блока, поэтому добавляем 0.5, чтобы было по центру
    double targetX = coord.x + 0.5;
    double targetY = coord.y;
    double targetZ = coord.z + 0.5;

    // Анимация: прыгание вверх-вниз
    long time = player.world.getTotalWorldTime();
    float animationTime = (time + partialTicks) * 0.1f; // Скорость анимации
    double bobOffset = Math.sin(animationTime) * 0.2 + 0.5; // Амплитуда 0.2 блока

    GlStateManager.pushMatrix();

    // --- 3. Трансформация ---

    // Перемещаем стрелку в координаты ЦЕЛИ (минус координаты камеры)
    // targetY + 2.0 (базовая высота) + bobOffset (прыгание)
    GlStateManager.translate(targetX - rPx, (targetY - rPy) + 2.5 + bobOffset, targetZ - rPz);

    // Вращение стрелки вокруг своей оси (чтобы было красиво)
    float rotationAngle = (time + partialTicks) * 3.0f;
    GlStateManager.rotate(rotationAngle, 0, 1, 0);

    // Переворот, чтобы указывала острием ВНИЗ (так как renderArrow рисует вверх)
    GlStateManager.rotate(180, 1, 0, 0);

    // --- 4. Настройки GL (Сквозь стены) ---
    float scale = 1.0f; // Размер стрелки (1.0 = примерно 1-2 блока высотой)
    GlStateManager.scale(scale, scale, scale);

    GlStateManager.disableTexture2D(); // Рисуем геометрией
    GlStateManager.disableLighting();  // Светящаяся
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

    // Магия, чтобы видеть сквозь стены
    GlStateManager.disableDepth();
    GlStateManager.depthMask(false);
    GlStateManager.disableCull(); // Видеть со всех сторон

    // Цвет
    int color = coord.color != 0 ? coord.color : 0x00FF00;

    float r = ((color >> 16) & 0xFF) / 255.0f;
    float g = ((color >> 8) & 0xFF) / 255.0f;
    float b = (color & 0xFF) / 255.0f;

    // Альфа: можно сделать, чтобы она исчезала, когда подходишь очень близко
    float alpha = 0.8f;
    if (dist < 5) {
        alpha = (float) (dist / 10.0f) * 0.8f;
    }

    GlStateManager.color(r, g, b, alpha);

    // --- 5. Рисуем ---
    renderArrow();

    // --- 6. Восстановление ---
    GlStateManager.enableCull();
    GlStateManager.depthMask(true);
    GlStateManager.enableDepth();
    GlStateManager.enableLighting();
    GlStateManager.enableTexture2D();
    GlStateManager.disableBlend();
    GlStateManager.popMatrix();
}
    private void renderArrow() {
        // Рисуем объемную стрелку (пирамидку)
        Tessellator tessellator = Tessellator.getInstance();
        net.minecraft.client.renderer.BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);

        double w = 0.4;  // Ширина стрелки
        double h = 1.5;  // Высота острия

        // Поскольку мы перевернули её на 180 градусов в основном методе,
        // рисуем "нормальную" пирамиду острием вверх (в локальных координатах),
        // которая в мире будет смотреть вниз.

        // Острие (Top)
        double topY = h;
        double botY = 0.0;

        // Перед
        buffer.pos(0, topY, 0).endVertex();
        buffer.pos(-w, botY, w).endVertex();
        buffer.pos(w, botY, w).endVertex();

        // Право
        buffer.pos(0, topY, 0).endVertex();
        buffer.pos(w, botY, w).endVertex();
        buffer.pos(w, botY, -w).endVertex();

        // Зад
        buffer.pos(0, topY, 0).endVertex();
        buffer.pos(w, botY, -w).endVertex();
        buffer.pos(-w, botY, -w).endVertex();

        // Лево
        buffer.pos(0, topY, 0).endVertex();
        buffer.pos(-w, botY, -w).endVertex();
        buffer.pos(-w, botY, w).endVertex();

        // Дно (крышка)
        buffer.pos(-w, botY, w).endVertex();
        buffer.pos(-w, botY, -w).endVertex();
        buffer.pos(w, botY, w).endVertex();

        buffer.pos(w, botY, w).endVertex();
        buffer.pos(-w, botY, -w).endVertex();
        buffer.pos(w, botY, -w).endVertex();

        tessellator.draw();
    }
}