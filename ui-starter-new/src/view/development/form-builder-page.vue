<template>
  <div>

    <Button type="primary" @click="getFormData">获取数据</Button>

    <form-builder ref="formBuilder"
                  v-bind:initFormData="initFormData"
                  v-bind:formGroupDefine="formGroupDefine"></form-builder>
  </div>
</template>

<script>
  import FormBuilder from '_c/form-builder/form-builder'

  export default {
    components: { FormBuilder },
    data: function () {
      let formGroupDefine = {
        'formGroupList': [
          {
            'groupName': '配置组1',
            'groupKey': 'group1',
            groupItemList: [
              {
                dictKey: 'testKey1',
                dictName: '测试key1',
                dictType: 'input'
              },
              {
                dictKey: 'testKey1_1',
                dictName: '测试多行输入框',
                dictType: 'textarea'
              }
            ]
          },
          {
            'groupName': '公司信息配置',
            'groupKey': 'group2',
            groupItemList: [
              {
                dictKey: 'testKey2',
                dictName: '测试key2',
                dictType: 'radio',
                dictKind: 'dict_std_status'
              },
              {
                itemKey: 'testKey3',
                itemName: '测试key3',
                itemType: 'select',
                dictKind: 'dict_std_status'
              },
              {
                itemKey: 'testKey4',
                itemName: '测试key4',
                itemType: 'checkbox',
                dictKind: 'dict_std_status',
                localDictItems: [{
                  kind: 'testDict',
                  itemKey: '1',
                  'itemName': '测试字典1',
                  'parentKind': '',
                  'parentKey': '',
                  'sortNum': 1,
                  'cssType': 'green'
                }, {
                  kind: 'testDict',
                  itemKey: '2',
                  'itemName': '测试字典2',
                  'parentKind': '',
                  'parentKey': '',
                  'sortNum': 2,
                  'cssType': 'red'
                }]
              },
              {
                itemKey: 'testKey5',
                itemName: '测试key45',
                itemType: 'checkbox',
                dictKind: 'dict_std_status'
              }
            ]
          }
        ]
      }
      return {
        formGroupDefine,
        initFormData: {
          'testKey1': '测试纸1',
          'testKey2': '1',
          'testKey3': '1',
          'testKey4': ['1', '2']
        }
      }
    },
    methods: {
      getFormData: function () {
        let flatFormData = this.$refs.formBuilder.getFlatFormData()
        console.log(flatFormData)
        alert('请查看日志窗口')
      }
    }
  }
</script>
