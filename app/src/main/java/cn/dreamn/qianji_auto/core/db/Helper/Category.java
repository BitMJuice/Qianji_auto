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

package cn.dreamn.qianji_auto.core.db.Helper;

import android.annotation.SuppressLint;

import com.xuexiang.xutil.data.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.dreamn.qianji_auto.core.db.DbManger;
import cn.dreamn.qianji_auto.core.db.Table.Regular;
import cn.dreamn.qianji_auto.core.utils.BillInfo;
import cn.dreamn.qianji_auto.core.utils.DataUtils;
import cn.dreamn.qianji_auto.core.utils.Tools;
import cn.dreamn.qianji_auto.utils.tools.JsEngine;
import cn.dreamn.qianji_auto.utils.tools.Logs;


public class Category {

    public static String getCategory(BillInfo billInfo) {
        try {
           /* V8 runtime = V8.createV8Runtime();
            String result = runtime.executeStringScript(getCategoryRegularJs(shopAccount, shopRemark, type));
*/
            String result = JsEngine.run(getCategoryRegularJs(billInfo.getShopAccount(), billInfo.getShopRemark(), BillInfo.getTypeName(billInfo.getType(true)), billInfo.getSource(), billInfo.getMoney(), billInfo.getTime()));

            Logs.d("Qianji_Cate", "自动分类结果：" + result);
            return result;
        } catch (Exception e) {
            Logs.i("自动分类执行出错！" + e.toString());
            return "NotFind";
        }

    }

    public static void setCateJs(BillInfo billInfo, String sort) {
        //这两种类型不需要
        if (billInfo.getType(true).equals(BillInfo.TYPE_CREDIT_CARD_PAYMENT) || billInfo.getType().equals(BillInfo.TYPE_TRANSFER_ACCOUNTS)) {
            return;
        }
        // String time = Tools.getTime("HH");
        String name = "[自动生成]" + billInfo.getSource();
        // String sort = "其它";
        String str = "";

        //    str += String.format("time = %s && ", time);
        str += String.format("shopName.indexOf('%s')!=-1 && ", billInfo.getShopAccount());
        str += String.format("shopRemark.indexOf('%s')!=-1 && ", billInfo.getShopRemark());
        str += String.format("type == '%s' && ", BillInfo.getTypeName(billInfo.getType(true)));
        str += String.format("source == '%s' && ", billInfo.getSource());

        String regular = "if(%s)return '%s';";

        int last = str.lastIndexOf('&');
        if (last != -1 && last != 0)
            str = str.substring(0, last - 1);

        regular = String.format(regular, str, sort);

        DataUtils dataUtils = new DataUtils();

        dataUtils.put("regular_billtype", billInfo.getSource());

        dataUtils.put("regular_name", "[自动生成]" + billInfo.getSource());
        dataUtils.put("regular_time1_link", "");
        dataUtils.put("regular_time1", "");
        dataUtils.put("regular_time2_link", "");
        dataUtils.put("regular_time2", "");
        dataUtils.put("regular_money1_link", "");
        dataUtils.put("regular_money1", "");
        dataUtils.put("regular_money2_link", "");
        dataUtils.put("regular_money2", "");

        dataUtils.put("regular_shopName_link", "包含");
        dataUtils.put("regular_shopName", billInfo.getShopAccount());
        dataUtils.put("regular_shopRemark_link", "包含");
        dataUtils.put("regular_shopRemark", billInfo.getShopRemark());

        dataUtils.put("regular_type", BillInfo.getTypeName(billInfo.getType(true)));
        dataUtils.put("regular_sort", sort);

        Category.addCategory(regular, name, sort, dataUtils.toString());

    }

    //获取所有的js
    @SuppressLint("SimpleDateFormat")
    public static String getCategoryRegularJs(String shopAccount, String shopRemark, String type, String source, String money, String time) {
        if (shopAccount == null) shopAccount = "";
        if (shopRemark == null) shopRemark = "";
        StringBuilder regList = new StringBuilder();
        Regular[] regular = DbManger.db.RegularDao().load();
        for (Regular value : regular) {
            regList.append(value.regular);
        }

        //   type = BillInfo.getTypeName(type);

        // 没有时间的时候要取当前时间
        if (time == null) {
            time = Tools.getTime("yyyy-MM-dd HH:mm:ss");
        }
        //NotFind
        String js = "function getCategory(shopName,shopRemark,type,year,month,day,hour,minute,second,source,money){ try{ %s return 'NotFind';}catch(e){ return 'NotFind'; } } var result = getCategory('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'); result == null ? 'NotFind' : result; ";
        // 格式化时间 用于传参给js
        Date date = DateUtils.string2Date(time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        Calendar calendar = Calendar.getInstance();
        // 这里要重复判断一下  防止给进来的时间格式无法解析
        if (date == null) {
            date = new Date();
        }
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        // 注意:1月份的值为0，需加1为现实中月份
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        // 24小时制
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        //yyyy-MM-dd HH:mm:ss
        return String.format(js, regList.toString(), shopAccount, shopRemark, type, year, month, day, hour, minute, second, source, money);
    }

    //获取所有的js
    @SuppressLint("SimpleDateFormat")
    public static String getOneRegularJs(String jsData, String shopAccount, String shopRemark, String type, String time, String source, String money) {
        if (shopAccount == null) shopAccount = "";
        if (shopRemark == null) shopRemark = "";

        //type = BillInfo.getTypeName(type);

        // 没有时间的时候要取当前时间
        if (time == null) {
            time = Tools.getTime("yyyy-MM-dd HH:mm:ss");
        }

        String js = "function getCategory(shopName,shopRemark,type,year,month,day,hour,minute,second,source,money){%s return '其它';} getCategory('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s');";
        // 格式化时间 用于传参给js
        Date date = DateUtils.string2Date(time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        Calendar calendar = Calendar.getInstance();
        // 这里要重复判断一下  防止给进来的时间格式无法解析
        if (date == null) {
            date = new Date();
        }
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        // 注意:1月份的值为0，需加1为现实中月份
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        // 24小时制
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        return String.format(js, jsData, shopAccount, shopRemark, type, year, month, day, hour, minute, second, source, money);
    }

    /**
     * js demo
     *  if(type==0)return "啊啊啊"
     *     if(shopName.startsWith("王记"))return "早餐"
     *     if(shopRemark.endsWith("迎选购"))return "主主主主"
     *     if(shopRemark.indexOf("迎选购")!=-1)return "主12主主主"
     *     if(shopRemark=="新老顾客欢")return "滚滚"
     *     if((/新老/g).test(shopName))return "ddd"
     *     return "其它"
     */
    /**
     * js demo
     * if(title.contents("123"))//标题 contents 、not contents、indexof、endof、regular（匹配到）
     * * if(sub.contents("123"))//副标题
     * * if(time>200 && time <100)//时间 < 、>、=
     * * return "okk"
     */

    public static Regular[] getAll() {
        return DbManger.db.RegularDao().loadAll();
    }

    public static void deny(int id) {
        DbManger.db.RegularDao().deny(id);
    }

    public static void enable(int id) {
        DbManger.db.RegularDao().enable(id);
    }

    public static Regular[] getOne(int id) {
        return DbManger.db.RegularDao().getOne(id);
    }

    public static void addCategory(String js, String name, String cate, String tableList) {
        DbManger.db.RegularDao().add(js, name, cate, tableList);
    }

    public static void changeCategory(int id, String js, String name, String cate, String tableList) {
        DbManger.db.RegularDao().update(id, js, name, cate, tableList);
    }

    public static void del(int id) {
        DbManger.db.RegularDao().delete(id);
    }

    public static void setSort(int id, int sort) {
        DbManger.db.RegularDao().setSort(id, sort);
    }


    public static void clear() {
        DbManger.db.RegularDao().clean();
    }
}
