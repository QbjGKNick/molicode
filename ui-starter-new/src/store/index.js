import Vue from 'vue'
import Vuex from 'vuex'

import app from './module/app.js'
import user from './module/user'
import dict from './module/dict'
import autoCode from './module/autoCode'
import config from './module/config'
import repo from './module/repo'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    //
  },
  mutations: {
    //
  },
  actions: {
    //
  },
  modules: {
    app,
    user,
    dict,
    autoCode,
    config,
    repo
  }
})
