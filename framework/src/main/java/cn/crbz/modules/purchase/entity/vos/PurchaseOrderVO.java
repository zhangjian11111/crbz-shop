package cn.crbz.modules.purchase.entity.vos;

import cn.crbz.modules.purchase.entity.dos.PurchaseOrder;
import cn.crbz.modules.purchase.entity.dos.PurchaseOrderItem;
import lombok.Data;

import java.util.List;

/**
 * 采购单VO
 *
 * @author Bulbasaur
 * @since 2020/11/26 19:54
 */
@Data
public class PurchaseOrderVO extends PurchaseOrder {

    private List<PurchaseOrderItem> purchaseOrderItems;
}
