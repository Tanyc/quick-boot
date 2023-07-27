package com.quick.modules.util;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * AMIS 页面配置 生成工具类
 */
public class AMISGeneratorUtils {

    /**
     * 生成 schema
     *
     * @return
     */
    public static String generateAMISCrudSchema(String aliasTableName, List<Map<String, Object>> fieldDetails) {
        JSONObject schema = new JSONObject();
        schema.put("id", "u:" + IdUtil.simpleUUID());
        schema.put("body", body(aliasTableName, fieldDetails));
        schema.put("type", "page");
        schema.put("regions", new JSONArray("body"));
        schema.put("toolbar", new JSONArray());
        JSONObject pullRefresh = new JSONObject();
        pullRefresh.put("disabled", true);
        schema.put("pullRefresh", pullRefresh);
        schema.put("asideResizor", false);
        JSONObject style = new JSONObject();
        style.put("boxShadow", " 0px 0px 0px 0px transparent");
        schema.put("style", style);
        return schema.toJSONString();
    }

    public static JSONArray body(String aliasTableName, List<Map<String, Object>> fieldDetails) {
        JSONArray body = new JSONArray();
        body.add(crud(aliasTableName, fieldDetails));
        return body;
    }

    /**
     * body->crud
     */
    public static JSONObject crud(String aliasTableName, List<Map<String, Object>> fieldDetails) {
        JSONObject crud = new JSONObject();
        crud.put("id", "u:" + IdUtil.simpleUUID());
        crud.put("api", api(aliasTableName));
        crud.put("type", "crud");
        crud.put("pageField", pageField());
        crud.put("perPageField", perPageField());
        crud.put("perPageAvailable", perPageAvailable());
        crud.put("columns", columns(aliasTableName,fieldDetails));
        crud.put("features", features());
        crud.put("filter", filter(fieldDetails));
        crud.put("filterEnabledList", filterEnabledList(fieldDetails));
        crud.put("filterSettingSource", filterSettingSource(fieldDetails));
        crud.put("bulkActions", bulkActions(aliasTableName));
        crud.put("headerToolbar", headerToolbar(aliasTableName,fieldDetails));
        crud.put("footerToolbar", footerToolbar());
        crud.put("itemActions", itemActions());
        return crud;
    }

    /**
     * crud->api
     * 格式
     * {
     * "url":"/api/system/SysUser/page",
     * "method":"post",
     * "adaptor":"",
     * "messages":{
     * <p>
     * },
     * "requestAdaptor":"api.data.model={\"username\":api.data.username,\"realName\":api.data.realName};\r\nreturn api;"
     * }
     */
    public static JSONObject api(String aliasTableName) {
        JSONObject api = new JSONObject();
        api.put("url", "/api/system/Online/Api/get");
        api.put("method", "post");
        api.put("requestAdaptor", pageRequestAdaptor(aliasTableName));
        api.put("adaptor", pageAdaptor(aliasTableName));

        return api;
    }

    /**
     * 分页请求适配器
     * api->requestAdaptor
     */
    public static String pageRequestAdaptor(String aliasTableName) {
        // 转成 APIJONS 分页查询参数
        String requestAdaptor = "api.data={\n" +
                "  \"[]\": {\n" +
                "    \"" + aliasTableName + "\": {\n" +
                "       },\n" +
                "    \"page\": (api.data.page-1),\n" +
                "    \"count\": api.data.count,\n" +
                "    \"query\": 2\n" +
                "  },\n" +
                " \"total@\": \"/[]/total\",\n" +
                " \"info@\": \"/[]/info\"\n" +
                "};\r\nreturn api;";
        return requestAdaptor;
    }

    /**
     * 新增请求适配器
     * api->requestAdaptor
     */
    public static String addRequestAdaptor(String aliasTableName) {
        // 转成 APIJONS 新增参数
        String requestAdaptor =  "api.data = {\n" +
                "\""+aliasTableName+"\": api.data,\n" +
                "  \"tag\": \""+aliasTableName+"\"\n" +
                "}\n" +
                "return api;";
        return requestAdaptor;
    }


    /**
     * 分页接收适配器
     *
     * @return
     */
    public static String pageAdaptor(String aliasTableName) {
        String adaptor = "return {\r\n    " +
                "\"status\": payload.status,\r\n    " +
                "\"msg\": payload.msg,\r\n    " +
                "\"data\": {\r\n        " +
                "\"items\": payload.data[\"[]\"].map(obj => obj." + aliasTableName + "),\r\n        " +
                "\"total\": payload.data.total\r\n    " +
                "}" +
                "\r\n}";
        return adaptor;
    }


    /**
     * crud->columns
     * 格式
     * {
     * "id":"u:9eabdbc8bcda",
     * "name":"createTime",
     * "type":"text",
     * "label":"创建时间"
     * }
     * 通过读取表结构来构建
     */
    public static JSONArray columns(String aliasTableName,List<Map<String, Object>> fieldDetails) {
        JSONArray columns = new JSONArray();

        for (Map<String, Object> fieldDetail : fieldDetails) {

            // 忽略删除标记字段
            if ("del_flag".equals(fieldDetail.get("Field").toString())) {
                continue;
            }

            JSONObject column = new JSONObject();
            column.put("id", "u:" + IdUtil.simpleUUID());
            column.put("type", "text");
            // 取数据库字段名
            column.put("name", fieldDetail.get("Field"));
            // 取数据库字段备注
            if (fieldDetail.get("Comment") != null) {
                column.put("label", fieldDetail.get("Comment"));
            } else {
                column.put("label", fieldDetail.get("Field"));
            }
            columns.add(column);
        }

        // 操作栏
        columns.add(operation(aliasTableName,fieldDetails));

        return columns;
    }

    /**
     * columns->operation
     */
    public static JSONObject operation(String aliasTableName,List<Map<String, Object>> fieldDetails) {
        // 操作栏
        JSONObject operation = new JSONObject();
        operation.put("id", "u:" + IdUtil.simpleUUID());
        operation.put("type", "operation");

        JSONArray buttons = new JSONArray();
        buttons.add(operationEdit(aliasTableName,fieldDetails));
        buttons.add(operationDelete(aliasTableName));

        operation.put("buttons", buttons);

        return operation;
    }

    /**
     * operation->edit
     */
    public static JSONObject operationEdit(String aliasTableName,List<Map<String, Object>> fieldDetails) {

        JSONObject edit = new JSONObject();
        edit.put("id", "u:" + IdUtil.simpleUUID());
        edit.put("type", "button");
        edit.put("label", "编辑");
        edit.put("level", "link");
        edit.put("hiddenOn", "${!ARRAYINCLUDES(permsCode,'"+aliasTableName+":update')}");
        edit.put("editorState", "default");

        JSONObject onEvent = new JSONObject();
        JSONObject click = new JSONObject();
        click.put("weight",0);
        JSONArray actions = new JSONArray();

        JSONObject action = new JSONObject();

        JSONObject drawer = new JSONObject();
        drawer.put("id", "u:" + IdUtil.simpleUUID());
        drawer.put("type", "drawer");
        drawer.put("title", "编辑");
        drawer.put("resizable", false);

        JSONObject body = new JSONObject();
        body.put("id", "u:" + IdUtil.simpleUUID());
        JSONObject api = new JSONObject();
        api.put("url", "/api/system/Online/Api/put");
        api.put("method", "post");
        api.put("adaptor", "");
        api.put("requestAdaptor", "api.data = {\n" +
                "\""+aliasTableName+"\": api.data,\n" +
                "  \"tag\": \""+aliasTableName+"\"\n" +
                "}\n" +
                "return api;");


        body.put("api", api);
        body.put("type", "form");

        JSONObject initApi = new JSONObject();
        initApi.put("url", "/api/system/Online/Api/get");
        initApi.put("method", "post");

        String initApiAdaptor = "return {\r\n    " +
                "\"status\": payload.status,\r\n    " +
                "\"msg\": payload.msg,\r\n    " +
                "\"data\": payload.data." + aliasTableName + "\r\n        " +
                "\r\n}";

        initApi.put("adaptor", initApiAdaptor);
        initApi.put("requestAdaptor", "api.data = {\n" +
                "\""+aliasTableName+"\": {\n" +
                "        \"id\": api.data.id\n" +
                "    }\n" +
                "}\n" +
                "return api;");

        body.put("initApi", initApi);

        JSONArray form = new JSONArray();
        for (Map<String, Object> fieldDetail : fieldDetails) {
            // 忽略字段
            if ("del_flag".equals(fieldDetail.get("Field").toString())
                    || "create_time".equals(fieldDetail.get("Field").toString())
                    || "create_by".equals(fieldDetail.get("Field").toString())
                    || "update_time".equals(fieldDetail.get("Field").toString())
                    || "update_by".equals(fieldDetail.get("Field").toString())
            ) {
                continue;
            }
            JSONObject column = new JSONObject();
            column.put("id", "u:" + IdUtil.simpleUUID());
            column.put("type", "input-text");
            column.put("editorPath", "input.base.default");
            column.put("editorPath", "input.base.default");
            column.put("editorState", "default");
            column.put("name", fieldDetail.get("Field"));
            column.put("label", fieldDetail.get("Comment"));
            if("id".equals(fieldDetail.get("Field").toString())){
                column.put("hidden", true);
            }
            form.add(column);
        }

        body.put("body", form);

        drawer.put("body",new JSONArray(body));

        action.put("drawer",drawer);
        action.put("actionType","drawer");

        actions.add(action);
        click.put("actions",actions);
        onEvent.put("click",click);
        edit.put("onEvent", onEvent);

        return edit;
    }

    /**
     * operation->delete
     */
    public static JSONObject operationDelete(String aliasTableName) {

        JSONObject delete = new JSONObject();
        delete.put("id", "u:" + IdUtil.simpleUUID());
        delete.put("type", "button");
        delete.put("label", "删除");
        delete.put("level", "link");
        delete.put("hiddenOn", "${!ARRAYINCLUDES(permsCode,'"+aliasTableName+":delete')}");
        delete.put("editorState", "default");

        JSONObject api = new JSONObject();
        api.put("url", "/api/system/Online/Api/delete");
        api.put("method", "post");
        api.put("adaptor", "");
        api.put("requestAdaptor", "api.data = {\n" +
                "\""+aliasTableName+"\": {\n" +
                "        \"id\": api.data.id\n" +
                "    },\n" +
                "  \"tag\": \""+aliasTableName+"\"\n" +
                "}\n" +
                "return api;");

        delete.put("api",api);
        delete.put("className","text-danger");
        delete.put("actionType","ajax");
        delete.put("confirmText","确定要删除？");

        return delete;
    }


    /**
     * crud->features
     */
    public static JSONArray features() {
        return new JSONArray("create", "filter", "bulkDelete", "update", "view", "delete");
    }

    /**
     * crud->messages
     */
    public static void messages() {

    }

    /**
     * 批量删除
     * crud->bulkActions
     */
    public static JSONArray bulkActions(String aliasTableName) {
        JSONObject bulkActions = new JSONObject();
        bulkActions.put("id", "u:" + IdUtil.simpleUUID());
        bulkActions.put("type", "button");
        bulkActions.put("label", "批量删除");
        bulkActions.put("level", "danger");
        bulkActions.put("hiddenOn", "${!ARRAYINCLUDES(permsCode,'"+aliasTableName+":delete')}");
        bulkActions.put("editorState", "default");

        JSONObject api = new JSONObject();
        api.put("url", "/api/system/Online/Api/delete");
        api.put("method", "post");
        api.put("adaptor", "");
        api.put("requestAdaptor", "api.data = {\n" +
                "\""+aliasTableName+"\": {\n" +
                "        \"id{}\": api.data.ids.split(\",\")\n" +
                "    },\n" +
                "  \"tag\": \""+aliasTableName+"\"\n" +
                "}\n" +
                "return api;");

        bulkActions.put("api",api);
        bulkActions.put("actionType","ajax");
        bulkActions.put("confirmText","确定要删除？");

        return new JSONArray(bulkActions);

    }

    /**
     * crud->itemActions
     */
    public static JSONArray itemActions() {
        return new JSONArray();
    }

    /**
     * crud->headerToolbar
     */
    public static JSONArray headerToolbar(String aliasTableName,List<Map<String, Object>> fieldDetails) {
        JSONArray headerToolbar = new JSONArray();
        headerToolbar.add(operationAdd(aliasTableName,fieldDetails));
        JSONObject type = new JSONObject();
        type.put("type","bulk-actions");
        headerToolbar.add(type);

        JSONObject tpl = new JSONObject();
        tpl.put("tpl","内容");
        tpl.put("type","columns-toggler");
        tpl.put("align","right");
        tpl.put("wrapperComponent","");

        headerToolbar.add(tpl);

        return headerToolbar;
    }

    public static JSONArray footerToolbar() {
        JSONArray headerToolbar = new JSONArray();

        JSONObject statistics = new JSONObject();
        statistics.put("type","statistics");
        headerToolbar.add(statistics);

        JSONObject pagination = new JSONObject();
        pagination.put("type","pagination");
        headerToolbar.add(pagination);

        JSONObject tpl = new JSONObject();
        tpl.put("tpl","内容");
        tpl.put("type","switch-per-page");
        tpl.put("wrapperComponent","");

        headerToolbar.add(tpl);

        return headerToolbar;
    }


    /**
     * 新增
     * @param aliasTableName
     * @return
     */
    public static JSONObject operationAdd(String aliasTableName,List<Map<String, Object>> fieldDetails) {

        JSONObject add = new JSONObject();
        add.put("id", "u:" + IdUtil.simpleUUID());
        add.put("type", "button");
        add.put("label", "新增");
        add.put("level", "primary");
        add.put("hiddenOn", "${!ARRAYINCLUDES(permsCode,'"+aliasTableName+":add')}");
        add.put("editorState", "default");

        JSONObject onEvent = new JSONObject();
        JSONObject click = new JSONObject();
        click.put("weight",0);
        JSONArray actions = new JSONArray();

        JSONObject action = new JSONObject();

        JSONObject drawer = new JSONObject();
        drawer.put("id", "u:" + IdUtil.simpleUUID());
        drawer.put("type", "drawer");
        drawer.put("title", "新增");
        drawer.put("resizable", false);

        JSONObject body = new JSONObject();
        body.put("id", "u:" + IdUtil.simpleUUID());
        JSONObject api = new JSONObject();
        api.put("url", "/api/system/Online/Api/post");
        api.put("method", "post");
        api.put("adaptor", "");
        api.put("requestAdaptor", addRequestAdaptor(aliasTableName));


        body.put("api", api);
        body.put("type", "form");

        JSONArray form = new JSONArray();
        for (Map<String, Object> fieldDetail : fieldDetails) {
            // 忽略字段
            if ("del_flag".equals(fieldDetail.get("Field").toString())
                    || "id".equals(fieldDetail.get("Field").toString())
                    || "create_time".equals(fieldDetail.get("Field").toString())
                    || "create_by".equals(fieldDetail.get("Field").toString())
                    || "update_time".equals(fieldDetail.get("Field").toString())
                    || "update_by".equals(fieldDetail.get("Field").toString())
            ) {
                continue;
            }
            JSONObject column = new JSONObject();
            column.put("id", "u:" + IdUtil.simpleUUID());
            column.put("type", "input-text");
            column.put("editorPath", "input.base.default");
            column.put("editorPath", "input.base.default");
            column.put("editorState", "default");
            column.put("name", fieldDetail.get("Field"));
            column.put("label", fieldDetail.get("Comment"));
            form.add(column);
        }

        body.put("body", form);

        drawer.put("body",new JSONArray(body));

        action.put("drawer",drawer);
        action.put("actionType","drawer");

        actions.add(action);
        click.put("actions",actions);
        onEvent.put("click",click);
        add.put("onEvent", onEvent);

        return add;
    }

    /**
     * crud->perPageAvailable
     */
    public static JSONArray perPageAvailable() {
        return new JSONArray(10,20,30,40,50,60,70,80,90,100);
    }

    /**
     * crud->filter
     */
    public static JSONObject filter(List<Map<String, Object>> fieldDetails) {
        JSONArray body = new JSONArray();
        for (Map<String, Object> fieldDetail : fieldDetails) {
            // 忽略字段
            if ("del_flag".equals(fieldDetail.get("Field").toString()) || "id".equals(fieldDetail.get("Field").toString())) {
                continue;
            }
            JSONObject column = new JSONObject();
            column.put("id", "u:" + IdUtil.simpleUUID());
            column.put("type", "input-text");
            column.put("editorPath", "input.base.default");
            column.put("editorPath", "input.base.default");
            column.put("editorState", "default");
            // 取数据库字段备注
            if (fieldDetail.get("Comment") != null) {
                column.put("label", fieldDetail.get("Comment"));
            } else {
                column.put("label", fieldDetail.get("Field"));
            }
            body.add(column);
        }
        JSONObject filter = new JSONObject();
        filter.put("id", "u:" + IdUtil.simpleUUID());
        filter.put("mode", "horizontal");
        filter.put("columnCount", 3);
        filter.put("body", body);

        return filter;
    }

    /**
     * crud->filterEnabledList
     * 格式
     * [{
     * "label":"realName",
     * "value":"realName"
     * }]
     */
    public static JSONArray filterEnabledList(List<Map<String, Object>> fieldDetails) {
        JSONArray filterEnabledList = new JSONArray();

        for (Map<String, Object> fieldDetail : fieldDetails) {
            // 忽略字段
            if ("del_flag".equals(fieldDetail.get("Field").toString()) || "id".equals(fieldDetail.get("Field").toString())) {
                continue;
            }
            JSONObject column = new JSONObject();
            column.put("label", fieldDetail.get("Field"));
            column.put("value", fieldDetail.get("Field"));
            filterEnabledList.add(column);
        }
        return filterEnabledList;
    }

    /**
     * crud->filterSettingSource
     */
    public static JSONArray filterSettingSource(List<Map<String, Object>> fieldDetails) {
        JSONArray filterSettingSource = new JSONArray();
        for (Map<String, Object> fieldDetail : fieldDetails) {
            // 忽略删除标记字段
            if ("del_flag".equals(fieldDetail.get("Field").toString())) {
                continue;
            }
            filterSettingSource.add(fieldDetail.get("Field"));
        }
        return filterSettingSource;
    }

    /**
     * page 页码
     * crud->pageField
     */
    public static String pageField() {
        return "page";
    }

    /**
     * 每页 数量
     * crud->perPageField
     */
    public static String perPageField() {
        return "count";
    }
}
