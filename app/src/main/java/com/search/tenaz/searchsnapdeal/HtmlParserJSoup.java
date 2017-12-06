package com.search.tenaz.searchsnapdeal;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by tenaz on 12/5/17.
 */

public class HtmlParserJSoup {

    private static Document doc;
    public static ArrayList<Model> imageList;
    private static String urlString;

    public static String getUrlForLoadMore(String query, String start) {
        StringBuilder url = new StringBuilder();
        url.append("https://www.snapdeal.com/acors/json/product/get/search/0/");
        url.append(start);
        url.append("/20?q=&sort=rlvncy&brandPageUrl=&searchState=previousRequest=true|serviceabilityUsed=false|filterState=null&pincode=&vc=&webpageName=" +
                "searchResult&campaignId=&brandName=&isMC=false&clickSrc=go_header&showAds=true&cartId=&page=srp&keyword=");
        url.append(query);
        return url.toString();


    }

    public static String getFirstUrl(String query) {
        imageList = new ArrayList<>();
        StringBuilder url = new StringBuilder();
        url.append("https://www.snapdeal.com/search?keyword=");
        url.append(query);
        url.append("&sort=rlvncy");
        return url.toString();
    }

    public static ArrayList<Model> getDetails(String urlString) {

        try {
            doc = Jsoup.connect(urlString).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Element element : doc.select("#products")) {

            Elements images = element.getElementsByTag("img");
            for (Element image : images) {
                Model model = new Model();
                String details = image.attr("data-src");
                if (details.isEmpty()) {
                    details = image.attr("src");
                }
                model.setDescription(image.attr("title"));
                model.setImageUrl(details);
                imageList.add(model);
            }
            imageList.remove(imageList.size()-1);

        }


        return imageList;
    }

    public static ArrayList<Model> getDetailsForLoadMore(String urlString) {

        try {
            doc = Jsoup.connect(urlString).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Element element : doc.getElementsByTag("body")) {

            Elements images = element.getElementsByTag("img");
            for (Element image : images) {
                Model model = new Model();
                String details = image.attr("data-src");
                if (details.isEmpty()) {
                    details = image.attr("src");
                }
                model.setDescription(image.attr("title"));
                model.setImageUrl(details);
                imageList.add(model);
            }
            imageList.remove(imageList.size()-1);

        }


        return imageList;
    }
}
