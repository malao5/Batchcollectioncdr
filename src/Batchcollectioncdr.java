import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Batchcollectioncdr {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("F:\\正纳文档\\新标签\\cdr批量采集\\1.txt"));
        //Reader fr =new FileReader("F:\\正纳文档\\新标签\\cdr批量采集\\1.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter("F:\\正纳文档\\新标签\\cdr批量采集\\2.txt", true));
        List<String> list = new ArrayList();
        String line;
        while ((line = br.readLine()) != null) {
            urlview(line,list);
        }
        List<String> list1 = removeRepeat(list);
        for (String s : list1) {
            bw.write(s);
            bw.newLine();
            bw.flush();
        }

    }
    public static List removeRepeat(List list){
        List<String> listTemp=new ArrayList();
        for(int i=0;i<list.size();i++){
            if(!listTemp.contains(list.get(i))){
                listTemp.add(list.get(i).toString());
            }
        }
        return listTemp;
    }
    private static void urlview(String line,List<String> list) throws Exception {
        HttpURLConnection httpUrl = null;
        try {
            URL url = new URL("http://" + line);

            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36)");
            httpUrl.connect();
            //InputStream in = url.openStream();
            InputStream in = httpUrl.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader bufr = new BufferedReader(isr);
            String str;
            while ((str = bufr.readLine()) != null) {
                findCdr(str, line,list);
            }

            bufr.close();
            isr.close();
            in.close();
        } catch (Exception e) {
            urlviewhttps(line,list);
        }


    }
    private static void urlviewhttps(String line,List<String> list) throws Exception {
        HttpURLConnection httpUrl = null;
        try {
            URL url = new URL("https://" + line);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            httpUrl.connect();
            //InputStream in = url.openStream();
            InputStream in = httpUrl.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader bufr = new BufferedReader(isr);
            String str;
            while ((str = bufr.readLine()) != null) {
                findCdr(str, line,list);
            }

            bufr.close();
            isr.close();
            in.close();
        } catch (Exception e) {
            BufferedWriter bww= new BufferedWriter(new FileWriter("F:\\正纳文档\\新标签\\cdr批量采集\\wrong.txt", true));
            bww.write(line);
            bww.newLine();
            bww.flush();
        }

    }



    private static void findCdr(String str, String line,List<String> list) {

        String regex = "[0-9-()（） ——]{7,18}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String group = matcher.group();
            String rs3=group.replaceAll("(<img(.*?)(src)=\"(?!.*?qq).*>)|(<a\\s+.*>)","");
            String rs1=rs3.replaceAll("[\\(|\\)|\\（|\\）|\\ ]","");
            if(!rs1.isEmpty()&&rs1!=null){

                if(rs1.startsWith("0")||rs1.startsWith("400")||rs1.startsWith("800")){
                    String rs2=rs1.replaceAll("[\\-|——]","");
                    if(rs2.length()>=10 && rs2.length()<=12) {
                        list.add(line+":"+rs2);
                    }
                }
            }

        }

    }
}