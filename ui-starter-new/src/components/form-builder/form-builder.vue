<template>
  <div>
    <div v-for="formGroup in formGroupDefine.formGroupList">
      <Card style="width:90%">
        <p slot="title">
          <Icon type="ios-film-outline"></Icon>
          {{formGroup.groupName}}
        </p>

        <Form :model="groupFormData[formGroup.groupKey]" :label-width="80">
          <div v-for="groupItem in formGroup.groupItemList">
            <FormItem :label="groupItem['itemName']">

              <div v-if="groupItem.itemType=='input'">
                <Input v-model="groupFormData[formGroup.groupKey][groupItem.itemKey]"
                       placeholder="Enter something..."></Input>
              </div>
              <div v-else-if="groupItem.itemType=='textarea'">
                <Input type="textarea" v-model="groupFormData[formGroup.groupKey][groupItem.itemKey]"
                       placeholder="Enter something..."></Input>
              </div>

              <div v-else-if="groupItem.itemType=='radio'">
                <DictRadio v-model="groupFormData[formGroup.groupKey][groupItem.itemKey]" :kind="groupItem.dictKind"
                           :localDictItems="groupItem.localDictItems"
                ></DictRadio>
              </div>

              <div v-else-if="groupItem.itemType=='select'">
                <DictSelect v-model="groupFormData[formGroup.groupKey][groupItem.itemKey]" :kind="groupItem.dictKind"
                            :localDictItems="groupItem.localDictItems"
                ></DictSelect>
              </div>

              <div v-else-if="groupItem.itemType=='checkbox'">
                <DictCheckBox v-model="groupFormData[formGroup.groupKey][groupItem.itemKey]" :kind="groupItem.dictKind"
                              :localDictItems="groupItem.localDictItems"
                ></DictCheckBox>
              </div>
            </FormItem>
          </div>
        </Form>

      </Card>
    </div>
  </div>
</template>

<script>
  import DictRadio from '../dict/DictRadio'
  import DictCheckBox from '../dict/DictCheckBox'
  import DictSelect from '../dict/DictSelect'

  var _ = require('underscore')

  export default {
    name: 'form-builder',
    props: {
      formGroupDefine: {
        type: Object,
        required: true
      },
      initFormData: {
        type: Object,
        default: function () {
          return {}
        }
      }
    },
    data: function () {
      let groupFormData = {}
      let initData = this.initFormData === null || this.initFormData === undefined ? {} : this.initFormData
      _.each(this.formGroupDefine.formGroupList, function (formGroup) {
        let formData = {}
        groupFormData[formGroup.groupKey] = formData
        _.each(formGroup.groupItemList, function (groupItem) {
          let initDataValue = initData[groupItem.itemKey]
          if (initDataValue !== null && initDataValue !== undefined) {
            formData[groupItem.itemKey] = initDataValue
          } else {
            if (groupItem.itemType == 'checkbox') {
              formData[groupItem.itemKey] = []
            } else {
              formData[groupItem.itemKey] = null
            }
          }
        })
      })

      return {
        groupFormData
      }
    },
    methods: {
      /**
       * 通过groupKey 获取表单数据
       * @param groupKey
       * @returns {*}
       */
      getFormDataByGroupKey: function (groupKey) {
        return this.groupFormData[groupKey]
      },
      /**
       * 获取按groupKey分组的所有数据
       * @returns {{}}
       */
      getGroupFormData: function () {
        return this.groupFormData
      },
      /**
       * 拉平的表单数据
       */
      getFlatFormData: function () {
        let flatFormData = {}
        let groupFormData = this.groupFormData
        _.each(_.keys(groupFormData), function (groupKey) {
          let formGroup = groupFormData[groupKey]
          _.each(_.keys(formGroup), function (itemKey) {
            flatFormData[itemKey] = formGroup[itemKey]
          })
        })
        return flatFormData
      }
    },
    components: {
      DictSelect,
      DictCheckBox,
      DictRadio
    }
  }
</script>

<style scoped>

</style>
