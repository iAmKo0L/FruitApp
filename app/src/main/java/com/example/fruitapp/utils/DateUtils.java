package com.example.fruitapp.utils;

import android.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final SimpleDateFormat DATE_FORMAT =
        new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static final SimpleDateFormat DATETIME_FORMAT =
        new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    private static final long ONE_DAY_MS = 24L * 60L * 60L * 1000L;

    public static String formatDate(long timestamp) {
        if (timestamp <= 0) return "—";
        return DATE_FORMAT.format(new Date(timestamp));
    }

    public static String formatDateTime(long timestamp) {
        if (timestamp <= 0) return "—";
        return DATETIME_FORMAT.format(new Date(timestamp));
    }

    /** Nhãn hạn sử dụng kèm trạng thái cảnh báo */
    public static String formatExpiryLabel(long expiryTimestamp) {
        if (expiryTimestamp <= 0) return "HSD: Không xác định";
        long diff = expiryTimestamp - System.currentTimeMillis();
        String dateStr = formatDate(expiryTimestamp);
        if (diff < 0) return "HSD: " + dateStr + "  ⚠ Đã hết hạn";
        long daysLeft = diff / ONE_DAY_MS;
        if (daysLeft == 0) return "HSD: " + dateStr + "  ⚠ Hết hạn hôm nay";
        if (daysLeft <= 3) return "HSD: " + dateStr + "  ⚠ Còn " + daysLeft + " ngày";
        return "HSD: " + dateStr;
    }

    /** Màu hiển thị theo mức độ còn hạn */
    public static int getExpiryColor(long expiryTimestamp) {
        if (expiryTimestamp <= 0) return Color.parseColor("#7F8C8D");
        long diff = expiryTimestamp - System.currentTimeMillis();
        if (diff < 0) return Color.parseColor("#C0392B");            // đỏ - hết hạn
        if (diff < 3L * ONE_DAY_MS) return Color.parseColor("#E67E22"); // cam - sắp hết hạn
        return Color.parseColor("#27AE60");                          // xanh lá - còn tươi
    }

    /** Tạo timestamp từ ngày hiện tại + số ngày */
    public static long daysFromNow(int days) {
        return System.currentTimeMillis() + (days * ONE_DAY_MS);
    }
}
