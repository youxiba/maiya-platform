package ny.shop.youxuan.merchantservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ny.shop.youxuan.merchantservice.entity.MerchantInfo;
import ny.shop.youxuan.merchantservice.mapper.MerchantInfoMapper;
import ny.shop.youxuan.merchantservice.service.MerchantInfoService;
import java.util.List;

@Service
public class MerchantInfoServiceImpl implements MerchantInfoService {
    @Autowired
    private MerchantInfoMapper mapper;

    @Override
    public MerchantInfo getByMid(String mid) {
        return mapper.findByMid(mid);
    }

    @Override
    public MerchantInfo getByUid(String uid) {
        return mapper.findByUid(uid);
    }

    @Override
    public String getUidByMid(String mid) {
        return mapper.getUidByMid(mid);
    }

    @Override
    public List<MerchantInfo> findNearby(Double lon, Double lat, Integer range) {
        return mapper.findNearby(lon, lat, range);
    }

    @Override
    public boolean addMerchant(MerchantInfo m) {
        return mapper.insert(m) > 0;
    }

    @Override
    public boolean updateMerchant(MerchantInfo m) {
        return mapper.updateById(m) > 0;
    }
}