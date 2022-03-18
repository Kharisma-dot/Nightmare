package nightmare.gui.notification;

import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {
    private static final CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();
    
    public static void doRender(float wid, float hei) {
        float startY = hei - 23;
        for (Notification notification : notifications) {
            if (notification == null)
                continue;

            notification.draw(wid, startY);
            startY -= notification.getHeight() + 2;
        }
        notifications.removeIf(Notification::shouldDelete);
    }

    public static void show(String title, String message, int delay) {
        notifications.add(new Notification(title, message, delay));
    }
}
