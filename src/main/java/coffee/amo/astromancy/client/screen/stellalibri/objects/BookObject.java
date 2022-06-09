package coffee.amo.astromancy.client.screen.stellalibri.objects;

import coffee.amo.astromancy.client.screen.stellalibri.BookScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

import static coffee.amo.astromancy.client.screen.stellalibri.BookScreen.screen;

public class BookObject {
    public boolean isHovering;
    public float hover;
    public int posX;
    public int posY;
    public int width;
    public int height;

    public BookObject(int posX, int posY, int width, int height)
    {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }
    public int hoverCap()
    {
        return 20;
    }

    public void render(Minecraft minecraft, PoseStack poseStack, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks)
    {

    }
    public void lateRender(Minecraft minecraft, PoseStack poseStack, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks)
    {

    }
    public void click(float xOffset, float yOffset, double mouseX, double mouseY)
    {

    }
    public void exit()
    {

    }
    public boolean isHovering(float xOffset, float yOffset, double mouseX, double mouseY)
    {
        return BookScreen.isHovering(mouseX,mouseY, offsetPosX(xOffset), offsetPosY(yOffset), width, height);
    }
    public int offsetPosX(float xOffset)
    {
        int guiLeft = (width - screen.bookWidth) / 2;
        return (int) (guiLeft+ this.posX + xOffset);
    }
    public int offsetPosY(float yOffset)
    {
        int guiTop = (height - screen.bookHeight) / 2;
        return (int) (guiTop + this.posY + yOffset);
    }
}
