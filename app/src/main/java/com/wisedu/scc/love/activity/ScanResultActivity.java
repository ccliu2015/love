package com.wisedu.scc.love.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wisedu.scc.love.R;
import com.wisedu.scc.love.bean.Book;
import com.wisedu.scc.love.bean.Product;
import com.wisedu.scc.love.utils.BarCodeUtil;
import com.wisedu.scc.love.utils.CommonUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by JZ on 2015/3/25.
 */
@EActivity(R.layout.activity_scan_result)
public class ScanResultActivity extends Activity{

    private String scanResult;

    @ViewById(R.id.webViewContainer)
    protected RelativeLayout webViewContainer;

    @ViewById(R.id.isbnContainer)
    protected RelativeLayout isbnContainer;

    @ViewById(R.id.webView)
    protected WebView webView;

    @ViewById(R.id.bookview_title)
    protected TextView title;

    @ViewById(R.id.bookview_author)
    protected TextView author;

    @ViewById(R.id.bookview_publisher)
    protected TextView publisher;

    @ViewById(R.id.bookview_publisherdate)
    protected TextView publishDate;

    @ViewById(R.id.bookview_isbn)
    protected TextView isbn;

    @ViewById(R.id.bookview_summary)
    protected TextView summary;

    @ViewById(R.id.bookview_rate)
    protected TextView rate;

    @ViewById(R.id.bookview_price)
    protected TextView price;

    @ViewById(R.id.bookview_pages)
    protected TextView page;

    @ViewById(R.id.bookview_content)
    protected TextView content;

    @ViewById(R.id.bookview_tag)
    protected TextView tag;

    @ViewById(R.id.bookview_authorinfo)
    protected TextView authorInfo;

    @ViewById(R.id.bookview_cover)
    protected ImageView cover;

    @ViewById(R.id.bookview_arrow)
    protected ImageView arrow;

    @ViewById(R.id.bookview_content_menu)
    protected TextView content_menu;

    @AfterViews
    public void doAfterViews(){
        // 允许 Javascript and 缩放
        WebSettings websettings = webView.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setBuiltInZoomControls(true);
        // 设置客户端
        webView.setWebViewClient(new WebViewClient() {
            // 在引用中打开URL，而非手机浏览器
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        // 加载URL
        scanResult = getIntent().getStringExtra("result");
        if(null!=scanResult){
            if(scanResult.contains(".")) {// 二维码扫描出的网址
                webViewContainer.setVisibility(View.VISIBLE);
                isbnContainer.setVisibility(View.GONE);
                webView.loadUrl(scanResult);
            } else {// 条形码扫描出的结果
                webViewContainer.setVisibility(View.GONE);
                BarCodeUtil.BarCodeType type = BarCodeUtil.getBarCodeType(scanResult);
                switch (type){
                    case BOOK:
                        isbnContainer.setVisibility(View.VISIBLE);
                        new Thread(bookNetworkTask).start();
                        break;
                    case CHNPDT:
                    case FORPDT:
                        // TODO 商品信息处理
                        // new Thread(productNetworkTask).start();
                        CommonUtil.shortToast(getApplicationContext(), "商品信息读取正在开发中……");
                        this.finish();
                        break;
                    case BILL:
                    case COUPON:
                    case CURRENCYNOTE:
                    case UNKNOWN:
                        CommonUtil.shortToast(getApplicationContext(), "该条形码目前不支持");
                        break;
                }
            }
        }
    }

    @Click(R.id.bookview_content_menu)
    public void dealContentMenu(){
        if(content.getVisibility()==View.GONE){
            arrow.setImageResource(R.drawable.down);
            content.setVisibility(View.VISIBLE);
        } else {
            arrow.setImageResource(R.drawable.right);
            content.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 处理器
     */
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            Object bobj = bundle.getParcelable("book");
            Book book = null==bobj?new Book():(Book)bobj;
            if(null!=book){
                title.setText(book.getTitle());
                author.setText("作者:"+book.getAuthor());
                publisher.setText("出版社:"+book.getPublisher());
                publishDate.setText("出版时间:"+book.getPublishDate());
                isbn.setText("ISBN:"+book.getISBN());
                summary.setText(book.getSummary());
                page.setText("页数:"+book.getPage());
                price.setText("定价:"+book.getPrice());
                content.setText(book.getContent());
                authorInfo.setText(book.getAuthorInfo());
                tag.setText("标签:"+book.getTag());
                cover.setImageBitmap(book.getBitmap());
            }
            Object pobj = bundle.getParcelable("product");
            Product product = null==pobj?new Product():(Product)pobj;
            if(null!=product){
                // TODO 商品信息展示
            }
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable bookNetworkTask = new Runnable() {
        @Override
        public void run() {
            Book book = BarCodeUtil.getBarCode_Book(scanResult);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("book", book);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable productNetworkTask = new Runnable() {
        @Override
        public void run() {
            Product product = BarCodeUtil.getBarCode_Product(scanResult);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("product", product);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };
}
