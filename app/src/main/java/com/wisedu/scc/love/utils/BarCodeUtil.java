package com.wisedu.scc.love.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.wisedu.scc.love.bean.Book;
import com.wisedu.scc.love.bean.Product;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @describe 条形码工具类
 */
public class BarCodeUtil {

    private static final String DOUBAN_BOOK_URL = "https://api.douban.com/v2/book/isbn/";
    private static final String CHNPDT_URL = "http://www.liantu.com/tiaoma/query.php";

    /**
     * 根据条形码获取书籍信息
     * @param barCode
     * @return
     */
    public static Book getBarCode_Book(String barCode){
        try {
            String url = DOUBAN_BOOK_URL.concat(barCode);
            String content = HttpsUtil.doHttpsGet(url, null);
            Book book = parseBookInfo(content);
            return book;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析图书JSON数据,把解析的数据封装在一个Book对象中
     * @return Book
     */
    private static Book parseBookInfo(String str){
        Book info = new Book();
        try{
            //先从String得到一个JSONObject对象
            JSONObject mess = new JSONObject(str);
            info.setId(mess.getString("id"));
            info.setTitle(mess.getString("title"));
            info.setBitmap(downLoadBitmap(mess.getString("image")));
            info.setAuthor(parseAuthor(mess.getJSONArray("author")));
            info.setPublisher(mess.getString("publisher"));
            info.setPublishDate(mess.getString("pubdate"));
            info.setISBN(mess.getString("isbn13"));
            info.setSummary(mess.getString("summary"));
            info.setAuthorInfo(mess.getString("author_intro"));
            info.setPage(mess.getString("pages"));
            info.setPrice(mess.getString("price"));
            info.setContent(mess.getString("catalog"));
            info.setRate(mess.getJSONObject("rating").getString("average"));
            info.setTag(parseTags(mess.getJSONArray("tags")));
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return info;
    }

    /**
     * 根据条形码获取书籍信息（该方法暂不可用）
     * @param barCode
     * @return
     */
    public static Product getBarCode_Product(String barCode){
        try {
            HttpParams httpParams = new BasicHttpParams();
            httpParams.setParameter("ean", barCode);
            String content = HttpsUtil.doHttpsPost(CHNPDT_URL, httpParams);
            Product product = parseProductInfo(content);
            return product;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析商品
     * @return Book
     */
    private static Product parseProductInfo(String str){
        Product info = new Product();
        try{
            //先从String得到一个JSONObject对象
            JSONObject mess = new JSONObject(str);
            info.setName(mess.getInt("price")+"");
            info.setFactory(mess.getString("fac_name"));
            info.setImage(downLoadBitmap(mess.getString("titleSrc")));
            info.setDescription(parseAuthor(mess.getJSONArray("guobie")));
        } catch (Exception e){
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 请求某个url上的图片资源
     * @return Bitmap
     */
    private static Bitmap downLoadBitmap(String bmurl) {
        Bitmap bm=null;
        InputStream is =null;
        BufferedInputStream bis=null;
        try{
            URL  url=new URL(bmurl);
            URLConnection connection=url.openConnection();
            bis=new BufferedInputStream(connection.getInputStream());
            //将请求返回的字节流编码成Bitmap
            bm= BitmapFactory.decodeStream(bis);
        }catch (Exception e){
            e.printStackTrace();
        }
        //关闭IO流
        finally {
            try {
                if(bis!=null)
                    bis.close();
                if (is!=null)
                    is.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return bm;
    }

    /**
     * 针对豆瓣图书的特殊信息，
     * 抽出一个parseAuthor方法解析作者信息
     * @return String
     */
    private static String parseAuthor (JSONArray arr) {
        StringBuffer str =new StringBuffer();
        for(int i=0;i<arr.length();i++) {
            try{
                str=str.append(arr.getString(i)).append(" ");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return str.toString();
    }

    /**
     * 针对豆瓣图书的特殊信息，抽出一个parseTags方法解析图书标签信息
     * @return String
     */
    private static String parseTags (JSONArray obj) {
        StringBuffer str =new StringBuffer();
        for(int i=0;i<obj.length();i++) {
            try{
                str=str.append(obj.getJSONObject(i).getString("name")).append(" ");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return str.toString();
    }

    /**
     * 获取条形码类型
     * @param code
     * @return
     */
    public static BarCodeType getBarCodeType(String code){
        if(StringUtil.isEmpty(code)){
            return BarCodeType.UNKNOWN;
        } else {
            int flag = Integer.parseInt(code.substring(0,3));
            if (flag==980) {
                return BarCodeType.BILL;
            } else if (flag>=977&&flag<=979) {
                return BarCodeType.BOOK;
            } else if (flag>=981&&flag<=983) {
                return BarCodeType.CURRENCYNOTE;
            } else if (flag>=990&&flag<=999) {
                return BarCodeType.COUPON;
            } else if(flag>=690&&flag<=699){
                return BarCodeType.CHNPDT;
            } else {
                return BarCodeType.FORPDT;
            }
        }
    }

    /**
     * 条形码类型枚举
     */
    public enum BarCodeType{
        BOOK, // 书籍
        BILL, // 应收票据
        CURRENCYNOTE, // 普通流通券
        COUPON, // 优惠券
        CHNPDT, // 国内商品
        FORPDT , // 国外商品
        UNKNOWN  // 未知类型
    }

}