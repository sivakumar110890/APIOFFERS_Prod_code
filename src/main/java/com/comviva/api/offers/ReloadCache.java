package com.comviva.api.offers;

import com.comviva.api.filters.AuthenticationFilter;
import com.comviva.api.j4u.utils.CellIDPoolIDCache;
import com.comviva.api.j4u.utils.ProductInfoCache;
import com.comviva.api.j4u.utils.ProductPriceCache;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.comviva.api.j4u.utils.J4UOfferConstants.REQUEST_BUFFER;

@WebServlet(urlPatterns = { "/J4U/cache" })
public class ReloadCache extends HttpServlet {
    public final static Logger LOGGER = Logger.getLogger(ReloadCache.class);
    public static final String CACHE = "cache";
    public static final String OP = "op";
    public static final String STATUS = "status";
    public static final String RELOAD = "reload";
    public static final String PRODUCTINFOCACHE = "productinfocache";
    public static final String PRODUCTPRICECACHE = "productpricecache";
    public static final String CELLPOOLIDCACHE = "cellpoolidcache";
    public static final String SUCCESS = "SUCCESS";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject postDataObj = AuthenticationFilter.objectToJSONObject(req.getAttribute(REQUEST_BUFFER));
        PrintWriter out;
        String cache = postDataObj.getString(CACHE);
        String op = postDataObj.getString(OP);
        if (op.equalsIgnoreCase(STATUS)) {
            StringBuilder stringBuilder = new StringBuilder() ;
            stringBuilder.append("ProdInfo Cache <br>") ;
            ProductInfoCache productInfoCache = ProductInfoCache.instance() ;
            stringBuilder.append(productInfoCache.toString()) ;
            stringBuilder.append("<br>Product Price Cache <br>") ;
            ProductPriceCache productPriceCache = ProductPriceCache.instance() ;
            stringBuilder.append(productPriceCache.toString()) ;
            stringBuilder.append("<br>CellID PoolID Cache<br>") ;
            CellIDPoolIDCache cellIDPoolIDCache = CellIDPoolIDCache.instance() ;
            stringBuilder.append(cellIDPoolIDCache.toString()) ;
            out = resp.getWriter();
            out.println(stringBuilder.toString());

        } else if (op.equalsIgnoreCase(RELOAD)) {
            if (null != cache && !cache.isEmpty()) {
                if (cache.equalsIgnoreCase(PRODUCTINFOCACHE)) {
                    ProductInfoCache productInfoCache = ProductInfoCache.instance() ;
                    productInfoCache.reloadCache();
                } else if (cache.equalsIgnoreCase(PRODUCTPRICECACHE)) {
                    ProductPriceCache productPriceCache = ProductPriceCache.instance() ;
                    productPriceCache.reloadCache();
                } else if (cache.equalsIgnoreCase(CELLPOOLIDCACHE)) {
                    CellIDPoolIDCache cellIDPoolIDCache = CellIDPoolIDCache.instance() ;
                    cellIDPoolIDCache.reloadCache();
                }
            }
            out = resp.getWriter();
            out.println(SUCCESS);
        }
    }
}
