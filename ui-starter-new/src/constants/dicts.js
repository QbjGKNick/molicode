const dictKinds = {
  STD_STATUS: "dict_std_status",
  PROJECT_TYPE_DICT: "project_type_dict",
  DATABASE_NAME_DICT: "database_name_dict",
  STD_BOOLEAN_DICT: "std_boolean_dict",
  STD_YESNO_DICT: "std_yesno_dict",
  TEMPLATE_TYPE_DICT: "template_type_dict",
  REPLACE_TYPE_DICT: "replace_type_dict",
  DATA_MODEL_TYPE_DICT: "data_model_type_dict",
  RESOURCE_TYPE_DICT: "resource_type_dict",
  STD_JSP_TAG: "std_jsp_tag",
  OUTPUT_TYPE_DICT: "output_type_dict",
  STD_GENDER: "dict_std_gender",
  ROlE_CODE_DICT: "role_code_dict"
};

export default {
  dictKinds,
  dictData: {
    dict_std_status: [
      {
        kind: dictKinds.STD_STATUS,
        itemKey: "1",
        itemName: "正常",
        parentKind: "",
        parentKey: "",
        sortNum: 1,
        cssType: "green"
      },
      {
        kind: dictKinds.STD_STATUS,
        itemKey: "-1",
        itemName: "删除",
        parentKind: "",
        parentKey: "",
        sortNum: 2,
        cssType: "red"
      }
    ],
    dict_std_gender: [
      {
        kind: dictKinds.STD_GENDER,
        itemKey: "1",
        itemName: "男",
        parentKind: "",
        parentKey: "",
        sortNum: 1,
        cssType: "green"
      },
      {
        kind: dictKinds.STD_GENDER,
        itemKey: "2",
        itemName: "女",
        parentKind: "",
        parentKey: "",
        sortNum: 2,
        cssType: "blue"
      }
    ],
    project_type_dict: [
      {
        kind: dictKinds.PROJECT_TYPE_DICT,
        itemKey: "1",
        itemName: "共享",
        parentKind: "",
        parentKey: "",
        sortNum: 1
      },
      {
        kind: dictKinds.PROJECT_TYPE_DICT,
        itemKey: "2",
        itemName: "私有",
        parentKind: "",
        parentKey: "",
        sortNum: 2
      }
    ],

    database_name_dict: [
      {
        kind: dictKinds.DATABASE_NAME_DICT,
        itemKey: "mysql",
        itemName: "MySql",
        parentKind: "",
        parentKey: "",
        sortNum: 1,
        cssType: "blue"
      }
      // {
      //   kind: dictKinds.DATABASE_NAME_DICT,
      //   itemKey: "oracle",
      //   itemName: "Oracle",
      //   parentKind: "",
      //   parentKey: "",
      //   sortNum: 2,
      //   cssType: "green"
      // }
    ],
    std_boolean_dict: [
      {
        kind: dictKinds.STD_BOOLEAN_DICT,
        itemKey: "true",
        itemName: "是",
        parentKind: "",
        parentKey: "",
        sortNum: 1,
        cssType: "blue"
      },
      {
        kind: dictKinds.STD_BOOLEAN_DICT,
        itemKey: "false",
        itemName: "否",
        parentKind: "",
        parentKey: "",
        sortNum: 1,
        cssType: "green"
      }
    ],
    std_yesno_dict: [
      {
        kind: dictKinds.STD_YESNO_DICT,
        itemKey: "1",
        itemName: "是",
        parentKind: "",
        parentKey: "",
        sortNum: 1,
        cssType: "blue"
      },
      {
        kind: dictKinds.STD_YESNO_DICT,
        itemKey: "2",
        itemName: "否",
        parentKind: "",
        parentKey: "",
        sortNum: 1,
        cssType: "green"
      }
    ],
    template_type_dict: [
      // {
      //   kind: dictKinds.TEMPLATE_TYPE_DICT,
      //   itemKey: "local",
      //   itemName: "本地模板",
      //   parentKind: "",
      //   parentKey: "",
      //   sortNum: 1,
      //   cssType: "blue"
      // },
      {
        kind: dictKinds.TEMPLATE_TYPE_DICT,
        itemKey: "git",
        itemName: "git",
        parentKind: "",
        parentKey: "",
        sortNum: 1,
        cssType: "green"
      }
    ],
    replace_type_dict: [
      {
        kind: dictKinds.REPLACE_TYPE_DICT,
        itemKey: "1",
        itemName: "全部替换，路径和内容一起替换",
        parentKind: "",
        parentKey: "",
        sortNum: 1,
        cssType: "blue"
      },
      {
        kind: dictKinds.REPLACE_TYPE_DICT,
        itemKey: "2",
        itemName: "只替换文本",
        parentKind: "",
        parentKey: "",
        sortNum: 1,
        cssType: "green"
      },
      {
        kind: dictKinds.REPLACE_TYPE_DICT,
        itemKey: "3",
        itemName: "只替换目录",
        parentKind: "",
        parentKey: "",
        sortNum: 1,
        cssType: "blue"
      },
      {
        kind: dictKinds.REPLACE_TYPE_DICT,
        itemKey: "4",
        itemName: "复制不替换",
        parentKind: "",
        parentKey: "",
        sortNum: 1,
        cssType: "green"
      }
    ],
    data_model_type_dict: [
      {
        kind: dictKinds.DATA_MODEL_TYPE_DICT,
        itemKey: "tableModel",
        itemName: "tableModel表模型",
        parentKind: "",
        parentKey: "",
        sortNum: 1
      }
      // , {
      //   kind: dictKinds.DATA_MODEL_TYPE_DICT,
      //   itemKey: 'json',
      //   'itemName': 'JSON',
      //   'parentKind': '',
      //   'parentKey': '',
      //   'sortNum': 2
      // },
      // {
      //   kind: dictKinds.DATA_MODEL_TYPE_DICT,
      //   itemKey: 'lineList',
      //   'itemName': '单行文本处理',
      //   'parentKind': '',
      //   'parentKey': '',
      //   'sortNum': 3
      // },
      // {
      //   kind: dictKinds.DATA_MODEL_TYPE_DICT,
      //   itemKey: 'cellList',
      //   'itemName': '单元文本处理',
      //   'parentKind': '',
      //   'parentKey': '',
      //   'sortNum': 4
      // },
      // {
      //   kind: dictKinds.DATA_MODEL_TYPE_DICT,
      //   itemKey: 'javaSource',
      //   'itemName': 'Java源码',
      //   'parentKind': '',
      //   'parentKey': '',
      //   'sortNum': 5
      // },
      // {
      //   kind: dictKinds.DATA_MODEL_TYPE_DICT,
      //   itemKey: 'rawContent',
      //   'itemName': '原始内容',
      //   'parentKind': '',
      //   'parentKey': '',
      //   'sortNum': 6
      // }
    ],
    resource_type_dict: [
      {
        kind: dictKinds.RESOURCE_TYPE_DICT,
        itemKey: "database",
        itemName: "数据库",
        parentKind: "",
        parentKey: "",
        sortNum: 1
      }
      // {
      //   kind: dictKinds.RESOURCE_TYPE_DICT,
      //   itemKey: "file",
      //   itemName: "文件",
      //   parentKind: "",
      //   parentKey: "",
      //   sortNum: 2
      // },
      // {
      //   kind: dictKinds.RESOURCE_TYPE_DICT,
      //   itemKey: "front",
      //   itemName: "前台输入",
      //   parentKind: "",
      //   parentKey: "",
      //   sortNum: 3
      // }
    ],
    std_jsp_tag: [
      {
        kind: dictKinds.STD_JSP_TAG,
        itemKey: "TEXT",
        itemName: "单行文本",
        parentKind: "",
        parentKey: "",
        sortNum: 1
      },
      {
        kind: dictKinds.STD_JSP_TAG,
        itemKey: "TEXTAREA",
        itemName: "多行文本",
        parentKind: "",
        parentKey: "",
        sortNum: 2
      },
      {
        kind: dictKinds.STD_JSP_TAG,
        itemKey: "SELECT",
        itemName: "下拉框",
        parentKind: "",
        parentKey: "",
        sortNum: 3
      },
      {
        kind: dictKinds.STD_JSP_TAG,
        itemKey: "RADIO",
        itemName: "单选项",
        parentKind: "",
        parentKey: "",
        sortNum: 4
      },
      {
        kind: dictKinds.STD_JSP_TAG,
        itemKey: "CHECKBOX",
        itemName: "多选项目",
        parentKind: "",
        parentKey: "",
        sortNum: 5
      },
      {
        kind: dictKinds.STD_JSP_TAG,
        itemKey: "HIDDEN",
        itemName: "隐藏域",
        parentKind: "",
        parentKey: "",
        sortNum: 6
      },
      {
        kind: dictKinds.STD_JSP_TAG,
        itemKey: "EDITOR",
        itemName: "富文本编辑器",
        parentKind: "",
        parentKey: "",
        sortNum: 7
      },
      {
        kind: dictKinds.STD_JSP_TAG,
        itemKey: "DATETIME",
        itemName: "日期时间",
        parentKind: "",
        parentKey: "",
        sortNum: 8
      },
      {
        kind: dictKinds.STD_JSP_TAG,
        itemKey: "DATE",
        itemName: "日期控件",
        parentKind: "",
        parentKey: "",
        sortNum: 9
      }
    ],
    [dictKinds.OUTPUT_TYPE_DICT]: [
      // {
      //   kind: dictKinds.OUTPUT_TYPE_DICT,
      //   itemKey: "1",
      //   itemName: "本地工程目录",
      //   parentKind: "",
      //   parentKey: "",
      //   sortNum: 1,
      //   cssType: "blue"
      // },
      {
        kind: dictKinds.OUTPUT_TYPE_DICT,
        itemKey: "2",
        itemName: "zip压缩包",
        parentKind: "",
        parentKey: "",
        sortNum: 2,
        cssType: "green"
      },
      {
        kind: dictKinds.OUTPUT_TYPE_DICT,
        itemKey: "3",
        itemName: "输出到前台",
        parentKind: "",
        parentKey: "",
        sortNum: 3,
        cssType: "green"
      }
    ],
    [dictKinds.ROlE_CODE_DICT]: [
      {
        kind: dictKinds.ROlE_CODE_DICT,
        itemKey: "sys_admin",
        itemName: "sys_admin",
        parentKind: "",
        parentKey: "",
        sortNum: 1,
        cssType: "blue"
      },
      {
        kind: dictKinds.ROlE_CODE_DICT,
        itemKey: "normal_user",
        itemName: "normal_user",
        parentKind: "",
        parentKey: "",
        sortNum: 2,
        cssType: "green"
      },
      {
        kind: dictKinds.ROlE_CODE_DICT,
        itemKey: "guest_user",
        itemName: "guest_user",
        parentKind: "",
        parentKey: "",
        sortNum: 3,
        cssType: "green"
      }
    ]
  }
};
