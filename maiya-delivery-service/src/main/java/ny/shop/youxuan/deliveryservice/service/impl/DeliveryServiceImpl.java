package ny.shop.youxuan.deliveryservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ny.shop.youxuan.deliveryservice.entity.DtbtInfo;
import ny.shop.youxuan.deliveryservice.mapper.DtbtInfoMapper;
import ny.shop.youxuan.deliveryservice.service.DeliveryService;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    @Autowired
    private DtbtInfoMapper mapper;

    @Override
    public boolean createDelivery(DtbtInfo info) {
        return mapper.insert(info) > 0;
    }

    @Override
    public boolean updateStatus(String infoId, int status) {
        return mapper.updateStatus(infoId, status) > 0;
    }
}