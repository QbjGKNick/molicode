<template>
  <Modal v-model="showModal"
         :title="title"
         ok-text="> 执行代码生成"
         :mask-closable="false"
         @on-cancel="cancel"
         width="94%">


    <Form :label-width="120" inline>
      <Row>
        <Col span="8">
          <Form-item label="className(类名)" prop="id" style="width: 90%">
            <Input v-model="tableDefine.id"></Input>
          </Form-item>
        </Col>

        <Col span="8">
          <Form-item label="中文名称" prop="cnname" style="width: 90%">
            <Input v-model="tableDefine.cnname"></Input>
          </Form-item>
        </Col>
        <Col span="8">
          <Form-item label="表自定义扩展" prop="customProps" style="width: 90%">
            <custom-props :customPropsObject="tableModel['customProps']"
                          @on-change="tableCustomPropsChange" v-if="showTableCusProps"></custom-props>
          </Form-item>
        </Col>
      </Row>

    </Form>

    <Table :columns="columns" :data="tableDefine.columns" size="small" :border="true"></Table>

    <div slot="footer">
      <Button type="default" :loading="loading" @click="cancel">
        取消
      </Button>

      <Button type="info" :loading="loading" @click="saveModelAndGenCode(false)">
        <Icon type="md-checkmark"></Icon>
        保存表模型
      </Button>
      <Button type="primary" :loading="loading" @click="saveModelAndGenCode(true)">
        <Icon type="md-play"></Icon>
        执行代码生成
      </Button>
    </div>
  </Modal>
</template>

<script>
  import constants from '@/constants/constants'
  import requestUtils from '@/request/requestUtils.js'
  import dictSelect from '@/view/components/dict/DictSelect'
  import customProps from './customProps'

  var _ = require('underscore')

  function buildInDataKey (groupKey) {
    return 'in_' + groupKey;
  }

  function buildComponentColumns (tableModel, customFieldGroupList, _this) {
    let tableDefine = tableModel.tableDefine;
    let componentColumns = [
      {
        title: '列名',
        key: 'columnName',
        width: 120
      },
      {
        title: '注释',
        key: 'comment',
        width: 150
      },
      {
        title: '类型',
        key: 'columnType',
        width: 100
      },
      {
        title: '最大长度',
        key: 'length',
        width: 80
      },
      {
        title: '是否主键',
        key: 'isPK',
        width: 80,
        render: (h, params) => {
          return h('i-switch', {
            props: {
              value: params.row['isPK'],
              size: 'small'
            },
            on: {
              'on-change': (val) => {
                tableDefine.columns[params.index]['isPK'] = val;
              }
            }
          });
        }
      },
      {
        title: '显示名称',
        key: 'cnname',
        width: 170,
        render: (h, params) => {
          return h('i-input', {
            props: {
              value: params.row['cnname'],
              size: 'small'
            },
            on: {
              'on-blur': (event) => {
                tableDefine.columns[params.index]['cnname'] = event.target.value;
              }
            }
          });
        }
      },
      {
        title: '控件',
        key: 'jspTag',
        width: 160,
        render: (h, params) => {
          return h(dictSelect, {
            props: {
              value: params.row['jspTag'],
              kind: constants.dicts.dictKinds.STD_JSP_TAG,
              size: 'small'
            },
            on: {
              'on-change': (val) => {
                tableDefine.columns[params.index]['jspTag'] = val;
              }
            }
          });
        }
      },
      {
        title: '必填',
        key: 'canBeNull',
        width: 55,
        render: (h, params) => {
          return h('i-switch', {
            props: {
              value: params.row['canBeNull'] === false,
              size: 'small'
            },
            on: {
              'on-change': (val) => {
                tableDefine.columns[params.index]['canBeNull'] = !val;
              }
            }
          });
        }
      }
    ];

    if (customFieldGroupList && customFieldGroupList.length > 0) {
      _.each(customFieldGroupList, function (customFieldGroup) {
        if (!customFieldGroup['configShow']) {
          return;
        }
        let inDataKey = buildInDataKey(customFieldGroup['groupKey']);
        componentColumns.push({
          title: customFieldGroup.desc,
          key: inDataKey,
          width: 80,
          render: (h, params) => {
            return h('i-switch', {
              props: {
                value: params.row[inDataKey],
                size: 'small'
              },
              on: {
                'on-change': (val) => {
                  tableDefine.columns[params.index][inDataKey] = val;
                }
              }
            });
          }
        });
      });
    }
    componentColumns.push({
      title: 'customProps',
      width: 120,
      render: (h, params) => {
        return h(customProps, {
          props: {
            customPropsObject: params.row['customProps'],
            size: 'small'
          },
          on: {
            'on-change': (val) => {
              tableDefine.columns[params.index]['customProps'] = val;
            }
          }
        });
      }
    });
    return componentColumns;
  }

  export default {
    props: {
      item: {
        type: Object,
        required: true
      }
    },
    data () {
      return {
        loading: false,
        loaded: false,
        showModal: false,
        showTableCusProps: false,
        title: '编辑并生成代码',
        tableModel: {},
        customFieldGroupList: [],
        tableDefine: {
          columns: []
        },
        columns: []
      };
    },
    methods: {
      cancel () {
        this.showModal = false;
      },
      'show': function (isShow) {
        this.showModal = isShow;
        if (isShow && !this.loaded) {
          this.fetchTableModel();
          this.showTableCusProps = true;
        }
      },
      fetchTableModel () {
        var projectKey = this.$store.state.autoCode.defaultProjectKey;
        if (!projectKey) {
          this.$Message.error({
            content: '默认项目尚未配置',
            duration: 5
          });
          return;
        }
        let param = {
          projectKey,
          tableName: this.item.tableName,
          'tableSourceName': this.item['sourceName']
        };
        requestUtils.postSubmit(this, constants.urls.autoCode.tableModel.getTableInfo, param, function (data) {
          processData(data['value'], data['customFieldGroupList']);
          this.tableModel = data['value'];
          this.customFieldGroupList = data['customFieldGroupList'];
          this.columns = buildComponentColumns(this.tableModel, this.customFieldGroupList);
          this.tableDefine = this.tableModel.tableDefine;
          this.title = '编辑生成代码，表（' + this.tableModel.tableDefine.dbTableName + ')';
        }, null, true);
      },
      saveModelAndGenCode: function (genCode) {
        processBeforeSave(this.tableModel, this.customFieldGroupList);
        let tableModelJson = JSON.stringify(this.tableModel);
        var projectKey = this.$store.state.autoCode.defaultProjectKey;
        var param = {
          projectKey,
          tableModelJson,
          tableName: this.tableDefine.dbTableName
        };
        requestUtils.postSubmit(this, constants.urls.autoCode.tableModel.saveTableModel, param, function (data) {
          if (genCode) {
            this.$emit(constants.actions.autoCode.genCode, data['value']);
            this.show(false);
          } else {
            this.$Message.success({
              content: '保存表模型设计成功',
              duration: 5
            });
            this.$emit(constants.actions.autoCode.tableModelNotify, this.tableModel);
          }
        }, null, true);
      },
      tableCustomPropsChange: function (val) {
        this.tableModel['customProps'] = val;
      }
    },
    components: {
      dictSelect,
      customProps
    }
  };

  function processData (tableModel, customFieldGroupList) {
    let tableDefine = tableModel.tableDefine;
    let bizFieldsMap = tableModel['bizFieldsMap'];
    if (bizFieldsMap === null || bizFieldsMap === undefined) {
      return;
    }
    _.mapObject(bizFieldsMap, function (bizFields, key) {
      let fieldsList = bizFields.split(',');
      let inKey = 'in_' + key;
      _.each(tableDefine.columns, function (column) {
        column[inKey] = _.contains(fieldsList, column['columnName']);
      });
      return '';
    });
  }

  function processBeforeSave (tableModel, customFieldGroupList) {
    let tableDefine = tableModel.tableDefine;
    _.each(customFieldGroupList, function (customFieldGroup) {
      let groupKey = customFieldGroup['groupKey'];
      let inKey = buildInDataKey(groupKey);
      let bizKeyList = [];
      _.each(tableDefine.columns, function (column) {
        if (column[inKey]) {
          bizKeyList.push(column['columnName']);
        }
      });
      if (bizKeyList.length > 0) {
        tableModel['bizFieldsMap'][groupKey] = bizKeyList.join(',');
      }
    });
  }
</script>
