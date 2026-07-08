package ny.shop.youxuan.merchantservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ny.shop.youxuan.merchantservice.entity.MerchantHours;
import ny.shop.youxuan.merchantservice.mapper.MerchantHoursMapper;
import ny.shop.youxuan.merchantservice.service.MerchantHoursService;

@Service
public class MerchantHoursServiceImpl implements MerchantHoursService {
    @Autowired
    private MerchantHoursMapper mapper;

    @Override
    public MerchantHours getByUid(String uid) {
        return mapper.findByUid(uid);
    }

    @Override
    public boolean isOpen(String mid) {
        MerchantHours h = mapper.findByMid(mid);
        return h != null && h.getOpenFlag() != null && h.getOpenFlag() && h.getWorkStatus() != null
                && h.getWorkStatus() == 1;
    }
}