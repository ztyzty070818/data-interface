# 接口需求

## 销售
```
path: /api/sales
method: post

返回格式:

正确
status 200
结果
{
  result: [
      {
          //全国
          "monthTarget": 4336000,                   //月销售目标
          "yearAddupRate": 1.0181,                  //年累计达成率
          "code": 3,                                //代码
          "monthAddupCompareRate": -0.4217,         //月累计同比增长率
          "monthAddupSales": 2115149,               //月累计销售额
          "yearAddupTarget": 30388651,              //年累计销售目标    
          "dayTarget": 144828,                      //日销售目标
          "abnormal": 2041,                         //订货异常专卖店数量
          "yearAddupCompareRate": 0.05855,          //年累计同比增长率
          "yearAddupSales": 30939375,               //年累计销售额
          "name": "",                               //名称
          "dayRate": 0.8201,                        //日达成率
          "daySales": 118780,                       //日销售额
          "monthAddupRate": 0.4878,                 //月累计达成率
          "dayDiff": -26048                         //日差距或日超额
      },
      {
          "monthTarget": 152200,
          "yearAddupRate": 1.032,
          "code": 16477,
          "monthAddupCompareRate": -0.43452,
          "monthAddupSales": 66582,
          "yearAddupTarget": 1064406,
          "dayTarget": 4923,
          "abnormal": 93,
          "yearAddupCompareRate": 0.04767,
          "yearAddupSales": 1098497,
          "name": "山西分公司",
          "dayRate": 0.5646,
          "daySales": 2781,
          "monthAddupRate": 0.5646,
          "dayDiff": -2142
      }
  ]
}

错误
status 400+
{
  error: '出错原因'
}
```


## 回单
仅仅需要中国，四大和板块，对应level=1,3,4
```
path: /api/receipt
method: post

返回格式:

正确
status 200
结果
{
  result: [
    {
        "monthTarget": 746164,                  //月回单目标
        "yearAddupRate": 0.9823,                //年累计达成率
        "code": "33041****",                    //代码
        "monthRate": 0.2954,                    //月达成率
        "monthAddupCompareRate": 0.1966,        //月累计同比
        "monthAddupReciept": 220461,            //月累计回单
        "yearAddupTarget": 5074358,             //年累计回单目标
        "monthAddupAbnormal": 1.0251,           //月累计异常的40+数量
        "yearAddupDiff": -89600.11936,          //年累计差距或超额
        "yearAddupCompareRate": 0.1363,         //年累计同比
        "grade": 3,                             //级别
        "name": "四大4",                         //名称
        "monthDiff": 4226,                      //月差距或超额
        "monthAddupRate": 361                   //月累计达成率
    },
    {
        "monthTarget": 284861,
        "yearAddupRate": 0.9988,
        "code": "版块1",
        "monthRate": 0.289,
        "monthAddupCompareRate": 0.2708,
        "monthAddupReciept": 82338,
        "yearAddupTarget": 1937334,
        "monthAddupAbnormal": 1.0047,
        "yearAddupDiff": -2265,
        "yearAddupCompareRate": 0.1429,
        "grade": 4,
        "name": "版块1",
        "monthDiff": -324,
        "monthAddupRate": 138
    }
  ]
}


错误
status 400+
{
  error: '出错原因'
}
```
