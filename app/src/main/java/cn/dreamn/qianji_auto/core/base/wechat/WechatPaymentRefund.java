/*
 * Copyright (C) 2021 dreamn(dream@dreamn.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.dreamn.qianji_auto.core.base.wechat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.dreamn.qianji_auto.core.base.Analyze;
import cn.dreamn.qianji_auto.core.utils.BillInfo;
import cn.dreamn.qianji_auto.core.utils.BillTools;
import cn.dreamn.qianji_auto.utils.tools.Logs;


/**
 * 微信支付成功
 */
public class WechatPaymentRefund extends Analyze {

    private static WechatPaymentRefund paymentSuccess;

    public static WechatPaymentRefund getInstance() {
        if (paymentSuccess != null) return paymentSuccess;
        paymentSuccess = new WechatPaymentRefund();
        return paymentSuccess;
    }


    @Override
    public BillInfo tryAnalyze(String content, String source) {
        BillInfo billInfo = super.tryAnalyze(content, source);

        if (billInfo == null) return null;
        if (billInfo.getShopRemark() == null || billInfo.getShopRemark().equals(""))
            billInfo.setShopRemark("微信支付退款");

        billInfo.setSilent(true);
        billInfo.setType(BillInfo.TYPE_INCOME);

        return billInfo;
    }

    @Override
    public BillInfo getResult(BillInfo billInfo) {
        String money = BillTools.getMoney(jsonObject.getJSONObject("topline").getJSONObject("value").getString("word"));
        billInfo.setMoney(BillTools.getMoney(money));
        billInfo.setShopAccount("微信支付");
        try {
            JSONArray line = jsonObject.getJSONObject("lines").getJSONArray("line");
            for (int i = 0; i < line.size(); i++) {
                JSONObject jsonObject1 = line.getJSONObject(i);
                String key = jsonObject1.getJSONObject("key").getString("word");
                String value = jsonObject1.getJSONObject("value").getString("word");

                switch (key) {
                    case "退款方式":
                        billInfo.setAccountName(value);
                        break;
                    case "商品详情":
                        billInfo.setShopRemark(value);
                    case "商户名称":
                        billInfo.setShopAccount(value);
                        break;
                }
            }
        } catch (Exception e) {
            Logs.i("解析json出现了错误！\n json数据：" + jsonObject.toJSONString() + "\n 错误：" + e.toString());
        }


        return billInfo;
    }
}
