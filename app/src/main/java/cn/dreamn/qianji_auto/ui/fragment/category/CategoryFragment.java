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

package cn.dreamn.qianji_auto.ui.fragment.category;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.thl.filechooser.FileChooser;
import com.thl.filechooser.FileInfo;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import cn.dreamn.qianji_auto.R;
import cn.dreamn.qianji_auto.core.db.Helper.Smses;
import cn.dreamn.qianji_auto.core.db.Table.Regular;
import cn.dreamn.qianji_auto.core.utils.App;
import cn.dreamn.qianji_auto.core.db.Helper.Category;
import cn.dreamn.qianji_auto.core.utils.Tools;
import cn.dreamn.qianji_auto.ui.adapter.CateAdapter;
import cn.dreamn.qianji_auto.ui.adapter.SmsAdapter;
import cn.dreamn.qianji_auto.ui.fragment.StateFragment;
import cn.dreamn.qianji_auto.utils.tools.FileUtils;
import cn.dreamn.qianji_auto.utils.tools.Logs;

import static cn.dreamn.qianji_auto.ui.adapter.CateAdapter.KEY_DATA;
import static cn.dreamn.qianji_auto.ui.adapter.CateAdapter.KEY_DENY;
import static cn.dreamn.qianji_auto.ui.adapter.CateAdapter.KEY_ID;
import static cn.dreamn.qianji_auto.ui.adapter.CateAdapter.KEY_JS;
import static cn.dreamn.qianji_auto.ui.adapter.CateAdapter.KEY_TITLE;
import static cn.dreamn.qianji_auto.ui.adapter.CateAdapter.KEY_VALUE;


@Page(name = "自动分类")
public class CategoryFragment extends StateFragment {

    @BindView(R.id.map_layout)
    SwipeRefreshLayout map_layout;
    @BindView(R.id.recycler_view)
    SwipeRecyclerView recyclerView;
    private CateAdapter mAdapter;
    private List<Map<String, String>> mDataList;
    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {

        showLoading("加载中...");

        WidgetUtils.initRecyclerView(recyclerView);
        mAdapter = new CateAdapter();
        recyclerView.setAdapter(mAdapter);

        recyclerView.setLongPressDragEnabled(true);
        recyclerView.setOnItemMoveListener(new OnItemMoveListener() {
            @Override
            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                // 此方法在Item拖拽交换位置时被调用。
                // 第一个参数是要交换为之的Item，第二个是目标位置的Item。

                // 交换数据，并更新adapter。
                int fromPosition = srcHolder.getAdapterPosition();
                int toPosition = targetHolder.getAdapterPosition();
                Collections.swap(mDataList, fromPosition, toPosition);
                mAdapter.notifyItemMoved(fromPosition, toPosition);
                //  Logs.d(mDataList.get(toPosition).get(SmsAdapter.KEY_TITLE)+"key id"+mDataList.get(toPosition).get(SmsAdapter.KEY_ID)+" to"+toPosition);
                Category.setSort(Integer.parseInt(mDataList.get(fromPosition).get(SmsAdapter.KEY_ID)), fromPosition);
                Category.setSort(Integer.parseInt(mDataList.get(toPosition).get(SmsAdapter.KEY_ID)), toPosition);

                // 返回true，表示数据交换成功，ItemView可以交换位置。
                return true;
            }

            @Override
            public void onItemDismiss(RecyclerView.ViewHolder viewHolder) {

            }

        });// 监听拖拽，更新UI。

        mAdapter.setOnItemClickListener(item -> new MaterialDialog.Builder(getContext())
                .title(R.string.tip_options)
                .items(R.array.menu_values_req)
                .itemsCallback((dialog, itemView, position, text) -> {
                    int id = Integer.parseInt(Objects.requireNonNull(item.get(KEY_ID)));
                    if (position == 0) {
                        Category.del(id);
                        SnackbarUtils.Long(getView(), getString(R.string.del_success)).info().show();
                        refresh();
                    } else if (position == 1) {
                        if (item.get(KEY_DATA).equals("")) {
                            SnackbarUtils.Long(getView(), getString(R.string.dontAllow)).info().show();
                            return;
                        }
                        Bundle params = new Bundle();
                        params.putString("id", String.valueOf(id));

                        params.putString("data", item.get(KEY_DATA));
                        openPage(EditFragment.class, params);
                    } else if (position == 2) {
                        Bundle params = new Bundle();
                        params.putString("id", String.valueOf(id));
                        params.putString("data", item.get(KEY_JS));
                        params.putString("name", item.get(KEY_TITLE));
                        openPage(JsFragment.class, params);
                    } else if (position == 3) {
                        Category.deny(id);
                        SnackbarUtils.Long(getView(), getString(R.string.deny_success)).info().show();
                        refresh();
                    } else if (position == 4) {
                        Category.enable(id);
                        SnackbarUtils.Long(getView(), getString(R.string.enable_success)).info().show();
                        refresh();
                    }

                })
                .show());
        //下拉刷新
        map_layout.setOnRefreshListener(this::loadData);
        refresh(); //第一次进入触发自动刷新，演示效果
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        refresh();
    }


    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();

        titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_add) {
            @Override
            public void performAction(View view) {


                new MaterialDialog.Builder(getContext())
                        .title(R.string.tip_options)
                        .items(R.array.menu_values_cate)
                        .itemsCallback((dialog, itemView, position, text) -> {

                            if (position == 0) {
                                openPage(EditFragment.class, true);
                            } else if (position == 1) {
                                openPage(JsFragment.class, true);
                            }

                        })
                        .show();
                /**
                 * // 设置需要传递的参数
                 * Bundle params = new Bundle();
                 * params.putBoolean(DateReceiveFragment.KEY_IS_NEED_BACK, false);
                 * int id = (int) (Math.random() * 100);
                 * params.putString(DateReceiveFragment.KEY_EVENT_NAME, "事件" + id);
                 * params.putString(DateReceiveFragment.KEY_EVENT_DATA, "事件" + id + "携带的数据");
                 */
            }
        });
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_import) {
            @Override
            public void performAction(View view) {

                new MaterialDialog.Builder(getContext())
                        .title("请选择")
                        .items(R.array.menu_values_import)
                        .itemsCallback((dialog, itemView, position, text) -> {
                            if (position == 0) {
                                try {
                                    int allowVersion = 49;
                                    FileChooser fileChooser = new FileChooser(getActivity(), filePath -> {

                                        String data = FileUtils.get(filePath.get(0).getFilePath());
                                        JSONObject jsonObject = JSONObject.parseObject(data);
                                        int version = jsonObject.getIntValue("version");
                                        String from = jsonObject.getString("from");
                                        if (version < allowVersion) {
                                            SnackbarUtils.Long(getView(), "不支持该版本的配置恢复").info().show();
                                            return;
                                        }
                                        if (!from.equals("Category")) {
                                            SnackbarUtils.Long(getView(), "该文件不是有效的自动分类配置数据文件").info().show();
                                            return;
                                        }

                                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                                        new MaterialDialog.Builder(getContext())
                                                .title("恢复提醒")
                                                .content("是否覆盖旧数据（清空所有数据不做保留）？")
                                                .positiveText("确定")
                                                .onPositive((dialog2, which) -> Category.clear())
                                                .negativeText("取消")
                                                .onAny((dialog3, which) -> {
                                                    for (int i = 0; i < jsonArray.size(); i++) {
                                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                        Category.addCategory(jsonObject1.getString("regular"), jsonObject1.getString("name"), jsonObject1.getString("cate"), jsonObject1.getString("tableList"));
                                                    }
                                                    SnackbarUtils.Long(getView(), "恢复成功").info().show();
                                                    refresh();
                                                })
                                                .show();


                                    });

                                    fileChooser.setTitle("请选择自动分类配置数据文件");
                                    fileChooser.setDoneText("确定");


                                    fileChooser.setChooseType(FileInfo.FILE_TYPE_AUTOJSON);
                                    fileChooser.open();


                                } catch (Exception e) {
                                    SnackbarUtils.Long(getView(), "不是自动记账所支持的恢复文件，请重新选择。").info().show();
                                }
                                //导入
                            } else if (position == 1) {
                                //导出
                                Regular[] regulars = Category.getAll();
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("version", App.getAppVerCode());
                                    jsonObject.put("from", "Category");
                                    JSONArray jsonArray = new JSONArray();
                                    for (Regular regular : regulars) {
                                        JSONObject jsonObject1 = new JSONObject();
                                        jsonObject1.put("name", regular.name);
                                        jsonObject1.put("regular", regular.regular);
                                        jsonObject1.put("tableList", regular.tableList);
                                        jsonObject1.put("cate", regular.cate);
                                        jsonArray.add(jsonObject1);
                                    }
                                    jsonObject.put("data", jsonArray);
                                    Tools.writeToCache(getContext(), "regular.autoJson", jsonObject.toJSONString());
                                    Tools.shareFile(getContext(), getContext().getExternalCacheDir().getPath() + "/regular.autoJson");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).show();

            }
        });

        return titleBar;


    }


    private void loadData() {
        new Handler().postDelayed(() -> {
            // mStatefulLayout.showLoading("正在加载分类规则");
            Regular[] regulars = Category.getAll();
            mDataList = new ArrayList<>();
            for (Regular regular : regulars) {
                Map<String, String> item = new HashMap<>();
                item.put(KEY_TITLE, regular.name);
                item.put(KEY_VALUE, regular.cate);
                item.put(KEY_DENY, regular.use == 1 ? "false" : "true40");
                item.put(KEY_ID, String.valueOf(regular.id));
                item.put(KEY_DATA, regular.tableList);
                item.put(KEY_JS, regular.regular);
                mDataList.add(item);
            }
            if (mDataList.size() == 0) {
                showEmpty("没有任何分类规则");
                return;
            }

            mAdapter.refresh(mDataList);
            if (map_layout != null) {
                map_layout.setRefreshing(false);
            }
            showContent();
        }, 1000);
    }

    @Override
    protected void initListeners() {

    }

    private void refresh() {
        map_layout.setRefreshing(true);
        loadData();
    }

}
