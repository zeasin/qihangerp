package cn.qihangerp.api.domain.bo;

import lombok.Data;

/**
 * 分配给供应商发货
 */
@Data
public class SupplierShipDistBo {
    private Long[] orderItemIds;
}
