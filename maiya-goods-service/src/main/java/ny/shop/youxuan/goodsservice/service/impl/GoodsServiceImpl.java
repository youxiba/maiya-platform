package ny.shop.youxuan.goodsservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ny.shop.youxuan.goodsservice.entity.*;
import ny.shop.youxuan.goodsservice.mapper.*;
import ny.shop.youxuan.goodsservice.service.GoodsService;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsInfoMapper giMapper;
    @Autowired
    private GoodsCategoryMapper gcMapper;
    @Autowired
    private ShoppingCartMapper scMapper;

    public GoodsInfo getByGoodsInfoId(String id) {
        return giMapper.findByGoodsInfoId(id);
    }

    public List<GoodsInfo> getByMid(String mid) {
        return giMapper.findByMid(mid);
    }

    public List<GoodsInfo> getOnSaleByMid(String mid) {
        return giMapper.findOnSaleByMid(mid);
    }

    @Transactional
    public boolean deductStock(String id, Integer q) {
        return giMapper.deductStock(id, q) > 0;
    }

    @Transactional
    public boolean rollbackStock(String id, Integer q) {
        return giMapper.rollbackStock(id, q) > 0;
    }

    @Transactional
    public boolean addSales(String id, Integer q) {
        return giMapper.addSales(id, q) > 0;
    }

    public List<GoodsCategory> getAllCategories() {
        return gcMapper.findAllOrdered();
    }

    public List<ShoppingCart> getCart(String uid) {
        return scMapper.findByUid(uid);
    }

    public boolean addToCart(ShoppingCart cart) {
        return scMapper.insert(cart) > 0;
    }
}