<template>
  <div class="user-avatar-dropdown">
    <Dropdown @on-click="handleClick">
      <Badge :dot="!!messageUnreadCount">
        <Avatar :src="avatorImgUrl"/>
      </Badge>
      <Icon :size="18" type="md-arrow-dropdown"></Icon>
      <DropdownMenu slot="list">
        <DropdownItem name="ownSpace">
          个人中心
          <Badge style="margin-left: 10px"></Badge>
        </DropdownItem>
        <DropdownItem name="logout">退出登录</DropdownItem>
      </DropdownMenu>
    </Dropdown>
  </div>
</template>

<script>
  import './user.less'
  import { mapActions } from 'vuex'
  import avatorImgUrl from '@/assets/images/avator.jpg'

  export default {
    name: 'User',
    props: {
      userAvatar: {
        type: String,
        default: ''
      },
      messageUnreadCount: {
        type: Number,
        default: 0
      }
    },
    data () {
      return {
        avatorImgUrl
      }
    },
    methods: {
      ...mapActions([
        'handleLogOut'
      ]),
      logout () {
        this.handleLogOut().then(() => {
          this.$router.push({
            name: 'login'
          })
        })
      },
      ownSpace () {
        this.$router.push({
          name: 'ownSpacePage'
        })
      },
      handleClick (name) {
        switch (name) {
          case 'logout':
            this.logout()
            break
          case 'ownSpace':
            this.ownSpace()
            break
        }
      }
    }
  }
</script>
