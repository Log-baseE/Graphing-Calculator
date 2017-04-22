package mechanisms;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PrettyDouble {
    /**
     * This static method will generate it's HTML representation,
     * including superscript
     *
     * @param doubleNumber is the double number that will be formatted
     */
    public static String beautifyGenerateHTML(double doubleNumber) {
        try {
            BigDecimal bigDecimal = new BigDecimal(doubleNumber);
            bigDecimal = bigDecimal.setScale(6, RoundingMode.HALF_UP);
            doubleNumber = bigDecimal.doubleValue();
        } catch (java.lang.NumberFormatException ignored) {
        }
        String doubleString = String.valueOf(doubleNumber);
        int indexOfExp = doubleString.indexOf("E");
        doubleString = doubleString.replace("-", "&ndash;");
        String number;
        int power = 0;
        if (indexOfExp >= 0) {
            power = Integer.valueOf(doubleString.substring(indexOfExp + 1));
            number = trimDouble(doubleString.substring(0, indexOfExp));
        } else if (doubleString.contains("NaN")) {
            number = "undefined";
        } else if (doubleString.contains("Infinity")) {
            number = doubleString.replace("Infinity", "&infin;");
        } else {
            number = trimDouble(doubleString);
        }
        String ten = "";
        if (indexOfExp >= 0) ten = "&times;10" + "<sup>" + power + "</sup>";
        doubleString = number + ten;
        //noinspection SpellCheckingInspection,UnnecessaryLocalVariable
        String HTMLstring = "<body style=\"overflow-y:hidden\"><p style=\"font-family:'Cambria';font-size:15px \">"
                + doubleString + "</p></body>";
        return HTMLstring;
    }

    /**
     * Removes trailing zeros of a decimal number<br>
     * e.g.<br>
     *     5.0 will be 5<br>
     *     7.01020 will be 7.0102
     * @param doubleNumber will be trimmed
     * @return trimmed value
     */
    @SuppressWarnings("WeakerAccess")
    public static String trimDouble(String doubleNumber) {
        if (doubleNumber.contains(".")) {
            while ((doubleNumber.endsWith("0") || doubleNumber.endsWith(".")) && doubleNumber.length() > 1) {
                if (doubleNumber.endsWith(".")) {
                    doubleNumber = doubleNumber.substring(0, doubleNumber.length() - 1);
                    break;
                }
                doubleNumber = doubleNumber.substring(0, doubleNumber.length() - 1);
            }
        }
        return doubleNumber;
    }
}
