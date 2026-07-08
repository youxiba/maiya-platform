package ny.shop.youxuan.goodsservice.service;

import java.util.List;
import ny.shop.youxuan.goodsservice.entity.*;

public interface GoodsService {
    GoodsInfo getByGoodsInfoId(String id);

    List<GoodsInfo> getByMid(String mid);

    List<GoodsInfo> getOnSaleByMid(String mid);

    boolean deductStock(String id, Integer qty);

    boolean rollbackStock(String id, Integer qty);

    boolean addSales(String id, Integer qty);

    List<GoodsCategory> getAllCategories();

    List<ShoppingCart> getCart(String uid);

    boolean addToCart(ShoppingCart cart);
}