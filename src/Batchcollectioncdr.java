import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1、给定指定的网址抓取对应的号码，目前抓取400，800以及0开头的座机，
 * 2、没有抓取到电话号码，已经报错的网址整理到wrong文件夹，手工确认
 * 3、
 */

public class Batchcollectioncdr {
    public static void main(String[] args) throws Exception {

        String filePath = System.getProperty("user.dir")+File.separator;

        BufferedReader br = new BufferedReader(new FileReader(filePath+"1.txt"));
        //Reader fr =new FileReader("F:\\正纳文档\\新标签\\cdr批量采集\\1.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath+"2.txt", true));

        BufferedWriter bww= new BufferedWriter(new FileWriter(System.getProperty("user.dir")+File.separator+"wrong.txt", true));
        List<String> list = new ArrayList();
        List<String> listwrong = new ArrayList();
        String line;
        while ((line = br.readLine()) != null) {
            urlview(line,list,listwrong);
        }
        List<String> lists1 = removeRepeat(list);
        List<String> lists2 = removeRepeat(listwrong);

        for (String s : lists1) {
            bw.write(s);
            bw.newLine();
            bw.flush();
        }
        for (String s : lists2) {
            bww.write(s);
            bww.newLine();
            bww.flush();
        }

    }

    /**
     * 去除重复值
     * @param list
     * @return
     */
    public static List removeRepeat(List list){
        List<String> listTemp=new ArrayList();
        for(int i=0;i<list.size();i++){
            if(!listTemp.contains(list.get(i))){
                listTemp.add(list.get(i).toString());
            }
        }
        return listTemp;
    }
    private static void urlview(String line,List<String> list,List<String> listwrong) throws Exception {
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
                findCdr(str, line,list,listwrong);
            }

            bufr.close();
            isr.close();
            in.close();
        } catch (Exception e) {
            urlviewhttps(line,list,listwrong);
        }


    }
    private static void urlviewhttps(String line,List<String> list,List<String> listwrong) throws Exception {
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
                findCdr(str, line,list,listwrong);
            }
            bufr.close();
            isr.close();
            in.close();
        } catch (Exception e) {
            listwrong.add(line);
        }

    }



    private static void findCdr(String str, String line,List<String> list,List<String> listwrong) throws Exception {
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
                    }else {
                        listwrong.add(line);
                    }
                }else {
                    listwrong.add(line);
                }
            }else {
                listwrong.add(line);
            }

        }

    }
}