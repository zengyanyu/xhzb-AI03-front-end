package com.xhzb.test;

import com.huaweicloud.sdk.iotda.v5.IoTDAClient;
import com.huaweicloud.sdk.iotda.v5.model.ListProductsRequest;
import com.huaweicloud.sdk.iotda.v5.model.ListProductsResponse;
import com.huaweicloud.sdk.iotda.v5.model.ProductSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Description IoTDeviceTest
 * @Author songyu
 * @Date 2026-08-11
 */
@SpringBootTest
public class IoTDeviceTest {

    @Autowired
    private IoTDAClient ioTDAClient;

    @Test
    public void testProductList(){

        //1.构建产品列表查询请求对象
        ListProductsRequest request = new ListProductsRequest();
        request.setLimit(50);

        //2.通过客户端调用并返回结果
        ListProductsResponse response = ioTDAClient.listProducts(request);

        //3.打印结果
        List<ProductSummary> products = response.getProducts();
        products.forEach(System.out::println);
    }
}
