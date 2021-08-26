import Main from '@/components/main'

/**
 * iview-admin中meta除了原生参数外可配置的参数:
 * meta: {
 *  title: { String|Number|Function }
 *         显示在侧边栏、面包屑和标签栏的文字
 *         使用'{{ 多语言字段 }}'形式结合多语言使用，例子看多语言的路由配置;
 *         可以传入一个回调函数，参数是当前路由对象，例子看动态路由和带参路由
 *  hideInBread: (false) 设为true后此级路由将不会出现在面包屑中，示例看QQ群路由配置
 *  hideInMenu: (false) 设为true后在左侧菜单不会显示该页面选项
 *  notCache: (false) 设为true后页面在切换标签后不会缓存，如果需要缓存，无需设置这个字段，而且需要设置页面组件name属性和路由配置的name一致
 *  access: (null) 可访问该页面的权限数组，当前路由设置的权限会影响子路由
 *  icon: (-) 该页面在左侧菜单、面包屑和标签导航处显示的图标，如果是自定义图标，需要在图标名称前加下划线'_'
 *  beforeCloseName: (-) 设置该字段，则在关闭当前tab页时会去'@/router/before-close.js'里寻找该字段名对应的方法，作为关闭前的钩子函数
 * }
 */

export default [
  {
    path: '/login',
    name: 'login',
    meta: {
      title: 'Login - 登录',
      hideInMenu: true
    },
    component: () => import('@/view/login/login.vue')
  },
  {
    path: '/',
    name: '_home',
    redirect: '/home',
    component: Main,
    meta: {
      hideInMenu: true,
      notCache: true
    },
    children: [
      {
        path: '/home',
        name: 'home',
        meta: {
          hideInMenu: true,
          title: '首页',
          notCache: true,
          icon: 'md-home'
        },
        component: () => import('@/view/single-page/home')
      }
    ]
  },
  {
    path: '/message',
    name: 'message',
    component: Main,
    meta: {
      hideInBread: true,
      hideInMenu: true
    },
    children: [
      {
        path: 'message_page',
        name: 'message_page',
        meta: {
          icon: 'md-notifications',
          title: '消息中心'
        },
        component: () => import('@/view/single-page/message/index.vue')
      }
    ]
  },
  {
    path: '/own-space',
    name: 'ownSpace',
    component: Main,
    meta: {
      hideInBread: true,
      hideInMenu: true
    },
    children: [
      {
        path: 'own-space-page',
        name: 'ownSpacePage',
        meta: {
          icon: 'md-notifications',
          title: '个人中心'
        },
        component: () => import('@/view/own-space/own-space.vue')
      }
    ]
  },
  {
    path: '/auto-code',
    name: 'auto-code',
    meta: {
      icon: 'md-plane',
      title: '自动代码工具'
    },
    component: Main,
    children: [
      {
        path: 'configuration',
        name: 'configuration',
        meta: {
          icon: 'md-list',
          title: '项目配置'
        },
        component: () => import('@/view/auto-code/configuration.vue')
      },
      {
        path: 'autoCode',
        name: 'autoCode',
        meta: {
          icon: 'md-code',
          title: '代码生成'
        },
        component: () => import('@/view/auto-code/autoCode.vue')
      }
    ]
  },
  {
    path: '/project-process',

    name: 'project-process',
    meta: {
      icon: 'md-cloud-download',
      title: '工程处理'
    },
    component: Main,
    children: [
      {
        path: 'replace',
        name: 'replace',
        meta: {
          icon: 'ios-copy',
          title: '全文替换工具'
        },
        component: () => import('@/view/auto-code/replace.vue')
      },
      {
        path: 'smartSegment',
        name: 'smartSegment',
        meta: {
          icon: 'ios-copy',
          title: 'smartSegment'
        },
        component: () => import('@/view/auto-code/smartSegment.vue')
      }
    ]
  },
  {
    path: '/moli-tool',
    name: 'tools',
    meta: {
      icon: 'md-hammer',
      title: '系统工具'
    },
    component: Main,
    children: [
      {
        path: 'userList',
        name: 'userList',
        meta: {
          icon: 'md-person',
          title: '用户管理'
        },
        component: () => import('@/view/user/list.vue')
      },
      {
        path: 'browser',
        name: 'browser',
        meta: {
          icon: 'logo-chrome',
          title: '魔力浏览器'
        },
        component: () => import('@/view/moli-tool/browser.vue')
      }
    ]
  },
  {
    path: '/help',
    name: 'helpCenter',
    meta: {
      icon: 'md-help',
      title: '帮助中心'
    },
    component: Main,
    children: [
      {
        path: 'autoCodeHelp',
        name: 'autoCodeHelp',
        meta: {
          icon: 'md-help',
          title: '自动代码帮助中心'
        },
        component: () => import('@/view/help/molicode/autoCodeHelp.vue')
      },
      {
        path: 'tableModelHelp',
        name: 'tableModelHelp',
        meta: {
          icon: 'md-help',
          title: '表模型帮助中心'
        },
        component: () => import('@/view/help/molicode/tableModelHelp.vue')
      }
    ]
  },
  {
    path: '/error_logger',
    name: 'error_logger',
    meta: {
      hideInBread: true,
      hideInMenu: true
    },
    component: Main,
    children: [
      {
        path: 'error_logger_page',
        name: 'error_logger_page',
        meta: {
          icon: 'ios-bug',
          title: '错误收集'
        },
        component: () => import('@/view/single-page/error-logger.vue')
      }
    ]
  },
  {
    path: '/401',
    name: 'error_401',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/401.vue')
  },
  {
    path: '/500',
    name: 'error_500',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/500.vue')
  },
  {
    path: '*',
    name: 'error_404',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/404.vue')
  }
]
