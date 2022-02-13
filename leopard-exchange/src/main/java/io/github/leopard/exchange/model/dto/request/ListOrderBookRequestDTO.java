package io.github.leopard.exchange.model.dto.request;

import javax.swing.text.StyledEditorKit.BoldAction;
import lombok.Data;

/**
 * 盘口深度
 *
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
@Data
public class ListOrderBookRequestDTO {

    private String market;
    private String interval = "0"; // 深度 String | Order depth. 0 means no aggregation is applied. default to 0
    private Integer limit = 10;  // TODO
    private boolean withId = false; //是否返回订单簿ID

}
