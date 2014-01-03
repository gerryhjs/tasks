/*
 * Copyright 2013 Sergiy Dubovik, WarnerJan Veldhuis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dubik.tasks.utils;

import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class IconUtil {
    /**
     * Gets a new icon with the overlayIcon paints over the original icon.
     *
     * @param c           the component where the returned icon will be used. The component is used as the
     *                    ImageObserver. It could be null.
     * @param icon        the original icon
     * @param overlayIcon the overlay icon.
     * @param location    the location as defined in SwingConstants - CENTER, NORTH, SOUTH, WEST, EAST, NORTH_EAST,
     *                    NORTH_WEST, SOUTH_WEST and SOUTH_EAST.
     * @return the new icon.
     */
    public static ImageIcon getOverlayIcon(Component c, ImageIcon icon, ImageIcon overlayIcon, int location) {
        return getOverlayIcon(c, icon, overlayIcon, location, new Insets(0, 0, 0, 0));
    }

    /**
     * Gets a new icon with the overlayIcon paints over the original icon.
     *
     * @param c           the component where the returned icon will be used. The component is used as the
     *                    ImageObserver. It could be null.
     * @param icon        the original icon
     * @param overlayIcon the overlay icon.
     * @param location    the location as defined in SwingConstants - CENTER, NORTH, SOUTH, WEST, EAST, NORTH_EAST,
     *                    NORTH_WEST, SOUTH_WEST and SOUTH_EAST.
     * @param insets      the insets to the border. This parameter has no effect if the location is CENTER. For example,
     *                    if the location is WEST, insets.left will be the gap of the left side of the original icon and
     *                    the left side of the overlay icon.
     * @return the new icon.
     */
    public static ImageIcon getOverlayIcon(Component c, ImageIcon icon, ImageIcon overlayIcon, int location, Insets insets) {
        int x = -1, y = -1;
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        int sw = overlayIcon.getIconWidth();
        int sh = overlayIcon.getIconHeight();
        switch (location) {
            case SwingConstants.CENTER:
                x = (w - sw) / 2;
                y = (h - sh) / 2;
                break;
            case SwingConstants.NORTH:
                x = (w - sw) / 2;
                y = insets.top;
                break;
            case SwingConstants.SOUTH:
                x = (w - sw) / 2;
                y = h - insets.bottom - sh;
                break;
            case SwingConstants.WEST:
                x = insets.left;
                y = (h - sh) / 2;
                break;
            case SwingConstants.EAST:
                x = w - insets.right - sw;
                y = (h - sh) / 2;
                break;
            case SwingConstants.NORTH_EAST:
                x = w - insets.right - sw;
                y = insets.top;
                break;
            case SwingConstants.NORTH_WEST:
                x = insets.left;
                y = insets.top;
                break;
            case SwingConstants.SOUTH_WEST:
                x = insets.left;
                y = h - insets.bottom - sh;
                break;
            case SwingConstants.SOUTH_EAST:
                x = w - insets.right - sw;
                y = h - insets.bottom - sh;
                break;
        }
        return getOverlayIcon(c, icon, overlayIcon, x, y);
    }

    /**
     * Gets a new icon with the overlayIcon paints over the original icon.
     *
     * @param c           the component where the returned icon will be used. The component is used as the
     *                    ImageObserver. It could be null.
     * @param icon        the original icon
     * @param overlayIcon the overlay icon.
     * @param x           the x location relative to the original icon where the overlayIcon will be pained.
     * @param y           the y location relative to the original icon where the overlayIcon will be pained.
     * @return the overlay icon
     */
    public static ImageIcon getOverlayIcon(Component c, ImageIcon icon, ImageIcon overlayIcon, int x, int y) {
        int w = icon == null ? overlayIcon.getIconWidth() : icon.getIconWidth();
        int h = icon == null ? overlayIcon.getIconHeight() : icon.getIconHeight();
        int sw = overlayIcon.getIconWidth();
        int sh = overlayIcon.getIconHeight();
        if (x != -1 && y != -1) {
            BufferedImage image = UIUtil.createImage(w, h, BufferedImage.TYPE_INT_ARGB);
            if (icon != null) {
                image.getGraphics().drawImage(icon.getImage(), 0, 0, w, h, c);
            }
            image.getGraphics().drawImage(overlayIcon.getImage(), x, y, sw, sh, c);
            return new ImageIcon(image);
        }
        else {
            return icon;
        }
    }
}
