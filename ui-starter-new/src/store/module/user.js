import {
  getContentByMsgId,
  getMessage,
  getUnreadCount,
  getUserInfo,
  hasRead,
  login,
  logout,
  removeReaded,
  restoreTrash
} from '@/api/user'
import { getToken, setToken } from '@/libs/util'
import constants from '@/constants/constants'
import requestUtils from '@/request/requestUtils.js'
import Cookies from 'js-cookie'

export default {
  state: {
    userInfo: null,
    login: false,

    userName: '',
    userId: '',
    avatarImgPath: '',
    token: getToken(),
    access: '',
    hasGetInfo: false,
    unreadCount: 0,
    messageUnreadList: [],
    messageReadedList: [],
    messageTrashList: [],
    messageContentStore: {}
  },
  mutations: {
    logout (state) {
      state.login = false
      state.userInfo = null
      state.hasGetInfo = false
      state.userName = ''
      state.userId = ''
    },
    [constants.types.SET_LOGIN_USER]: function (state, payload) {
      state.login = true
      let userInfo = payload['userInfo']
      state.userInfo = userInfo
      state.token = getToken()
    },
    setAvatar (state, avatarPath) {
      state.avatarImgPath = avatarPath
    },
    setUserId (state, id) {
      state.userId = id
    },
    setUserName (state, name) {
      state.userName = name
    },
    setAccess (state, access) {
      state.access = access
    },
    setToken (state, token) {
      state.token = token
      setToken(token)
    },
    setHasGetInfo (state, status) {
      state.hasGetInfo = status
    },
    setMessageCount (state, count) {
      state.unreadCount = count
    },
    setMessageUnreadList (state, list) {
      state.messageUnreadList = list
    },
    setMessageReadedList (state, list) {
      state.messageReadedList = list
    },
    setMessageTrashList (state, list) {
      state.messageTrashList = list
    },
    updateMessageContentStore (state, { msg_id, content }) {
      state.messageContentStore[msg_id] = content
    },
    moveMsg (state, { from, to, msg_id }) {
      const index = state[from].findIndex(_ => _.msg_id === msg_id)
      const msgItem = state[from].splice(index, 1)[0]
      msgItem.loading = false
      state[to].unshift(msgItem)
    }
  },
  getters: {
    messageUnreadCount: state => state.messageUnreadList.length,
    messageReadedCount: state => state.messageReadedList.length,
    messageTrashCount: state => state.messageTrashList.length
  },
  actions: {
    // ??????
    handleLogin ({ commit }, { userName, password }) {
      userName = userName.trim()
      return new Promise((resolve, reject) => {
        login({
          userName,
          password
        }).then(res => {
          const data = res.data
          commit('setToken', data.token)
          resolve()
        }).catch(err => {
          reject(err)
        })
      })
    },
    // ????????????
    handleLogOut ({ state, commit }) {
      return new Promise((resolve, reject) => {
        requestUtils.postSubmit(this, constants.urls.sys.acUser.logout, {}, function (data) {
          commit('setToken', '')
          commit('setAccess', [])
          commit('logout')
          resolve()
        }, function () {
          reject()
        })
      })
    },
    // ????????????????????????
    getUserInfo ({ state, commit }, payload) {
      var searchParam = { 't': new Date().getTime() }
      var _this = payload && payload['_vue'] ? payload['_vue'] : this
      return new Promise((resolve, reject) => {
        if (state.login) {
          resolve(state.userInfo)
          return
        }
        requestUtils.postSubmit(_this, constants.urls.sys.acUser.getLoginUser, searchParam, function (data) {
          if (data.value) {
            let userInfo = data.value
            commit(constants.types.SET_LOGIN_USER, { userInfo: userInfo, _vue: _this })
            commit('setAvatar', '')
            commit('setUserName', userInfo.nickName)
            commit('setUserId', data.userName)
            commit('setAccess', [])
            commit('setHasGetInfo', true)
            resolve(data.value)
          } else {
            resolve(null)
          }
        }, function (data) {
          reject(new Error('Could not get login User'))
        }, false)
      })
    },
    // ???????????????????????????????????????????????????????????????????????????????????????
    getUnreadMessageCount ({ state, commit }) {
      getUnreadCount().then(res => {
        const { data } = res
        commit('setMessageCount', data)
      })
    },
    // ????????????????????????????????????????????????????????????????????????
    getMessageList ({ state, commit }) {
      return new Promise((resolve, reject) => {
        getMessage().then(res => {
          const { unread, readed, trash } = res.data
          commit('setMessageUnreadList', unread.sort((a, b) => new Date(b.create_time) - new Date(a.create_time)))
          commit('setMessageReadedList', readed.map(_ => {
            _.loading = false
            return _
          }).sort((a, b) => new Date(b.create_time) - new Date(a.create_time)))
          commit('setMessageTrashList', trash.map(_ => {
            _.loading = false
            return _
          }).sort((a, b) => new Date(b.create_time) - new Date(a.create_time)))
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // ??????????????????????????????id????????????
    getContentByMsgId ({ state, commit }, { msg_id }) {
      return new Promise((resolve, reject) => {
        let contentItem = state.messageContentStore[msg_id]
        if (contentItem) {
          resolve(contentItem)
        } else {
          getContentByMsgId(msg_id).then(res => {
            const content = res.data
            commit('updateMessageContentStore', { msg_id, content })
            resolve(content)
          })
        }
      })
    },
    // ????????????????????????????????????
    hasRead ({ state, commit }, { msg_id }) {
      return new Promise((resolve, reject) => {
        hasRead(msg_id).then(() => {
          commit('moveMsg', {
            from: 'messageUnreadList',
            to: 'messageReadedList',
            msg_id
          })
          commit('setMessageCount', state.unreadCount - 1)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // ????????????????????????????????????
    removeReaded ({ commit }, { msg_id }) {
      return new Promise((resolve, reject) => {
        removeReaded(msg_id).then(() => {
          commit('moveMsg', {
            from: 'messageReadedList',
            to: 'messageTrashList',
            msg_id
          })
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // ??????????????????????????????????????????
    restoreTrash ({ commit }, { msg_id }) {
      return new Promise((resolve, reject) => {
        restoreTrash(msg_id).then(() => {
          commit('moveMsg', {
            from: 'messageTrashList',
            to: 'messageReadedList',
            msg_id
          })
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    [constants.types.LOAD_LOGIN_USER]: ({ state, commit, dispatch }, payload) => {
      var searchParam = { 't': new Date().getTime() }
      var _this = payload['_vue'] ? payload['_vue'] : this
      return new Promise((resolve, reject) => {
        if (state.login) {
          resolve(state.userInfo)
          return
        }
        requestUtils.postSubmit(_this, constants.urls.sys.acUser.getLoginUser, searchParam, function (data) {
          if (data.value) {
            commit(constants.types.SET_LOGIN_USER, { userInfo: data.value, _vue: _this })
            state.userInfo = data.value
            resolve(data.value)
          } else {
            resolve(null)
          }
        }, function (data) {
          reject(new Error('Could not get login User'))
        }, false)
      })
    },
    [constants.types.CHANGE_PASSWORD]: ({ state, commit, dispatch }, payload) => {
      var _this = payload['_vue'] ? payload['_vue'] : this
      var loadingKey = payload['loadingKey']
      return new Promise((resolve, reject) => {
        requestUtils.postSubmit(_this, constants.urls.sys.acUser.changePassword, payload['data'], function (data) {
          resolve(data)
        }, null, loadingKey)
      })
    }
  }
}
