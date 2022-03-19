package nightmare.gui.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import nightmare.fonts.impl.Fonts;
import nightmare.module.Module;
import nightmare.utils.ColorUtils;
import nightmare.utils.ScreenUtils;
import nightmare.utils.TimerUtils;
import nightmare.utils.animation.Animation;
import nightmare.utils.animation.AnimationUtils;
import nightmare.utils.render.RenderUtils;

public class Notification {
    public static Minecraft mc = Minecraft.getMinecraft();

    public boolean isClassicNotification;
    public String message;
    public String title;
    TimerUtils timer;
    private float animationX;
    private float animationY;
    private float width;
    private final float height;
    private int delay;
    private Animation animation;
   
    public Notification(String title, String message, int delay) {

        this.message = message;
        this.title = title;
        this.delay = delay;
        
        if(Fonts.REGULAR.REGULAR_20.REGULAR_20.stringWidth(title) < Fonts.REGULAR.REGULAR_16.REGULAR_16.stringWidth(message)) {
            this.width = Fonts.REGULAR.REGULAR_16.REGULAR_16.stringWidth(message) + 45;
        }else{
            this.width = Fonts.REGULAR.REGULAR_20.REGULAR_20.stringWidth(title) + 45;
        }
              
        this.height = 22.0f;
        this.animationX = 140.0f;
        animation = new Animation(100.0F, ScreenUtils.getHeight());
        this.timer = new TimerUtils();
        timer.reset();
    }

    public void draw(float x, float offsetY) {
    	
        float target = isFinished() ? width : 0;
        animation.interpolate(target, offsetY, 6);
        this.animationX = animation.getX();

        if (animationY == 0) {
            animationY = offsetY;
        }

        animationY = animation.getY();

        float x1 = x - width + this.animationX;
        float x2 = x + animationX + 0;

        float y1 = animationY - 2;
        float y2 = y1 + height;

    	RenderUtils.drawRect(x1 + 35, y1, x2, y2, ColorUtils.getBackgroundColor());
        Fonts.REGULAR.REGULAR_20.REGULAR_20.drawString(this.title, x1 + 40, y1 + 3, -1);
        Fonts.REGULAR.REGULAR_16.REGULAR_16.drawString(this.message, x1 + 40, y1 + 14, -1);
    }

    public boolean shouldDelete() {
        return isFinished() && this.animationX == width;
    }

    public float getHeight() {
        return height;
    }

    private boolean isFinished() {
        return timer.delay(delay);
    }
}
