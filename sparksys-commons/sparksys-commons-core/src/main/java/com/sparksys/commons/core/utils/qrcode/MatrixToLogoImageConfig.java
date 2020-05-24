package com.sparksys.commons.core.utils.qrcode;

import java.awt.*;

/**
 * description: MatrixToLogoImageConfig
 *
 * @Author zhouxinlei
 * @Date  2020-05-24 13:15:13
 */
public class MatrixToLogoImageConfig {
    /**
     * logo默认边框颜色
     */
    public static final Color DEFAULT_BORDER_COLOR = Color.RED;
    /**
     * logo默认边框宽度
     */
    public static final int DEFAULT_BORDER = 2;
    /**
     * logo大小默认为照片的1/5
     */
    public static final int DEFAULT_LOGO_PART = 5;

    private final int border = DEFAULT_BORDER;
    private final Color borderColor;
    private final int logoPart;

    public MatrixToLogoImageConfig() {
        this(DEFAULT_BORDER_COLOR, DEFAULT_LOGO_PART);
    }

    public MatrixToLogoImageConfig(Color borderColor, int logoPart) {
        this.borderColor = borderColor;
        this.logoPart = logoPart;
    }

    public Color getBorderColor() {
        return this.borderColor;
    }

    public int getBorder() {
        return this.border;
    }

    public int getLogoPart() {
        return this.logoPart;
    }
}
