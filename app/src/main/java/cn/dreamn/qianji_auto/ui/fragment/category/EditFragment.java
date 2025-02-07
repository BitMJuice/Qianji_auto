/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package cn.dreamn.qianji_auto.ui.fragment.category;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import cn.dreamn.qianji_auto.R;
import cn.dreamn.qianji_auto.core.base.Manager;
import cn.dreamn.qianji_auto.core.db.Helper.BookNames;
import cn.dreamn.qianji_auto.core.db.Helper.Caches;
import cn.dreamn.qianji_auto.core.db.Helper.Category;
import cn.dreamn.qianji_auto.core.utils.DataUtils;
import cn.dreamn.qianji_auto.core.utils.Tools;
import cn.dreamn.qianji_auto.ui.core.BaseFragment;
import cn.dreamn.qianji_auto.ui.fragment.asset.category.AdapterData;
import cn.dreamn.qianji_auto.ui.fragment.asset.category.CateChoose;
import cn.dreamn.qianji_auto.utils.tools.JsEngine;
import cn.dreamn.qianji_auto.utils.tools.Logs;

/*
import com.eclipsesource.v8.V8;*/


@Page(name = "规则编辑")
public class EditFragment extends BaseFragment {


    @BindView(R.id.regular_name)
    MaterialEditText regular_name;
    @BindView(R.id.regular_time1_link)
    RoundButton regular_time1_link;
    @BindView(R.id.regular_time1)
    MaterialEditText regular_time1;
    @BindView(R.id.regular_time2_link)
    RoundButton regular_time2_link;
    @BindView(R.id.regular_time2)
    MaterialEditText regular_time2;


    @BindView(R.id.regular_money1_link)
    RoundButton regular_money1_link;
    @BindView(R.id.regular_money1)
    MaterialEditText regular_money1;
    @BindView(R.id.regular_money2_link)
    RoundButton regular_money2_link;
    @BindView(R.id.regular_money2)
    MaterialEditText regular_money2;


    @BindView(R.id.regular_shopName_link)
    RoundButton regular_shopName_link;
    @BindView(R.id.regular_shopName)
    MaterialEditText regular_shopName;

    @BindView(R.id.regular_shopRemark_link)
    RoundButton regular_shopRemark_link;
    @BindView(R.id.regular_shopRemark)
    MaterialEditText regular_shopRemark;

    @BindView(R.id.regular_type)
    RoundButton regular_type;
    @BindView(R.id.regular_sort)
    RoundButton regular_sort;


    @BindView(R.id.btn_test)
    ButtonView btn_test;
    @BindView(R.id.btn_save)
    ButtonView btn_save;

    @BindView(R.id.regular_billtype)
    RoundButton regular_billtype;

    private int regularId = -1;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_auto_catgory_edit;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        try {
            initSet();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        initListen();
    }

    private void initSet() throws UnsupportedEncodingException {
        regular_time1_link.setText(R.string.choose_time_equal);
        regular_time2_link.setText(R.string.choose_time_equal);
        Bundle arguments = getArguments();
        if (arguments == null) return;
        String id = arguments.getString("id");
        String data = arguments.getString("data");
        if (id != null && !id.equals("")) {
            Logs.d(data);
            regularId = Integer.parseInt(id);
            DataUtils dataUtils = new DataUtils();
            dataUtils.parse(data);
            regular_name.setText(dataUtils.get("regular_name"));
            regular_time1_link.setText(dataUtils.get("regular_time1_link"));
            regular_time1.setText(dataUtils.get("regular_time1"));
            regular_time2_link.setText(dataUtils.get("regular_time2_link"));
            regular_time2.setText(dataUtils.get("regular_time2"));
            regular_shopName_link.setText(dataUtils.get("regular_shopName_link"));
            regular_shopName.setText(dataUtils.get("regular_shopName"));
            regular_shopRemark_link.setText(dataUtils.get("regular_shopRemark_link"));
            regular_shopRemark.setText(dataUtils.get("regular_shopRemark"));
            regular_type.setText(dataUtils.get("regular_type"));
            regular_sort.setText(dataUtils.get("regular_sort"));
            regular_billtype.setText(Manager.get(dataUtils.get("regular_billtype")));

            regular_money1_link.setText(dataUtils.get("regular_money1_link"));
            regular_money1.setText(dataUtils.get("regular_money1"));
            regular_money2_link.setText(dataUtils.get("regular_money2_link"));
            regular_money2.setText(dataUtils.get("regular_money2"));
        }

    }

    private void initListen() {
        regular_time1_link.setOnClickListener(v -> {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.tip_options)
                    .items(R.array.menu_values_time)
                    .itemsCallback((dialog, itemView, position, text) -> {
                        regular_time1_link.setText(text);
                    })
                    .show();
        });
        regular_time2_link.setOnClickListener(v -> {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.tip_options)
                    .items(R.array.menu_values_time)
                    .itemsCallback((dialog, itemView, position, text) -> {
                        regular_time2_link.setText(text);
                    })
                    .show();
        });

        regular_money1_link.setOnClickListener(v -> {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.tip_options)
                    .items(R.array.menu_values_time)
                    .itemsCallback((dialog, itemView, position, text) -> {
                        regular_money1_link.setText(text);
                    })
                    .show();
        });
        regular_money2_link.setOnClickListener(v -> {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.tip_options)
                    .items(R.array.menu_values_time)
                    .itemsCallback((dialog, itemView, position, text) -> {
                        regular_money2_link.setText(text);
                    })
                    .show();
        });
        regular_shopName_link.setOnClickListener(v -> {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.tip_options)
                    .items(R.array.menu_values_regular)
                    .itemsCallback((dialog, itemView, position, text) -> {
                        regular_shopName_link.setText(text);
                    })
                    .show();
        });
        regular_shopRemark_link.setOnClickListener(v -> {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.tip_options)
                    .items(R.array.menu_values_regular)
                    .itemsCallback((dialog, itemView, position, text) -> {
                        regular_shopRemark_link.setText(text);
                    })
                    .show();
        });
        regular_type.setOnClickListener(v -> {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.tip_options)
                    .items(R.array.menu_values_regular_type)
                    .itemsCallback((dialog, itemView, position, text) -> {
                        regular_type.setText(text);
                    })
                    .show();
        });
        regular_billtype.setOnClickListener(v -> {
            try {
                new MaterialDialog.Builder(getContext())
                        .title(R.string.tip_options)
                        .items(Manager.getAll())
                        .itemsCallback((dialog, itemView, position, text) -> {
                            regular_billtype.setText(text);
                        })
                        .show();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        regular_sort.setOnClickListener(v -> {

            String type = regular_type.getText().toString();
            if (BookNames.getAllLen() == 0) {
                showChoose(type, "-1", (data) -> regular_sort.setText(data));
            } else {
                BookNames.showBookSelect(getContext(), "请选择账本", bundle -> {
                    String book_id = bundle.getString("book_id");
                    if (book_id == null || book_id.equals("")) book_id = "-1";
                    showChoose(type, book_id, (data) -> regular_sort.setText(data));
                });
            }


        });
        btn_test.setOnClickListener(v -> {
            LayoutInflater factory = LayoutInflater.from(getContext());
            @SuppressLint("InflateParams") final View textEntryView = factory.inflate(R.layout.fragment_auto_catgory_edit_test, null);
            final MaterialEditText cate_time = textEntryView.findViewById(R.id.cate_time);
            final MaterialEditText cate_money = textEntryView.findViewById(R.id.cate_money);
            final MaterialEditText cate_shopName = textEntryView.findViewById(R.id.cate_shopName);
            final MaterialEditText cate_shopRemark = textEntryView.findViewById(R.id.cate_shopRemark);
            final RoundButton cate_type = textEntryView.findViewById(R.id.cate_type);
            final RoundButton cate_type2 = textEntryView.findViewById(R.id.cate_type2);
            cate_time.setText(Caches.getOneString("cate_time", Tools.getTime("yyyy-MM-dd HH:mm:ss")));
            cate_money.setText(Caches.getOneString("cate_money", "0"));
            cate_type.setText("支出");
            cate_type.setOnClickListener(v22 -> {
                new MaterialDialog.Builder(getContext())
                        .title(R.string.tip_options)
                        .items(R.array.menu_values_regular_type)
                        .itemsCallback((dialog, itemView, position, text) -> {
                            cate_type.setText(text);
                        })
                        .show();
            });
            cate_type2.setText("任意类型");
            cate_type2.setOnClickListener(v33 -> {
                try {
                    new MaterialDialog.Builder(getContext())
                            .title(R.string.tip_options)
                            .items(Manager.getAll())
                            .itemsCallback((dialog, itemView, position, text) -> {
                                cate_type2.setText(text);
                            })
                            .show();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            cate_shopName.setText(Caches.getOneString("cate_shopName", ""));
            cate_shopRemark.setText(Caches.getOneString("cate_shopRemark", ""));
            new MaterialDialog.Builder(getContext())
                    .customView(textEntryView, true)
                    .positiveText("测试")
                    .onPositive((dialog, which) -> {
                        String js = getCateJs();
                        Caches.AddOrUpdate("cate_time", cate_time.getEditValue());
                        Caches.AddOrUpdate("cate_money", cate_money.getEditValue());
                        Caches.AddOrUpdate("cate_shopName", cate_shopName.getEditValue());
                        Caches.AddOrUpdate("cate_shopRemark", cate_shopRemark.getEditValue());
                        try {
                            /*V8 runtime = V8.createV8Runtime();

                            String result = runtime.executeStringScript(Category.getOneRegularJs(js,cate_shopName.getEditValue(), cate_shopRemark.getEditValue(), cate_type.getEditValue(),cate_time.getEditValue()));
*/

                            String jsData = Category.getOneRegularJs(js, cate_shopName.getEditValue(), cate_shopRemark.getEditValue(), cate_type.getText().toString(), cate_time.getEditValue(), cate_type2.getText().toString(), cate_money.getEditValue());
                            Logs.i("当前测试js数据", jsData);

                            String result = JsEngine.run(jsData);
                            Logs.d("Qianji_Cate", "自动分类结果：" + result);
                            assert result != null;
                            new MaterialDialog.Builder(getContext())
                                    .title("自动分类结果")
                                    .content(result)
                                    .positiveText(getString(R.string.input_ok))
                                    .show();
                        } catch (Exception e) {
                            Logs.d("自动分类执行出错！" + e.toString());
                            new MaterialDialog.Builder(getContext())
                                    .title("自动分类执行出错")
                                    .content(e.toString())
                                    .positiveText(getString(R.string.input_ok))
                                    .show();
                        }
                    })
                    .show();
        });

        btn_save.setOnClickListener(v -> {
            //获取名称
            String name = regular_name.getEditValue();
            if (name.equals("")) {
                SnackbarUtils.Long(getView(), "名称不能为空").danger().show();
                return;
            }
            //获取分类
            String sort = regular_sort.getText().toString();
            if (sort.equals("")) {
                SnackbarUtils.Long(getView(), "分类不能为空").danger().show();
                return;
            }


            String regular = getCateJs();
            if (regular.equals("")) return;
            DataUtils dataUtils = new DataUtils();

            dataUtils.put("regular_billtype", regular_billtype.getText());

            dataUtils.put("regular_name", regular_name.getEditValue());
            dataUtils.put("regular_time1_link", regular_time1_link.getText());
            dataUtils.put("regular_time1", regular_time1.getEditValue());
            dataUtils.put("regular_time2_link", regular_time2_link.getText());
            dataUtils.put("regular_time2", regular_time2.getEditValue());

            dataUtils.put("regular_money1_link", regular_money1_link.getText());
            dataUtils.put("regular_money1", regular_money1.getEditValue());
            dataUtils.put("regular_money2_link", regular_money2_link.getText());
            dataUtils.put("regular_money2", regular_money2.getEditValue());

            dataUtils.put("regular_shopName_link", regular_shopName_link.getText());
            dataUtils.put("regular_shopName", regular_shopName.getEditValue());
            dataUtils.put("regular_shopRemark_link", regular_shopRemark_link.getText());
            dataUtils.put("regular_shopRemark", regular_shopRemark.getEditValue());

            dataUtils.put("regular_type", regular_type.getText());
            dataUtils.put("regular_sort", regular_sort.getText());


            if (regularId != -1) {
                Category.changeCategory(regularId, regular, name, sort, dataUtils.toString());
            } else {
                Category.addCategory(regular, name, sort, dataUtils.toString());
            }
            popToBack("CategoryFragment", null);
        });

    }

    private String getCateJs() {
        //获取名称
        String name = regular_name.getEditValue();
        if (name.equals("")) {
            SnackbarUtils.Long(getView(), "名称不能为空").danger().show();
            return "";
        }
        //获取分类
        String sort = regular_sort.getText().toString();
        if (sort.equals("")) {
            SnackbarUtils.Long(getView(), "分类不能为空").danger().show();
            return "";
        }


        String regularList = "";
        //时间获取
        if (!regular_time1.getEditValue().equals(""))
            regularList += String.format("time %s %s && ", regular_time1_link.getText().toString(), regular_time1.getEditValue());
        if (!regular_time2.getEditValue().equals(""))
            regularList += String.format("time %s %s && ", regular_time2_link.getText().toString(), regular_time2.getEditValue());

        //时间获取
        if (!regular_money1.getEditValue().equals(""))
            regularList += String.format("money %s %s && ", regular_money1_link.getText().toString(), regular_money1.getEditValue());
        if (!regular_money2.getEditValue().equals(""))
            regularList += String.format("money %s %s && ", regular_money2_link.getText().toString(), regular_money2.getEditValue());

        String shopName = regular_shopName.getEditValue();
        String shopNameLink = regular_shopName_link.getText().toString();
        if (!shopName.equals("")) {
            if (shopNameLink.equals(getString(R.string.choose_regular_contains))) {
                regularList += String.format("shopName.indexOf('%s')!=-1 && ", shopName);
            } else if (shopNameLink.equals(getString(R.string.choose_regular_equal))) {
                regularList += String.format("shopName == '%s' &&", shopName);
            } else if (shopNameLink.equals(getString(R.string.choose_regular_startOf))) {
                regularList += String.format("shopName.startsWith('%s') && ", shopName);
            } else if (shopNameLink.equals(getString(R.string.choose_regular_endOf))) {
                regularList += String.format("shopName.endsWith('%s') && ", shopName);
            } else if (shopNameLink.equals(getString(R.string.choose_regular_regex))) {
                regularList += String.format("(/%s/g).test(shopName) && ", shopName);
            }
        }
        String shopRemark = regular_shopRemark.getEditValue();
        String shopRemarkLink = regular_shopRemark_link.getText().toString();
        if (!shopRemark.equals("")) {
            if (shopRemarkLink.equals(getString(R.string.choose_regular_contains))) {
                regularList += String.format("shopRemark.indexOf('%s')!=-1 && ", shopRemark);
            } else if (shopRemarkLink.equals(getString(R.string.choose_regular_equal))) {
                regularList += String.format("shopRemark == '%s' &&", shopRemark);
            } else if (shopRemarkLink.equals(getString(R.string.choose_regular_startOf))) {
                regularList += String.format("shopRemark.startsWith('%s') && ", shopRemark);
            } else if (shopRemarkLink.equals(getString(R.string.choose_regular_endOf))) {
                regularList += String.format("shopRemark.endsWith('%s') && ", shopRemark);
            } else if (shopRemarkLink.equals(getString(R.string.choose_regular_regex))) {
                regularList += String.format("(/%s/g).test(shopRemark) && ", shopRemark);
            }
        }
        //获取类型
        String type = regular_type.getText().toString();
        if (!type.equals(getString(R.string.choose_type_null))) {
            regularList += String.format("type == '%s' && ", type);
        }
//获取类型
        String type2 = regular_billtype.getText().toString();
        if (!type2.equals(getString(R.string.choose_type_null))) {
            regularList += String.format("source == '%s' && ", type2);
        }

        int last = regularList.lastIndexOf('&');
        if (last != -1 && last != 0)
            regularList = regularList.substring(0, last - 1);

        if (regularList.equals("")) {
            SnackbarUtils.Long(getView(), "规则填写错误:条件内容为空。").danger().show();
            return "";
        }
        //获取规则

        //  Logs.i("当前测试规则",regular);


        return String.format("if(%s)return '%s';", regularList, sort);


    }

    interface Choose {
        void onChoose(String data);
    }

    private void showChoose(String type, String book_id, Choose choose) {


        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cate_choose, null);

        ExpandableListView expandableListView = view.findViewById(R.id.expandableListViewData);

        CateChoose cateChoose = new CateChoose(expandableListView, getContext(), type, false, book_id);

        cateChoose.refresh();

        cateChoose.setOnClick(new CateChoose.CallBack() {
            @Override
            public void OnLongClickGroup(Bundle parent) {
                choose.onChoose(parent.getString("name"));
                dialog.cancel();
            }

            @Override
            public void OnLongClickChild(Bundle parent, Bundle child) {
                choose.onChoose(child.getString("name"));
                dialog.cancel();
            }


            @Override
            public void OnClickChild(Bundle parent, Bundle child) {
                choose.onChoose(child.getString("name"));
                dialog.cancel();
            }
        });

        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


}
