import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class PrettyDouble {
    /**
     *
     * @param doubleNumber this
     */
    public static String beautifyGenerateHTML(double doubleNumber){
        try{
            BigDecimal bigDecimal = new BigDecimal(doubleNumber);
            bigDecimal = bigDecimal.setScale(6, RoundingMode.HALF_UP);
            System.out.println(bigDecimal);
            doubleNumber = bigDecimal.doubleValue();
        } catch (java.lang.NumberFormatException ex){
        }
        String doubleString = String.valueOf(doubleNumber);
        int indexOfExp = doubleString.indexOf("E");
        doubleString = doubleString.replace("-", "&ndash;");
        String number;
        int power=0;
        if(indexOfExp>=0) {
            power = Integer.valueOf(doubleString.substring(indexOfExp+1));
            number = trimDouble(doubleString.substring(0, indexOfExp));
        }
        else if (doubleString.contains("NaN")) number = "undefined";
        else if (doubleString.contains("Infinity")) number = doubleString.replace("Infinity","&infin;");
        else number = trimDouble(doubleString);
//        System.out.println(indexOfExp);
        String ten = "";
        if(indexOfExp>=0) ten = "&times;10"+"<sup>"+power+"</sup>";
        doubleString = number+ten;
        String HTMLstring = "<body style=\"overflow-y:hidden\"><p style=\"font-family:'Cambria';font-size:15px \">"+doubleString+"</p></body>";
        return HTMLstring;
    }
    public static String trimDouble(String doubleNumber){
        while((doubleNumber.endsWith("0")||doubleNumber.endsWith("."))&&doubleNumber.length()>1)
            doubleNumber = doubleNumber.substring(0,doubleNumber.length()-1);
        return doubleNumber;
    }
    public static void main(String[] args){}
}
