package ny.shop.youxuan.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.userservice.entity.RightInfo;
import ny.shop.youxuan.userservice.mapper.RightInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限标识管理服务
 */
@Service
public class RightInfoServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(RightInfoServiceImpl.class);

    @Autowired
    private RightInfoMapper rightInfoMapper;

    /** 添加权限 */
    public ApiResult<RightInfo> addRight(RightInfo rightInfo) {
        rightInfoMapper.insert(rightInfo);
        log.info("权限创建成功: rightId={}, name={}", rightInfo.getRightId(), rightInfo.getName());
        return ApiResult.success(rightInfo);
    }

    /** 更新权限 */
    public ApiResult<RightInfo> updateRight(RightInfo rightInfo) {
        RightInfo info = rightInfoMapper.selectOne(
                new LambdaQueryWrapper<RightInfo>().eq(RightInfo::getRightId, rightInfo.getRightId()));
        if (info == null) {
            return ApiResult.error(400, "权限不存在");
        }
        info.setName(rightInfo.getName());
        info.setRoleRight(rightInfo.getRoleRight());
        rightInfoMapper.updateById(info);
        return ApiResult.success(info);
    }

    /** 删除权限 */
    public ApiResult<Boolean> deleteRight(String rightId) {
        rightInfoMapper.delete(new LambdaQueryWrapper<RightInfo>().eq(RightInfo::getRightId, rightId));
        log.info("权限删除成功: rightId={}", rightId);
        return ApiResult.success(true);
    }

    /** 查询单个权限 */
    public ApiResult<RightInfo> getByRightId(String rightId) {
        RightInfo info = rightInfoMapper.selectOne(
                new LambdaQueryWrapper<RightInfo>().eq(RightInfo::getRightId, rightId));
        if (info == null) return ApiResult.error("权限不存在");
        return ApiResult.success(info);
    }

    /** 查询所有权限 */
    public ApiResult<List<RightInfo>> getAll() {
        List<RightInfo> list = rightInfoMapper.selectList(null);
        return ApiResult.success(list);
    }
}
