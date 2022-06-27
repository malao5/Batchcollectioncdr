import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test3 {
    public static void main(String[] args) {

        String str = "<ul>\n" +
                "                            <li><a href=\"/accounting/svat.html\" target=\"_blank\">021-655610000</a></li>\n" +
                "                            <li><a href=\"/accounting/gvat.html\" target=\"_blank\" class=\"color-orange\">一般纳税人</a></li>\n" +
                "                            <li><a href=\"/product/index.html?ProductName=%e5%a4%96%e8%b5%84%e5%b0%8f%e8%a7%84%e6%a8%a1%e4%bc%81%e4%b8%9a\" target=\"_blank\">外资小规模企业</a></li>\n" +
                "                            <li><a href=\"/product/index.html?ProductName=%e5%a4%96%e8%b5%84%e4%bc%81%e：021-6556100004%b8%9a%e4%bb%a3%e8%a1%a8%e5%a4%84\" target=\"_blank\">外资企业代表处</a></li>\n" +
                "                            <li><a href=\"/product/index.html?ProductName=%e5%9b%bd%e5%86%85%e8%bf%9b%e5%87%ba%e5%8f%a3%e4%bc%81%e4%b8%9a\" target=\"_blank\">国内进出口企业</a></li>\n" +
                "                            <li><a href=\"/product/index.html?ProductName=%e7%94%b5%e5%ad%90%e5%8f%91%e7%:021-655610000a5%a8\" target=\"_blank\" class=\"color-orange\">电子发票</a></li>\n" +
                "                            <li><a href=\"/product/index.html?ProductName=%e7%a8%8e%e5%8a%a1%e4%bb%a3%e5%8a%9e\" target=\"_blank\">税务代办</a></li>\n" +
                "                        </ul>";
        String regex = "(:[0-9-()（） ——]{7,18})|(：[0-9-()（） ——]{7,18})|(>[0-9-()（） ——]{7,18})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String group = matcher.group();
            System.out.println(group);
        }

    }
}
