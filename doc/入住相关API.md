
 # 入住办理

 ## 查看老人信息详情(入住字段填充)

**请求方式**：`GET`

**请求路径**：`/nursing/healthAssessment/elder/{id}`

**请求参数**：路径参数，评估id

**响应示例**：

```JSON
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "phone": "15100000000", //联系电话
        "medicalPaymentMethod": "城镇职工基本医疗保险", //医疗费用支付方式
        "coreSuggestion": 1,//// 核心建议，0-不建议入住，1-建议入住
        "nation": "汉族", //民族
        "educationLevel": "文盲", //文化程度
        "idCardNo": "41072519730102512x",//身份证号
        "name": "老金",//老人姓名
        "socialSecurityCard": "41072519730102512x",//社保卡号
        "livingSituation": "独居",//居住情况
        "religiousBelief": "无",//宗教信仰
        "economicSource": "退休金",//经济来源
        "maritalStatus": "已婚"//婚姻状况
    }
}
```



 ## 查询所有护理等级信息

**接口地址**:`/nursing/nursingLevel/listAll`

**请求方式**:`GET`

**请求参数**:

暂无

**响应示例**:

```JavaScript
{
    "msg": "操作成功",
    "code": 200,
    "data": [
        {
            "createBy": "1671403256519078153",
            "createTime": "2023-09-26 15:40:08",
            "updateTime": "2023-09-26 15:40:08",
            "id": 1,
            "name": "特级护理等级",
            "lplanId": 1,
            "fee": 2700.00,
            "status": 1,
            "description": "特级护理等级适用于那些需要高度关注和专业照顾的老年人，包含了全面的护理项目和照顾"
        },
        {
            "createBy": "1671403256519078153",
            "createTime": "2023-09-26 15:45:12",
            "updateTime": "2023-09-26 15:45:12",
            "id": 2,
            "name": "一级护理等级",
            "lplanId": 2,
            "fee": 2000.00,
            "status": 1,
            "description": "一级护理等级适用于需要一定程度护理和照料的老年人，该等级提供了基本的日常护理项目"
        }
        ....
    ]
}
```

 ## 根据床位状态查询获取所有楼层数据

**接口地址**:`/elder/floor/getRoomAndBedByBedStatus/{status}`

**请求方式**:`GET`

**请求参数**:

| 参数名称 | 参数说明                       | 是否必须 |
| -------- | ------------------------------ | -------- |
| status   | 床位状态，0：未入住，1：已入住 | true     |

**响应示例**:

```JavaScript
{
  "msg": "操作成功",
  "code": 200,
  "data": [
    {
      "value": '1',
      "label": '1楼',
      "children": [
        {
          "value": '2',
          "label": '101',
          "children": [
            {
              "value": '22',
              "label": '101-1'
            }
          ]
        },
        {
          "value": '3',
          "label": '102',
          "children": [
            {
              "value": '33',
              "label": '102-1'
            }
          ]
        }
      ]
    }
  ]
}
```

 ## 查询房间数据(楼层、房间、价格)

**接口地址**:`/elder/room/one/{id}`

**请求方式**:`GET`

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型       |
| -------- | -------- | -------- | -------- | -------------- |
| id       | 房间ID   | path     | true     | integer(int64) |

**响应示例**:

```JSON
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "floorName": "8楼",
        "floorId": "8",
        "roomId": "51",
        "code": "801",
        "price": "8000.00"
    }
}
```

 ## 申请入住

**接口地址**:`/nursing/checkIn/apply`

**请求方式**:`POST`

**请求示例**:

```JSON
{
  "healthAssessmentId":56,//评估id
  "checkInElderDto": {
    "address": "北京市昌平区",
    "age": "60",
    "birthday": "1946-12-19",
    "idCardNationalEmblemImg": "https://itheim.oss-cn-beijing.aliyuncs.com/481be4c8-5a04-4475-8712-bd3f53f6e4ff.jpg",
    "idCardNo": "132123194612191234",
    "idCardPortraitImg": "https://itheim.oss-cn-beijing.aliyuncs.com/2038127d-bd2e-46eb-8b9a-867205a048c6.jpg",
    "image": "https://itheim.oss-cn-beijing.aliyuncs.com/9fc6b8ac-3ed5-432c-8835-8ca91abc2569.png",
    "name": "刘天龙",
    "phone": "15543234565",
    "sex": 1,
    "nation":"民族",
    "educationLevel":"文化程度",
    "socialSecurityCard":"社保卡号",
    "livingSituation":"居住情况",
    "religiousBelief":"宗教信仰",
    "economicSource":"经济来源",
    "maritalStatus":"婚姻状况",
    "medicalPaymentMethod":"医疗费用支付方式",
    "coreSuggestion":"核心建议"
  },
  "elderFamilyDtoList": [
    {
      "kinship": "1",
      "name": "刘天",
      "phone": "15543234568"
    }
  ],
  "checkInConfigDto": {
    "bedFee": "4000.00",
    "bedId": "10",
    "code": "107",
    "deposit": 3000,
    "endDate": "2024-09-30 00:00:00",
    "feeEndDate": "2024-08-31 00:00:00",
    "feeStartDate": "2024-08-26 00:00:00",
    "floorId": "1",
    "floorName": "1楼",
    "governmentSubsidy": 1,
    "insurancePayment": 1,
    "nursingFee": 2000,
    "nursingLevelId": 77,
    "nursingLevelName": "1号护理计划",
    "otherFees": 1,
    "roomId": "7",
    "startDate": "2024-08-26 00:00:00"
  },
  "checkInContractDto": {
    "agreementPath": "https://itheim.oss-cn-beijing.aliyuncs.com/72731f06-d7d3-41ca-9343-b9422993ebda.png",
    "contractName": "刘天龙",
    "signDate": "2024-08-26 15:47:39",
    "thirdPartyName": "刘天",
    "thirdPartyPhone": "15543234568"
  }
}
```

**响应示例**:

```JavaScript
{"msg":"操作成功","code":200}
```

 ## 查询入住详情

**接口地址**:`/nursing/checkIn/detail/{id}`

**请求方式**:`GET`

**请求参数**:

| 参数名称 | 参数说明 | 是否必须 |
| -------- | -------- | -------- |
| id       | 入住id   | true     |

**响应示例**:

```JSON
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "checkInElderVo": {
            "id": 326,
            "name": "李天龙",
            "idCardNo": "132123195612131234",
            "birthday": "2024-08-11",
            "sex": 1,
            "phone": "13211223322",
            "address": "北京市昌平区",
            "image": "https://itheim.oss-cn-beijing.aliyuncs.com/85f734bd-26be-4be1-8086-0ffae0b2374d.png",
            "idCardNationalEmblemImg": "https://itheim.oss-cn-beijing.aliyuncs.com/843b67d3-1061-4a27-89e1-a7974c826c36.jpg",
            "idCardPortraitImg": "https://itheim.oss-cn-beijing.aliyuncs.com/00fd6a90-d982-4981-92c8-5c29ebb276d9.jpg",
            "age": 67,
            "nation":"民族",
            "educationLevel":"文化程度",
            "socialSecurityCard":"社保卡号",
            "livingSituation":"居住情况",
            "religiousBelief":"宗教信仰",
            "economicSource":"经济来源",
            "maritalStatus":"婚姻状况",
            "medicalPaymentMethod":"医疗费用支付方式",
            "coreSuggestion":"核心建议"
        },
        "elderFamilyVoList": [
            {
                "name": "李彤",
                "phone": "13211223311",
                "kinship": "0"
            }
        ],
        "checkInConfigVo": {
            "createBy": "1",
            "createTime": "2024-08-26 15:44:08",
            "updateTime": "2024-08-26 07:44:08",
            "id": 2,
            "checkInId": 6,
            "nursingLevelId": 77,
            "nursingLevelName": "1号护理计划",
            "feeStartDate": "2024-08-26 00:00:00",
            "feeEndDate": "2024-08-31 00:00:00",
            "deposit": 3000.00,
            "nursingFee": 2000.00,
            "bedFee": 6000.00,
            "insurancePayment": 00,
            "governmentSubsidy": 00,
            "otherFees": 00,
            "sortOrder": 0,
            "startDate": "2024-08-26 00:00:00",
            "endDate": "2024-09-30 00:00:00",
            "bedNumber": "106-1"
        },
        "contract": {
            "createBy": "1",
            "createTime": "2024-08-26 15:44:08",
            "updateTime": "2024-08-26 07:44:08",
            "id": 2,
            "elderId": 326,
            "contractName": "李天龙的入住合同",
            "contractNumber": "HT202408261544080001",
            "agreementPath": "https://itheim.oss-cn-beijing.aliyuncs.com/0908707d-7ad4-4d30-ad7d-efdb9c946015.jpg",
            "thirdPartyPhone": "13211223311",
            "thirdPartyName": "李彤",
            "elderName": "李天龙",
            "startDate": "2024-08-26 00:00:00",
            "endDate": "2024-09-30 00:00:00",
            "status": 0,
            "signDate": "2024-08-26 00:00:00",
            "sortOrder": 0
        }
    }
}
```
