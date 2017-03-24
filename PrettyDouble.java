
public class PrettyDouble {
    /**
     *
     * @param expression
     */
    public static String beautifyGenerateHTML(String expression){
        int indexOfExp = expression.indexOf("E");
        expression = expression.replace("-", "&ndash;");
        String number;
        if(indexOfExp>=0) number = trimDouble(expression.substring(0, indexOfExp));
        else if (expression.contains("NaN")) number = "undefined";
        else if (expression.contains("Infinity")) number = expression.replace("Infinity","&infin;");
        else number = trimDouble(expression);
//        System.out.println(indexOfExp);
        String ten = "";
        if(indexOfExp>=0) ten = "&times;10"+"<sup>"+expression.substring(indexOfExp+1)+"</sup>";
        expression = number+ten;
        String HTMLstring = "<body style='overflow-y:hidden'><p style=\"font-family:'Cambria';font-size:15px \">"+expression+"</p></body>";
        return HTMLstring;
    }
    public static String trimDouble(String doubleNumber){
        if(doubleNumber.endsWith(".0")) return doubleNumber.replace(".0", "");
        else return doubleNumber;
    }
    public static void main(String[] args){}
}
